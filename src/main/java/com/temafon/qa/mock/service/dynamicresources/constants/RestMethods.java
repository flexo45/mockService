package com.temafon.qa.mock.service.dynamicresources.constants;

public enum RestMethods {

    GET, POST;

    public static RestMethods parseString(String s){
        switch (s.toLowerCase()){
            case "get":
                return GET;
            case "post":
                return POST;
            default:
                return null;
        }
    }
}
