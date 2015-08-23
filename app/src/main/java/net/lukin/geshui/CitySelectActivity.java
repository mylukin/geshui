package net.lukin.geshui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class CitySelectActivity extends AppCompatActivity {

    protected final String CLASS_NAME = getClass().getSimpleName();
    protected ListView list_select_cities;
    protected SparseArray<CityModel> cities;
    protected List<CityModel> listCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_city_select);

        list_select_cities = (ListView) findViewById(R.id.list_select_cities);

        cities = MyApplication.getCities();

        // 把城市信息转换成List
        listCities = new ArrayList<>();
        int size = cities.size() - 1;
        for (int i = size; i >= 0; i--) {
            listCities.add(cities.valueAt(i));
        }

        list_select_cities.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, listCities));
        list_select_cities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CitySelectActivity.this, MainActivity.class);
                intent.putExtra("select_city", listCities.get(position));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}

