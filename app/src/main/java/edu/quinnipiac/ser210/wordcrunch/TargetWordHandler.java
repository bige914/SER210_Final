package edu.quinnipiac.ser210.wordcrunch;
/**
 * TargetWordHandler class, used to pull out a word from JSON text that is needed for other
 * components of the application.
 *
 * @authors Ellsworth Evarts IV
 * @date 5/02/2020
 */
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
