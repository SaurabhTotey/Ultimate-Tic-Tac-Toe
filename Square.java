package com.saurabhtotey.ultimatetictactoe;

/**
 * @author Saurabh Totey
 *
 */
public class Square {


    public String taken;
    public TicTacToeBoard miniBoard;

    /**
     * The constructor for a square in one of the miniboards
     */
    public Square(boolean isBigSquare){
        this.taken = "open";
        this.miniBoard = null;
        if(isBigSquare){
            this.miniBoard = new TicTacToeBoard(false);
        }
    }

    public void updateBigSquare(){
        if(this.miniBoard != null){
            this.taken = this.miniBoard.boardWonBy();
        }
    }

    @Override
    public Square clone(){
        Square toReturn;
        if(this.miniBoard == null){
            toReturn = new Square(false);
        }else{
            toReturn = new Square(true);
            toReturn.miniBoard = this.miniBoard.clone();
        }
        toReturn.taken = this.taken;
        return toReturn;
    }
}
