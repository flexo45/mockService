package com.temafon.qa.mock.servlets.administration;

import com.temafon.qa.mock.service.accounts.AccountService;
import com.temafon.qa.mock.service.accounts.UserProfile;
import com.temafon.qa.mock.service.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SignInServlet extends HttpServlet {

    public static final String path = "/signin";
    private static final String AUTH_COOKIE_NAME = "MOCKSESSION";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        String page = PageGenerator.getInstance()
                .getUnAuthorizedAdminPage(getTemplaterData("Sign In"));

        response.getWriter().println(page);
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String login = null;
        String password = null;
        String line = "";
        while ((line = request.getReader().readLine()) != null){
            String[] parameter = line.split("=");
            if(parameter.length == 2){
                switch (parameter[0]){
                    case "login":
                        login = parameter[1];
                        break;
                    case "password":
                        password = parameter[1];
                        break;
                }
            }
        }

        if(login == null || password == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            SignInServlet.redirectToSignIn(request, response);
        }
        else {
            UserProfile userProfile = AccountService.getInstance().getUserByLogin(login);

            if(userProfile == null || !userProfile.getPass().equals(password)){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                SignInServlet.redirectToSignIn(request, response);
            }
            else {
                String sessionId = request.getSession().getId();
                String domain = request.getServerName();
                String path = "/";

                Cookie cookie = new Cookie(AUTH_COOKIE_NAME, sessionId);
                cookie.setDomain(domain);
                cookie.setPath(path);

                response.addCookie(cookie);
                AccountService.getInstance().addSession(sessionId, userProfile);

                String back_url = request.getParameter("back_url");
                String host = request.getServerName();
                int port = request.getServerPort();
                response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                if(back_url == null){
                    response.setHeader("Location", "http://" + host + ":" + port + AdminServlet.path);
                }
                else {
                    response.setHeader("Location", URLDecoder.decode(back_url, "UTF-8"));
                }
            }
        }
    }

    private Map<String, Object> getTemplaterData(String title){
        Map<String, Object> pageParams = new HashMap<>();
        pageParams.put("title", title);
        return pageParams;
    }

    public static void redirectToSignIn(HttpServletRequest request, HttpServletResponse response){
        String host = request.getServerName();
        int port = request.getServerPort();
        String back_url = request.getRequestURL().toString();

        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        try {
            response.setHeader("Location", "http://" + host + ":" + port + path + "?back_url=" + URLEncoder.encode(back_url, "UTF-8"));
        }
        catch (UnsupportedEncodingException e){
            response.setHeader("Location", "http://" + host + ":" + port + path + "?back_url=");
        }

    }

    public static Cookie getSessionCookie(HttpServletRequest request){
        for(Cookie cookie : request.getCookies()){
            if(cookie.getName().equals(AUTH_COOKIE_NAME)){
                return cookie;
            }
        }
        return null;
    }

    public static UserProfile verifyAuthorization(HttpServletRequest request, HttpServletResponse response){
        Cookie cookie = getSessionCookie(request);
        if(cookie != null){
            UserProfile userProfile = AccountService.getInstance().getUserBySessionId(cookie.getValue());
            if(userProfile != null){
                return userProfile;
            }
        }
        redirectToSignIn(request, response);
        return null;
    }
}
