import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Set;

public class Main {

    private static final Set<String> CHOICES = Set.of("y", "yes");

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
        String choice;
        do {
            Guess program = new Guess();
            program.loop();
            System.out.println("Would you like to play again? (y or n)");

            try {
                choice = br.readLine();
            }
            catch (Exception e){
                return;
            }
        } while (CHOICES.contains(choice));
    }


}
