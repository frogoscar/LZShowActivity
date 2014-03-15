package com.xhhlz.lzshow;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FontLibContentActivity extends Activity {

	private Button backButton;
	private Button enterButton;
	private TextView title;
	private TextView content;
	private String fontLibName = null;
	private ProgressDialog progressDialog = null;
	private static final String DEFAULT_TRAINING_FONTLIB_FOLDER = "books/";
	private static final String DEFAULT_STRING_TYPE = "string";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		fontLibName = intent.getStringExtra(FontLibActivity.FONT_LIB_SELECTION);
		if (fontLibName == null) {
			finish();
			return;
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fontlib_content);

		progressDialog = ProgressDialog.show(FontLibContentActivity.this,
				"Please wait...", "loading font library list...", true);
		initView();
		addAllListener();

		new Thread(new Runnable() {

			public void run() {
				String words = null;
				try {
					words = readFromAssertFileByFileName(fontLibName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (words != null)
					content.setText(words);
				else
					Toast.makeText(FontLibContentActivity.this,
							R.string.toast_exception_fontlib, Toast.LENGTH_LONG)
							.show();
				progressDialog.dismiss();
			}
		}).run();
	}

	private void initView() {
		backButton = (Button) findViewById(R.id.content_back);
		enterButton = (Button) findViewById(R.id.enter_fontlib_btn);
		title = (TextView) findViewById(R.id.content_title);
		content = (TextView) findViewById(R.id.content);

		title.setText(getString(getResources().getIdentifier(fontLibName,
				DEFAULT_STRING_TYPE, getPackageName())));
	}

	private void addAllListener() {
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

		enterButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(FontLibContentActivity.this,
						PractiseActivity.class);
				intent.putExtra(FontLibActivity.FONT_LIB_SELECTION, fontLibName);
				startActivity(intent);
				finish();
			}
		});
	}

	private String readFromAssertFileByFileName(String filename)
			throws IOException {
		InputStream in = getResources().getAssets().open(
				DEFAULT_TRAINING_FONTLIB_FOLDER + filename
						+ PractiseActivity.DEFAULT_SUFFIX);
		// get the number of bytes of the file
		int length = in.available();
		// create byte array
		byte[] buffer = new byte[length];
		// read the data from the file to the byte array
		in.read(buffer);
		return EncodingUtils.getString(buffer, PractiseActivity.ENCODING);
	}

}
