package edu.msu.leemyou1.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String player1Name, player2Name;
    private Player playerOne = null;
    private Player playerTwo = null;
    private TextView playerTurn;
    private int player = -1;
    private boolean start = false;
    private boolean p1WasFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Grid theGrid = getGridView().getGrid();

        if (savedInstanceState != null) {
            playerTurn = findViewById(R.id.playerTurnText);
            getGridView().loadInstanceState(savedInstanceState);
            start = theGrid.getStart();
            // set up stuff for on done here
            player = theGrid.getCurrentPlayer();

            player1Name = savedInstanceState.getString("user");
            player2Name = savedInstanceState.getString("pass");

            if (start) {
                playerOne = new Player(player1Name);
                playerOne.setShip(theGrid.getShips());
                playerTwo = new Player(player2Name);
                playerTwo.setShip(theGrid.getShips());
                if (player == 1) {
                    playerTurn.setText(String.format("%s guess the ship", player1Name));
                } else {
                    playerTurn.setText(String.format("%s guess the ship", player2Name));
                }
            } else {
                if (player == 1) {
                    playerTurn.setText(String.format("%s place your ships on the grid", player1Name));
                    playerOne = new Player(player1Name);
                    if (!theGrid.getP1First())
                    {
                        playerTwo = new Player(player2Name);
                        playerTwo.setShip(theGrid.getShips());
                    }
                    //  playerOne.setShip(theGrid.getShips());
                    //theGrid.setPlayerTurn(1);
                } else {
                    playerTurn.setText(String.format("%s place your ships on the grid", player2Name));
                    playerTwo = new Player(player2Name);

                    if (theGrid.getP1First())
                    {
                        playerOne = new Player(player1Name);
                        playerOne.setShip(theGrid.getShips());
                    }
                    //playerTwo.setShip(theGrid.getShips());

                    //theGrid.setPlayerTurn(2);
                }
            }


            getGridView().invalidate();
        }

        if (savedInstanceState == null) {
            playerTurn = findViewById(R.id.playerTurnText);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                player1Name = extras.getString("user");
                player2Name = extras.getString("pass");
                theGrid.setP1name(player1Name);
                theGrid.setP2name(player2Name);
            }

            Random ran = new Random();
            List<Integer> numList = Arrays.asList(1, 2);
            int startPlayer = numList.get(ran.nextInt(numList.size()));
            // take out
            //startPlayer = 1;
            if (startPlayer == 1) {
                playerTurn.setText(String.format("%s place your ships on the grid", player1Name));
                playerOne = new Player(player1Name);
                p1WasFirst = true;
                theGrid.setP1First(true);
                theGrid.setPlayerTurn(1);
            } else {
                playerTurn.setText(String.format("%s place your ships on the grid", player2Name));
                playerTwo = new Player(player2Name);
                theGrid.setPlayerTurn(2);
            }
        }

        setDoneButton(theGrid, this.findViewById(R.id.doneButton));

    }

    public void onDone(View view) {
        GridView theView = this.findViewById(R.id.puzzleView);
        Grid theGrid = theView.getGrid();
        Button doneButton = findViewById(R.id.doneButton);

        if (theGrid.getPlayerOneBoats() == 0) {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("winner", player2Name);
            intent.putExtra("loser", player1Name);
            startActivity(intent);
        } else if (theGrid.getPlayerTwoBoats() == 0) {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("winner", player1Name);
            intent.putExtra("loser", player2Name);
            startActivity(intent);
        } else {
            if (theGrid.isDone()) {
                //switches from player 1 to player 2 for placing ships
                if (playerTwo == null && playerOne != null && playerOne.getShip().size() == 0) {
                    player = 1;
                    theGrid.AddPlayer1(playerOne);
                    theGrid.saveShipLocation(playerOne, "one");
                    theGrid.gridReset();
                    theView.invalidate();
                    playerTurn.setText(String.format("%s place your ships on the grid", player2Name));
                    playerTwo = new Player(player2Name);
                    theGrid.setPlayerTurn(2);
                    //switches from player 2 to player 1 for placing ships
                } else if (playerOne == null && playerTwo != null && playerTwo.getShip().size() == 0) {
                    player = 2;
                    theGrid.AddPlayer2(playerTwo);
                    theGrid.saveShipLocation(playerTwo, "two");
                    theGrid.gridReset();
                    theView.invalidate();
                    playerTurn.setText(String.format("%s place your ships on the grid", player1Name));
                    playerOne = new Player(player1Name);
                    theGrid.setPlayerTurn(1);
                } else if (playerOne != null && playerOne.getShip().size() == 0) {
                    theGrid.AddPlayer1(playerOne);
                    theGrid.saveShipLocation(playerOne, "one");
                    theGrid.gridReset();
                    theView.invalidate();
                    playerTurn.setText(String.format("%s guess the ship", player2Name));
                } else if (playerTwo != null && playerTwo.getShip().size() == 0) {
                    theGrid.AddPlayer2(playerTwo);
                    theGrid.saveShipLocation(playerTwo, "two");
                    theGrid.gridReset();
                    theView.invalidate();
                    playerTurn.setText(String.format("%s guess the ship", player1Name));
                }
            }

            // start game
            if (playerOne != null && playerTwo != null && playerOne.getShip().size() == 4 && playerTwo.getShip().size() == 4) {
                if (playerOne.getShip().size() == 4 && playerTwo.getShip().size() == 4 && !start) {
                    start = true;
                    theGrid.gameBegin();
                    theView.invalidate();
                    theGrid.setPlayerTurn(player);
                }
            }

            if (start) {
                Button surrenderButton = findViewById(R.id.buttonSurrender);
                surrenderButton.setVisibility(View.VISIBLE);
                doneButton.setEnabled(false);
                if (theGrid.getShot()) {
                    if (theGrid.GetPlayerTurn() == 1) {
                        theGrid.setPlayerTurn(2);
                        playerTurn.setText(String.format("%s guess the ship", player2Name));
                        theView.invalidate();
                        theGrid.resetShot();
                    } else if (theGrid.GetPlayerTurn() == 2) {
                        theGrid.setPlayerTurn(1);
                        playerTurn.setText(String.format("%s guess the ship", player1Name));
                        theView.invalidate();
                        theGrid.resetShot();
                    }
                }
            }
        }
    }

    public void onSurrender(View view) {
        GridView theView = this.findViewById(R.id.puzzleView);
        Grid theGrid = theView.getGrid();

        if (theGrid.GetPlayerTurn() == 1) {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("winner", player2Name);
            intent.putExtra("loser", player1Name);
            startActivity(intent);
        } else if (theGrid.GetPlayerTurn() == 2) {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("winner", player1Name);
            intent.putExtra("loser", player2Name);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        getGridView().saveInstanceState(bundle);
    }

    /**
     * Get the puzzle view
     *
     * @return PuzzleView reference
     */
    private GridView getGridView() {
        return this.findViewById(R.id.puzzleView);
    }

    public void setDoneButton(Grid grid, Button button) {
        grid.setDoneButton(button);
    }

}