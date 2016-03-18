package com.temafon.qa.mock.service.authorizarion;

import org.apache.commons.codec.binary.Base64;

public class AuthorizationManager {
    private static final String LOGIN = "tema";

    private static final String PASSWORD = "tema";

    public static Boolean verifyAuthorizationCode(String code){

        if(code.contains("Basic ")){
            code = code.replace("Basic ", "");
        }
        else { return false; }

        byte[] bytes = Base64.decodeBase64(code.getBytes());

        String text = new String(bytes);

        String[] authData = text.replace("Basic ", "").split(":");

        if(authData[0].equals(LOGIN) && authData[1].equals(PASSWORD)){
            return true;
        }

        return false;
    }
}
