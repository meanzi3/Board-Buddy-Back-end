package sumcoda.boardbuddy.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.MemberRequest;
import sumcoda.boardbuddy.enumerate.MemberRole;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MemberTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    private final String username = "testUser";

    @BeforeEach
    void setUp() {
        // 테스트를 위한 사용자 생성 및 저장
        Member member = Member.buildMember(
                username,
                "password",
                "nickname",
                "test@example.com",
                null,
                "서울특별시",
                "종로구",
                "필운동",
                2,
                50,
                0,
                0,
                0,
                0,
                0,
                0,
                null,
                null,
                MemberRole.USER,
                null
        );
        memberRepository.save(member);
    }

    @Test
    @DisplayName("유저 위치 업데이트 테스트")
    @WithMockUser(username = "testUser", roles = "USER")
    public void updateMemberLocationTest() throws Exception {
        // 위치 정보 DTO 생성
        MemberRequest.LocationDTO locationDTO = new MemberRequest.LocationDTO("서울특별시", "종로구", "신교동");

        // JSON 변환
        String jsonRequest = objectMapper.writeValueAsString(locationDTO);

        // MockMvc 를 사용하여 API 요청
        mockMvc.perform(post("/api/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("위치 정보 설정을 성공하였습니다."));

        // 데이터베이스에서 유저를 가져와서 업데이트된 위치 정보 검증
        Member updatedMember = memberRepository.findByUsername(username).orElseThrow(() -> new MemberRetrievalException("유효하지 않은 사용자입니다."));
        assertThat(updatedMember.getSido()).isEqualTo("서울특별시");
        assertThat(updatedMember.getSigu()).isEqualTo("종로구");
        assertThat(updatedMember.getDong()).isEqualTo("신교동");
    }
}