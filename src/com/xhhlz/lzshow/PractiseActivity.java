package com.xhhlz.lzshow;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.xhhlz.lzshow.constant.UserRecord;
import com.xhhlz.lzshow.listener.PractiseWriteViewListener;
import com.xhhlz.lzshow.provider.RecordUtil;
import com.xhhlz.lzshow.provider.UserRecordUtil;
import com.xhhlz.lzshow.ui.PractiseWriteView;

/**
 * 字帖练字界面
 * 
 * @author kelvin
 * 
 */
public class PractiseActivity extends Activity {

	private Button backButton;// return to last menu
	private Button fontlibButton;// font library button
	private Button lastButton;// last word
	private Button nextButton;// next word
	private Button clearButton;// clear the word

	// private PractiseWriteView writeView; // button to draw the white board

	private static int SCREEN_HEIGHT = 0;
	private static int SCREEN_WIDTH = 0;

	private static String words = null;
	private static String curWord = null;
	private static int curWordIndex = 0;
	private static int wordSum = 0;
	private static int writeSum = 0;

	public static final String DEFAULT_SUFFIX = ".txt";
	public static final String ENCODING = "GB2312";
	private static final String DEFAULT_TRAINING_FONTLIB = "fontlib500";
	private static final String DEFAULT_TRAINING_FONTLIB_FOLDER = "txt/";

	private static Resources resource;
	private static String packageName;

	private static final String DEFAULT_STRING_TYPE = "string";

	private static String curLibName = null;
	
	private PractiseWriteView writeView;

	public static void writeSumPlusPlus() {
		writeSum++;
	}

	private static void resetStaticVariable() {
		words = null;
		curWord = null;
		curWordIndex = 0;
		wordSum = 0;
		writeSum = 0;
		curLibName = null;
	}

	public void init() {
		backButton = (Button) findViewById(R.id.practise_button_back);
		fontlibButton = (Button) findViewById(R.id.practise_fontlib_btn);
		lastButton = (Button) findViewById(R.id.practise_previous_btn);
		nextButton = (Button) findViewById(R.id.practise_next_btn);
		clearButton = (Button) findViewById(R.id.practise_clear_btn);

	    writeView =
		 (PractiseWriteView)findViewById(R.id.practise_write_view);
	    writeView.addPractiseWriteViewListener(new PractiseWriteViewListener()
	    {
			public void isShare() {
				// TODO Auto-generated method stub
//				new AlertDialog.Builder(PractiseActivity.this)
//				.setIcon(android.R.drawable.ic_dialog_info)
//				.setTitle(R.string.save_dialog_title)
//				.setMessage(R.string.save_dialog_message)
//				.setPositiveButton(R.string.save_dialog_confirmbtn,
//						new DialogInterface.OnClickListener() {
//
//						
//							public void onClick(
//									DialogInterface dialog,
//									int which) {
//								// TODO Auto-generated method stub
//								dialog.dismiss();
//								ScreenShoot myss = new ScreenShoot(SCREEN_WIDTH, SCREEN_HEIGHT,
//										getWindow(), "/LZShow/training");
//								int result = myss.getCurrentImage();
//								String savePath = new String();
//								if (result == 0) {
//									// 获得保存图片的路径
//									savePath = myss.getFilePath();
//								}
//								else if (result == 1) {
//									Toast.makeText(PractiseActivity.this, "存储图片异常！",
//											Toast.LENGTH_LONG).show();
//									return;
//								} else if (result == 2) {
//									Toast.makeText(PractiseActivity.this, "存储卡不可用！",
//											Toast.LENGTH_LONG).show();
//									return;
//								}
//								// 弹出菜单
//								Intent intent = new Intent(
//										Intent.ACTION_SEND);
//								intent.setType("image/jpeg");
//								// intent.putExtra(Intent.EXTRA_SUBJECT,
//								// "分享");
//								intent.putExtra(
//										Intent.EXTRA_STREAM, Uri
//												.fromFile(new File(
//														savePath)));
//								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//								startActivity(Intent.createChooser(
//										intent, "分享至"));
//							}
//						})
//				.setNegativeButton(R.string.save_dialog_cancelBtn,
//						new DialogInterface.OnClickListener() {
//
//						
//							public void onClick(
//									DialogInterface dialog,
//									int which) {
//								// TODO Auto-generated method stub
//								dialog.cancel();
//							}
//						}).create().show();
			}
	    	
	    });
	}

	private static boolean setPreviousWord() {
		if (curWordIndex == 0) {
			return false;
		}

		curWordIndex--;
		curWord = words.substring(curWordIndex, curWordIndex + 1);
		return true;
	}

	private static boolean setNextWord() {
		if (curWordIndex == wordSum - 1)
			return false;

		curWordIndex++;
		curWord = words.substring(curWordIndex, curWordIndex + 1);
		return true;
	}

	public static String getCurWord() {
		return curWord;
	}

