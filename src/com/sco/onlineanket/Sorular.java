package com.sco.onlineanket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Sorular extends Activity {	public static final Context context = null;
ProgressDialog dialog = null;
	public TextView Soru;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	Button Geri,Ileri;
	public 	RadioButton A,B,C,D,E;
	public 	String Cevap;
	ArrayList<HashMap<String, String>> mArrayList = new ArrayList<HashMap<String, String>>();
	ListView list;String gelen;
	String Sorum,AnketB;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sorular);

		 gelen =getIntent().getExtras().getString("Giden");
		   	SharedPreferences KID = getSharedPreferences("KID",MODE_PRIVATE);
	   		SharedPreferences.Editor KIDE = KID.edit();
				if(gelen!=null){
					KIDE.putString("AnketB",gelen);
				}
			KIDE.commit();	
		Geri = (Button)findViewById(R.id.button3);  
		Ileri = (Button)findViewById(R.id.button1);  
		A	=(RadioButton)findViewById(R.id.A);
		B	=(RadioButton)findViewById(R.id.B);
		C	=(RadioButton)findViewById(R.id.C);
		D	=(RadioButton)findViewById(R.id.D);
		E	=(RadioButton)findViewById(R.id.E);
		Soru=(TextView)findViewById(R.id.textView2);
		
		
		Ileri.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Cevap="";
				Sorum=Soru.getText().toString().trim();
				SharedPreferences KID1 = getSharedPreferences("KID",MODE_PRIVATE);
		         AnketB = KID1.getString("AnketB", "");
				if(A.isChecked()){
					Cevap="A";
					A.setChecked(false);
				 }else if(B.isChecked()){
						Cevap="B";
						B.setChecked(false);
				 }else if(C.isChecked()){
						Cevap="C";
						C.setChecked(false);
				 }else if(D.isChecked()){
						Cevap="D";
						D.setChecked(false);
				}else if(E.isChecked()){
					Cevap="E";
					E.setChecked(false);
		        }
				else{
					Cevap="F";					
				}

			new SSoruGetir().execute();
			}
		});
		Geri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.AnaMenu"));	
			
			}
	    });

		new SoruGetir().execute();
	}//on  create
	
class SoruGetir extends AsyncTask<String, String, Void>
	{
	private ProgressDialog progressDialog = new ProgressDialog(Sorular.this);
	    InputStream is = null ;
	    String result = "";
	    protected void onPreExecute() {
	       progressDialog.setMessage("Sistemdeki Sorular Yükleniyor...");
	       progressDialog.show();
	     }
	       @Override
		protected Void doInBackground(String... params) {
		  String url_select = "http://andanket.galerionline.net/SoruGetir.php";
		  HttpClient httpClient = new DefaultHttpClient();
		  HttpPost httpPost = new HttpPost(url_select);
	          ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
		    try {  
		    param.add(new BasicNameValuePair("Anket",gelen));
			httpPost.setEntity(new UrlEncodedFormEntity(param));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();

			//read content
			is =  httpEntity.getContent();					

			} catch (Exception e) {
				Log.e("log_tag", "Http Baðlantý Hatasý "+e.toString());
			}
		try {
		    BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = "";
			while((line=br.readLine())!=null)
			{
			   sb.append(line+"\n");
			}
				is.close();
				result=sb.toString();		

					} catch (Exception e) {
						Log.e("log_tag", "Veri Dönüþtürme Problemi : "+e.toString());
					}

				return null;

			}
		protected void onPostExecute(Void v) {
			String Tesekkur = null;
			String AYok = null;
			String Bulunamadi = null;
			try {
					Log.e("log_tag", "Tesekkur : "+result);
		            JSONArray jArray = new JSONArray(result);
		            for(int i=0;i<jArray.length();i++){
	                    JSONObject json_data = jArray.getJSONObject(i);
	                    Soru.setText(json_data.getString("Soru"));
	                    A.setText(json_data.getString("A"));
	                    B.setText(json_data.getString("B"));
	                    C.setText(json_data.getString("C"));
	                    D.setText(json_data.getString("D"));
	                    E.setText(json_data.getString("E"));
	                     Tesekkur=json_data.getString("Tesekkur");
	                     AYok=json_data.getString("AYok");
	                     Bulunamadi=json_data.getString("Bulunamadi");
	            }
					this.progressDialog.dismiss();
            		if(Tesekkur.contains("1")){
    			    	Toast.makeText(Sorular.this,"Sorularýmýz Bitti Teþekkürler", Toast.LENGTH_SHORT).show();
            		  	finish();
    					startActivity(new Intent("com.sco.onlineanket.Tebrikler"));	
		    		}
		    		else if(Bulunamadi.contains("1")){
		    			Toast.makeText(Sorular.this,"Ankete Ait Sistemde Soruyu Bulamadýk!", Toast.LENGTH_SHORT).show();
            		  	finish();
		    			startActivity(new Intent("com.sco.onlineanket.AnketCevapla"));	
		    		}
		    		else if(AYok.contains("1")){
		    		  	Toast.makeText(Sorular.this,"Gönderdiðiniz Bilgiler Sisteme Eriþmedi", Toast.LENGTH_SHORT).show();
            		  	finish();
		    		  	startActivity(new Intent("com.sco.onlineanket.AnketCevapla"));		   
		    		}
			    
			} catch (Exception e) {
				Log.e("log_tag", "JSON Hatasý : "+e.toString());
			}
		}
	}
