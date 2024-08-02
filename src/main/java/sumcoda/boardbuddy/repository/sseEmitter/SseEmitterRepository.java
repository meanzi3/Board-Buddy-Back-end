package sumcoda.boardbuddy.repository.sseEmitter;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface SseEmitterRepository {

    void saveEventCache(String eventCacheId, Object event);

    Map<String, Object> findAllEventCacheStartsWithUsername(String username);

    void deleteEmitterById(String emitterId);
}
