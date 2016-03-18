package com.temafon.qa.mock.service.dynamicresources.constants;

public enum Strategy {

    SEQUENCE, RANDOM, SCRIPT;

    public Strategy parseString(String s){
        switch (s.toLowerCase()){
            case "sequence":
                return SEQUENCE;
            case "random":
                return RANDOM;
            case "script":
                return SCRIPT;
            default:
                return null;
        }
    }
}
