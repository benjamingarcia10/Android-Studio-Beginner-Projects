package edu.sjsu.android.accgame;

public class Particle {
    private static final float COR = 0.7f;
    public float mPosX;
    public float mPosY;
    private float mVelX;
    private float mVelY;

    public void updatePosition(float sx, float sy, float sz, long timestamp) {
//        float dt = (System.nanoTime() - timestamp) / 1000000000.0f;
//        float dt = (System.nanoTime() - timestamp);
//        mVelX += -sx * dt;
//        mVelY += -sy * dt;
//        mPosX += mVelX * dt;
//        mPosY += mVelY * dt;

        mVelX += -sx;
        mVelY += -sy;
        mPosX += mVelX;
        mPosY += mVelY;
    }

    public void resolveCollisionWithBounds(float mHorizontalBound, float mVerticalBound) {
        if (mPosX > mHorizontalBound) {
            mPosX = mHorizontalBound;
            mVelX = -mVelX * COR;
        } else if (mPosX < -mHorizontalBound) {
            mPosX = -mHorizontalBound;
            mVelX = -mVelX * COR;
        }
        if (mPosY > mVerticalBound) {
            mPosY = mVerticalBound;
            mVelY = -mVelY * COR;
        } else if (mPosY < -mVerticalBound) {
            mPosY = -mVerticalBound;
            mVelY = -mVelY * COR;
        }
    }
}