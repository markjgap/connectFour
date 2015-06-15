/**
 * Main class to run a single game of Connect Four between two opponents.
 */
public class Main {

    public static void main(String[] args) {

        Player p1 = new MyPlayer();
        Player p2 = new RandomPlayer();

        Game gameState = new Game(p1, p2);
        int winner = gameState.startGame(true);
    }
}
