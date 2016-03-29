package com.temafon.qa.mock.service.templater;

import com.temafon.qa.mock.service.accounts.UserProfile;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResourceItem;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResponseItem;

import java.util.HashMap;
import java.util.Map;

public class DynamicResponsePages {

    public static String getAddResponsePage(UserProfile userProfile, long resourceId, String resourcePath){

        Map<String, Object> content_data = new HashMap<>();
        content_data.put("path", resourcePath);
        content_data.put("resID", resourceId);

        String group_content = PageGenerator.getInstance().getContent("create_response.html", content_data);

        String content = PageGenerator.getInstance().getAdminConsole(userProfile, group_content);

        return PageGenerator.getInstance().getMainPage(content, "Add response for dynamic resource - " + resourcePath);
    }

    public static String getPage(UserProfile userProfile, DynamicResourceItem dynamicResourceItem){

        Map<String, Object> resource_view_data = getResourceViewData(dynamicResourceItem);

        String group_content = PageGenerator.getInstance().getContent("resource_view.html", resource_view_data);

        String content = PageGenerator.getInstance().getAdminConsole(userProfile, group_content);

        return PageGenerator.getInstance().getMainPage(content, "Dynamic resource - " + dynamicResourceItem.getPath());
    }

    private static Map<String, Object> getResourceViewData(DynamicResourceItem dynamicResourceItem){
        Map<String, Object> data = new HashMap<>();

        data.put("method", dynamicResourceItem.getMethod());
        data.put("path", dynamicResourceItem.getPath());

        String strategy = dynamicResourceItem.getStrategy().toString();
        String strategy_impl = "";

        data.put("strategy", strategy);

        switch (strategy.toUpperCase()){
            case "SCRIPT":
                strategy_impl = dynamicResourceItem.getScript() == null ? "Script not found!" : dynamicResourceItem.getScript();
                break;
            case "SEQUENCE":
                strategy_impl = "Sequential search the responses";
                break;
            case "RANDOM":
                strategy_impl = "Response randomly selected";
                break;
            default:
                strategy_impl = "Unknown";
                break;
        }

        data.put("str_impl", strategy_impl);

        data.put("default_response", dynamicResourceItem.getDefaultResponse() == null ? "Default response not found!" : dynamicResourceItem.getDefaultResponse());

        data.put("responses", getResponseList(dynamicResourceItem));

        return data;
    }

    private static String getResponseList(DynamicResourceItem dynamicResourceItem){

        String responses = "";

        for(DynamicResponseItem response : dynamicResourceItem.getResponses()){

            String headers = "";

            Map<String, Object> response_data = new HashMap<>();

            for(Map.Entry<String, String> header : response.getHeaders().entrySet()){

                Map<String, Object> header_data = new HashMap<>();

                header_data.put("header", header.getKey() + ": " + header.getValue());

                headers = headers + PageGenerator.getInstance().getElement("response_header.html", header_data);

            }

            response_data.put("name", response.getName());

            response_data.put("code", response.getResponseCode());

            response_data.put("headers", headers);

            responses = responses + PageGenerator.getInstance().getElement("response.html", response_data);

        }

        return responses;

    }

}
