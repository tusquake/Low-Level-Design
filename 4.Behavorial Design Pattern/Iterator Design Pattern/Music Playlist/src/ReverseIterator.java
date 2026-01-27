public class ReverseIterator implements Iterator<Song> {
    private Playlist playlist;
    private int currentPosition;

    public ReverseIterator(Playlist playlist) {
        this.playlist = playlist;
        this.currentPosition = playlist.getCount() - 1;
    }

    @Override
    public boolean hasNext() {
        return currentPosition >= 0;
    }

    @Override
    public Song next() {
        if (!hasNext()) {
            throw new RuntimeException("No more songs");
        }
        return playlist.getSongAt(currentPosition--);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove not supported");
    }
}
