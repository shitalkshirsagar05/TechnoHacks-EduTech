import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

public class ToDoList {
    private static class ToDoItem {
        private String task;
        private int priority;
        private Date dueDate;
        private String category;
        private String status;
        private String notes;
        private ArrayList<String> attachments;

        public ToDoItem(String task, int priority, Date dueDate, String category, String status, String notes) {
            this.task = task;
            this.priority = priority;
            this.dueDate = dueDate;
            this.category = category;
            this.status = status;
            this.notes = notes;
            this.attachments = new ArrayList<>();
        }

        public String getTask() {
            return task;
        }

        public int getPriority() {
            return priority;
        }

        public Date getDueDate() {
            return dueDate;
        }

        public String getCategory() {
            return category;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public ArrayList<String> getAttachments() {
            return attachments;
        }

        public void addAttachment(String attachment) {
            attachments.add(attachment);
        }

        @Override
        public String toString() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Priority: ").append(priority).append(", Task: ").append(task)
                    .append(", Due Date: ").append(dateFormat.format(dueDate)).append(", Category: ")
                    .append(category).append(", Status: ").append(status).append("\nNotes: ").append(notes)
                    .append("\nAttachments: ");
            for (String attachment : attachments) {
                stringBuilder.append(attachment).append(", ");
            }
            return stringBuilder.toString();
        }
    }

    private static class Theme {
        private String name;
        private String backgroundColor;
        private String textColor;

        public Theme(String name, String backgroundColor, String textColor) {
            this.name = name;
            this.backgroundColor = backgroundColor;
            this.textColor = textColor;
        }

        public String getName() {
            return name;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public String getTextColor() {
            return textColor;
        }
    }

    private static ArrayList<Theme> themes = new ArrayList<>();

    static {
        themes.add(new Theme("Default", "White", "Black"));
        themes.add(new Theme("Dark Mode", "Black", "White"));
        themes.add(new Theme("Light Mode", "White", "Black"));
    }

    private static void applyTheme(Theme theme) {
        System.out.println("Applying theme: " + theme.getName());
        // Code to apply the theme to the console interface
        // For example, you could change the background and text colors here
    }

    public static void main(String[] args) {
        ArrayList<ToDoItem> toDoList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int choice;
        Theme currentTheme = themes.get(0); // Default theme

        do {
            System.out.println("1. Add item");
            System.out.println("2. Remove item");
            System.out.println("3. View items");
            System.out.println("4. Share list");
            System.out.println("5. Update task status");
            System.out.println("6. Add notes or attachments");
            System.out.println("7. Change theme");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter item to add: ");
                    String addItem = scanner.nextLine();
                    System.out.print("Enter priority (1: high, 2: medium, 3: low): ");
                    int priority = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter due date (dd-MM-yyyy): ");
                    String dueDateString = scanner.nextLine();
                    Date dueDate = null;
                    try {
                        dueDate = new SimpleDateFormat("dd-MM-yyyy").parse(dueDateString);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please use dd-MM-yyyy.");
                        break;
                    }
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    toDoList.add(new ToDoItem(addItem, priority, dueDate, category, "Not started", ""));
                    System.out.println("Item added to the list.");
                    break;
                case 2:
                    System.out.print("Enter index of item to remove: ");
                    int removeIndex = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (removeIndex >= 0 && removeIndex < toDoList.size()) {
                        toDoList.remove(removeIndex);
                        System.out.println("Item removed from the list.");
                    } else {
                        System.out.println("Invalid index.");
                    }
                    break;
                case 3:
                    System.out.println("To-Do List:");
                    toDoList.sort(Comparator.comparingInt(ToDoItem::getPriority));
                    for (int i = 0; i < toDoList.size(); i++) {
                        System.out.println((i + 1) + ". " + toDoList.get(i));
                    }
                    break;
                case 4:
                    System.out.print("Enter email or username to share with: ");
                    String shareWithEmail = scanner.nextLine();
                    System.out.println("Shared with: " + shareWithEmail);
                    break;
                case 5:
                    System.out.print("Enter index of item to update status: ");
                    int updateIndex = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (updateIndex >= 0 && updateIndex < toDoList.size()) {
                        System.out.print("Enter new status (Not started, In progress, Completed): ");
                        String newStatus = scanner.nextLine();
                        toDoList.get(updateIndex).setStatus(newStatus);
                        System.out.println("Status updated.");
                    } else {
                        System.out.println("Invalid index.");
                    }
                    break;
                case 6:
                    System.out.print("Enter index of item to add notes or attachments: ");
                    int addNotesIndex = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (addNotesIndex >= 0 && addNotesIndex < toDoList.size()) {
                        System.out.print("Enter notes: ");
                        String notes = scanner.nextLine();
                        toDoList.get(addNotesIndex).setNotes(notes);
                        System.out.print("Enter number of attachments: ");
                        int numAttachments = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        for (int i = 0; i < numAttachments; i++) {
                            System.out.print("Enter attachment " + (i + 1) + ": ");
                            String attachment = scanner.nextLine();
                            toDoList.get(addNotesIndex).addAttachment(attachment);
                        }
                        System.out.println("Notes and attachments added.");
                    } else {
                        System.out.println("Invalid index.");
                    }
                    break;
                case 7:
                    System.out.println("Available Themes:");
                    for (int i = 0; i < themes.size(); i++) {
                        System.out.println((i + 1) + ". " + themes.get(i).getName());
                    }
                    System.out.print("Select theme: ");
                    int themeChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (themeChoice > 0 && themeChoice <= themes.size()) {
                        currentTheme = themes.get(themeChoice - 1);
                        applyTheme(currentTheme);
                    } else {
                        System.out.println("Invalid theme choice.");
                    }
                    break;
                case 8:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8);

        scanner.close();
    }
}
