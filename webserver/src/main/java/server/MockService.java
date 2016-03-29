package server;

import com.temafon.qa.mock.service.accounts.AccountService;
import com.temafon.qa.mock.service.accounts.UserProfile;
import com.temafon.qa.mock.service.data.DBException;
import com.temafon.qa.mock.service.data.DBManager;
import com.temafon.qa.mock.service.data.dataSet.DynamicResource;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResourceItem;
import com.temafon.qa.mock.service.dynamicresources.DynamicResourcesService;
import com.temafon.qa.mock.service.dynamicresources.constants.RestMethods;
import com.temafon.qa.mock.service.dynamicresources.constants.Strategy;
import com.temafon.qa.mock.service.managedpool.JmsPoolManager;
import com.temafon.qa.mock.service.scripthandler.GroovyHandler;
import com.temafon.qa.mock.servlets.administration.AdminServlet;
import com.temafon.qa.mock.servlets.administration.SignInServlet;
import com.temafon.qa.mock.servlets.administration.SignOutServlet;
import com.temafon.qa.mock.servlets.config.SetConfigServlet;
import com.temafon.qa.mock.servlets.dynamicresource.DynamicResourceDispatherServlet;
import com.temafon.qa.mock.servlets.echo.EchoServlet;
import com.temafon.qa.mock.servlets.jms.JmsPoolMonitorServlet;
import com.temafon.qa.mock.servlets.jms.morequest.MORequestServlet;
import com.temafon.qa.mock.servlets.jms.textmessage.TextMessageServlet;
import com.temafon.qa.mock.servlets.mail.SendMailServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class MockService {

    public static void main(String[] args) throws Exception {

        new Thread(new JmsPoolManager()).start();

        Server server = new Server(8080);

        ServletContextHandler servletContextHandler =
                new ServletContextHandler(ServletContextHandler.SESSIONS);

        server.setHandler(servletContextHandler);

        servletContextHandler.addServlet(new ServletHolder(new EchoServlet()), EchoServlet.path);

        servletContextHandler.addServlet(new ServletHolder(new AdminServlet()), AdminServlet.path);

        servletContextHandler.addServlet(new ServletHolder(new SignInServlet()), SignInServlet.path);

        servletContextHandler.addServlet(new ServletHolder(new SignOutServlet()), SignOutServlet.path);

        servletContextHandler.addServlet(new ServletHolder(new DynamicResourceDispatherServlet()), DynamicResourceDispatherServlet.path);

        servletContextHandler.addServlet(new ServletHolder(new SetConfigServlet()), "/config");

        servletContextHandler.addServlet(new ServletHolder(new JmsPoolMonitorServlet()), "/jmspool");

        servletContextHandler.addServlet(new ServletHolder(new TextMessageServlet()), "/textmessage");

        servletContextHandler.addServlet(new ServletHolder(new MORequestServlet()), "/mo");

        servletContextHandler.addServlet(new ServletHolder(new SendMailServlet()), "/sendmail");

        //loadDefaultData();

        server.start();

        server.join();
    }

    private static void loadDefaultData(){

        AccountService.getInstance().addNewUser(new UserProfile("admin", "admin", "admin@local.host"));

        try {

            DBManager.getInstance().getDynamicResourceDataManager().addMethod(RestMethods.GET.toString());
            DBManager.getInstance().getDynamicResourceDataManager().addMethod(RestMethods.POST.toString());

            DBManager.getInstance().getDynamicResourceDataManager().addStrategy(Strategy.SCRIPT.toString());
            DBManager.getInstance().getDynamicResourceDataManager().addStrategy(Strategy.SEQUENCE.toString());
            DBManager.getInstance().getDynamicResourceDataManager().addStrategy(Strategy.RANDOM.toString());

        }
        catch (DBException e){
            e.printStackTrace();
        }

        DynamicResourcesService.getInstance().addDynamicResource(new DynamicResourceItem("testres", RestMethods.GET, Strategy.SCRIPT));
        DynamicResourcesService.getInstance().addDynamicResource(new DynamicResourceItem("notify", RestMethods.GET, Strategy.SCRIPT));
        DynamicResourcesService.getInstance().addDynamicResource(new DynamicResourceItem("simple_service", RestMethods.POST, Strategy.SCRIPT));
    }

}
