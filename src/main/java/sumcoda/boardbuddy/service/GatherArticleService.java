package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.repository.GatherArticleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GatherArticleService {

    private final GatherArticleRepository gatherArticleRepository;

    /**
     * 내가 작성한 모집글 조회
     *
     * @param username 회원의 username
     * @return 내가 작성한 모집글 DTO 리스트
     **/
    @Transactional
    public List<GatherArticleResponse.GatherArticleDTO> getMyGatherArticles(String username) {
        return gatherArticleRepository.findGatherArticleDTOByUsername(username);
    }
}
