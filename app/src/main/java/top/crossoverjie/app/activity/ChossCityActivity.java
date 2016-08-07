package top.crossoverjie.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import crossoverjie.top.crossweather.R;

/**
 * Created by Administrator on 2016/8/7.
 */
public class ChossCityActivity extends Activity {

    private EditText choose_city_edit ;
    private Button query_city ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switch_city_layout);
        choose_city_edit = (EditText) findViewById(R.id.choose_city_edit);
        query_city = (Button) findViewById(R.id.query_city);
        query_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city_name = choose_city_edit.getText().toString() ;
                Intent intent = new Intent() ;
                intent.putExtra("city_name",city_name) ;
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }
}
