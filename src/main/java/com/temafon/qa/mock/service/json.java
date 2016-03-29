package com.temafon.qa.mock.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;

public class Json {

    public static JsonObject getJson(BufferedReader reader) throws IOException{

        StringBuilder content = new StringBuilder();
        String line = "";

        try {
            while ((line = reader.readLine()) != null){
                content.append(line);
            }
        }
        catch (IOException e){
            return  null;
        }
        finally {
            reader.close();
        }

        JsonElement element = new JsonParser().parse(content.toString());

        return element.getAsJsonObject();
    }
}
