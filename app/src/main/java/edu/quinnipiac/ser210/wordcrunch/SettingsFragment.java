package edu.quinnipiac.ser210.wordcrunch;

import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{
    private TextView diffSet;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private NavController navController = null;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        navController = Navigation.findNavController(view);

        Button backButton = (Button) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        Button easyButton = (Button) view.findViewById(R.id.easy_button);
        easyButton.setOnClickListener(this);
        Button mediumButton = (Button) view.findViewById(R.id.medium_button);
        mediumButton.setOnClickListener(this);
        Button hardButton = (Button) view.findViewById(R.id.hard_button);
        hardButton.setOnClickListener(this);
        diffSet = (TextView) view.findViewById(R.id.diff_set);
        checkSharedPreference();
    }
    private void checkSharedPreference(){
        String val = sharedPreferences.getString(getString(R.string.difficulty), "easy");
        diffSet.setText(val);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_button){
            navController.navigateUp();
        }
        if (v.getId() == R.id.easy_button){
            saveData("easy");
            Log.d("onClick", "easy");
        }
        if (v.getId() == R.id.medium_button){
            saveData("medium");
            Log.d("onClick", "medium");
        }
        if (v.getId() == R.id.hard_button){
            saveData("hard");
            Log.d("onClick", "hard");
        }
    }
    private void saveData(String _difficulty){
        editor = sharedPreferences.edit();
        editor.putString(getString(R.string.difficulty), _difficulty);
        editor.apply();
        diffSet.setText(_difficulty);
    }
}
