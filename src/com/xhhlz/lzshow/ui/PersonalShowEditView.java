package com.xhhlz.lzshow.ui;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * ����������Ӧ�ĸ�����ĳ�ʼ����,
 * ����EditText�ķ����и�������ǽ����ǿ��Ա༭�ģ������������������б༭����Ȼ���ǲ��ɽ��ܵ�
 * @author enming xie
 * 
 */
public class PersonalShowEditView extends EditText{
    private Paint mPaint;
	
	public PersonalShowEditView(Context context, AttributeSet attrs) {
		 super(context, attrs);
		// TODO Auto-generated constructor stub
		 
         mPaint = new Paint();
         mPaint.setStyle(Paint.Style.STROKE);
         mPaint.setColor(0xE0E0E0E0);
	}
	
	/**
	 * ����������Ľ���ĺ���
	 */
	@Override
    protected void onDraw(Canvas canvas) {
        int height = canvas.getHeight();//�����Ļ�Ŀ��
        int width = canvas.getWidth();//�����Ļ�ĸ߶�
		
        Log.d("screenW", width+"");
        Log.d("screenH", height+"");
        
        Paint paint = mPaint;
        int count = 6;
        int x = height/9;
        int step = height/8;
        for (int i = 0; i < count ; i++) {
            canvas.drawLine(10, x, width-10, x, paint);
            x = x+step;
        }

        super.onDraw(canvas);
    }
}
