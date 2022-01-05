package serverbot.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RoleController {

    @Autowired
    RoleManagement roleManagement;

    @GetMapping("/changeRoleType/{roleType}/{roleId}/{serverId}")
    @PreAuthorize("isAuthenticated()")
    public String changeRoleType(Model model, @PathVariable(value = "roleType") String stringRoleType, @PathVariable(value = "roleId") String roleId, @PathVariable(value = "serverId")
            String serverId) {
        RoleType roleType = RoleType.valueOf(stringRoleType);
        Role role = roleManagement.findByRoleId(roleId).get();
        role.setRoleType(roleType);
        roleManagement.save(role);
        return "redirect:/server?id=" + serverId + "&rolesShown=1";
    }
}
