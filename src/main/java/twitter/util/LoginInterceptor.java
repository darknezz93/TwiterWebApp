package twitter.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private Environment env;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getParameter("accessToken");
        String accessTokenSecret = request.getParameter("accessTokenSecret");
        String consumerKey = env.getProperty("spring.social.twitter.appId");
        String consumerSecret = env.getProperty("spring.social.twitter.appSecret");
        boolean isAuthorized = false;
        if(accessToken != null && accessTokenSecret != null) {
            Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
            isAuthorized = twitter.isAuthorized();
        }
        prepareResponse(response, isAuthorized);
        return isAuthorized;
    }

    private void prepareResponse(HttpServletResponse response, boolean isAuthorized) throws IOException {
        if(!isAuthorized) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


}