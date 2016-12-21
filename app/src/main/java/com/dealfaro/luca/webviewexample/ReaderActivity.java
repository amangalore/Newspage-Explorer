package com.dealfaro.luca.webviewexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.webkit.WebViewClient;
import android.net.Uri;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ReaderActivity extends ActionBarActivity {

    static final public String MYPREFS = "myprefs";
    static final public String PREF_URL = "restore_url";
    static final public String WEBPAGE_NOTHING = "about:blank";
    static final public String LOG_TAG = "webview_example";

    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        Bundle intent = getIntent().getExtras();
        final String sender = intent.getString("newspaper");
        myWebView = (WebView) findViewById(R.id.webView1);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // Binds the Javascript interface
        myWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        myWebView.loadUrl(sender);
        myWebView.loadUrl("javascript:alert(\"Hello\")");
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("http://nypost.com")||url.contains("http://www.mercurynews.com")
                        ||url.contains("http://www.latimes.com")){
                    view.loadUrl(url);
                }
                else{
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public void clickShare(View v){
        EditText et = (EditText) findViewById(R.id.editText);
        String msg = et.getText().toString();
        String weburl = myWebView.getUrl();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = weburl;
        if (msg.equals("")){
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check this out!");
        }
        else {
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, msg);
        }
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public class JavaScriptInterface {
        Context mContext; // Having the context is useful for lots of things,
        // like accessing preferences.

        /** Instantiate the interface and set the context */
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void myFunction(String args) {
            final String myArgs = args;
            Log.i(LOG_TAG, "I am in the javascript call.");
            runOnUiThread(new Runnable() {
                public void run() {
                    Button v = (Button) findViewById(R.id.button1);
                    v.setText(myArgs);
                }
            });

        }

    }


    @Override
    public void onPause() {

        Method pause = null;
        try {
            pause = WebView.class.getMethod("onPause");
        } catch (SecurityException e) {
            // Nothing
        } catch (NoSuchMethodException e) {
            // Nothing
        }
        if (pause != null) {
            try {
                pause.invoke(myWebView);
            } catch (InvocationTargetException e) {
            } catch (IllegalAccessException e) {
            }
        } else {
            // No such method.  Stores the current URL.
            String suspendUrl = myWebView.getUrl();
            SharedPreferences settings = getSharedPreferences(ReaderActivity.MYPREFS, 0);
            SharedPreferences.Editor ed = settings.edit();
            ed.putString(PREF_URL, suspendUrl);
            ed.commit();
            // And loads a URL without any processing.
            myWebView.clearView();
            myWebView.loadUrl(WEBPAGE_NOTHING);
        }
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}