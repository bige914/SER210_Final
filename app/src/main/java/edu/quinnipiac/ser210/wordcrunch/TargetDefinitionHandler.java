package edu.quinnipiac.ser210.wordcrunch;
/**
 * TargetDefinitionHandler class, used to pull out a definition from JSON text that is needed for other
 * components of the application.
 *
 * @authors Ellsworth Evarts IV
 * @date 5/02/2020
 */
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
