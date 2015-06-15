
/**
 * Implements a Connect Four player that chooses a valid move at random
 */

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HumanPlayer extends Player {


    public HumanPlayer() {
        return;
    }

    public void setPlayerNumber(int number) {
        this.playerNumber = number;
    }


    public int chooseMove(Board gameBoard) {

        while (true) {

            // Prompt the user to choose a move
            System.out.println("Enter the column number of your move:");
            Scanner inputScanner = new Scanner(System.in);
            int move;
            try {
                move = inputScanner.nextInt();
            } catch (InputMismatchException e) {
                move = -1;
            }
            this.clearInput();

            if (gameBoard.move(this.playerNumber, move)) {
                return move;
            } else {
                System.out.println("Invalid Move!!!");
            }
        }
    }

    private void clearInput() {
        try {
            if (System.in.available()>0) {
                while (System.in.read() != '\n') ;
            }
        } catch (IOException e) {

        }
    }
}
