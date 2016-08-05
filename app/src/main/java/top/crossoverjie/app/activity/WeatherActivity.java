package top.crossoverjie.app.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import crossoverjie.top.crossweather.R;
import top.crossoverjie.app.model.json.GsonImpl;
import top.crossoverjie.app.model.json.Weather;
import top.crossoverjie.app.uitl.DataUtility;
import top.crossoverjie.app.uitl.HttpCallBackListener;
import top.crossoverjie.app.uitl.HttpUtil;
import top.crossoverjie.app.uitl.NetworkUtils;

/**
 * Created by Administrator on 2016/8/6.
 */
public class WeatherActivity extends Activity {

    private LinearLayout weatherInfoLayout ;
    private TextView cityNameText ;
    private TextView publishText;
    private TextView currentDate;
    private TextView weatherDescText;
    private TextView tmpText;//当前温度

    private SharedPreferences prefs ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE) ;
        setContentView(R.layout.weather_layout) ;
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        currentDate = (TextView) findViewById(R.id.current_date);
        weatherDescText = (TextView) findViewById(R.id.weather_desc);
        tmpText = (TextView) findViewById(R.id.tmp);
        prefs =PreferenceManager.getDefaultSharedPreferences(this);
        boolean isConnected = NetworkUtils.isConnected(this);
        publishText.setText("同步中..");
        weatherInfoLayout.setVisibility(View.INVISIBLE) ;//先隐藏
        if (!isConnected){//没有网络连接
            boolean has_date = prefs.getBoolean("has_data", false);
            if (!has_date){//也没有存数据
                Toast.makeText(this,"请联网后重试",Toast.LENGTH_LONG).show();
                NetworkUtils.openWirelessSettings(this);
                return;
            }else{
                //SharedPreferences存有数据就显示该数据
                Toast.makeText(this,"没有网络显示之前数据",Toast.LENGTH_LONG).show();
                showWeather();
                return ;
            }
        }
        String httpUrl = "https://api.heweather.com/x3/weather?city=江津" +
                "&key=082719d5f5454dd5ac63d0675374758f";
        queryWeatherFromServer(httpUrl);
    }

    public void queryWeatherFromServer(final String addr){
        HttpUtil.sendRequest(addr, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                final Weather weather = GsonImpl.get().toObject(response, Weather.class);
                //先保存到SharedPreference中
                DataUtility.saveToSharedPreferences(WeatherActivity.this,weather);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather() ;
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步出错");
                    }
                });
            }
        });
    }

    private void showWeather() {

//        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String cityName = prefs.getString("city_name","") ;
        String loc = prefs.getString("publish","") ;
        String weatherDesc = prefs.getString("weather_desc","") ;
        String tmp = prefs.getString("tmp","") ;
        String nowDate= prefs.getString("current_date","") ;

        cityNameText.setText(cityName);
        publishText.setText(loc+"发布") ;
        currentDate.setText(nowDate);
        weatherDescText.setText(weatherDesc) ;
        tmpText.setText(tmp+"C");
        weatherInfoLayout.setVisibility(View.VISIBLE);
    }
}
