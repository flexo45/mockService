package com.temafon.qa.mock.service.managedpool;

import com.temafon.qa.mock.service.config.ConfigManager;

import javax.jms.JMSException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JmsPoolManager implements Runnable {

    private boolean stop = false;

    private static String lastActions = "";

    public static String getLastActions(){
        return lastActions;
    }

    public static void setLastAction(String text){
        lastActions = text;
    }

    @Override
    public void run() {

        setLastAction("Run at: " + new Date());

        while (!stop){
            try {
                Thread.sleep(ConfigManager.JMS_MANAGER_SLEEP_TIMEOUT);
            }
            catch (Exception e){
                setLastAction(new Date() + "\r\nError on sleep: " + e.getMessage());
            }

            Map<String, Map<String, Object>> pools = new HashMap<>(JmsConnectionPool.getInstance().getJmsPoolMap());

            setLastAction("Last inspection: " + new Date());

            for(Map.Entry<String, Map<String, Object>> pool : pools.entrySet()){

                Date date = JmsConnectionPool.getInstance().getLastDate(pool.getValue());

                if((new Date().getTime() - date.getTime()) > ConfigManager.JMS_MANAGER_POOL_LIFETIME){
                    try {
                        JmsConnectionPool.getInstance().freeResource(pool.getKey());
                        setLastAction(new Date() + "\r\nClean up pool: " + pool.getKey());
                    }
                    catch (JMSException e){
                        e.printStackTrace();
                        setLastAction(new Date() + "\r\nError on clean up pool: " + pool.getKey());
                    }
                }
            }
        }
    }
}
