package com.xhhlz.lzshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.xhhlz.lzshow.ui.PractiseWriteView;

public class FontLibActivity extends Activity {

	private static final String LEFT_VIEW = "left_view";
	private static final String MIDDLE_VIEW = "middle_view";
	private static final String RIGHT_VIEW = "right_view";
	private static final String DEF_DRAWABLE_TYPE = "drawable";

	public static final String FONT_LIB_SELECTION = "font_lib_selection";

	private static final String[] FONT_LIB_NAMESET = { "fontlib500",
			"fontlib2500", "famous_words", "songci300", "tangshi300", "dream" };

	private ListView fontLibListView;
	private Button backButton;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fontlib_layout);
		fontLibListView = (ListView) findViewById(R.id.fontlib_listview);
		backButton = (Button) findViewById(R.id.back);
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		initFontLibListView();
		fontLibListView.setAdapter(fontLibAdapter);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (PractiseWriteView.ifWrite())
			PractiseWriteView.clear();
	}

	private List<Map<String, Object>> fontLibItemList = null;

	private FontLibListViewAdapter fontLibAdapter = null;

	private void initFontLibListView() {
		fontLibItemList = new ArrayList<Map<String, Object>>();
		// need to fill fontLibItemList
		// ...

		Map<String, Object> test1 = new HashMap<String, Object>();
		test1.put(LEFT_VIEW, FONT_LIB_NAMESET[0]);
		test1.put(MIDDLE_VIEW, FONT_LIB_NAMESET[1]);
		test1.put(RIGHT_VIEW, FONT_LIB_NAMESET[2]);
		Map<String, Object> test2 = new HashMap<String, Object>();
		test2.put(LEFT_VIEW, FONT_LIB_NAMESET[3]);
		test2.put(MIDDLE_VIEW, FONT_LIB_NAMESET[4]);
		test2.put(RIGHT_VIEW, FONT_LIB_NAMESET[5]);
		Map<String, Object> empty = new HashMap<String, Object>();

		fontLibItemList.add(test1);
		fontLibItemList.add(test2);
		fontLibItemList.add(empty);
		fontLibItemList.add(empty);

		fontLibAdapter = new FontLibListViewAdapter();
	}

	/**
	 * Font Library List Adapter
	 * 
	 * @author kelvin
	 * 
	 */
	private class FontLibListViewAdapter extends BaseAdapter {

		/**
		 * Font Library List
		 * 
		 * @author kelvin
		 * 
		 */
		final class FontLibListItemView {
			public ImageView leftView;
			public ImageView middleView;
			public ImageView rightView;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return fontLibItemList.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return fontLibItemList.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			@SuppressWarnings("unchecked")
			Map<String, Object> fontLibListEntity = (Map<String, Object>) getItem(position);
			if (fontLibListEntity != null) {
				FontLibListItemView fontLibListItemView = null;
				if (convertView == null) {
					fontLibListItemView = new FontLibListItemView();
					convertView = LayoutInflater.from(FontLibActivity.this)
							.inflate(R.layout.fontlib_item, null);
					fontLibListItemView.leftView = (ImageView) convertView
							.findViewById(R.id.leftImageView);
					fontLibListItemView.middleView = (ImageView) convertView
							.findViewById(R.id.middleImageView);
					fontLibListItemView.rightView = (ImageView) convertView
							.findViewById(R.id.rightImageView);
					convertView.setTag(fontLibListItemView);
				} else {
					fontLibListItemView = (FontLibListItemView) convertView
							.getTag();
				}

				if (convertView != null) {
					Resources resource = getResources();

					String packageName = getApplicationInfo().packageName;

					Object leftImgName = fontLibListEntity.get(LEFT_VIEW);
					Object middleImgName = fontLibListEntity.get(MIDDLE_VIEW);
					Object rightImgName = fontLibListEntity.get(RIGHT_VIEW);

					if (leftImgName != null) {
						fontLibListItemView.leftView.setImageResource(resource
								.getIdentifier(leftImgName.toString(),
										DEF_DRAWABLE_TYPE, packageName));
						fontLibListItemView.leftView
								.setOnClickListener(new ImageViewOnClickListener(
										leftImgName));
					}

					if (middleImgName != null) {
						fontLibListItemView.middleView
								.setImageResource(resource.getIdentifier(
										middleImgName.toString(),
										DEF_DRAWABLE_TYPE, packageName));
						fontLibListItemView.middleView
								.setOnClickListener(new ImageViewOnClickListener(
										middleImgName));
					}

					if (rightImgName != null) {
						fontLibListItemView.rightView.setImageResource(resource
								.getIdentifier(rightImgName.toString(),
										DEF_DRAWABLE_TYPE, packageName));
						fontLibListItemView.rightView
								.setOnClickListener(new ImageViewOnClickListener(
										rightImgName));
					}
				}
			}

			return convertView;
		}

		public class ImageViewOnClickListener implements OnClickListener {

			private Object imageName;

			public ImageViewOnClickListener(Object leftImgName) {
				this.imageName = leftImgName;
			}

			public void onClick(View v) {
				Intent intent = new Intent(FontLibActivity.this,
						FontLibContentActivity.class);
				intent.putExtra(FONT_LIB_SELECTION, imageName.toString());
				startActivity(intent);
			}

		}

	}
}
