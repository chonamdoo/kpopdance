package kr.ds.kpopdance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import kr.ds.config.Config;
import kr.ds.data.BaseResultListener;
import kr.ds.data.LogData;
import kr.ds.db.BookMarkDB;
import kr.ds.handler.ListHandler;
import kr.ds.utils.DsObjectUtils;


public class WebViewActivity extends BaseActivity implements View.OnClickListener{
	private ListHandler mSavedata;
	private Bundle mBundle;
	private String mobliehomepage;
	private WebView WebHomepage;
	private LinearLayout ProgressArea;
	private Toolbar mToolbar;
	private BookMarkDB mBookMarkDB;
	private Cursor mCursor;

	private LinearLayout mLinearLayoutShare;
    private LinearLayout mLinearLayoutBookMark;
	private ImageView mImageViewBookMark;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mBookMarkDB = new BookMarkDB(getApplicationContext());
		if(savedInstanceState != null){
			mSavedata = (ListHandler) savedInstanceState.getParcelable("data");
			mobliehomepage = "https://www.youtube.com/watch?v=" + mSavedata.getVideo_id();
		}else{
			mBundle = getIntent().getExtras();
			mSavedata = (ListHandler) mBundle.getParcelable("data");
			mobliehomepage = "https://www.youtube.com/watch?v=" + mSavedata.getVideo_id();
		}
		
		setContentView(R.layout.activity_web);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {
	    	mToolbar.setTitle(mSavedata.getTitle());
	    	setSupportActionBar(mToolbar);
	    	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    }
		WebHomepage = (WebView) findViewById(R.id.web_homepage);
		ProgressArea = (LinearLayout) findViewById(R.id.progress_area);

		mImageViewBookMark = (ImageView)findViewById(R.id.imageView_bookmark);
		(mLinearLayoutShare = (LinearLayout) findViewById(R.id.linearLayout_share)).setOnClickListener(this);
        (mLinearLayoutBookMark = (LinearLayout) findViewById(R.id.linearLayout_bookmark)).setOnClickListener(this);

		mBookMarkDB.open();
        mCursor = mBookMarkDB.BookMarkConfirm(mSavedata.getDd_uid());
        if(mCursor.getCount() > 0){
            mImageViewBookMark.setImageResource(R.drawable.icon_book_on);
        }else{
            mImageViewBookMark.setImageResource(R.drawable.icon_book_off);
        }
        mCursor.close();
        mBookMarkDB.close();


		try {
			if(mobliehomepage != null){
				if(!mobliehomepage.matches("")){
					getWebView(mobliehomepage);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		setLog();
		
	
	}

	public void setLog(){
		new LogData(getApplicationContext()).clear().setCallBack(new BaseResultListener() {
			@Override
			public <T> void OnComplete() {

			}

			@Override
			public <T> void OnComplete(Object data) {

			}

			@Override
			public void OnMessage(String str) {

			}
		}).setUrl(Config.URL+ Config.URL_XML+ Config.URL_LOG).setParam("?dd_uid="+mSavedata.getDd_uid()).getView();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putParcelable("data", mSavedata);
	}
	
	public void getWebView(final String url) {
		final Handler handler = new Handler();
		Runnable doInit = new Runnable() {
			public void run() {
				WebHomepage.getSettings().setLoadWithOverviewMode(true);
				WebHomepage.getSettings().setUseWideViewPort(true);
				WebHomepage.setVerticalScrollbarOverlay(true);
				WebHomepage.getSettings().setSupportZoom(true);
				WebHomepage.getSettings().setBuiltInZoomControls(true);
				WebHomepage.getSettings().setJavaScriptEnabled(true); 
				WebHomepage.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
				WebHomepage.getSettings().setSaveFormData(true);
				WebHomepage.getSettings().setSavePassword(true);
				WebHomepage.loadUrl(url);
				WebHomepage.setWebViewClient(new WebViewClients());
				
				WebHomepage.setWebChromeClient(new WebChromeClient() {
					@Override
					public boolean onJsAlert(WebView view, String url,
							String message, final JsResult result) {
						new AlertDialog.Builder(WebViewActivity.this)
								.setMessage(message)
								.setPositiveButton(android.R.string.ok,
										new AlertDialog.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												result.confirm();
											}
										}).setCancelable(false).create().show();
						return true;

					};
				});
				WebHomepage.setDownloadListener(new DownloadListener() {
				    @Override
				    public void onDownloadStart(String url, String userAgent,
				            String contentDisposition, String mimetype,
				            long contentLength) {

				        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				    }
				});
			}
		};
		ProgressArea.setVisibility(View.VISIBLE);
		handler.postDelayed(doInit, 0);
	}
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

