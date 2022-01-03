package serverbot.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ServerController {

    @Autowired
    ServerManagement serverManagement;

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

        /*OAuth2AuthorizedClient client = auth2AuthorizedClientService
                .loadAuthorizedClient(
                        auth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                        auth2AuthenticationToken.getName());*/

        model.addAttribute("servers", serverManagement.getServersOfUser(auth2AuthenticationToken.getPrincipal().getAttributes().get("id").toString()));

        return "server/overview";
    }

    @GetMapping("/server")
    @PreAuthorize("isAuthenticated()")
    public String detail(@RequestParam("id") Server server, Model model) {

        model.addAttribute("server", server);

        return "server/detail";
    }
}
