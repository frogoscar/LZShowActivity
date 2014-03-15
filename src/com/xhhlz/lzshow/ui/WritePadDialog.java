package com.xhhlz.lzshow.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;

import com.xhhlz.lzshow.R;
import com.xhhlz.lzshow.listener.WritePadDialogListener;

/**
 * 这个是获取画白板的那个dailog的界面，注意这个界面是以一个固定值设置的大小， 而在实际的项目中需要获得指定的屏幕高度和宽度进而进行尺寸
 * 
 * @author enming xie
 * 
 */
public class WritePadDialog extends Dialog {
	Context context;
	LayoutParams p;
	WritePadDialogListener dialogListener;
	private int screenWidth;
	// private int screenHeight;
	private int dialogHeight;

	private float leftTopX;
	private float leftTopY;
	private float rightTopX;
	private float rightTopY;
	private float leftBottomX;
	private float leftBottomY;
	private float rightBottomX;
	private float rightBottomY;

	static final int BACKGROUND_COLOR = Color.WHITE;
	static final int BRUSH_COLOR = Color.BLACK;
	PaintView mView;
	/** The index of the current color to use. */
	int mColorIndex;

	public WritePadDialog(Context context, int width, int height,
			WritePadDialogListener dialogListener) {
		super(context);
		this.context = context;
		this.screenWidth = width;
		// this.screenHeight = height;
		this.dialogListener = dialogListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.write_pad_dialog);

		dialogHeight = (int) (screenWidth * (1.2));

		p = getWindow().getAttributes(); // get the dialog's attributes, through which get the word's position
		p.height = dialogHeight; // height set to 0.4 * screen's height
		p.width = (int) screenWidth; // width set to 0.6 * screen's width
		getWindow().setAttributes(p);

