package edu.quinnipiac.ser210.wordcrunch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TargetDefinitionHandler {
    public String getDefinition(String wordJsonStr)throws JSONException {
        JSONArray wordJsonArr = new JSONArray(wordJsonStr);
        JSONObject wordJsonObj = wordJsonArr.getJSONObject(0);
        return wordJsonObj.getString("text");
    }
}
