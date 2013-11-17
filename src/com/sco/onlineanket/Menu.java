package com.sco.onlineanket;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class Menu extends Activity {
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);        
		ImageView AnketOlustur=(ImageView) findViewById(R.id.imageButton2);
		ImageView Anketlerim=(ImageView) findViewById(R.id.imageButton1);
		ImageView SoruEkle=(ImageView) findViewById(R.id.imageButton3);
		ImageView SoruSilGuncelle=(ImageView) findViewById(R.id.imageButton4);
		ImageView Sonuclar=(ImageView) findViewById(R.id.imageButton5);
		ImageView Cikis=(ImageView) findViewById(R.id.imageButton6);
		Sonuclar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.Sonuclar"));	
			}
		});
		SoruSilGuncelle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.SoruSilGuncelle"));	
			}
		});
		SoruEkle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.SoruEkle"));	
			}
		});
		Anketlerim.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.Anketim"));	
			}
		});
		AnketOlustur.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.AnketOlustur"));	
			}
		});
		Cikis.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog = ProgressDialog.show(Menu.this, "", 
                        "Çýkýþ Yapýlýyor...", true);
				 new Thread(new Runnable() {
					    public void run() {
					    	Cik();					      
					    }
					  }).start();				
			}
		});
		
}
		void Cik(){
			try{			
				 
				httpclient=new DefaultHttpClient();
				httppost= new HttpPost("http://andanket.galerionline.net/Cikis.php"); 
				nameValuePairs = new ArrayList<NameValuePair>(1);
				nameValuePairs.add(new BasicNameValuePair("Cik","1"));  
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				final String response = httpclient.execute(httppost, responseHandler);
				System.out.println("Response : " + response); 
				runOnUiThread(new Runnable() {
				    public void run() {
						dialog.dismiss();
				    }
				});
				
				if(response.equalsIgnoreCase("Cikti")){
					runOnUiThread(new Runnable() {
					    public void run() {
					    	Toast.makeText(Menu.this,"Çýkýþ Baþarýyla Gerçekleþti", Toast.LENGTH_SHORT).show();
					    }
					});
					
					 finish();
				}else{
					showAlert();				
				}
				
			}catch(Exception e){
				dialog.dismiss();
				System.out.println("Hata : " + e.getMessage());
			}
		}
		public void showAlert(){
			Menu.this.runOnUiThread(new Runnable() {
			    public void run() {
			    	AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
			    	builder.setTitle("Hata");
			    	builder.setMessage("Çýkýþ Sýrasýnda Problem Meydana Geldi Lütfen Tekrar Deneyin!")  
			    	       .setCancelable(false)
			    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			    	           public void onClick(DialogInterface dialog, int id) {
			    	           }
			    	       });		    	       
			    	AlertDialog alert = builder.create();
			    	alert.show();		    	
			    }
			});
		}
}