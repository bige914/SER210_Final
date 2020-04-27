package edu.quinnipiac.ser210.wordcrunch;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ScoreUpdater {
    private int cur_correct;
    private int cur_incorrect;
    private int cur_total;

    private int add_correct;
    private int add_incorrect;
    private int add_total;
    private static Context context;

    public static Context getContext(){
        return context;
    }
    public static void setContext(Context context1){
        context = context1;
    }



    //takes data from GameFragment
    public void addCorrect(int addCorrect){
        add_correct = addCorrect;
    }
    //takes data from GameFragment
    public void addIncorrect(int addIncorrect){
        add_incorrect = addIncorrect;
    }

    public ScoreUpdater(){

        //create a cursor
        SQLiteOpenHelper wordCrunchDataBaseHelper = new WordCrunchDatabaseHelper(context);
        try {
            SQLiteDatabase db = wordCrunchDataBaseHelper.getReadableDatabase();
            int _id = 1;
            Cursor cursor = db.query("PERFORMANCE",
                    new String[] {"CORRECT", "INCORRECT", "TOTAL"},
                    "_id = ?",
                    new String[] {Integer.toString(_id)},
                    null, null, null);

            //Move to the first record in the Cursor
            if (cursor.moveToFirst()) {
                //get the performance details from the cursor
                cur_correct = cursor.getInt(0);
                cur_incorrect = cursor.getInt(1);
                cur_total = cursor.getInt(3);
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException e){
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_LONG);
            toast.show();
        }
        add_total = add_correct + add_incorrect;
        add_correct+=cur_correct;
        add_incorrect+=cur_incorrect;
        add_total+=cur_total;

        Integer[] add_array = new Integer[]{add_correct, add_incorrect, add_total};
        new UpdateDB().execute(add_array);
    }

private class UpdateDB extends AsyncTask<Integer,Void,Boolean>{


    @Override
    protected Boolean doInBackground(Integer... perfStats) {

        int correct = perfStats[0];
        int incorrect = perfStats[1];
        int total = perfStats[2];
        SQLiteOpenHelper dbHelper = new WordCrunchDatabaseHelper(context);

        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int _id = 1;
            ContentValues cv = new ContentValues();
            cv.put("CORRECT", correct);
            cv.put("INCORRECT", incorrect);
            cv.put("TOTAL", total);
            /*
            db.update(
                    "PERFORMANCE",
                    drinkValues,
                    "_id = ?",
                    new String[]{Integer.toString(_id)});*/

            db.close();
            return true;

        }
        catch (SQLiteException e){
            Log.e("ScoreUpdater","Error" + e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean toDB) {
        if (toDB){
        Intent intent = new Intent(context, PerformanceFragment.class);
        intent.putExtra("correct", add_correct);
        intent.putExtra("incorrect", add_incorrect);
        intent.putExtra("total", add_total);
        }
        else Log.e("onPExecute", "intent failure to move data to PerformanceFragment");
        //super.onPostExecute(toDB);
    }


}
}
