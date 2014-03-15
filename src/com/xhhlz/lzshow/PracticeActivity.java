package com.xhhlz.lzshow;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xhhlz.lzshow.listener.WritePadDialogListener;
import com.xhhlz.lzshow.ui.PersonalShowEditView;
import com.xhhlz.lzshow.ui.WritePadDialog;
import com.xhhlz.lzshow.util.ScreenShoot;

public class PracticeActivity extends Activity {
	/** Called when the activity is first created. */
	private Handler handler = new Handler();
	private ImageView cursorimg;
	private Bitmap mSignBitmap;

	private LinearLayout tablerow[];// 进行编辑页面的一个row,一共有6个row

	private ImageView ivFont[][];
	private PersonalShowEditView psev;

	private int screenWidth;// 屏幕宽度
	private int screenHeight;// 屏幕高度
	private int fontHeight;// 字体的高度，与PersonalShowEditView里的值是一样的
	private int fontWidth;// 字体宽度，与PersonalShowEditView里的值有一定的关系
	private int xloc = 0;
	private int yloc = 0;
	private int cap[] = { 0, 0, 0, 0, 0, 0 };

	private Button widthButton;
	private Button blankButton;
	private Button enterButton;
	private Button delButton;
	private Button delAllButton;
	private Button saveButton;

	/**
	 * 信息初始化
	 */
	private void init() {
		tablerow = new LinearLayout[6];
		ivFont = new ImageView[6][10];

		tablerow[0] = (LinearLayout) this
				.findViewById(R.id.personalshow_table_view_row1);
		tablerow[1] = (LinearLayout) this
				.findViewById(R.id.personalshow_table_view_row2);
		tablerow[2] = (LinearLayout) this
				.findViewById(R.id.personalshow_table_view_row3);
		tablerow[3] = (LinearLayout) this
				.findViewById(R.id.personalshow_table_view_row4);
		tablerow[4] = (LinearLayout) this
				.findViewById(R.id.personalshow_table_view_row5);
		tablerow[5] = (LinearLayout) this
				.findViewById(R.id.personalshow_table_view_row6);

		/**
		 * 初始化屏幕的高度和宽度
		 */

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels; // 屏幕宽度（像素）
		screenHeight = metric.heightPixels; // 屏幕高度（像素）

		/**
		 * 初始化字体的高度和宽度
		 */
		fontHeight = screenHeight / 8;
		fontWidth = screenHeight / 10;

		int maxFont = screenWidth / fontWidth;// 每一行的最多字体个数

		/**
		 * 新建光标的图片用来显示光标
		 */
		cursorimg = (ImageView) new ImageView(this.getApplication()
				.getApplicationContext());
		cursorimg.setBackgroundResource(R.drawable.cursors);
		int cursorimgHeight = (int) (fontHeight * 0.8);
		cursorimg.setLayoutParams(new LayoutParams(6, cursorimgHeight));// 确定crusorimg的显示大小
		tablerow[0].addView(cursorimg);
		handler.postDelayed(new Runnable() {
			public void run() {
				AnimationDrawable frameAnimation = (AnimationDrawable) cursorimg
						.getBackground();
				frameAnimation.start();
			}
		}, 50);

		/**
		 * 新建若干imageView，便于在上面刷新字体
		 */
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < maxFont; j++) {
				ivFont[i][j] = new ImageView(this.getApplication()
						.getApplicationContext());
				ivFont[i][j].setLayoutParams(new LayoutParams(fontWidth,
						fontHeight));
			}

