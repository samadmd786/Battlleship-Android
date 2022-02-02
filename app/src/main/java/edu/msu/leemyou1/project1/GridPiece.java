package edu.msu.leemyou1.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class GridPiece {
    /**
     * The image for the actual piece.
     */
    private boolean hasShip = false;
    private boolean hasp1ship = false;
    private boolean hasp2ship = false;

    private final Bitmap piece;

    private Bitmap missedMarker;

    private Bitmap hitMarker;

    private Bitmap shipPic;
    /**
     * x location.
     * We use relative x locations in the range 0-1 for the center
     * of the puzzle piece
     */
    private float x = 0;

    /**
     * y location
     */
    private float y = 0;

    /**
     * The grid piece ID
     */
    private final int id;

    private Ship ship = null;

    /**
     * Used to handle hit/miss markers
     * 0 = not shot at, 1 = miss, hit = 2
     */
    private int isHitP1 = 0;
    private int isHitP2 = 0;

    public GridPiece(Context context, int id) {
        this.id = id;
        piece = BitmapFactory.decodeResource(context.getResources(), id);

        missedMarker = BitmapFactory.decodeResource(context.getResources(), R.drawable.missmarker);

        hitMarker = BitmapFactory.decodeResource(context.getResources(), R.drawable.hitmarker);

        shipPic = BitmapFactory.decodeResource(context.getResources(), R.drawable.patrol_boat);

        Bitmap resizedBitmap2 = Bitmap.createScaledBitmap(hitMarker, piece.getWidth(),  piece.getHeight(), false);
        hitMarker = resizedBitmap2;
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(missedMarker,  piece.getWidth(),  piece.getHeight(), false);
        missedMarker = resizedBitmap;
        Bitmap resizedBitmap3 = Bitmap.createScaledBitmap(shipPic,  shipPic.getWidth() * 2,  shipPic.getHeight() * 2, false);
        shipPic = resizedBitmap3;
    }

    /**
     * Draw the puzzle piece
     * @param canvas Canvas we are drawing on
     * @param marginX Margin x value in pixels
     * @param marginY Margin y value in pixels
     * @param gridSize Size we draw the puzzle in pixels
     * @param scaleFactor Amount we scale the puzzle pieces when we draw them
     */
    public void draw(Canvas canvas, int marginX, int marginY, int gridSize, float scaleFactor, int player) {
        canvas.save();

        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(marginX + x * gridSize, marginY + y * gridSize);

        // Scale it to the right size
        canvas.scale(scaleFactor, scaleFactor);

        // This magic code makes the center of the piece at 0, 0
        canvas.translate(-piece.getWidth() / 2f, -piece.getHeight() / 2f);

        // Draw the bitmap
        canvas.drawBitmap(piece, 0, 0, null);


        if (player == 1){
            if (isHitP2 == 2){
                canvas.translate(-shipPic.getWidth() / 2f, -shipPic.getHeight() / 2f);
                canvas.drawBitmap(shipPic, piece.getWidth() / 2f, piece.getHeight() / 2f, null);
                canvas.restore();
                canvas.save();

                // Convert x,y to pixels and add the margin, then draw
                canvas.translate(marginX + x * gridSize, marginY + y * gridSize);

                // Scale it to the right size
                canvas.scale(scaleFactor, scaleFactor);

                // This magic code makes the center of the piece at 0, 0
                canvas.translate(-piece.getWidth() / 2f, -piece.getHeight() / 2f);
                canvas.drawBitmap(hitMarker, 0, 0, null);
            }
            else if (isHitP2 == 1){

                canvas.drawBitmap(missedMarker, 0, 0, null);
            }
        }else if (player == 2){
            if (isHitP1 == 2){
                canvas.translate(-shipPic.getWidth() / 2f, -shipPic.getHeight() / 2f);
                canvas.drawBitmap(shipPic, piece.getWidth() / 2f, piece.getHeight() / 2f, null);
                canvas.translate(marginX + x * gridSize, marginY + y * gridSize);
                canvas.translate(-piece.getWidth() / 2f, -piece.getHeight() / 2f);
                canvas.restore();
                canvas.save();

                // Convert x,y to pixels and add the margin, then draw
                canvas.translate(marginX + x * gridSize, marginY + y * gridSize);

                // Scale it to the right size
                canvas.scale(scaleFactor, scaleFactor);

                // This magic code makes the center of the piece at 0, 0
                canvas.translate(-piece.getWidth() / 2f, -piece.getHeight() / 2f);
                canvas.drawBitmap(hitMarker, 0, 0, null);
            }
            else if (isHitP1 == 1){

                canvas.drawBitmap(missedMarker, 0, 0, null);
            }
        }
        canvas.restore();
    }

    public boolean hasShip()
    {
        return hasShip;
    }

    public void setHasShip(boolean val)
    {
        hasShip = val;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public int getWidth(){
        return piece.getWidth();
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public boolean setHit(boolean val, int player) {
        if (player == 1){
            if (val && !hasp1ship){
                isHitP2 = 2;
                return true;
            }
            else if (!val && !hasp1ship){
                isHitP2 = 1;
                return true;
            }
            else if (val && hasp1ship && hasp2ship){
                isHitP2 = 2;
                return true;
            }
            else if (val && hasp1ship){
                isHitP2 = 1;
                return false;
            }
        }
        else if (player == 2){
            if (val && !hasp2ship){
                isHitP1 = 2;
                return true;
            }
            else if (!val  && !hasp2ship){
                isHitP1 = 1;
                return true;
            }
            else if (val && hasp2ship && hasp1ship){
                isHitP1 = 2;
                return true;
            }
            else if (val && hasp2ship){
                isHitP1 = 1;
                return false;
            }
        }
        return false;
    }

    public boolean GetHit(int player)
    {
        if (player == 1){
            return isHitP2 == 2 || isHitP2 == 1;
        }else if (player == 2){
            return isHitP1 == 2 || isHitP1 == 1;
        }
        return false;
    }

    void setHasPlayerShip(boolean val, int playernum){
        hasShip = val;
        if (playernum == 1){
            hasp1ship = true;
        }else if (playernum == 2){
            hasp2ship = true;
        }
    }

    public boolean hasPlayer1Ship(){
        return hasp1ship;
    }

    public boolean hasPlayer2Ship(){
        return hasp2ship;
    }

    public int GetisHitP1()
    {
        return isHitP1;
    }

    public int GetisHitP2()
    {
        return isHitP2;
    }

    public void SetisHitP1(int val)
    {
        isHitP1 = val;
    }

    public void SetisHitP2(int val)
    {
        isHitP2 = val;
    }

    public void setHasShipP1(boolean val)
    {
        hasp1ship = val;
    }

    public void setHasShipP2(boolean val)
    {
        hasp2ship = val;
    }
}
