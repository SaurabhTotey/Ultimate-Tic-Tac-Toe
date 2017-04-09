package com.saurabhtotey.ultimatetictactoe;

/**
 * @author Saurabh Totey
 *
 */
public class TicTacToeBoard {

    public Square[][] board;
    public boolean isBig;

    public TicTacToeBoard(boolean isBigBoard){
        this.board = new Square[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                this.board[i][j] = new Square(isBigBoard);
            }
        }
        isBig = isBigBoard;
    }

    @Override
    public TicTacToeBoard clone(){
        TicTacToeBoard toReturn = new TicTacToeBoard(this.isBig);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                toReturn.board[i][j] = this.board[i][j].clone();
            }
        }
        return toReturn;
    }

    public void turnBoardSquareTo(String toTurnSquareInto, int boardX, int boardY){
        this.board[boardX][boardY].taken = toTurnSquareInto;
    }

    public String boardWonBy(){
        String[] possibilities = {"circle", "x"};
        String whoWon = "";
        for(String winner : possibilities) {
            for(int i = 0; i < 3; i++) {
                if(this.board[i][0].taken.contains(winner) && this.board[i][1].taken.contains(winner) && this.board[i][2].taken.contains(winner)){
                    whoWon += winner;
                }
                if(this.board[0][i].taken.contains(winner) && this.board[1][i].taken.contains(winner) && this.board[2][i].taken.contains(winner)){
                    whoWon += winner;
                }
            }
            if(this.board[0][0].taken.contains(winner) && this.board[1][1].taken.contains(winner) && this.board[2][2].taken.contains(winner)){
                whoWon += winner;
            }
            if(this.board[2][0].taken.contains(winner) && this.board[1][1].taken.contains(winner) && this.board[0][2].taken.contains(winner)){
                whoWon += winner;
            }
        }
        if(whoWon.equals("")){
            whoWon = "x_circle";
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(this.board[i][j].taken.equals("open")){
                        whoWon = "open";
                        break;
                    }
                }
                if(whoWon.equals("open")){
                    break;
                }
            }
        }
        return whoWon;
    }

}
