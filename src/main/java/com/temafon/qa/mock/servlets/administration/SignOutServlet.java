package com.temafon.qa.mock.servlets.administration;

import com.temafon.qa.mock.service.accounts.AccountService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignOutServlet extends HttpServlet {

    public static final String path = "/signout";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cookie cookie = SignInServlet.getSessionCookie(request);

        if(cookie != null){
            AccountService.getInstance().deleteSession(cookie.getValue());
            SignInServlet.redirectToSignIn(request, response);
        }
        else {
            response.getWriter().println("Alarm! Session not found!");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
