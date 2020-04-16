package edu.quinnipiac.ser210.wordcrunch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TargetWordHandler {
    public String getWord(String translationJsonStr) throws JSONException {
        JSONObject translationJsonObj = new JSONObject(translationJsonStr);
        JSONArray translationJsonArr = translationJsonObj.getJSONArray("outputs");
        JSONObject translationJsonObj2 = translationJsonArr.getJSONObject(0);
        //System.out.println("translationJsonObj2: name " + translationJsonObj2.getString("output"));
        return translationJsonObj2.getString("output");




    }
}
