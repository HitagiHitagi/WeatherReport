package demo.task7.Provider;

import demo.task7.DBHelperCityID;
import demo.task7.DBHelperWeather;

/**
 * Created by Ha on 2016/3/1.
 */
public class URIList {
    public static final String CONTENT = "content://";
    public static final String AUTHORITY = "demo.task7";


    public static final String WEATHER_URI = CONTENT + AUTHORITY + "/" + DBHelperWeather.TABLE_NAME;
    public static final String CITYINFO_URI = CONTENT + AUTHORITY + "/" + DBHelperCityID.TABLE_NAME;
}
