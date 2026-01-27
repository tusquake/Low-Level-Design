public class MusicPlayer {
    public static void main(String[] args) {
        // Create playlist
        Playlist myPlaylist = new Playlist();
        myPlaylist.addSong(new Song("Bohemian Rhapsody", "Queen", 355));
        myPlaylist.addSong(new Song("Hotel California", "Eagles", 391));
        myPlaylist.addSong(new Song("Stairway to Heaven", "Led Zeppelin", 482));

        // Forward iteration
        System.out.println("=== Forward Iteration ===");
        Iterator<Song> iterator = myPlaylist.createIterator();
        while (iterator.hasNext()) {
            Song song = iterator.next();
            System.out.println("Playing: " + song);
        }

        // Reverse iteration
        System.out.println("\n=== Reverse Iteration ===");
        Iterator<Song> reverseIter = new ReverseIterator(myPlaylist);
        while (reverseIter.hasNext()) {
            Song song = reverseIter.next();
            System.out.println("Playing: " + song);
        }

        // Remove elements while iterating
        System.out.println("\n=== Removing Songs ===");
        iterator = myPlaylist.createIterator();
        while (iterator.hasNext()) {
            Song song = iterator.next();
            if (song.getDuration() > 400) {
                System.out.println("Removing long song: " + song);
                iterator.remove();
            }
        }

        System.out.println("Remaining songs: " + myPlaylist.getCount());
    }
}
