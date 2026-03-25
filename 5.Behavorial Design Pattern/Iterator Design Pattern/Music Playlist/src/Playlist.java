public class Playlist implements Aggregate<Song> {
    private Song[] songs;
    private int count;
    private static final int MAX_SONGS = 100;

    public Playlist() {
        songs = new Song[MAX_SONGS];
        count = 0;
    }

    public void addSong(Song song) {
        if (count < MAX_SONGS) {
            songs[count++] = song;
        }
    }

    public int getCount() { return count; }
    public Song getSongAt(int index) {
        return (index >= 0 && index < count) ? songs[index] : null;
    }

    @Override
    public Iterator<Song> createIterator() {
        return new PlaylistIterator();
    }

    // Inner class for concrete iterator
    private class PlaylistIterator implements Iterator<Song> {
        private int currentPosition = 0;

        @Override
        public boolean hasNext() {
            return currentPosition < count;
        }

        @Override
        public Song next() {
            if (!hasNext()) {
                throw new RuntimeException("No more songs");
            }
            return songs[currentPosition++];
        }

        @Override
        public void remove() {
            if (currentPosition <= 0) {
                throw new RuntimeException("Cannot remove before next()");
            }

            int removeIndex = currentPosition - 1;
            for (int i = removeIndex; i < count - 1; i++) {
                songs[i] = songs[i + 1];
            }
            count--;
            currentPosition--;
        }
    }
}