		mView = new PaintView(context);
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.tablet_view);
		frameLayout.addView(mView);
		mView.requestFocus();

		/**
		 * 清除按钮的操作
		 */
		Button btnClear = (Button) findViewById(R.id.tablet_clear);
		btnClear.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				mView.clear();
			}
		});

		/**
		 * 确认按钮的操作
		 */
		Button btnOk = (Button) findViewById(R.id.tablet_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				try {
					dialogListener.refreshActivity(mView.getCachebBitmap());
					dismiss();
					Log.d("this", "this");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		/**
		 * 取消按钮的操作
		 */
		Button btnCancel = (Button) findViewById(R.id.tablet_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				cancel();
			}
		});

		/**
		 * 下一个按钮的操作
		 */
		Button btnNext = (Button) findViewById(R.id.tablet_next);
		btnNext.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// 刷新到activity页面里
				try {
					dialogListener.refreshActivity(mView.getCachebBitmap());
					Log.d("this", "this");
				} catch (Exception e) {
					e.printStackTrace();
				}
				mView.clear();
			}
		});
	}

	/**
	 * This view implements the drawing canvas.
	 * 
	 * It handles all of the input events and drawing functions.
	 */
	class PaintView extends View {
		private Paint paint;
		private Canvas cacheCanvas;
		private Bitmap cachebBitmap;
		private Path path;

		private Paint dotLinePaint;
		private PathEffect dottedLineEffect;

		private static final int DEFAULT_STROKE_WIDTH = 1;
		private static final int DEFAULT_LINE_PAINT_COLOR = Color.RED;

		private Paint framePaint;

		private int curW;
		private int curH;

		public Bitmap getCachebBitmap() {
			if (!path.isEmpty()) {
				framePaint.setColor(Color.WHITE);
				framePaint.setStrokeWidth(2);
				dotLinePaint.setColor(Color.WHITE);
				dotLinePaint.setStrokeWidth(2);
				drawLineRect();

				/* 复制位图信息，传递出去 */
				Matrix matrix = new Matrix();
				matrix.postScale(1, 1);
				Bitmap dstbmp = Bitmap.createBitmap(cachebBitmap, 0, 0,
						cachebBitmap.getWidth(), cachebBitmap.getHeight(),
						matrix, true);

				return dstbmp;
			} else
				return null;
		}

		public PaintView(Context context) {
			super(context);
			init();
		}

		private void init() {

			leftTopX = 20;
			leftTopY = 30;// (screenHeight - dialogHeight)/2
			rightTopX = screenWidth - 60;
			rightTopY = leftTopY;

			leftBottomX = 20;
			leftBottomY = screenWidth - 80 + 30;
			rightBottomX = rightTopX;
			rightBottomY = leftBottomY;

			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(15);// 设置笔刷的宽度
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			path = new Path();
			cachebBitmap = Bitmap.createBitmap(p.width, (int) (p.height * 0.8),
					Config.ARGB_8888);
			cacheCanvas = new Canvas(cachebBitmap);
			cacheCanvas.drawColor(Color.WHITE);

			// 初始化边框画笔
			framePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			framePaint.setStrokeWidth(DEFAULT_STROKE_WIDTH); // 设置画笔粗细
			framePaint.setColor(DEFAULT_LINE_PAINT_COLOR); // 设置画笔颜色

			// 初始化虚线画笔
			dotLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			dottedLineEffect = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
			dotLinePaint.setStrokeWidth(DEFAULT_STROKE_WIDTH); // 设置画笔粗细
			dotLinePaint.setStyle(Paint.Style.STROKE); // 设置画笔风格
			dotLinePaint.setColor(DEFAULT_LINE_PAINT_COLOR); // 设置画笔颜色
			dotLinePaint.setPathEffect(dottedLineEffect); // 设置虚线效果

			drawLineRect();
		}

		private void drawLineRect() {
			// draw frame lines
			cacheCanvas.drawLine(leftTopX, leftTopY, rightTopX, rightTopY,
					framePaint);
			cacheCanvas.drawLine(leftTopX, leftTopY, leftBottomX, leftBottomY,
					framePaint);
			cacheCanvas.drawLine(rightTopX, rightTopY, rightBottomX,
					rightBottomY, framePaint);
			cacheCanvas.drawLine(rightBottomX, rightBottomY, leftBottomX,
					leftBottomY, framePaint);

			// draw dotted lines
			cacheCanvas.drawLine(leftTopX, leftTopY, rightBottomX,
					rightBottomY, dotLinePaint);
			cacheCanvas.drawLine(rightTopX, rightTopY, leftBottomX,
					leftBottomY, dotLinePaint);

			cacheCanvas.drawLine(leftTopX, (leftBottomY - leftTopY) / 2
					+ leftTopY, rightTopX, (rightBottomY - rightTopY) / 2
					+ rightTopY, dotLinePaint);
			cacheCanvas.drawLine((rightTopX - leftTopX) / 2 + leftTopX,
					leftTopY, (rightBottomX - leftBottomX) / 2 + leftBottomX,
					leftBottomY, dotLinePaint);
		}

		public void clear() {
			if (cacheCanvas != null) {
				paint.setColor(BACKGROUND_COLOR);

				cacheCanvas.drawPaint(paint);
				paint.setColor(Color.BLACK);
				cacheCanvas.drawColor(Color.WHITE);

				invalidate();

				/* 画笔设置为红色的 */
				framePaint.setColor(Color.RED);
				framePaint.setStrokeWidth(1);
				dotLinePaint.setColor(Color.RED);
				dotLinePaint.setStrokeWidth(1);
				drawLineRect();
				
				path.reset();
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// canvas.drawColor(BRUSH_COLOR);
			canvas.drawBitmap(cachebBitmap, 0, 0, null);
			canvas.drawPath(path, paint);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {

			curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
			curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
			if (curW >= w && curH >= h) {
				return;
			}

			if (curW < w)
				curW = w;
			if (curH < h)
				curH = h;

			Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
					Bitmap.Config.ARGB_8888);
			Canvas newCanvas = new Canvas();
			newCanvas.setBitmap(newBitmap);
			if (cachebBitmap != null) {
				newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
			}
			cachebBitmap = newBitmap;
			cacheCanvas = newCanvas;
		}

		private float cur_x, cur_y;

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			if (x < leftTopX)
				x = leftTopX;
			else if (x > rightTopX)
				x = rightTopX;
			if (y < leftTopY)
				y = leftTopY;
			else if (y > leftBottomY)
				y = leftBottomY;

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				cur_x = x;
				cur_y = y;

				path.moveTo(cur_x, cur_y);
				break;
			}

			case MotionEvent.ACTION_MOVE: {
				path.quadTo(cur_x, cur_y, x, y);
				cur_x = x;
				cur_y = y;
				break;
			}

			case MotionEvent.ACTION_UP: {
				cacheCanvas.drawPath(path, paint);
				break;
			}
			}

			invalidate();

			return true;
		}
	}
}
