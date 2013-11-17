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
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Sonuclar extends Activity {
	Button Geri;
	ArrayList<String> mArrayList;
	ProgressDialog dialog = null;
	 Spinner spin;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	String Anket;
	ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sonuclar);
		Geri = (Button)findViewById(R.id.button3);  
		list	=(ListView)findViewById(R.id.listView1);
		Geri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.Menu"));	
			
			}
	    });
		dialog = ProgressDialog.show(Sonuclar.this, "", 
                "Anketler Listeleniyor...", true);
		  spin = (Spinner) findViewById(R.id.spinner1);			
		  spin.setOnItemSelectedListener(new OnItemSelectedListener() {
			    @Override
			    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			    	Anket=spin.getSelectedItem().toString();
			    	if(Anket!="Seçiniz"){
			    		new Sonucum().execute();
			    	}
			    }

			    @Override
			    public void onNothingSelected(AdapterView<?> parentView) {
			        // your code here
			    }

			});

         mArrayList = new ArrayList<String>();
         mArrayList.add("Seçiniz");
		 new Thread(new Runnable() {
			    public void run() {
			    	Olus();					      
			    }
			  }).start();


			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_list_item_1, mArrayList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin.setAdapter(adapter);

	}//oncreate
	class Sonucum extends AsyncTask<String, String, Void>
	{
	private ProgressDialog progressDialog = new ProgressDialog(Sonuclar.this);
	    InputStream is = null ;
	    String result = "";
	    protected void onPreExecute() {
	       progressDialog.setMessage("Ankete Ait Sonuçlar Yükleniyor...");
	       progressDialog.show();
	     }
	       @Override
		protected Void doInBackground(String... params) {
		  String url_select = "http://andanket.galerionline.net/Sonuclar.php"; 
		  SharedPreferences KID1 = getSharedPreferences("KID",MODE_PRIVATE);
	      String KAdi = KID1.getString("KullaniciAdi", "");
		  HttpClient httpClient = new DefaultHttpClient();
		  HttpPost httpPost = new HttpPost(url_select);
	      ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
		    try {
		    param.add(new BasicNameValuePair("KullaniciAdi",KAdi));
		    param.add(new BasicNameValuePair("Anket",Anket));
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
			String Cevap = null;
			try {
				ArrayList<HashMap<String, String>> mArrayList = new ArrayList<HashMap<String, String>>();
	            JSONArray jArray = new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
              	  	HashMap<String, String> map = new HashMap<String, String>(); 
                        JSONObject json_data = jArray.getJSONObject(i);
                        map.put("Baslik",  json_data.getString("Baslik"));
                        Cevap=json_data.getString("Cevap");
                        mArrayList.add(map);
                        Log.i("log_tag","Baslik: "+json_data.getString("Baslik")
                        );
           
                }
				this.progressDialog.dismiss();
		 		if(Cevap.contains("GirisYap")){
			    	Toast.makeText(Sonuclar.this,"Sonuçlarý Görüntülemek Ýçin Yetkiniz Yok", Toast.LENGTH_SHORT).show();
					finish();
					startActivity(new Intent(Sonuclar.this, Menu.class));
	    		}
		 		else if(Cevap.contains("SoruYok")){
			    	Toast.makeText(Sonuclar.this,"Sistemimizde Ankete Ait Sorular Bulunamadý", Toast.LENGTH_SHORT).show();
	    		}
		 		else if(Cevap.contains("AnketSec")){
			    	Toast.makeText(Sonuclar.this,"Ýþlem Yapabilmeniz Ýçin Anketlerinizden Birini Seçmeniz Gerekmektedir.", Toast.LENGTH_SHORT).show();

	    		}
		 		
			    SimpleAdapter simpleAdpt = new SimpleAdapter(Sonuclar.this, mArrayList, android.R.layout.simple_list_item_1, new String[] {"Baslik"}, new int[] {android.R.id.text1}); 
			    list.setAdapter(simpleAdpt);
			} catch (Exception e) {
				Log.e("log_tag", "JSON Hatasý : "+e.toString());
			}
		}
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
				    	Toast.makeText(Sonuclar.this,"Anketleri Görüntülemek Ýçin Yetkiniz Yok", Toast.LENGTH_SHORT).show();
				    }
				});
				finish();
				startActivity(new Intent(Sonuclar.this, KullaniciGirisi.class));
			}
			
		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Hata : " + e.getMessage());
		}

	}
}
