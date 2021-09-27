package hanium.videoMeeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class VideoMeetingApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoMeetingApplication.class, args);
	}
}
