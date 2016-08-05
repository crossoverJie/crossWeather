package top.crossoverjie.app.uitl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import top.crossoverjie.app.model.json.Weather;

/**
 * Created by Administrator on 2016/8/6.
 */
public class DataUtility {
    /**
     * 保存天气数据到SharedPreferences中
     * @param context
     * @param weather
     */
    public static void saveToSharedPreferences(Context context,Weather weather){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        Weather.HeWeatherdataserviceBean bean = weather.getHeWeatherdataservice().get(0);
        String cityName = bean.getBasic().getCity();
        String loc = bean.getBasic().getUpdate().getLoc();//发布时间
        String weatherDesc= bean.getNow().getCond().getTxt();//现在的天气
        String tmp = bean.getNow().getTmp();//当前温度
        SharedPreferences.Editor edit =
                PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString("city_name",cityName) ;
        edit.putBoolean("has_data",true) ;
        edit.putString("publish",loc) ;
        edit.putString("weather_desc",weatherDesc) ;
        edit.putString("tmp", tmp) ;
        edit.putString("current_date",  sdf.format(new Date())) ;
        edit.commit();

    }
}
