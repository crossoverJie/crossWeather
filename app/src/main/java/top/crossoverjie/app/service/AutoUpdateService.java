package top.crossoverjie.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;

import top.crossoverjie.app.model.json.GsonImpl;
import top.crossoverjie.app.model.json.Weather;
import top.crossoverjie.app.uitl.DataUtility;
import top.crossoverjie.app.uitl.HttpCallBackListener;
import top.crossoverjie.app.uitl.HttpUtil;

/**
 * Created by Administrator on 2016/8/7.
 */
public class AutoUpdateService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                refresh_weather() ;
                Log.d("my_tag","自动更新当前时间:"+new Date().toString()) ;
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time =8 * 60 * 60 * 1000 ;//八个小时更新一次
        long triggerTime = SystemClock.elapsedRealtime() + time;
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,pi);

        return super.onStartCommand(intent, flags, startId);
    }


    public void refresh_weather(){
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
        String city_name = spf.getString("city_name","") ;
        StringBuilder city_txt = new StringBuilder(city_name);
        String httpUrl = "https://api.heweather.com/x3/weather?city="+city_txt+"" +
                "&key=082719d5f5454dd5ac63d0675374758f";
        HttpUtil.sendRequest(httpUrl, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                final Weather weather = GsonImpl.get().toObject(response, Weather.class);
                //先保存到SharedPreference中
                DataUtility.saveToSharedPreferences(AutoUpdateService.this,weather);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();

            }
        });

    }
}
