package edu.quinnipiac.ser210.wordcrunch;
/**
 * GameFragment fragment, accepts inputs from TextEdit, has buttons for answer submission and new game
 * game where user will get a word with a missing letter and must fill in with correct letter among 3 choices
 *
 * @authors Ellsworth Evarts IV
 * @date 5/02/2020
 */
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment implements View.OnClickListener{

    private NavController navController = null;

    private DatabaseHelper myDb;
    private Cursor cursor;

    private String[] easy_mode;
    private String[] medium_mode;
    private String[] hard_mode;

    private final static String[] ALPHABET = {"a","b","c","d","e","f","g","h","i","j",
                                              "k","l","m","n","o","p","q","r","s","t",
                                              "u","v","w","x","y","z"};

    private Random rand = new Random();
    private int rand_easy;
    private int rand_medium;
    private int rand_hard;
    private int rand_select;
    private int rand_alpha;
    private int rand_diff;

    private int num_correct;
    private int num_incorrect;

    private String complete_word = ""; //looks something like this ??a?
    private String new_word;//word on screen the player sees
    private String letters; //letters to be displayed with user_choices
    private String alpha;//the correct letter choice
    private String input;//the value from user_input EditText


    private TextView user_view;//the displayed word
    private TextView user_choices;// the displayed characters
    private TextView definition_view;

    private TextView local_correct; //displays current correct score locally
    private TextView local_incorrect; //displays current incorrect score locally

    private EditText user_input;//the letter the user types into the game, check value against 'alpha'



    private String baseUrl = "https://api.datamuse.com/words?sp=";
    private String maxWords = "&max=10";

    private String baseUrl1 = "https://api.wordnik.com/v4/word.json/";
    private String endUrl1 = "/definitions?limit=1&includeRelated=false&sourceDictionaries=webster&useCanonical=false&includeTags=false&api_key=";
    private String apiKey = ""; // get your key here: www.developer.wordnik.com
    private String definition; //full definition to be displayed
    private String[] definitionUrl = new String[4];

    private TargetWordHandler tWordHandler = new TargetWordHandler();
    private TargetDefinitionHandler tDefinitionHandler = new TargetDefinitionHandler();

    private String difficulty;

    public GameFragment()
    {
        //required empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = mPreferences.edit();

        myDb = new DatabaseHelper(getContext());
        cursor = myDb.getData();
        Log.d("DBDump ", DatabaseUtils.dumpCursorToString(cursor));

        difficulty = mPreferences.getString(getString(R.string.difficulty), "");
    }

    private void wordGenerator(){
        String[] user_letters = {"","",""};
        easy_mode = new String[]{"?","?","?","?"};
        medium_mode = new String[]{"?","?","?","?","?"};
        hard_mode = new String[]{"?","?","?","?","?","?"};


        int rand_letter_pos = rand.nextInt(3);

        rand_easy = rand.nextInt(4);
        rand_medium = rand.nextInt(5);
        rand_hard = rand.nextInt(6);
        rand_select = rand.nextInt(10);
        rand_alpha = rand.nextInt(26);
        alpha = ALPHABET[rand_alpha];
        Log.d("alpha ", alpha);

        user_letters[rand_letter_pos] = alpha;
        for (int i=0; i < user_letters.length; i++){
            if(user_letters[i].equals("")){
                user_letters[i] = ALPHABET[rand.nextInt(26)];
            }
        }
        easy_mode[rand_easy] = alpha;
        medium_mode[rand_medium] = alpha;
        hard_mode[rand_hard] = alpha;

        String[] diffLevel;
        switch (difficulty){
            case "easy": diffLevel = easy_mode; rand_diff = rand_easy;
            break;
            case "medium": diffLevel = medium_mode; rand_diff = rand_medium;
            break;
            case "hard": diffLevel = hard_mode; rand_diff = rand_hard;
                break;
            default: diffLevel = easy_mode;
        }



        StringBuilder builder1 = new StringBuilder();

        for (String s : diffLevel){
            builder1.append(s);
        }
        complete_word = builder1.toString();

        StringBuilder builder2 = new StringBuilder();
        for (String s : user_letters){
            builder2.append(s);
        }
        letters = builder2.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        wordGenerator();//generate word ??a? <-- this type of format
        new FetchWord().execute(complete_word);//send word for processing
        definitionUrl[0] = baseUrl1;
        definitionUrl[2] = endUrl1;
        definitionUrl[3] = apiKey;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        Button backButton = (Button) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        Button goButton = (Button) view.findViewById(R.id.go_button);
        goButton.setOnClickListener(this);
        Button resetButton = (Button) view.findViewById(R.id.new_word_button);
        resetButton.setOnClickListener(this);
        user_view = (TextView) view.findViewById(R.id.word);
        user_choices = (TextView) view.findViewById(R.id.generated_letters);
        user_input = (EditText) view.findViewById(R.id.missing_letter);
        local_correct = (TextView) view.findViewById(R.id.score_correct);
        local_incorrect = (TextView) view.findViewById(R.id.score_incorrect);
        definition_view = (TextView) view.findViewById(R.id.definition_tv);
    }

    public static void hideKeyboardFrom(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.back_button){
            navController.navigateUp();
        }
        if (v.getId() == R.id.go_button){
            hideKeyboardFrom(getContext(),v);
            Log.d("difficulty", difficulty);
            //send completed problem to be scored
            input = user_input.getText().toString();
            if (input.equals("")){
                Toast.makeText(getContext(), "Please select a letter.", Toast.LENGTH_SHORT).show();
            }
            else if (!input.equals(alpha)){
                num_incorrect+=1;
                String incorrectS = Integer.toString(num_incorrect);
                local_incorrect.setText(incorrectS);
                Toast.makeText(getContext(), "Oops wrong answer!", Toast.LENGTH_SHORT).show();
            }
            else {
                num_correct+=1;
                String correctS = Integer.toString(num_correct);
                local_correct.setText(correctS);
                Toast.makeText(getContext(), "GOOD JOB!", Toast.LENGTH_SHORT).show();
                wordGenerator();//generate word ??a? <-- this type of format
                new FetchWord().execute(complete_word);//send word for processing
            }
        }
        if (v.getId() == R.id.new_word_button){
            wordGenerator();//generate word ??a? <-- this type of format
            new FetchWord().execute(complete_word);//send word for processing
        }
    }

    @Override
    public void onPause() {
        int total = num_correct+num_incorrect;
        Cursor data = myDb.getData();
        data.moveToFirst();
        if (data.getCount() == 0){
            Log.e("Database", "Database error nothing found");
        }
        else{
            int cur_correct = data.getInt(data.getColumnIndex(DatabaseHelper.COL_2));
            int new_correct = cur_correct+num_correct;
            int cur_incorrect = data.getInt(data.getColumnIndex(DatabaseHelper.COL_3));
            int new_incorrect = cur_incorrect+num_incorrect;
            int cur_total = data.getInt(data.getColumnIndex(DatabaseHelper.COL_4));
            int new_total = cur_total+total;

//data.getColumnIndex(DatabaseHelper.COL_1)
            data.moveToFirst();
            boolean updateData = myDb.updateData(1,new_correct, new_incorrect, new_total);
            if (updateData){
                Toast.makeText(getContext(), "Data inserted", Toast.LENGTH_SHORT).show();
                Log.d("DBDump ", DatabaseUtils.dumpCursorToString(cursor));
            }
        }
        super.onPause();
    }

    private class FetchWord extends AsyncTask<String,Void,String> { //generates a word for the game
        @Override
        protected String doInBackground(String... words) {
            HttpURLConnection urlConnection =null;
            String word = baseUrl + words[0] + maxWords;
            try{
                String tmp = word;

                URL url = new URL(tmp);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                if (urlConnection.getInputStream() == null) {
                    Log.e("no connection", "no connection");
                    return null;
                }
                word = getStringFromBuffer(
                        new BufferedReader(new InputStreamReader(urlConnection.getInputStream())));
                assert word != null;
                Log.d("word", word);
            }catch (Exception e){
                Log.e("GameDoInBackground","Error" + e.getMessage());
                return null;
            }finally {
                if(urlConnection !=null)
                    urlConnection.disconnect();
            }

            return word;
        }

        private String getStringFromBuffer(BufferedReader bufferedReader) throws Exception{
            StringBuffer buffer = new StringBuffer();
            String line;

            while((line = bufferedReader.readLine()) != null){
                buffer.append(line + '\n');
            }
            if (bufferedReader!=null){
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    Log.e("GameFragment","Error" + e.getMessage());
                    return null;
                }
            }
            Log.d("word", buffer.toString());
            return  tWordHandler.getWord(buffer.toString(), rand_select);
        }

        @Override
        protected void onPostExecute(String result) {

            if(result != null){
                Log.d("onPostExecute",result);
                new_word = result;
                definitionUrl[1] = result; // set the word value into the Definition String array
                char[] tmp = new_word.toCharArray();

                tmp[rand_diff] = '_';
                new_word = String.valueOf(tmp);
                Log.d("new_word", new_word);

                user_view.setText(new_word);
                user_choices.setText(letters);

                user_input.getText().clear();
                new FetchDefinition().execute(definitionUrl);//make a call to get definition for word
            }else
                Log.d("onPostExecute","null");
        }
    }

    private class FetchDefinition extends AsyncTask<String,Void,String> { //generate a definition with given word
        @Override
        protected String doInBackground(String... words) {
            HttpURLConnection urlConnection =null;
            String definitionUrl = words[0] + words[1] + words[2] + words[3];

            try{
                String tmp = definitionUrl;

                URL url = new URL(tmp);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                if (urlConnection.getInputStream() == null) {
                    Log.e("no connection", "no connection");
                    return null;
                }
                definitionUrl = getStringFromBuffer(
                        new BufferedReader(new InputStreamReader(urlConnection.getInputStream())));
                assert definitionUrl != null;
                Log.d("definitionUrl", definitionUrl);
            }catch (Exception e){
                Log.e("GameDoInBackground","Error" + e.getMessage());
                return null;
            }finally {
                if(urlConnection !=null)
                    urlConnection.disconnect();
            }
            return definitionUrl;
        }

        private String getStringFromBuffer(BufferedReader bufferedReader) throws Exception{
            StringBuffer buffer = new StringBuffer();
            String line;

            while((line = bufferedReader.readLine()) != null){
                buffer.append(line + '\n');
            }
            if (bufferedReader!=null){
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    Log.e("GameFragment","Error" + e.getMessage());
                    return null;
                }
            }
            Log.d("word", buffer.toString());
            return  tDefinitionHandler.getDefinition(buffer.toString());
        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null){
                Log.d("onPostExecute",result);
                definition = result;
                definition_view.setText(definition);
            }else
                Log.d("onPostExecute","null");
        }
    }
}





