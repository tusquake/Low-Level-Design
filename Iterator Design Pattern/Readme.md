# Iterator Design Pattern - Simple Guide

## What is Iterator Pattern?

Iterator pattern provides a way to access elements of a collection **sequentially** without exposing the underlying structure (array, list, tree, etc.).

**Think of it as:** TV remote that lets you change channels without knowing how the TV stores channels internally.

---

## Real-World Analogy: Playlist

**Without Iterator (Direct Access):**
```
You need to know:
- Is it an array?
- Is it a linked list?
- What's the index?
- How to get next song?

songs[0], songs[1], songs[2]... // Exposes array structure
```

**With Iterator (Abstraction):**
```
Just use:
- hasNext() - Are there more songs?
- next() - Play next song

Don't care about internal storage!
```

---

## Problem Without Iterator

```java
// Different collections, different ways to iterate
public class Library {
    
    public void printBooks() {
        // Array - use index
        Book[] arrayBooks = getArrayBooks();
        for (int i = 0; i < arrayBooks.length; i++) {
            System.out.println(arrayBooks[i]);
        }
        
        // List - use iterator
        List<Book> listBooks = getListBooks();
        for (Book book : listBooks) {
            System.out.println(book);
        }
        
        // Tree - complex traversal
        TreeNode root = getTreeBooks();
        printTree(root); // Need special method
    }
}
```

**Problem:** Different iteration logic for each collection type!

---

## Solution With Iterator

```java
// Uniform way to iterate any collection
Iterator<Book> iterator = collection.createIterator();

while (iterator.hasNext()) {
    Book book = iterator.next();
    System.out.println(book);
}
// Same code works for array, list, tree, etc.!
```

---

## Implementation

### 1. Iterator Interface

```java
public interface Iterator<T> {
    boolean hasNext();  // Are there more elements?
    T next();          // Get next element
}
```

### 2. Concrete Iterator

```java
public class BookIterator implements Iterator<Book> {
    private Book[] books;
    private int position = 0;
    
    public BookIterator(Book[] books) {
        this.books = books;
    }
    
    @Override
    public boolean hasNext() {
        return position < books.length;
    }
    
    @Override
    public Book next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Book book = books[position];
        position++;
        return book;
    }
}
```

### 3. Aggregate Interface

```java
public interface BookCollection {
    Iterator<Book> createIterator();
}
```

### 4. Concrete Aggregate

```java
public class Library implements BookCollection {
    private Book[] books;
    private int count = 0;
    
    public Library(int size) {
        books = new Book[size];
    }
    
    public void addBook(Book book) {
        if (count < books.length) {
            books[count] = book;
            count++;
        }
    }
    
    @Override
    public Iterator<Book> createIterator() {
        return new BookIterator(books);
    }
}
```

### 5. Book Class

```java
public class Book {
    private String title;
    private String author;
    
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }
    
    @Override
    public String toString() {
        return "'" + title + "' by " + author;
    }
}
```

### 6. Client Code

```java
public class IteratorDemo {
    public static void main(String[] args) {
        
        // Create library
        Library library = new Library(5);
        
        // Add books
        library.addBook(new Book("1984", "George Orwell"));
        library.addBook(new Book("To Kill a Mockingbird", "Harper Lee"));
        library.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald"));
        
        // Get iterator
        Iterator<Book> iterator = library.createIterator();
        
        // Iterate through books
        System.out.println("Books in library:");
        while (iterator.hasNext()) {
            Book book = iterator.next();
            System.out.println(book);
        }
    }
}
```

**Output:**
```
Books in library:
'1984' by George Orwell
'To Kill a Mockingbird' by Harper Lee
'The Great Gatsby' by F. Scott Fitzgerald
```

---

## Real-World Example: Social Media Feed

