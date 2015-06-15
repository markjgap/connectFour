/**
 * Implements a Connect Four player that chooses a valid move at random
 */

import java.util.Random;

public class RandomPlayer extends Player {

    private Random random;
    public RandomPlayer() {
        this.random = new Random();
        return;
    }

    public void setPlayerNumber(int number) {
        this.playerNumber = number;
    }

    public int chooseMove(Board gameBoard) {

        while (true) {
            int move = random.nextInt(7);

            if (gameBoard.move(this.playerNumber, move)) {
                return move;
            }
        }
    }
}
