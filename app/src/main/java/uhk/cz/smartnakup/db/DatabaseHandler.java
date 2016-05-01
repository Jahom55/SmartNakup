package uhk.cz.smartnakup.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jaromir on 21.2.2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "ProductDatabase";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE products " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "xcor INTEGER," +
                "ycor INTEGER " +
                ") ";
        String sql2 = "CREATE TABLE cart " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "quantity INTEGER, " +
                "product_id INTEGER," +
                "bought INTEGER," +
                "FOREIGN KEY(product_id) REFERENCES products(id)" +
                ") ";


        db.execSQL(sql);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS students";
        String sql2 = "DROP TABLE IF EXISTS cart";
        db.execSQL(sql2);
        db.execSQL(sql);

        onCreate(db);
    }

}
