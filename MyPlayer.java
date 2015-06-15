import java.util.ArrayList;
import java.util.Random;

/**
 * Contains a heuristic to check board favorability.
 */
public class MyPlayer extends Player {

    Random rand;

    private class Move {
        int move;
        double value;

        Move(int move) {
            this.move = move;
            this.value = 0.0;
        }
    }

    public MyPlayer() {
        rand = new Random();
        return;
    }

    public void setPlayerNumber(int number) {
        this.playerNumber = number;
    }


    public int chooseMove(Board gameBoard) {

        long start = System.nanoTime();

        Move bestMove = search(gameBoard, 7, this.playerNumber);
        System.out.println(bestMove.value);

        long diff = System.nanoTime()-start;
        double elapsed = (double)diff/1e9;
        System.out.println("Elapsed Time: " + elapsed + " sec");
        return bestMove.move;
    }

    public Move search(Board gameBoard, int maxDepth, int playerNumber) {

        ArrayList<Move> moves = new ArrayList<Move>();

        // Try each possible move
        for (int i=0; i<Board.BOARD_SIZE; i++) {

            // Skip this move if the column isn't open
            if (!gameBoard.isColumnOpen(i)) {
                continue;
            }

            // Place a tile in column i
            Move thisMove = new Move(i);
            gameBoard.move(playerNumber, i);

            // Check gameStatus to see if that ended the game
            // -1 if the last move did not end the game
            // 0 if the last move ended the game in a tie (this happens when all the slots are filled and no player has won)
            // 1  if the last move ended the game with player 1 winning
            // 2  if the last move ended the game with player 2 winning
            int gameStatus = gameBoard.checkIfGameOver(i);
            if (gameStatus >= 0) {

                if (gameStatus == 0) {
                    // Tie game
                    thisMove.value = 0.0;
                } else if (gameStatus == playerNumber) {
                    // Win
                    thisMove.value = 1.0;
                } else {
                    // Loss
                    thisMove.value = -1.0;
                }

            } else if (maxDepth == 0) {
                // If we can't search any more levels down then apply a heuristic to the board
                thisMove.value = heuristic(gameBoard, playerNumber);

            } else {
                // Search down an additional level
                Move responseMove = search(gameBoard, maxDepth-1, (playerNumber == 1 ? 2 : 1));
                thisMove.value = -responseMove.value;
            }

            // Store the move
            moves.add(thisMove);

            // Remove the tile from column i
            gameBoard.undoMove(i);
        }

        // Pick the highest value move
        return this.getBestMove(moves);

    }

    // integer in each position is the number of times that position can be part of a combination of 4.
    private static int[][] weightedTable = {
            {3, 4, 5, 7, 5, 4, 3},
            {4, 6, 8, 10, 8, 6, 4},
            {5, 8, 11, 13, 11, 8, 5},
            {7, 10, 13, 16, 13, 10, 7},
            {5, 8, 11, 13, 11, 8, 5},
            {4, 6, 8, 10, 8, 6, 4},
            {3, 4, 5, 7, 5, 4, 3}
    };

    /*
     * Searches every column to evaluate board and looks for 3 in a row vertically.
     * Also calculates every taken position from a weighted table to check board favorability.
     * @param gameBoard is the board to be evaluated
     * @param playerNumber is the player to check if the board favors
     * @return This should return a number between -1.0 and 1.0.
     *         If the board favors playerNumber then the return value should be close to 1.0
     *         If the board favors playerNumber's opponent then the return value should be close to -1.0
     *         If the board favors neither player then the return value should be close to 0.0
     */
    private static double evaluateColumn(Board gameBoard, int playerNumber) {
        int connectSize1 = 0;
        int connectSize2 = 0;
        double connectTotal = 0;
        double weightedSum = 0;
        for (int i = 0; i < gameBoard.BOARD_SIZE; i++) {
            for (int j = 0; j < gameBoard.BOARD_SIZE; j++) {
                if (gameBoard.getBoard()[i][j] == 0) {
                    break;
                }
                else if (gameBoard.getBoard()[i][j] == playerNumber) {
                    weightedSum += weightedTable[i][j];
                    for(int z = j; z < gameBoard.BOARD_SIZE; z++){
                        if(gameBoard.getBoard()[i][z] == playerNumber){
                            connectSize1++;
                        }
                        else break;
                    }
                    if(connectSize1 < 3){
                        connectSize1 = 0;
                    }
                }
                else if (gameBoard.getBoard()[i][j] != playerNumber) {
                    weightedSum -= weightedTable[i][j];
                    for(int z = j; z < gameBoard.BOARD_SIZE; z++){
                        if(gameBoard.getBoard()[i][z] == playerNumber){
                            connectSize2++;
                        }
                        else break;
                    }
                    if(connectSize2 < 3){
                        connectSize2 = 0;
                    }
                }
            }
            int diff = connectSize1 - connectSize2;
            if(diff < 0) {
                if (connectSize2 == 3) {
                    connectTotal -= 0.05;
                    connectSize1 = 0;
                    connectSize2 = 0;
                }
            }
            else if (connectSize1 == 3) {
                connectTotal += 0.05;
                connectSize1 = 0;
                connectSize2 = 0;
            }

            if (weightedSum > 0){
                connectTotal +=  (weightedSum * (0.001));
            }
            else if (weightedSum < 0){
                connectTotal -= (weightedSum * (0.001));
            }
        }
        return connectTotal;
    }

