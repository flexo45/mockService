package com.temafon.qa.mock.servlets.jms;

import com.google.gson.Gson;
import com.temafon.qa.mock.service.managedpool.JmsConnectionPool;
import com.temafon.qa.mock.service.managedpool.JmsPoolManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JmsPoolMonitorServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Map<String, Object>> pools = new HashMap<>(JmsConnectionPool.getInstance().getJmsPoolMap());

        Gson gson = new Gson();

        List<PoolObject> poolData = new ArrayList<>();

        for(Map.Entry<String, Map<String, Object>> pool : pools.entrySet()){
            PoolObject poolObject = new PoolObject();
            poolObject.server = pool.getKey();
            poolObject.use_conn = JmsConnectionPool.getInstance().getUseConnections(pool.getValue()).size();
            poolObject.free_conn = JmsConnectionPool.getInstance().getFreeConnections(pool.getValue()).size();
            poolObject.last_use = JmsConnectionPool.getInstance().getLastDate(pool.getValue()).toString();
            poolData.add(poolObject);
        }

        String resp_text = "{\"pool_manager_last_action\":\"" + JmsPoolManager.getLastActions()
                + "\", \"pool_status\":" + gson.toJson(poolData) + "}";

        response.getWriter().println(resp_text);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    private class PoolObject {

        public String server;
        public Integer free_conn;
        public Integer use_conn;
        public String last_use;
    }
}
