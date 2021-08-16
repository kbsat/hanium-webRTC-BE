package hanium.videoMeeting.service;

import hanium.videoMeeting.DTO.RoomDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final JoinRoomRepository joinRoomRepository;
    private final OpenVidu openVidu;

    @Transactional
    public String create(RoomDto roomDto, Long userId) {

        User host = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);

        // 일치하는 방제가 있는지 확인
        if (roomRepository.findByTitle(roomDto.getTitle()).isPresent()) {
            throw new ExistedRoomTitleException();
        }

        // host와 title, password를 입력하여 방 생성
        Room room = new Room(host, roomDto.getTitle(), roomDto.getPassword());

        // 세션 생성
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

        return room.getSession();
    }


    @Transactional
    public String join(RoomDto roomDto, Long userId) {
        Room room = roomRepository.findByTitle(roomDto.getTitle()).orElseThrow(NoSuchRoomException::new);
        User user = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);
        String token = null;

        if (room.getSession() == null) {
            throw new NoRoomSessionException();
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
                .orElseThrow();

        if (session == null) {
            throw new NoRoomSessionException();
        }
        try {
            token = session.createConnection(connectionProperties).getToken();

            log.info("[Room : {}] {} 세션에서 토큰 발행 : {}", room.getTitle(), room.getSession(), token);

            Join_Room joinRoom = new Join_Room(user, room, token);
            joinRoomRepository.save(joinRoom);

        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace();

            throw new OpenViduServerException();
        }

        return token;

    }

    @Transactional
    public void delete(Room room) {
        // CASCADE 설정이 되어있으므로 room을 삭제하면 이와 연관된 Join_Room도 삭제됨

        List<Session> activeSessions = openVidu.getActiveSessions();
        Optional<Session> openViduSession = activeSessions.stream().filter(s -> s.getSessionId().equals(room.getSession())).findFirst();
        try {
            openViduSession.orElseThrow(NoSuchSessionException::new).close();
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace();
            throw new OpenViduServerException();
        }
        roomRepository.delete(room);

    }

    public Room findRoomById(Long roomId){
        return roomRepository.findById(roomId).orElseThrow(NoSuchRoomException::new);
    }

    public Room findRoomByTitle(String title){
        return roomRepository.findByTitle(title).orElseThrow(NoSuchRoomException::new);
    }

    public Room findRoomBySession(String session){
        return roomRepository.findBySession(session).orElseThrow(NoSuchRoomException::new);
    }

}
