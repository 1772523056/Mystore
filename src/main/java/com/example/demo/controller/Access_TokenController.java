package com.example.demo.controller;

import com.example.demo.DTO.AccessTokenDTO;
import com.example.demo.DTO.GithubUserDTO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.provider.GithubProvider;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class Access_TokenController {
    @Autowired
    GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientID;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirecturi;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;


    @GetMapping("/callback")
    public String callback (@RequestParam(name = "code") String code,
                            @RequestParam(name = "state") String state,
                            HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO ();
        accessTokenDTO.setCode (code);
        accessTokenDTO.setClient_id (clientID);
        accessTokenDTO.setState (state);
        accessTokenDTO.setClient_secret (clientSecret);
        accessTokenDTO.setRedirect_uri (redirecturi);
        String Access_token = githubProvider.getAccessToken (accessTokenDTO);
        GithubUserDTO githubUserDTO = githubProvider.getUser (Access_token);
        if (githubUserDTO != null) {
            User user = new User ();
            String token = UUID.randomUUID ().toString ();
            user.setToken (token);
            user.setName (githubUserDTO.getName ());
            user.setAccountId (String.valueOf (githubUserDTO.getId ()));
            user.setAvatarUrl (githubUserDTO.getAvatar_url ());
            userService.createOrUpdate(user);
            response.addCookie (new Cookie ("Token",token ));


            return "redirect:/";
        }
        return "index";
    }
    @GetMapping("/logout")
    public String logOut(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession ().removeAttribute ("user");
        Cookie cookie=new Cookie ("Token", null);
        cookie.setMaxAge (0);
        response.addCookie (cookie);
        return "redirect:/";
    }
}
