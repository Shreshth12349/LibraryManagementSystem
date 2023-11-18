import java.util.*;
import java.sql.SQLException;
public class UI {
    public static void Interface() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to our library");
        System.out.println("Select the operation you want to perform");

        while (true) {
            System.out.println();
            System.out.println("1. View all books");
            System.out.println("2. Search books");
            System.out.println("3. Update book information");
            System.out.println("4. Add book");
            System.out.println("5. Delete book");
            System.out.println("6. Issue Book");
            System.out.println("7. Return book");
            System.out.println("8. View unavailable books");
            System.out.println("9. View available books");
            System.out.println("0. Quit");
            int input;
            while (true) {
                try {
                    input = sc.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a valid choice.");
                    sc.nextLine();
                }
            }
            switch (input) {
                case 0:
                    System.out.println("Thanks for using our library!");
                    return;
                case 1:
                    functions.viewAllBooks();
                    break;
                case 2:
                    functions.findBooks();
                    break;
                case 3:
                    try {
                        functions.updateBookInfo();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    functions.addBook();
                    break;
                case 5:
                    functions.removeBook();
                    break;
                case 6:
                    functions.issueBook();
                    break;
                case 7:
                    functions.returnBook();
                    break;
                case 8:
                    functions.showUnavailableBooks();
                    break;
                case 9:
                    functions.showAvailableBooks();
                    break;
                default:
                    System.out.println("Please enter a valid choice.");
            }
        }
    }
}
