package com.expressive.refresh.demo.view;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.expressive.refresh.demo.R;

public class CircularToggle extends FrameLayout {

	private ImageView ivContained;
	private ImageView ivUncontained;
	private boolean isContained = true;
	private boolean isVisible = true;

	public CircularToggle(Context context) {
		super(context);
		init(context);
	}

	public CircularToggle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CircularToggle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {

		LayoutInflater.from(context).inflate(R.layout.circular_toggle, this, true);

		ivContained   = findViewById(R.id.iv_contained);
		ivUncontained = findViewById(R.id.iv_uncontained);

		setClickable(true);
		setFocusable(true);
		setHapticFeedbackEnabled(true);

		setupElevationAnimator();
		setupTouchScale();
	}

	public void hide() {
		if (isVisible) {
			isVisible = false;
			animate()
				.scaleX(0f)
				.scaleY(0f)
				.setInterpolator(new AccelerateInterpolator(2f))
				.setDuration(200)
				.start();
		}
	}

	public void show() {
		if (!isVisible) {
			isVisible = true;
			animate()
				.scaleX(1f)
				.scaleY(1f)
				.setInterpolator(new DecelerateInterpolator(2f))
				.setDuration(200)
				.start();
		}
	}

	public boolean isVisibleState() {
		return isVisible;
	}
	
	private void setupElevationAnimator() {
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			
			float defaultElevation = dp(0);
			float pressedElevation = dp(12);
			
			StateListAnimator animator = new StateListAnimator();
			
			animator.addState(
			new int[]{android.R.attr.state_pressed},
			ObjectAnimator.ofFloat(this, "translationZ", pressedElevation).setDuration(100)
			);
			
			animator.addState(
			new int[]{},
			ObjectAnimator.ofFloat(this, "translationZ", 0f).setDuration(150)
			);
			
			setStateListAnimator(animator);
		}
	}
	
	private void setupTouchScale() {
		
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
					
					case MotionEvent.ACTION_DOWN:
					animate().scaleX(0.92f).scaleY(0.92f).setDuration(100).start();
					break;
					
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
					animate().scaleX(1f).scaleY(1f).setDuration(120).start();
					break;
				}
				
				return false;
			}
		});
	}
	
	public void toggle() {
		performHapticFeedback(HapticFeedbackConstants.CONFIRM, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
		
		final ImageView current = isContained ? ivContained : ivUncontained;
		final ImageView next = isContained ? ivUncontained : ivContained;
		
		isContained = !isContained;
		
		current.animate()
		.alpha(0f)
		.scaleX(0.85f)
		.scaleY(0.85f)
		.setDuration(120)
		.start();
		
		current.postDelayed(new Runnable() {
			@Override
			public void run() {
				
				current.setVisibility(View.GONE);
				
				next.setVisibility(View.VISIBLE);
				next.setAlpha(0f);
				next.setScaleX(0.6f);
				next.setScaleY(0.6f);
				
				next.animate()
				.alpha(1f)
				.scaleX(1f)
				.scaleY(1f)
				.setInterpolator(new OvershootInterpolator(1.2f))
				.setDuration(260)
				.start();
			}
		}, 120);
	}

	public void setContained(boolean contained) {
		isContained = contained;

		ivContained.setVisibility(contained ? View.VISIBLE : View.GONE);
		ivUncontained.setVisibility(contained ? View.GONE : View.VISIBLE);
	}

	public boolean isContained() {
		return isContained;
	}

	private float dp(float value) {
		return value * getResources().getDisplayMetrics().density;
	}
}
