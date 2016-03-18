package com.temafon.qa.mock.service.jms;

import com.sun.messaging.Destination;
import com.temafon.qa.mock.service.managedpool.JmsConnectionPool;

import javax.jms.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JMSManager {

    Connection connection;
    Session session;
    Destination destination;
    MessageProducer messageProducer;
    Message message;
    Map<String, String> stringProperties = new HashMap<>();

    public void addStringProperties(String key, String value){
        stringProperties.put(key, value);
    }

    public void sendMessage(String server, String queue, Object object) throws JMSException{

        JMSException exception = null;

        connection = JmsConnectionPool.getInstance().getFreeConnection(server);
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = new com.sun.messaging.Queue(queue);

        if(object.getClass() == String.class){
            message = session.createTextMessage((String)object);
        }
        else{
            try {
                message = session.createObjectMessage((Serializable) object);
            }
            catch (ClassCastException e){
                throw new JMSException("Not supported Object: {" + object.getClass().toString() + "}\r\n" + e.toString());
            }
        }

        if(stringProperties != null){
            for(Map.Entry<String, String> property : stringProperties.entrySet()){
                message.setStringProperty(property.getKey(), property.getValue());
            }
        }

        try {
            messageProducer = session.createProducer(destination);

            messageProducer.send(message);
        }
        catch (JMSException e){
            exception = e;
        }
        finally {
            messageProducer.close();
            JmsConnectionPool.getInstance().closeConnection(connection);
        }

        if(exception != null){ throw new JMSException(exception.getMessage()); }
    }

}
