package top.crossoverjie.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import top.crossoverjie.app.model.City;
import top.crossoverjie.app.model.County;
import top.crossoverjie.app.model.Province;

/**
 * Created by Administrator on 2016/8/3.
 */
public class CrossWeatherDB {
    /**数据库名称**/
    private static final int version =1 ;

    /**数据库版本**/
    private static final String DB_NAME ="cross_weather" ;

    private static CrossWeatherDB crossWeatherDB ;
    private SQLiteDatabase db ;

    private CrossWeatherDB(Context context){
        CrossWeatherOpenHelper dbHelper = new CrossWeatherOpenHelper(context,DB_NAME,null,version) ;
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CrossWeather实例
     * @param context
     * @return
     */
    private synchronized static CrossWeatherDB getInstance(Context context){
        if (crossWeatherDB == null){
            crossWeatherDB = new CrossWeatherDB(context) ;
        }
        return crossWeatherDB ;
    }

    /**
     * 保存省份
     * @param province
     */
    public void saveProvicen(Province province){
        if (province != null){
            ContentValues values = new ContentValues() ;
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("Province",null,values) ;
        }
    }

    /**
     * 获取所有的省份
     * @return
     */
    public List<Province> loadProvinces(){
        List<Province> provinces = new ArrayList<Province>() ;
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                Province province = new Province() ;
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinces.add(province) ;
            }while (cursor.moveToNext());
        }
        if (cursor !=null){
            cursor.close();
        }
        return provinces ;
    }

    /**
     * 保存城市
     * @param city
     */
    public void saveCity(City city){
        if (city != null){
            ContentValues values = new ContentValues() ;
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());
            db.insert("city",null,values) ;
        }
    }

    /**
     * 返回所有城市
     * @return
     */
    public List<City> loadCities(int provinceId){
        List<City> cities = new ArrayList<City>() ;
        Cursor cursor = db.query("City", null, "province_id=?",
                new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()){
            do {
                City city = new City() ;
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                cities.add(city) ;
            }while (cursor.moveToNext());
        }
        if (cursor !=null){
            cursor.close();
        }
        return cities ;
    }



    /**
     * 保存县
     * @param county
     */
    public void saveCounty(County county){
        if (county != null){
            ContentValues values = new ContentValues() ;
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_id",county.getCityId());
            db.insert("County",null,values) ;
        }
    }

    /**
     * 返回所有县城
     * @return
     */
    public List<County> loadCounties(int cityId){
        List<County> counties = new ArrayList<County>() ;
        Cursor cursor = db.query("City", null, "city_id=?", new String[]{String.valueOf(cityId)}
                , null, null, null);
        if (cursor.moveToFirst()){
            do {
                County county = new County() ;
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                counties.add(county) ;
            }while (cursor.moveToNext());
        }
        if (cursor !=null){
            cursor.close();
        }
        return counties ;
    }

}
