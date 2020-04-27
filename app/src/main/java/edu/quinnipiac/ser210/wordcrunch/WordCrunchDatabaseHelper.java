package edu.quinnipiac.ser210.wordcrunch;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class WordCrunchDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "wordCrunch"; // the name of our database
    private static final int DB_VERSION = 1; // the version of the database

    WordCrunchDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);

    }

    public static void insertScore(SQLiteDatabase db, int correct, int incorrect, int total){
        ContentValues scoreValues = new ContentValues();
        scoreValues.put("CORRECT", correct);
        scoreValues.put("INCORRECT", incorrect);
        scoreValues.put("TOTAL", total);
        db.insert("PERFORMANCE", null, scoreValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);

    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1){
            db.execSQL("CREATE TABLE PERFORMANCE (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "CORRECT INTEGER, "
                    + "INCORRECT INTEGER, "
                    + "TOTAL INTEGER);");
            insertScore(db, 0, 0, 0);
        }
    }
}
