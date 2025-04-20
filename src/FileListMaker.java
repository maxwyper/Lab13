import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class FileListMaker {
    private static List<String> itemList = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static String currentFileName = null;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            displayMenu();
            String choice = scanner.nextLine().toUpperCase();

            try {
                switch (choice) {
                    case "A" -> addItem();
                    case "D" -> deleteItem();
                    case "I" -> insertItem();
                    case "M" -> moveItem();
                    case "V" -> viewList();
                    case "C" -> clearList();
                    case "O" -> openFile();
                    case "S" -> saveFile();
                    case "Q" -> running = quitProgram();
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("A - Add item");
        System.out.println("D - Delete item");
        System.out.println("I - Insert item");
        System.out.println("M - Move item");
        System.out.println("V - View list");
        System.out.println("C - Clear list");
        System.out.println("O - Open list from disk");
        System.out.println("S - Save list to disk");
        System.out.println("Q - Quit");
        System.out.print("Choose an option: ");
    }

    private static void addItem() {
        System.out.print("Enter item to add: ");
        itemList.add(scanner.nextLine());
        needsToBeSaved = true;
    }

    private static void deleteItem() {
        viewList();
        System.out.print("Enter index to delete: ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index >= 0 && index < itemList.size()) {
            itemList.remove(index);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void insertItem() {
        System.out.print("Enter index to insert at: ");
        int index = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter item to insert: ");
        String item = scanner.nextLine();
        if (index >= 0 && index <= itemList.size()) {
            itemList.add(index, item);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void moveItem() {
        viewList();
        System.out.print("Enter index of item to move: ");
        int from = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new index: ");
        int to = Integer.parseInt(scanner.nextLine());

        if (from >= 0 && from < itemList.size() && to >= 0 && to <= itemList.size()) {
            String item = itemList.remove(from);
            itemList.add(to, item);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid indices.");
        }
    }

    private static void viewList() {
        System.out.println("\nCurrent List:");
        for (int i = 0; i < itemList.size(); i++) {
            System.out.println(i + ": " + itemList.get(i));
        }
    }

    private static void clearList() {
        itemList.clear();
        needsToBeSaved = true;
        System.out.println("List cleared.");
    }

    private static void openFile() throws IOException {
        if (needsToBeSaved) {
            System.out.print("Unsaved changes exist. Save current list before loading new one? (Y/N): ");
            String choice = scanner.nextLine().toUpperCase();
            if (choice.equals("Y")) {
                saveFile();
            }
        }

        System.out.print("Enter filename to open (without .txt): ");
        String filename = scanner.nextLine() + ".txt";
        Path path = Paths.get(filename);
        if (Files.exists(path)) {
            itemList = Files.readAllLines(path);
            currentFileName = filename;
            needsToBeSaved = false;
            System.out.println("File loaded successfully.");
        } else {
            System.out.println("File not found.");
        }
    }

    private static void saveFile() throws IOException {
        if (currentFileName == null) {
            System.out.print("Enter a filename to save as (without .txt): ");
            currentFileName = scanner.nextLine() + ".txt";
        }

        Files.write(Paths.get(currentFileName), itemList);
        needsToBeSaved = false;
        System.out.println("List saved to " + currentFileName);
    }

    private static boolean quitProgram() throws IOException {
        if (needsToBeSaved) {
            System.out.print("You have unsaved changes. Save before quitting? (Y/N): ");
            String choice = scanner.nextLine().toUpperCase();
            if (choice.equals("Y")) {
                saveFile();
            }
        }
        System.out.println("Goodbye!");
        return false;
    }
}