class SSoruGetir extends AsyncTask<String, String, Void>
{
private ProgressDialog progressDialog = new ProgressDialog(Sorular.this);
    InputStream is = null ;
    String result = "";
    protected void onPreExecute() {
       progressDialog.setMessage("Cevaplar Kaydediliyor...");
       progressDialog.show();
     }
       @Override
	protected Void doInBackground(String... params) {
	  String url_select = "http://andanket.galerionline.net/SoruCevapla.php";
	  HttpClient httpClient = new DefaultHttpClient();
	  HttpPost httpPost = new HttpPost(url_select);
          ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
	    try {  
	    param.add(new BasicNameValuePair("Anket",AnketB));
	    param.add(new BasicNameValuePair("Soru",Sorum));
	    param.add(new BasicNameValuePair("Cevap",Cevap));
		httpPost.setEntity(new UrlEncodedFormEntity(param));
		HttpResponse httpResponse = httpClient.execute(httpPost);	
		HttpEntity httpEntity = httpResponse.getEntity();
		//read content
		is =  httpEntity.getContent();		


		} catch (Exception e) {
			Log.e("log_tag", "Http Baðlantý Hatasý "+e.toString());
		}
	try {
	    BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = "";
		while((line=br.readLine())!=null)
		{
		   sb.append(line+"\n");
		}
			is.close();
			result=sb.toString();	
				} catch (Exception e) {
					Log.e("log_tag", "Veri Dönüþtürme Problemi : "+e.toString());
				}

			return null;

		}
	protected void onPostExecute(Void v) {
		String Tesekkur = null;
		String AYok = null;
		String Bulunamadi = null;
		


				try {
					Log.e("log_tag", "Tesekkur : "+result);
			            JSONArray jArray = new JSONArray(result);
			            for(int i=0;i<jArray.length();i++){
			                    JSONObject json_data = jArray.getJSONObject(i);
			                    Soru.setText(json_data.getString("Soru"));
			                    A.setText(json_data.getString("A"));
			                    B.setText(json_data.getString("B"));
			                    C.setText(json_data.getString("C"));
			                    D.setText(json_data.getString("D"));
			                    E.setText(json_data.getString("E"));
			                     Tesekkur=json_data.getString("Tesekkur");
			                     AYok=json_data.getString("AYok");
			                     Bulunamadi=json_data.getString("Bulunamadi");
			            }
						this.progressDialog.dismiss();
	            		if(Tesekkur.contains("1")){
	            			    	Toast.makeText(Sorular.this,"Sorularýmýz Bitti Teþekkürler", Toast.LENGTH_SHORT).show();
	    	            		  	finish();
	            					startActivity(new Intent("com.sco.onlineanket.Tebrikler"));	
	            		}
	            		else if(Bulunamadi.contains("1")){
	            			Toast.makeText(Sorular.this,"Ankete Ait Sistemde Soruyu Bulamadýk!", Toast.LENGTH_SHORT).show();
	            		  	finish();
	            			startActivity(new Intent("com.sco.onlineanket.AnketCevapla"));	
	            		}
	            		else if(AYok.contains("1")){
	            		  	Toast.makeText(Sorular.this,"Gönderdiðiniz Bilgiler Sisteme Eriþmedi", Toast.LENGTH_SHORT).show();
	            		  	finish();
	    					startActivity(new Intent("com.sco.onlineanket.AnketCevapla"));	  
	            		}
				} catch (Exception e) {
				
					Log.e("log_tag", "Gelen Result : "+e.toString());
		
				}

	}
}
}