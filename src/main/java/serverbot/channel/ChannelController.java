package serverbot.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChannelController {

    @Autowired
    private ChannelManagement channelManagement;

    @GetMapping("/changeChannelType/{channelType}/{channelId}/{serverId}")
    @PreAuthorize("isAuthenticated()")
    public String changeRoleType(Model model, @PathVariable(value = "channelType") String stringChannelType, @PathVariable(value = "channelId") String channelId, @PathVariable(value = "serverId")
            String serverId) {
        ChannelType channelType = ChannelType.valueOf(stringChannelType);
        Channel channel = channelManagement.findById(new ChannelId(channelId, serverId)).get();
        channel.setChannelType(channelType);
        channelManagement.save(channel);
        return "redirect:/server?id=" + serverId + "&channelsShown=1";
    }
}
