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

	private LinearLayout tablerow[];// ���б༭ҳ���һ��row,һ����6��row

	private ImageView ivFont[][];
	private PersonalShowEditView psev;

	private int screenWidth;// ��Ļ���
	private int screenHeight;// ��Ļ�߶�
	private int fontHeight;// ����ĸ߶ȣ���PersonalShowEditView���ֵ��һ����
	private int fontWidth;// �����ȣ���PersonalShowEditView���ֵ��һ���Ĺ�ϵ
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
	 * ��Ϣ��ʼ��
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
		 * ��ʼ����Ļ�ĸ߶ȺͿ��
		 */

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels; // ��Ļ��ȣ����أ�
		screenHeight = metric.heightPixels; // ��Ļ�߶ȣ����أ�

		/**
		 * ��ʼ������ĸ߶ȺͿ��
		 */
		fontHeight = screenHeight / 8;
		fontWidth = screenHeight / 10;

		int maxFont = screenWidth / fontWidth;// ÿһ�е�����������

		/**
		 * �½�����ͼƬ������ʾ���
		 */
		cursorimg = (ImageView) new ImageView(this.getApplication()
				.getApplicationContext());
		cursorimg.setBackgroundResource(R.drawable.cursors);
		int cursorimgHeight = (int) (fontHeight * 0.8);
		cursorimg.setLayoutParams(new LayoutParams(6, cursorimgHeight));// ȷ��crusorimg����ʾ��С
		tablerow[0].addView(cursorimg);
		handler.postDelayed(new Runnable() {
			public void run() {
				AnimationDrawable frameAnimation = (AnimationDrawable) cursorimg
						.getBackground();
				frameAnimation.start();
			}
		}, 50);

		/**
		 * �½�����imageView������������ˢ������
		 */
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < maxFont; j++) {
				ivFont[i][j] = new ImageView(this.getApplication()
						.getApplicationContext());
				ivFont[i][j].setLayoutParams(new LayoutParams(fontWidth,
						fontHeight));
			}

		/**
		 * ����button�ĳ�ʼ��
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
				 * �½�һ���װ�dialog������д���ݣ�������Ҫ�������dialog��Ļ�Ŀ��
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
											.setImageBitmap(mSignBitmap);// ָ����дͼ��
									// ָ����дͼ��Ĵ�С�����ֵĴ�С
									tablerow[xloc].removeView(cursorimg);
									tablerow[xloc].addView(ivFont[xloc][yloc]);

									yloc = yloc + 1;
									int myfontwidth = (yloc + 1) * fontWidth;// ����һ�����Ժ��������
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
									/* ��ʾ��Ϣ��ֻ������ôһ������ */
									Toast.makeText(getApplicationContext(),
											"ֻ������һ�����֣�", 100);
								}
							}
						});
				writeTabletDialog.show();
			}
		});
	}

	/**
	 * ��ÿһ��button�����ϼ�����ť������¼�
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
		 * �ո�ť����Ӧ�¼�����������ť��ͬ������û��ͼƬ���ڶ�Ӧ��λ����
		 */
		blankButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (xloc < 6) {
					// ָ����дͼ��Ĵ�С�����ֵĴ�С
					ivFont[xloc][yloc].setImageBitmap(null);// ָ����дͼ��
					tablerow[xloc].removeView(cursorimg);
					tablerow[xloc].addView(ivFont[xloc][yloc]);

					yloc = yloc + 1;
					int myfontwidth = (yloc + 1) * fontWidth;// ����һ�����Ժ��������
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
					/* ��ʾ��Ϣ��ֻ������ôһ������ */
					Toast.makeText(PracticeActivity.this, "ֻ������һ�����֣�",
							Toast.LENGTH_SHORT).show();
				}
			}

		});

		enterButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				tablerow[xloc].removeView(cursorimg);
				if (xloc < 5) {
					for (int j = yloc; j < 6; j++) {
						// ָ����дͼ��Ĵ�С�����ֵĴ�С
						ivFont[xloc][j].setImageBitmap(null);// ָ����дͼ��
						tablerow[xloc].addView(ivFont[xloc][j]);
					}

					xloc = xloc + 1;
					yloc = 0;

				} else {
					/* ��ʾ��Ϣ��ֻ������ôһ������ */
					Toast.makeText(PracticeActivity.this, "ֻ������һ�����֣�",
							Toast.LENGTH_SHORT).show();
				}

				tablerow[xloc].addView(cursorimg);
			}

		});

		delButton.setOnClickListener(new OnClickListener() {

		
			public void onClick(View v) {
				if(xloc == 0 && yloc == 0)
					return;
				
				if(xloc < 6)//���һ���ֵ�λ�����ջ�ʹxloc, ylocλ�ڣ�6�� 0��
					tablerow[xloc].removeView(cursorimg);// �Ƴ����
				else//�������һ�������Ĺ��λ�ڵ�6�е�6���ֵ�λ��
					tablerow[5].removeView(cursorimg);
				
				yloc = yloc - 1;

				if (yloc == -1)// ����ǵ�һ�еĵ�һ��ͼƬ���Ƶ���һ��
				{
					xloc = xloc - 1;
					yloc = 5;
				}
				tablerow[xloc].removeView(ivFont[xloc][yloc]);// �Ƴ�ͼƬ
				tablerow[xloc].addView(cursorimg);// ���Ϲ��
			}

		});

		delAllButton.setOnClickListener(new OnClickListener() {

	
			public void onClick(View v) {
				// �Ƴ����е�view
				for (int i = 0; i < 6; i++) {
					tablerow[i].removeAllViews();
				}
				tablerow[0].addView(cursorimg);// ����һ�м��Ϲ��
				xloc = 0;
				yloc = 0;// ���õ�ǰλ��Ϊ��һ�е�һ��
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
					// ��ñ���ͼƬ��·��
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
											// �����˵�
											Intent intent = new Intent(
													Intent.ACTION_SEND);
											intent.setType("image/jpeg");
											// intent.putExtra(Intent.EXTRA_SUBJECT,
											// "����");
											intent.putExtra(
													Intent.EXTRA_STREAM, Uri
															.fromFile(new File(
																	savePath)));
											intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											startActivity(Intent.createChooser(
													intent, "������"));
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
					Toast.makeText(PracticeActivity.this, "�洢ͼƬ�쳣��",
							Toast.LENGTH_LONG).show();
				} else if (result == 2) {
					Toast.makeText(PracticeActivity.this, "�洢�������ã�",
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