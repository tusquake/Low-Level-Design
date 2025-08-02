public class UniversalPlayerCorrect extends MediaPlayerCorrect {
    public void play(AudioFormat audio) {
        System.out.println("Playing any audio: " + audio.getExtension());
    }
}
