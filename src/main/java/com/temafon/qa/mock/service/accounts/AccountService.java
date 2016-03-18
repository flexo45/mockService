package com.temafon.qa.mock.service.accounts;

import com.temafon.qa.mock.service.data.DBException;
import com.temafon.qa.mock.service.data.DBManager;
import com.temafon.qa.mock.service.data.dataSet.User;

import java.util.HashMap;
import java.util.Map;

public class AccountService {

    private static AccountService instance;

    public static AccountService getInstance(){
        if(instance == null){
            instance = new AccountService();
        }
        return instance;
    }

    private AccountService(){
        sessionIdToProfile = new HashMap<>();
    }

    private final Map<String, UserProfile> sessionIdToProfile;

    public void addNewUser(UserProfile userProfile) {
        try {
            DBManager.getInstance().getUserDataManager()
                    .addUser(userProfile.getLogin(), userProfile.getPass(), userProfile.getEmail());
        }
        catch (DBException ex){
            ex.printStackTrace();
        }
    }

    public UserProfile getUserByLogin(String login) {

        try {
            User user = DBManager.getInstance().getUserDataManager().getUser(login);
            if(user != null){
                return new UserProfile(user.getLogin(), user.getPassword(), user.getEmail());
            }
        }
        catch (DBException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public UserProfile getUserBySessionId(String sessionId) {
        return sessionIdToProfile.get(sessionId);
    }

    public void addSession(String sessionId, UserProfile userProfile) {
        sessionIdToProfile.put(sessionId, userProfile);
    }

    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }
}
