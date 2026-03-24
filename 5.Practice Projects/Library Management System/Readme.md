# Simple Library Management System

## Simple Analogy

**Library:**
- **Book:** The physical item with a title and unique ID (ISBN).
- **Member:** A person who can borrow books.
- **Library System:** The "Librarian" who checks if a book is available and keeps track of who has what.
- **Reservation:** A "Waitlist" for when the book you want is currently out.

It's like a specialized inventory system where items are not "sold" but "rented" for a limited time.

---

## Implementation

### 1. Book and Member (Entity Classes)

```java
public class Book {
    private String id;
    private String title;
    private boolean isBorrowed;
    // ...
}

public class Member {
    private String id;
    private String name;
    private List<Book> borrowedBooks;
    // ...
}
```

### 2. Library (The Manager)

```java
public class Library {
    private Map<String, Book> books;
    private Map<String, Member> members;

    public void issueBook(String bookId, String memberId) {
        // Logic: Check availability -> Mark as borrowed -> Add to member's list
    }

    public void returnBook(String bookId) {
        // Logic: Mark as available -> Remove from member's list
    }
}
```

---

## Demo

The demo simulates adding books, registering members, and issuing/returning books while handling "Book Already Issued" scenarios.

```java
Library myLibrary = new Library();
myLibrary.addBook("B001", "Design Patterns");
myLibrary.registerMember("M101", "Alice");

myLibrary.issueBook("B001", "M101");
myLibrary.displayStatus();
```

---

## Output

```
--- Book Issue ---
Successfully issued 'Design Patterns' to Alice.

--- Book Return ---
Successfully returned 'Design Patterns'.

--- Library Status ---
Total Books: 5
Available: 5
Borrowed: 0
```

---

## Key Benefits

- **Availability Tracking**: Instantly know how many copies of a book are free.
- **Member Accountability**: Track exactly what every member has borrowed to prevent loss.
- **Searchability**: Quickly find a book by its unique ID or title.

---

## Real-World Uses

- **Public Libraries**: Community resource management.
- **Office Equipment Tracking**: Laptops, monitors, and cables.
- **Inventory Check-out Systems**: Warehouse tools and shared machinery.

---

## Quick Summary

| Component | Role |
|-----------|------|
| Book | Represents the individual item |
| Member | Represents the user of the system |
| Library | Core logic for issuing and returning |
