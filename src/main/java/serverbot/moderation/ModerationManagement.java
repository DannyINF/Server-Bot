package serverbot.moderation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModerationManagement {

    @Autowired
    ModerationRepository moderationRepository;

    public Streamable<Moderation> findAll() {
        return moderationRepository.findAll();
    }

    public void save(Moderation moderation) {
        moderationRepository.save(moderation);
    }
}