```java
// Post class
class Post {
    private String content;
    private String author;
    
    public Post(String author, String content) {
        this.author = author;
        this.content = content;
    }
    
    @Override
    public String toString() {
        return author + ": " + content;
    }
}

// Feed Iterator
class FeedIterator implements Iterator<Post> {
    private List<Post> posts;
    private int position = 0;
    
    public FeedIterator(List<Post> posts) {
        this.posts = posts;
    }
    
    @Override
    public boolean hasNext() {
        return position < posts.size();
    }
    
    @Override
    public Post next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Post post = posts.get(position);
        position++;
        return post;
    }
}

// Social Media Feed
class SocialMediaFeed implements Iterable<Post> {
    private List<Post> posts = new ArrayList<>();
    
    public void addPost(Post post) {
        posts.add(post);
    }
    
    @Override
    public Iterator<Post> iterator() {
        return new FeedIterator(posts);
    }
}

// Usage
public class SocialMediaDemo {
    public static void main(String[] args) {
        SocialMediaFeed feed = new SocialMediaFeed();
        
        feed.addPost(new Post("Alice", "Just finished my morning run!"));
        feed.addPost(new Post("Bob", "Coffee time ☕"));
        feed.addPost(new Post("Charlie", "Working on a new project"));
        
        System.out.println("=== Your Feed ===");
        Iterator<Post> iterator = feed.iterator();
        
        while (iterator.hasNext()) {
            Post post = iterator.next();
            System.out.println(post);
            System.out.println("---");
        }
    }
}
```

**Output:**
```
=== Your Feed ===
Alice: Just finished my morning run!
---
Bob: Coffee time ☕
---
Charlie: Working on a new project
---
```

---

## Java's Built-in Iterator

Java provides `Iterator` interface in `java.util` package:

```java
import java.util.*;

public class JavaIteratorExample {
    public static void main(String[] args) {
        
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        
        // Get iterator
        Iterator<String> iterator = names.iterator();
        
        // Iterate
        while (iterator.hasNext()) {
            String name = iterator.next();
            System.out.println(name);
            
            // Can remove during iteration
            if (name.equals("Bob")) {
                iterator.remove(); // Safe removal
            }
        }
        
        System.out.println("\nAfter removal: " + names);
    }
}
```

**Output:**
```
Alice
Bob
Charlie

After removal: [Alice, Charlie]
```

---

## Enhanced For-Loop (Syntactic Sugar)

Java's enhanced for-loop uses Iterator internally:

```java
List<String> fruits = Arrays.asList("Apple", "Banana", "Orange");

// Enhanced for-loop
for (String fruit : fruits) {
    System.out.println(fruit);
}

// Is equivalent to:
Iterator<String> iterator = fruits.iterator();
while (iterator.hasNext()) {
    String fruit = iterator.next();
    System.out.println(fruit);
}
```

---

## Different Iterator Types

### 1. Forward Iterator (Standard)

```java
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    System.out.println(iterator.next());
}
```

### 2. Bidirectional Iterator (ListIterator)

```java
ListIterator<String> iterator = list.listIterator();

// Forward
while (iterator.hasNext()) {
    System.out.println(iterator.next());
}

// Backward
while (iterator.hasPrevious()) {
    System.out.println(iterator.previous());
}
```

### 3. Filtered Iterator

```java
class FilteredIterator implements Iterator<Integer> {
    private Iterator<Integer> iterator;
    private Integer nextValue;
    
    public FilteredIterator(Iterator<Integer> iterator) {
        this.iterator = iterator;
        advance();
    }
    
    private void advance() {
        nextValue = null;
        while (iterator.hasNext()) {
            Integer value = iterator.next();
            if (value % 2 == 0) { // Only even numbers
                nextValue = value;
                break;
            }
        }
    }
    
    @Override
    public boolean hasNext() {
        return nextValue != null;
    }
    
    @Override
    public Integer next() {
        Integer current = nextValue;
        advance();
        return current;
    }
}

// Usage
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
Iterator<Integer> evenIterator = new FilteredIterator(numbers.iterator());

while (evenIterator.hasNext()) {
    System.out.println(evenIterator.next()); // Only prints 2, 4, 6, 8
}
```

