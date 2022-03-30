import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Hangman hm = new Hangman();
        while (hm.play()){
            ///*empty*/
        }


    }
}


class Hangman {
    Hangman() {
        readWordFile("25 common nouns.txt");
    }
    Hangman(String fileName){
        readWordFile(fileName);
    }
    boolean firstPlay = true;
    int numWrongGuesses = 0;
    boolean win = false;
    String secretWord;
    char[] checkWord;
    final int numGuesses = 9;

    StringBuilder guessedLetters = new StringBuilder();
    StringBuilder missedLetters = new StringBuilder();


    boolean play(){
        if (firstPlay){
            System.out.println(title);
        }
        else{
            numWrongGuesses = 0;
            win = false;
            guessedLetters= new StringBuilder();
            missedLetters = new StringBuilder();
        }
        secretWord=  generateGuess(0);
        firstPlay = false;

        while (loop()){
            //empty
        }
        if (win){
            System.out.println(winScreen());
        }
        else {
            System.out.println(loseScreen());
        }
        return userInput("Play Again? (yes or no)",1) == 'y';
    }

    private String loseScreen() {
        return "Sorry! The secret word is \"" + secretWord +"\"! You Lose!";
    }

    private String winScreen() {
        return "Yes! The secret word is \"" + secretWord +"\"! You Win!";
    }



    boolean loop(){
        System.out.print(drawGallows(numWrongGuesses));
        System.out.println(missedLetters());
        System.out.println(showUnGuessedWord());
        char in = userInput("Guess a Letter:", 0);
        if (checkIfLetterInWord(in)){
            if (!guessedLetters.toString().contains(String.valueOf(in))) {
                guessedLetters.append(in);
            }

            char [] cagl = guessedLetters.toString().toCharArray();
            Arrays.sort(cagl);
            if (Arrays.compare(cagl, checkWord) == 0){
                win = true;
                return false;
            }
        }
        else{
            missedLetters.append(in);
            if (++numWrongGuesses >= numGuesses){
                win = false;
                return false;
            }
        }
        return true;
    }

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    char userInput(String message, int mode){
        int attempts =100;
        while (attempts-- >= 0) {
            System.out.println(message);
            try {
                String input = br.readLine();
                if (mode == 0) {
                    if (input.length() > 1) {
                        System.out.println("Hold your horses, too many letters.");
                    } else if (input.length() == 0){
                        continue;
                    }
                    else if (Character.toUpperCase(input.charAt(0)) >= 'A' &&
                            Character.toUpperCase(input.charAt(0)) <= 'Z') {
                        return Character.toUpperCase(input.charAt(0));
                    } else {
                        System.out.println("That was not a letter.");
                    }
                } else if (mode == 1) {
                    if (input.contains( "y")){
                        return 'y';
                    }
                    else {
                        return 'n';
                    }
                }
                else {
                    System.out.println("Bad input mode: " + mode);
                }
            }
            catch (IOException ioe) {
                System.out.println("IO Exception, Shutting Down");
                System.exit(1);
            }
        }
        System.out.println("User has entered an invalid input 100 times, Shutting Down");
        System.exit(1);
        //throw new UserInputError("User has entered an invalid input 100 times, Shutting Down");
        return 'a';
    }
    boolean checkIfLetterInWord(char c){
        return secretWord.contains(String.valueOf(c));
    }

