package com.kamigo.wordtrainer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {

    private float waterLevel = 0f; // от 0 (внизу) до 1 (вверху)
    private Paint paint;
    private ObjectAnimator animator;

    private Runnable onReachedTop;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xFF2196F3); // синий цвет волны
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float height = getHeight() * (1 - waterLevel);
        canvas.drawRect(0, height, getWidth(), getHeight(), paint);
    }

    public void startRising(long durationMillis, Runnable callback) {
        stop(); // остановить если уже шло

        onReachedTop = callback;

        animator = ObjectAnimator.ofFloat(this, "waterLevel", 0f, 1f);
        animator.setDuration(durationMillis);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (onReachedTop != null) {
                    onReachedTop.run();
                    onReachedTop = null;
                }
            }
        });

        animator.start();
    }

    public void stop() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    public void resetToBottom() {
        stop();
        waterLevel = 0f;
        invalidate();
    }

    public void setWaterLevel(float level) {
        this.waterLevel = level;
        invalidate();
    }

    public float getWaterLevel() {
        return waterLevel;
    }
}
