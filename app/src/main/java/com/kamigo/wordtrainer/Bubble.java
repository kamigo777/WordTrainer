package com.kamigo.wordtrainer;

import java.util.Random;

public class Bubble {
    public float x, y;
    public float radius;
    public float speedY;
    public float driftX;
    public float driftPhase;

    private static final Random random = new Random();

    public Bubble(int width, int height) {
        x = random.nextInt(width);
        y = height + random.nextInt(height / 2); // старт ниже экрана

        radius = 10 + random.nextInt(20); // размер пузырька (10–30 px)

        // БЫСТРЕЕ поднимаются: 7–11 пикселей за кадр
        speedY = 7 + random.nextFloat() * 4;

        driftX = 0.5f + random.nextFloat(); // боковое колебание
        driftPhase = random.nextFloat() * (float) Math.PI * 2;
    }

    public void update() {
        y -= speedY;
        driftPhase += 0.05f;
        x += (float) Math.sin(driftPhase) * driftX;
    }

    public boolean isOffScreen() {
        return y + radius < 0;
    }
}
