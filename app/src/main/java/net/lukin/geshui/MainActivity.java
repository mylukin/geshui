package net.lukin.geshui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    protected final String CLASS_NAME = getClass().getSimpleName();
    private long firstTime = 0;
    protected TextView inverse_pre_tax_income;
    protected TextView calculate_after_tax_income;
    protected LinearLayout table_view_result;
    protected Button select_cities;
    protected Button select_base_number;
    protected EditText input_salary;
    private BDLocationListener BDLocationListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置程序标题
        setTitle(R.string.app_title);
        // 设置内容
        setContentView(R.layout.activity_main);
        // umeng统计
        MobclickAgent.updateOnlineConfig(getApplication());
        // 接收用户选择的城市属性
        CityModel city = getIntent().getParcelableExtra("select_city");
        if (city == null) {
            // 定位用户地理位置
            ((MyApplication) getApplication()).registerLocationListener(BDLocationListener);
            ((MyApplication) getApplication()).LocationStart();
        } else {
            showMainActivity(city);
        }

    }

    /**
     * 显示主页面
     *
     * @param city_code
     */
    private void showMainActivity(int city_code) {
        // 选择城市
        SparseArray<CityModel> cities = MyApplication.getCities();
        CityModel city = cities.get(city_code);
        if (city != null) {
            showMainActivity(city);
        }
    }

    /**
     * 显示页面
     *
     * @param city
     */
    private void showMainActivity(final CityModel city) {

        // 获取输入值
        input_salary = (EditText) findViewById(R.id.input_salary);

        // 获取城市选择按钮
        View.OnClickListener select_cities_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CitySelectActivity.class);
                startActivity(intent);
            }
        };
        select_cities = (Button) findViewById(R.id.select_cities);
        select_cities.setText(city.name);
        select_cities.setOnClickListener(select_cities_listener);
        select_base_number = (Button) findViewById(R.id.select_base_number);
        select_base_number.setOnClickListener(select_cities_listener);

        // 获取显示结果布局
        table_view_result = (LinearLayout) findViewById(R.id.table_view_result);
        table_view_result.setVisibility(View.GONE);

        // 定义计算税收事件
        View.OnClickListener calcTaxBtnOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入数值
                String salary = input_salary.getText().toString();
                if (salary.length() < 1) {
                    Toast.makeText(MainActivity.this, input_salary.getHint().toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
                double pre_tax_income = 0;
                double after_tax_income = 0;
                double taxValue = 0;
                double insure = 0;

                // 让输入框失去焦点
                input_salary.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input_salary.getWindowToken(), 0);

                // 按钮
                Button click_btn = (Button) v;
                // 税前收入
                double income = Double.valueOf(salary);
                // 个税起征点
                double baseSalary = city.base_salary;
                // 保险明细
                MyApplication.Insurance insurance = ((MyApplication) getApplication()).calcInsure(city, income);
                // 计算税后收入
                if (click_btn.getId() == R.id.button_calculate_after_tax_income) {
                    // 三险一金
                    insure = insurance.private_sum_val;
                    // 个税
                    if (income < baseSalary) {
                        taxValue = 0;
                    } else {
                        taxValue = MyApplication.calcDeductTax(baseSalary, income, insure);
                    }
                    // 税前收入
                    pre_tax_income = income;
                    // 税后收入
                    after_tax_income = Math.max(income - insure - taxValue, 0);
                }
                // 反推税前收入
                else if (click_btn.getId() == R.id.button_inverse_pre_tax_income) {
                    // 个税
                    if (income < baseSalary) {
                        taxValue = 0;
                    } else {
                        taxValue = MyApplication.calcPayableTax(baseSalary, income);
                    }
                    // 保险费率
                    double insure_rate = (1 - (city.private_yanglao_rate + city.private_yiliao_rate + city.private_shiye_rate + city.private_gongjijin_rate));
                    // 税前收入
                    pre_tax_income = (income + taxValue) / insure_rate;
                    // 保险明细
                    insurance = ((MyApplication) getApplication()).calcInsure(city, pre_tax_income);
                    // 三险一金
                    insure = insurance.private_sum_val;
                    // 税后收入
                    after_tax_income = income;
                    pre_tax_income = income + taxValue + insure;
                }

                // 税前收入
                ((TextView) findViewById(R.id.text_pre_tax_income)).setText(new DecimalFormat("###,###").format(pre_tax_income));
                // 税后收入
                ((TextView) findViewById(R.id.text_after_tax_income)).setText(new DecimalFormat("###,###").format(after_tax_income));
                // 缴纳个税
                ((TextView) findViewById(R.id.text_pay_tax_value)).setText(new DecimalFormat("###,###").format(taxValue));
                // 三险一金
                ((TextView) findViewById(R.id.text_full_insurance)).setText(new DecimalFormat("###,###").format(insure));
                ((TextView) findViewById(R.id.private_yanglao)).setText(new DecimalFormat("###,###").format(insurance.private_yanglao));
                ((TextView) findViewById(R.id.private_yiliao)).setText(new DecimalFormat("###,###").format(insurance.private_yiliao));
                ((TextView) findViewById(R.id.private_shiye)).setText(new DecimalFormat("###,###").format(insurance.private_shiye));
                ((TextView) findViewById(R.id.private_gongjijin)).setText(new DecimalFormat("###,###").format(insurance.private_gongjijin));
                ((TextView) findViewById(R.id.private_sum_val)).setText(new DecimalFormat("###,###").format(insurance.private_sum_val));
                ((TextView) findViewById(R.id.company_yanglao)).setText(new DecimalFormat("###,###").format(insurance.company_yanglao));
                ((TextView) findViewById(R.id.company_yiliao)).setText(new DecimalFormat("###,###").format(insurance.company_yiliao));
                ((TextView) findViewById(R.id.company_shiye)).setText(new DecimalFormat("###,###").format(insurance.company_shiye));
                ((TextView) findViewById(R.id.company_gongshang)).setText(new DecimalFormat("###,###").format(insurance.company_gongshang));
                ((TextView) findViewById(R.id.company_shengyu)).setText(new DecimalFormat("###,###").format(insurance.company_shengyu));
                ((TextView) findViewById(R.id.company_gongjijin)).setText(new DecimalFormat("###,###").format(insurance.company_gongjijin));
                ((TextView) findViewById(R.id.company_sum_val)).setText(new DecimalFormat("###,###").format(insurance.company_sum_val));

                // 计算税后收入，并刷新结果
                table_view_result.setVisibility(View.VISIBLE);
            }
        };

        // 计算税后收入
        calculate_after_tax_income = (TextView) findViewById(R.id.button_calculate_after_tax_income);
        calculate_after_tax_income.setOnClickListener(calcTaxBtnOnClickListener);

        // 反推税前收入
        inverse_pre_tax_income = (TextView) findViewById(R.id.button_inverse_pre_tax_income);
        inverse_pre_tax_income.setOnClickListener(calcTaxBtnOnClickListener);
    }


    /**
     * 定位回调
     */
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;

            switch (location.getLocType()) {
                // 定位成功
                case BDLocation.TypeGpsLocation:
                case BDLocation.TypeNetWorkLocation:
                case BDLocation.TypeOffLineLocation:
                    showMainActivity(Integer.valueOf(location.getCityCode()));
                    ((MyApplication) getApplication()).LocationStop();
                    ((MyApplication) getApplication()).unRegisterLocationListener(BDLocationListener);
                    break;
                // 定位失败
                default:

                    break;
            }
        }
    }

    // 双击退出程序
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            // 如果两次按键时间间隔大于1500毫秒，则不退出
            if (secondTime - firstTime > 1500) {
                Toast.makeText(MainActivity.this, "再次点击退出程序", Toast.LENGTH_SHORT).show();
                // 更新firstTime
                firstTime = secondTime;
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
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
