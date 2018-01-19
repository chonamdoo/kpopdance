package kr.ds.handler;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016-08-05.
 */
public class TemaHandler implements Parcelable{

    public String uid;
    public String name;
    public boolean isSelect;

    public TemaHandler(){
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public TemaHandler(Parcel src){
        this.uid = src.readString();
        this.name = src.readString();
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(this.uid);
        dest.writeString(this.name);

    }
    public static final Creator CREATOR = new Creator() { //데이터 가져오기

        @Override
        public Object createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new TemaHandler(in);
        }
        @Override
        public Object[] newArray(int size) {
            // TODO Auto-generated method stub
            return new TemaHandler[size];
        }
    };





}
