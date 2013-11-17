package com.sco.onlineanket;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class AnaMenu extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ana_menu);
		ImageView Buton=(ImageView) findViewById(R.id.MenuCikis);
		ImageView Giris=(ImageView) findViewById(R.id.imageButton2);
		ImageView UyeOl=(ImageView) findViewById(R.id.imageButton1);
		ImageView AnketCevapla=(ImageView) findViewById(R.id.imageButton3);
		AnketCevapla.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.AnketCevapla"));
			}
		});
		Giris.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.KullaniciGirisi"));
			}
		});
		UyeOl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				startActivity(new Intent("com.sco.onlineanket.UyeOl"));
			}
		});
			Buton.setOnClickListener(new OnClickListener(){
				public void onClick(View arg0){
					 finish();
				}
			});
	}
}
