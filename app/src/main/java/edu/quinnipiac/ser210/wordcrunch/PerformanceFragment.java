package edu.quinnipiac.ser210.wordcrunch;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerformanceFragment extends Fragment implements View.OnClickListener{
    private NavController navController = null;

    public PerformanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_performance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        Button backButton = (Button) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        Button resetButton = (Button) view.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);
        TextView corrText = (TextView) view.findViewById(R.id.value_correct);
        TextView incorrText = (TextView) view.findViewById(R.id.value_incorrect);
        String correct;
        try {
            Intent intent = new Intent();

            correct = intent.getStringExtra("correct");
            corrText.setText(correct);
        }
        catch (NullPointerException e){
            Log.e("PerfIntentFailure ", "Performance error: " + e);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.back_button){
            navController.navigateUp();
        }
        if (v.getId() == R.id.reset_button){
            //reset database
        }

    }
}
