package com.temafon.qa.mock.servlets.dynamicresource;

import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResourceItem;
import com.temafon.qa.mock.service.dynamicresources.DynamicResourcesService;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResponseItem;
import com.temafon.qa.mock.service.scripthandler.GroovyHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

public class DynamicResourceDispatherServlet extends HttpServlet {

    public static final String path = "/dynamic/*";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getRequestURL().toString().replace("http://"
                + request.getServerName()
                + (request.getServerPort() == 0 ? "" : ":"+request.getServerPort())
                + request.getServletPath() + "/"
                , "");

        DynamicResourceItem resourceItem = DynamicResourcesService.getInstance().getDynamicResource(path);

        resourceItem.setScript("return 'test1'");

        DynamicResponseItem dynamicResponse1 = new DynamicResponseItem("test1");

        dynamicResponse1.addHeader("x-status-code", "200");
        dynamicResponse1.addHeader("x-status-text", "OK");

        resourceItem.addResponse(dynamicResponse1);

        switch (resourceItem.getStrategy()){
            case SEQUENCE:
                resourceItem.getNextResponse().processResponse(response);
                break;
            case RANDOM:
                int rnd_idx = new Random().nextInt(resourceItem.getResponses().size() - 1);
                resourceItem.getResponses().get(rnd_idx).processResponse(response);
                break;
            case SCRIPT:
                try {
                    String responseName = GroovyHandler.executeDispatchScript(resourceItem.getScript(), request);

                    for (DynamicResponseItem dynamicResponse : resourceItem.getResponses()){
                        if(dynamicResponse.getName().equals(responseName)){
                            dynamicResponse.processResponse(response);
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    resourceItem.getDefaultResponse().processResponse(response);
                }


                break;
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
