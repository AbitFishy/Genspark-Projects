import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Clock;
import java.util.concurrent.TimeUnit;

public class DragonCave {
    public DragonCave() {
        double rng = Math.random();
        goodDragon = rng > .5 ? 1 : 2;
    }

    private void sleep(int sleepFor){
        if (!actuallySleep){
            return;
        }
        try {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (InterruptedException ie) {
            System.out.println("Whelp! the waiting thread was interrupted! Time to die a painful death");
        }
    }

    private final int goodDragon;
    protected Clock clock;
    protected boolean actuallySleep = true;
    private void Intro() {

            System.out.println("You are in a land full of dragons.");
            sleep(2);
            System.out.println("In front of you, you see two caves.");
            sleep(2);
            System.out.println("In one cave, the dragon is friendly");
            sleep(2);
            System.out.println("and will share his treasure with you");
            sleep(2);
            System.out.println("The other dragon is greedy and hungry");
            sleep(2);
            System.out.println("and will eat you on sight.");
            sleep(2);
            System.out.println("Which cave will you go into?");
            sleep(2);

    }

    private void Suspense(){
            System.out.print("You approach the cave");
            sleep(1);
            System.out.print(".");
            sleep(1);
            System.out.print(".");
            sleep(1);
            System.out.println(".");
            sleep(1);
            System.out.print("It is dark and spooky");
            sleep(1);
            System.out.print(".");
            sleep(1);
            System.out.print(".");
            sleep(2);
            System.out.println(".");
            sleep(1);
            System.out.println("A large dragon jumps out in front of you!");
            System.out.println("He opens he jaws and ...!");
            sleep(3);


    }


    private void Hungry(){
        System.out.println("Gobbles you down in one bite!");
    }

    private void Friendly(){
        System.out.println("Welcomes you to his cave and offers you");
        System.out.println("your pick of the treasure!");
    }

    public boolean Play(){
        Intro();
        int choice = 0;
        int attempts = 10;
        BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
        while (attempts-- > 0){
            try{
                System.out.println("(1 or 2)");
                choice = Integer.parseInt(br.readLine());
                if (choice == 1 || choice == 2){
                    attempts = 0;
                }
                else{
                    choice = 0;
                }
            }
            catch (Exception e){
                if (attempts <= 0){
                    Indecision();
                    return false;
                }
            }
        }

        boolean lived;
        if (choice == goodDragon) {
            Suspense();
            Friendly();
            lived = true;

        } else if (choice == 0){
            Indecision();
            lived = false;
        }
        else {
            Suspense();
            Hungry();
            lived = false;
        }
        return lived;
    }

    private void Indecision(){
        System.out.println("Standing there paralysed with indecision,");
        System.out.println("A dragon suddenly appears in front of you");
        System.out.println("and devours you in one bite.");
    }
}