    String showUnGuessedWord(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < secretWord.length(); i++){
            sb.append(guessedLetters.toString().contains(String.valueOf(secretWord.charAt(i))) ?secretWord.charAt(i) : "_");
        }
        return sb.toString();
    }


    String title =  " ///----------------------------------------------------|\n" +
                    "|//|``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,|\n" +
                    "|//|``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,|\n" +
                    "|//|``,,##,,``##``,,``###`,,``,###,,``,##`,##########`,,|\n" +
                    "|//|``,,##,,``##``,,`##,##,,```##`##``,##`,##`,,``,,``,,|\n" +
                    "|//|``,,########``,,#######,``,##`,##`,##`,##`,,#####`,,|\n" +
                    "|//|``,,##,,``##``,##`,,``##``,##`,,##,##`,##`,,``,##`,,|\n" +
                    "|//|``,,##,,``##``##``,,``,##`,##`,,``###`,##########`,,|\n" +
                    "|//|``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,|\n" +
                    "|//|``,,##,,``,,##,,``,###,,``,###,,``,##`,###,,``,##`,,|\n" +
                    "|//|``,,#`#,``,#`#,,``##,##,``,##`##``,##`,##`##``,##`,,|\n" +
                    "|//|``,,#`,#``#,`#,,`#######``,##`,##`,##`,##`,##`,##`,,|\n" +
                    "|//|``,,#`,,##,,`#,,##,,``,##`,##`,,##,##`,##`,,##,##`,,|\n" +
                    "|//|``,,#`,,``,,`#,##`,,``,,##,##`,,``###`,##`,,``###`,,|\n" +
                    "|//|``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,|\n" +
                    "|//|``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,``,,|\n" +
                    "|//|----------------------------------------------------|\n" +
                    "|///////////////////////////////////////////////////////\n" +
                    "|//////////////////////////////////////////////////////\n" +
                    "------------------------------------------------------";


    String missedLetters(){
        return "Missed Letters: " + missedLetters;
    }

    /**
     * @param wrongGuesses How much of the person Hanged to draw
     * @return complete gallows and specified amount of Hanged in one String
     */
    String drawGallows(int wrongGuesses){
        int guessCount = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(" +---+\n");
        if (wrongGuesses > guessCount++){
            sb.append(" |");
        }
        else {
            sb.append("  ");
        }
        sb.append("   |\n");

        if (wrongGuesses > guessCount++){
            sb.append(" O");
        }
        else {
            sb.append("  ");
        }
        sb.append("   |\n");

//        if (wrongGuesses > guessCount++){
//            sb.append(" T");
//        }
//        else{
//            sb.append("  ");
//        }
//        sb.append("   |\n");

        if (wrongGuesses > guessCount++){
            sb.append("/");
        }
        else{
            sb.append(" ");
        }
        if (wrongGuesses > guessCount++){
            sb.append("T");
        }
        else sb.append(" ");

        sb.append(wrongGuesses > guessCount++ ? "\\" : " ");
        sb.append("  |\n");

        sb.append(wrongGuesses > guessCount++ ? " ^" : "  ");
        sb.append("   |\n");
        sb.append(wrongGuesses > guessCount++ ? "/ ": "  ");
        sb.append(wrongGuesses > guessCount++ ? "\\": " ");
        sb.append("  |\n     |\n=======\n");


        return sb.toString();
    }

    /**
     * @param length desired length of random word
     *               0 for any length
     * @return random word of length or "" if no words of length found
     */
    String generateGuess(int length) {
        String result = "";
        boolean found = false;
        int startIndex = 0;
        int endIndex = wordlist.size();
        for (int i = 0; i < wordlist.size(); i++) {
            if (wordlist.get(i).length() == length && !found) {
                found = true;
                startIndex = i;
            } else if (found) {
                if (wordlist.get(i).length() > length) {
                    endIndex = i;
                    break;
                }
            }
        }
        if (!found && length == 0){
            found = true;
        }

        if (found) {
            var subset = wordlist.subList(startIndex, endIndex);
            int rand = (int) (Math.random() * subset.size());
            result = subset.get(rand);
        }
        StringBuilder cwb = new StringBuilder();
        for (int i = 0; i < result.length(); i++){
            if (!cwb.toString().contains( String.valueOf(result.charAt(i))) ){
                cwb.append( result.charAt(i));
            }
        }
        checkWord = cwb.toString().toCharArray();
        Arrays.sort(checkWord);

        return result;
    }

    ArrayList<String> wordlist = new ArrayList<>();

    /**
     * @param filename Filename of word list to load, if
     *                 error, wordlist will have only "cat"
     */
    void readWordFile(String filename){
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            while (br.ready()) {
                wordlist.add( br.readLine().toUpperCase());
            }

        } catch(IOException fnfe){
            System.out.println("Problem reading word list");
            wordlist.add("CAT");
        }
        wordlist.sort((String x, String y) -> {
            if (x.length() == y.length()){
                return 0;
            }
            else{
                return x.length() > y.length() ? 1 : -1;
            }
        });
    }

}

class UserInputError extends IOException{
    UserInputError(String message){
        super(message);
    }
}