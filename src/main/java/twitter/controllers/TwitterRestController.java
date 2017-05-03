package twitter.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/twitter")
public class TwitterRestController {

    @Autowired
    private Environment env;

    @RequestMapping("/getChannel")
    public List<TwitterProfile> getChannel(@PathParam(value = "channelName") String channelName,
                             HttpServletRequest request) {
        Twitter twitter = retrieveTwitter(request);
        if(channelName != null && !channelName.equals("")) {
            List<TwitterProfile> profiles = twitter.userOperations().searchForUsers(channelName, 1, 25);
            return profiles;
        }
        return null;
    }

    @RequestMapping("/getChannelTweets")
    public List<Tweet> getChannelTweets(@PathParam(value = "screenName") String screenName,
                                 HttpServletRequest request) {
        Twitter twitter = retrieveTwitter(request);
        List<Tweet> tweets = twitter.timelineOperations().getUserTimeline(screenName, 50);
        return tweets;
    }

    @RequestMapping("/searchChannelTweets")
    public List<Tweet> searchChannelTweets(@PathParam(value = "searchTerm") String searchTerm,
                                             HttpServletRequest request) {
        Twitter twitter = retrieveTwitter(request);
        SearchResults searchResults = twitter.searchOperations().search(searchTerm, 20, -1L, -1L);
        return searchResults.getTweets();
    }

    private Twitter retrieveTwitter(HttpServletRequest request) {
        String accessToken = request.getParameter("accessToken");
        String accessTokenSecret = request.getParameter("accessTokenSecret");
        String consumerKey = env.getProperty("spring.social.twitter.appId");
        String consumerSecret = env.getProperty("spring.social.twitter.appSecret");
        return new TwitterTemplate(consumerKey,
                consumerSecret,
                accessToken,
                accessTokenSecret);
    }
}