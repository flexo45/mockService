package com.temafon.qa.mock.servlets.administration;

import com.temafon.qa.mock.service.accounts.UserProfile;
import com.temafon.qa.mock.service.data.dataSet.Script;
import com.temafon.qa.mock.service.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdminServlet extends HttpServlet {

    public static final String path = "/admin";

    private UserProfile userProfile = null;

    private Map<String, Object> contentParams = new HashMap<>();

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        userProfile = SignInServlet.verifyAuthorization(request, response);
        if(userProfile != null){

            String pageParameter = request.getParameter("page");
            if(pageParameter == null){
                contentParams.put("title", "Admin Console");
                contentParams.put("datetime", new Date().toString());

                response.getWriter().println(PageGenerator.getInstance().getAuthorizedAdminPage("welcome.html"
                        , getDefaultTemplaterData(contentParams)));
            }
            else if(pageParameter.equals("dynamic_resources")){
                contentParams.put("title", "Dynamic Resources");
                contentParams.put("resource_row", PageGenerator.getInstance().getDynamicPageContent());

                response.getWriter().println(PageGenerator.getInstance().getAuthorizedAdminPage("dynamic_resources.html"
                        , getDefaultTemplaterData(contentParams)));
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private Map<String, Object> getDefaultTemplaterData(Map<String, Object> pageParams){
        pageParams.put("user", userProfile);
        pageParams.put("role", "default"); //TODO ROLES
        return pageParams;
    }
}
