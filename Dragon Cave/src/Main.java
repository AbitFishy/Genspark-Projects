public class Main {

    public static void main(String[] args) {
        DragonCave dc = new DragonCave();
        boolean lived = dc.Play();
        String text = lived ? "You lived!" : "You died!";
        System.out.println(text);
    }
}

