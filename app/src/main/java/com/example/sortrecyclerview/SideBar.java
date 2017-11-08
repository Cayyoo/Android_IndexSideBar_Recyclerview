package com.example.sortrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 侧边栏 字母A-Z排序
 */
public class SideBar extends View {
	/**触摸事件*/
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

	public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };

	/**手指摁下后的选中状态*/
	private int choose = -1;

	private Paint paint = new Paint();

	private TextView mTextDialog;


	public SideBar(Context context) {
		super(context);
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 为SideBar设置显示字母的TextView
	 * @param textDialog
	 */
	public void setTextView(TextView textDialog) {
		this.mTextDialog = textDialog;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();

		//获取每一个字母的高度
		int singleHeight = height / b.length;

		for (int i = 0; i < b.length; i++) {
			//绘制的字体颜色
			//paint.setColor(Color.WHITE);
			paint.setColor(Color.rgb(33, 65, 98));
			//字体大小
			paint.setTextSize(30);
			//加粗字体
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			//抗锯齿
			paint.setAntiAlias(true);
			//防抖动，绘制的图像较柔和
			paint.setDither(true);

			//选中的状态
			if (i == choose) {
				//摁住的字母的颜色
				paint.setColor(Color.parseColor("#3399ff"));
				//true为粗体，false为非粗体
				paint.setFakeBoldText(true);
			}

			//x坐标等于中间-字符串宽度的一半
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos = singleHeight * i + singleHeight;

			canvas.drawText(b[i], xPos, yPos, paint);

			paint.reset();
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();

		final int oldChoose = choose;

		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;

		//点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数
		final int c = (int) (y / getHeight() * b.length);

		switch (action) {
			case MotionEvent.ACTION_UP:
				/*
				摁下侧边栏，再抬起手势，呈现的背景色
				0x00000000表示透明色：R:0 G:0 B:0 A:0
				 */
				//setBackground(new ColorDrawable(0x00000000));
				setBackgroundColor(ContextCompat.getColor(getContext(),R.color.transparent));

				choose = -1;

				//刷新下一帧动画，重绘View
				invalidate();

				if (mTextDialog != null) {
					mTextDialog.setVisibility(View.INVISIBLE);
				}
				break;
			default:
				//摁下侧边栏时的背景色
				setBackgroundResource(R.drawable.shape_sidebar_bg);

				if (oldChoose != c) {
					if (c >= 0 && c < b.length) {

						if (listener != null) {
							listener.onTouchingLetterChanged(b[c]);
						}

						if (mTextDialog != null) {
							mTextDialog.setText(b[c]);
							mTextDialog.setVisibility(View.VISIBLE);
						}

						choose = c;

						//刷新下一帧动画，重绘View
						invalidate();
					}
				}
				break;
		}
		return true;
	}

	/**
	 * 触摸事件
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}


	public interface OnTouchingLetterChangedListener {
		void onTouchingLetterChanged(String s);
	}

}