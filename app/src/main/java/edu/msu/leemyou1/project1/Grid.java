
package edu.msu.leemyou1.project1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Grid {
    /**
     * The size of the grid in pixels
     */

    private int gridSize;

    /**
     * The name of the bundle keys to save the puzzle
     */
    private final static String LOCATIONS = "Grid.locations";
    private final static String IDS = "Grid.ids";
    private final static String LANDSCAPE = "Grid.start";
    private final static String PLAYER1NAME = "Grid.player1name";
    private final static String PLAYER2NAME = "Grid.player2name";
    private final static String CURRENTPLAYER = "Grid.current";
    private final static String HITP1 = "Grid.hitp1";
    private final static String HITP2 = "Grid.hitp2";
    private final static String P1SHIPX = "Grid.p1x";
    private final static String P1SHIPY = "Grid.p1y";
    private final static String P2SHIPX = "Grid.p2x";
    private final static String P2SHIPY = "Grid.p2y";
    private final static String GRIDIDS = "Grid.gridids";
    private final static String HASSHOT = "Grid.shot";
    private final static String HASSHIP = "Grid.ship";
    private final static String HASSHIPP1 = "Grid.hasship1";
    private final static String HASSHIPP2 = "Grid.hasship2";
    private final static String P1BOATS = "Grid.boats1";
    private final static String P2BOATS = "Grid.boats2";
    private final static String SNAPPED = "Grid.snap";
    private final static String FIRST = "Grid.first";
    /**
     * Percentage of the display width or height that
     * is occupied by the grid.
     */
    float SCALE_IN_VIEW = 0.9f;

    /**
     * Left margin in pixels
     */
    private int marginX;

    /**
     * Top margin in pixels
     */
    private int marginY;

    private float scaleFactor;

    private final ArrayList<ArrayList<GridPiece>> gridPieces = new ArrayList<>();

    final static int NUM_OF_COLUMNS = 4;

    final static int NUM_OF_ROWS = 4;

    private final Context mContext;

    private float sizeOfGrid;

    private int shipSize;

    private final ArrayList<Ship> pieces = new ArrayList<>();

    private Ship dragging = null;

    /**
     * Most recent relative X touch when dragging
     */
    private float lastRelX;

    /**
     * Most recent relative Y touch when dragging
     */
    private float lastRelY;

    private final GridView gridView;

    private Player player1;

    private Player player2;

    private boolean start = false;

    private int currentPlayer = -1;

    private boolean hasShot = false;

    private int playerOneBoats = 4;

    private int playerTwoBoats = 4;

    private String p1name = "";
    private String p2name = "";
    private boolean p1first = false;

    private Button buttonDone;

    public Grid(Context context, GridView gridView) {
        // add all grid pieces
        mContext = context;
        for (int a = 0; a < NUM_OF_COLUMNS; ++a) {
            gridPieces.add(new ArrayList<>());
            for (int b = 0; b < NUM_OF_ROWS; ++b) {
                gridPieces.get(a).add(new GridPiece(context, R.drawable.grid_piece));
            }
        }


        // Load the ship pieces
        Ship ship1 = new Ship(context, R.drawable.patrol_boat, 0.05f, 0f);
        Ship ship2 = new Ship(context, R.drawable.patrol_boat, 0.35f, 0f);
        Ship ship3 = new Ship(context, R.drawable.patrol_boat, 0.65f, 0f);
        Ship ship4 = new Ship(context, R.drawable.patrol_boat, 0.95f, 0f);

        ship1.setGrid(this);
        pieces.add(ship1);
        ship2.setGrid(this);
        pieces.add(ship2);
        ship3.setGrid(this);
        pieces.add(ship3);
        ship4.setGrid(this);
        pieces.add(ship4);

        this.gridView = gridView;
    }


    public void draw(Canvas canvas) {
        int hit = canvas.getHeight();
        int wid = canvas.getWidth();

        // Determine the minimum of the two dimensions
        int minDim = wid < hit ? wid : hit;

        gridSize = (int) (minDim * SCALE_IN_VIEW);
        shipSize = (int) (minDim * SCALE_IN_VIEW);

        sizeOfGrid = (float) gridSize / NUM_OF_COLUMNS;
        // Compute the margins so we center the grid
        marginX = (wid - gridSize) / 2;
        marginY = (hit - gridSize) / 2;

        scaleFactor = sizeOfGrid / (float) gridPieces.get(0).get(0).getWidth();

        float scaleFactorShip = (float) (sizeOfGrid / 1.5) / (float) pieces.get(0).getWidth();

        setGridPiecesLocations();

        // draw all the Gridpieces
        for (ArrayList<GridPiece> playingArea : gridPieces) {
            for (GridPiece piece : playingArea) {
                piece.draw(canvas, marginX, marginY, gridSize, scaleFactor, currentPlayer);
            }
        }
        if (!start) {
            for (Ship ship : pieces) {
                ship.draw(canvas, marginX, marginY, shipSize, scaleFactorShip);
            }
        }

        /**
         * The canvas
         */
    }

    public void setGridPiecesLocations() {
        sizeOfGrid = (float) gridSize / NUM_OF_COLUMNS;

        float pieceYLocation = (sizeOfGrid / gridSize) / 2;
        float movePieceYLocationBy = pieceYLocation * 2;

        for (ArrayList<GridPiece> row : gridPieces) {
            float pieceXLocation = (sizeOfGrid / gridSize) / 2;
            float movePieceXLocationBy = pieceXLocation * 2;
            for (GridPiece piece : row) {
                piece.setX(pieceXLocation);
                piece.setY(pieceYLocation);
                pieceXLocation += movePieceXLocationBy;
            }

            pieceYLocation += movePieceYLocationBy;
        }
    }

    /**
     * Tests to see if a shot hit or misses the ship and sets up for drawing the hit/miss marker
     *
     * @param x x location of the click
     * @param y y location of the click
     */
    public void shipHitTest(double x, double y) {
        // finds the nearest snap point to the click and sets x,y to those values
        if (!hasShot) {
            if (start) {
                boolean notOwn = false;
                for (ArrayList<GridPiece> outer : gridPieces) {
                    for (GridPiece inner : outer) {
                        if (Math.abs(x - inner.getX()) < Ship.SNAP_DISTANCE &&
                                Math.abs(y - inner.getY()) < Ship.SNAP_DISTANCE && !inner.GetHit(currentPlayer)) {
                            if (inner.hasShip()) {
                                notOwn = inner.setHit(true, currentPlayer);
                                if (currentPlayer == 1 && notOwn == true) {
                                    playerTwoBoats -= 1;
                                    if (playerTwoBoats == 0) {
                                        Intent intent = new Intent(mContext, ResultActivity.class);
                                        intent.putExtra("winner", p1name);
                                        intent.putExtra("loser", p2name);
                                        mContext.startActivity(intent);
                                    }
                                } else if (currentPlayer == 2 && notOwn == true) {
                                    playerOneBoats -= 1;
                                    if (playerOneBoats == 0) {
                                        Intent intent = new Intent(mContext, ResultActivity.class);
                                        intent.putExtra("winner", p2name);
                                        intent.putExtra("loser", p1name);
                                        mContext.startActivity(intent);
                                    }
                                }
                            } else {
                                notOwn = inner.setHit(false, currentPlayer);
                            }
                            hasShot = true;
                            //if (!notOwn){
                            //   hasShot = false;
                            //}
                            break;
                        }
                    }
                }
            }
        }
        if(hasShot) {
            buttonDone.setEnabled(true);
        }
    }

    public void saveInstanceState(Bundle bundle) {
        float[] locations = new float[pieces.size() * 2];
        int[] ids = new int[pieces.size()];

        float[] p1X = new float[pieces.size()];
        float[] p1Y = new float[pieces.size()];
        float[] p2X = new float[pieces.size()];
        float[] p2Y = new float[pieces.size()];
        boolean[] snap = new boolean[pieces.size()];

        int[][] idsGridPieces = {{0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}};
        int[][] p1hitGridPieces = {{0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}};
        int[][] p2hitGridPieces = {{0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}};
        boolean[][] gridHasShip = {{false, false, false, false},
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false}};
        boolean[][] gridHasShipP1 = {{false, false, false, false},
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false}};
        boolean[][] gridHasShipP2 = {{false, false, false, false},
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false}};

        for (int i = 0; i < gridPieces.size(); i++) {
            for (int j = 0; j < gridPieces.get(i).size(); j++) {
                idsGridPieces[i][j] = gridPieces.get(i).get(j).getId();
                p1hitGridPieces[i][j] = gridPieces.get(i).get(j).GetisHitP1();
                p2hitGridPieces[i][j] = gridPieces.get(i).get(j).GetisHitP2();
                gridHasShip[i][j] = gridPieces.get(i).get(j).hasShip();
                gridHasShipP1[i][j] = gridPieces.get(i).get(j).hasPlayer1Ship();
                gridHasShipP2[i][j] = gridPieces.get(i).get(j).hasPlayer2Ship();
            }
        }

        for (int i = 0; i < pieces.size(); i++) {
            Ship piece = pieces.get(i);
            locations[i * 2] = piece.getX();
            locations[i * 2 + 1] = piece.getY();
            ids[i] = piece.getId();

            p1X[i] = piece.GetP1X();
            p1Y[i] = piece.GetP1Y();
            p2X[i] = piece.GetP2X();
            p2Y[i] = piece.GetP2Y();
            snap[i] = piece.getSnapped();
        }


        // gridpiece info
        bundle.putSerializable(HITP1, p1hitGridPieces);
        bundle.putSerializable(HITP2, p2hitGridPieces);
        bundle.putSerializable(GRIDIDS, idsGridPieces);
        bundle.putSerializable(HASSHIP, gridHasShip);
        bundle.putSerializable(HASSHIPP1, gridHasShipP1);
        bundle.putSerializable(HASSHIPP2, gridHasShipP2);

        // ship info
        bundle.putFloatArray(LOCATIONS, locations);
        bundle.putIntArray(IDS, ids);
        bundle.putFloatArray(P1SHIPX, p1X);
        bundle.putFloatArray(P1SHIPY, p1Y);
        bundle.putFloatArray(P2SHIPX, p2X);
        bundle.putFloatArray(P2SHIPY, p2Y);
        bundle.putBooleanArray(SNAPPED, snap);

        // Grid info
        bundle.putInt(CURRENTPLAYER, currentPlayer);
        bundle.putBoolean(LANDSCAPE, start);

        // miscelaneous
        bundle.putBoolean(HASSHOT, hasShot);
        bundle.putString(PLAYER1NAME, p1name);
        bundle.putString(PLAYER2NAME, p2name);
        bundle.putInt(P1BOATS, playerOneBoats);
        bundle.putInt(P2BOATS, playerTwoBoats);
        bundle.putBoolean(FIRST, p1first);
    }

    /**
     * Read the puzzle from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        float[] locations = bundle.getFloatArray(LOCATIONS);
        int[] ids = bundle.getIntArray(IDS);
        start = bundle.getBoolean(LANDSCAPE);
        currentPlayer = bundle.getInt(CURRENTPLAYER);
        hasShot = bundle.getBoolean(HASSHOT);
        p1name = bundle.getString(PLAYER1NAME);
        p2name = bundle.getString(PLAYER2NAME);
        playerOneBoats = bundle.getInt(P1BOATS);
        playerTwoBoats = bundle.getInt(P2BOATS);
        p1first = bundle.getBoolean(FIRST);

        // load gridpiece
        int[][] p1hitGridPieces = (int[][]) bundle.getSerializable(HITP1);
        int[][] p2hitGridPieces = (int[][]) bundle.getSerializable(HITP2);
        boolean[][] hasShip = (boolean[][]) bundle.getSerializable(HASSHIP);
        boolean[][] hasShipP1 = (boolean[][]) bundle.getSerializable(HASSHIPP1);
        boolean[][] hasShipP2 = (boolean[][]) bundle.getSerializable(HASSHIPP2);

        int[][] gridids = (int[][]) bundle.getSerializable(GRIDIDS);
        // load ships
        float[] p1X = bundle.getFloatArray(P1SHIPX);
        float[] p1Y = bundle.getFloatArray(P1SHIPY);
        float[] p2X = bundle.getFloatArray(P2SHIPX);
        float[] p2Y = bundle.getFloatArray(P2SHIPY);
        boolean[] snap = bundle.getBooleanArray(SNAPPED);

        for (int i = 0; i < ids.length - 1; i++) {

            // Find the corresponding piece
            // We don't have to test if the piece is at i already,
            // since the loop below will fall out without it moving anything
            for (int j = i + 1; j < ids.length; j++) {
                Ship t = pieces.get(i);
                pieces.set(i, pieces.get(j));
                pieces.set(j, t);
            }
        }

        // set ship info
        for (int i = 0; i < pieces.size(); i++) {
            Ship piece = pieces.get(i);
            piece.setX(locations[i * 2]);
            piece.setY(locations[i * 2 + 1]);
            pieces.get(i).SetP1X(p1X[i]);
            pieces.get(i).SetP1Y(p1Y[i]);
            pieces.get(i).SetP2X(p2X[i]);
            pieces.get(i).SetP2Y(p2Y[i]);
            pieces.get(i).setSnapped(snap[i]);
            //pieces.set(i, piece);
        }

        // set gridpiece info
        for (int i = 0; i < gridPieces.size(); i++) {
            for (int j = 0; j < gridPieces.get(i).size(); j++) {
                gridPieces.get(i).get(j).SetisHitP1(p1hitGridPieces[i][j]);
                gridPieces.get(i).get(j).SetisHitP2(p2hitGridPieces[i][j]);
                gridPieces.get(i).get(j).setHasShip(hasShip[i][j]);
                gridPieces.get(i).get(j).setHasShipP1(hasShipP1[i][j]);
                gridPieces.get(i).get(j).setHasShipP2(hasShipP2[i][j]);
            }
        }
    }

    /**
     * Handle a touch event from the view.
     * @param view  The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
    public boolean onTouchEvent(View view, MotionEvent event) {
        //
        // Convert an x,y location to a relative location in the
        // grid.
        //

        float relX = (event.getX() - marginX) / shipSize;
        float relY = (event.getY() - marginY) / shipSize;

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                return onTouched(relX, relY);
            case MotionEvent.ACTION_UP:
                shipHitTest(relX, relY);
                view.invalidate();
            case MotionEvent.ACTION_CANCEL:
                return onReleased(view, relX, relY);

            case MotionEvent.ACTION_MOVE:
                // If we are dragging, move the piece and force a redraw
                if (dragging != null) {
                    dragging.move(relX - lastRelX, relY - lastRelY);
                    lastRelX = relX;
                    lastRelY = relY;
                    view.invalidate();
                    return true;
                }
                break;
        }

        return false;
    }

    /**
     * Handle a touch message. This is when we get an initial touch
     * @param x x location for the touch, relative to the ship - 0 to 1 over the ship
     * @param y y location for the touch, relative to the ship - 0 to 1 over the ship
     * @return true if the touch is handled
     */
    private boolean onTouched(float x, float y) {
        if (!start) {
            // Check each piece to see if it has been hit
            // We do this in reverse order so we find the pieces in front
            for (int p = pieces.size() - 1; p >= 0; p--) {
                if (pieces.get(p).hit(x, y, shipSize, scaleFactor)) {
                    // We hit a piece!
                    dragging = pieces.get(p);
                    for (ArrayList<GridPiece> piece : gridPieces) {
                        for (GridPiece gridpiece : piece) {
                            if (gridpiece.getShip() == dragging) {
                                gridpiece.setShip(null);
                            }
                        }
                    }
                    // changes
                    pieces.remove(dragging);
                    pieces.add(dragging);
                    // end changes
                    lastRelX = x;
                    lastRelY = y;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Handle a release of a touch message.
     * @param x x location for the touch release, relative to the ship - 0 to 1 over the ship
     * @param y y location for the touch release, relative to the ship - 0 to 1 over the ship
     * @return true if the touch is handled
     */
    private boolean onReleased(View view, float x, float y) {

        if (dragging != null) {
            if (dragging.snap(currentPlayer)) {
                // We have snapped into place
                view.invalidate();
            }
            dragging = null;
            return true;
        }

        return false;
    }

    public void AddPlayer1(Player playerOne) {
        player1 = playerOne;
    }

    public void AddPlayer2(Player playerTwo) {
        player2 = playerTwo;
    }

    public ArrayList<ArrayList<GridPiece>> getGridPieces() {
        return gridPieces;
    }

    public boolean isDone() {
        int counter = 0;

        for (Ship piece : pieces) {
            if (piece.getSnapped()) {
                counter += 1;
            }
        }

        return counter == 4;
    }

    public ArrayList<Ship> getShips() {
        return pieces;
    }

    public void gridReset() {
        int count = 1;
        for (Ship piece : pieces) {
            piece.setSnapped(false);
            if (count == 1) {
                piece.setX(0.05f);
            } else if (count == 2) {
                piece.setX(0.35f);
            } else if (count == 3) {
                piece.setX(0.65f);
            } else if (count == 4) {
                piece.setX(0.95f);
            }
            piece.setY(0);
            count += 1;
        }
    }

    public void saveShipLocation(Player thePlayer, String playerNumber) {
        if (playerNumber.equals("one")) {
            for (Ship piece : pieces) {
                piece.savePlayerOne();
            }
        }

        if (playerNumber.equals("two")) {
            for (Ship piece : pieces) {
                piece.savePlayerTwo();
            }
        }
        thePlayer.setShip(pieces);
    }

    public void gameBegin() {
        start = true;
    }

    public void setPlayerTurn(int turn) {
        currentPlayer = turn;
    }

    public int GetPlayerTurn() {
        return currentPlayer;
    }

    public void resetShot() {
        hasShot = false;
    }

    public boolean getShot() {
        return hasShot;
    }

    public int getPlayerOneBoats() {
        return playerOneBoats;
    }

    public int getPlayerTwoBoats() {
        return playerTwoBoats;
    }

    public void scale(float dScale, float xOne, float yOne) {
        if (dScale > 1) {
            SCALE_IN_VIEW *= 1.02;
        } else if (dScale < 1) {
            SCALE_IN_VIEW *= 0.98;
        }
    }

    public void setP1name(String player1Name) {
        p1name = player1Name;
    }

    public void setP2name(String player2Name) {
        p2name = player2Name;
    }

    public String getP1name() {
        return p1name;
    }

    public String getP2name() {
        return p2name;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getStart() {
        return start;
    }

    public void setDoneButton(Button doneButton) {
        this.buttonDone = doneButton;
    }

    public boolean getP1First()
    {
        return p1first;
    }

    public void setP1First(boolean val)
    {
        p1first = val;
    }
}
