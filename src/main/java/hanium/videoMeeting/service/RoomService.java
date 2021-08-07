package hanium.videoMeeting.service;

import hanium.videoMeeting.DTO.RoomDto;
import hanium.videoMeeting.advice.exception.ExistedRoomTitleException;
import hanium.videoMeeting.advice.exception.NoRoomSessionException;
import hanium.videoMeeting.advice.exception.NoSuchRoomException;
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



}
