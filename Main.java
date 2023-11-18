public class Main {
    public static void main(String[] args) {
        DatabaseHandler db = new DatabaseHandler();
        Book.deleteLibrary();
        Book.createLibrary();
        UI.Interface();
    }
}
