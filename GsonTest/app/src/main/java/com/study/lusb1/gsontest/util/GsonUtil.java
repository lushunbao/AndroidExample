package com.study.lusb1.gsontest.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lusb1 on 2017/3/1.
 */

public class GsonUtil {
    public static <T> T buildClassFromJson(Reader json,Class<T> type){
        Gson gson = new Gson();
        T res = gson.fromJson(json,type);
        return res;
    }

    public static <T> List<T> buildListFromJson(Reader json, Class<T> type){
        List<T> res = new ArrayList<>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement rootElement = parser.parse(json);
        JsonArray jsonArray;
        if(rootElement.isJsonArray()){
            jsonArray = rootElement.getAsJsonArray();
            Iterator it = jsonArray.iterator();
            while(it.hasNext()){
                JsonElement element = (JsonElement) it.next();
                T t = gson.fromJson(element,type);
                res.add(t);
            }
        }
        else{
            res.add(gson.fromJson(json,type));
        }
        return res;
    }
}
