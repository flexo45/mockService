package com.temafon.qa.mock.servlets.administration;

import com.google.gson.*;
import com.temafon.qa.mock.service.Json;
import com.temafon.qa.mock.service.accounts.UserProfile;
import com.temafon.qa.mock.service.dynamicresources.DynamicResourcesService;
import com.temafon.qa.mock.service.dynamicresources.constants.RestMethods;
import com.temafon.qa.mock.service.dynamicresources.constants.Strategy;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResourceItem;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResponseItem;
import com.temafon.qa.mock.service.templater.DynamicResourcePages;
import com.temafon.qa.mock.service.templater.DynamicResponsePages;
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
                displayDefaultAdminPage(response);
            }
            else if(pageParameter.equals("dynamic_resources")){
                contentParams.put("title", "Dynamic Resources");
                contentParams.put("resource_row", PageGenerator.getInstance().getDynamicPageContent());

                response.getWriter().println(PageGenerator.getInstance().getAuthorizedAdminPage("dynamic_resources.html"
                        , getDefaultTemplaterData(contentParams)));
            }
            else if(pageParameter.equals("resource")){
                String path = request.getParameter("path");
                if(path == null){
                    displayDefaultAdminPage(response);
                    return;
                }

                response.getWriter().println(DynamicResourcePages.getPage(userProfile
                        , DynamicResourcesService.getInstance().getDynamicResource(path)));

            }
            else if(pageParameter.equals("create_resource")){
                contentParams.put("title", "Create Dynamic Resources");
                response.getWriter().println(PageGenerator.getInstance().getAuthorizedAdminPage("create_resource.html"
                        , getDefaultTemplaterData(contentParams)));
            }
            else if(pageParameter.equals("edit_resource")){
                String resourceId = request.getParameter("res_id");
                if(resourceId == null){
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                response.getWriter().println(DynamicResourcePages.getEditResourcePage(userProfile
                        , DynamicResourcesService.getInstance().getDynamicResource(Long.valueOf(resourceId))));

            }
            else if(pageParameter.equals("create_response")){
                String resourceId = request.getParameter("res_id");
                if(resourceId == null){
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                DynamicResourceItem item
                        = DynamicResourcesService.getInstance().getDynamicResource(Long.valueOf(resourceId));

                response.getWriter().println(DynamicResponsePages
                        .getAddResponsePage(userProfile, item.getId(), item.getPath()));

            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        userProfile = SignInServlet.verifyAuthorization(request, response);
        if(userProfile != null){
            try {
                JsonObject json = Json.getJson(request.getReader());

                String actionParameter = json.get("action").getAsString();

                if(actionParameter.equals("get_resource")){
                    String path = json.get("resource_path").getAsString();

                    DynamicResourceItem dynamicResourceItem =
                            DynamicResourcesService.getInstance().getDynamicResource(path);

                    Map<String, Object> resource = new HashMap<>();
                    resource.put("name", dynamicResourceItem.getPath());
                    resource.put("method", dynamicResourceItem.getMethod().toString().toLowerCase());
                    resource.put("strategy", dynamicResourceItem.getStrategy().toString().toLowerCase());
                    resource.put("script", dynamicResourceItem.getScript());

                    DynamicResponseItem responseItem = dynamicResourceItem.getDefaultResponse();
                    if(responseItem != null){
                        resource.put("default_response_id", responseItem.getId());
                        resource.put("default_response_name", responseItem.getName());
                    }

                    response.getWriter().println(new Gson().toJson(resource));
                }
                else if(actionParameter.equals("create_resource")){
                    String path = json.get("name").getAsString();
                    RestMethods method = RestMethods.parseString(json.get("method").getAsString());
                    Strategy strategy = Strategy.parseString(json.get("strategy").getAsString());
                    String script = json.get("script").getAsString();

                    DynamicResourceItem item = new DynamicResourceItem(path, method, strategy);

                    item.setScript(script);

                    DynamicResourcesService.getInstance().addDynamicResource(item);
                }
                else if(actionParameter.equals("update_resource")){
                    long id = json.get("id").getAsLong();
                    String path = json.get("name").getAsString();
                    RestMethods method = RestMethods.parseString(json.get("method").getAsString());
                    Strategy strategy = Strategy.parseString(json.get("strategy").getAsString());
                    String script = json.get("script").getAsString();
                    long default_resp_id = json.get("default_resp").getAsLong();

                    DynamicResourceItem item = DynamicResourcesService.getInstance().getDynamicResource(id);
                    item.setPath(path);
                    item.setMethod(method);
                    item.setStrategy(strategy);
                    item.setScript(script);
                    item.setDefaultResponse(default_resp_id);

                    DynamicResourcesService.getInstance().updateDynamicResource(item);
                }
                else if(actionParameter.equals("get_responses")){
                    Map<Long, String> responseList = new HashMap<>();

                    long resourceId = json.get("resource_id").getAsLong();
                    DynamicResourceItem item = DynamicResourcesService.getInstance().getDynamicResource(resourceId);
                    for(DynamicResponseItem it : item.getResponses()){
                        responseList.put(it.getId(), it.getName());
                    }

                    response.getWriter().println(new Gson().toJson(responseList));
                }
                else if(actionParameter.equals("create_response")){
                    long resourceId = json.get("resource_id").getAsLong();
                    String name = json.get("name").getAsString();
                    int code = json.get("code").getAsInt();
                    String content = json.get("content").getAsString();
                    String script = json.get("script").getAsString();

                    DynamicResponseItem dynamicResponse = new DynamicResponseItem(name);
                    dynamicResponse.setResponseCode(code);
                    dynamicResponse.setContent(content.getBytes());
                    dynamicResponse.setScript(script);

                    JsonElement element = json.get("headers");
                    if(!element.getClass().equals(JsonNull.class)){
                        JsonArray arr = element.getAsJsonArray();
                        for(int i = 0; i < arr.size(); i++){
                            String j_header = arr.get(i).getAsString();
                            dynamicResponse.addHeader(j_header.split(":")[0], j_header.split(":")[1]);
                        }
                    }

                    DynamicResourcesService.getInstance().addResponseToResource(resourceId, dynamicResponse);
                }
            }
            catch (NullPointerException e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            catch (Exception e){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            response.setHeader("X-Action-Code", "200");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private Map<String, Object> getDefaultTemplaterData(Map<String, Object> pageParams){
        pageParams.put("user", userProfile);
        pageParams.put("role", "default"); //TODO ROLES
        return pageParams;
    }

    private void displayDefaultAdminPage(HttpServletResponse response) throws IOException{
        contentParams.put("title", "Admin Console");
        contentParams.put("datetime", new Date().toString());

        response.getWriter().println(PageGenerator.getInstance().getAuthorizedAdminPage("welcome.html"
                , getDefaultTemplaterData(contentParams)));
    }
}
