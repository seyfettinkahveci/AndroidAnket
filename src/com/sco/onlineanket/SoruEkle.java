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
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
public class SoruEkle extends Activity implements OnItemSelectedListener{
	ProgressDialog dialog = null;
	EditText Soru,A,B,C,D,E;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	Button Geri,Olustur;
	ArrayList<String> mArrayList;
	 Spinner spin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.soruekle);
		Geri = (Button)findViewById(R.id.button3);  
		Olustur = (Button)findViewById(R.id.button1);
		Soru	=(EditText)findViewById(R.id.editText1);
		A	=(EditText)findViewById(R.id.editText2);
		B	=(EditText)findViewById(R.id.editText3);
		C	=(EditText)findViewById(R.id.editText4);
		D	=(EditText)findViewById(R.id.editText5);
		E	=(EditText)findViewById(R.id.editText6);
		Geri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.Menu"));	
			
			}
	    });
		dialog = ProgressDialog.show(SoruEkle.this, "", 
                "Anketler Listeleniyor...", true);
		

         mArrayList = new ArrayList<String>();
         mArrayList.add("Seçiniz");
		 new Thread(new Runnable() {
			    public void run() {
			    	Olus();					      
			    }
			  }).start();

		  spin = (Spinner) findViewById(R.id.spinner1);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, mArrayList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin.setAdapter(adapter);
		Olustur.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = ProgressDialog.show(SoruEkle.this, "", 
                        "Sorular Ekleniyor...", true);
				 new Thread(new Runnable() {
					    public void run() {
					    	Ekle();					      
					    }
					  }).start();	
		
			    }
		});

			}//on create
	void Ekle(){
		try{			
			 
			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://andanket.galerionline.net/SoruEkle.php"); 
			SharedPreferences KID1 = getSharedPreferences("KID",MODE_PRIVATE);
	        String KAdi = KID1.getString("KullaniciAdi", "");
			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("Soru",Soru.getText().toString().trim()));  
			nameValuePairs.add(new BasicNameValuePair("A",A.getText().toString().trim()));   
			nameValuePairs.add(new BasicNameValuePair("B",B.getText().toString().trim()));   
			nameValuePairs.add(new BasicNameValuePair("C",C.getText().toString().trim()));   
			nameValuePairs.add(new BasicNameValuePair("D",D.getText().toString().trim()));   
			nameValuePairs.add(new BasicNameValuePair("E",E.getText().toString().trim()));   
			nameValuePairs.add(new BasicNameValuePair("Anket",spin.getSelectedItem().toString()));  
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
				    	Toast.makeText(SoruEkle.this,"Sorular Başarıyla Eklendi", Toast.LENGTH_SHORT).show();
				    }
				});
				finish();
				startActivity(new Intent(SoruEkle.this, Menu.class));
			}
			else if(response.equalsIgnoreCase("Alanlari Doldurun")){
				runOnUiThread(new Runnable() {
				    public void run() {
				    	Toast.makeText(SoruEkle.this,"Lütfen Boş ALanları Doldurun!", Toast.LENGTH_SHORT).show();
				    }
				});
			}
			else if(response.equalsIgnoreCase("GirisYap")){
				runOnUiThread(new Runnable() {
				    public void run() {
				    	Toast.makeText(SoruEkle.this,"Sorun Eklemek İçin Yetkiniz Yok", Toast.LENGTH_SHORT).show();
				    }
				});
				finish();
				startActivity(new Intent(SoruEkle.this, KullaniciGirisi.class));
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
		SoruEkle.this.runOnUiThread(new Runnable() {
		    public void run() {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(SoruEkle.this);
		    	builder.setTitle("Hata");
		    	builder.setMessage("Soru Eklenirken Hata Meydana Geldi Lütfen Tekrar Deneyin!")  
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
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
	  }
	 
	  @Override
	  public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	  }
	
	void Olus(){
		try{			
			 
			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://andanket.galerionline.net/Anketler.php"); 
			SharedPreferences KID1 = getSharedPreferences("KID",MODE_PRIVATE);
	        String KAdi = KID1.getString("KullaniciAdi", "");
			nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("KullaniciAdi",KAdi));  
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpclient.execute(httppost, responseHandler);
			
			String result = response.toString();
            JSONArray jArray = new JSONArray(result);
                  for(int i=0;i<jArray.length();i++){

                          JSONObject json_data = jArray.getJSONObject(i);
                          mArrayList.add(new String(json_data.getString("Baslik")));
                          Log.i("log_tag","Baslik: "+json_data.getString("Baslik")
                          );
             
                  }
									
			runOnUiThread(new Runnable() {
			    public void run() {
					dialog.dismiss();
			    }
			});
		if(response.equalsIgnoreCase("GirisYap")){
				runOnUiThread(new Runnable() {
				    public void run() {
				    	Toast.makeText(SoruEkle.this,"Anketlere Soru Eklemek İçin Yetkiniz Yok", Toast.LENGTH_SHORT).show();
				    }
				});
				finish();
				startActivity(new Intent(SoruEkle.this, KullaniciGirisi.class));
			}
			
		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Hata : " + e.getMessage());
		}

	}

	    }