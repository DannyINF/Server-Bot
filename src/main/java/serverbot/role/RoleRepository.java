package serverbot.role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, String> {
    Streamable<Role> findAll();

    Streamable<Role> findByServerIdAndRoleType(String serverId, RoleType roleType);
}
