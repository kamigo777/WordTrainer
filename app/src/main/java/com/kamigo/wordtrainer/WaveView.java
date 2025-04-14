package com.kamigo.wordtrainer;

import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {

    public interface OnWaterReachedTopListener {
        void onWaterTopReached();
    }

    private OnWaterReachedTopListener listener;

    private Paint paint;
    private float waterTopY;
    private float screenHeight;
    private long startTime;
    private final long duration = 6000; // 6 секунд
    private boolean isRunning = false;
    private final Handler handler = new Handler();

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) return;

            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1f, (float) elapsed / duration);

            waterTopY = screenHeight * (1f - progress);
            invalidate();

            // Достигнут верх
            if (progress >= 1f) {
                isRunning = false;
                if (listener != null) {
                    listener.onWaterTopReached();
                }
                return;
            }

            handler.postDelayed(this, 16);
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (screenHeight == 0) screenHeight = getHeight();

        float bottomY = screenHeight;
        float waterHeight = bottomY - waterTopY;

        if (waterHeight > 0) {
            float third = 1f / 3f;
            LinearGradient gradient = new LinearGradient(
                    0, waterTopY, 0, bottomY,
                    new int[]{
                            Color.parseColor("#6CA6CD"),
                            Color.parseColor("#274472"),
                            Color.parseColor("#020B1F")
                    },
                    new float[]{0f, third * 1.5f, 1f},
                    Shader.TileMode.CLAMP
            );
            paint.setShader(gradient);
            canvas.drawRect(0, waterTopY, getWidth(), bottomY, paint);
        }
    }

    public void startRising(OnWaterReachedTopListener listener) {
        this.listener = listener;
        startTime = System.currentTimeMillis();
        isRunning = true;
        handler.post(updateRunnable);
    }

    public void stop() {
        isRunning = false;
        handler.removeCallbacks(updateRunnable);
        invalidate();
    }

    public void reset() {
        waterTopY = getHeight();
        invalidate();
    }
}
