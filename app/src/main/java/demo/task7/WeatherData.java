package demo.task7;

import java.util.List;

/**
 * Created by Ha on 2016/3/1.
 */
public class WeatherData {
    public Forecast forecast;
    public Realtime realtime;
    public List<Alert> alert;

    public String getcity() {
        return forecast.city;
    }

    public String getDate_y() {
        return forecast.date_y;
    }

    public String getTemp1() {
        return forecast.temp1;
    }

    public String getSD() {
        return realtime.SD;
    }

    public String getWD() {
        return realtime.WD;
    }

    public String getWS() {
        return realtime.WS;
    }

    public String getTime() {
        return realtime.time;
    }

    public String getWeather() {
        return realtime.weather;
    }

    public String getAlert() {
        if (alert.size() > 0) {
            return alert.get(0).detail;
        } else return "暂无天气预警";


    }


    public static class Forecast {
        private String city;
        private String date_y;
        private String fl1;//风力
        private String fx1;//风向
        private String fx2;//风向
        private String img_single;//天气
        private String temp1;//温度
    }

    public static class Realtime {
        private String SD;//湿度
        private String WD;//风向
        private String WS;//风级
        private String time;//时间
        private String weather;//
    }


    public static class Alert {

        public String detail;
        public String title;
        public String type;

    }
//    "alert": [
//    {
//        "Abnormal": "",
//            "city_code": "101010100",
//            "detail": "北京市气象台02日20时10分发布霾黄色预警,预计2日后半夜至4日，北京大部分地区有中度霾，南部和东部有重度霾，能见度较低，请注意防范。",
//            "holiday": "",
//            "level": "黄色",
//            "pub_time": 1456920600000,
//            "title": "北京市气象台02日20时发布霾黄色预警!",
//            "type": "霾"
//    }
//    ]
}