	private ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Display display = getWindowManager().getDefaultDisplay();
		SCREEN_HEIGHT = display.getHeight();// screen height 
		SCREEN_WIDTH = display.getWidth();// screen width

		super.onCreate(savedInstanceState);

		resource = getResources();
		packageName = getPackageName();
		resetStaticVariable();

		progressDialog = ProgressDialog.show(PractiseActivity.this, "Please wait...",
				"loading user data...", true);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.practise_activity_layout);

		init();
		addAllListener();
	}

	private void readFromAssertFileByFileName(String filename)
			throws IOException {
		InputStream in = getResources().getAssets().open(
				DEFAULT_TRAINING_FONTLIB_FOLDER + filename + DEFAULT_SUFFIX);
		// get the number of bytes of the file
		int length = in.available();
		// create byte array
		byte[] buffer = new byte[length];
		// read the data from the file to byte array
		in.read(buffer);
		words = EncodingUtils.getString(buffer, ENCODING);
	}

	// private boolean readFromAssertFileByFontLibName(String fontLibName)
	// throws IOException {
	// String fontLib = fontLibName.split(":")[0];
	//
	// if (getString(R.string.fontlib500).equals(fontLib))
	// readFromAssertFileByFileName("fontlib500");
	// else if (getString(R.string.fontlib2500).equals(fontLib))
	// readFromAssertFileByFileName("fontlib2500");
	// else if (getString(R.string.beautiful_words).equals(fontLib))
	// readFromAssertFileByFileName("beautiful_words");
	// else
	// return false;
	//
	// return true;
	// }

	@Override
	protected void onDestroy() {
		if (wordSum > 0) {
			// write Database
			if (!RecordUtil.addWriteSumToDb(PractiseActivity.this, writeSum))
				Toast.makeText(PractiseActivity.this,
						R.string.toast_exception_db, Toast.LENGTH_SHORT).show();
		}
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent != null) {
			String selection = intent
					.getStringExtra(FontLibActivity.FONT_LIB_SELECTION);
			if (selection != null) {
				curLibName = selection;
				try {
					readFromAssertFileByFileName(curLibName);
				} catch (IOException e) {
					e.printStackTrace();
				}
				int retIndex = UserRecordUtil.queryCurWordIndexByFontLibName(
						curLibName, PractiseActivity.this);
				curWordIndex = ((retIndex == -1) ? 0 : retIndex);
				wordSum = words.length();
				curWord = words.substring(curWordIndex, curWordIndex + 1);
			}
		}
	}

	/**
	 * All the Listener functions
	 */
	public void addAllListener() {
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}

		});

		fontlibButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// prompt the font library Activity
				Intent intent = new Intent(PractiseActivity.this,
						FontLibActivity.class);
				startActivity(intent);

			}

		});

		lastButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (setPreviousWord())
					if (PractiseWriteView.ifWrite()) {
						writeSum++;
						PractiseWriteView.clear();
					}
			}

		});

		nextButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (setNextWord())
					if (PractiseWriteView.ifWrite()) {
						writeSum++;
						PractiseWriteView.clear();
					}
			}

		});

		clearButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (PractiseWriteView.ifWrite()) {
					writeSum++;
					PractiseWriteView.clear();// static function to clear the screen
				}
			}

		});
	}

	@Override
	protected void onResume() {
		if (words == null)
			new Thread(new Runnable() {
				public void run() {
					// read user practice history, time-consuming
					UserRecord latestRecord = null;
					try {
						latestRecord = UserRecordUtil
								.queryLatestUserRecord(PractiseActivity.this);
						if (latestRecord != null) {
							curLibName = latestRecord.getFontLibName();
							curWordIndex = latestRecord.getUpdateIndex();
							try {
								readFromAssertFileByFileName(curLibName);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					// By default, load fontlib500.txt
					if (words == null) {
						curLibName = DEFAULT_TRAINING_FONTLIB;
						try {
							readFromAssertFileByFileName(curLibName);
						} catch (IOException e) {
							e.printStackTrace();
						}
						curWordIndex = 0;
					}

					wordSum = words.length();
					curWord = words.substring(curWordIndex, curWordIndex + 1);

					progressDialog.dismiss();
				}
			}).start();
		super.onResume();
	}

	public static String getFontLibName(Context context) {
		String fontLibName = "";

		fontLibName += context.getString(resource.getIdentifier(curLibName,
				DEFAULT_STRING_TYPE, packageName));
		fontLibName += ": ";
		fontLibName += ((curWordIndex + 1) + "/" + wordSum);

		return fontLibName;
	}

	public static String getCurFontLib() {
		return curLibName;
	}

	public static int getCurWordIndex() {
		return curWordIndex;
	}

	public static int getSCREEN_HEIGHT() {
		return SCREEN_HEIGHT;
	}

	public static int getSCREEN_WIDTH() {
		return SCREEN_WIDTH;
	}

}
