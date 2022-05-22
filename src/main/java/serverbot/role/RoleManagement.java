package serverbot.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RoleManagement {

    @Autowired
    RoleRepository roleRepository;

    public Streamable<Role> findAll() {
        return roleRepository.findAll();
    }

    public Optional<Role> findByRoleId(String roleId) {
        return roleRepository.findByRoleId(roleId);
    }

    public Streamable<Role> findByServerIdAndRoleType(String serverId, RoleType roleType) {
        return roleRepository.findByServerIdAndRoleType(serverId, roleType);
    }

    public void save(Role role) {
        roleRepository.save(role);
    }

    public void delete(Role role) {
        roleRepository.delete(role);
    }
}
