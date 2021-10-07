package hanium.videoMeeting.service;

import hanium.videoMeeting.DTO.ReserveMailDto;
import hanium.videoMeeting.DTO.RoomDto;
import hanium.videoMeeting.DTO.RoomReadDto;
import hanium.videoMeeting.DTO.RoomReserveDto;
import hanium.videoMeeting.advice.exception.*;
import hanium.videoMeeting.domain.Join_Room;
import hanium.videoMeeting.domain.Room;
import hanium.videoMeeting.domain.User;
import hanium.videoMeeting.repository.JoinRoomRepository;
import hanium.videoMeeting.repository.RoomRepository;
import hanium.videoMeeting.repository.UserRepository;
import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final JoinRoomRepository joinRoomRepository;
    private final OpenVidu openVidu;
    private final MailService mailService;

    @Transactional
    public String create(RoomDto roomDto, Long userId) {

        User host = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);

        // 일치하는 방제가 있는지 확인
        Room sameTitleRoom = roomRepository.findByTitle(roomDto.getTitle()).orElse(null);

        if (sameTitleRoom != null) {
            // - 예약된 방인지 확인
            if (sameTitleRoom.getIsReserved() != null) {
                throw new ExistedRoomTitleException();
            }
            checkSameTitleRoomSessionDone(sameTitleRoom);
        }

        // host와 title, password를 입력하여 방 생성
        Room room = new Room(host, roomDto.getTitle(), roomDto.getPassword());

        // 세션 생성
        makeSession(room);

        return room.getSession();
    }

    public void makeSession(Room room) {
        try {
            Session session = openVidu.createSession();
            room.connectSession(session.getSessionId());

            roomRepository.save(room);

        } catch (OpenViduException e) {
            log.warn("오픈비두 할당 오류가 발생했습니다.");
            e.printStackTrace();

            throw new OpenViduServerException();
        } catch (NullPointerException ne) {
            log.warn("오픈비두에서 세션을 생성하지 못했습니다.");
            ne.printStackTrace();

            throw new OpenViduServerException();
        }
    }

    @Transactional
    public String join(RoomDto roomDto, Long userId) {
        Room room = roomRepository.findByTitle(roomDto.getTitle()).orElseThrow(NoSuchRoomException::new);
        User user = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);

        List<Join_Room> joinRooms = room.getJoinRooms();
        List<Join_Room> isJoined = joinRooms.stream().filter(joinRoom -> Objects.equals(joinRoom.getUser().getId(), userId)).collect(Collectors.toList());
        if (!isJoined.isEmpty()) {
            throw new JoinDuplException();
        }

        String token = null;

        // 예약방 세션 할당

        if (room.getSession() == null) {
            if (room.getIsReserved()) {
                //예약시간과 현재시간을 비교
                if (room.getStart_time().isBefore(LocalDateTime.now())) {
                    // 세션 생성
                    makeSession(room);
                    room.reserveDone(); // 일반 방처럼 isReserved 제거
                    log.info("예약한 방의 세션이 할당됐습니다.");
                } else {
                    log.warn("예약시간이 아직 되지 않았습니다.");
                    throw new ReservationTimeMisMatchException();
                }
            } else {
                //예약한 방이 아니면 오류
                throw new NoRoomSessionException();
            }
        }

        ConnectionProperties connectionProperties = new ConnectionProperties.Builder()
                .type(ConnectionType.WEBRTC)
                .role(OpenViduRole.PUBLISHER)
                .data("userData")
                .build();

        List<Session> activeSessions = openVidu.getActiveSessions();
        Session session = activeSessions.stream()
                .filter(s -> s.getSessionId().equals(room.getSession()))
                .findFirst()
                .orElse(null);

        // 세션이 만료된 방이면 방 정보를 삭제함.
        if (session == null) {
            roomRepository.delete(room);
        } else {

            try {
                token = session.createConnection(connectionProperties).getToken();

                log.info("[Room : {}] {} 세션에서 토큰 발행 : {}", room.getTitle(), room.getSession(), token);

                Join_Room joinRoom = new Join_Room(user, room, token);
                joinRoomRepository.save(joinRoom);

            } catch (OpenViduJavaClientException | OpenViduHttpException e) {
                e.printStackTrace();

                throw new OpenViduServerException();
            }

        }

        return token;

    }

    @Transactional
    public void delete(Room room) {
        // CASCADE 설정이 되어있으므로 room을 삭제하면 이와 연관된 Join_Room도 삭제됨

        List<Session> activeSessions = openVidu.getActiveSessions();
        Session openViduSession = activeSessions.stream()
                .filter(s -> s.getSessionId().equals(room.getSession()))
                .findFirst()
                .orElse(null);

        try {
            if (openViduSession != null) {
                openViduSession.close();
            }
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace();
            throw new OpenViduServerException();
        } finally {
            roomRepository.delete(room);
        }

    }

    public Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(NoSuchRoomException::new);
    }

    public Room findRoomByTitle(String title) {
        return roomRepository.findByTitle(title).orElseThrow(NoSuchRoomException::new);
    }

    public Room findRoomBySession(String session) {
        return roomRepository.findBySession(session).orElseThrow(NoSuchRoomException::new);
    }

    @Transactional
    public String reserve(RoomReserveDto roomReserveDto, Long userId) {

        User host = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);
        Room sameTitleRoom = roomRepository.findByTitle(roomReserveDto.getTitle()).orElse(null);

        if (sameTitleRoom != null) {
            // - 예약된 방인지 확인
            if (sameTitleRoom.getIsReserved() != null) {
                throw new ExistedRoomTitleException();
            }
            checkSameTitleRoomSessionDone(sameTitleRoom);
        }

        if (roomReserveDto.getPassword().equals("")) {
            throw new NoRoomPasswordException();
        }

        // host와 title, password, isReserved, reservationTime을 입력하여 방 생성
        Room room = new Room(host, roomReserveDto.getTitle(), roomReserveDto.getPassword(), roomReserveDto.getReservationTime());
        roomRepository.save(room);

        return room.getTitle();
    }

    public List<RoomReadDto> findRoomByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // 세션이 발급된 방이고 비밀번호가 "" 인 방(공개방)을 찾음
        List<Room> rooms = roomRepository.findByPage(pageable);

        // 세션 만료되었는지 확인
        List<RoomReadDto> roomReadDtoList = rooms.stream()
                .filter(room -> room.getStart_time().plusHours(4).isAfter(LocalDateTime.now()) && room.getStart_time().isBefore(LocalDateTime.now()))
                .map(RoomReadDto::new).collect(Collectors.toList());

        return roomReadDtoList;

    }

    public long countPublicRoom() {
        return roomRepository.countPublicRoom();
    }

    @Transactional
    public boolean exit(Long userId, String session) {
        User nowUser = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);
        Room nowRoom = roomRepository.findBySession(session).orElseThrow(NoSuchRoomException::new);

        Join_Room joinRoom = joinRoomRepository.findByUserWithRoom(nowUser, nowRoom).orElseThrow(NoSuchJoinRoomException::new);

        // 만약 호스트가 나간다면 방이 삭제된다.
        if (Objects.equals(nowRoom.getHost().getId(), nowUser.getId())) {
            delete(nowRoom);
        }

        // 호스트가 나가는 것이 아니라면 그냥 한명이 삭제된다.
        boolean isRemoved = nowRoom.getJoinRooms().remove(joinRoom);
        if (isRemoved) {
            nowRoom.minusJoinPeople();
        }
        return isRemoved;
    }

    // 세션이 현재 존재하는 방인지 확인하고 만료된 방이라면 해당 방을 삭제한다.
    private void checkSameTitleRoomSessionDone(Room sameTitleRoom) {
        // - 아직 세션이 유효한 방인지 확인
        List<Session> activeSessions = openVidu.getActiveSessions();
        Session sameTitleRoomSession = activeSessions.stream()
                .filter(s -> s.getSessionId().equals(sameTitleRoom.getSession()))
                .findFirst()
                .orElse(null);

        // 세션이 만료된 방이면 삭제 후 등록. 세션이 만료되지 않았다면 방 중복 오류
        if (sameTitleRoomSession == null) {
            roomRepository.delete(sameTitleRoom);
        } else {
            throw new ExistedRoomTitleException();
        }
    }

    // 현재 시간에 실행되는 예약방을 찾음
    public List<ReserveMailDto> findReserveRoomByNowTime() {
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTime.of(nowTime.getYear(), nowTime.getMonth(), nowTime.getDayOfMonth(), nowTime.getHour(), nowTime.getMinute(), 0);
        LocalDateTime endTime = LocalDateTime.of(nowTime.getYear(), nowTime.getMonth(), nowTime.getDayOfMonth(), nowTime.getHour(), nowTime.getMinute(), 59);

        List<Room> reservedRooms = roomRepository.findReservedRoomsBetweenTime(startTime, endTime);
        List<ReserveMailDto> reserveMailDtos = null;
        if(!reservedRooms.isEmpty()){
            reserveMailDtos = reservedRooms.stream()
                    .map(room -> ReserveMailDto
                            .builder()
                            .reserveTime(room.getStart_time())
                            .roomName(room.getTitle())
                            .roomHostName(room.getHost().getName())
                            .email(room.getHost().getEmail())
                            .build()).collect(Collectors.toList());
        }

        return reserveMailDtos;
    }

    public void sendReserveMail(){
        List<ReserveMailDto> reservedRoomMailDtos = findReserveRoomByNowTime();

        if(reservedRoomMailDtos != null){
            reservedRoomMailDtos.forEach(reservedRoomMailDto -> {
                try {
                    mailService.sendReserveMail(reservedRoomMailDto);
                } catch (MessagingException e) {
                    log.warn("예약메일 발송 실패");
                }
            });
        }

    }
}
