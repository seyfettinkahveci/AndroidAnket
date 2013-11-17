package com.sco.onlineanket;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AnketGuncelleSil extends Activity {
	ProgressDialog dialog,Dialog2 = null;
	HttpPost httppost,httppost2;
	StringBuffer buffer;
	HttpResponse response,response2;
	HttpClient httpclient,httpclient2;
	List<NameValuePair> nameValuePairs,nameValuePairs2;
	EditText Baslik;
	Button Geri,Guncelle,Sil;
	ArrayList<HashMap<String, String>> mArrayList = new ArrayList<HashMap<String, String>>();
	ListView list;
	String gelen;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.anketguncellesil);
		 gelen =getIntent().getExtras().getString("Giden");
		Geri = (Button)findViewById(R.id.button3);  
		Guncelle = (Button)findViewById(R.id.button1);  
		Sil = (Button)findViewById(R.id.button2);  
		Baslik= (EditText)findViewById(R.id.Baslik);
		Geri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.Anketim"));	
			
			}
	    });
		Guncelle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Dialog2 = ProgressDialog.show(AnketGuncelleSil.this, "", 
		                "Anket Güncelleniyor...", true);
				 new Thread(new Runnable() {
						@Override
					    public void run() {
					   	Guncelle();					      
					    }
					  }).start();
			
			}
	    });
		Sil.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Dialog2 = ProgressDialog.show(AnketGuncelleSil.this, "", 
		                "Anket Siliniyor...", true);
				 new Thread(new Runnable() {
						@Override
					    public void run() {
					   	Sil();					      
					    }
					  }).start();
			
			}
	    });
		dialog = ProgressDialog.show(AnketGuncelleSil.this, "", 
                "Anket Bulunuyor...", true);
		 new Thread(new Runnable() {
			 @Override
			    public void run() {
			    	Olus();					      
			    }
			  }).start();
	}//on create
	public void Olus(){
		try{			
			 
			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://andanket.galerionline.net/AnketGSil.php"); 
			SharedPreferences KID1 = getSharedPreferences("KID",MODE_PRIVATE);
			SharedPreferences KID = getSharedPreferences("KID",MODE_PRIVATE);
			SharedPreferences.Editor KIDE = KID.edit();
			if(gelen!=null){
				KIDE.putString("Eski",gelen);
			}
			KIDE.commit();
	        String KAdi = KID1.getString("KullaniciAdi", "");
			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("KullaniciAdi",KAdi));  
			nameValuePairs.add(new BasicNameValuePair("Ara",gelen)); 
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpclient.execute(httppost, responseHandler);
			final String result = response.toString();
									
			runOnUiThread(new Runnable() {
				@Override
			    public void run() {
					dialog.dismiss();
					EditText Text1 =(EditText)findViewById(R.id.Baslik);
					Text1.setText(result);
			    }
			});
		if(response.equalsIgnoreCase("GirisYap")){
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	Toast.makeText(AnketGuncelleSil.this,"Anketleri Görüntülemek Ýçin Yetkiniz Yok", Toast.LENGTH_SHORT).show();
				    }
				});
				finish();
				startActivity(new Intent(AnketGuncelleSil.this, KullaniciGirisi.class));
			}
			else if(response.equalsIgnoreCase("Gelmedi")){
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	Toast.makeText(AnketGuncelleSil.this,"Boþ Alanlarý Doldurunuz", Toast.LENGTH_SHORT).show();
				    }
				});
				finish();
				startActivity(new Intent(AnketGuncelleSil.this, KullaniciGirisi.class));
			}
		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Hata : " + e.getMessage());
		}

	}
	public void Guncelle(){
		try{			
			 
			httpclient2=new DefaultHttpClient();
			httppost2= new HttpPost("http://andanket.galerionline.net/AnketGuncelle.php"); 
			SharedPreferences KID1 = getSharedPreferences("KID",MODE_PRIVATE);
	        String KAdi = KID1.getString("KullaniciAdi", "");
	        String Eski = KID1.getString("Eski", "");
			nameValuePairs2 = new ArrayList<NameValuePair>(3);
			nameValuePairs2.add(new BasicNameValuePair("KullaniciAdi",KAdi));  
			nameValuePairs2.add(new BasicNameValuePair("Yeni",Baslik.getText().toString().trim())); 
			nameValuePairs2.add(new BasicNameValuePair("Eski",Eski)); 
			httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
			ResponseHandler<String> responseHandler2 = new BasicResponseHandler();
			String response2 = httpclient2.execute(httppost2, responseHandler2);
									
			runOnUiThread(new Runnable() {
				@Override
			    public void run() {
			    	Dialog2.dismiss();
			    }
			});
		if(response2.equalsIgnoreCase("Guncellendi")){
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	Toast.makeText(AnketGuncelleSil.this,"Bilgiler Baþarýyla Güncellendi", Toast.LENGTH_SHORT).show();
				    }
				});
				finish();
				startActivity(new Intent(AnketGuncelleSil.this, Anketim.class));
			}
		else if(response2.equalsIgnoreCase("GirisYap")){
			runOnUiThread(new Runnable() {
				@Override
			    public void run() {
			    	Toast.makeText(AnketGuncelleSil.this,"Anketleri Görüntülemek Ýçin Yetkiniz Yok", Toast.LENGTH_SHORT).show();
			    }
			});
			finish();
			startActivity(new Intent(AnketGuncelleSil.this, KullaniciGirisi.class));
		}
			else if(response2.equalsIgnoreCase("Gelmedi")){
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	Toast.makeText(AnketGuncelleSil.this,"Boþ Alanlarý Doldurunuz", Toast.LENGTH_SHORT).show();
				    }
				});
			}
			else if(response2.equalsIgnoreCase("Yanlis")){
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	
				    	Toast.makeText(AnketGuncelleSil.this,"Bilgiler Sistemle Eþleþmemektedir!", Toast.LENGTH_SHORT).show();
				    }
				});
			}
			else if(response2.equalsIgnoreCase("Hata")){
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	Toast.makeText(AnketGuncelleSil.this,"Bilgiler Güncellenirken Hata Ýle Karþýlaþýldý", Toast.LENGTH_SHORT).show();
				    }
				});
			}
			else{
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	Toast.makeText(AnketGuncelleSil.this,"Hata Meydana Geldi", Toast.LENGTH_SHORT).show();
				    }
				});
			}
		}catch(Exception e){
			Dialog2.dismiss();
			System.out.println("Hata bu: " + e.getMessage());
		}

	}
	public void Sil(){
		try{			
			 
			httpclient2=new DefaultHttpClient();
			httppost2= new HttpPost("http://andanket.galerionline.net/AnketSil.php"); 
			SharedPreferences KID1 = getSharedPreferences("KID",MODE_PRIVATE);
	        String KAdi = KID1.getString("KullaniciAdi", "");
	        String Eski = KID1.getString("Eski", "");
			nameValuePairs2 = new ArrayList<NameValuePair>(3);
			nameValuePairs2.add(new BasicNameValuePair("KullaniciAdi",KAdi));  
			nameValuePairs2.add(new BasicNameValuePair("Yeni",Baslik.getText().toString().trim())); 
			nameValuePairs2.add(new BasicNameValuePair("Eski",Eski)); 
			httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
			ResponseHandler<String> responseHandler2 = new BasicResponseHandler();
			String response2 = httpclient2.execute(httppost2, responseHandler2);
									
			runOnUiThread(new Runnable() {
				@Override
			    public void run() {
			    	Dialog2.dismiss();
			    }
			});
		if(response2.equalsIgnoreCase("Silindi")){
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	Toast.makeText(AnketGuncelleSil.this,"Anket Baþarýyla Silindi", Toast.LENGTH_SHORT).show();
				    }
				});
				finish();
				startActivity(new Intent(AnketGuncelleSil.this, Anketim.class));
			}
		else if(response2.equalsIgnoreCase("GirisYap")){
			runOnUiThread(new Runnable() {
				@Override
			    public void run() {
			    	Toast.makeText(AnketGuncelleSil.this,"Bu Ýþlem Ýçin Yetkiniz Yok", Toast.LENGTH_SHORT).show();
			    }
			});
			finish();
			startActivity(new Intent(AnketGuncelleSil.this, KullaniciGirisi.class));
		}
			else if(response2.equalsIgnoreCase("Gelmedi")){
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	Toast.makeText(AnketGuncelleSil.this,"Boþ Alanlarý Doldurunuz", Toast.LENGTH_SHORT).show();
				    }
				});
			}
			else if(response2.equalsIgnoreCase("Yanlis")){
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	
				    	Toast.makeText(AnketGuncelleSil.this,"Bilgiler Sistemle Eþleþmemektedir!", Toast.LENGTH_SHORT).show();
				    }
				});
			}
			else if(response2.equalsIgnoreCase("Hata")){
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	Toast.makeText(AnketGuncelleSil.this,"Anket Silinirken Hata Ýle Karþýlaþýldý", Toast.LENGTH_SHORT).show();
				    }
				});
			}
			else{
				runOnUiThread(new Runnable() {
					@Override
				    public void run() {
				    	Toast.makeText(AnketGuncelleSil.this,"Hata Meydana Geldi", Toast.LENGTH_SHORT).show();
				    }
				});
			}
		}catch(Exception e){
			Dialog2.dismiss();
			System.out.println("Hata bu: " + e.getMessage());
		}

	}
}
