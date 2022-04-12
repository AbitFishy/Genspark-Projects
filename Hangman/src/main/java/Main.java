import org.apache.commons.lang3.StringUtils;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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
    //int numWrongGuesses = 0;
    boolean win = false;
    String secretWord;
    TreeSet<Character> checkWord;
    final int numGuesses = 9;
    protected static final Path savefile = Path.of( "savefile.txt");

    LinkedHashSet<Character> guessedLetters = new LinkedHashSet<>();
    StringBuilder missedLetters = new StringBuilder();


    boolean play(){
        if (firstPlay){
            System.out.println(title);
            askForName();
        }
        else{
            //numWrongGuesses = 0;
            win = false;
            guessedLetters= new LinkedHashSet<>();
            missedLetters = new StringBuilder();
        }
        if (!checkAndRecoverGame()) {
            secretWord = generateGuess(0);
        }
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
        deleteSaveGame();
        System.out.println(checkAndRecordHighScore(missedLetters.length()));
        return userInput("Play Again? (yes or no)", 1) == 'y';
    }

    private String loseScreen() {
        return "Sorry! The secret word is \"" + secretWord +"\"! You Lose!";
    }

    private String winScreen() {
        return "Yes! The secret word is \"" + secretWord +"\"! You Win!";
    }



    boolean loop(){
        //System.out.print(drawGallows(numWrongGuesses));
        System.out.print(drawGallows(missedLetters.length()));
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
            //if (++numWrongGuesses >= numGuesses){
            if (missedLetters.length() >= numGuesses){
                win = false;
                return false;
            }
        }


        saveGame();
        return true;
    }

    protected void saveGame(){   //do not trim missedLetters()
        String game = drawGallows(missedLetters.length()).trim() + "\n%\n" + missedLetters() + "\n%\n" +
                StringUtils.join( guessedLetters,"," ) + "\n%\n" + secretWord;
        try {
            Files.writeString(savefile, game);
        } catch (IOException e) {
            System.err.println("Could Not Save Game to " + savefile);
            e.printStackTrace();
        }
    }
    protected void deleteSaveGame(){
        try{
            Files.deleteIfExists(savefile);
        }
        catch (IOException e){
            System.err.println("Could Not Delete Save File " + savefile);
            e.printStackTrace();
        }
    }
    protected boolean checkAndRecoverGame(){

        try {
            if (Files.exists(savefile) && Files.size(savefile)  != 0){

                return deserialize(Files.readString(savefile));
            }
        } catch (IOException e) {
            System.err.println("Savefile Found But Not Readable");
        }
        return false;
    }

    protected boolean deserialize(String game){
        try {
            var parts = game.split("\n%\n");
            missedLetters = new StringBuilder();
            if (parts[1].indexOf(':') + 2 <= parts[1].length()) {
                missedLetters.append(parts[1],parts[1].indexOf(':') + 2, parts[1].length());
            }
            if (parts[2].length() > 0) {
                var parts2 = parts[2].split(",");
                var ignore = Arrays.stream(parts2).reduce("", (u, s) -> {
                    if (s.length() == 1) {
                        guessedLetters.add(s.charAt(0));
                    }
                    return s;
                });
            }
            secretWord = parts[3];
            checkWord = Arrays.stream(secretWord.split("")).map(s -> s.charAt(0)).collect(Collectors.toCollection(TreeSet::new));
            return true;
        }
        catch (Exception ioobe){
            System.err.println("Save file error");
            return false;
        }
    }
    String playerName = "";
    protected static final String highScoreDelim = "'";
    protected void askForName(){
        System.out.println("What is your name?");

        try{
            var line = br.readLine();
            line = line.replace(highScoreDelim, "").replace("\n", "").replace(highScoreDelim,"'");
            if (line.length() > 20) {
                line = line.substring(0,20);
            }
            playerName = line.trim();

        } catch (IOException e) {
            e.printStackTrace();
            playerName= "NULL";
        }
        System.out.println("Welcome " + playerName+ "!");
    }

    static protected final Path highScoreFile = Path.of("highscore.txt");

    protected String checkAndRecordHighScore(int score){
        StringBuilder hssb = new StringBuilder();
        try{
            TreeMap<String,Integer> hstable = new TreeMap<>();
            if (!Files.exists(highScoreFile)) {
                hssb.append("You Have The First Score Of ").append(score).append("!");
            } else {
                var is = Files.lines(highScoreFile);
                var ignore = is.map( f -> {
                           var s = f.split(highScoreDelim);
                            hstable.put(s[0],Integer.valueOf(s[1]));
                            return 0;
                        }).reduce((x,y) -> 0);
                TreeSet<Integer> scores = new TreeSet<>(hstable.values());
                var highScore = scores.first();
                HashSet<String> highScores = new HashSet<>();
                hstable.entrySet().stream().peek(e -> {
                    if (Objects.equals(e.getValue(), highScore)){
                        highScores.add(e.getKey());
                    }
                }).reduce( (a,b) -> a);

                if (score < highScore) {
                    if (highScores.contains(playerName)) {
                        hssb.append("You Beat Your Old Record Of ").append(highScore).append("!");
                        if (highScores.size() > 1){
                            hssb.append(" Tied With ");

                        }
                    } else {
                        hssb.append("You Beat The High Score Of ").append(highScore).append("! Set By ");


                    }
                    formatTiedPlayers(highScores, hssb);
                }
                else if (score == highScore) {
                    if (highScores.contains(playerName)){
                        hssb.append("You Tied Your Previous Record Of ").append(highScore).append("!");
                        if (highScores.size() >1) {
                                hssb.append(" Along With ");
                        }
                    }
                    else {
                        hssb.append("You Tied The High Score Of ").append(highScore).append("! Set By ");

                    }
                    formatTiedPlayers(highScores, hssb);
                }
            }
            hstable.put(playerName,score);
            StringBuilder fout = new StringBuilder();
            var ignore = hstable.entrySet().stream().map((e) -> fout.append(e.getKey()).append(highScoreDelim).append(e.getValue()).append("\n")).reduce( (a,b) -> a);
            Files.writeString(highScoreFile, fout.toString());
        } catch (IOException e) {
            hssb.append("Cannot Read/Write HighScores");
        }
        return hssb.toString();
    }

    private void formatTiedPlayers(HashSet<String> highScores, StringBuilder hssb) {
        highScores.remove(playerName);
        if (highScores.size() > 0) {
            String lastHigh = (String) highScores.toArray()[0];
            highScores.remove(lastHigh);
            hssb.append(StringUtils.join(highScores, ", "));
            if (highScores.size() > 0) {
                hssb.append(" and ");
            }
            hssb.append(lastHigh).append("!");
        }
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
        //vvv ignore output vvv
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

/*
class UserInputError extends IOException{
    UserInputError(String message){
        super(message);
    }
}*/
