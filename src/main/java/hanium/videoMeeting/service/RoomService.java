package hanium.videoMeeting.service;

import hanium.videoMeeting.DTO.RoomDto;
import hanium.videoMeeting.advice.exception.ExistedRoomTitleException;
import hanium.videoMeeting.advice.exception.NoRoomSessionException;
import hanium.videoMeeting.advice.exception.NoSuchRoomException;
import hanium.videoMeeting.domain.Join_Room;
import hanium.videoMeeting.domain.Room;
import hanium.videoMeeting.repository.JoinRoomRepository;
import hanium.videoMeeting.repository.RoomRepository;
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
    private final JoinRoomRepository joinRoomRepository;
    private final OpenVidu openVidu;

    @Transactional
    public Long createRoom(RoomDto roomDto) {

        /**
         * TODO
         * 방 만드는 사람을 확인 후 이를 연결해주는 작업 필요. ( Room의 host_id 연관관계 추가해야함 )
         */

        // 일치하는 방제가 있는지 확인
        if (roomRepository.findByTitle(roomDto.getTitle()).isPresent()) {
            throw new ExistedRoomTitleException();
        }

        Room room = new Room(roomDto.getTitle(), roomDto.getPassword());
        // 세션 생성
        try {
            Session session = openVidu.createSession();
            room.connectSession(session.getSessionId());

            roomRepository.save(room);

        } catch (OpenViduException e) {
            log.warn("오픈비두 할당 오류가 발생했습니다.");
            e.printStackTrace();
        } catch (NullPointerException ne) {
            log.warn("오픈비두에서 세션을 생성하지 못했습니다.");
            ne.printStackTrace();
        }

        return room.getId();
    }


    public String join(RoomDto roomDto) {
        Room room = roomRepository.findByTitle(roomDto.getTitle()).orElseThrow(NoSuchRoomException::new);

        if (room.getSession() == null) {
            throw new NoRoomSessionException("세션을 할당받지 못한 Room 입니다.");
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

        if(session == null){
            throw new NoRoomSessionException();
        }
        try {
            String token = session.createConnection(connectionProperties).getToken();

            log.info("[{}] {} 세션에서 토큰 발행 : {}", room.getTitle(), room.getSession(), token);

            return token;
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace();
        }
        return null;

        /**
         * TODO
         * Join_Room 객체 하나 생성해야함. ( User와 Room을 연결 - 어떤 방에 연결되어있는지 확인 용)
         * Room 객체의 people_num도 업데이트 필요
         */


    }

}
