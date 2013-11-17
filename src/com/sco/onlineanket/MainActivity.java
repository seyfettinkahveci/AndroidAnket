package com.sco.onlineanket;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		WebView webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/index.html");  
		
		 Thread Goruntule = new Thread()
		    {
		    	public void run() {
		      		
		    		try {
						sleep(7000);
						finish();
						startActivity(new Intent("com.sco.onlineanket.AnaMenu"));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
		    		finally{
		    			finish();
		    		}		
				}
		    };
		    
		    Goruntule.start();   
		    
		    } 
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}





