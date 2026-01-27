public class Main {
    public static void main(String[] args) {
        MP3Format mp3 = new MP3Format();
        AudioFormat wav = new AudioFormat("wav");

        MediaPlayerCorrect mediaPlayer = new MediaPlayerCorrect();
        mediaPlayer.play(mp3);

        UniversalPlayerCorrect universalPlayer = new UniversalPlayerCorrect();
        universalPlayer.play(wav);
        universalPlayer.play(mp3); // Also works due to inheritance
    }
}