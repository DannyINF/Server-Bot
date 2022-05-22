package serverbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import serverbot.role.Role;
import serverbot.role.RoleManagement;
import serverbot.role.RoleType;
import serverbot.util.SpringContextUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CmdRole implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {
        String action = args[0];
        String id = args[1];
        RoleManagement roleManagement = SpringContextUtils.getBean(RoleManagement.class);
        switch (action) {
            case "create":
            case "add":
                roleManagement.save(new Role(id, event.getGuild().getId(), RoleType.valueOf(args[2])));
                break;
            case "delete":
            case "remove":
                roleManagement.delete(roleManagement.findByRoleId(id).get());
                break;
            case "edit":
                Role editRole = roleManagement.findByRoleId(id).get();
                editRole.setRoleType(RoleType.valueOf(args[2]));
                roleManagement.save(editRole);
                break;
            case "show":
                StringBuilder stringBuilder = new StringBuilder();
                if (Stream.of(RoleType.values())
                    .map(Enum::name)
                    .collect(Collectors.toList()).contains(id)) {
                    for (Role role : roleManagement.findByServerIdAndRoleType(event.getGuild().getId(), RoleType.valueOf(id))) {
                        stringBuilder.append(event.getGuild().getRoleById(role.getRoleId()).getName());
                        stringBuilder.append(" ");
                    }
                } else {
                    stringBuilder.append(roleManagement.findByRoleId(id).get().getRoleType());
                }
                event.getChannel().sendMessage(stringBuilder.toString()).queue();
                break;
        }
    }
}
