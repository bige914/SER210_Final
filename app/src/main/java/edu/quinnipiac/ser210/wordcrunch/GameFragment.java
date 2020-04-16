package edu.quinnipiac.ser210.wordcrunch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment implements View.OnClickListener{

    private NavController navController = null;

    private final static String[] EASY_MODE = {"?","?","?","?"};
    private String[] easy_mode;
    private final static String[] MEDIUM_MODE = {"?","?","?","?","?"};
    private final static String[] HARD_MODE = {"?","?","?","?","?","?"};
    private final static String[] ALPHABET = {"a","b","c","d","e","f","g","h","i","j",
                                              "k","l","m","n","o","p","q","r","s","t",
                                              "u","v","w","x","y","z"};

    private Random rand = new Random();

    private int rand_easy;
    private int rand_medium;
    private int rand_hard;
    private int rand_select;
    private int rand_alpha;

    private String complete_word;
    private String alpha;


    private String[] itemStr = new String[2];
    private String url1 = "https://api.datamuse.com/words?sp=";
    private String url2 = "";
    private String maxWords = "&max=10";
    private String baseUrl = url1 + url2 + maxWords;
    private String inpt = "";

    private TargetWordHandler tWordHandler = new TargetWordHandler();

    public GameFragment() {
        // Required empty public constructor
    }

    public void wordGenerator(){

        complete_word = "";
        easy_mode = EASY_MODE;

        rand_easy = rand.nextInt(4);
        rand_medium = rand.nextInt(5);
        rand_hard = rand.nextInt(6);
        rand_select = rand.nextInt(10);
        rand_alpha = rand.nextInt(26);
        alpha = ALPHABET[rand_alpha];

        easy_mode[rand_easy] = alpha;

        StringBuilder builder = new StringBuilder();
        for (String s : easy_mode){
            builder.append(s);
        }
        String tmp = builder.toString();
        complete_word = tmp;

        for (int i = 0; i< builder.length(); i++){
            builder = new StringBuilder();
            builder.append(i);
        }



        //builder.delete(0, builder.length());
        easy_mode = EASY_MODE;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.back_button){
            navController.navigateUp();
        }
        if (v.getId() == R.id.go_button){
            //send completed problem to be scored
            Toast.makeText(getContext(), "Go button " + Arrays.toString(EASY_MODE), Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.new_word_button){
            wordGenerator();
            Toast.makeText(getContext(), "ResetButton " + complete_word, Toast.LENGTH_SHORT).show();

        }

    }

/*
    private class FetchTranslation extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection =null;
            String translation = "";


            try{
                String target = "target="+params[0]+"&";
                String input = "input="+params[1];

                URL url = new URL(baseUrl + target + input);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-RapidAPI-Key","8db1d44a5emsh082121746e4b546p16eb75jsn4e81403eff20");

                urlConnection.connect();


                if (urlConnection.getInputStream() == null) {
                    Log.e("no connection", "no connection");
                    return null;
                }
                translation = getStringFromBuffer(
                        new BufferedReader(new InputStreamReader(urlConnection.getInputStream())));
                assert translation != null;
                Log.d("translation", translation);
            }catch (Exception e){
                Log.e("MainActivity","Error" + e.getMessage());
                return null;
            }finally {
                if(urlConnection !=null)
                    urlConnection.disconnect();
            }

            return translation;
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
                    Log.e("MainActivity","Error" + e.getMessage());
                    return null;
                }
            }
            Log.d("translation", buffer.toString());
            return  tWordHandler.getWord(buffer.toString());
        }

        @Override
        protected void onPostExecute(String result) {

            if(result != null){
                Bundle bundle = new Bundle();
                Log.d("onPostExecute",result);

                bundle.putString("translation", result);

                //reset edit text field
                //word.getText().clear();


                //pass along display data of translated word to resultFragment and move to said Fragment
                //navController.navigate(R.id.action_mainFragment_to_resultFragment, bundle);

            }else
                Log.d("onPostExecute","null");
        }
    }
    */
}
