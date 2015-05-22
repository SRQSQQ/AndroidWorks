package com.example.agedating;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class LogoActivity extends Activity {
	private final int SPLASH_DISPLAY_LENGHT = 5000;	//�ӳ�����
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.logo_main);
		
        /**
         * ����  �����µ�Activity
         */
        new Handler().postDelayed(new Runnable() {  
            public void run() {  
                Intent mainIntent = new Intent(LogoActivity.this,  
                        MainActivity.class);  
                LogoActivity.this.startActivity(mainIntent);	//�����µ�Activity
                LogoActivity.this.finish();	//�رյ�ǰActivity
            }  
  
        }, SPLASH_DISPLAY_LENGHT);          
    }	
}
