package com.kamigo.wordtrainer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import java.util.*;

public class BubbleView extends View {

    private List<Bubble> bubbles = new ArrayList<>();
    private Paint paint = new Paint();
    private Handler handler = new Handler();
    private Random random = new Random();
    private int width, height;

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(0x80FFFFFF); // полупрозрачный белый
        paint.setAntiAlias(true);
        handler.post(updateRunnable);  // запуск анимации пузырьков
    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            updateBubbles();
            invalidate();
            handler.postDelayed(this, 16); // ~60 FPS
        }
    };

    private void updateBubbles() {
        // Добавляем 1–2 пузырька каждый кадр
        if (width > 0 && height > 0) {
            int count = 1 + random.nextInt(2); // 1–2 пузыря
            for (int i = 0; i < count; i++) {
                bubbles.add(new Bubble(width, height));
            }
        }

        // Обновляем и удаляем вышедшие за экран пузыри
        Iterator<Bubble> iterator = bubbles.iterator();
        while (iterator.hasNext()) {
            Bubble bubble = iterator.next();
            bubble.update();
            if (bubble.isOffScreen()) {
                iterator.remove();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (width == 0) width = getWidth();
        if (height == 0) height = getHeight();

        for (Bubble bubble : bubbles) {
            canvas.drawCircle(bubble.x, bubble.y, bubble.radius, paint);
        }
    }

    // ✅ Сброс пузырей вниз
    public void resetToBottom() {
        bubbles.clear();    // Удалить все текущие пузыри
        invalidate();       // Перерисовать View
    }

    // ✅ Запустить подъём пузырей
    public void startRising() {
        handler.removeCallbacks(updateRunnable);
        handler.post(updateRunnable);
    }

    // ✅ Остановить движение пузырей
    public void stopRising() {
        handler.removeCallbacks(updateRunnable);
    }
}
