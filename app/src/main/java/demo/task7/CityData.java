package demo.task7;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ha on 2016/3/3.
 */
public class CityData {
    @SerializedName("城市代码")
    List<CityID> mCityIDList;

    public static class CityID {

        @SerializedName("省")
        public String province;

        @SerializedName("市")
        List<City> mCityList;

        public static class City {
            @SerializedName("市名")
            public String city;
            @SerializedName("编码")
            public String cityID;

        }
    }

    public String getCity(int province, int city) {
        return mCityIDList.get(province).mCityList.get(city).city;
    }

    public String getCityID(int province, int city) {
        return mCityIDList.get(province).mCityList.get(city).cityID;
    }
}
