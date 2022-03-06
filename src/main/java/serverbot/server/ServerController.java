package serverbot.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import serverbot.Main;
import serverbot.channel.ChannelManagement;
import serverbot.member.MemberId;
import serverbot.member.MemberManagement;
import serverbot.role.RoleManagement;

import java.util.Optional;

@Controller
public class ServerController {

    @Autowired
    ServerManagement serverManagement;

    @Autowired
    RoleManagement roleManagement;

    @Autowired
    ChannelManagement channelManagement;

    @Autowired
    MemberManagement memberManagement;

    @Autowired
    OAuth2AuthorizedClientService auth2AuthorizedClientService;

    @GetMapping("/servers")
    @PreAuthorize("isAuthenticated()")
    public String servers(Model model, @RequestParam("id") Optional<String> serverId, OAuth2AuthenticationToken auth2AuthenticationToken) {
        if (serverId.isPresent()) {
            Optional<Server> server = serverManagement.findById(serverId.get());
            if (server.isPresent()) {
                model.addAttribute("server", server.get());
                return "server/detail";
            }
        }

        model.addAttribute("servers", serverManagement.getServersOfUser(auth2AuthenticationToken.getPrincipal().getAttributes().get("id").toString()));

        return "server/overview";
    }

    @GetMapping("/server")
    @PreAuthorize("isAuthenticated()")
    public String detail(@RequestParam("id") Server server, @RequestParam("rolesShown") Optional<Integer> rolesShown, @RequestParam("channelsShown") Optional<Integer> channelsShown, Model model) {

        model.addAttribute("server", server);
        model.addAttribute("rolemanagement", roleManagement);
        model.addAttribute("roles", Main.jda.getGuildById(server.getId()).getRoles());
        model.addAttribute("jda", Main.jda);
        if (rolesShown.isPresent() && rolesShown.get() == 1) {
            model.addAttribute("rolesShown", true);
        } else {
            model.addAttribute("rolesShown", false);
        }

        model.addAttribute("channelManagement", channelManagement);
        model.addAttribute("channels", Main.jda.getGuildById(server.getId()).getChannels());
        if (channelsShown.isPresent() && channelsShown.get() == 1) {
            model.addAttribute("channelsShown", true);
        } else {
            model.addAttribute("channelsShown", false);
        }

        model.addAttribute("memberManagement", memberManagement);
        model.addAttribute("members", Main.jda.getGuildById(server.getId()).getMembers());
        if (channelsShown.isPresent() && channelsShown.get() == 1) {
            model.addAttribute("channelsShown", true);
        } else {
            model.addAttribute("channelsShown", false);
        }

        return "server/detail";
    }

}
