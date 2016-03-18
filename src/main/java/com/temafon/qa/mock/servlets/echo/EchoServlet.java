package com.temafon.qa.mock.servlets.echo;

import com.google.gson.Gson;
import com.temafon.qa.mock.service.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class EchoServlet extends HttpServlet {

    public final static String path = "/echo";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{

        Map<String, Object> parameterMap = new HashMap<>();
        String result;

        parameterMap.put("method", request.getMethod());
        parameterMap.put("url", request.getRequestURL().toString());
        parameterMap.put("query", request.getQueryString() == null ? "NaN" : request.getQueryString());

        Enumeration<String> headers = request.getHeaderNames();

        StringBuilder headersString = new StringBuilder();

        while (headers.hasMoreElements()){
            headersString.append(headers.nextElement() + ": " + request.getHeader(headers.nextElement()) + "</br>");
        }

        parameterMap.put("headers", headersString.toString());

        String content;

        try {
            content = readContent(request);
        }
        catch (IOException e){
            content = "";
        }

        parameterMap.put("content", content);

        if((request.getParameter("f") == null ? "" : request.getParameter("f")).equals("json")){
            response.setContentType("application/json");
            result = new Gson().toJson(parameterMap);
        }
        else if((request.getParameter("f") == null ? "" : request.getParameter("f")).equals("debug")){
            List<String> data = Arrays.asList("1", "2", "3");
            Path file = Paths.get("echofile123.txt");
            Files.write(file, data, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
            response.setContentType("text/html");
            result = "Look for echofile123.txt in " + file.toUri().toString();
        }
        else {
            response.setContentType("text/html;charset=utf-8");
            result = PageGenerator.getInstance().getPage("echo.html", parameterMap);
        }

        try {
            response.getWriter().println(result);
        }
        catch (IOException e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        response.setStatus(HttpServletResponse.SC_OK);

    }

    private String readContent(HttpServletRequest request) throws IOException{
        StringBuilder content = new StringBuilder();
        BufferedReader reader;

        String line = "";
        reader = request.getReader();

        try {
            while ((line = reader.readLine()) != null){
                content.append(line);
            }
        }
        catch (IOException e){
            //TODO
        }
        finally {
            reader.close();
        }

        return content.toString();
    }
}
