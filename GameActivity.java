package com.saurabhtotey.ultimatetictactoe;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;

public class GameActivity extends AppCompatActivity {

    public TicTacToeBoard mainBoard = null;
    public String[] players = {"Player One", ""};
    public HashMap<String, String> correspondingSymbols = new HashMap<String, String>();
    public String whoseTurn = players[0];
    public int[] prevMove = {-1, -1};
    public boolean isUltimate;
    public int opponentType;
    public boolean allowMove = true;

    public String computerToWin = "11 ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        isUltimate = intent.getBooleanExtra("isUltimate", false);
        mainBoard = new TicTacToeBoard(isUltimate);
        if(isUltimate){
            setContentView(R.layout.activity_ultimate_tic_tac_toe_board);
            updateToGo();
        }else{
            setContentView(R.layout.activity_quick_tac_toe_board);
        }
        opponentType = intent.getIntExtra("opponentType", 0);
        players[1] = (opponentType == 2) ? "The Computer" : "Player Two";
        correspondingSymbols.put(players[0], "x");
        correspondingSymbols.put(players[1], "circle");
    }

    public void chooseSquare(View view){
        String toChangeTo = correspondingSymbols.get(whoseTurn);
        chooseSquare(Integer.parseInt(Character.toString(((String) view.getTag()).charAt(0))), Integer.parseInt(Character.toString(((String) view.getTag()).charAt(1))));
    }

    public boolean chooseSquare(int first, int second){
        if(!allowMove){
            return false;
        }
        boolean validMove = (!isUltimate && opponentType == 0 && changeSquare(first / 3, second / 3, correspondingSymbols.get(whoseTurn))) || (!isUltimate && opponentType == 1 && changeSquare(first / 3, second / 3, correspondingSymbols.get(whoseTurn))) || (!isUltimate && opponentType == 2 && changeSquare(first / 3, second / 3, correspondingSymbols.get(whoseTurn))) || (isUltimate && opponentType == 0 && changeSquare(first, second, correspondingSymbols.get(whoseTurn), prevMove[0], prevMove[1])) || (isUltimate && opponentType == 1 && changeSquare(first, second, correspondingSymbols.get(whoseTurn), prevMove[0], prevMove[1])) || (isUltimate && opponentType == 2 && changeSquare(first, second, correspondingSymbols.get(whoseTurn), prevMove[0], prevMove[1]));
        if(validMove) {
            prevMove[0] = first % 3;
            prevMove[1] = second % 3;
            if(whoseTurn.equals(players[0])){
                whoseTurn = players[1];
            } else {
                whoseTurn = players[0];
            }
            if(mainBoard.boardWonBy().contains("x") && mainBoard.boardWonBy().contains("circle")){
                ((TextView) findViewById(R.id.textViewTop)).setText("No one wins!");
                ((TextView) findViewById(R.id.textViewBottom)).setText("Game over!");
                allowMove = false;
            }else if(mainBoard.boardWonBy().contains("circle")){
                ((TextView) findViewById(R.id.textViewTop)).setText(players[1] + " wins!");
                ((TextView) findViewById(R.id.textViewBottom)).setText("Game over!");
                allowMove = false;
            }else if(mainBoard.boardWonBy().contains("x")){
                ((TextView) findViewById(R.id.textViewTop)).setText(players[0] + " wins!");
                ((TextView) findViewById(R.id.textViewBottom)).setText("Game over!");
                allowMove = false;
            }else{
                ((TextView) findViewById(R.id.textViewTop)).setText("");
                ((TextView) findViewById(R.id.textViewBottom)).setText(whoseTurn + "'s turn!");
            }
            if(isUltimate){
                updateToGo();
            }
        }
        if(opponentType == 2 && whoseTurn.equals(players[1])){
            computerChooseSquare();
        }
        return validMove;
    }

    public void computerChooseSquare(){
        int[] best = {1, 0};
        String[] edges = {"10", "01", "21", "12"};
        if(isUltimate){
            if(computerToWin.equals("11 ")){
                if(prevMove[0] == 0 && prevMove[1] == 0 || prevMove[0] == 2 && prevMove[1] == 2){
                    computerToWin += "00 22";
                }else{
                    computerToWin += "20 02";
                }
            }
            best[0] = (int) (Math.random() * 9);
            best[1] = (int) (Math.random() * 9);

            chooseSquare(best[0], best[1]);
        }else{
            int numOpen = 0;
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(mainBoard.board[i][j].taken.equals("open")){
                        numOpen++;
                    }
                }
            }
            if(numOpen == 8 && mainBoard.board[1][1].taken.equals("open")){
                best[1] = 1;
            }else if(numOpen == 8){
                best[0] = 0;
            }else if(!(mainBoard.board[1][1].taken.equals("circle") && numOpen == 6 && (mainBoard.board[0][0].taken.equals("x") && mainBoard.board[2][2].taken.equals("x") || mainBoard.board[0][2].taken.equals("x") && mainBoard.board[2][0].taken.equals("x")))){
                best[0] = -1;
                int[][][] spotRatingPerMove = new int[3][3][2];
                int[] spotRating = new int[2];
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        if(mainBoard.board[i][j].taken.equals("open")){
                            spotRating = getSpotRating("circle", mainBoard, i, j);
                            spotRatingPerMove[i][j][0] = spotRating[0];
                            spotRatingPerMove[i][j][1] = spotRating[1];
                            if(spotRating[0] == 0){
                                best[0] = i;
                                best[1] = j;
                            }else if(best[0] == -1 || spotRatingPerMove[best[0]][best[1]][0] != 0 && spotRating[1] > spotRatingPerMove[best[0]][best[1]][1]){
                                best[0] = i;
                                best[1] = j;
                            }
                        }else{
                            spotRatingPerMove[i][j][0] = -1;
                            spotRatingPerMove[i][j][1] = -1;
                        }
                    }
                }
            }
            chooseSquare(best[0] * 3, best[1] * 3);
        }
    }

    private int[] getSpotRating(String playerSymbol, TicTacToeBoard board, int xCoord, int yCoord){
        int[] spotRating = new int[2];
        TicTacToeBoard testBoard = board.clone();
        testBoard.board[xCoord][yCoord].taken = playerSymbol;
        String winner = testBoard.boardWonBy();
        if(winner.equals("open")){
            String newSymbol = (playerSymbol.equals("x")) ? "circle" : "x";
            int[] newSpotRating = new int[2];
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(testBoard.board[i][j].taken.equals("open")){
                        newSpotRating = getSpotRating(newSymbol, testBoard, i, j);
                        spotRating[0] += newSpotRating[0];
                        spotRating[1] += newSpotRating[1];
                    }
                }
            }
        }else{
            if(winner.contains("x") && winner.contains("circle")){
                spotRating[0]++;
                spotRating[1]++;
            }else if(winner.contains("x")){
                spotRating[0]++;
            }else{
                spotRating[1]++;
            }
        }
        return spotRating;
    }

    public boolean changeSquare(int squareX, int squareY, String toChangeTo, int toGoBigX, int toGoBigY){
        if(0 <= squareX && 0 <= squareY && 9 > squareX && 9 > squareY && mainBoard.board[squareX / 3][squareY / 3].miniBoard.board[squareX % 3][squareY % 3].taken.equals("open") && ((toGoBigX == -1 || toGoBigY  == -1 || !mainBoard.board[toGoBigX][toGoBigY].taken.equals("open")) || squareX / 3 == toGoBigX && squareY / 3 == toGoBigY) && mainBoard.board[squareX / 3][squareY / 3].taken.equals("open")) {
            mainBoard.board[squareX / 3][squareY / 3].miniBoard.board[squareX % 3][squareY % 3].taken = toChangeTo;
            drawInSmallSquare(toChangeTo, squareX, squareY);
            updateAllBigSquares();
            return true;
        }
        return false;
    }

    public boolean changeSquare(int squareX, int squareY, String toChangeTo){
        if(0 <= squareX && 0 <= squareY && 3 > squareX && 3> squareY && mainBoard.board[squareX][squareY].taken.equals("open")){
            mainBoard.board[squareX][squareY].taken = toChangeTo;
            drawInBigSquare(toChangeTo, squareX, squareY);
            return true;
        }
        return false;
    }

    public void updateAllBigSquares(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                mainBoard.board[i][j].updateBigSquare();
                if(mainBoard.board[i][j].taken.contains("x") && mainBoard.board[i][j].taken.contains("circle")){
                    drawInBigSquare("x_circle", i, j);
                }else if(mainBoard.board[i][j].taken.contains("circle")){
                    drawInBigSquare("circle_transparent_background", i, j);
                }else if(mainBoard.board[i][j].taken.contains("x")){
                    drawInBigSquare("x_transparent_background", i, j);
                }
            }
        }
    }

    public void updateToGo(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(allowMove && mainBoard.board[i / 3][j / 3].miniBoard.board[i % 3][j % 3].taken.equals("open") && ((prevMove[0] == -1 || prevMove[1] == -1 || !mainBoard.board[prevMove[0]][prevMove[1]].taken.equals("open")) || i / 3 == prevMove[0] && j / 3 == prevMove[1]) && mainBoard.board[i / 3][j / 3].taken.equals("open")){
                    ((ImageButton) findViewById(getResources().getIdentifier("b" + i + j, "id", getPackageName()))).setImageResource(R.color.yellow);
                }else if(mainBoard.board[i / 3][j / 3].miniBoard.board[i % 3][j % 3].taken.equals("open")){
                    ((ImageButton) findViewById(getResources().getIdentifier("b" + i + j, "id", getPackageName()))).setImageResource(android.R.color.white);
                }
            }
        }
    }

    public void drawInBigSquare(String toChangeTo, int xCoord, int yCoord){
        int resource;
        if(toChangeTo.equals("x_circle")){
            resource = R.drawable.x_circle;
        } else if(toChangeTo.equals("circle_transparent_background")){
            resource = R.drawable.circle;
        } else if(toChangeTo.equals("x_transparent_background")){
            resource = R.drawable.x;
        } else if(toChangeTo.equals("x")){
            resource = R.drawable.x;
            ((ImageView) findViewById(getResources().getIdentifier("i" + xCoord + yCoord, "id", getPackageName()))).setBackgroundResource(android.R.color.white);
        } else {
            resource = R.drawable.circle;
            ((ImageView) findViewById(getResources().getIdentifier("i" + xCoord + yCoord, "id", getPackageName()))).setBackgroundResource(android.R.color.white);
        }
        ((ImageView) findViewById(getResources().getIdentifier("i" + xCoord + yCoord, "id", getPackageName()))).setVisibility(View.VISIBLE);
        ((ImageView) findViewById(getResources().getIdentifier("i" + xCoord + yCoord, "id", getPackageName()))).setImageResource(resource);
    }

    public void drawInSmallSquare(String toChangeTo, int xCoord, int yCoord){
        int resource = (toChangeTo.equals("x")) ? R.drawable.x : R.drawable.circle;
        ((ImageButton) findViewById(getResources().getIdentifier("b" + xCoord + yCoord, "id", getPackageName()))).setImageResource(resource);
    }

    public void finish(View view){
        finish();
    }

}
