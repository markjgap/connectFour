/**
 * Abstract class for a Connect Four player.
 */
public abstract class Player {

    public int playerNumber;

    abstract public int chooseMove(Board gameBoard);
    abstract public void setPlayerNumber(int number);
}
