package hanium.videoMeeting.service;

import hanium.videoMeeting.DTO.RoomDto;
import hanium.videoMeeting.advice.exception.ExistedRoomTitleException;
import hanium.videoMeeting.domain.Room;
import hanium.videoMeeting.repository.RoomRepository;
import io.openvidu.java.client.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class RoomServiceTest {

    @Autowired private OpenVidu openVidu;
    @Autowired private RoomService roomService;
    @Autowired private RoomRepository roomRepository;

    @DisplayName("룸 생성 테스트")
    @Test
    public void createRoomTest() throws Exception {
        //given
        RoomDto roomDto = new RoomDto("test","12345");
        String session = roomService.create(roomDto,1L);

        //when
        Room createdRoom = roomRepository.findBySession(session).orElse(null);

        //then
        assertThat(createdRoom).isNotNull();
        assertThat(createdRoom.getTitle()).isEqualTo(roomDto.getTitle());
        assertThat(createdRoom.getPassword()).isEqualTo(roomDto.getPassword());
        assertThat(createdRoom.getPeople_num()).isEqualTo(0);
        assertThat(createdRoom.getSession()).isNotNull();
        System.out.println("생성 날짜 : " + createdRoom.getStart_time());
        System.out.println("세션 ID : " + createdRoom.getSession());


        // 테스트를 위해 만든 세션 제거
        List<Session> activeSessions = openVidu.getActiveSessions();
        activeSessions.forEach(s -> {
            try {
                s.close();
            } catch (OpenViduJavaClientException | OpenViduHttpException e) {
                e.printStackTrace();
            }
        });

    }
    
    @Test
    public void createDuplicateTitle() throws Exception {
        //given
        RoomDto roomDto = new RoomDto("duplicationTest","12345");
        roomService.create(roomDto,1L);

        //when
        RoomDto dupRoomDto = new RoomDto("duplicationTest","789456");
        try{
            roomService.create(dupRoomDto,1L);
        } catch (ExistedRoomTitleException e){
            return;
        }

        //then
        fail("중복 예외가 발생해야합니다.");

    }

    @DisplayName("방 참가 및 토큰 생성 예제")
    @Test
    public void joinRoom() throws Exception {
        //given
        RoomDto roomDto = new RoomDto("test","12345");
        roomService.create(roomDto,1L);

        //when
        String token = roomService.join(roomDto,1L);

        //then
        assertThat(token).isNotNull();
        System.out.println(token);
    }

    @Test
    public void openviduSessionList() throws Exception {
        //given
        RoomDto roomDto = new RoomDto("test","12345");
        roomService.create(roomDto,1L);
        List<Session> activeSessions = openVidu.getActiveSessions();

        activeSessions.forEach(s -> System.out.println(s.getSessionId()));

        //when

        //then
    }


}