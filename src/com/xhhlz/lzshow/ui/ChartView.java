package com.xhhlz.lzshow.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ChartView extends View {
	public int XPoint = 45; // ԭ���X����
	public int YPoint = 465; // ԭ���Y����
	public int XScale = 12; // X�Ŀ̶ȳ���
	public int YScale = 42; // Y�Ŀ̶ȳ���
	public int XLength = 400; // X��ĳ���
	public int YLength = 430; // Y��ĳ���
	public String[] XLabel; // X�Ŀ̶�
	public String[] YLabel; // Y�Ŀ̶�
	public int[] Data; // ����
	public String Title; // ��ʾ�ı���

	public ChartView(Context context, AttributeSet attr) {
		super(context, attr);
		this.setInfo(new String[] { "", "", "2", "", "4", "", "6", "", "8", "",
				"10", "", "12", "", "14", "", "16", "", "18", "", "20", "",
				"22", "", "24", "", "26", "", "28", "", "30", "" }, // X��̶�
				new String[] { "", "20", "40", "60", "80", "100", "120", "140",
						"160", "180", "200" }, // Y��̶�
				new int[] { 15, 23, 10, 36, 45, 40, 12 }, // ����
				"�����ּ�¼һ��");
	}

	public void setInfo(String[] XLabels, String[] YLabels, int[] AllData,
			String strTitle) {
		XLabel = XLabels;
		YLabel = YLabels;
		Data = AllData;
		Title = strTitle;
	}

	public void setData(int[] AllData) {
		Data = AllData;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);// ��дonDraw����
		// canvas.drawColor(Color.WHITE);//���ñ�����ɫ
		Paint paint = new Paint();
		
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);// ȥ���
		paint.setColor(Color.DKGRAY);
		
		// ����Y��
		canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint); // ����
		for (int i = 0; i * YScale < YLength; i++) {
			canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i
					* YScale, paint); // �̶�
			try {
				canvas.drawText(YLabel[i], XPoint - 22,
						YPoint - i * YScale + 5, paint); // ����
			} catch (Exception e) {
			}
		}
		canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength
				+ 6, paint); // ��ͷ
		canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength
				+ 6, paint);
		// ����X��
		canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint); // ����
		for (int i = 0; i * XScale < XLength; i++) {
			if (i % 2 == 0)// x��Ŀ̶�ֻ��ż�������п̶�ֵ
				canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i
						* XScale, YPoint - 5, paint); // �̶�
			try {
				canvas.drawText(XLabel[i], XPoint + i * XScale - 3,
						YPoint + 20, paint); // ����

			} catch (Exception e) {
			}
			/*
			 if (i > 0 && YCoord(Data[i - 1]) != -999 && YCoord(Data[i]) != -999) // ��֤��Ч����
				canvas.drawLine(XPoint + (i - 1) * XScale, YCoord(Data[i - 1]),
						XPoint + i * XScale, YCoord(Data[i]), paint);
			canvas.drawCircle(XPoint + i * XScale, YCoord(Data[i]), 2, paint); 
			 */
		}

		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint - 3, paint); // ��ͷ
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint + 3, paint);
		
		paint.setColor(Color.BLACK);
		paint.setTextSize(28);
		canvas.drawText(Title, 220, 32, paint);
		
		
		/*������*/
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);// ȥ���
		paint.setColor(Color.BLUE);// ��ɫ
		
		int lastX = 0, lastY = 0;
		int length = Data.length;
		for (int i = 0; i < length; i++) {
			// ����ֵ
			/*�����ص�data���ݵ�data[0]��һ�ŵ�����*/
			if(YCoord(Data[i]) != -999 && Data[i] != 0)
			{
				canvas.drawLine(XPoint + lastX * XScale, YCoord(lastY),
						XPoint + (i+1) * XScale, YCoord(Data[i]), paint);
				canvas.drawCircle(XPoint + (i+1) * XScale, YCoord(Data[i]), 2, paint);
				
				lastX = i+1;//����data[]��0��ʼ��ȡ����
				lastY = Data[i];	
			}
		}
		
		
	}

	private int YCoord(int y0) // �������ʱ��Y���꣬������ʱ����-999
	{
		int y = y0;

		try {
			return YPoint - y * YScale / Integer.parseInt(YLabel[1]);
		} catch (Exception e) {
		}
		return y;
	}
}
