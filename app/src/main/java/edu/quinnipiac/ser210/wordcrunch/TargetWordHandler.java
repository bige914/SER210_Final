package edu.quinnipiac.ser210.wordcrunch;

import android.content.ClipData;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TargetWordHandler {
    public String getWord(String translationJsonStr, int i) throws JSONException {
        JSONArray wordJsonArr = new JSONArray(translationJsonStr);
        JSONObject wordJsonObj = wordJsonArr.getJSONObject(i);
        return wordJsonObj.getString("word");

        //JSONObject translationJsonObj = new JSONObject(translationJsonStr);
        //JSONArray translationJsonArr = translationJsonObj.getJSONArray("outputs");
        //JSONObject translationJsonObj2 = translationJsonArr.getJSONObject(0);
        //System.out.println("translationJsonObj2: name " + translationJsonObj2.getString("output"));
       // return translationJsonObj2.getString("output");




    }
}
