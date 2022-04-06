import java.io.*;
import java.sql.DataTruncation;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Hangman hm = new Hangman();
        playRecur(hm);
    }
    public static void playRecur(Hangman hm){
        if (hm.play()){
            playRecur(hm);
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
    TreeSet<Character> checkWord;
    final int numGuesses = 9;

    LinkedHashSet<Character> guessedLetters = new LinkedHashSet<>();
    StringBuilder missedLetters = new StringBuilder();


    boolean play(){
        if (firstPlay){
            System.out.println(title);
        }
        else{
            numWrongGuesses = 0;
            win = false;
            guessedLetters= new LinkedHashSet<>();
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
        if (userInput("Play Again? (yes or no)",1) == 'y')
        {
            return true;
        }
        return false;
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
            guessedLetters.add(in);

            TreeSet<Character> cagl = new TreeSet<>(guessedLetters);
            if (cagl.equals(checkWord)){
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
        return userInput(message, mode, 1000);
    }
    char userInput(String message, int mode, int attempts) {
        if (attempts-- <= 0) {
            System.out.println("User has entered an invalid input 100 times, Shutting Down");
            System.exit(1);
            return ' ';
        }

        System.out.println(message);
        try {
            String input = br.readLine();
            if (mode == 0) {
                if (input.length() > 1) {
                    System.out.println("Hold your horses, too many letters.");
                } else if (input.length() == 0) {
                    //continue;
                } else if (Character.toUpperCase(input.charAt(0)) >= 'A' &&
                        Character.toUpperCase(input.charAt(0)) <= 'Z') {
                    return Character.toUpperCase(input.charAt(0));
                } else {
                    System.out.println("That was not a letter.");
                }
            } else if (mode == 1) {
                if (input.contains("y")) {
                    return 'y';
                } else {
                    return 'n';
                }
            } else {
                System.out.println("Bad input mode: " + mode);
            }
        } catch (IOException ioe) {
            System.out.println("IO Exception, Shutting Down");
            System.exit(1);
        }
        return userInput(message, mode, attempts);
    }

    boolean checkIfLetterInWord(char c){
        return secretWord.contains(String.valueOf(c));
    }

    String showUnGuessedWord(){
        StringBuilder sb = new StringBuilder();

        secretWord.chars().mapToObj( c -> guessedLetters.toString().contains(String.valueOf((char)c)) ? sb.append((char)c) : sb.append('_')).collect(Collectors.toList());
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
        return "Missed Letters: " + missedLetters.toString();
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

        var wordChoices = wordlist.stream().filter(w -> w.length() == length || (length == 0)).collect(Collectors.toList());

        if (wordChoices.size() != 0){
            found = true;
        }

        if (found) {

            int rand = (int) (Math.random() * wordChoices.size());
            result = wordChoices.get(rand);


            checkWord = Arrays.stream(result.split("")).map(s -> s.charAt(0)).collect(Collectors.toCollection(TreeSet::new));
        }
        return result;
    }

    ArrayList<String> wordlist = new ArrayList<>();

    /**
     * @param filename Filename of word list to load, if
     *                 error, wordlist will have only "cat"
     */
    void readWordFile(String filename){
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(filename));

            readRecurse(bfr);

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

    void readRecurse(BufferedReader bfr) throws IOException {
        String line;
        if (bfr.ready() && (line = bfr.readLine()) != null){
            wordlist.add( line.toUpperCase());
            readRecurse(bfr);
        }
    }

}

class UserInputError extends IOException{
    UserInputError(String message){
        super(message);
    }
}