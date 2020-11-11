import java.io.Serializable;

public class MorraInfo implements Serializable {

    boolean p1;
    boolean p2;
    boolean disOut;
    int p1Points, p2Points;
    int p1Plays, p2Plays; // 0 to 5
    int p1Guess, p2Guess; // 0 to 5

    boolean have2Players;
    boolean p1Wins, p2Wins;

    boolean p1PlayAgain;
    boolean p2PlayAgain;

    int totalRounds = 3;
    public static final long serialVersionUID = 100;

    MorraInfo() {
        p1Points = 0;
        p2Points = 0;
        p1Plays = -1;
        p2Plays = -1;
        p1Guess = -1;
        p2Guess = -1;
        have2Players = false;
        p1Wins = false;
        p2Wins = false;
        p1PlayAgain = false;
        p2PlayAgain = false;
        disOut = false;
//        int TotalRounds = 3; //todo : change to user entered value

    }

}