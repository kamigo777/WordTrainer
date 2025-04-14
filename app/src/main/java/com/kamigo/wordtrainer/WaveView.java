package com.kamigo.wordtrainer;

import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {

    private Paint paint;
    private float waterTopY;
    private float screenHeight;
    private long startTime;
    private final long duration = 6000; // 6 секунд
    private Handler handler = new Handler();

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        startTime = System.currentTimeMillis();
        handler.post(updateRunnable);
    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
            handler.postDelayed(this, 16);
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (screenHeight == 0) {
            screenHeight = getHeight();
        }

        long elapsed = System.currentTimeMillis() - startTime;
        float progress = Math.min(1f, (float) elapsed / duration);

        waterTopY = screenHeight * (1f - progress);
        float bottomY = screenHeight;
        float waterHeight = bottomY - waterTopY;

        if (waterHeight > 0) {
            float third = 1f / 3f;

            LinearGradient gradient = new LinearGradient(
                    0, waterTopY,
                    0, bottomY,
                    new int[] {
                            Color.parseColor("#6CA6CD"), // верх — светлее синий
                            Color.parseColor("#274472"), // центр — тёмно-синий
                            Color.parseColor("#020B1F")  // низ — почти чёрный
                    },
                    new float[] {
                            0f,
                            third * 1.5f,
                            1f
                    },
                    Shader.TileMode.CLAMP
            );

            paint.setShader(gradient);
            canvas.drawRect(0, waterTopY, getWidth(), bottomY, paint);
        }
    }
}
