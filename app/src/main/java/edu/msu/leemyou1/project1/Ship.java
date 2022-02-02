package edu.msu.leemyou1.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Ship {
    /**
     * THe image for the actual piece.
     */
    private final Bitmap piece;

    /**
     * x location.
     * We use relative x locations in the range 0-1 for the center
     * of the puzzle piece.
     */
    private float x;

    /**
     * y location
     */
    private float y;

    /**
     * The ship piece ID
     */
    private final int id;

    /**
     * the grid
     */
    private Grid grid;

    /**
     * We consider a piece to be in the right location if within
     * this distance.
     */
    final static float SNAP_DISTANCE = 0.125f;

    private boolean snapped = false;

    private float player1X;

    private float player1Y;

    private float player2X;

    private float player2Y;

    public Ship(Context context, int id, float x, float y) {
        this.x = x;
        this.y = y;
        this.id = id;
        piece = BitmapFactory.decodeResource(context.getResources(), id);

    }

    /**
     * Draw the ship piece
     * @param canvas Canvas we are drawing on
     * @param marginX Margin x value in pixels
     * @param marginY Margin y value in pixels
     * @param puzzleSize Size we draw the puzzle in pixels
     * @param scaleFactor Amount we scale the puzzle pieces when we draw them
     */
    public void draw(Canvas canvas, int marginX, int marginY, int puzzleSize, float scaleFactor) {
        canvas.save();

        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(marginX + x * puzzleSize, marginY + y * puzzleSize);

        // Scale it to the right size
        canvas.scale(scaleFactor, scaleFactor);

        // This magic code makes the center of the piece at 0, 0
        canvas.translate(-piece.getWidth() / 2f, -piece.getHeight() / 2f);

        // Draw the bitmap
        canvas.drawBitmap(piece, 0, 0, null);
        canvas.restore();
    }

    /**
     * Test to see if we have touched a ship piece
     * @param testX X location as a normalized coordinate (0 to 1)
     * @param testY Y location as a normalized coordinate (0 to 1)
     * @param shipSize the size of the ship in pixels
     * @param scaleFactor the amount to scale a piece by
     * @return true if we hit the piece
     */
    public boolean hit(float testX, float testY,
                       int shipSize, float scaleFactor) {

        // Make relative to the location and size to the piece size
        int pX = (int)((testX - x) * shipSize / scaleFactor) +
                piece.getWidth() / 2;
        int pY = (int)((testY - y) * shipSize / scaleFactor) +
                piece.getHeight() / 2;

        if(pX < 0 || pX >= piece.getWidth() ||
                pY < 0 || pY >= piece.getHeight()) {
            return false;
        }

        // We are within the rectangle of the piece.
        // Are we touching actual picture?
        return (piece.getPixel(pX, pY) & 0xff000000) != 0;

    }

    /**
     * Move the puzzle piece by dx, dy
     * @param dx x amount to move
     * @param dy y amount to move
     */
    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    /**
     * If we are within SNAP_DISTANCE of the correct
     * answer, snap to the correct answer exactly.
     * @return boolean
     */
    public boolean snap(int currentplayer)
    {
        for(ArrayList<GridPiece> outer : grid.getGridPieces())
        {
            for(GridPiece inner : outer) {
                if (Math.abs(x - inner.getX()) < SNAP_DISTANCE &&
                        Math.abs(y - inner.getY()) < SNAP_DISTANCE) {

                    x = inner.getX();
                    y = inner.getY();
                    snapped = true;
                    inner.setShip(this);
                    inner.setHasPlayerShip(true, currentplayer);
                    return true;
                }
            }
        }
        snapped = false;
        return false;
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

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void setSnapped(boolean set) {
        snapped = set;
    }

    public boolean getSnapped() { return snapped; }

    public void savePlayerOne() {
        player1X = x;
        player1Y = y;
    }

    public void savePlayerTwo() {
        player2X = x;
        player2Y = y;
    }

    /**
     * Test to see if we have touched a ship piece
     * @param testX X location as a normalized coordinate (0 to 1)
     * @param testY Y location as a normalized coordinate (0 to 1)
     * @param shipSize the size of the ship in pixels
     * @param scaleFactor the amount to scale a piece by
     * @return true if we hit the piece
     */
    public boolean playerOneHit(float testX, float testY,
                       int shipSize, float scaleFactor) {

        // Make relative to the location and size to the piece size
        int pX = (int)((testX - player1X) * shipSize / scaleFactor) +
                piece.getWidth() / 2;
        int pY = (int)((testY - player1Y) * shipSize / scaleFactor) +
                piece.getHeight() / 2;

        if(pX < 0 || pX >= piece.getWidth() ||
                pY < 0 || pY >= piece.getHeight()) {
            return false;
        }

        // We are within the rectangle of the piece.
        // Are we touching actual picture?
        return (piece.getPixel(pX, pY) & 0xff000000) != 0;

    }

    /**
     * Test to see if we have touched a ship piece
     * @param testX X location as a normalized coordinate (0 to 1)
     * @param testY Y location as a normalized coordinate (0 to 1)
     * @param shipSize the size of the ship in pixels
     * @param scaleFactor the amount to scale a piece by
     * @return true if we hit the piece
     */
    public boolean playerTwoHit(float testX, float testY,
                                int shipSize, float scaleFactor) {

        // Make relative to the location and size to the piece size
        int pX = (int)((testX - player2X) * shipSize / scaleFactor) +
                piece.getWidth() / 2;
        int pY = (int)((testY - player2Y) * shipSize / scaleFactor) +
                piece.getHeight() / 2;

        if(pX < 0 || pX >= piece.getWidth() ||
                pY < 0 || pY >= piece.getHeight()) {
            return false;
        }

        // We are within the rectangle of the piece.
        // Are we touching actual picture?
        return (piece.getPixel(pX, pY) & 0xff000000) != 0;

    }

    public float GetP1X(){
        return player1X;
    }

    public float GetP2X(){
        return player2X;
    }

    public float GetP1Y(){
        return player1Y;
    }

    public float GetP2Y(){
        return player2Y;
    }

    public void SetP1X(float val){
        player1X = val;
    }

    public void SetP2X(float val){
        player2X = val;
    }

    public void SetP1Y(float val){
        player1Y = val;
    }

    public void SetP2Y(float val){
        player2Y = val;
    }
}
