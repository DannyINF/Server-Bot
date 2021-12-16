package serverbot.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ServerManagement {

    @Autowired
    ServerRepository serverRepository;

    public void save(Server server) {
        serverRepository.save(server);
    }

    public Streamable<Server> findAll() {
        return serverRepository.findAll();
    }

    public Optional<Server> findById(String serverId) {
        return serverRepository.findById(serverId);
    }

    public void changeAnnouncementsCounterBy(String id, int amount) {
        Server server = serverRepository.findById(id).get();
        server.setAnnouncementsCounter(server.getAnnouncementsCounter() + amount);
        serverRepository.save(server);
    }

    public void changeXpMultiplierTo(String id, int multiplier) {
        Server server = serverRepository.findById(id).get();
        server.setXpMultiplier(multiplier);
        serverRepository.save(server);
    }
}
