package com.dealfaro.luca.webviewexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MainActivity extends ActionBarActivity {

    String NY = "http://nypost.com";
    String SJ = "http://www.mercurynews.com";
    String LA = "http://www.latimes.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickDaily(View v){
        Intent intent = new Intent(MainActivity.this, ReaderActivity.class);
        intent.putExtra("newspaper", NY);
        startActivity(intent);
    }

    public void clickTimes(View v){
        Intent intent = new Intent(MainActivity.this, ReaderActivity.class);
        intent.putExtra("newspaper", LA);
        startActivity(intent);
    }

    public void clickMercury(View v){
        Intent intent = new Intent(MainActivity.this, ReaderActivity.class);
        intent.putExtra("newspaper", SJ);
        startActivity(intent);
    }

}
