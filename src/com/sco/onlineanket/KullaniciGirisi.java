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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class KullaniciGirisi extends Activity {
	Button b,PAnimsa,Geri;
	EditText et,pass;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kullanicigirisi);
		  b = (Button)findViewById(R.id.button1);  
		  PAnimsa = (Button)findViewById(R.id.button2);  
		  Geri = (Button)findViewById(R.id.button3);  
	      et = (EditText)findViewById(R.id.KullaniciAdi);
	      pass= (EditText)findViewById(R.id.Parola);
	        
	        b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog = ProgressDialog.show(KullaniciGirisi.this, "", 
	                        "Kullanýcý Doðrulama...", true);
					 new Thread(new Runnable() {
						    public void run() {
						    	login();					      
						    }
						  }).start();				
				}
			});
	        Geri.setOnClickListener(new OnClickListener() {
	    				@Override
	    				public void onClick(View v) {
	    					finish();
	    					startActivity(new Intent(KullaniciGirisi.this,AnaMenu.class));	
	    				}
	    	});
	        PAnimsa.setOnClickListener(new OnClickListener() {
	    				@Override
	    				public void onClick(View v) {
	    					finish();
	    					startActivity(new Intent("com.sco.onlineanket.ParolamiUnuttum"));				
	    				}
	    	});
	}
	void login(){
		try{			
			 
			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://andanket.galerionline.net/Giris.php"); // make sure the url is correct.
			//add your data
			nameValuePairs = new ArrayList<NameValuePair>(2);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
			nameValuePairs.add(new BasicNameValuePair("KullaniciAdi",et.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
			nameValuePairs.add(new BasicNameValuePair("Parola",pass.getText().toString().trim())); 
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//Execute HTTP Post Request
			response=httpclient.execute(httppost);
			// edited by James from coderzheaven.. from here....
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response); 
			runOnUiThread(new Runnable() {
			    public void run() {
					dialog.dismiss();
			    }
			});
			
			if(response.equalsIgnoreCase("Kullanici Bulunamadi")){
				showAlert();	
				
			}else{			
				runOnUiThread(new Runnable() {
				    public void run() {
				    	SharedPreferences KID = getSharedPreferences("KID",MODE_PRIVATE);
						SharedPreferences.Editor KIDE = KID.edit();
						KIDE.putString("KullaniciAdi",et.getText().toString().trim());
						KIDE.commit();
				    	Toast.makeText(KullaniciGirisi.this,"Baþarýyla Giriþ Yapýldý", Toast.LENGTH_SHORT).show();
				    }
				});
				
				 	
				    
				finish();
				startActivity(new Intent("com.sco.onlineanket.Menu"));	
			}
			
		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Hata : " + e.getMessage());
		}
	}
	public void showAlert(){
		KullaniciGirisi.this.runOnUiThread(new Runnable() {
		    public void run() {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(KullaniciGirisi.this);
		    	builder.setTitle("Giriþ Yapýlamadý");
		    	builder.setMessage("Girdiðiniz Bilgilere Ait Kullanýcý Bulunamadý Lütfen Tekrar Deneyin!")  
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
