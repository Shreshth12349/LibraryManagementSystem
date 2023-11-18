import java.util.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
public class Book {
    private int isbn;
    private String title, genre, author, availability;
    private Date borrowDate;
    private Date dueDate;
    private String borrowedBy;
    public static String passString(String s) {
        if (s == null) {
            return "NULL";
        } else {
            return "'" + s + "'";
        }
    }
    public Book(int isbn, String title, String genre, String author){
        this.isbn= isbn;
        this.title  = title;
        this.genre = genre;
        this.author = author;
        this.availability = "yes";
    }
    public static void deleteLibrary(){
        String query = "DROP TABLE Books";
        try {
            DatabaseHandler.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createLibrary(){
        String query = "CREATE TABLE IF NOT EXISTS Books (\n" +
                "isbn INTEGER PRIMARY KEY,\n" +
                "title TEXT NOT NULL,\n" +
                "author TEXT NOT NULL,\n" +
                "genre TEXT NOT NULL,\n" +
                "issuedBy TEXT,\n" +
                "issueDate DATE,\n" +
                "dueDate DATE, \n" +
                "availability TEXT CHECK (availability IN ('yes', 'no'))\n" +
                ");";
        try {
            DatabaseHandler.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        addBook(524936, "To Kill a Mockingbird", "Harper Lee", "Fiction");
        addBook(789415, "1984", "George Orwell", "Dystopian");
        addBook(235678, "Pride and Prejudice", "Jane Austen", "Romance");
        addBook(901234, "The Great Gatsby", "F. Scott Fitzgerald", "Fiction");
        addBook(456789, "Moby-Dick", "Herman Melville", "Adventure");
        addBook(312476, "The Catcher in the Rye", "J.D. Salinger", "Fiction");
        addBook(874563, "War and Peace", "Leo Tolstoy", "Historical Fiction");
        addBook(690124, "The Hobbit", "J.R.R. Tolkien", "Fantasy");
        addBook(432839, "One Hundred Years of Solitude", "Gabriel García Márquez", "Magic Realism");
        addBook(875392, "The Lord of the Rings", "J.R.R. Tolkien", "Fantasy");
    }
    public static void addBook(int isbn, String title, String author, String genre){
        String query = "INSERT INTO Books " +  "(isbn, title ,author, genre, availability"+ ")\n"
                + "VALUES(" + isbn +  ", " + passString(title) + ", " + passString(author) + ", " + passString(genre) + "," + passString("yes") + ");";
        try {
            DatabaseHandler.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateBookISBN(String isbn, int newIsbn) {
        String query = "UPDATE Books SET isbn = " + newIsbn + " WHERE isbn = " + isbn + ";";
        try {
            DatabaseHandler.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateBookInfo(String isbn, String fieldName, String newInfo) {
        try {
            String query1 = String.format("SELECT * Books WHERE isbn = '%s'", isbn);
            DatabaseHandler.executeUpdate(query1);
            String query = String.format("UPDATE Books SET '%s' = '%s' WHERE isbn = '%s'", fieldName, newInfo, isbn);
            try {
                DatabaseHandler.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }catch (SQLException e){
        }
    }
    public static void markBorrowed(int isbn, String issuer) {
        String query1 = String.format("SELECT * FROM Books WHERE availability = 'yes' AND isbn = '%s';", isbn);
        ResultSet resultSet = null;
        try {
            resultSet = Book.getBookByIsbn(isbn);
            if (resultSet.next()) {
                String query2 = "UPDATE Books SET availability = " + passString("no") + ", issuedBy = " +
                        passString(issuer) + ", issueDate = CURRENT_DATE, " +
                        "dueDate = date('now', '+7 days') WHERE isbn = " + isbn + ";";
                try {
                    DatabaseHandler.executeUpdate(query2);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No available books found for ISBN " + isbn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception as needed
            }
        }
    }
    public static void markReturned(int isbn) {
        String query = String.format("UPDATE Books SET availability = %s, issuedBy = NULL, issueDate = NULL WHERE isbn = %s;", passString("yes"), isbn);
        try {
            DatabaseHandler.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static ResultSet searchBooks(String keyword) throws SQLException {
        String query = "SELECT * FROM Books WHERE title LIKE '%" + keyword + "%' OR author LIKE '%" + keyword + "%' OR genre LIKE '%" + keyword + "%';";
        try {
            return DatabaseHandler.executeQuery(query);
        } catch (SQLException e) {
            throw e;
        }
    }
    public static ResultSet getAllBooks() {
        String query = "SELECT * FROM Books;";
        ResultSet resultSet = null;
        try {
            resultSet = DatabaseHandler.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
        return resultSet;
    }
    public static ResultSet viewAvailableBooks() {
        String query = "SELECT * FROM Books WHERE availability = 'yes';";
        ResultSet resultSet = null;
        try {
            resultSet = DatabaseHandler.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public static ResultSet viewUnavailableBooks() {
        String query = "SELECT * FROM Books WHERE availability = 'no';";
        ResultSet resultSet = null;
        try {
            resultSet = DatabaseHandler.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
        return resultSet;
    }
    public static void deleteBook(int isbnToDelete){
        String query = "DELETE FROM Books WHERE isbn = " + isbnToDelete + ";";
        try {
            DatabaseHandler.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static ResultSet getBookByIsbn(int isbn){
        String query = "SELECT * FROM Books WHERE isbn = " + isbn + ";";
        ResultSet resultSet = null;
        try {
            resultSet = DatabaseHandler.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
        return resultSet;
    }
    public static boolean bookExists(int isbn) throws SQLException {
        String query = "SELECT * FROM Books WHERE isbn = " + isbn + ";";
        ResultSet resultSet = null;
        try {
            resultSet = DatabaseHandler.executeQuery(query);
            System.out.println("Executing query: " + query);
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }
}
