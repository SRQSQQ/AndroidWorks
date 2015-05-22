package com.example.agedating;

import java.io.ByteArrayOutputStream;

import org.json.JSONObject;

import util.ValidateUtil;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class DatingActivity extends Activity {
	ImageView imageView;
	Bitmap bm;
	Dialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dating_main);
		
		View view = View.inflate(DatingActivity.this, R.layout.dialog_main, null);
		dialog = new Dialog(DatingActivity.this, R.style.MyDialog);
		dialog.setContentView(view);
		
		selectPhoto();	//选择照片并年龄检测		
	}
	
	/*
	 * 选择照片
	 */
	public void selectPhoto() {
		imageView = (ImageView) findViewById(R.id.imageView);

		String fileSrc = (String) getIntent().getExtras().get("fileSrc");		
		
		// 读大小
		Options options = new Options();
		options.inJustDecodeBounds = true;
		bm = BitmapFactory.decodeFile(fileSrc, options);

		// 位图
		options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
				(double) options.outWidth / 1024f,
				(double) options.outHeight / 1024f)));
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(fileSrc, options);
				
		imageView.setImageBitmap(bm);			
				
		agedating();
	}
	
	public void agedating() {
		dialog.show();
		FaceppDetect faceppDetect = new FaceppDetect();
		faceppDetect.setDetectCallback(new DetectCallback() {
			
			@Override
			public void detectResult(JSONObject rst) {
				// TODO Auto-generated method stub
				
				// 红画笔
				Paint paint = new Paint();
				paint.setColor(Color.WHITE);
				paint.setStrokeWidth(Math.max(bm.getWidth(),
						bm.getHeight()) / 300f);
				
				// 新建画布
				Bitmap bitmap = Bitmap.createBitmap(bm.getWidth(),
						bm.getHeight(), bm.getConfig());
				Canvas canvas = new Canvas(bitmap);
				canvas.drawBitmap(bm, new Matrix(), null);
				
				try {
					// 找到所以脸
					final int count = rst.getJSONArray("face").length();
					for (int i = 0; i < count; ++i) {
						float x, y, w, h;
						int age, range, realAge;
						String gender;

						// 获取中心点
						x = (float) rst.getJSONArray("face")
								.getJSONObject(i)
								.getJSONObject("position")
								.getJSONObject("center").getDouble("x");
						y = (float) rst.getJSONArray("face")
								.getJSONObject(i)
								.getJSONObject("position")
								.getJSONObject("center").getDouble("y");

						//获取脸的大小
						w = (float) rst.getJSONArray("face")
								.getJSONObject(i)
								.getJSONObject("position")
								.getDouble("width");
						h = (float) rst.getJSONArray("face")
								.getJSONObject(i)
								.getJSONObject("position")
								.getDouble("height");

						// 获得年龄
						age = rst.getJSONArray("face").getJSONObject(i)
								.getJSONObject("attribute")
								.getJSONObject("age").getInt("value");
						range = rst.getJSONArray("face")
								.getJSONObject(i)
								.getJSONObject("attribute")
								.getJSONObject("age").getInt("range");
						gender = rst.getJSONArray("face")
								.getJSONObject(i)
								.getJSONObject("attribute")
								.getJSONObject("gender")
								.getString("value");

						// 按百分比改为真实大小
						x = x / 100 * bm.getWidth();
						w = w / 100 * bm.getWidth() * 0.7f;
						y = y / 100 * bm.getHeight();
						h = h / 100 * bm.getHeight() * 0.7f;

						if (gender.equals("Male")) {
							gender = "男";
						} else if(gender.equals("Female")) {
							gender = "女";
						}

						paint.setTextSize(25);
						canvas.drawText(String.valueOf(age) + "岁", x - 5, y
								- h - 15, paint);
						canvas.drawText(String.valueOf(gender) + " ", x - 35, y
								- h - 15, paint);

						// 画出框标记出来
						canvas.drawLine(x - w, y - h, x - w, y + h,
								paint);
						canvas.drawLine(x - w, y - h, x + w, y - h,
								paint);
						canvas.drawLine(x + w, y + h, x - w, y + h,
								paint);
						canvas.drawLine(x + w, y + h, x + w, y - h,
								paint);
					}
					
					// 保存新图片
					bm = bitmap;

					DatingActivity.this.runOnUiThread(new Runnable() {

						public void run() {
							// show the image
							imageView.setImageBitmap(bm);
							dialog.dismiss();
							Toast.makeText(DatingActivity.this, "检测完成！", Toast.LENGTH_LONG).show();
						}
					});
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
		faceppDetect.detect(bm);
	}
		
	private class FaceppDetect {
		DetectCallback callback = null;

		public void setDetectCallback(DetectCallback detectCallback) {
			callback = detectCallback;
		}

		public void detect(final Bitmap image) {

			new Thread(new Runnable() {

				public void run() {
					HttpRequests httpRequests = new HttpRequests(
							"7601afcb5e8dab0db079dee5551d7a43",
							"Tm9D5WIw-IHYs0mvQCr7jPj2_YncGJ0T", true, false);
					// Log.v(TAG, "image size : " + img.getWidth() + " " +
					// img.getHeight());

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					float scale = Math.min(
							1,
							Math.min(600f / bm.getWidth(),
									600f / bm.getHeight()));
					Matrix matrix = new Matrix();
					matrix.postScale(scale, scale);

					Bitmap imgSmall = Bitmap.createBitmap(bm, 0, 0,
							bm.getWidth(), bm.getHeight(), matrix, false);
					// Log.v(TAG, "imgSmall size : " + imgSmall.getWidth() + " "
					// + imgSmall.getHeight());					

					imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					byte[] array = stream.toByteArray();

					try {
						// detect
						JSONObject result = httpRequests
								.detectionDetect(new PostParameters()
										.setImg(array));
						// finished , then call the callback function
						if (callback != null) {
							callback.detectResult(result);
						}
					} catch (FaceppParseException e) {
						e.printStackTrace();
						DatingActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								ValidateUtil.checkNetWork(DatingActivity.this);								
							}
						});
					}

				}
			}).start();
		}
	}

	
	interface DetectCallback {
		void detectResult(JSONObject rst);
	}
}
