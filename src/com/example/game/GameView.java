package com.example.game;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView {

    /**
     * Загружаем спрайт
     */
    private Bitmap bmp;
    private Bitmap bmpKilled;

    /**
     * Поле рисования
     */
    private SurfaceHolder holder;

    /**
     * объект класса GameView
     */
    private GameManager gameLoopThread;

    /**
     * Объект класса Sprite
     */
    private List<Sprite> sprites = new ArrayList<Sprite>();

    /**
     * Конструктор
     */
    public GameView(Context context) {
        super(context);
        gameLoopThread = new GameManager(this);
        holder = getHolder();

          /*Рисуем все наши объекты и все все все*/
        holder.addCallback(new SurfaceHolder.Callback() {
            /*** Уничтожение области рисования */
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            /** Создание области рисования */
            public void surfaceCreated(SurfaceHolder holder) {
                createSprites();
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
                startTime = System.currentTimeMillis();
            }

            /** Изменение области рисования */
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
        bmpKilled = BitmapFactory.decodeResource(getResources(), R.drawable.killed);
    }

    private void createSprites() {
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));
        sprites.add(createSprite(R.drawable.sprite));

    }

    private Sprite createSprite(int resouce) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);

        return new Sprite(this, bmp, bmpKilled);
    }

    /**
     * Функция рисующая все спрайты и фон
     */
    long startTime;
    long time;

    private void updateTime() {
        time = System.currentTimeMillis() - startTime;
    }

    protected void onDraw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(Color.BLACK);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(20);
            if (isAnybodyAlive()) updateTime();
            canvas.drawText("time: " + time + " ms", 20, 20, paint);
            for (Sprite sprite : sprites)
                sprite.onDraw(canvas);
        }
    }

    private boolean isAnybodyAlive() {
        for (Sprite sprite : sprites) {
            if (sprite.killed == false) return true;
        }
        return false;
    }

    long lastClick = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            synchronized (getHolder()) {
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    if (sprite.isCollition(event.getX(), event.getY())) {
                        sprite.killed = true;
                        break;
                    }
                }
                return super.onTouchEvent(event);
            }
        }
        return true;
    }
}