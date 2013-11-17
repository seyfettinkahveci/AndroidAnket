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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Anketim extends Activity {
	ProgressDialog dialog = null;
	EditText Soru,A,B,C,D,E;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	Button Geri;
	ArrayList<HashMap<String, String>> mArrayList = new ArrayList<HashMap<String, String>>();
	ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.anketlerim);
		Geri = (Button)findViewById(R.id.button3);  
		list	=(ListView)findViewById(R.id.listView1);
		Geri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.Menu"));	
			
			}
	    });

			new AnketGetir().execute();
		    list.setClickable(true);
		    list.setTextFilterEnabled(true);
		    list.setOnItemClickListener(new OnItemClickListener()
		    {
			    public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
			    {      
			         Intent Yeni = new Intent (Anketim.this,AnketGuncelleSil.class);
			        Yeni.putExtra("Giden", list.getItemAtPosition(position).toString());	

						finish();
			         startActivity(Yeni);
			    }
		     });

		  
	}//on create
	
	class AnketGetir extends AsyncTask<String, String, Void>
	{
	private ProgressDialog progressDialog = new ProgressDialog(Anketim.this);
	    InputStream is = null ;
	    String result = "";
	    protected void onPreExecute() {
	       progressDialog.setMessage("Sistemdeki Anketleriniz Yükleniyor...");
	       progressDialog.show();
	     }
	       @Override
		protected Void doInBackground(String... params) {
		  String url_select = "http://andanket.galerionline.net/Anketler.php"; 
		  SharedPreferences KID1 = getSharedPreferences("KID",MODE_PRIVATE);
	      String KAdi = KID1.getString("KullaniciAdi", "");
		  HttpClient httpClient = new DefaultHttpClient();
		  HttpPost httpPost = new HttpPost(url_select);
	      ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
		    try {
		    param.add(new BasicNameValuePair("KullaniciAdi",KAdi));
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
			    	Toast.makeText(Anketim.this,"Anketleri Görüntülemek Ýçin Yetkiniz Yok", Toast.LENGTH_SHORT).show();
					finish();
					startActivity(new Intent(Anketim.this, KullaniciGirisi.class));
	    		}
		 		else if(Cevap.contains("AnketYok")){
			    	Toast.makeText(Anketim.this,"Sistemimizde Size Ait Anket Bulunamadý", Toast.LENGTH_SHORT).show();
					finish();
					startActivity(new Intent(Anketim.this, Menu.class));
	    		}
		 		
			    SimpleAdapter simpleAdpt = new SimpleAdapter(Anketim.this, mArrayList, android.R.layout.simple_list_item_1, new String[] {"Baslik"}, new int[] {android.R.id.text1}); 
			    list.setAdapter(simpleAdpt);
			} catch (Exception e) {
				Log.e("log_tag", "JSON Hatasý : "+e.toString());
			}
		}
	}	
	
}
