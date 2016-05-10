package demo.task7.Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import demo.task7.DBHelperWeather;

/**
 * Created by Ha on 2016/3/1.
 */
public class WeatherContentProvider extends ContentProvider {

    private static UriMatcher sUriMatcher;

    public static final int CODE = 1;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(URIList.AUTHORITY, DBHelperWeather.TABLE_NAME, CODE);
    }

    private DBHelperWeather mDBHelperWeather;

    @Override
    public boolean onCreate() {
        mDBHelperWeather = new DBHelperWeather(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            return null;
        }
        return mDBHelperWeather.getReadableDatabase().query(
                tableName,
                new String[]{DBHelperWeather.CITY, DBHelperWeather.CITYID},
                selection,
                selectionArgs, null, null, null);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            return 0;
        }
        return mDBHelperWeather.getReadableDatabase().update(
                DBHelperWeather.TABLE_NAME,
                values, selection, selectionArgs);

//        mSqLiteDatabase.update(
//                demo.task7.DBHelperWeather.TABLE_NAME,
//                contentValues,
//                demo.task7.DBHelperWeather.CITY + "=?",
//                new String[]{weatherData.getcity()});


    }

    private String getTableName(Uri uri) {
        int type = sUriMatcher.match(uri);
        String tableName = null;
        switch (type) {
            case CODE:
                tableName = DBHelperWeather.TABLE_NAME;
                break;
        }
        return tableName;
    }
}
