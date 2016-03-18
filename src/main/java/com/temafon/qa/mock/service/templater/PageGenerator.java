package com.temafon.qa.mock.service.templater;

import com.temafon.qa.mock.service.accounts.UserProfile;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResourceItem;
import com.temafon.qa.mock.service.dynamicresources.DynamicResourcesService;
import com.temafon.qa.mock.servlets.administration.AdminServlet;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class PageGenerator {

    private static final String HTML_DIR = "mockservice" + File.separator + "templates";
    private static final String ELEMENT_DIR = HTML_DIR + File.separator + "elements";
    private static final String CONTENT_DIR = HTML_DIR + File.separator + "contents";
    private static final String MAIN_PAGE = "main_page.html";
    private static final String ADMIN_CONSOLE = "admin_console.html";
    private static final String SIGN_IN = "auth_form.html";

    private static PageGenerator pageGenerator;
    private final Configuration cfg;

    public static PageGenerator getInstance() {
        if (pageGenerator == null)
            pageGenerator = new PageGenerator();
        return pageGenerator;
    }

    public String getPage(String pageName, Map<String, Object> data) {

        return getHtmlPage(pageName, data);

    }

    public String getUnAuthorizedAdminPage(Map<String, Object> data){

        Map<String, Object> pageData = new HashMap<>();

        pageData.put("title", data.get("title"));
        pageData.put("content", getContent(SIGN_IN, data));

        return getHtmlPage(MAIN_PAGE, pageData);
    }

    public String getAuthorizedAdminPage(String contentName, Map<String, Object> data) {

        Map<String, Object> consoleData = new HashMap<>();
        Map<String, Object> pageData = new HashMap<>();

        for (Map.Entry<String, Object> it : data.entrySet()) {
            if(it.getKey().equals("user")){
                consoleData.put("auth_block", getAuthBlock((UserProfile) it.getValue()));
            }
            else if(it.getKey().equals("role")){
                consoleData.put("menu", getMainMenu());
            }
        }

        consoleData.put("group_content", getContent(contentName, data));

        pageData.put("title", data.get("title"));
        pageData.put("content", getContent(ADMIN_CONSOLE, consoleData));

        return getHtmlPage(MAIN_PAGE, pageData);
    }

    private PageGenerator() {
        cfg = new Configuration();
    }

    private String getElement(String elementName, Map<String, Object> content){
        Writer stream = new StringWriter();
        try {
            Template template = cfg.getTemplate(ELEMENT_DIR + File.separator + elementName);
            template.process(content, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }

    private String getContent(String contentName, Map<String, Object> content){
        Writer stream = new StringWriter();
        try {
            Template template = cfg.getTemplate(CONTENT_DIR + File.separator + contentName);
            template.process(content, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }

    private String getHtmlPage(String pageName, Map<String, Object> content){
        Writer stream = new StringWriter();
        try {
            Template template = cfg.getTemplate(HTML_DIR + File.separator + pageName);
            template.process(content, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }

    private Map<String, String> getMenuItems(){
        Map<String, String> menu_items = new HashMap<>();
        menu_items.put("Index", AdminServlet.path);
        menu_items.put("Dynamic resources", AdminServlet.path + "?page=dynamic_resources");
        return menu_items;
    }

    private String getMainMenu(){
        Map<String, Object> menu = new HashMap<>();
        String main_menu = "";
        for(Map.Entry<String, String> it : getMenuItems().entrySet()){
            main_menu = main_menu + "<li><a href=\"" + it.getValue() + "\">" + it.getKey() + "</a></li>";
        }
        menu.put("main_menu", main_menu);
        return getElement("main_menu.html", menu);
    }

    private String getAuthBlock(UserProfile userProfile){
        Map<String, Object> auth = new HashMap<>();
        auth.put("userName", userProfile.getLogin());
        return getElement("auth_block.html", auth);
    }

    public String getDynamicPageContent(){

        StringBuilder data = new StringBuilder();

        for(DynamicResourceItem resourceItem : DynamicResourcesService.getInstance().getDynamicResourceItemList()){
            String row = "<tr>\n" +
                    "<td><a href=\"" + AdminServlet.path + "?page=resource&path=" + resourceItem.getPath() + "\">"
                    + resourceItem.getMethod() + " " + resourceItem.getPath() + "</a></td>\n" +
                    "<td>" + resourceItem.getStrategy() + "</td>\n" +
                    "<td>" + "</td>\n" +
                    "</tr>";
            data.append(row);
        }

        return data.toString();
    }
}
