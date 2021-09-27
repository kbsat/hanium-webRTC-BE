package hanium.videoMeeting.service;

import hanium.videoMeeting.DTO.ReserveMailDto;
import hanium.videoMeeting.domain.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    public void sendReserveMail(ReserveMailDto reserveMailDto) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(msg, true, "UTF-8");

        String roomHostName = reserveMailDto.getRoomHostName();
        String roomName = reserveMailDto.getRoomName();
        String hostEmail = reserveMailDto.getEmail();
        LocalDateTime reserveTime = reserveMailDto.getReserveTime();

        messageHelper.setSubject("[VideoMeeting] 예약된 회의에 참석하세요.");
        String htmlForm = "<!DOCTYPE html>\n" +
                "<html lang=\"kr\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body style=\"margin: 0; padding: 0;\">\n" +
                "<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "    <tr>\n" +
                "        <td bgcolor=\"#ffffff\" align=\"center\" style=\"padding: 0 10px 0 10px;\">\n" +
                "            <!--[if (gte mso 9)|(IE)]>\n" +
                "            <table role=\"presentation\" align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"600\">\n" +
                "                <tr>\n" +
                "                    <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                "            <![endif]-->\n" +
                "            <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\"\n" +
                "                   class=\"table-max\">\n" +
                "                <tbody>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                            <tbody>\n" +
                "                            <tr>\n" +
                "                                <td align=\"center\">\n" +
                "                                    <img src=\"https://cdn.pixabay.com/photo/2020/07/02/18/28/video-conference-5363856_960_720.png\"\n" +
                "                                         width=\"100%\" border=\"0\" alt=\"Replace with your text\"\n" +
                "                                         style=\"display: block; border:0; width:100%; height:auto !important;\"\n" +
                "                                         class=\"img-max\"></td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td>\n" +
                "                                    <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                        <tbody>\n" +
                "                                        <tr>\n" +
                "                                            <td align=\"center\" height=\"25\" style=\"height:25px; font-size: 0;\">&nbsp;\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                        <tr>\n" +
                "                                            <td align=\"left\"><h1\n" +
                "                                                    style=\"font-family: Helvetica, Arial, sans-serif; font-size: 28px; font-weight:normal; color: #2C3E50; margin:0; mso-line-height-rule:exactly;\">\n" +
                "                                                [VideoMeeting] 예약된 회의 시간이 되었습니다.</h1></td>\n" +
                "                                        </tr>\n" +
                "                                        <tr>\n" +
                "                                            <td align=\"center\" height=\"25\" style=\"height:25px; font-size: 0;\">&nbsp;\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                        <tr>\n" +
                "                                            <td align=\"left\"\n" +
                "                                                style=\"font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 25px; color: #2C3E50;\">\n" +
                "                                                Host 이름 :" + roomHostName + "\n" +
                "                                                <br>\n" +
                "                                                <br>\n" +
                "                                                방 이름 :" + roomName +"\n" +
                "                                                <br>\n" +
                "                                                <br>\n" +
                "                                                예약 시간 :"+ reserveTime+"\n" +
                "                                                <br>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                        </tbody>\n" +
                "                                    </table>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "            <!--[if (gte mso 9)|(IE)]>\n" +
                "            </td>\n" +
                "            </tr>\n" +
                "            </table>\n" +
                "            <![endif]-->\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td align=\"center\" height=\"25\" style=\"height:25px; font-size: 0;\">&nbsp;</td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>";
        messageHelper.setText(htmlForm, true);
        messageHelper.setTo(hostEmail);

        msg.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(hostEmail));
        mailSender.send(msg);
    }
}
