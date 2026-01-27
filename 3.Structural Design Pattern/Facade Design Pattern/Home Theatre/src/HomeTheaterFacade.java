class HomeTheaterFacade {
    private DVDPlayer dvdPlayer;
    private Projector projector;
    private SoundSystem soundSystem;
    private Lights lights;
    private Screen screen;

    public HomeTheaterFacade(DVDPlayer dvd, Projector projector,
                             SoundSystem sound, Lights lights, Screen screen) {
        this.dvdPlayer = dvd;
        this.projector = projector;
        this.soundSystem = sound;
        this.lights = lights;
        this.screen = screen;
    }

    public void watchMovie(String movie) {
        System.out.println("Getting ready to watch a movie...");

        lights.dim(10);
        screen.down();
        projector.on();
        projector.wideScreenMode();
        projector.setInput("DVD");
        soundSystem.on();
        soundSystem.setSurroundSound();
        soundSystem.setVolume(15);
        dvdPlayer.on();
        dvdPlayer.play(movie);

        System.out.println("Movie is now playing!");
    }

    public void endMovie() {
        System.out.println("Shutting down movie theater...");

        dvdPlayer.stop();
        dvdPlayer.off();
        soundSystem.off();
        projector.off();
        screen.up();
        lights.on();

        System.out.println("Movie theater is shut down!");
    }
}