---

## Real-World Example: Playlist Iterator

```java
class Song {
    private String title;
    private String artist;
    
    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }
    
    @Override
    public String toString() {
        return "🎵 " + title + " - " + artist;
    }
}

class Playlist implements Iterable<Song> {
    private List<Song> songs = new ArrayList<>();
    
    public void addSong(Song song) {
        songs.add(song);
    }
    
    @Override
    public Iterator<Song> iterator() {
        return songs.iterator();
    }
    
    // Shuffle iterator
    public Iterator<Song> shuffleIterator() {
        List<Song> shuffled = new ArrayList<>(songs);
        Collections.shuffle(shuffled);
        return shuffled.iterator();
    }
    
    // Reverse iterator
    public Iterator<Song> reverseIterator() {
        return new Iterator<Song>() {
            int position = songs.size() - 1;
            
            @Override
            public boolean hasNext() {
                return position >= 0;
            }
            
            @Override
            public Song next() {
                return songs.get(position--);
            }
        };
    }
}

// Usage
public class PlaylistDemo {
    public static void main(String[] args) {
        Playlist playlist = new Playlist();
        playlist.addSong(new Song("Bohemian Rhapsody", "Queen"));
        playlist.addSong(new Song("Stairway to Heaven", "Led Zeppelin"));
        playlist.addSong(new Song("Hotel California", "Eagles"));
        
        System.out.println("=== Normal Order ===");
        for (Song song : playlist) {
            System.out.println(song);
        }
        
        System.out.println("\n=== Reverse Order ===");
        Iterator<Song> reverse = playlist.reverseIterator();
        while (reverse.hasNext()) {
            System.out.println(reverse.next());
        }
        
        System.out.println("\n=== Shuffle ===");
        Iterator<Song> shuffle = playlist.shuffleIterator();
        while (shuffle.hasNext()) {
            System.out.println(shuffle.next());
        }
    }
}
```

**Output:**
```
=== Normal Order ===
🎵 Bohemian Rhapsody - Queen
🎵 Stairway to Heaven - Led Zeppelin
🎵 Hotel California - Eagles

=== Reverse Order ===
🎵 Hotel California - Eagles
🎵 Stairway to Heaven - Led Zeppelin
🎵 Bohemian Rhapsody - Queen

=== Shuffle ===
🎵 Hotel California - Eagles
🎵 Bohemian Rhapsody - Queen
🎵 Stairway to Heaven - Led Zeppelin
```

---

## Benefits

1. **Uniform Interface:** Same way to iterate different collections
2. **Encapsulation:** Internal structure hidden
3. **Multiple Iterators:** Can have multiple iterators on same collection
4. **Different Traversals:** Forward, backward, filtered, shuffled
5. **Single Responsibility:** Collection focuses on storage, Iterator on traversal

---

## When to Use

✅ **Use Iterator when:**
- Need to traverse collection without exposing internal structure
- Want to provide multiple ways to traverse same collection
- Need to iterate over different collection types uniformly

**Examples:**
- File system traversal
- Tree/graph traversal
- Database result sets
- Social media feeds
- Playlists

---

## Quick Summary

| Concept | Description |
|---------|-------------|
| **Purpose** | Access elements sequentially without exposing structure |
| **Analogy** | TV remote (change channels without knowing storage) |
| **Key Methods** | hasNext(), next() |
| **Benefits** | Uniform interface, encapsulation, multiple traversals |
| **Use Case** | Collections, feeds, playlists, file systems |

---

## Memory Trick

**Iterator = Waiter at buffet**
- You ask: "Is there more food?" (hasNext)
- Waiter gives: "Here's next dish" (next)
- You don't know how kitchen organizes food (encapsulation)

**Remember:** Iterator knows the path, you just follow!