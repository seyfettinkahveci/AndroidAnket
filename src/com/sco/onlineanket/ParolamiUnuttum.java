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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ParolamiUnuttum extends Activity {
	Button PAnimsa,Geri;
	ProgressDialog dialog = null;
	EditText Mail;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parolamiunuttum);
		PAnimsa = (Button)findViewById(R.id.button1);  
		Geri = (Button)findViewById(R.id.button3);  
	    Mail= (EditText)findViewById(R.id.EPosta);
	    Geri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.KullaniciGirisi"));			
			}
	    });
	    PAnimsa.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog = ProgressDialog.show(ParolamiUnuttum.this, "", 
		                        "Kullanýcý Bulunuyor...", true);
						 new Thread(new Runnable() {
							    public void run() {
							    	login();					      
							    }
							  }).start();				
					}
		});
		
	}
	void login(){
		try{			
			 
			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://andanket.galerionline.net/ParolamiAnimsa.php"); 
			nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("Mail",Mail.getText().toString().trim()));  
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response); 
			runOnUiThread(new Runnable() {
			    public void run() {
					dialog.dismiss();
			    }
			});
			
			if(response.equalsIgnoreCase("Parola Degisti")){
				runOnUiThread(new Runnable() {
				    public void run() {
				    	Toast.makeText(ParolamiUnuttum.this,"Yeni Parolanýz Mail Adresinize Gönderildi", Toast.LENGTH_SHORT).show();
				    }
				});
				finish();
				startActivity(new Intent(ParolamiUnuttum.this, KullaniciGirisi.class));
			}else{
				showAlert();				
			}
			
		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Hata : " + e.getMessage());
		}
	}
	public void showAlert(){
		ParolamiUnuttum.this.runOnUiThread(new Runnable() {
		    public void run() {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(ParolamiUnuttum.this);
		    	builder.setTitle("Kullanýcý Bulunamadý");
		    	builder.setMessage("Kullanýcý Bulunamadý Lütfen Tekrar Deneyin!")  
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
