package serverbot.member;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import serverbot.Main;
import serverbot.server.Server;
import serverbot.util.DiscordActions;

@Controller
public class MemberController {

    // TODO: Add modal for number of deldays and message
    @GetMapping("/member/ban")
    @PreAuthorize("isAuthenticated()")
    public String ban(@RequestParam("sid") Server server, @RequestParam("mid") String memberId) {
        DiscordActions.ban(Main.jda.getGuildById(server.getId()), Main.jda.getGuildById(server.getId()).getMemberById(memberId), "", 0);
        return "redirect:/stats?sid=" + server.getId() + "&mid=" + memberId;
    }

    @GetMapping("/member/kick")
    @PreAuthorize("isAuthenticated()")
    public String kick(@RequestParam("sid") Server server, @RequestParam("mid") String memberId) {
        DiscordActions.kick(Main.jda.getGuildById(server.getId()), Main.jda.getGuildById(server.getId()).getMemberById(memberId), "");
        return "redirect:/stats?sid=" + server.getId() + "&mid=" + memberId;
    }

    @GetMapping("/member/exil")
    @PreAuthorize("isAuthenticated()")
    public String exil(@RequestParam("sid") Server server, @RequestParam("mid") String memberId, OAuth2AuthenticationToken authenticationToken) {
        DiscordActions.exil(Main.jda.getGuildById(server.getId()), Main.jda.getGuildById(server.getId()).getMemberById(memberId), Main.jda.getGuildById(server.getId()).getMemberById(authenticationToken.getPrincipal().getAttributes().get("id").toString()), 0L, "");
        return "redirect:/stats?sid=" + server.getId() + "&mid=" + memberId;
    }

    @GetMapping("/member/rename")
    @PreAuthorize("isAuthenticated()")
    public String rename(@RequestParam("sid") Server server, @RequestParam("mid") String memberId) {
        DiscordActions.rename(Main.jda.getGuildById(server.getId()), Main.jda.getGuildById(server.getId()).getMemberById(memberId), "newName");
        return "redirect:/stats?sid=" + server.getId() + "&mid=" + memberId;
    }
}
