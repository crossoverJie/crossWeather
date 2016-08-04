package top.crossoverjie.app.uitl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import top.crossoverjie.app.model.GsonImpl;
import top.crossoverjie.app.model.Weather;

/**
 * Created by Administrator on 2016/8/4.
 */
public class Test {
    String url = "https://api.heweather.com/x3/citylist?search=allchina&key=082719d5f5454dd5ac63d0675374758f";
    String httpUrl = "https://api.heweather.com/x3/weather?cityid=CN101040100&key=082719d5f5454dd5ac63d0675374758f";
//    String jsonResult = request(httpUrl);

    public static String request(String httpUrl) {
        BufferedReader reader = null;String result = null;StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead); sbf.append("\r\n");
            }
            reader.close();
            sbf.deleteCharAt(11);
            sbf.deleteCharAt(15);
            sbf.delete(22,26);
            result = sbf.toString();
        } catch (Exception e) { e.printStackTrace(); }
        return result;
    }

    public static void main(String[] args) {
        String httpUrl = "https://api.heweather.com/x3/weather?city=重庆" +
                "&key=082719d5f5454dd5ac63d0675374758f";
        String jsonResult = request(httpUrl);
        System.out.println(jsonResult);
        Weather weather = GsonImpl.get().toObject(jsonResult, Weather.class);
        System.out.println(weather);
    }
}
