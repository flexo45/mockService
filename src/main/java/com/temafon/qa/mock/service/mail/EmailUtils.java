package com.temafon.qa.mock.service.mail;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailUtils {

    private static EmailUtils instance;

    public static EmailUtils getInstance(){
        if(instance == null) instance = new EmailUtils();
        instance.loadDefaultValues();
        return instance;
    }

    private EmailUtils(){ }

    private void loadDefaultValues(){
        FROM_ADDRES = "no_reply@temamedia.ru";
        FROM_PERSON = "TemaMedia QA";
        SERVER = "post.temamedia.ru";
    }

    private String FROM_ADDRES;
    private String FROM_PERSON;
    private String SERVER;



    public void sendEmailCustom(String to, String subject, String message, String server) throws Exception{

        SERVER = server;

        sendEmailDefaultConfig(to, subject, message);
    }

    public void sendEmailCustom(String to, String subject, String message
            , String fromAddres, String fromPerson) throws Exception{

        FROM_ADDRES = fromAddres;
        FROM_PERSON = fromPerson;

        sendEmailDefaultConfig(to, subject, message);
    }

    public void sendEmailCustom(String to, String subject, String message, String server
            , String fromAddres, String fromPerson) throws Exception{

        FROM_ADDRES = fromAddres;
        FROM_PERSON = fromPerson;
        SERVER = server;

        sendEmailDefaultConfig(to, subject, message);
    }

    public void sendEmailDefaultConfig(String to, String subject, String message) throws Exception
    {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", SERVER);

        Session session = Session.getInstance(properties);

        MimeMessage msg = new MimeMessage(session);
        /*set message headers*/
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");

        msg.setFrom(new InternetAddress(FROM_ADDRES, FROM_PERSON));

        msg.setReplyTo(InternetAddress.parse(FROM_ADDRES, false));

        msg.setSubject(subject, "UTF-8");

        msg.setText(message, "UTF-8");

        msg.setSentDate(new Date());

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

        Transport.send(msg);
    }
}
