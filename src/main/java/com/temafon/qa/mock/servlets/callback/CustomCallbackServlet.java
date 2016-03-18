package com.temafon.qa.mock.servlets.callback;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomCallbackServlet {

    private Map<String, String> headers = new HashMap<>();
    private int http_status = HttpServletResponse.SC_OK;
    private String content;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
