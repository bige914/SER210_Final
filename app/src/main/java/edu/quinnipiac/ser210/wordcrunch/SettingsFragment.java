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


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{
    private NavController navController = null;

    public SettingsFragment() {
        // Required empty public constructor
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
        navController = Navigation.findNavController(view);
        Button backButton = (Button) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        Button easyButton = (Button) view.findViewById(R.id.easy_button);
        easyButton.setOnClickListener(this);
        Button mediumButton = (Button) view.findViewById(R.id.medium_button);
        easyButton.setOnClickListener(this);
        Button hardButton = (Button) view.findViewById(R.id.hard_button);
        easyButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button: navController.navigateUp();

            case R.id.easy_button: break;
            //implement ability to change score later
            case R.id.medium_button: break;
            //implement ability to change score later
            case R.id.hard_button: break;
            //implement ability to change score later

        }

    }
}