	private class WebViewClients extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if(url.startsWith("market:")){
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
				return true;
			}else{
				view.loadUrl(url);
			}
			return true;
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			ProgressArea.setVisibility(View.VISIBLE);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			ProgressArea.setVisibility(View.GONE);
		}
	}
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (KeyCode == KeyEvent.KEYCODE_BACK) {
				if(WebHomepage.canGoBack()){
					WebHomepage.goBack();
				}else{
					finish();
				}
				return true;
			}
		}
		return super.onKeyDown(KeyCode, event);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
            finish();
            return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void SendMMS() {
        if(!DsObjectUtils.isEmpty(mSavedata.getTitle()) && !DsObjectUtils.isEmpty(mSavedata.getDd_uid())) {
            try {
                Intent NextIntent = new Intent(Intent.ACTION_SEND);
                NextIntent.setType("text/plain");
                NextIntent.putExtra(Intent.EXTRA_SUBJECT, mSavedata.getTitle());
                NextIntent.putExtra(Intent.EXTRA_TEXT, "반갑습니다.^^ 무료탁구레슨 입니다.\n\n " +
                        "동영상:\n" + "https://www.youtube.com/watch?v="+mSavedata.getVideo_id() +
                        "\n\n어플다운:\n" + "https://play.google.com/store/apps/details?id=kr.ds.kpopdance" +
                "\n\n많은 다운 부탁드립니다.");
                startActivity(Intent.createChooser(NextIntent, mSavedata.getTitle() + "공유하기"));
            } catch (Exception e) {
                // TODO: handle exception
                Log.i("TEST", e.toString() + "");
            }
        }else {
            Toast.makeText(getApplicationContext(),"계속 문제가 발생시 관리자에게 문의해주시기 바랍니다.",Toast.LENGTH_SHORT).show();
        }
    }

	@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearLayout_share:
                SendMMS();
                break;
            case R.id.linearLayout_bookmark:
                if(!DsObjectUtils.isEmpty(mSavedata.getDd_uid())) {
                    mBookMarkDB.open();
                    mCursor = mBookMarkDB.BookMarkConfirm(mSavedata.getDd_uid());
                    if (mCursor.getCount() == 0) {
                        mBookMarkDB.createNote(mSavedata.getDd_uid());
                        mImageViewBookMark.setImageResource(R.drawable.icon_book_on);
                        Toast.makeText(getApplicationContext(), R.string.bookmark_save, Toast.LENGTH_SHORT).show();
                    } else {
                        mBookMarkDB.deleteNote(mSavedata.getDd_uid());
                        mImageViewBookMark.setImageResource(R.drawable.icon_book_off);
                        Toast.makeText(getApplicationContext(), R.string.bookmark_cancel, Toast.LENGTH_SHORT).show();
                    }
                    mCursor.close();
                    mBookMarkDB.close();

                    setResult(RESULT_OK);
                }else{
                    Toast.makeText(getApplicationContext(),"계속 문제가 발생시 관리자에게 문의해주시기 바랍니다.",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.linearLayout_like:

                break;
        }
    }
}
