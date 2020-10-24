package edu.sjsu.android.accgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

public class SimulationView extends View implements SensorEventListener {
    private Bitmap mField;
    private Bitmap mBasket;
    private Bitmap mBitmap;
    private Display mDisplay;
    private static final int BALL_SIZE = 128;
    private static final int BASKET_SIZE = 160;

    private float mXOrigin;
    private float mYOrigin;
    private float mHorizontalBound;
    private float mVerticalBound;

    private float mSensorX;
    private float mSensorY;
    private float mSensorZ;
    private long mSensorTimeStamp;

    private Particle mBall = new Particle();

    private SensorManager sensorManager;

    public SimulationView(Context context) {
        super(context);
        // Initialize images from drawable
        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        mBitmap = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true);
        Bitmap basket = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
        mBasket = Bitmap.createScaledBitmap(basket, BASKET_SIZE, BASKET_SIZE, true);
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inPreferredConfig = Bitmap.Config.RGB_565;
//        mField = BitmapFactory.decodeResource(getResources(), R.drawable.field, opts);

        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        mField = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.field), (int) mDisplay.getWidth(),
                (int) mDisplay.getHeight(), false);

        // Initialize Sensor (register listener in startSimulation method)
        sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mXOrigin = w * 0.5f;
        mYOrigin = h * 0.5f;

        mHorizontalBound = (w - BALL_SIZE) * 0.5f;
        mVerticalBound = (h - BALL_SIZE) * 0.5f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mField, 0, 0, null);
        canvas.drawBitmap(mBasket, mXOrigin - BASKET_SIZE / 2, mYOrigin - BASKET_SIZE / 2, null);

        mBall.updatePosition(mSensorX, mSensorY, mSensorZ, mSensorTimeStamp);
        mBall.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound);

        canvas.drawBitmap(mBitmap,
                (mXOrigin - BALL_SIZE / 2) + mBall.mPosX,
                (mYOrigin - BALL_SIZE / 2) - mBall.mPosY, null);
        invalidate();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }

        switch (mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                mSensorX = sensorEvent.values[0];
                mSensorY = sensorEvent.values[1];
                break;
            case Surface.ROTATION_90:
                mSensorX = -sensorEvent.values[1];
                mSensorY = sensorEvent.values[0];
                break;
            case Surface.ROTATION_180:
                mSensorX = -sensorEvent.values[0];
                mSensorY = -sensorEvent.values[1];
                break;
            case Surface.ROTATION_270:
                mSensorX = sensorEvent.values[1];
                mSensorY = sensorEvent.values[0];
                break;
        }
        mSensorZ = sensorEvent.values[2];
        mSensorTimeStamp = sensorEvent.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void startSimulation() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopSimulation() {
        sensorManager.unregisterListener(this);
    }
}