package edu.quinnipiac.ser210.wordcrunch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TargetWordHandler {
    public String getWord(String wordJsonStr, int i) throws JSONException {
        JSONArray wordJsonArr = new JSONArray(wordJsonStr);
        JSONObject wordJsonObj = wordJsonArr.getJSONObject(i);
        return wordJsonObj.getString("word");
    }
}
