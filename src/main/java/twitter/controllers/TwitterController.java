package twitter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class TwitterController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @RequestMapping(method= RequestMethod.GET, value = "/main")
    public String mainPage(Model model) {
        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "redirect:/connect/twitter";
        }
        Connection<Twitter> connection = connectionRepository.findPrimaryConnection(Twitter.class);
        model.addAttribute("accessToken", connection.createData().getAccessToken());
        model.addAttribute("accessTokenSecret", connection.createData().getSecret());
        return "main";
    }


}
