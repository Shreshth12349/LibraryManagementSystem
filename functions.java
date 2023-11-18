import org.sqlite.SQLiteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.util.NoSuchElementException;
public class functions {
    public static void viewAllBooks() {
        try {
            System.out.println(formatting.format2DArray(formatting.resultSetTo2DArray(Book.getAllBooks())));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void viewSearchResults() {
        try {
            System.out.println(formatting.format2DArray(formatting.resultSetTo2DArray(Book.viewAvailableBooks())));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void viewAvailableBooks() {
        try {
            System.out.println(formatting.format2DArray(formatting.resultSetTo2DArray(Book.viewAvailableBooks())));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void viewUnavailableBooks() {
        try {
            System.out.println(formatting.format2DArray(formatting.resultSetTo2DArray(Book.viewAvailableBooks())));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateBookInfo () throws SQLException {
        System.out.println("Enter the ISBN of the book you want to update: ");
        String isbn = InputManager.sc.nextLine();
        if(Book.bookExists(Integer.parseInt(isbn))){
            int choice;
            String fieldName = null;
            String newInfo = null;
            boolean isValidChoice = false;
            while (!isValidChoice) {
                System.out.println("Select the field you want to update: ");
                System.out.println("1. Title");
                System.out.println("2. Author");
                System.out.println("3. Genre");
                System.out.print("Enter your choice: ");
                if (InputManager.sc.hasNextInt()) {
                    choice = InputManager.sc.nextInt();
                    InputManager.sc.nextLine(); // Consume the newline character left after reading the integer
                    switch (choice) {
                        case 1:
                            isValidChoice = true;
                            fieldName = "title";
                            System.out.println("Enter the new title:");
                            break;
                        case 2:
                            isValidChoice = true;
                            fieldName = "author";
                            System.out.println("Enter the new author:");
                            break;
                        case 3:
                            isValidChoice = true;
                            fieldName = "genre";
                            System.out.println("Enter the new genre:");
                            break;
                        default:
                            System.out.println("Enter a valid choice.");
                    }
                    newInfo = InputManager.sc.nextLine();
                    Book.updateBookInfo(isbn, fieldName, newInfo);
                    System.out.println("Book successfully updated!");
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                    InputManager.sc.nextLine();
                }
            }
        }else{
            System.out.println("Book with that ISBN doesn't exist");
        }
    }
    public static void addBook() {
        System.out.println("Enter the book's ISBN");
        String isbn = InputManager.sc.nextLine();
        int isbnInt = Integer.parseInt(isbn);
        System.out.println("Enter the book's Title");
        String title = InputManager.sc.nextLine();
        System.out.println("Enter the book's Author");
        String author = InputManager.sc.nextLine();
        System.out.println("Enter the book's Genre");
        String genre = InputManager.sc.nextLine();
        Book.addBook(isbnInt, title, author, genre);
        System.out.println("Book added.");
    }
    public static void removeBook() {
        System.out.println("Enter the ISBN of the book you want to delete:");
        int isbn = InputManager.sc.nextInt();
        Book.deleteBook(isbn);
        System.out.println("Book deleted!");
    }
    public static void issueBook() {
        System.out.println("Enter the issuer's name: ");
        String name = InputManager.sc.next();
        System.out.println("Enter the ISBN of book you want to issue: ");
        int isbn = InputManager.sc.nextInt();
        Book.markBorrowed(isbn, name);
        System.out.println("Book issued");
    }
    public static void returnBook() {
        System.out.println("Enter the ISBN of the book you want to return: ");
        int isbn = InputManager.sc.nextInt();
        try {
            if (Book.bookExists(isbn)) {
                String query = "UPDATE Books SET availability = " + "'yes'" +
                        ", issuedBy = NULL, issueDate = NULL, dueDate = NULL WHERE isbn = " + isbn + ";";
                try {
                    DatabaseHandler.executeUpdate(query);
                    System.out.println("Book returned successfully.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No issued book found for ISBN " + isbn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
    }
    public static void showUnavailableBooks(){
        try{
            ResultSet resultSet = Book.viewUnavailableBooks();
            System.out.println(formatting.format2DArray(formatting.resultSetTo2DArray(resultSet)));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void showAvailableBooks(){
        try{
            ResultSet resultSet = Book.viewAvailableBooks();
            System.out.println(formatting.format2DArray(formatting.resultSetTo2DArray(resultSet)));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void findBooks() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the keyword you want to search:");
        String keyword = sc.next();
        ResultSet resultSet = null;
        try {
            resultSet = Book.searchBooks(keyword);
            if (resultSet.next()) {
                String result = formatting.format2DArray(formatting.resultSetTo2DArray(resultSet));
                System.out.println(result);
            } else {
                System.out.println("No books found for keyword " + keyword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
