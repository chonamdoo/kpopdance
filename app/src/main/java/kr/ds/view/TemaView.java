package kr.ds.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import kr.ds.adapter.TemaAdapter;
import kr.ds.config.Config;
import kr.ds.data.BaseResultListener;
import kr.ds.data.TemaData;
import kr.ds.handler.TemaHandler;
import kr.ds.kpopdance.R;
import kr.ds.utils.DsObjectUtils;
import kr.ds.widget.ScrollGridView;

/**
 * Created by Administrator on 2016-11-09.
 */
public class TemaView extends LinearLayout{
    private ArrayList<TemaHandler> mData;
    private ResultListner mResultListner;
    private TemaAdapter mTemaAdapter;
    private ScrollGridView mScrollGridView;
    private int mSelectPositon = 0;
    private String mCode = "";
    private String mParam = "";
    public interface ResultListner{
        public <T> void onReturn(Object data, int position);
        public void onComplete();
    }

    public TemaView setCallBack(ResultListner resultListner){
        mResultListner = resultListner;
        return this;
    }
    public TemaView setParam(String code){
        if(!DsObjectUtils.isEmpty(code)) {
            mParam =  code;
        }
        return this;
    }

    public TemaView(Context context) {
        super(context);
    }

    public TemaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TemaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TemaView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public TemaView setSelectPosition (int selectposition){
        mSelectPositon = selectposition;
        return this;
    }
    public TemaView init(final Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = (View)inflater.inflate(R.layout.tema,null);
        mScrollGridView = (ScrollGridView)view.findViewById(R.id.gridView);
        mScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mResultListner != null){
                    mResultListner.onReturn(mData.get(position), position);
                }
            }
        });

        new TemaData().clear().setCallBack(new BaseResultListener() {
            @Override
            public <T> void OnComplete() {

            }

            @Override
            public <T> void OnComplete(Object data) {
                if(data != null) {
                    mData = (ArrayList<TemaHandler>) data;
                    for (int i = 0; i < mData.size(); i++) {
                        if (mSelectPositon == i) {
                            mData.get(i).setSelect(true);
                        } else {
                            mData.get(i).setSelect(false);
                        }
                    }
                    mTemaAdapter = new TemaAdapter(context, mData);
                    mScrollGridView.setAdapter(mTemaAdapter);
                }
                if (mResultListner != null) {
                    mResultListner.onComplete();
                }
            }
            @Override
            public void OnMessage(String str) {

            }
        }).setUrl(Config.URL+ Config.URL_XML+ Config.URL_TEMA).setParam(mParam).getView();
        addView(view);
        return this;
    }
    /*
    public void init(final Context context){
        mLinearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLinearLayout.setLayoutParams(param);
        mLinearLayout.setOrientation(VERTICAL);
        new TemaData().clear().setCallBack(new BaseResultListener() {
            @Override
            public <T> void OnComplete() {

            }

            @Override
            public <T> void OnComplete(Object data) {
                mData = (ArrayList<TemaHandler>) data;
                LinearLayout[] linearLayout = new LinearLayout[mData.size()];
                TextView[] textView = new TextView[mData.size()];

                for(int i = 0 ; i < mData.size(); i++){
                    linearLayout[i] = new LinearLayout(context);
                    linearLayout[i].setOrientation(VERTICAL);
                    linearLayout[i].setBackgroundColor(Color.WHITE);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    param.setMargins(0, 0, 0, 5);
                    linearLayout[i].setLayoutParams(param);

                    textView[i] = new TextView(context);
                    textView[i].setText(mData.get(i).getNttcd_name());
                    textView[i].setPadding(ScreenUtils.getInstacne().getPixelFromDPI(context, 5),  ScreenUtils.getInstacne().getPixelFromDPI(context, 5), 0, ScreenUtils.getInstacne().getPixelFromDPI(context, 5));
                    textView[i].setTextColor(Color.BLACK);
                    textView[i].setId(i);
                    textView[i].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = (int) v.getId();
                            if(mResultListner != null){
                               mResultListner.onReturn(mData.get(position));
                            }
                        }
                    });
                    linearLayout[i].addView(textView[i]);
                    mLinearLayout.addView(linearLayout[i]);

                }
                addView(mLinearLayout);
            }


            @Override
            public void OnMessage(String str) {

            }
        }).setUrl(Config.URL+ Config.URL_XML+ Config.URL_TEMA).setParam("").getView();

    }
    */


}
