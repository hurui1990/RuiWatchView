package com.hurui.ruiwatchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hurui on 2017/8/21.
 */

public class RuiWatchView extends View {

	private Paint mPaint;
	private Calendar mCalendar;
	private int mWidth;
	private int mHeight;
	private int mBackgroundColor;
	private int mDisplayType;

	public RuiWatchView(Context context) {
		super(context);
	}

	public RuiWatchView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RuiWatchView);
		mBackgroundColor = typedArray.getColor(R.styleable.RuiWatchView_custom_background, Color.BLACK);
		mDisplayType = typedArray.getInteger(R.styleable.RuiWatchView_custom_display_type, 0);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(mBackgroundColor);
		mPaint.setStyle(Paint.Style.FILL);
		mCalendar = Calendar.getInstance();

		typedArray.recycle();
	}

	public RuiWatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * 开始执行绘制过程，每秒绘制一次
	 * */
	public void start(){
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				postInvalidate();
			}
		}, 0, 1000);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mWidth = this.getMeasuredWidth();
		mHeight = this.getMeasuredHeight();
		int len = Math.min(mWidth, mHeight);
		mPaint.setTextSize(len / 24);
		mPaint.setStrokeWidth(len / 2 / 720);
		drawPlate(canvas, len);
		drawPoints(canvas, len);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int size = Math.min(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(size, size);
	}

	//绘制表盘
	protected void drawPlate(Canvas canvas, int len){
		canvas.save();

		int r = len / 2;
		mPaint.setColor(mBackgroundColor);
		//绘制表盘
		canvas.drawCircle(r, r, r-r/144, mPaint);
		for(int i=0; i< 60; i++){
			mPaint.setColor(Color.WHITE);
			if(i % 5 == 0){
				mPaint.setStrokeWidth(r / 72);
				canvas.drawLine(r+9*r/10, r, len-r/144, r, mPaint);
			}else {
				mPaint.setStrokeWidth(r / 180);
				canvas.drawLine(r+14*r/15, r, len-r/144, r, mPaint);
			}
			canvas.rotate(6 , r, r);
		}
		canvas.translate(r,r);
		//绘制时间数字
		for(int i=1; i<= 12; i++){
			int degree = 30 * i;
			double radians = Math.toRadians(degree);
			mPaint.setStrokeWidth(4);
			String hourText;
			if(mDisplayType == 1){
				hourText = getHoursGreece(i);
			}else {
				hourText = getHoursArabia(i)+"";
			}
			Rect rect = new Rect();
			mPaint.getTextBounds(hourText, 0, hourText.length(), rect);
			int textWidth = rect.width();
			int textHeight = rect.height();
			canvas.drawText(hourText,
					(float) ((r * 4 / 5) * Math.cos(radians) - textWidth / 2),
					(float) ((r * 4 / 5) * Math.sin(radians) + textHeight/2),
					mPaint);
		}
		canvas.restore();
	}

	//转换显示的时间为阿拉伯数字
	private int getHoursArabia(int hours){
		switch (hours){
			case 1:
				return 4;
			case 2:
				return 5;
			case 3:
				return 6;
			case 4:
				return 7;
			case 5:
				return 8;
			case 6:
				return 9;
			case 7:
				return 10;
			case 8:
				return 11;
			case 9:
				return 12;
			case 10:
				return 1;
			case 11:
				return 2;
			case 12:
				return 3;
			default:
				return 0;
		}
	}

	//转换显示的时间为希腊数字
	private String getHoursGreece(int hours){
		switch (hours){
			case 1:
				return "Ⅳ";
			case 2:
				return "V";
			case 3:
				return "VI";
			case 4:
				return "VII";
			case 5:
				return "VIII";
			case 6:
				return "IX";
			case 7:
				return "X";
			case 8:
				return "XI";
			case 9:
				return "XII";
			case 10:
				return "I";
			case 11:
				return "II";
			case 12:
				return "III";
			default:
				return "";
		}
	}

	//绘制三根指针
	protected void drawPoints(Canvas canvas, int len){
		mCalendar.setTimeInMillis(System.currentTimeMillis());
		int hours = mCalendar.get(Calendar.HOUR) % 12;
		int minutes = mCalendar.get(Calendar.MINUTE);
		int seconds = mCalendar.get(Calendar.SECOND);
		int degree;
		double radians;
		int r = len / 2;
		int startX = r;
		int startY = r;
		int endX;
		int endY;

		//画时针，计算时针的转过的角度
		degree = 360 / 12 * hours + (30 * minutes / 60);
		radians = Math.toRadians(degree);
		//时针的起点为圆的中点

		//通过三角函数计算时针终点的位置，时针最短，取长度的0.5倍
		endX = (int) (startX + r * Math.cos(radians) * 0.5);
		endY = (int) (startY + r * Math.sin(radians) * 0.5);
		canvas.save();
		mPaint.setStrokeWidth(r / 71);
		//初始角度是0，应该从12点钟开始算，所以要逆时针旋转90度
		canvas.rotate((-90), r, r);
		canvas.drawLine(startX, startY, endX, endY, mPaint);

		radians = Math.toRadians(degree - 180);
		endX = (int) (startX + r * 0.05 * Math.cos(radians));
		endY = (int) (startY + r * 0.05 * Math.sin(radians));
		canvas.drawLine(startX, startY, endX, endY, mPaint);
		canvas.restore();

		//画分针，计算分针转过的角度
		degree = 360 / 60 * minutes + (6 * seconds / 60);
		radians = Math.toRadians(degree);
		endX = (int) (startX + r * Math.cos(radians) * 0.6);
		endY = (int) (startY + r * Math.sin(radians) * 0.6);
		canvas.save();
		mPaint.setStrokeWidth(r / 120);
		canvas.rotate(-90, r, r);
		canvas.drawLine(startX, startY, endX, endY, mPaint);

		radians = Math.toRadians(degree - 180);
		endX = (int) (startX + r * 0.08 * Math.cos(radians));
		endY = (int) (startY + r * 0.08 * Math.sin(radians));
		canvas.drawLine(startX, startY, endX, endY, mPaint);
		canvas.restore();

		//画秒针
		degree = 360 / 60 * seconds;
		radians = Math.toRadians(degree);
		endX = (int)(startX + r * Math.cos(radians) * 0.7);
		endY = (int)(startY + r * Math.sin(radians) * 0.7);
		canvas.save();
		mPaint.setStrokeWidth(r / 240);
		canvas.rotate(-90, r, r);
		canvas.drawLine(startX, startY, endX, endY, mPaint);

	    radians = Math.toRadians(degree - 180);
		endX = (int)(startX + r * 0.1 * Math.cos(radians));
		endY = (int)(startY + r * 0.1 * Math.sin(radians));
		canvas.drawLine(startX, startY, endX, endY, mPaint);
		canvas.restore();
	}

	public void setDisplayType(DISPLAY_TYPE displayType){
		switch (displayType){
			case ARABIA:
				mDisplayType = 0;
				break;
			case GREECE:
				mDisplayType = 1;
				break;
		}
		invalidate();
	}

	public void setPlateColor(PLATE_COLOR plateColor){
		switch (plateColor){
			case BLACK:
				mBackgroundColor = Color.BLACK;
				break;
			case BLUE:
				mBackgroundColor = Color.BLUE;
				break;
			case GREEN_DARK:
				mBackgroundColor = Color.parseColor("#FF064539");
				break;
			case GREEN_LIGHT:
				mBackgroundColor = Color.parseColor("#FF0ED6B0");
				break;
		}
	}

	public static enum DISPLAY_TYPE{
		ARABIA,
		GREECE;
		private DISPLAY_TYPE(){}
	}

	public static enum PLATE_COLOR{
		BLACK,
		GREEN_DARK,
		GREEN_LIGHT,
		BLUE;
		private PLATE_COLOR(){}
	}
}
