package hanium.videoMeeting;

import hanium.videoMeeting.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class ReservedMailScheduler {
    private final RoomService roomService;

    @Scheduled(cron = "0 * * * * *")
    public void run(){
        // 매 분마다 현재 시각에 예약된 방이 있는지 찾아서 메일 발송
        roomService.sendReserveMail();
    }
}
