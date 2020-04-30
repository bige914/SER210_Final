package edu.quinnipiac.ser210.wordcrunch;

import android.content.Intent;
import android.database.Cursor;
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
    private DatabaseHelper myDb;
    private int num_correct;
    private int num_incorrect;
    private int success_average;
    private int total;

    public PerformanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        myDb = new DatabaseHelper(getContext());
        Cursor data = myDb.getData();
        data.moveToFirst();
        num_correct = data.getInt(1);
        num_incorrect = data.getInt(2);
        total = data.getInt(3);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_performance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (total == 0){
            success_average = 0;
        }
        else{
            success_average = (num_correct/total);
        }
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        Button backButton = (Button) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        Button resetButton = (Button) view.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);
        TextView corrText = (TextView) view.findViewById(R.id.value_correct);
        TextView incorrText = (TextView) view.findViewById(R.id.value_incorrect);
        TextView valuePercent = (TextView) view.findViewById(R.id.value_percent);
        String correct = Integer.toString(num_correct);
        Log.d("perfCorrect", correct);
        corrText.setText(correct);
        String incorrect = Integer.toString(num_incorrect);
        incorrText.setText(incorrect);
        String average = Integer.toString(success_average);
        valuePercent.setText(average);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.back_button){
            navController.navigateUp();
        }
        if (v.getId() == R.id.reset_button){
            //reset database
            myDb.deleteData("0");
            myDb.deleteData("1");
        }

    }
}
