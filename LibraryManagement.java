
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class LibraryManagement {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<String> books = new ArrayList<>();
    static ArrayList<String> issuedBooks = new ArrayList<>();
    static ArrayList<String> issuedTo = new ArrayList<>();

    // Main Method
    public static void main(String[] args) {

        loadBooks();

        int choice = 0;

        do {
            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Search Book");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. Delete Book");
            System.out.println("7. View Issued Books");
            System.out.println("8. Library Statistics");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");

          if (!sc.hasNextInt()) {

    if (!sc.hasNextLine()) {
        break;
    }

    System.out.println("Please enter a valid number.");
    sc.nextLine();
    continue;
}
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addBook();
                    break;

                case 2:
                    viewBooks();
                    break;

                case 3:
                    searchBook();
                    break;

                case 4:
                    issueBook();
                    break;

                case 5:
                    returnBook();
                    break;

                case 6:
                    deleteBook();
                    break;

                case 7:
                    viewIssuedBooks();
                    break;

                case 8:
                    libraryStatistics();
                    break;

                case 9:
                    saveBooks();
                    System.out.println("Thank you for using Library Management System!");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }

        } while (choice != 9);

        sc.close();
    }

    // Library Statistics
    static void libraryStatistics() {

        int totalBooks = books.size();
        int issuedCount = issuedBooks.size();
        int availableCount = totalBooks - issuedCount;

        System.out.println("\n===== LIBRARY STATISTICS =====");
        System.out.println("Total Books     : " + totalBooks);
        System.out.println("Available Books : " + availableCount);
        System.out.println("Issued Books    : " + issuedCount);
    }

    // Load Books from File
    static void loadBooks() {

        File file = new File("libraryData.txt");

        if (!file.exists()) {
            System.out.println("No previous data found. Starting new library.");
            return;
        }

        try {
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|", 3);

                if (parts.length >= 2) {

                    String status = parts[0];
                    String bookName = parts[1];

                    books.add(bookName);

                    if (status.equals("ISSUED")) {

                        issuedBooks.add(bookName);

                        String studentName = parts.length == 3
                                ? parts[2]
                                : "Unknown";

                        issuedTo.add(studentName);
                    }
                }
            }

            fileScanner.close();

            System.out.println("Library data loaded successfully!");

        } catch (Exception e) {
            System.out.println("Error loading library data!");
        }
    }

    // Save Books to File
    static void saveBooks() {

        try {
            PrintWriter writer = new PrintWriter("libraryData.txt");

            for (String book : books) {

                if (issuedBooks.contains(book)) {

                    int index = issuedBooks.indexOf(book);
                    String studentName = issuedTo.get(index);

                    writer.println("ISSUED|" + book + "|" + studentName);

                } else {

                    writer.println("AVAILABLE|" + book);
                }
            }

            writer.close();

            System.out.println("Library data saved successfully!");

        } catch (Exception e) {
            System.out.println("Error saving library data!");
        }
    }

    // View Books
    static void viewBooks() {

        if (books.isEmpty()) {
            System.out.println("No books available in library.");
            return;
        }

        System.out.println("\n===== LIBRARY BOOKS =====");

        for (int i = 0; i < books.size(); i++) {

            String status = issuedBooks.contains(books.get(i))
                    ? "Issued"
                    : "Available";

            System.out.println(
                    (i + 1) + ". " + books.get(i) + " [" + status + "]");
        }

        System.out.println("Total Books: " + books.size());
    }

    // Search Book
    static void searchBook() {

        System.out.print("Enter book name to search: ");
        String searchName = sc.nextLine().trim();

        if (searchName.isEmpty()) {
            System.out.println("Search name cannot be empty!");
            return;
        }

        boolean found = false;

        for (String book : books) {

            if (book.toLowerCase().contains(searchName.toLowerCase())) {

                String status = issuedBooks.contains(book)
                        ? "Issued"
                        : "Available";

                System.out.println("Book Found: " + book);
                System.out.println("Status: " + status);

                found = true;
            }
        }

        if (!found) {
            System.out.println("Book not found!");
        }
    }

    // Issue Book
    static void issueBook() {

        if (books.isEmpty()) {
            System.out.println("No books available in library!");
            return;
        }

        System.out.print("Enter book name to issue: ");
        String bookName = sc.nextLine().trim();

        if (!books.contains(bookName)) {
            System.out.println("Book not found!");
            return;
        }

        if (issuedBooks.contains(bookName)) {
            System.out.println("This book is already issued!");
            return;
        }

        System.out.print("Enter student name: ");
        String studentName = sc.nextLine().trim();

        if (studentName.isEmpty()) {
            System.out.println("Student name cannot be empty!");
            return;
        }

        issuedBooks.add(bookName);
        issuedTo.add(studentName);

        saveBooks();

        System.out.println("Book issued successfully to " + studentName + "!");
    }

    // Return Book
    static void returnBook() {

        if (issuedBooks.isEmpty()) {
            System.out.println("No books are currently issued!");
            return;
        }

        System.out.print("Enter book name to return: ");
        String bookName = sc.nextLine().trim();

        if (!issuedBooks.contains(bookName)) {
            System.out.println("This book was not issued!");
            return;
        }

        int index = issuedBooks.indexOf(bookName);
        String studentName = issuedTo.get(index);

        issuedBooks.remove(index);
        issuedTo.remove(index);

        saveBooks();

        System.out.println("Book returned successfully!");
        System.out.println("Returned by student: " + studentName);
    }

    // Add Book
    static void addBook() {

        System.out.print("Enter book name: ");
        String bookName = sc.nextLine().trim();

        if (bookName.isEmpty()) {
            System.out.println("Book name cannot be empty!");
            return;
        }

        if (books.contains(bookName)) {
            System.out.println("Book already exists!");
            return;
        }

        books.add(bookName);

        saveBooks();

        System.out.println("Book added successfully!");
    }

    // View Issued Books
    static void viewIssuedBooks() {

        if (issuedBooks.isEmpty()) {
            System.out.println("No books are currently issued!");
            return;
        }

        System.out.println("\n===== ISSUED BOOKS =====");

        for (int i = 0; i < issuedBooks.size(); i++) {

            System.out.println((i + 1) + ". " + issuedBooks.get(i));
            System.out.println("   Issued To: " + issuedTo.get(i));
        }

        System.out.println("Total Issued Books: " + issuedBooks.size());
    }

    // Delete Book
    static void deleteBook() {

        if (books.isEmpty()) {
            System.out.println("No books available to delete!");
            return;
        }

        System.out.print("Enter book name to delete: ");
        String bookName = sc.nextLine().trim();

        if (!books.contains(bookName)) {
            System.out.println("Book not found!");
            return;
        }

        if (issuedBooks.contains(bookName)) {
            System.out.println("Return the book before deleting it!");
            return;
        }

        books.remove(bookName);

        saveBooks();

        System.out.println("Book deleted successfully!");
    }
}
