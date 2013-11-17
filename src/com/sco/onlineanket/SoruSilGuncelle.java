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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SoruSilGuncelle extends Activity {
	ProgressDialog dialog = null;
	EditText Soru,A,B,C,D,E;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	String Sil;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	Button Geri;
	ArrayList<HashMap<String, String>> mArrayList = new ArrayList<HashMap<String, String>>();
	ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sorusilguncelle);
		Geri = (Button)findViewById(R.id.button3);  
		list	=(ListView)findViewById(R.id.listView1);
		Geri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.Menu"));	
			
			}
	    });
			new SoruGetir().execute();
		    list.setClickable(true);
		    list.setTextFilterEnabled(true);
		    list.setOnItemClickListener(new OnItemClickListener()
		    {
			    public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
			    {      
					dialog = ProgressDialog.show(SoruSilGuncelle.this, "", 
			                "Soru Siliniyor...", true);
					Sil=list.getItemAtPosition(position).toString();
					 new Thread(new Runnable() {
						    public void run() {
						    	Sil();					      
						    }
					 }).start();
			    }
		     });
		
	}//on create

	///
	public void Sil(){
		try{			
			 
			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://andanket.galerionline.net/SoruSil.php"); 
			SharedPreferences KID1 = getSharedPreferences("KID",MODE_PRIVATE);
	        String KAdi = KID1.getString("KullaniciAdi", "");
			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("KullaniciAdi",KAdi));  
			nameValuePairs.add(new BasicNameValuePair("Eski",Sil)); 
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			ResponseHandler<String> responseHandler2 = new BasicResponseHandler();
			String response2 = httpclient.execute(httppost, responseHandler2);
									
			runOnUiThread(new Runnable() {
				@Override
			    public void run() {
			    	dialog.dismiss();
			    }
			});
		if(response2.equalsIgnoreCase("Silindi")){
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	Toast.makeText(SoruSilGuncelle.this,"Soru Baþarýyla Silindi", Toast.LENGTH_SHORT).show();
				    }
				});
				finish();
				startActivity(new Intent(SoruSilGuncelle.this, Menu.class));
			}
		else if(response2.equalsIgnoreCase("GirisYap")){
			runOnUiThread(new Runnable() {
				@Override
			    public void run() {
			    	Toast.makeText(SoruSilGuncelle.this,"Bu Ýþlem Ýçin Yetkiniz Yok", Toast.LENGTH_SHORT).show();
			    }
			});
			finish();
			startActivity(new Intent(SoruSilGuncelle.this, KullaniciGirisi.class));
		}
			else if(response2.equalsIgnoreCase("Gelmedi")){
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	Toast.makeText(SoruSilGuncelle.this,"Boþ Alanlarý Doldurunuz", Toast.LENGTH_SHORT).show();
				    }
				});
			}
			else if(response2.equalsIgnoreCase("Yanlis")){
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	
				    	Toast.makeText(SoruSilGuncelle.this,"Bilgiler Sistemle Eþleþmemektedir!", Toast.LENGTH_SHORT).show();
				    }
				});
			}
			else if(response2.equalsIgnoreCase("Hata")){
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	Toast.makeText(SoruSilGuncelle.this,"Soru Silinirken Hata Ýle Karþýlaþýldý", Toast.LENGTH_SHORT).show();
				    }
				});
			}
			else{
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	Toast.makeText(SoruSilGuncelle.this,"Hata Meydana Geldi", Toast.LENGTH_SHORT).show();
				    }
				});
			}
		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Hata bu: " + e.getMessage());
		}

	}
	
	
	
	class SoruGetir extends AsyncTask<String, String, Void>
	{
	private ProgressDialog progressDialog = new ProgressDialog(SoruSilGuncelle.this);
	    InputStream is = null ;
	    String result = "";
	    protected void onPreExecute() {
	       progressDialog.setMessage("Sistemdeki Sorularýnýz Yükleniyor...");
	       progressDialog.show();
	     }
	       @Override
		protected Void doInBackground(String... params) {
		  String url_select = "http://andanket.galerionline.net/Sorularim.php"; 
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
                        map.put("Soru",  json_data.getString("Soru"));
                        Cevap=json_data.getString("Cevap");
                        mArrayList.add(map);
                        Log.i("log_tag","Baslik: "+json_data.getString("Soru")
                        );
           
                }
				this.progressDialog.dismiss();
		 		if(Cevap.contains("GirisYap")){
			    	Toast.makeText(SoruSilGuncelle.this,"Sorularý Görüntülemek Ýçin Yetkiniz Yok", Toast.LENGTH_SHORT).show();
					finish();
					startActivity(new Intent(SoruSilGuncelle.this, KullaniciGirisi.class));
	    		}
		 		else if(Cevap.contains("SoruYok")){
			    	Toast.makeText(SoruSilGuncelle.this,"Sistemimizde Size Ait Soru Bulunamadý", Toast.LENGTH_SHORT).show();
					finish();
					startActivity(new Intent(SoruSilGuncelle.this, Menu.class));
	    		}
		 		
			    SimpleAdapter simpleAdpt = new SimpleAdapter(SoruSilGuncelle.this, mArrayList, android.R.layout.simple_list_item_1, new String[] {"Soru"}, new int[] {android.R.id.text1}); 
			    list.setAdapter(simpleAdpt);
			} catch (Exception e) {
				Log.e("log_tag", "JSON Hatasý : "+e.toString());
			}
		}
	}	
	
	
	
	
}
