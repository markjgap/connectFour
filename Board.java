/**
 * This class stores a Connect Four game board as a 7x7 2D array where 0 represents an open slot, 1 represents a slot
 * occupied by one of Player 1's tiles, and 2 represents a slot occupied by one of Player 2's tiles.
 */

public class Board {

    public static final int BOARD_SIZE = 7;
    private static final int CONNECT_GOAL = 4;

    //Game Board
    private int[][] board;

    /**
     * Initializes an empty board
     */
    public Board() {

        this.board = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i=0; i<BOARD_SIZE; i++) {
            for (int j=0; j<BOARD_SIZE; j++) {
                this.board[i][j] = 0;
            }
        }
    }

    /**
     * Initializes a copy of the board
     */
    public Board(Board toCopy) {
        this.board = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i=0; i<BOARD_SIZE; i++) {
            for (int j=0; j<BOARD_SIZE; j++) {
                this.board[i][j] = toCopy.board[i][j];
            }
        }
    }

    public int[][] getBoard() {
        return this.board;
    }


    /**
     * Checks to see if there is is an open slot in the specified column. Returns true if the column is not full and
     * false if the column is full.
     */
    public boolean isColumnOpen(int column) {
        if (getFirstEmptySlot(column) < 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Places a tile for player in the specified column. Returns true if the move was valid, false if the move was
     * not valid.
     *
     * E.g. move(1,5) will place a tile for player 1 in column 5 of the board (assuming that it is a valid move)
     *
     * @param player        An integer (either 1 or 2) indicating the number of the player who is placing the tile
     * @param column        An integer between 0 and 6 (inclusive) indicating which column to place the tile in
     * @return              True if the move was valid and the board was updated; false if the move was invalid
     *                      and the board was not updated.
     */
    public boolean move(int player, int column) {
        int row = getFirstEmptySlot(column);
        if (row < 0) {
            return false;
        } else {
            this.board[row][column] = player;
            return true;
        }
    }

    /**
     * Removes the last tile that was placed in the column. Returns true if a tile was removed and false if the
     * column did not contain any tiles
     */
    public boolean undoMove(int column) {
        int lastMoveRow = this.getLastMoveRow(column);
        if (lastMoveRow < 0) {
            return false;
        }

        this.board[lastMoveRow][column] = 0;
        return true;
    }

    /**
     * Takes the column where the last tile was placed and returns one of the following:
     *
     * -1 if the last move did not end the game
     * 0  if the last move ended the game in a tie (this happens when all the slots are filled and no player has won)
     * 1  if the last move ended the game with player 1 winning
     * 2  if the last move ended the game with player 2 winning
     *
     * @param lastMoveColumn the column index where the most recent tile was placed
     */
    public int checkIfGameOver(int lastMoveColumn) {

        int lastMoveRow = this.getLastMoveRow(lastMoveColumn);
        int player = this.board[lastMoveRow][lastMoveColumn];

        boolean winner = this.checkColumn(player, lastMoveRow, lastMoveColumn);
        winner =  winner || this.checkRow(player, lastMoveRow, lastMoveColumn);
        winner = winner || this.checkDiagonal(player, lastMoveRow, lastMoveColumn);

        if (winner) {
            return player;
        }

        return this.isGameTied();
    }

    /**
     * Helper method that prints a representation of the board to System.out
     *
     */
    public void printBoard() {
        String rowSep = "-----------------------------";
        String columnSep = "| ";


        for (int i=BOARD_SIZE-1; i>= 0; i--) {
            System.out.println(rowSep);
            for (int j=0; j<BOARD_SIZE; j++) {
                System.out.print(columnSep + this.cellToString(this.board[i][j]));
            }
            System.out.println(columnSep);
        }
        System.out.println(rowSep);

        System.out.print(" ");
        for (int i=0; i<BOARD_SIZE; i++) {
            System.out.print(" " + i + "  " );
        }
        System.out.println("");
    }

    private String cellToString(int cellValue) {
        if (cellValue == 0) {
            return "  ";
        } else if (cellValue == 1) {
            return "X ";
        } else {
            return "O ";
        }
    }

    private int isGameTied() {
        for (int j=0; j<BOARD_SIZE; j++) {
            if (this.board[BOARD_SIZE-1][j] == 0) {
                return -1;
            }
        }
        return 0;
    }

    private int getFirstEmptySlot(int column) {
        if (column < 0 || column >= BOARD_SIZE) {
            return -1;
        }

        int open = -1;
        for (int i=0; i<BOARD_SIZE; i++) {
            if (this.board[i][column] == 0) {
                open = i;
                break;
            }
        }
        return open;
    }

    private int getLastMoveRow(int lastMoveColumn) {
        if (lastMoveColumn < 0 || lastMoveColumn >= BOARD_SIZE) {
            return -1;
        }

        int lastMoveRow = -1;
        for (int i=BOARD_SIZE-1; i>=0; i--) {
            if (this.board[i][lastMoveColumn] != 0) {
                lastMoveRow = i;
                break;
            }
        }

        return lastMoveRow;
    }



    private boolean checkColumn(int player, int lastMoveRow, int lastMoveColumn) {

        int connectNum = 0;
        for (int i=lastMoveRow; i >= 0; i--) {
            if (this.board[i][lastMoveColumn] == player) {
                connectNum++;
            } else {
                break;
            }
        }

        if (connectNum >= CONNECT_GOAL) {
            return true;
        }

        return false;
    }

    private boolean checkRow(int player, int lastMoveRow, int lastMoveColumn) {
        int connectNum = 0;
        for (int j=lastMoveColumn; j < BOARD_SIZE; j++) {
            if (this.board[lastMoveRow][j] == player) {
                connectNum++;
            } else {
                break;
            }
        }

        for (int j=lastMoveColumn-1; j >= 0; j--) {
            if (this.board[lastMoveRow][j] == player) {
                connectNum++;
            } else {
                break;
            }
        }

        if (connectNum >= CONNECT_GOAL) {
            return true;
        }

        return false;
    }

    private boolean checkDiagonal(int player, int lastMoveRow, int lastMoveColumn) {
        int connectNum = 0;


        for (int i=lastMoveRow, j=lastMoveColumn; i < BOARD_SIZE && j<BOARD_SIZE; i++, j++) {
            if (this.board[i][j] == player) {
                connectNum++;
            } else {
                break;
            }
        }

        for (int i=lastMoveRow-1, j=lastMoveColumn-1; i>=0 && j >= 0; i--, j--) {
            if (this.board[i][j] == player) {
                connectNum++;
            } else {
                break;
            }
        }

        if (connectNum >= CONNECT_GOAL) {
            return true;
        }

        connectNum = 0;
        for (int i=lastMoveRow, j=lastMoveColumn; i < BOARD_SIZE && j>=0; i++, j--) {
            if (this.board[i][j] == player) {
                connectNum++;
            } else {
                break;
            }
        }

        for (int i=lastMoveRow-1, j=lastMoveColumn+1; i >= 0 && j < BOARD_SIZE; i--, j++) {
            if (this.board[i][j] == player) {
                connectNum++;
            } else {
                break;
            }
        }

        if (connectNum >= CONNECT_GOAL) {
            return true;
        }

        return false;
    }


}
