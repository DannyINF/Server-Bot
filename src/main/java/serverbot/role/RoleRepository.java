package serverbot.role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, RoleId> {
    Streamable<Role> findAll();

    Optional<Role> findByRoleId(String roleId);

    Streamable<Role> findByServerIdAndRoleType(String serverId, RoleType roleType);
}
