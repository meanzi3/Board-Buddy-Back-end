package sumcoda.boardbuddy.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class SMSCertificationRepository {

    // Redis에 저장되는 Key값이 중복되지 않도록 상수 선언
    private final String RECEIVED_PHONE_NUMBER_PREFIX = "sms:received:phone:number ";
    // Redis에서 해당 데이터의 유효시간(TTL)을 설정
    private final int TIME_TO_LIVE = 3 * 60;
    private final String ATTEMPT_PREFIX = "sms:attempt:";

    private final StringRedisTemplate redisTemplate;

    /**
     * 사용자가 입력한 휴대폰 번호와 인증번호를 저장하고 TTL을 180초로 설정
     *
     * @param phoneNumber 사용자가 입력한 핸드폰 번호
     * @param certificationNumber 사용자에게 전송할 인증번호
     **/
    public void createSMSCertification(String phoneNumber, String certificationNumber) {
        redisTemplate.opsForValue()
                .set(RECEIVED_PHONE_NUMBER_PREFIX + phoneNumber, certificationNumber, Duration.ofSeconds(TIME_TO_LIVE));
    }

    /**
     * Redis에서 휴대폰번호(KEY)에 해당하는 인증번호를 리턴
     *
     * @param phoneNumber 사용자가 입력한 핸드폰 번호
     * @return 해당 휴대폰번호(KEY)에 해당하는 인증번호
     **/
    public String getSMSCertification(String phoneNumber) {
        return redisTemplate.opsForValue().get(RECEIVED_PHONE_NUMBER_PREFIX + phoneNumber);
    }

    /**
     * 인증이 완료되었을 경우 메모리 관리를 위해 Redis에 저장된 인증번호 삭제
     *
     * @param phoneNumber 사용자가 입력한 핸드폰 번호
     **/
    public void removeSMSCertification(String phoneNumber) {
        redisTemplate.delete(RECEIVED_PHONE_NUMBER_PREFIX + phoneNumber);
    }

    /**
     * Redis에 해당 휴대폰번호(KEY)로 저장된 인증번호(VALUE)가 존재하는지 확인
     *
     * @param phoneNumber 사용자가 입력한 핸드폰 번호
     * @return 해당 휴대폰번호(KEY)에 해당하는 인증번호(VALUE)가 존재한다면 TRUE 리턴
     **/
    public Boolean hasKey(String phoneNumber) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(RECEIVED_PHONE_NUMBER_PREFIX + phoneNumber));
    }

    /**
     * Redis에서 인증번호 검증 시도 횟수를 조회
     *
     * @param phoneNumber 사용자의 핸드폰 번호
     * @return 시도 횟수
     **/
    public int getAttemptCount(String phoneNumber) {
        String attempts = redisTemplate.opsForValue().get(ATTEMPT_PREFIX + phoneNumber);
        return attempts == null ? 0 : Integer.parseInt(attempts);
    }

    /**
     * Redis에서 인증번호 검증 시도 횟수를 증가
     *
     * @param phoneNumber 사용자의 핸드폰 번호
     **/
    public void incrementAttemptCount(String phoneNumber) {
        int attempts = getAttemptCount(phoneNumber);
        redisTemplate.opsForValue().set(ATTEMPT_PREFIX + phoneNumber, String.valueOf(attempts + 1));
    }

    /**
     * Redis에서 인증번호 검증 시도 횟수를 초기화
     *
     * @param phoneNumber 사용자의 핸드폰 번호
     **/
    public void resetAttemptCount(String phoneNumber) {
        redisTemplate.delete(ATTEMPT_PREFIX + phoneNumber);
    }
}
