package com.xhhlz.lzshow.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ChartView extends View {
	public int XPoint = 45; // 原点的X坐标
	public int YPoint = 465; // 原点的Y坐标
	public int XScale = 12; // X的刻度长度
	public int YScale = 42; // Y的刻度长度
	public int XLength = 400; // X轴的长度
	public int YLength = 430; // Y轴的长度
	public String[] XLabel; // X的刻度
	public String[] YLabel; // Y的刻度
	public int[] Data; // 数据
	public String Title; // 显示的标题

	public ChartView(Context context, AttributeSet attr) {
		super(context, attr);
		this.setInfo(new String[] { "", "", "2", "", "4", "", "6", "", "8", "",
				"10", "", "12", "", "14", "", "16", "", "18", "", "20", "",
				"22", "", "24", "", "26", "", "28", "", "30", "" }, // X轴刻度
				new String[] { "", "20", "40", "60", "80", "100", "120", "140",
						"160", "180", "200" }, // Y轴刻度
				new int[] { 15, 23, 10, 36, 45, 40, 12 }, // 数据
				"月练字记录一览");
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
		super.onDraw(canvas);// 重写onDraw方法
		// canvas.drawColor(Color.WHITE);//设置背景颜色
		Paint paint = new Paint();
		
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);// 去锯齿
		paint.setColor(Color.DKGRAY);
		
		// 设置Y轴
		canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint); // 轴线
		for (int i = 0; i * YScale < YLength; i++) {
			canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i
					* YScale, paint); // 刻度
			try {
				canvas.drawText(YLabel[i], XPoint - 22,
						YPoint - i * YScale + 5, paint); // 文字
			} catch (Exception e) {
			}
		}
		canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength
				+ 6, paint); // 箭头
		canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength
				+ 6, paint);
		// 设置X轴
		canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint); // 轴线
		for (int i = 0; i * XScale < XLength; i++) {
			if (i % 2 == 0)// x轴的刻度只是偶数日期有刻度值
				canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i
						* XScale, YPoint - 5, paint); // 刻度
			try {
				canvas.drawText(XLabel[i], XPoint + i * XScale - 3,
						YPoint + 20, paint); // 文字

			} catch (Exception e) {
			}
			/*
			 if (i > 0 && YCoord(Data[i - 1]) != -999 && YCoord(Data[i]) != -999) // 保证有效数据
				canvas.drawLine(XPoint + (i - 1) * XScale, YCoord(Data[i - 1]),
						XPoint + i * XScale, YCoord(Data[i]), paint);
			canvas.drawCircle(XPoint + i * XScale, YCoord(Data[i]), 2, paint); 
			 */
		}

		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint - 3, paint); // 箭头
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint + 3, paint);
		
		paint.setColor(Color.BLACK);
		paint.setTextSize(28);
		canvas.drawText(Title, 220, 32, paint);
		
		
		/*画数据*/
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);// 去锯齿
		paint.setColor(Color.BLUE);// 颜色
		
		int lastX = 0, lastY = 0;
		int length = Data.length;
		for (int i = 0; i < length; i++) {
			// 数据值
			/*所返回的data数据的data[0]是一号的数据*/
			if(YCoord(Data[i]) != -999 && Data[i] != 0)
			{
				canvas.drawLine(XPoint + lastX * XScale, YCoord(lastY),
						XPoint + (i+1) * XScale, YCoord(Data[i]), paint);
				canvas.drawCircle(XPoint + (i+1) * XScale, YCoord(Data[i]), 2, paint);
				
				lastX = i+1;//数据data[]从0开始读取数据
				lastY = Data[i];	
			}
		}
		
		
	}

	private int YCoord(int y0) // 计算绘制时的Y坐标，无数据时返回-999
	{
		int y = y0;

		try {
			return YPoint - y * YScale / Integer.parseInt(YLabel[1]);
		} catch (Exception e) {
		}
		return y;
	}
}
