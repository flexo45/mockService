package com.temafon.qa.mock.servlets.mail;

import com.temafon.qa.mock.service.authorizarion.AuthorizationManager;
import com.temafon.qa.mock.service.mail.EmailUtils;
import com.temafon.qa.mock.service.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SendMailServlet extends HttpServlet {

    Document document;
    String to = null;
    String subject = null;
    String message = null;
    String server = "";
    String fromAddres;
    String fromPerson;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    /*
    * <email server="">
    *     <from person=""></from>
    *     <to></to>
    *     <subject></subject>
    *     <message></message>
    * </email>
    *
    * */
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

        try {
            document = XMLUtils.getXmlDocument(request.getInputStream());
        }
        catch (Exception e){
            throw new ServletException(e.toString(), e);
        }

        if(document == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        document.getDocumentElement().normalize();



        try {
            to = document.getElementsByTagName("to").item(0).getTextContent();
            subject = document.getElementsByTagName("subject").item(0).getTextContent();
            message = document.getElementsByTagName("message").item(0).getTextContent();
            server = document.getDocumentElement().getAttribute("server");
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        try {
            fromAddres = document.getElementsByTagName("from").item(0).getTextContent();
            fromPerson = ((Element)document.getElementsByTagName("from").item(0)).getAttribute("person");
        }
        catch (Exception e){
            fromAddres = "";
            fromPerson = "";
        }

        try {
            if(server.isEmpty() && fromAddres.isEmpty()){
                EmailUtils.getInstance().sendEmailDefaultConfig(to, subject, message);
            }
            else if(server.isEmpty() && !fromAddres.isEmpty()){
                EmailUtils.getInstance().sendEmailCustom(to, subject, message, fromAddres, fromPerson);
            }
            else if(!server.isEmpty() && !fromAddres.isEmpty()){
                EmailUtils.getInstance().sendEmailCustom(to, subject, message, server, fromAddres, fromPerson);
            }
            else if(!server.isEmpty() && fromAddres.isEmpty()){
                EmailUtils.getInstance().sendEmailCustom(to, subject, message, server);
            }
            else {
                EmailUtils.getInstance().sendEmailDefaultConfig(to, subject, message);
            }
        }
        catch (Exception e){
            throw new ServletException(e.toString(), e);
        }

        response.setHeader("X-Status-Code", "0");
        response.setHeader("X-Status-Text", "OK");
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
