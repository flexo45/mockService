package com.temafon.qa.mock.service.managedpool;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.QueueConnectionFactory;
import javax.jms.JMSException;

import javax.jms.Connection;
import java.util.*;

public class JmsConnectionPool {

    ///Singleton///

    private static JmsConnectionPool instance;

    public static JmsConnectionPool getInstance(){
        if(instance == null) { instance = new JmsConnectionPool(); }
        return instance;
    }

    ///Private constructor///

    private JmsConnectionPool(){

        new Thread(new JmsPoolManager()).start();

    }

    ///Properties///

    private final Integer MAX_CONNECTIONS = 5;

    private final Integer WAIT_FREE_CONNECTION_TIMEOUT = 30;

    private static final String FREE_CONNECTION_FIELD = "free_connections";

    private static final String USE_CONNECTION_FIELD = "use_connections";

    private static final String Q_FACTORY_FIELD = "queue_factory";

    private static final String LAST_USING = "last_using";

    private Integer connectionID = 0;

    private Connection connection;

    private volatile Map<String, String> connectionIDtoServer = new HashMap<>();

    private volatile Map<String, Map<String, Object>> jmsPoolMap = new HashMap<>();

    ///Methods///

    public Map<String, Map<String, Object>> getJmsPoolMap(){
        return jmsPoolMap;
    }

    public synchronized List<Connection> getFreeConnections(Map<String, Object> pool){
        return (List<Connection>)pool.get(FREE_CONNECTION_FIELD);
    }

    public synchronized List<Connection> getUseConnections(Map<String, Object> pool){
        return (List<Connection>)pool.get(USE_CONNECTION_FIELD);
    }

    public synchronized Date getLastDate(Map<String, Object> pool){
        return (Date)pool.get(LAST_USING);
    }

    public synchronized void freeResource(String server) throws JMSException {
        Map<String, Object> pool = jmsPoolMap.get(server);

        if(pool == null){ throw new JMSException("Error on free resource, pool=" + server + " not exist\r\n"); }

        List<Connection> freeConnections = (List<Connection>)pool.get(FREE_CONNECTION_FIELD);
        List<Connection> useConnections = (List<Connection>)pool.get(USE_CONNECTION_FIELD);

        for(Connection conn : freeConnections){
            conn.close();
        }

        for(Connection conn : useConnections){
            conn.close();
        }

        jmsPoolMap.remove(server);
    }

    public synchronized Connection getFreeConnection(String server) throws JMSException{

        Map<String, Object> pool = getJmsPool(server);

        List<Connection> freeConnections = (List<Connection>)pool.get(FREE_CONNECTION_FIELD);
        List<Connection> useConnections = (List<Connection>)pool.get(USE_CONNECTION_FIELD);
        QueueConnectionFactory factory = (QueueConnectionFactory)pool.get(Q_FACTORY_FIELD);

        if(freeConnections.isEmpty() && useConnections.size() <= MAX_CONNECTIONS){
            try {
                connection = factory.createConnection();
            }
            catch (Exception e){
                throw new JMSException(e.getMessage());
            }

            connection.setClientID((++connectionID).toString());
            connectionIDtoServer.put(connectionID.toString(), server);
            freeConnections.add(connection);
        }
        else {
            Integer timeout = WAIT_FREE_CONNECTION_TIMEOUT;

            while (freeConnections.isEmpty()){
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e){
                    throw new JMSException("Thread throw InterruptedException, while sleep");
                }
                timeout--;
                if(timeout < 0){
                    throw new JMSException("Get free connection timeout");
                }
            }
        }

        connection = freeConnections.iterator().next();
        freeConnections.remove(connection);
        useConnections.add(connection);
        pool.put(LAST_USING, new Date());

        return connection;
    }

    public synchronized void closeConnection(Connection connection) throws JMSException{
        String server = connectionIDtoServer.get(connection.getClientID());
        if(server == null){
            connection.close();
            throw new JMSException("Connection with id=" + connection.getClientID() + " not found");
        }

        Map<String, Object> pool = getJmsPool(server);
        List<Connection> freeConnections = (List<Connection>)pool.get(FREE_CONNECTION_FIELD);
        List<Connection> useConnections = (List<Connection>)pool.get(USE_CONNECTION_FIELD);

        useConnections.remove(connection);
        freeConnections.add(connection);
    }

    private void createJmsPool(String server) throws JMSException{
        QueueConnectionFactory queueConnectionFactory = new QueueConnectionFactory();
        queueConnectionFactory.setProperty(ConnectionConfiguration.imqAddressList, server);

        Map<String, Object> jmsPool = new HashMap<>();

        jmsPool.put(Q_FACTORY_FIELD, queueConnectionFactory);
        jmsPool.put(FREE_CONNECTION_FIELD, new ArrayList<Connection>());
        jmsPool.put(USE_CONNECTION_FIELD, new ArrayList<Connection>());
        jmsPool.put(LAST_USING, new Date());

        jmsPoolMap.put(server, jmsPool);
    }

    private Map<String, Object> getJmsPool(String server) throws JMSException{

        Map<String, Object> pool = jmsPoolMap.get(server);
        if(pool == null){
            createJmsPool(server);
        }

        return jmsPoolMap.get(server);
    }

}
