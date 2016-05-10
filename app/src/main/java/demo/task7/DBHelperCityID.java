package demo.task7;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ha on 2016/3/1.
 */
public class DBHelperCityID extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "city";
    public static final String TABLE_NAME = "city_info";
    public static final String ID = "_id";
    public static final String CITY = "city";
    public static final String CITY_ID = "cityID";

    public DBHelperCityID(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( "
                + ID + " integer PRIMARY KEY autoincrement, "
                + CITY + " varchar(10) not null, "
                + CITY_ID + " varchar(20) not null "
                + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
