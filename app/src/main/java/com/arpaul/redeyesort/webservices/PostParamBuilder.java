package com.arpaul.redeyesort.webservices;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by Aritra on 05-08-2016.
 */
public class PostParamBuilder {

    public String prepareParam(LinkedHashMap<String, String> hashParam) {
        StringBuilder strBuilder = new StringBuilder();

        if(hashParam != null && hashParam.size() > 0) {
            Set<String> keyset = hashParam.keySet();
            strBuilder.append("?");
            int i = 0;
            for (String key : keyset) {
                strBuilder.append(key + "=" + hashParam.get(key));
                if(i < keyset.size() - 1)
                    strBuilder.append("&");
                i++;
            }
        }
        return strBuilder.toString();
    }
}
