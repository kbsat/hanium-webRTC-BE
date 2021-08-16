package hanium.videoMeeting.service;

import hanium.videoMeeting.DTO.RoomDto;
import hanium.videoMeeting.advice.exception.ExistedRoomTitleException;
import hanium.videoMeeting.domain.Room;
import hanium.videoMeeting.repository.RoomRepository;
import io.openvidu.java.client.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class RoomServiceTest {

    @Autowired private RoomService roomService;
    @Autowired private RoomRepository roomRepository;

    private String session;

    @DisplayName("룸 생성 테스트")
    @Test
    @Order(1)
    public void createRoomTest() throws Exception {
        //given
        RoomDto roomDto = new RoomDto("test","12345");
        session = roomService.create(roomDto,1L);

        //when
        Room createdRoom = roomRepository.findBySession(session).orElse(null);

        if(createdRoom == null){
            fail("방을 생성하지 못했습니다.");
        }

        //then
        assertThat(createdRoom).isNotNull();
        assertThat(createdRoom.getTitle()).isEqualTo(roomDto.getTitle());
        assertThat(createdRoom.getPassword()).isEqualTo(roomDto.getPassword());
        assertThat(createdRoom.getPeople_num()).isEqualTo(0);
        assertThat(createdRoom.getSession()).isNotNull();
        System.out.println("생성 날짜 : " + createdRoom.getStart_time());
        System.out.println("세션 ID : " + createdRoom.getSession());

    }
    
    @Test
    @Order(2)
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
    @Order(3)
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
    @Order(4)
    public void deleteRoom() throws Exception {
        //given
        //when
        Room createdRoom = roomRepository.findBySession(session).orElse(null);
        if(createdRoom == null){
            fail("방을 생성하지 못했습니다.");
        }

        roomService.delete(createdRoom);

        //then
        Room afterDeleteRoom = roomRepository.findBySession(session).orElse(null);
        assertThat(afterDeleteRoom).isNull();

    }


}