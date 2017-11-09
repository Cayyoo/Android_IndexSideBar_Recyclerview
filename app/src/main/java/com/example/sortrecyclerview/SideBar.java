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

	/**
	 * 定义了显示在最右边的浮动的索引项的列表,当然这个是固定的，
	 * 所以可以直接初始化，如果需要变动的话则可以通过自定义属性来指定
	 */
	public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };

	/**手指摁下后的选中状态,用于标记点击存放字母数组中的下标*/
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

		/**
		 * 注意:在自定义view中的如果不需要设置wrap_content属性就不需要自己重写onMeasure方法
		 * 因为在onMeasure方法中系统默认会自动测量两种模式:一个是match_parent另一个则是自己指定明确尺寸大小
		 * 这两种方法对应着这一种MeasureSpec.AT_MOST测量模式，由于我们设置这个自定义浮动的字母索引表宽度是指定明确大小
		 * 高度是match_parent模式，所以这里就不要手动测量了直接通过getHeight和getWidth直接拿到系统自动测量好高度和宽度
		 * */
		int height = getHeight();
		int width = getWidth();

		//让整个显示的每个字母均分整个屏幕高度尺寸，这个singleHeight就是每个字母占据的高度
		int singleHeight = height / b.length;

		//遍历循环绘制每一个字母text
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

			//判断如果点击字母的下标等于i,那么就会设置绘制点击字母的样式用于高亮显示
			if (i == choose) {
				//摁住的字母的颜色
				paint.setColor(Color.parseColor("#3399ff"));
				//true为粗体，false为非粗体
				paint.setFakeBoldText(true);
			}

			/**
			 * 注意:canvas在绘制text的时候,他绘制的起点不是text的左上角而是它的左下角
			 * (xPos,yPos)表示每个字母左下角的位置的坐标
			 *
			 * xPos = width / 2 - paint.measureText(b[i]) / 2 的意思很容易理解,就是用
			 * (总的view的宽度(可能还包括leftPadding和rightPadding的大小)-每个字母宽度)/2得到就是每个字母的左下角的X坐标,
			 *
			 * 仔细想下每个text的起点的x坐标都是一样的。paint.measureText(b[i])得到每一个字母宽度大小，
			 * 由于是左下角，所以它们的Y坐标:应该如下设置 yPos = singleHeight * i + singleHeight;
			 * */
			//x坐标等于中间-字符串宽度的一半
			float xPos = width / 2 - paint.measureText(b[i]) / 2;//得到绘制字母text的起点的X坐标
			float yPos = singleHeight * i + singleHeight;//得到绘制字母text的起点的Y坐标

			//开始绘制每个字母
			canvas.drawText(b[i], xPos, yPos, paint);

			//绘制完一个字母需要重置一下画笔对象
			paint.reset();
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {//重写view的触摸事件分发方法
		final int action = event.getAction();

		//由于只涉及到Y轴坐标,只获取y坐标
		final float y = event.getY();

		//oldChoose用于记录上一次点击字母所在字母数组中的下标
		final int oldChoose = choose;

		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;

		/*
		点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数。
		得到点击或触摸的位置从而确定对应点击或触摸的字母所在字母数组中的下标
		 */
		final int c = (int) (y / getHeight() * b.length);

		switch (action) {
			case MotionEvent.ACTION_UP:
				/*
				摁下侧边栏，再抬起手势，呈现的背景色
				0x00000000表示透明色：R:0 G:0 B:0 A:0
				 */
				//setBackground(new ColorDrawable(0x00000000));
				setBackgroundColor(ContextCompat.getColor(getContext(),R.color.transparent));

				//此时记录下标的变量也需要重置
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
	 * 注册自定义监听器，触摸事件
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}


	/**
	 * 定义一个接口,用于回调出点击后的字母,显示在弹出的字母对话框中
	 */
	public interface OnTouchingLetterChangedListener {
		void onTouchingLetterChanged(String s);
	}

}