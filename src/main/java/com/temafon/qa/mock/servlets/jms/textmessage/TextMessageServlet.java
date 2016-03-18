package com.temafon.qa.mock.servlets.jms.textmessage;

import javax.jms.JMSException;

import com.temafon.qa.mock.service.authorizarion.AuthorizationManager;
import com.temafon.qa.mock.service.jms.JMSManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TextMessageServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        if(auth == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        else {
            if(!AuthorizationManager.verifyAuthorizationCode(auth)){
                response.setHeader("X-Status-Code", "100");
                response.setHeader("X-Status-Text", "Login/password incorrect");
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
        }

        String server = request.getParameter("server");
        String queue = request.getParameter("queue");
        String text =  request.getParameter("text");

        String[] properties = request.getParameterValues("property");

        if(server != null && queue != null && text != null ){
            try {

                JMSManager jms = new JMSManager();

                if(properties != null){
                    for(String p : properties){
                        String[] prop = p.split(":");
                        jms.addStringProperties(prop[0], prop[1]);
                    }
                }

                jms.sendMessage(server, queue, text);

                response.setHeader("X-Status-Code", "0");
                response.setHeader("X-Status-Text", "OK");
                response.setStatus(HttpServletResponse.SC_OK);

            }
            catch (JMSException e){
                response.setHeader("X-Status-Code", "500");
                response.setHeader("X-Status-Text", "Server Error: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            catch (Exception e){
                response.setHeader("X-Status-Code", "510");
                response.setHeader("X-Status-Text", "Unexpected Server Error: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        else {
            response.setHeader("X-Status-Code", "400");
            response.setHeader("X-Status-Text", "Required parameters is empty [server, queue, text]");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
