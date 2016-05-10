package demo.task7;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import demo.task7.Provider.URIList;

public class MainActivity extends Activity implements View.OnClickListener {

    public static final int MESSAGE_CODE = 1458;
    public static final int REFRESH_TIME = 300000;
    public static final String INITIAL_STATUS = "initial status";
    public static final String IS_INITIAL = "city_info is initial";
    private static String TAG = MainActivity.class.getSimpleName();
    private TextView mTextView;
    private String mWeather;
    public static final String[] URI = {"北京", "101010100", "上海", "101020100", "深圳", "101280601"};
    private Button mButtonConfirm;
    private EditText mEditText;
    private SQLiteDatabase mSqLiteDatabase;
    private DBHelperWeather mDBHelperWeather;
    private ContentResolver mContentResolver;
    private Spinner mSpinner;
    private WeatherData mWeatherData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        sendRefreshMessage();
        cityInfoInit();


    }

    private void init() {
        mDBHelperWeather = new DBHelperWeather(MainActivity.this);
        mSqLiteDatabase = mDBHelperWeather.getWritableDatabase();

        mContentResolver = getContentResolver();

        mButtonConfirm = (Button) findViewById(R.id.button_confirm);
        mButtonConfirm.setOnClickListener(this);


        mTextView = (TextView) findViewById(R.id.text_view_temp);
        mEditText = (EditText) findViewById(R.id.edit_text);

        spinnerSetting();
    }

    private void spinnerSetting() {
        final List<String> list = new ArrayList<>();
        list.add("北京");
        list.add("上海");
        list.add("深圳");
        list.add("成都");
        list.add("天津");
        list.add("南京");
        mSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mEditText.setText(list.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void sendRefreshMessage() {
        Refresh refresh = new Refresh();
        Message message = new Message();
        message.what = MESSAGE_CODE;
        refresh.sendMessageDelayed(message, REFRESH_TIME);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_confirm:
                checkCity();
                break;
        }

    }

    private void checkCity() {
        String city = String.valueOf(mEditText.getText());
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(
                Uri.parse(URIList.WEATHER_URI), null,
                DBHelperWeather.CITY + "=?", new String[]{city}, null);
        assert cursor != null;

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                String cityID = cursor.getString(cursor.getColumnIndexOrThrow(DBHelperWeather.CITYID));
                Log.i(TAG, "onClick: " + cityID);
                String url = "http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId=" + cityID;
                new RequestNetworkDataTask().execute(url);
            }
        } else {
            Toast.makeText(MainActivity.this, "请输入正确地名", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }


    class RequestNetworkDataTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String[] params) {
            return requestData(params[0]);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Gson gson = new Gson();
            mWeatherData = gson.fromJson(result, WeatherData.class);
            updateWeatherInfo(result, mWeatherData);
            displayWeatherInfo(mWeatherData);
//            mTextView.setText(mWeather);
        }

        private void updateWeatherInfo(String result, WeatherData weatherData) {

            ContentResolver contentResolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(demo.task7.DBHelperWeather.CONTENT, result);
            contentResolver.update(
                    Uri.parse(URIList.WEATHER_URI), contentValues,
                    DBHelperWeather.CITY + "=?",
                    new String[]{weatherData.getcity()});
        }
    }

    private void displayWeatherInfo(WeatherData weatherData) {
        mWeather = weatherData.getDate_y() + weatherData.getTime() + "获取实时天气情况:\n"
                + "城市：" + weatherData.getcity() + "   天气：" + weatherData.getWeather() + "   温度：" + weatherData.getTemp1() + "\n"
                + "湿度：" + weatherData.getSD();
        TextView cityInfo = (TextView) findViewById(R.id.text_view_city);

        String s = weatherData.getcity() + weatherData.getDate_y() + weatherData.getTime() + "获取实时天气情况:";
        cityInfo.setText(s);
        TextView temp = (TextView) findViewById(R.id.text_view_temp);
        temp.setText(weatherData.getTemp1());
        TextView sd = (TextView) findViewById(R.id.text_view_sd);
        String s1 = "湿度：" + weatherData.getSD() + "\n风向：" + weatherData.getWD() + " 风速：" + weatherData.getWS() + "\n";
        sd.setText(s1);
        TextView alert = (TextView) findViewById(R.id.text_view_alert);
        alert.setText(weatherData.getAlert());
    }

    private String requestData(String urlString) {

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(30000);
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            String result = "";

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                Reader reader = new InputStreamReader(inputStream);
                char[] buffer = new char[1024];
                int len;
                while ((len = reader.read(buffer)) != -1) {
                    result += new String(buffer, 0, len);
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void cityInfoInit() {
        SharedPreferences sharedPreferences = getSharedPreferences(INITIAL_STATUS, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(IS_INITIAL, false)) {
            InputStream inputStream = getResources().openRawResource(R.raw.cityid);
            Reader reader = new InputStreamReader(inputStream);
            Gson gson = new Gson();
            CityData cityData = gson.fromJson(reader, CityData.class);
            SQLiteDatabase sqLiteDatabase = mDBHelperWeather.getWritableDatabase();
            for (int i = 0; i < cityData.mCityIDList.size(); i++) {
                for (int j = 0; j < cityData.mCityIDList.get(i).mCityList.size(); j++) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelperWeather.CITY, cityData.getCity(i, j));
                    contentValues.put(DBHelperWeather.CITYID, cityData.getCityID(i, j));
                    contentValues.put(DBHelperWeather.CONTENT, "无记录");
                    sqLiteDatabase.insert(DBHelperWeather.TABLE_NAME, null, contentValues);
                }
            }
            Toast.makeText(MainActivity.this, "初始化成功", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_INITIAL, true);
            editor.apply();
        }
    }

    @SuppressLint("HandlerLeak")
    public class Refresh extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_CODE:
                    Log.i(TAG, "使用缓存数据更新天气情况");
                    displayWeatherInfo(mWeatherData);
                    sendRefreshMessage();
                    break;
            }
        }
    }
}
