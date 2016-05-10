package demo.task7;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ha on 2016/3/1.
 */
public class DBHelperWeather extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weather_history";

    public static final String TABLE_NAME = "weather";
    public static final String ID = "_id";
    public static final String CITY = "城市";
    public static final String CITYID = "城市ID";
    public static final String CONTENT = "content";

    public DBHelperWeather(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( "
                + ID + " integer PRIMARY KEY autoincrement, "
                + CITY + " varchar(10) not null, "
                + CITYID + " varchar(10) not null, "
                + CONTENT + " varchar(20000) not null "
                + ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