    /*
     * Searches every row to evaluate board and looks for 3 in a row horizontally.
     * Also calculates every taken position from a weighted table to check board favorability.
     * @param gameBoard is the board to be evaluated
     * @param playerNumber is the player to check if the board favors
     * @return This should return a number between -1.0 and 1.0.
     *         If the board favors playerNumber then the return value should be close to 1.0
     *         If the board favors playerNumber's opponent then the return value should be close to -1.0
     *         If the board favors neither player then the return value should be close to 0.0
     */
    private static double evaluateRow(Board gameBoard, int playerNumber) {
        int connectSize1 = 0;
        int connectSize2 = 0;
        double connectTotal = 0;
        double weightedSum = 0;
        for (int j = 0; j < gameBoard.BOARD_SIZE; j++) {
            for (int i = 0; i < gameBoard.BOARD_SIZE; i++) {
                if (gameBoard.getBoard()[i][j] == 0) {
                    break;
                }
                else if (gameBoard.getBoard()[i][j] == playerNumber) {
                    weightedSum += weightedTable[i][j];
                    int oneSpace = 0;
                        for (int z = j; z < gameBoard.BOARD_SIZE; z++) {
                            if (gameBoard.getBoard()[i][z] == playerNumber) {
                                connectSize1++;
                            } else if (gameBoard.getBoard()[i][z] == 0) {
                                oneSpace++; // allow one open space between a group of 3
                                if(oneSpace == 2){
                                    break;
                                }
                            } else break;
                        }
                    if(connectSize1 < 3){
                        connectSize1 = 0;
                    }
                }
                else if (gameBoard.getBoard()[i][j] != playerNumber) {
                    weightedSum -= weightedTable[i][j];
                    for(int z = j; z < gameBoard.BOARD_SIZE; z++){
                        if(gameBoard.getBoard()[i][z] == playerNumber){
                            connectSize2++;
                        }
                        else break;
                    }
                    if(connectSize2 < 3){
                        connectSize2 = 0;
                    }
                }
            }
            int diff = connectSize1 - connectSize2;
            if(diff < 0) {
                if (connectSize2 == 3) {
                    connectTotal -= 0.05;
                    connectSize1 = 0;
                    connectSize2 = 0;
                }
            }
            else if (connectSize1 == 3) {
                connectTotal += 0.05;
                connectSize1 = 0;
                connectSize2 = 0;
            }

            if (weightedSum > 0){
                connectTotal +=  (weightedSum * (0.001));
            }
            else if (weightedSum < 0){
                connectTotal -= (weightedSum * (0.001));
            }
        }
        return connectTotal;
    }

    /*
     * Applies a heuristic to check how much the current state of the board favors that player
     * @param gameBoard is the board to be evaluated
     * @param playerNumber is the player to check if the board favors
     * @return This should return the largest integer between -1.0 and 1.0. Where 1.0 favors the playerNumber
     */
    public double heuristic(Board gameBoard, int playerNumber) {
        //evaluates the columns
        double col = evaluateColumn(gameBoard, playerNumber);
        //evaluates the rows
        double row = evaluateRow(gameBoard, playerNumber);

        if(col >= row){
            return col;
        }
        else {
            return row;
        }
    }


    private Move getBestMove(ArrayList<Move> moves) {

        double max = -1.0;
        Move bestMove = moves.get(0);

        for (Move cm : moves) {
            if (cm.value > max) {
                max = cm.value;
                bestMove = cm;
            }
        }

        return bestMove;
    }



}
