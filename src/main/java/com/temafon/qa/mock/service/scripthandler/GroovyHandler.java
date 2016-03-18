package com.temafon.qa.mock.service.scripthandler;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import javax.servlet.http.HttpServletRequest;

public class GroovyHandler {

    public static String executeDispatchScript(String script, HttpServletRequest request){
        Binding binding = new Binding();
        binding.setVariable("request", request);
        GroovyShell groovyShell = new GroovyShell(binding);
        Object result = groovyShell.evaluate(script);
        return result.toString();
    }

}
