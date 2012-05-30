package mast.avalons.com;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DbHelper extends SQLiteOpenHelper 
        implements BaseColumns {
    
    public static final String TABLE_NAME = "table_name";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String Z = "z";
    public static final String T = "t";
    
    public DbHelper(Context context) {
        super(context, Provider.DB, null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME 
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+ 
                X + " TEXT, " +
                Y + " TEXT, " +
                Z + " TEXT, " +
                T + " TEXT);");       
    }
 
    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }   
}