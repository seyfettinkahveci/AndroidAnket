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

public class UyeOl extends Activity {
	Button UyeOl,Geri;
	ProgressDialog dialog = null;
	EditText Mail,KullaniciAdi,Parola;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uyeol);
		UyeOl = (Button)findViewById(R.id.button1);  
		Geri = (Button)findViewById(R.id.button3);  
	    Mail= (EditText)findViewById(R.id.EPosta);
	    KullaniciAdi= (EditText)findViewById(R.id.KullaniciAdi);
	    Parola= (EditText)findViewById(R.id.Parola);
	    Geri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.AnaMenu"));			
			}
	    });
	    UyeOl.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog = ProgressDialog.show(UyeOl.this, "", 
		                        "Kay�t ��lemi Yap�l�yor...", true);
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
			httppost= new HttpPost("http://andanket.galerionline.net/UyeOl.php"); 
			nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("Mail",Mail.getText().toString().trim()));  
			nameValuePairs.add(new BasicNameValuePair("KullaniciAdi",KullaniciAdi.getText().toString().trim()));  
			nameValuePairs.add(new BasicNameValuePair("Parola",Parola.getText().toString().trim()));  
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response); 
			runOnUiThread(new Runnable() {
			    public void run() {
					dialog.dismiss();
			    }
			});
			
			if(response.equalsIgnoreCase("Kayit Basarili")){
				runOnUiThread(new Runnable() {
				    public void run() {
				    	Toast.makeText(UyeOl.this,"�yeli�iniz Ba�ar�yla Ger�ekle�ti Te�ekk�rler", Toast.LENGTH_SHORT).show();
				    }
				});
				finish();
				    	startActivity(new Intent("com.sco.onlineanket.KullaniciGirisi"));	
			}
			else if(response.equalsIgnoreCase("Bu Kullanici Kayitlidir")){
				runOnUiThread(new Runnable() {
				    public void run() {
				    	Toast.makeText(UyeOl.this,"Girdi�iniz Bilgilere Ait Kullan�c� Sisteme Kay�tl�d�r", Toast.LENGTH_SHORT).show();
				    }
				});
			}
			else if(response.equalsIgnoreCase("Alanlari Doldurun")){
				runOnUiThread(new Runnable() {
				    public void run() {
				    	Toast.makeText(UyeOl.this,"L�tfen Bo� Alanlar� Doldurun", Toast.LENGTH_SHORT).show();
				    }
				});
			}
			else if(response.equalsIgnoreCase("Lutfen Uygun Mail Giriniz")){
				runOnUiThread(new Runnable() {
				    public void run() {
				    	Toast.makeText(UyeOl.this,"Mail Adresiniz Uygun De�il L�tfen Uygun Mail Giriniz", Toast.LENGTH_SHORT).show();
				    }
				});
			}

			else{
				showAlert();				
			}
			
		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Hata : " + e.getMessage());
		}
	}
	public void showAlert(){
		UyeOl.this.runOnUiThread(new Runnable() {
		    public void run() {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(UyeOl.this);
		    	builder.setTitle("Kay�t Hatas�");
		    	builder.setMessage("Kay�t S�ras�nda Hata �le Kar��la��ld� L�tfen Tekrar Deneyiniz")  
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
