package edu.quinnipiac.ser210.wordcrunch;
/**
 * DataBaseHelper class, helper class used to create, interact with the Database and its Table
 * has methods for Database and Table creation, ability to add/edit/get data of Table
 *
 * @authors Ellsworth Evarts IV
 * @date 5/02/2020
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Performance.db";
    public static final String TABLE_NAME = "performance_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "CORRECT";
    public static final String COL_3 = "INCORRECT";
    public static final String COL_4 = "TOTAL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("+COL_1+" INTEGER PRIMARY KEY, CORRECT INTEGER, INCORRECT INTEGER, TOTAL INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(int correct, int incorrect, int total){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, correct);
        contentValues.put(COL_3, incorrect);
        contentValues.put(COL_4, total);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("select * from " + TABLE_NAME, null);
        return data;
    }

    public boolean updateData(int id, int correct, int incorrect, int total){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, correct);
        contentValues.put(COL_3, incorrect);
        contentValues.put(COL_4, total);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {Integer.toString(id)});
        return  true;
    }
}