		/**
		 * 所有button的初始化
		 */
		widthButton = (Button) this.findViewById(R.id.personalshow_view_width);
		blankButton = (Button) this.findViewById(R.id.personalshow_view_blank);
		enterButton = (Button) this.findViewById(R.id.personalshow_view_enter);
		delButton = (Button) this.findViewById(R.id.personalshow_view_del);
		delAllButton = (Button) this
				.findViewById(R.id.personalshow_view_delAll);
		saveButton = (Button) this.findViewById(R.id.personalshow_view_save);

	}


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_show);

		init();
		addAllListener();

		psev = (PersonalShowEditView) this
				.findViewById(R.id.personalshow_edit_view);
		psev.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				/**
				 * 新建一个白板dialog用于书写数据，其中需要给予这个dialog屏幕的宽度
				 */
				WritePadDialog writeTabletDialog = new WritePadDialog(
						PracticeActivity.this, screenWidth, screenHeight,
						new WritePadDialogListener() {

							public void refreshActivity(Object object) {
								if(object == null)
									return;
								mSignBitmap = (Bitmap) object;
								if (xloc < 6) {
									ivFont[xloc][yloc]
											.setImageBitmap(mSignBitmap);// 指定手写图形
									// 指定手写图像的大小就是字的大小
									tablerow[xloc].removeView(cursorimg);
									tablerow[xloc].addView(ivFont[xloc][yloc]);

									yloc = yloc + 1;
									int myfontwidth = (yloc + 1) * fontWidth;// 增加一个字以后的字体宽度
									if (myfontwidth > screenWidth) {
										cap[xloc] = yloc;
										xloc = xloc + 1;
										yloc = 0;
									}
									
									if (xloc < 6)
										tablerow[xloc].addView(cursorimg);
									else
										tablerow[5].addView(cursorimg);
								} else {
									/* 提示信息，只能有这么一屏文字 */
									Toast.makeText(getApplicationContext(),
											"只能输入一屏文字！", 100);
								}
							}
						});
				writeTabletDialog.show();
			}
		});
	}

	/**
	 * 给每一个button都加上监听按钮点击的事件
	 */
	private void addAllListener() {
		widthButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// train_button.setBackgroundResource(R.drawable.button_yellowon);
				Toast.makeText(PracticeActivity.this, R.string.maintain_info, Toast.LENGTH_SHORT).show();
			}

		});

		/**
		 * 空格按钮的响应事件，与正常按钮相同，但是没有图片放在对应的位置上
		 */
		blankButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (xloc < 6) {
					// 指定手写图像的大小就是字的大小
					ivFont[xloc][yloc].setImageBitmap(null);// 指定手写图形
					tablerow[xloc].removeView(cursorimg);
					tablerow[xloc].addView(ivFont[xloc][yloc]);

					yloc = yloc + 1;
					int myfontwidth = (yloc + 1) * fontWidth;// 增加一个字以后的字体宽度
					if (myfontwidth > screenWidth) {
						cap[xloc] = yloc;
						xloc = xloc + 1;
						yloc = 0;
					}
					
					if (xloc < 6)
						tablerow[xloc].addView(cursorimg);
					else
						tablerow[5].addView(cursorimg);
				} else {
					/* 提示信息，只能有这么一屏文字 */
					Toast.makeText(PracticeActivity.this, "只能输入一屏文字！",
							Toast.LENGTH_SHORT).show();
				}
			}

		});

		enterButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				tablerow[xloc].removeView(cursorimg);
				if (xloc < 5) {
					for (int j = yloc; j < 6; j++) {
						// 指定手写图像的大小就是字的大小
						ivFont[xloc][j].setImageBitmap(null);// 指定手写图形
						tablerow[xloc].addView(ivFont[xloc][j]);
					}

					xloc = xloc + 1;
					yloc = 0;

				} else {
					/* 提示信息，只能有这么一屏文字 */
					Toast.makeText(PracticeActivity.this, "只能输入一屏文字！",
							Toast.LENGTH_SHORT).show();
				}

				tablerow[xloc].addView(cursorimg);
			}

		});

		delButton.setOnClickListener(new OnClickListener() {

		
			public void onClick(View v) {
				if(xloc == 0 && yloc == 0)
					return;
				
				if(xloc < 6)//最后一个字的位置最终会使xloc, yloc位于（6， 0）
					tablerow[xloc].removeView(cursorimg);// 移除光标
				else//对于最后一个字它的光标位于第6行第6个字的位置
					tablerow[5].removeView(cursorimg);
				
				yloc = yloc - 1;

				if (yloc == -1)// 如果是第一行的第一个图片，移到上一行
				{
					xloc = xloc - 1;
					yloc = 5;
				}
				tablerow[xloc].removeView(ivFont[xloc][yloc]);// 移除图片
				tablerow[xloc].addView(cursorimg);// 加上光标
			}

		});

		delAllButton.setOnClickListener(new OnClickListener() {

	
			public void onClick(View v) {
				// 移除所有的view
				for (int i = 0; i < 6; i++) {
					tablerow[i].removeAllViews();
				}
				tablerow[0].addView(cursorimg);// 给第一行加上光标
				xloc = 0;
				yloc = 0;// 设置当前位置为第一行第一列
			}

		});

		saveButton.setOnClickListener(new OnClickListener() {

		
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// train_button.setBackgroundResource(R.drawable.button_yellowon);
				ScreenShoot myss = new ScreenShoot(screenWidth, screenHeight,
						getWindow(), "/LZShow/practice");
				int result = myss.getCurrentImage();
				if (result == 0) {
					// 获得保存图片的路径
					final String savePath = myss.getFilePath();
					new AlertDialog.Builder(PracticeActivity.this)
							.setIcon(android.R.drawable.ic_dialog_info)
							.setTitle(R.string.save_dialog_title)
							.setMessage(R.string.save_dialog_message)
							.setPositiveButton(R.string.save_dialog_confirmbtn,
									new DialogInterface.OnClickListener() {

									
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
											// 弹出菜单
											Intent intent = new Intent(
													Intent.ACTION_SEND);
											intent.setType("image/jpeg");
											// intent.putExtra(Intent.EXTRA_SUBJECT,
											// "分享");
											intent.putExtra(
													Intent.EXTRA_STREAM, Uri
															.fromFile(new File(
																	savePath)));
											intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											startActivity(Intent.createChooser(
													intent, "分享至"));
										}
									})
							.setNegativeButton(R.string.save_dialog_cancelBtn,
									new DialogInterface.OnClickListener() {

									
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.cancel();
											finish();
										}
									}).create().show();
				} else if (result == 1) {
					Toast.makeText(PracticeActivity.this, "存储图片异常！",
							Toast.LENGTH_LONG).show();
				} else if (result == 2) {
					Toast.makeText(PracticeActivity.this, "存储卡不可用！",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.finish();
	}

}