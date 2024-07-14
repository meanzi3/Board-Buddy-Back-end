package sumcoda.boardbuddy.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SMSCertificationUtil {

    @Value("${spring.coolsms.api-key}")
    private String apiKey;

    @Value("${spring.coolsms.api-secret}")
    private String apiSecret;

    @Value("${spring.coolsms.sender-number}")
    private String senderNumber;

    DefaultMessageService messageService;


    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    /**
     * 인증 메시지를 전송할 핸드폰 번호를 지정하고 메시지의 형식을 작성
     *
     * @param receivePhoneNumber 사용자가 입력한 핸드폰 번호
     * @param certificationNumber 사용자에게 전송할 인증번호
     **/
    public SingleMessageSentResponse sendSMS(String receivePhoneNumber, String certificationNumber){
        Message message = new Message();
        message.setFrom(senderNumber);
        message.setTo(receivePhoneNumber);
        message.setText("[보드버디] 본인 확인 인증번호는 "+ certificationNumber + " 입니다.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        log.info(response.toString());
        return response;
    }

}
