package com.temafon.qa.mock.service.templater;

import com.temafon.qa.mock.service.accounts.UserProfile;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResourceItem;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResponseItem;

import java.util.HashMap;
import java.util.Map;

public class DynamicResourcePages {

    public static String getPage(UserProfile userProfile, DynamicResourceItem dynamicResourceItem){

        Map<String, Object> resource_view_data = getResourceViewData(dynamicResourceItem);

        String group_content = PageGenerator.getInstance().getContent("resource_view.html", resource_view_data);

        String content = PageGenerator.getInstance().getAdminConsole(userProfile, group_content);

        return PageGenerator.getInstance().getMainPage(content, "Dynamic resource - " + dynamicResourceItem.getPath());
    }

    public static String getEditResourcePage(UserProfile userProfile, DynamicResourceItem dynamicResourceItem){
        Map<String, Object> resource_edit_data = getResourceViewData(dynamicResourceItem);

        String group_content = PageGenerator.getInstance().getContent("edit_resource.html", resource_edit_data);

        String content = PageGenerator.getInstance().getAdminConsole(userProfile, group_content);

        return PageGenerator.getInstance().getMainPage(content, "Edit dynamic resource - " + dynamicResourceItem.getPath());
    }

    private static Map<String, Object> getEditResourceData(DynamicResourceItem dynamicResourceItem){
        Map<String, Object> data = new HashMap<>();

        data.put("path", dynamicResourceItem.getPath());
        data.put("resID", dynamicResourceItem.getId());

        return data;
    }

    private static Map<String, Object> getResourceViewData(DynamicResourceItem dynamicResourceItem){
        Map<String, Object> data = new HashMap<>();

        data.put("resID", dynamicResourceItem.getId());
        data.put("method", dynamicResourceItem.getMethod().toString());
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

        data.put("default_response", dynamicResourceItem.getDefaultResponse() == null
                ? "Default response not found!" : dynamicResourceItem.getDefaultResponse().getName());

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
