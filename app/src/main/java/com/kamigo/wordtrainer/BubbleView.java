package com.kamigo.wordtrainer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Choreographer;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BubbleView extends View implements Choreographer.FrameCallback {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final List<Bubble> bubbles = new ArrayList<>();
    private final Random random = new Random();
    private final Handler delayHandler = new Handler();

    private int viewWidth;
    private int viewHeight;
    private boolean isRunning = false;
    private long riseDuration = 6000;
    private long startTime;

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(0x80FFFFFF); // полупрозрачный белый
    }

    public void startBubbles(long durationMillis) {
        riseDuration = durationMillis;
        startTime = System.currentTimeMillis();
        isRunning = true;

        delayHandler.postDelayed(() -> {
            Choreographer.getInstance().postFrameCallback(this);
        }, 2000); // задержка 2 секунды
    }

    public void stopBubbles() {
        isRunning = false;
        Choreographer.getInstance().removeFrameCallback(this);
        delayHandler.removeCallbacksAndMessages(null);
    }

    public void resetBubbles() {
        stopBubbles();
        bubbles.clear();
        invalidate();
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        if (!isRunning) return;

        long now = System.currentTimeMillis();

        // создаём 1–2 пузыря за кадр
        for (int i = 0; i < 1 + random.nextInt(2); i++) {
            addBubble(now);
        }

        Iterator<Bubble> iterator = bubbles.iterator();
        while (iterator.hasNext()) {
            Bubble bubble = iterator.next();
            float progress = (now - bubble.birthTime) / (float) riseDuration;
            bubble.y = viewHeight - viewHeight * progress;

            // исчезают за 10 пикселей до верхнего края
            if (bubble.y <= 10) {
                iterator.remove();
                continue;
            }

            bubble.alpha = 1.0f - progress;
        }

        invalidate();
        Choreographer.getInstance().postFrameCallback(this);
    }

    private void addBubble(long now) {
        if (viewWidth == 0 || viewHeight == 0) return;
        float radius = 5 + random.nextInt(10);
        float x = radius + random.nextFloat() * (viewWidth - 2 * radius);
        float y = viewHeight;

        bubbles.add(new Bubble(x, y, radius, now));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Bubble bubble : bubbles) {
            paint.setAlpha((int) (bubble.alpha * 255));
            canvas.drawCircle(bubble.x, bubble.y, bubble.radius, paint);
        }
    }

    private static class Bubble {
        float x, y, radius;
        float alpha = 1.0f;
        long birthTime;

        Bubble(float x, float y, float radius, long birthTime) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.birthTime = birthTime;
        }
    }
}