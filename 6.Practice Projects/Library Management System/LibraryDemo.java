import java.util.*;

class Book {
    private String bookId;
    private String title;
    private boolean isBorrowed;

    public Book(String bookId, String title) {
        this.bookId = bookId;
        this.title = title;
        this.isBorrowed = false;
    }

    public void setBorrowed(boolean borrowed) {
        this.isBorrowed = borrowed;
    }

    public boolean isBorrowed() { return isBorrowed; }
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", bookId, title, isBorrowed ? "Borrowed" : "Available");
    }
}

class Member {
    private String memberId;
    private String name;
    private List<Book> borrowedBooks;

    public Member(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    public void addBorrowedBook(Book book) {
        borrowedBooks.add(book);
    }

    public void removeBorrowedBook(Book book) {
        borrowedBooks.remove(book);
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }

    @Override
    public String toString() {
        return String.format("[%s] %s (Borrowed: %d books)", memberId, name, borrowedBooks.size());
    }
}

class Library {
    private Map<String, Book> books = new HashMap<>();
    private Map<String, Member> members = new HashMap<>();

    public void addBook(Book book) {
        books.put(book.getBookId(), book);
    }

    public void registerMember(Member member) {
        members.put(member.getMemberId(), member);
    }

    public void issueBook(String bookId, String memberId) {
        Book book = books.get(bookId);
        Member member = members.get(memberId);

        if (book == null || member == null) {
            System.out.println("Error: Book / Member not found!");
            return;
        }

        if (book.isBorrowed()) {
            System.out.println("Error: Book '" + book.getTitle() + "' is already borrowed!");
            return;
        }

        book.setBorrowed(true);
        member.addBorrowedBook(book);
        System.out.println("SUCCESS: '" + book.getTitle() + "' issued to " + member.getName() + ".");
    }

    public void returnBook(String bookId, String memberId) {
        Book book = books.get(bookId);
        Member member = members.get(memberId);

        if (book == null || member == null) {
            System.out.println("Error: Invalid return operation!");
            return;
        }

        if (member.getBorrowedBooks().contains(book)) {
            book.setBorrowed(false);
            member.removeBorrowedBook(book);
            System.out.println("SUCCESS: '" + book.getTitle() + "' returned by " + member.getName() + ".");
        } else {
            System.out.println("Error: Member " + member.getName() + " doesn't have this book!");
        }
    }

    public void displayLibraryStatus() {
        System.out.println("\n=== Library Inventory ===");
        books.values().forEach(System.out::println);
        System.out.println("========================\n");
    }
}

public class LibraryDemo {
    public static void main(String[] args) {
        System.out.println("=== SIMPLE LIBRARY MANAGEMENT SYSTEM ===\n");

        Library library = new Library();

        // Add some books
        library.addBook(new Book("B001", "Design Patterns (GoF)"));
        library.addBook(new Book("B002", "Effective Java"));
        library.addBook(new Book("B003", "Clean Code"));
        library.addBook(new Book("B004", "The Pragmatic Programmer"));

        // Register members
        library.registerMember(new Member("M101", "Alice"));
        library.registerMember(new Member("M102", "Bob"));

        // Issue books
        library.issueBook("B001", "M101");
        library.issueBook("B002", "M101");
        library.issueBook("B003", "M102");

        // Try to issue already borrowed book
        library.issueBook("B001", "M102");

        // Display status
        library.displayLibraryStatus();

        // Return a book
        library.returnBook("B001", "M101");

        // Final status
        library.displayLibraryStatus();
    }
}
