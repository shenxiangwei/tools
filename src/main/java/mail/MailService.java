package mail;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String fromMail;

    /**
     * 发送邮件的方法
     *
     * 三种收件人模式的解释
     * 1，Message.RecipientType.TO  直接接收人
     * 2，Message.RecipientType.CC  明抄送收件人
     * 3，Message.RecipientType.BCc 暗抄送收件人（不会 被直接收件人 和 明抄送收件人 看见的收件人）
     *
     * @param toAddr  收件人的 邮件地址
     * @param ccAddr  抄送人的 邮件地址
     * @param subject 邮件标题
     * @param html    邮件内容
     * @param srcVos  静态资源类
     * @param athVos  附件资源类
     */
    public void sendMail(String[] toAddr, String[] ccAddr, String subject, String html, List<MailFileVo> srcVos, List<MailFileVo> athVos) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromMail);
            helper.setSubject(subject);
            helper.setText(html, true);
            if (toAddr != null && toAddr.length > 0) {
                Address[] addresses = new Address[toAddr.length];
                for (int i = 0; i < toAddr.length; i ++) {
                    addresses[i] = new InternetAddress(toAddr[i]);
                }
                message.setRecipients(Message.RecipientType.TO, addresses);
            }
            if (ccAddr != null && ccAddr.length > 0) {
                Address[] addresses = new Address[ccAddr.length];
                for (int i = 0; i < ccAddr.length; i ++) {
                    addresses[i] = new InternetAddress(ccAddr[i]);
                }
                message.setRecipients(Message.RecipientType.CC, addresses);
            }
            if (CollectionUtils.isNotEmpty(srcVos)) {
                for (MailFileVo srcVo : srcVos) {
                    helper.addInline(srcVo.getName(), srcVo.getFile());
                }
            }
            if (CollectionUtils.isNotEmpty(athVos)) {
                for (MailFileVo athVo : athVos) {
                    helper.addAttachment(athVo.getName(), athVo.getFile());
                }
            }
            mailSender.send(message);
        } catch (MessagingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static void main(String[] args) {

        sendMail(buildJavaMailSender(),new String[]{"xiangwei.shen@shangri-la.com"},
                null,"ceshi","ceshi",null,null);
    }

    private static JavaMailSender buildJavaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("smtp.outlook.com");
        sender.setPort(587);
        sender.setUsername("AI.WDE.Training@shangri-la.com");
        sender.setPassword("chwrbvqgnlsdvybs");
        Properties properties = sender.getJavaMailProperties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.timeout", "25000");
        properties.put("mail.smtp.starttls.enable", "true");
        return sender;
    }


    /**
     * 支持ttls方式
     * @param mailSender
     * @param toAddr
     * @param ccAddr
     * @param subject
     * @param html
     * @param srcVos
     * @param athVos
     */
    public static void sendMail(JavaMailSender mailSender, String[] toAddr, String[] ccAddr, String subject, String html, List<MailFileVo> srcVos, List<MailFileVo> athVos) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("AI.WDE.Training@shangri-la.com");
            helper.setSubject(subject);
            helper.setText(html, true);
            if (toAddr != null && toAddr.length > 0) {
                Address[] addresses = new Address[toAddr.length];
                for (int i = 0; i < toAddr.length; i ++) {
                    addresses[i] = new InternetAddress(toAddr[i]);
                }
                message.setRecipients(Message.RecipientType.TO, addresses);
            }
            if (ccAddr != null && ccAddr.length > 0) {
                Address[] addresses = new Address[ccAddr.length];
                for (int i = 0; i < ccAddr.length; i ++) {
                    addresses[i] = new InternetAddress(ccAddr[i]);
                }
                message.setRecipients(Message.RecipientType.CC, addresses);
            }
            if (CollectionUtils.isNotEmpty(srcVos)) {
                for (MailFileVo srcVo : srcVos) {
                    helper.addInline(srcVo.getName(), srcVo.getFile());
                }
            }
            if (CollectionUtils.isNotEmpty(athVos)) {
                for (MailFileVo athVo : athVos) {
                    helper.addAttachment(athVo.getName(), athVo.getFile());
                }
            }
            mailSender.send(message);
        } catch (MessagingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }


}
