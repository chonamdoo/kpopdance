package kr.ds.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ds.handler.TemaHandler;
import kr.ds.kpopdance.R;
import kr.ds.utils.DsObjectUtils;

/**
 * Created by Administrator on 2016-11-10.
 */
public class TemaAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<TemaHandler> mData;
    private LayoutInflater mInflater;

    public TemaAdapter(Context context, ArrayList<TemaHandler> data) {
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public int getWidth(){
        Point p = new Point();
        p.x = mContext.getResources().getDisplayMetrics().widthPixels;
        return p.x;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.tema_item,null);
            holder.textViewName = (TextView) convertView.findViewById(R.id.textView_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(mData.get(position).isSelect == true){
            holder.textViewName.setTextColor(Color.RED);
        }else{
            holder.textViewName.setTextColor(0xff383f47);
        }
        if(!DsObjectUtils.getInstance(mContext).isEmpty(mData.get(position).getName())){
            holder.textViewName.setText(mData.get(position).getName());
        }else{
            holder.textViewName.setText("");
        }
        return convertView;
    }
    class ViewHolder {
        TextView textViewName;
    }
}
