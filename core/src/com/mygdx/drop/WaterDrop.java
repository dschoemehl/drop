package com.mygdx.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;

public class WaterDrop {
    float x;
    float y;
    float size;
    float speed;
    float scaleSpeed;
    float originalSize;
    float elapsedTime = .2f;
    Interpolation easeSize = Interpolation.bounceOut;
    Rectangle collision;

    public WaterDrop(float x, float y, float size, float speed, float scaleSpeed){
        this.x = x;
        this.y = y;
        this.size = size;
        this.originalSize = size;
        this.speed = speed;
        this.scaleSpeed = scaleSpeed;
        collision = new Rectangle(x,y,size,size);
    }

    public void update(float deltaTime){
        elapsedTime += deltaTime;

        //juicy scaling
        float progress = Math.min(1f, elapsedTime/scaleSpeed);
        float sizeScale = easeSize.apply(progress);
        size = originalSize * sizeScale;

        //move position and collision rect
        y -= speed * deltaTime;
        collision.x = x;
        collision.y = y;
    }

}
