package top.crossoverjie.app.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import java.util.List;

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

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    private MapView mapView ;
    private BaiduMap map ;
    public StringBuffer city_txt ;

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
        }else {
            //有网络使用百度地图进行定位
            mLocationClient = new LocationClient(getApplicationContext()); //声明LocationClient类
            mLocationClient.registerLocationListener(myListener); //注册监听函数
            initLocation();
            mLocationClient.start();
        }

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

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span=1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }




    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            city_txt = new StringBuffer(location.getCity()) ;
            String cityCode = location.getCityCode();
            System.out.println(cityCode);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.d("BaiduLocationApiDem", sb.toString());
            city_txt.deleteCharAt(city_txt.length()-1);

            String httpUrl = "https://api.heweather.com/x3/weather?city="+city_txt+"" +
                    "&key=082719d5f5454dd5ac63d0675374758f";
            queryWeatherFromServer(httpUrl);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}
