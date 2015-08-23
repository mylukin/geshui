package net.lukin.geshui;

import android.app.Application;
import android.util.SparseArray;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.io.Serializable;

/**
 * Created by lukin on 15/8/20.
 */
public class MyApplication extends Application {

    private LocationClient mLocationClient = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initBaidLocation();
    }

    /**
     * 获取对象
     *
     * @return
     */
    public LocationClient getLocationClient() {
        return mLocationClient;
    }

    /**
     * 初始化定位参数
     */
    public void initBaidLocation() {
        // 声明LocationClient类
        mLocationClient = new LocationClient(this);

        LocationClientOption option = new LocationClientOption();
        // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        // 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(1000);
        // 可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        // 可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        // 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setLocationNotify(true);
        // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        // 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        // 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.setIgnoreKillProcess(false);
        // 可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        // 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setEnableSimulateGps(false);

        mLocationClient.setLocOption(option);

    }

    /**
     * 注册定位监听函数
     *
     * @param listener
     */
    public void registerLocationListener(BDLocationListener listener) {
        mLocationClient.registerLocationListener(listener);
    }

    /**
     * 取消注册定位监听
     *
     * @param listener
     */
    public void unRegisterLocationListener(BDLocationListener listener) {
        mLocationClient.unRegisterLocationListener(listener);
    }

    /**
     * 定位开始
     */
    public void LocationStart() {
        mLocationClient.start();
    }

    /**
     * 定位结束
     */
    public void LocationStop() {
        mLocationClient.stop();
    }


    /**
     * 获取城市配置
     *
     * @return List
     */
    public static SparseArray<CityModel> getCities() {

        SparseArray<CityModel> cities = new SparseArray<>();

        cities.put(289, new CityModel(
                // 城市：索引，名称，个税起征点
                "Shanghai", "上海", 3500,
                // 社保：上限，下限
                16353, 3271,
                // 公积金：上限，下限
                1145, 127,
                // 个人：养老，医疗，医疗额外收费，失业，公积金
                0.08, 0.02, 0, 0.005, 0.07,
                // 企业：养老，医疗，医疗额外收费，失业，工伤，生育，公积金
                0.21, 0.11, 0, 0.015, 0.005, 0.01, 0.07
        ));

        cities.put(131, new CityModel(
                // 城市：索引，名称，个税起征点
                "Beijing", "北京", 3500,
                // 社保：上限，下限，二级下限
                17379, 2317, 3476,
                // 公积金：上限，下限
                2327, 206,
                // 个人：养老，医疗，医疗额外收费，失业，公积金
                0.08, 0.02, 3, 0.002, 0.12,
                // 企业：养老，医疗，医疗额外收费，失业，工伤，生育，公积金
                0.20, 0.10, 0, 0.01, 0.008, 0.008, 0.12
        ));

        return cities;
    }

    /**
     * 计算个税
     *
     * @param baseSalary 个税起征点
     * @param income     收入
     * @param insure     保险
     * @return double
     */
    public static double calcDeductTax(double baseSalary, double income, double insure) {
        double taxRate;
        Integer taxQuick;
        double taxableIncome = income - insure - baseSalary;
        if (taxableIncome <= 1500) {
            taxRate = 0.03;
            taxQuick = 0;
        } else if (taxableIncome > 1500 && taxableIncome <= 4500) {
            taxRate = 0.1;
            taxQuick = 105;
        } else if (taxableIncome > 4500 && taxableIncome <= 9000) {
            taxRate = 0.2;
            taxQuick = 555;
        } else if (taxableIncome > 9000 && taxableIncome <= 35000) {
            taxRate = 0.25;
            taxQuick = 1005;
        } else if (taxableIncome > 35000 && taxableIncome <= 55000) {
            taxRate = 0.3;
            taxQuick = 2755;
        } else if (taxableIncome > 55000 && taxableIncome <= 80000) {
            taxRate = 0.35;
            taxQuick = 5505;
        } else {
            taxRate = 0.45;
            taxQuick = 13505;
        }
        return Math.max(taxableIncome * taxRate - taxQuick, 0);
    }

    /**
     * 反推应缴税额
     *
     * @param baseSalary 个税起征点
     * @param income     收入
     * @return double
     */
    public static double calcPayableTax(double baseSalary, double income) {
        double taxRate;
        Integer taxQuick;
        double taxableIncome = income - baseSalary;

        if (taxableIncome <= 1455) {
            taxRate = 0.03;
            taxQuick = 0;
        } else if (taxableIncome > 1455 && taxableIncome <= 4155) {
            taxRate = 0.1;
            taxQuick = 105;
        } else if (taxableIncome > 4155 && taxableIncome <= 7755) {
            taxRate = 0.2;
            taxQuick = 555;
        } else if (taxableIncome > 7755 && taxableIncome <= 27255) {
            taxRate = 0.25;
            taxQuick = 1005;
        } else if (taxableIncome > 27255 && taxableIncome <= 41255) {
            taxRate = 0.3;
            taxQuick = 2755;
        } else if (taxableIncome > 41255 && taxableIncome <= 57505) {
            taxRate = 0.35;
            taxQuick = 5505;
        } else {
            taxRate = 0.45;
            taxQuick = 13505;
        }

        taxableIncome = (taxableIncome - taxQuick) / (1 - taxRate);
        if (taxableIncome <= 1500) {
            taxRate = 0.03;
            taxQuick = 0;
        } else if (taxableIncome > 1500 && taxableIncome <= 4500) {
            taxRate = 0.1;
            taxQuick = 105;
        } else if (taxableIncome > 4500 && taxableIncome <= 9000) {
            taxRate = 0.2;
            taxQuick = 555;
        } else if (taxableIncome > 9000 && taxableIncome <= 35000) {
            taxRate = 0.25;
            taxQuick = 1005;
        } else if (taxableIncome > 35000 && taxableIncome <= 55000) {
            taxRate = 0.3;
            taxQuick = 2755;
        } else if (taxableIncome > 55000 && taxableIncome <= 80000) {
            taxRate = 0.35;
            taxQuick = 5505;
        } else {
            taxRate = 0.45;
            taxQuick = 13505;
        }

        return Math.max(taxableIncome * taxRate - taxQuick, 0);
    }


    /**
     * 计算缴纳
     *
     * @param city   城市配置
     * @param income 收入金额
     * @return Insurance
     */
    public Insurance calcInsure(CityModel city, double income) {
        // 社保对象
        Insurance insurance = new Insurance();
        // 社保上限
        double income_max = city.insure_max_val;
        double income_min = city.insure_min_val;
        // 北京有第二下限
        if (city.idx.equals("Beijing")) {
            // 低于下限，按照下限计算
            if (income < city.insure_min_val) {
                insurance.private_yanglao = city.insure_min_val * city.private_yanglao_rate;
                insurance.private_shiye = city.insure_min_val * city.private_shiye_rate;
                insurance.company_yanglao = city.insure_min_val * city.company_yanglao_rate;
                insurance.company_shiye = city.insure_min_val * city.company_shiye_rate;
            }
            if (income < city.insure_2_min_val) {
                insurance.private_yiliao = city.insure_2_min_val * city.private_yiliao_rate + city.private_yiliao_extra_val;
                insurance.company_yiliao = city.insure_2_min_val * city.company_yiliao_rate + city.company_yiliao_extra_val;
                insurance.company_gongshang = city.insure_2_min_val * city.company_gongshang_rate;
                insurance.company_shengyu = city.insure_2_min_val * city.company_shengyu_rate;
            }
        } else {
            // 低于下限，按照下限计算
            if (income < income_min) {
                insurance.private_yiliao = income_min * city.private_yiliao_rate + city.private_yiliao_extra_val;
                insurance.private_yanglao = income_min * city.private_yanglao_rate;
                insurance.private_shiye = income_min * city.private_shiye_rate;
                insurance.company_yanglao = income_min * city.company_yanglao_rate;
                insurance.company_yiliao = income_min * city.company_yiliao_rate + city.company_yiliao_extra_val;
                insurance.company_gongshang = income_min * city.company_gongshang_rate;
                insurance.company_shengyu = income_min * city.company_shengyu_rate;
                insurance.company_shiye = income_min * city.company_shiye_rate;
            }
        }


        // 高于上限，按照上限计算
        if (income > income_max) {
            insurance.private_yiliao = income_max * city.private_yiliao_rate + city.private_yiliao_extra_val;
            insurance.private_yanglao = income_max * city.private_yanglao_rate;
            insurance.private_shiye = income_max * city.private_shiye_rate;
            insurance.company_yanglao = income_max * city.company_yanglao_rate;
            insurance.company_yiliao = income_max * city.company_yiliao_rate + city.company_yiliao_extra_val;
            insurance.company_gongshang = income_max * city.company_gongshang_rate;
            insurance.company_shengyu = income_max * city.company_shengyu_rate;
            insurance.company_shiye = income_max * city.company_shiye_rate;
        }
        // 按照实际计算
        else {
            insurance.private_yiliao = income * city.private_yiliao_rate + city.private_yiliao_extra_val;
            insurance.private_yanglao = income * city.private_yanglao_rate;
            insurance.private_shiye = income * city.private_shiye_rate;
            insurance.company_yanglao = income * city.company_yanglao_rate;
            insurance.company_yiliao = income * city.company_yiliao_rate + city.company_yiliao_extra_val;
            insurance.company_gongshang = income * city.company_gongshang_rate;
            insurance.company_shengyu = income * city.company_shengyu_rate;
            insurance.company_shiye = income * city.company_shiye_rate;
        }
        // 个人公积金计算
        insurance.private_gongjijin = Math.min(city.gongjj_max_val, Math.max(income * city.private_gongjijin_rate, city.gongjj_min_val));
        // 个人总计缴纳
        insurance.private_sum_val = insurance.private_yanglao + insurance.private_yiliao + insurance.private_shiye + insurance.private_gongjijin;
        // 企业公积金计算
        insurance.company_gongjijin = Math.min(city.gongjj_max_val, Math.max(income * city.company_gongjijin_rate, city.gongjj_min_val));
        // 企业总计缴纳
        insurance.company_sum_val = insurance.company_yanglao + insurance.company_yiliao + insurance.company_shiye + insurance.company_gongshang + insurance.company_shengyu + insurance.company_gongjijin;

        return insurance;
    }

    /**
     * 保险
     */
    public class Insurance implements Serializable {
        public double private_yanglao;
        public double private_yiliao;
        public double private_shiye;
        public double private_gongjijin;
        public double private_sum_val;
        public double company_yanglao;
        public double company_yiliao;
        public double company_shiye;
        public double company_gongshang;
        public double company_shengyu;
        public double company_gongjijin;
        public double company_sum_val;
    }

}
