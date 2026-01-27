public class FacadePatternDemo {
    public static void main(String[] args) {
        // Create subsystem objects
        DVDPlayer dvd = new DVDPlayer();
        Projector projector = new Projector();
        SoundSystem sound = new SoundSystem();
        Lights lights = new Lights();
        Screen screen = new Screen();

        // Create facade
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(
                dvd, projector, sound, lights, screen);

        // Use simple interface
        homeTheater.watchMovie("Inception");
        System.out.println("\n--- Intermission ---\n");
        homeTheater.endMovie();
    }
}