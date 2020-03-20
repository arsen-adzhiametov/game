package com.example.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Sprite {
    /**
     * Рядков в спрайте = 4
     */
    private static final int BMP_ROWS = 4;

    /**
     * Колонок в спрайте = 3
     */
    private static final int BMP_COLUMNS = 3;

    /**
     * Объект класса GameView
     */
    private GameView gameView;

    /**
     * Картинка
     */
    private Bitmap bmp;
    private Bitmap bmpKilled;

    /**
     * Позиция по Х=0
     */
    private int x = 5;

    /**
     * Позиция по У=0
     */
    private int y = 0;

    /**
     * Скорость по Х=5
     */
    private int xSpeed = 5;

    private int ySpeed = 5;

    /**
     * Текущий кадр = 0
     */
    private int currentFrame = 0;
    public boolean killed = false;
    /**
     * Ширина
     */
    private int width;

    /**
     * Ввыоста
     */
    private int height;

    /**
     * Конструктор
     */
    public Sprite(GameView gameView, Bitmap bmp, Bitmap killed) {
        this.gameView = gameView;
        this.bmp = bmp;
        this.bmpKilled = killed;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;

        Random rnd = new Random();
        xSpeed = rnd.nextInt(10) - 5;
        ySpeed = rnd.nextInt(10) - 5;

        x = rnd.nextInt(gameView.getWidth() - width);
        y = rnd.nextInt(gameView.getHeight() - height);
    }

    /**
     * Перемещение объекта, его направление
     */
    private void update() {
        if (x >= gameView.getWidth() - width - xSpeed || x + xSpeed <= 0) {
            xSpeed = -xSpeed;
        }

        x = x + xSpeed;

        if (y >= gameView.getHeight() - height - ySpeed || y + ySpeed <= 0) {
            ySpeed = -ySpeed;
        }

        y = y + ySpeed;
        currentFrame = ++currentFrame % BMP_COLUMNS;
    }

    private void updateKilled() {
        y = y + 30;
    }

    /**
     * Рисуем наши спрайты
     */
    public void onDraw(Canvas canvas) {
        if (killed) {
            updateKilled();
            canvas.drawBitmap(bmpKilled, x, y, null);
        } else {
            update();
            int srcX = currentFrame * width;
            int srcY = getAnimationRow() * height;
            Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
            Rect dst = new Rect(x, y, x + width, y + height);

            canvas.drawBitmap(bmp, src, dst, null);
        }
    }

    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 up, 1 left, 0 down, 2 right
    int[] DIRECTION_TO_ANIMATION_MAP = {3, 1, 0, 2};

    private int getAnimationRow() {
        double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        return DIRECTION_TO_ANIMATION_MAP[direction];
    }

    /**
     * Проверка на столкновения
     */
    public boolean isCollition(float x2, float y2) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }

//    private int getAnimationRow() {
//        if (xSpeed>0 && ySpeed<xSpeed) return 2;
//        else if(xSpeed>0 && ySpeed>xSpeed) return 0;
//        else if(xSpeed>0 && ySpeed<xSpeed) return 3;
//        else return 1;
//    }
}
