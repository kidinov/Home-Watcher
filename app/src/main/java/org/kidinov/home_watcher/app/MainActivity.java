package org.kidinov.home_watcher.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


public class MainActivity extends Activity {
    private static final String URL = "http://awe.fe100.net:8080/?action=snapshot";

    private WebView webView;
    private Thread refresher;
    private volatile boolean refresherRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(URL);
    }

    @Override
    public void onResume() {
        super.onResume();

        refresherRunning = true;
        refresher = new Thread(new RefresherRunnable());
        refresher.start();
    }

    @Override
    public void onPause() {
        refresherRunning = false;
        super.onPause();

    }

    public class RefresherRunnable implements Runnable {

        @Override
        public void run() {
            while (refresherRunning) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl(URL);
                    }
                });
            }
        }
    }

}
