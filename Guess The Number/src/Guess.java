import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Guess {
    public Guess()
    {
    }
    final private int secretNum = (int)(20 * Math.random()) + 1;
    private boolean correctlyGuessed = false;
    final private int maxAttempts = 6;
    private int attemptsRemaining = maxAttempts;
    final private  BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
    private String playerName = "";
    final private String INVALID_NAME = "InvalidName";

    private int processGuess(int guess){
        if (guess > 20 || guess < 1){
            return 3;
        }
        return Integer.compare(guess, secretNum);
    }

    private void intro(){
        System.out.println("Hello! What is your name?");
        int numReadAttempts = 5;
        do {
            try {
                playerName = br.readLine();
                break;
            } catch (IOException ioe) {
                if (numReadAttempts-- > 0) {
                    System.out.println("Please Enter a Valid Name");
                } else {
                    System.out.println("Too Many Invalid Attempts");
                    playerName = INVALID_NAME;
                }
            }
        }while (numReadAttempts > 0);
        if (Objects.equals(playerName, INVALID_NAME)) {
            attemptsRemaining = 0;
        } else {
            System.out.println("Well, " + playerName + ", I am thinking of a number between 1 and 20.");
        }
    }

    public void loop(){
        intro();
        while (!correctlyGuessed && attemptsRemaining-- > 0){
            System.out.println("Take a guess.");
            int guess = 0;
            try {
                guess = Integer.parseInt(br.readLine());
            }
            catch (Exception e){
                System.out.println("That was not a valid number");
                continue;
            }


            switch(processGuess(guess)){
            case 1:
                System.out.println("Your guess is too high");
                break;
            case -1:
                 System.out.println("You guess is too low");
                 break;
            case 0:
                 correctlyGuessed = true;
                 break;
            default:
                 System.out.println("Your guess is outside the range of 1 and 20");

            }
        }
        if (correctlyGuessed){
            System.out.println("Good job " + playerName + "! You guessed my number in " +
                    (maxAttempts - attemptsRemaining) + " guesses!");
        }
        else {
            System.out.println("Sorry " + playerName + ". You did not guess my number in 6 guesses.");
        }
    }
}
