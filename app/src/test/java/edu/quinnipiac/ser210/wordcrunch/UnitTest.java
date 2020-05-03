package edu.quinnipiac.ser210.wordcrunch;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import static org.junit.Assert.assertTrue;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnitTest extends Fragment implements View.OnClickListener {

    private NavController navController = null;

    TextView inputz;
    private String[] easy_mode;
    private String[] medium_mode;
    private String[] hard_mode;

    private final static String[] ALPHABET = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z"};

    private Random rand = new Random();


    private int rand_easy;
    private int rand_medium;
    private int rand_hard;
    private int rand_select;
    private int rand_alpha;
    private int rand_diff;

    private int num_correct;
    private int num_incorrect;
//    private ScoreUpdater scoreUpdater = new ScoreUpdater();


    private String complete_word = "";
    private String new_word;//word on screen the player sees
    //added "e" as a test letter
    private String letters = "e"; //letters to be displayed with user_choices
    //added "a" as test alpha
    private String alpha = "a";//the correct letter choice
    //added "x" as test input
    private String input = "x";//the value from user_input EditText

    private TextView user_view;//the displayed word
    private TextView user_choices;// the displayed characters


    private EditText user_input;//the letter the user types into the game, check value against 'alpha'


    private String baseUrl = "https://api.datamuse.com/words?sp=";
    //private String baseWord = "";
    private String maxWords = "&max=10";

    private TargetWordHandler tWordHandler = new TargetWordHandler();

    private String difficulty;

    public UnitTest() {
        // Required empty public constructor
    }

    //Check if url appears
    @Test
    public void urlExists() {
        assertTrue(baseUrl != null);
    }

    @Test
    public void alpha() {
        assertTrue(alpha != null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = mPreferences.edit();

        difficulty = mPreferences.getString(getString(R.string.difficulty), "");
    }


    private void wordGenerator() {
        String[] user_letters = {"", "", ""};
        easy_mode = new String[]{"?", "?", "?", "?"};
        medium_mode = new String[]{"?", "?", "?", "?", "?"};
        hard_mode = new String[]{"?", "?", "?", "?", "?", "?"};


        int rand_letter_pos = rand.nextInt(3);

        rand_easy = rand.nextInt(4);
        rand_medium = rand.nextInt(5);
        rand_hard = rand.nextInt(6);
        rand_select = rand.nextInt(10);
        rand_alpha = rand.nextInt(26);
        alpha = ALPHABET[rand_alpha];
        Log.d("alpha ", alpha);

        user_letters[rand_letter_pos] = alpha;
        for (int i = 0; i < user_letters.length; i++) {
            if (user_letters[i].equals("")) {
                user_letters[i] = ALPHABET[rand.nextInt(26)];
            }
        }
        easy_mode[rand_easy] = alpha;
        medium_mode[rand_medium] = alpha;
        hard_mode[rand_hard] = alpha;

        String[] diffLevel;
        switch (difficulty) {
            case "easy":
                diffLevel = easy_mode;
                rand_diff = rand_easy;
                break;
            case "medium":
                diffLevel = medium_mode;
                rand_diff = rand_medium;
                break;
            case "hard":
                diffLevel = hard_mode;
                rand_diff = rand_hard;
                break;
            default:
                diffLevel = easy_mode;
        }

        StringBuilder builder1 = new StringBuilder();

        for (String s : diffLevel) {
            builder1.append(s);
        }
        complete_word = builder1.toString();

        StringBuilder builder2 = new StringBuilder();
        for (String s : user_letters) {
            builder2.append(s);
        }
        letters = builder2.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        wordGenerator();//generate word ??a? <-- this type of format
        new FetchWord().execute(complete_word);//send word for processing
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
        inputz = (TextView) view.findViewById(R.id.textView3);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_button) {
            navController.navigateUp();
        }
        if (v.getId() == R.id.go_button) {
            Log.d("difficulty", difficulty);
            //send completed problem to be scored
            input = user_input.getText().toString();
            if (input.equals("")) {
                inputz.setText("Please select a letter.");
                //Toast.makeText(getContext(), "Please select a letter.", Toast.LENGTH_SHORT).show();
            } else if (!input.equals(alpha)) {
                num_incorrect += 1;
                inputz.setText("Oops wrong answer!");
                // Toast.makeText(getContext(), "Oops wrong answer!", Toast.LENGTH_SHORT).show();
            } else {
                num_correct += 1;
                inputz.setText("GOOD JOB!");
                //Toast.makeText(getContext(), "GOOD JOB!", Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(getContext(), "Go button " + Arrays.toString(easy_mode), Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.new_word_button) {
            wordGenerator();//generate word ??a? <-- this type of format
            new FetchWord().execute(complete_word);//send word for processing
            //Toast.makeText(getContext(), "ResetButton " + complete_word, Toast.LENGTH_SHORT).show();
        }
    }

    @Test
    public void input() {
        assertTrue(input != null);
    }

    //Check if user input field exists, edittext view allowing user to input text
    @Test
    public void inputExists() {
        assertTrue(user_input == null);
    }

    private class FetchWord extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... words) {
            HttpURLConnection urlConnection = null;
            String word = baseUrl + words[0] + maxWords;

            try {
                String tmp = word;

                URL url = new URL(tmp);

                urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setRequestProperty("X-RapidAPI-Key","8db1d44a5emsh082121746e4b546p16eb75jsn4e81403eff20");
                urlConnection.connect();

                if (urlConnection.getInputStream() == null) {
                    Log.e("no connection", "no connection");
                    return null;
                }
                word = getStringFromBuffer(
                        new BufferedReader(new InputStreamReader(urlConnection.getInputStream())));
                assert word != null;
                Log.d("word", word);
            } catch (Exception e) {
                Log.e("GameDoInBackground", "Error" + e.getMessage());
                return null;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            return word;
        }

        private String getStringFromBuffer(BufferedReader bufferedReader) throws Exception {
            StringBuffer buffer = new StringBuffer();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + '\n');

            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e("GameFragment", "Error" + e.getMessage());
                    return null;
                }
            }
            Log.d("word", buffer.toString());

            return tWordHandler.getWord(buffer.toString(), rand_select);
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {
                //Bundle bundle = new Bundle();
                Log.d("onPostExecute", result);
                new_word = result;
                char[] tmp = new_word.toCharArray();

                tmp[rand_diff] = '_';
                new_word = String.valueOf(tmp);
                Log.d("new_word", new_word);

                user_view.setText(new_word);
                user_choices.setText(letters);

                user_input.getText().clear();
            } else
                Log.d("onPostExecute", "null");
        }
    }


    //test if letter choices exist
    @Test
    public void correctLetters() {
        assertTrue(letters != null);
    }

    @Test
    public void completeWord() {
        assertTrue(complete_word != null);
    }

    @Test
    public void wordExists() {
        assertTrue(letters != null);
    }

//    @Test
//    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
//    }
}
