/**
 * Class that runs a Connect Four game between two opponents.
 */
public class Game {

    private Board gameBoard;
    private Player p1;
    private Player p2;

    /**
     * Initializes a game object with two concrete instances of the Player class that will compete against each other
     */
    public Game(Player p1, Player p2) {
        this.p1 = p1;
        p1.setPlayerNumber(1);
        this.p2 = p2;
        p2.setPlayerNumber(2);

    }

    /**
     * Starts the game. This method can be called multiple times on a single game object, and it will start a new game
     * each time.
     *
     * @param printBoard    If this is true then the board will be printed to System.out after each player's move
     * @return              0 if the game ended in a tie
     *                      1 if Player 1 won
     *                      2 if Player 2 won
     */
    public int startGame(boolean printBoard) {

        // Initialize a new board for the game
        this.gameBoard = new Board();

        // Boolean to track which players turn it is
        boolean player1Turn = true;

        Player currentPlayer;
        Player winner = null;

        // Each iteration of this while loop represents one turn
        while (true) {

            // Set currentPlayer to the player who will move on this turn
            if (player1Turn) {
                currentPlayer = this.p1;
            } else {
                currentPlayer = this.p2;
            }

            // Print the board to System.out
            if (printBoard) {
                if (currentPlayer == this.p1) {
                    System.out.println("Player 1 (X) Move :");
                    System.out.println();
                    System.out.println();
                } else {
                    System.out.println("Player 2 (O) Move :");
                    System.out.println();
                    System.out.println();
                }
                this.gameBoard.printBoard();
                System.out.println();
            }

            // Pass a copy of the board to currentPlayer and have it choose a move
            int move = currentPlayer.chooseMove(new Board(this.gameBoard));

            // If currentPlayer chose an invalid move then it automatically loses
            if (!this.gameBoard.move(currentPlayer.playerNumber, move)) {
                // Player made an invalid move and automatically loses
                winner = (currentPlayer == this.p1 ? this.p2 : this.p1);
                System.out.println("Player " + currentPlayer.playerNumber + " made an invalid move and forfeits.");
                break;
            }

            // Check to see if currentPlayer's move ended the game
            int winningPlayerNum = this.gameBoard.checkIfGameOver(move);
            if (winningPlayerNum != -1) {
                if (winningPlayerNum == 0) {
                    winner = null;
                } else if (winningPlayerNum == this.p1.playerNumber) {
                    winner = this.p1;
                } else if (winningPlayerNum == this.p2.playerNumber) {
                    winner = this.p2;
                }

                break;
            }

            // Now it's the other player's turn
            player1Turn = !player1Turn;
        }

        // Print the final board
        if (printBoard) {
            this.gameBoard.printBoard();
        }

        // Report the outcome of the game
        if (winner == this.p1) {
            System.out.println("Player 1 (X) Won!");
            return 1;
        } else if (winner == this.p2){
            System.out.println("Player 2 (O) Won!");
            return 2;
        } else {
            System.out.println("Tie Game!");
            return 0;
        }
    }

}
