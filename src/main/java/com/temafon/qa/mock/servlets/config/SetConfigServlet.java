package com.temafon.qa.mock.servlets.config;

import com.temafon.qa.mock.service.config.ConfigManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by m.guriev on 29.02.2016.
 */
public class SetConfigServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String param = request.getParameter("parameter");
        String value = request.getParameter("value");

        if(param.equals("JMS_MANAGER_SLEEP_TIMEOUT")){
            ConfigManager.JMS_MANAGER_SLEEP_TIMEOUT = Long.parseLong(value); }
        else if(param.equals("JMS_MANAGER_POOL_LIFETIME")){
            ConfigManager.JMS_MANAGER_POOL_LIFETIME = Long.parseLong(value); }
    }
}
