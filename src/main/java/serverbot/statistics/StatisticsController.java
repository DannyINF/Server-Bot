package serverbot.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import serverbot.Main;
import serverbot.member.MemberId;
import serverbot.member.MemberManagement;
import serverbot.server.Server;

import javax.validation.Valid;

@Controller
public class StatisticsController {

    @Autowired
    MemberManagement memberManagement;

    @Autowired
    StatisticsManagement statisticsManagement;


    @GetMapping("/stats")
    @PreAuthorize("isAuthenticated()")
    public String detail(@RequestParam("sid") Server server, @RequestParam("mid") String memberId, Model model) {
        model.addAttribute("discordMember", Main.jda.getGuildById(server.getId()).getMemberById(memberId));
        model.addAttribute("server", server);
        model.addAttribute("member", memberManagement.findById(new MemberId(memberId, server.getId())));
        model.addAttribute("stats", statisticsManagement.findById(new StatisticsId(memberId, server.getId())).get());
        return "stats/detail";
    }

    @GetMapping("/stats/edit")
    @PreAuthorize("isAuthenticated()")
    public String stats_edit(@RequestParam("sid") Server server, @RequestParam("mid") String memberId, Model model, StatisticsForm statisticsForm) {
        model.addAttribute("discordMember", Main.jda.getGuildById(server.getId()).getMemberById(memberId));
        model.addAttribute("server", server);
        model.addAttribute("member", memberManagement.findById(new MemberId(memberId, server.getId())));
        model.addAttribute("stats", statisticsManagement.findById(new StatisticsId(memberId, server.getId())).get());
        return "stats/edit";
    }

    @PostMapping("/stats/edit")
    @PreAuthorize("isAuthenticated()")
    public String stats_edit_post(@RequestParam("sid") Server server, @RequestParam("mid") String memberId, Model model, @Valid StatisticsForm statisticsForm, Errors errors) {
        Statistics statistics = statisticsManagement.findById(new StatisticsId(memberId, server.getId())).get();
        System.out.println(statisticsForm.getFirstJoin());
        statistics.edit(statisticsForm.getWords(), statisticsForm.getMessages(), statisticsForm.getChars(), statisticsForm.getVoiceTime(), statisticsForm.getFirstJoin(), statisticsForm.getLastJoin(), statisticsForm.getLastLeave(), statisticsForm.getXp(), statisticsForm.getLevel(), statisticsForm.getCurrency());
        statisticsManagement.save(statistics);
        System.out.println(statistics.getFirstJoin());
        return "redirect:/stats?sid=" + server.getId() + "&mid=" + memberId;
    }

}
