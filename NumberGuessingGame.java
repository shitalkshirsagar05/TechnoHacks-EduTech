import java.util.Scanner;

public class NumberGuessingGame {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int lowerBound = 1;
        int upperBound = 100;
        int difficulty;
        int maxAttempts = 10; // Maximum number of attempts
        int randomNumber;
        int guess;
        int attempts = 0;

        System.out.println("Welcome to the Number Guessing Game!");

        System.out.println("Enter the number of players: ");
        int numPlayers = scanner.nextInt();

        int[] playerAttempts = new int[numPlayers];
        String[] playerNames = new String[numPlayers];

        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter player " + (i + 1) + "'s name: ");
            playerNames[i] = scanner.next();
        }

        System.out.println("Choose the theme:");
        System.out.println("1. Colors");
        System.out.println("2. Animals");
        System.out.println("3. Shapes");
        System.out.print("Enter your choice: ");
        int themeChoice = scanner.nextInt();

        String themeHint = "";
        switch (themeChoice) {
            case 1:
                themeHint = "Hint: The color of the sky.";
                break;
            case 2:
                themeHint = "Hint: The king of the jungle.";
                break;
            case 3:
                themeHint = "Hint: A shape with three sides.";
                break;
            default:
                themeHint = "Hint: Something you see every day.";
                break;
        }

        System.out.println("Choose the difficulty level:");
        System.out.println("1. Easy (Numbers between 1 and 50)");
        System.out.println("2. Medium (Numbers between 1 and 100)");
        System.out.println("3. Hard (Numbers between 1 and 1000)");
        System.out.print("Enter your choice: ");
        difficulty = scanner.nextInt();

        switch (difficulty) {
            case 1:
                upperBound = 50;
                break;
            case 2:
                upperBound = 100;
                break;
            case 3:
                upperBound = 1000;
                break;
            default:
                System.out.println("Invalid choice. Setting difficulty to Medium.");
                upperBound = 100;
                break;
        }

        // Select a random pattern for number generation
        int patternChoice = (int) (Math.random() * 2); // 0 for Fibonacci, 1 for prime numbers

        // Generate the target number based on the selected pattern
        switch (patternChoice) {
            case 0: // Fibonacci sequence
                randomNumber = generateFibonacciNumber(lowerBound, upperBound);
                break;
            case 1: // Prime numbers
                randomNumber = generatePrimeNumber(lowerBound, upperBound);
                break;
            default:
                randomNumber = (int) (Math.random() * upperBound) + 1; // Default random number generation
                break;
        }

        System.out.println("I have chosen a number between " + lowerBound + " and " + upperBound + ". Players, try to guess it!");
        System.out.println(themeHint);

        int currentPlayer = 0;

        while (attempts < maxAttempts) {
            System.out.println(playerNames[currentPlayer] + "'s turn.");
            System.out.println("Attempts left: " + (maxAttempts - attempts));
            System.out.print("Enter your guess: ");
            guess = scanner.nextInt();
            playerAttempts[currentPlayer]++;
            attempts++;

            if (guess < randomNumber) {
                System.out.println("Too low! Try again.");
            } else if (guess > randomNumber) {
                System.out.println("Too high! Try again.");
            } else {
                System.out.println("Congratulations, " + playerNames[currentPlayer] + "! You guessed the number in " + playerAttempts[currentPlayer] + " attempts.");
                break;
            }

            currentPlayer = (currentPlayer + 1) % numPlayers; // Move to the next player
            Thread.sleep(1000); // Pause for 1 second for visual effect
            System.out.print("\033[H\033[2J"); // Clear the console
            System.out.flush();
        }

        if (attempts == maxAttempts) {
            System.out.println("Sorry, you've all run out of attempts. The number was: " + randomNumber);
        }

        scanner.close();
    }

    // Helper method to generate a Fibonacci number within the given range
    private static int generateFibonacciNumber(int lowerBound, int upperBound) {
        int a = 0, b = 1, c = 0;
        while (c < lowerBound || c > upperBound) {
            c = a + b;
            a = b;
            b = c;
        }
        return c;
    }

    // Helper method to generate a prime number within the given range
    private static int generatePrimeNumber(int lowerBound, int upperBound) {
        for (int i = upperBound; i >= lowerBound; i--) {
            if (isPrime(i)) {
                return i;
            }
        }
        return -1; // No prime number found in the range
    }

    // Helper method to check if a number is prime
    private static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
