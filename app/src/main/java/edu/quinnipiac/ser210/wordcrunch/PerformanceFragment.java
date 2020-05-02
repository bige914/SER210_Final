package edu.quinnipiac.ser210.wordcrunch;
/**
 * PerformanceFragment fragment, gets and displays user scores/percent of answers correct from database,
 * also has added ability to reset values within the database Table to default of 0.
 *
 * @authors Ellsworth Evarts IV
 * @date 5/02/2020
 */
import android.database.Cursor;
import android.database.DatabaseUtils;
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
    private double success_average;
    private int avg;
    private int total;

    private TextView corrText;
    private TextView incorrText;
    private TextView valuePercent;
    public PerformanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        myDb = new DatabaseHelper(getContext());
        Cursor data = myDb.getData();
        data.moveToFirst();
        num_correct = data.getInt(data.getColumnIndex(DatabaseHelper.COL_2));
        num_incorrect = data.getInt(data.getColumnIndex(DatabaseHelper.COL_3));
        total = data.getInt(data.getColumnIndex(DatabaseHelper.COL_4));
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
            success_average = (double)num_correct/total;
            success_average= success_average*100;
            avg=(int)success_average;
        }
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        Button backButton = (Button) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        Button resetButton = (Button) view.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);
        corrText = (TextView) view.findViewById(R.id.value_correct);
        incorrText = (TextView) view.findViewById(R.id.value_incorrect);
        valuePercent = (TextView) view.findViewById(R.id.value_percent);
        String correct = Integer.toString(num_correct);
        Log.d("perfCorrect", correct);
        corrText.setText(correct);
        String incorrect = Integer.toString(num_incorrect);
        incorrText.setText(incorrect);
        String average = Integer.toString(avg);
        valuePercent.setText(average);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.back_button){
            navController.navigateUp();
        }
        if (v.getId() == R.id.reset_button){
            //reset database
            myDb = new DatabaseHelper(getContext());
            boolean updateData = myDb.updateData(1,0,0,0);
            if (updateData){
                Log.d("Performance: ", "database reset");
                corrText.setText("0");
                incorrText.setText("0");
                valuePercent.setText("0");
                myDb = new DatabaseHelper(getContext());
                Cursor cursor = myDb.getData();
                Log.d("DBDump ", DatabaseUtils.dumpCursorToString(cursor));
            }
        }

    }
}
