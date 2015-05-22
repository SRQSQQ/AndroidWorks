package com.example.agedating;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	ImageButton imgBtn;
	private Bitmap bitmap = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		imgBtn = (ImageButton) findViewById(R.id.imgBtn);
		imgBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, 1);
			}
		});			
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (1 == requestCode) {
			if (data != null) {
				Cursor cursor = getContentResolver().query(data.getData(),
						null, null, null, null);
				cursor.moveToFirst();
				int idx = cursor.getColumnIndex(ImageColumns.DATA);
				String fileSrc = cursor.getString(idx);	//获取照片文件
				
				Intent intent = new Intent(MainActivity.this, DatingActivity.class);
				intent.putExtra("fileSrc", fileSrc);
				startActivity(intent);				
			}			
		}
	}
	
	
}
