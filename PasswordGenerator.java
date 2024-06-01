import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PasswordGenerator {

    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:,.<>?";

    private static final List<String> passwordHistory = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Generate Password");
            System.out.println("2. View Password History");
            System.out.println("3. Export Password");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    generateAndDisplayPassword(scanner);
                    break;
                case 2:
                    viewPasswordHistory();
                    break;
                case 3:
                    exportPassword();
                    break;
                case 4:
                    System.out.println("Exiting program.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void generateAndDisplayPassword(Scanner scanner) {
        System.out.print("Enter the length of the password: ");
        int length = scanner.nextInt();

        System.out.print("Include lowercase characters? (yes/no): ");
        boolean includeLowercase = scanner.next().equalsIgnoreCase("yes");

        System.out.print("Include uppercase characters? (yes/no): ");
        boolean includeUppercase = scanner.next().equalsIgnoreCase("yes");

        System.out.print("Include digits? (yes/no): ");
        boolean includeDigits = scanner.next().equalsIgnoreCase("yes");

        System.out.print("Include special characters? (yes/no): ");
        boolean includeSpecialChars = scanner.next().equalsIgnoreCase("yes");

        System.out.print("Enter custom characters to include (leave empty for default): ");
        String customChars = scanner.next();

        String password = generatePassword(length, includeLowercase, includeUppercase, includeDigits, includeSpecialChars, customChars);
        passwordHistory.add(password);

        System.out.println("Generated password: " + password);
        System.out.println("Password strength: " + getPasswordStrength(password));
        System.out.println("Entropy: " + calculateEntropy(length, includeLowercase, includeUppercase, includeDigits, includeSpecialChars, customChars));
    }

    private static void viewPasswordHistory() {
        if (passwordHistory.isEmpty()) {
            System.out.println("Password history is empty.");
        } else {
            System.out.println("Password History:");
            for (int i = 0; i < passwordHistory.size(); i++) {
                System.out.println((i + 1) + ". " + passwordHistory.get(i));
            }
        }
    }

    private static void exportPassword() {
        if (passwordHistory.isEmpty()) {
            System.out.println("No password generated yet.");
            return;
        }

        System.out.print("Enter the index of the password to export: ");
        Scanner scanner = new Scanner(System.in);
        int index = scanner.nextInt();

        if (index < 1 || index > passwordHistory.size()) {
            System.out.println("Invalid index.");
            return;
        }

        String password = passwordHistory.get(index - 1);

        // Export to a file
        try {
            FileWriter writer = new FileWriter("exported_password.txt");
            writer.write(password);
            writer.close();
            System.out.println("Password exported to 'exported_password.txt'.");
        } catch (IOException e) {
            System.out.println("Failed to export password to file.");
        }

        // Export to clipboard
        StringSelection selection = new StringSelection(password);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);

        System.out.println("Password copied to clipboard.");
    }

    private static String generatePassword(int length, boolean includeLowercase, boolean includeUppercase, boolean includeDigits, boolean includeSpecialChars, String customChars) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        String charSet = "";
        if (includeLowercase) charSet += LOWERCASE_CHARS;
        if (includeUppercase) charSet += UPPERCASE_CHARS;
        if (includeDigits) charSet += DIGITS;
        if (includeSpecialChars) charSet += SPECIAL_CHARS;
        charSet += customChars;

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charSet.length());
            password.append(charSet.charAt(randomIndex));
        }

        return password.toString();
    }

    private static String getPasswordStrength(String password) {
        int strength = 0;

        if (password.length() >= 8) {
            strength++;
        }

        if (password.matches(".*[a-z].*")) {
            strength++;
        }

        if (password.matches(".*[A-Z].*")) {
            strength++;
        }

        if (password.matches(".*\\d.*")) {
            strength++;
        }

        if (password.matches(".*[!@#$%^&*()-_=+\\[\\]{}|;:,.<>?].*")) {
            strength++;
        }

        if (password.length() >= 12) {
            strength++;
        }

        if (password.length() >= 16) {
            strength++;
        }

        if (password.length() >= 20) {
            strength++;
        }

        if (strength <= 2) {
            return "Weak";
        } else if (strength <= 4) {
            return "Moderate";
        } else {
            return "Strong";
        }
    }

    private static double calculateEntropy(int length, boolean includeLowercase, boolean includeUppercase, boolean includeDigits, boolean includeSpecialChars, String customChars) {
        String charSet = "";
        if (includeLowercase) charSet += LOWERCASE_CHARS;
        if (includeUppercase) charSet += UPPERCASE_CHARS;
        if (includeDigits) charSet += DIGITS;
        if (includeSpecialChars) charSet += SPECIAL_CHARS;
        charSet += customChars;

        int numPossibleCharacters = charSet.length();
        return Math.log(Math.pow(numPossibleCharacters, length)) / Math.log(2);
    }
}
