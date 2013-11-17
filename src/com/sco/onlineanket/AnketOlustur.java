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

public class AnketOlustur extends Activity {
	Button Olustur,Geri;
	ProgressDialog dialog = null;
	EditText Baslik;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.anketolustur);
		Olustur = (Button)findViewById(R.id.button1);  
		Geri = (Button)findViewById(R.id.button3);  
		Baslik= (EditText)findViewById(R.id.Baslik);
	    Geri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.Menu"));			
			}
	    });
	    Olustur.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = ProgressDialog.show(AnketOlustur.this, "", 
                        "Anket Oluþturuluyor...", true);
				 new Thread(new Runnable() {
					    public void run() {
					    	Olus();					      
					    }
					  }).start();				
			}
});
	}//on create
	void Olus(){
		try{			
			 
			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://andanket.galerionline.net/AnketOlustur.php"); 
			SharedPreferences KID1 = getSharedPreferences("KID",MODE_PRIVATE);
	        String KAdi = KID1.getString("KullaniciAdi", "");
			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("Baslik",Baslik.getText().toString().trim()));  
			nameValuePairs.add(new BasicNameValuePair("KullaniciAdi",KAdi));  
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response); 
			runOnUiThread(new Runnable() {
			    public void run() {
					dialog.dismiss();
			    }
			});
			
			if(response.equalsIgnoreCase("Olustu")){
				runOnUiThread(new Runnable() {
				    public void run() {
				    	Toast.makeText(AnketOlustur.this,"Anketiniz Baþarýyla Oluþturuldu", Toast.LENGTH_SHORT).show();
				    }
				});
				finish();
				startActivity(new Intent(AnketOlustur.this, Menu.class));
			}
			else if(response.equalsIgnoreCase("Alanlari Doldurun")){
				runOnUiThread(new Runnable() {
				    public void run() {
				    	Toast.makeText(AnketOlustur.this,"Anketin Baþlýðý Boþ Olamaz!", Toast.LENGTH_SHORT).show();
				    }
				});
			}
			else if(response.equalsIgnoreCase("GirisYap")){
				runOnUiThread(new Runnable() {
				    public void run() {
				    	Toast.makeText(AnketOlustur.this,"Anket Oluþturmak Ýçin Yetkiniz Yok", Toast.LENGTH_SHORT).show();
				    }
				});
				finish();
				startActivity(new Intent(AnketOlustur.this, KullaniciGirisi.class));
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
		AnketOlustur.this.runOnUiThread(new Runnable() {
		    public void run() {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(AnketOlustur.this);
		    	builder.setTitle("Hata");
		    	builder.setMessage("Anket Olusturulurken Hata Meydana Geldi Lütfen Tekrar Deneyin!")  
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
