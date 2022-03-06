package serverbot.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import serverbot.Main;
import serverbot.server.Server;
import serverbot.statistics.StatisticsId;
import serverbot.statistics.StatisticsManagement;

@Controller
public class MemberController {

    @Autowired
    MemberManagement memberManagement;

    @Autowired
    StatisticsManagement statisticsManagement;


    @GetMapping("/member")
    @PreAuthorize("isAuthenticated()")
    public String detail(@RequestParam("sid") Server server, @RequestParam("mid") String memberId, Model model) {
        model.addAttribute("discordMember", Main.jda.getGuildById(server.getId()).getMemberById(memberId));
        model.addAttribute("server", server);
        model.addAttribute("member", memberManagement.findById(new MemberId(memberId, server.getId())));
        model.addAttribute("stats", statisticsManagement.findById(new StatisticsId(memberId, server.getId())).get());
        return "member/detail";
    }
}
