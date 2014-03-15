package com.xhhlz.lzshow.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.xhhlz.lzshow.PractiseActivity;
import com.xhhlz.lzshow.listener.PractiseWriteViewListener;
import com.xhhlz.lzshow.provider.UserRecordUtil;

/**
 * �������ֽ���д�ְ�
 * 
 * @author kelvin
 * 
 */
public class PractiseWriteView extends SurfaceView implements
		SurfaceHolder.Callback {

	/**
	 * ���ֻ���
	 */
	private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	/**
	 * �߿򻭱�
	 */
	private Paint framePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	/**
	 * ���߻���
	 */
	private Paint dotLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	/**
	 * ����Ч��
	 */
	private PathEffect dotLineEffect = new DashPathEffect(new float[] { 5, 5,
			5, 5 }, 1);

	/**
	 * �ֻ滭��
	 */
	private Paint handwritingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private Paint fontLibPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	/**
	 * �����Զ����������
	 */
	private Typeface copyBookFace;

	/**
	 * �ֱ��Զ����������
	 */
	private Typeface penFace;

	/**
	 * ���ֱ߿����Ͻ�X����
	 */
	private float leftTopX;

	/**
	 * ���ֱ߿����Ͻ�Y����
	 */
	private float leftTopY;

	/**
	 * ���ֱ߿����½�X����
	 */
	private float leftBottomX;

	/**
	 * ���ֱ߿����½�Y����
	 */
	private float leftBottomY;

	/**
	 * ���ֱ߿����Ͻ�X����
	 */
	private float rightTopX;

	/**
	 * ���ֱ߿����Ͻ�Y����
	 */
	private float rightTopY;

	/**
	 * ���ֱ߿����½�X����
	 */
	private float rightBottomX;

	/**
	 * ���ֱ߿����½�Y����
	 */
	private float rightBottomY;

	/**
	 * Ĭ���߶δ�ϸ
	 */
	private static final int DEFAULT_STROKE_WIDTH = 1;

	/**
	 * Ĭ���ֻ�ʼ���ϸ
	 */
	private static final int DEFAULT_HANDWRTING_STROKE_WIDTH = 20;

	private static final int DEFAULT_FONGLIB_TEXT_SIZE = 25;

	/**
	 * Ĭ�����ֻ�����ɫ
	 */
	private static final int DEFAULT_TEXT_PAINT_COLOR = 0xFFEB8B75;

	/**
	 * Ĭ���߶λ�����ɫ
	 */
	private static final int DEFAULT_LINE_PAINT_COLOR = Color.RED;

	/**
	 * Ĭ�ϻ�����ɫ
	 */
	private static final int DEFAULT_CANVAS_COLOR = Color.WHITE;

	/**
	 * Ĭ�ϸֱ���ɫ
	 */
	private static final int DEFAULT_PEN_COLOR = Color.BLACK;

	/**
	 * Ĭ�����ִ�С
	 */
	private int DEFAULT_TEXT_SIZE; // ��Ļ��ȵ�3/4

	/**
	 * �����ֳ�ʼX����
	 */
	private int DEFAULT_START_X; // Ӧ������Ļ��ȵ�1/8

	/**
	 * �����ֳ�ʼY����
	 */
	private int DEFAULT_START_Y; // ��Ļ�߶ȵ�1/2

	/**
	 * SurfaceHolder
	 */
	private SurfaceHolder surfaceHolder;

	/**
	 * �����л�ˢ���߳�
	 */
	private WordChangeThread wordChangeThread;

	// private static final String SRC_PEN_FONT = "fonts/gangbi.ttf";

	// private static final String SRC_KAITI_FONT = "fonts/kaiti.ttf";

	private static final String SRC_XINGKAI_FONT = "fonts/xingkai.ttf";

	private static final String DEFAULT_FONT_DEMO = "��";

	private static Path path;
	
	private PractiseWriteViewListener practiseWriteViewListener;

	/**
	 * ��ʼ����Ļ��������Ⱥ͸߶ȣ�
	 */
	private void initScreenParams() {
		int screenHeight = PractiseActivity.getSCREEN_HEIGHT();
		int screenWidth = PractiseActivity.getSCREEN_WIDTH();

		DEFAULT_TEXT_SIZE = screenWidth * 3 / 4;
		DEFAULT_START_X = screenWidth / 8;
		DEFAULT_START_Y = screenHeight / 2;
	}

	/**
	 * ��ʼ������
	 */
	private void initPaint() {
		// ��ʼ�����ֻ���
		textPaint.setTextSize(DEFAULT_TEXT_SIZE); // ���������С
		textPaint.setColor(DEFAULT_TEXT_PAINT_COLOR); // ���û�����ɫ
		textPaint.setTypeface(copyBookFace); // �����Զ�������

		// ��ʼ���߿򻭱�
		framePaint.setStrokeWidth(DEFAULT_STROKE_WIDTH); // ���û��ʴ�ϸ
		framePaint.setColor(DEFAULT_LINE_PAINT_COLOR); // ���û�����ɫ

		// ��ʼ�����߻���
		dotLinePaint.setStrokeWidth(DEFAULT_STROKE_WIDTH); // ���û��ʴ�ϸ
		dotLinePaint.setStyle(Paint.Style.STROKE); // ���û��ʷ��
		dotLinePaint.setColor(DEFAULT_LINE_PAINT_COLOR); // ���û�����ɫ
		dotLinePaint.setPathEffect(dotLineEffect); // ��������Ч��

		// ��ʼ���ֻ滭��
		handwritingPaint.setStrokeWidth(DEFAULT_HANDWRTING_STROKE_WIDTH); // ���û��ʴ�ϸ
		handwritingPaint.setStyle(Paint.Style.STROKE); // ���û��ʷ��

		handwritingPaint.setTypeface(penFace);
		handwritingPaint.setStrokeJoin(Paint.Join.BEVEL);
		handwritingPaint.setColor(DEFAULT_PEN_COLOR); // ���û�����ɫ

		path = new Path();

		fontLibPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
		fontLibPaint.setColor(DEFAULT_PEN_COLOR);
		fontLibPaint.setTextSize(DEFAULT_FONGLIB_TEXT_SIZE);
	}

	/**
	 * ��ʼ��ViewĬ�Ϸ���
	 * 
	 * @param context
	 *            ������
	 */
	private void initView(Context context) {

		// ʵ���������Զ������� (���ȡ�û������ļ�����ȡ������Ϣ)
		copyBookFace = Typeface.createFromAsset(context.getAssets(),
				SRC_XINGKAI_FONT);

		penFace = Typeface.createFromAsset(context.getAssets(),
				SRC_XINGKAI_FONT);

		initPaint();

		initCoordinate();
	}

	private void initCoordinate() {
		FontMetrics fm = textPaint.getFontMetrics();

		leftTopX = DEFAULT_START_X;
		leftTopY = DEFAULT_START_Y + fm.top;

		rightTopX = DEFAULT_START_X + getStringWidth(DEFAULT_FONT_DEMO);
		rightTopY = leftTopY;

		leftBottomX = leftTopX;
		leftBottomY = DEFAULT_START_Y + fm.bottom;

		rightBottomX = rightTopX;
		rightBottomY = leftBottomY;
	}

	/**
	 * ��ʼ��View�ṩ�������Ͳ�������
	 * 
	 * @param context
	 *            ������
	 * @param typeFace
	 *            Ҫ���õ����屣��·��
	 */
	// private void initView(Context context, String typeFace) {
	//
	// // ʵ���������Զ������� (���ȡ�û������ļ�����ȡ������Ϣ)
	// copyBookFace = Typeface.createFromAsset(context.getAssets(), typeFace);
	//
	// penFace = Typeface.createFromAsset(context.getAssets(),
	// SRC_XINGKAI_FONT);
	//
	// initPaint();
	// initCoordinate();
	// }

	/**
	 * ���췽��
	 * 
	 * @param context
	 *            ������
	 */
	// public PractiseWriteView(Context context) {
	// super(context);
	//
	// initScreenParams();
	// // ���û����������
	// initView(context);
	//
	// // ����
	// Log.d("debug", "PractiseWriteView(Context context)");
	//
	// surfaceHolder = this.getHolder();
	// surfaceHolder.addCallback(this);
	//
	// // initView(typeFace);
	// }

	/**
	 * ���췽��
	 * 
	 * @param context
	 *            ������
	 * @param attr
	 *            ���Լ�
	 */
	public PractiseWriteView(Context context, AttributeSet attr) {
		super(context, attr);
		
		
		initScreenParams();
		// ���û����������
		initView(context);

		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);
		// ����
		// initView(typeFace);
	}

	public void addPractiseWriteViewListener(PractiseWriteViewListener mylistener)
	{
		practiseWriteViewListener = mylistener;
	}
	/**
	 * ��ȡ�ַ������
	 * 
	 * @param str
	 *            �ַ���
	 * @return �ַ������
	 */
	private float getStringWidth(String str) {
		return textPaint.measureText(str);
	}

	/**
	 * ����������Ϊ��ɫ
	 * 
	 * @param canvas
	 *            ����
	 */
	private void paintWhite(Canvas canvas) {
		canvas.drawColor(DEFAULT_CANVAS_COLOR);
	}

	/**
	 * ���ַ���
	 * 
	 * @param drawText
	 *            Ҫ�����
	 * @param canvas
	 *            ����
	 */
	private void drawText(String drawText, Canvas canvas) {
		canvas.drawText(drawText, DEFAULT_START_X, DEFAULT_START_Y, textPaint);
	}

	private void penDraw(Canvas canvas) {
		canvas.drawPath(path, handwritingPaint);
	}

	/**
	 * ���߷���
	 * 
	 * @param stringWidth
	 *            Ҫ���ֵĿ��
	 * @param canvas
	 *            ����
	 */
	private void drawLine(float stringWidth, Canvas canvas) {

		// draw frame lines
		canvas.drawLine(leftTopX, leftTopY, rightTopX, rightTopY, framePaint);
		canvas.drawLine(leftTopX, leftTopY, leftBottomX, leftBottomY,
				framePaint);
		canvas.drawLine(rightTopX, rightTopY, rightBottomX, rightBottomY,
				framePaint);
		canvas.drawLine(rightBottomX, rightBottomY, leftBottomX, leftBottomY,
				framePaint);

		// draw dotted lines
		canvas.drawLine(leftTopX, leftTopY, rightBottomX, rightBottomY,
				dotLinePaint);
		canvas.drawLine(rightTopX, rightTopY, leftBottomX, leftBottomY,
				dotLinePaint);

		canvas.drawLine(leftTopX, (leftBottomY - leftTopY) / 2 + leftTopY,
				rightTopX, (rightBottomY - rightTopY) / 2 + rightTopY,
				dotLinePaint);
		canvas.drawLine((rightTopX - leftTopX) / 2 + leftTopX, leftTopY,
				(rightBottomX - leftBottomX) / 2 + leftBottomX, leftBottomY,
				dotLinePaint);
	}

	private void drawFontLib(String fontlib, Canvas canvas) {
		canvas.drawText(fontlib, DEFAULT_FONGLIB_TEXT_SIZE,
				DEFAULT_FONGLIB_TEXT_SIZE + 10, fontLibPaint);
	}

	// protected void paint(Canvas canvas) {
	// //
	// }
	//
	// protected void repaint() {
	// SurfaceHolder holder = this.getHolder();
	// Canvas canvas = holder.lockCanvas();
	// paint(canvas);
	// holder.unlockCanvasAndPost(canvas);
	// }

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		wordChangeThread = new WordChangeThread();
		wordChangeThread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// �������ݿ�
		UserRecordUtil.insertLatestUserRecord(getContext(),
				PractiseActivity.getCurFontLib(),
				PractiseActivity.getCurWordIndex());

		if (PractiseWriteView.ifWrite())
			PractiseActivity.writeSumPlusPlus();
		wordChangeThread.setRunning(false);

	}

	private float cur_x;
	private float cur_y;

	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		if (x < leftTopX)
		{
			practiseWriteViewListener.isShare();
			return false;
		}
		else if (x > rightTopX)
		{
			practiseWriteViewListener.isShare();
			return false;
		}
		
		if (y < leftTopY)
		{
			practiseWriteViewListener.isShare();
			return false;
		}
		else if (y > leftBottomY)
		{
			practiseWriteViewListener.isShare();
			return false;
		}

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
			// ��path
			// reDraw();
			// path.reset();
			break;
		}
		}

		invalidate();

		return true;
	}

	// private void reDraw() {
	// Canvas canvas = surfaceHolder.lockCanvas();
	//
	// // ����������Ϊ��ɫ
	// paintWhite(canvas);
	// // ����
	// drawLine(getStringWidth(curWord), canvas);
	// // ����
	// drawText(curWord, canvas);
	//
	// penDraw(canvas);
	//
	// surfaceHolder.unlockCanvasAndPost(canvas);
	// }

	public static void clear() {
		path.reset();
	}

	/**
	 * �����л�ˢ���߳�
	 * 
	 * @author kelvin
	 * 
	 */
	class WordChangeThread extends Thread {

		private volatile boolean isRunning = true;

		public synchronized boolean isRunning() {
			return isRunning;
		}

		public synchronized void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}

		public void run() {

			Canvas canvas = null;
			// int curWordIndex = 0;
			// int total = words.length();

			while (isRunning) {

				String curWord = PractiseActivity.getCurWord();

				canvas = surfaceHolder.lockCanvas();
				// if (curWordIndex < total) {
				// curWord = words.substring(curWordIndex, curWordIndex + 1);
				// curWordIndex++;
				// } else {
				// curWordIndex = 0;
				// curWord = words.substring(curWordIndex, curWordIndex + 1);
				// }

				// ����������Ϊ��ɫ
				paintWhite(canvas);

				drawFontLib(PractiseActivity.getFontLibName(getContext()),
						canvas);

				// ����
				drawLine(getStringWidth(curWord), canvas);
				// ����
				drawText(curWord, canvas);

				penDraw(canvas);

				surfaceHolder.unlockCanvasAndPost(canvas);

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean ifWrite() {
		return !path.isEmpty();
	}

}