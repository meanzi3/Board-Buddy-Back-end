package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.AuthRequest;
import sumcoda.boardbuddy.dto.AuthResponse;
import sumcoda.boardbuddy.dto.client.MemberAuthProfileDTO;
import sumcoda.boardbuddy.dto.fetch.MemberAuthProfileProjection;
import sumcoda.boardbuddy.exception.auth.InvalidPasswordException;
import sumcoda.boardbuddy.exception.auth.SMSCertificationAttemptExceededException;
import sumcoda.boardbuddy.exception.auth.SMSCertificationExpiredException;
import sumcoda.boardbuddy.exception.auth.SMSCertificationNumberMismatchException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.exception.member.PhoneNumberAlreadyExistsException;
import sumcoda.boardbuddy.mapper.MemberProfileMapper;
import sumcoda.boardbuddy.repository.member.MemberRepository;
import sumcoda.boardbuddy.repository.SmsCertificationRepository;
import sumcoda.boardbuddy.util.SmsCertificationUtil;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final SmsCertificationUtil smsCertificationUtil;

    private final SmsCertificationRepository smsCertificationRepository;

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final CloudFrontSignedUrlService cloudFrontSignedUrlService;

    private final MemberProfileMapper memberProfileMapper;


    /**
     * 사용자가 입력한 휴대폰 번호로 6자리 인증번호 전송
     *
     * @param sendSMSCertificationDTO 사용자가 입력한 핸드폰 번호가 저장되어있는 DTO
     **/
    public void sendSMS(AuthRequest.SendSMSCertificationDTO sendSMSCertificationDTO){
        String receivePhoneNumber = sendSMSCertificationDTO.getPhoneNumber();
        Boolean isExistsPhoneNumber = memberRepository.existsByPhoneNumber(receivePhoneNumber);

        if (isExistsPhoneNumber) {
            throw new PhoneNumberAlreadyExistsException("이미 회원가입된 핸드폰 번호 입니다. 핸드폰 번호를 한번 더 확인하세요.");
        }

        SecureRandom secureRandom = new SecureRandom();
        int randomNumber = 100000 + secureRandom.nextInt(900000);
        String certificationNumber = String.valueOf(randomNumber);

        SingleMessageSentResponse singleMessageSentResponse = smsCertificationUtil.sendSMS(receivePhoneNumber, certificationNumber);
        if (singleMessageSentResponse == null) {
            throw new SMSCertificationExpiredException("서버에서 오류가 발생하여 인증 번호 메시지를 전송하지 못하였습니다. 관리자에게 문의하세요.");
        }

        smsCertificationRepository.createSMSCertification(receivePhoneNumber, certificationNumber);
    }

    /**
     * 사용자가 입력한 인증번호가 우효하면서 일치하는지 검증
     *
     * @param validateSMSCertificationDTO 사용자의 핸드폰 번호와 사용자에게 전송할 인증번호가 저장되어있는 DTO
     **/
    public void validateCertificationNumber(AuthRequest.ValidateSMSCertificationDTO validateSMSCertificationDTO) {
        String phoneNumber = validateSMSCertificationDTO.getPhoneNumber();

        checkAttemptCountExceeded(phoneNumber);

        if (isCertificationNumberExpired(phoneNumber)) {
            throw new SMSCertificationExpiredException("인증번호가 유효하지 않습니다.");
        }

        if (!isCertificationNumberMatching(validateSMSCertificationDTO)) {
            incrementAttemptCount(phoneNumber);
            throw new SMSCertificationNumberMismatchException("입력하신 인증번호가 일치하지 않습니다.");
        }

        smsCertificationRepository.removeSMSCertification(phoneNumber);

        smsCertificationRepository.resetAttemptCount(phoneNumber);
    }

    /**
     * 인증 시도 횟수가 초과되었는지 확인
     *
     * @param phoneNumber 사용자의 핸드폰 번호
     **/
    private void checkAttemptCountExceeded(String phoneNumber) {
        if (smsCertificationRepository.getAttemptCount(phoneNumber) >= 3) {
            throw new SMSCertificationAttemptExceededException("인증 시도 횟수가 3회를 초과하였습니다.");
        }
    }

    /**
     * 인증시도 횟수를 증가
     *
     * @param phoneNumber 사용자의 핸드폰 번호
     **/
    private void incrementAttemptCount(String phoneNumber) {
        smsCertificationRepository.incrementAttemptCount(phoneNumber);
    }

    /**
     * 사용자가 입력한 핸드폰 번호로 전송된 인증번호가 유효한지 확인
     *
     * @param phoneNumber 사용자의 핸드폰 번호
     * @return 안증번호가 만료되었다면 true 반환, 만료되지 않았다면 false 반환
     **/
    private boolean isCertificationNumberExpired(String phoneNumber) {
        return !smsCertificationRepository.hasKey(phoneNumber);
    }

    /**
     * 사용자가 입력한 인증번호가 유효하면서 올바른 인증번호를 입력한 것인지 확인
     *
     * @param validateSMSCertificationDTO 사용자의 핸드폰 번호와 사용자에게 전송할 인증번호가 저장되어있는 DTO
     * @return 안증번호가 유효하다면 true 반환, 유요하지 않다면 false 반환
     **/
    public boolean isCertificationNumberMatching(AuthRequest.ValidateSMSCertificationDTO validateSMSCertificationDTO) {
        String phoneNumber = validateSMSCertificationDTO.getPhoneNumber();

        return !isCertificationNumberExpired(phoneNumber) &&
                smsCertificationRepository.getSMSCertification(phoneNumber)
                        .equals(validateSMSCertificationDTO.getCertificationNumber());
    }

    /**
     * 사용자의 로그인 상태를 확인
     *
     * @param username 로그인 사용자 아이디
     * @return 사용자가 로그인한 상태라면 해당 사용자의 프로필 반환 아니라면 null 반환
     **/
    public MemberAuthProfileDTO checkMemberAuthenticationStatus(String username) {
        MemberAuthProfileProjection projection = memberRepository.findMemberAuthProfileByUsername(username).orElseThrow(() ->
                new MemberRetrievalException("해당 유저를 찾을 수 없습니다. 관리자에게 문의하세요."));

        return memberProfileMapper.toMemberAuthProfileDTO(projection);
    }

    /**
     * 입력된 비밀번호가 현재 로그인된 사용자의 비밀번호와 일치하는지 확인
     * @param validatePasswordDTO 입력된 비밀번호가 저장된 DTO
     * @param username 로그인 사용자 아이디
     */
    public void validatePassword(AuthRequest.ValidatePasswordDTO validatePasswordDTO, String username) {
        AuthResponse.ProfileDTO profileDTO = memberRepository.findAuthDTOByUsername(username).orElseThrow(() ->
                new MemberRetrievalException("해당 유저를 찾을 수 없습니다. 관리자에게 문의하세요."));

        // DB에 저장된 암호화된 비밀번호와 입력된 비밀번호를 비교
        boolean isValidate = bCryptPasswordEncoder.matches(validatePasswordDTO.getPassword(), profileDTO.getPassword());

        if (!isValidate) {
            throw new InvalidPasswordException("입력하신 비밀번호가 올바르지 않습니다.");
        }
    }
}
