package net.lukin.geshui;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lukin on 15/8/21.
 */
class CityModel implements Parcelable {
    // 索引名称
    public String idx;
    // 城市名
    public String name;
    // 个税起征点
    public int base_salary;
    // 社保上限
    public int insure_max_val;
    // 社保下限
    public int insure_min_val;
    // 社保第二下限
    public int insure_2_min_val;
    // 公积金上限
    public int gongjj_max_val;
    // 公积金下限
    public int gongjj_min_val;
    // 个人养老
    public double private_yanglao_rate;
    // 个人医疗
    public double private_yiliao_rate;
    // 个人医疗，额外收费
    public int private_yiliao_extra_val;
    // 个人失业
    public double private_shiye_rate;
    // 个人公积金
    public double private_gongjijin_rate;
    // 企业养老
    public double company_yanglao_rate;
    // 企业医疗
    public double company_yiliao_rate;
    // 企业医疗，额外收费
    public int company_yiliao_extra_val;
    // 企业失业
    public double company_shiye_rate;
    // 企业工伤
    public double company_gongshang_rate;
    // 企业生育
    public double company_shengyu_rate;
    // 企业公积金
    public double company_gongjijin_rate;

    @Override
    public String toString() {
        return name;
    }


    public CityModel(
            String idx,
            String name,
            int base_salary,
            int insure_max_val,
            int insure_min_val,
            int gongjj_max_val,
            int gongjj_min_val,
            double private_yanglao_rate,
            double private_yiliao_rate,
            int private_yiliao_extra_val,
            double private_shiye_rate,
            double private_gongjijin_rate,
            double company_yanglao_rate,
            double company_yiliao_rate,
            int company_yiliao_extra_val,
            double company_shiye_rate,
            double company_gongshang_rate,
            double company_shengyu_rate,
            double company_gongjijin_rate
    ) {
        this(
                idx,
                name,
                base_salary,
                insure_max_val,
                insure_min_val,
                0,
                gongjj_max_val,
                gongjj_min_val,
                private_yanglao_rate,
                private_yiliao_rate,
                private_yiliao_extra_val,
                private_shiye_rate,
                private_gongjijin_rate,
                company_yanglao_rate,
                company_yiliao_rate,
                company_yiliao_extra_val,
                company_shiye_rate,
                company_gongshang_rate,
                company_shengyu_rate,
                company_gongjijin_rate
        );
    }

    public CityModel(
            String idx,
            String name,
            int base_salary,
            int insure_max_val,
            int insure_min_val,
            int insure_2_min_val,
            int gongjj_max_val,
            int gongjj_min_val,
            double private_yanglao_rate,
            double private_yiliao_rate,
            int private_yiliao_extra_val,
            double private_shiye_rate,
            double private_gongjijin_rate,
            double company_yanglao_rate,
            double company_yiliao_rate,
            int company_yiliao_extra_val,
            double company_shiye_rate,
            double company_gongshang_rate,
            double company_shengyu_rate,
            double company_gongjijin_rate
    ) {
        this.idx = idx;
        this.name = name;
        this.base_salary = base_salary;
        this.insure_max_val = insure_max_val;
        this.insure_min_val = insure_min_val;
        this.insure_2_min_val = insure_2_min_val;
        this.gongjj_max_val = gongjj_max_val;
        this.gongjj_min_val = gongjj_min_val;
        this.private_yanglao_rate = private_yanglao_rate;
        this.private_yiliao_rate = private_yiliao_rate;
        this.private_yiliao_extra_val = private_yiliao_extra_val;
        this.private_shiye_rate = private_shiye_rate;
        this.private_gongjijin_rate = private_gongjijin_rate;
        this.company_yanglao_rate = company_yanglao_rate;
        this.company_yiliao_rate = company_yiliao_rate;
        this.company_yiliao_extra_val = company_yiliao_extra_val;
        this.company_shiye_rate = company_shiye_rate;
        this.company_gongshang_rate = company_gongshang_rate;
        this.company_shengyu_rate = company_shengyu_rate;
        this.company_gongjijin_rate = company_gongjijin_rate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idx);
        dest.writeString(this.name);
        dest.writeInt(this.base_salary);
        dest.writeInt(this.insure_max_val);
        dest.writeInt(this.insure_min_val);
        dest.writeInt(this.insure_2_min_val);
        dest.writeInt(this.gongjj_max_val);
        dest.writeInt(this.gongjj_min_val);
        dest.writeDouble(this.private_yanglao_rate);
        dest.writeDouble(this.private_yiliao_rate);
        dest.writeInt(this.private_yiliao_extra_val);
        dest.writeDouble(this.private_shiye_rate);
        dest.writeDouble(this.private_gongjijin_rate);
        dest.writeDouble(this.company_yanglao_rate);
        dest.writeDouble(this.company_yiliao_rate);
        dest.writeInt(this.company_yiliao_extra_val);
        dest.writeDouble(this.company_shiye_rate);
        dest.writeDouble(this.company_gongshang_rate);
        dest.writeDouble(this.company_shengyu_rate);
        dest.writeDouble(this.company_gongjijin_rate);
    }

    protected CityModel(Parcel in) {
        this.idx = in.readString();
        this.name = in.readString();
        this.base_salary = in.readInt();
        this.insure_max_val = in.readInt();
        this.insure_min_val = in.readInt();
        this.insure_2_min_val = in.readInt();
        this.gongjj_max_val = in.readInt();
        this.gongjj_min_val = in.readInt();
        this.private_yanglao_rate = in.readDouble();
        this.private_yiliao_rate = in.readDouble();
        this.private_yiliao_extra_val = in.readInt();
        this.private_shiye_rate = in.readDouble();
        this.private_gongjijin_rate = in.readDouble();
        this.company_yanglao_rate = in.readDouble();
        this.company_yiliao_rate = in.readDouble();
        this.company_yiliao_extra_val = in.readInt();
        this.company_shiye_rate = in.readDouble();
        this.company_gongshang_rate = in.readDouble();
        this.company_shengyu_rate = in.readDouble();
        this.company_gongjijin_rate = in.readDouble();
    }

    public static final Creator<CityModel> CREATOR = new Creator<CityModel>() {
        public CityModel createFromParcel(Parcel source) {
            return new CityModel(source);
        }

        public CityModel[] newArray(int size) {
            return new CityModel[size];
        }
    };
}
