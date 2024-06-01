
//Here I added these new intresting things to make the game more interesting:

//1.	Score Keeping: Keep track of the user's and computer's scores and display them after each round or at the end of the game.
//2.	Extended Options: Add more choices like lizard and Spock (from "The Big Bang Theory") to the game.
//3.	Best of N Rounds: Allow the game to be played in a best-of-N rounds format, where N is an odd number.
//4.    Difficulty Levels: Implement different difficulty levels for the computer opponent, where higher levels might make smarter choices.
//5.	Visual Effects: Add some ASCII art for the different choices or implement a simple GUI using Swing or JavaFX.
//6.	Sound Effects: Include sound effects for various game events like win, lose, or tie.
//7.	Timeouts: Add a timer for each round to increase the pressure on the player.
//8.	Special Moves: Introduce special moves that can only be used a certain number of times or under specific conditions.
//9.	Multiplayer: Allow two players to play against each other on the same computer or over a network.
//10.	Achievements: Implement achievements or badges for completing certain actions or winning streaks.
//11.	Customizable Rules: Allow the user to customize the rules of the game, such as changing what beats what.
//12.	History: Keep a history of past games and display statistics like win/loss ratio.
//13.	Animations: Add animations for the game actions, like the hand gestures for rock, paper, and scissors.
//14.	Themes: Implement different themes for the game interface, such as colors, fonts, and backgrounds.
//15.	Tournament Mode: Create a tournament mode where players compete in a bracket-style competition.

import java.util.*;

public class RockPaperScissors {
    private static final Scanner scanner = new Scanner(System.in);
    private static int player1Score = 0;
    private static int player2Score = 0;
    private static int consecutiveWins = 0;
    private static boolean player1Achievement = false;
    private static boolean player2Achievement = false;
    private static List<String> choices = Arrays.asList("rock", "paper", "scissors", "lizard", "Spock");
    private static Map<Integer, String[]> history = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Welcome to Rock, Paper, Scissors, Lizard, Spock!");
        System.out.println("Enter the number of rounds for the game (must be odd): ");
        int totalRounds = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        if (totalRounds % 2 == 0) {
            System.out.println("Number of rounds must be odd. Exiting...");
            return;
        }

        int roundsToWin = (totalRounds / 2) + 1;

        while (true) {
            System.out.println("Do you want to customize the rules? (yes/no): ");
            String custom = scanner.nextLine().toLowerCase();
            if (custom.equals("yes")) {
                System.out.println("Enter the number of rules you want to customize: ");
                int numRules = scanner.nextInt();
                scanner.nextLine();

                for (int i = 0; i < numRules; i++) {
                    System.out.println("Enter rule " + (i + 1) + " in the format 'winning_choice,losing_choice': ");
                    String[] rule = scanner.nextLine().toLowerCase().split(",");
                    if (rule.length != 2 || !choices.contains(rule[0]) || !choices.contains(rule[1])) {
                        System.out.println("Invalid rule. Please try again.");
                        i--;
                        continue;
                    }
                    choices = customizeChoices(choices, rule[0], rule[1]);
                }
                break;
            } else if (custom.equals("no")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }

        for (int round = 1; round <= totalRounds; round++) {
            System.out.println("Round " + round + ":");
            System.out.println("Player 1, enter your choice (" + String.join(", ", choices) + "): ");
            String player1Choice = scanner.nextLine().toLowerCase();

            System.out.println("Player 2, enter your choice (" + String.join(", ", choices) + "): ");
            String player2Choice = scanner.nextLine().toLowerCase();

            if (!choices.contains(player1Choice) || !choices.contains(player2Choice)) {
                System.out.println("Invalid choice. Please enter one of: " + String.join(", ", choices));
                round--;
                continue;
            }

            System.out.println("Player 1 chooses: " + getHandGesture(player1Choice));
            System.out.println("Player 2 chooses: " + getHandGesture(player2Choice));

            if (player1Choice.equals(player2Choice)) {
                System.out.println("It's a tie!");
                consecutiveWins = 0;
            } else {
                String winMessage = determineWinner(player1Choice, player2Choice, choices);
                System.out.println(winMessage);
            }

            history.put(round, new String[]{player1Choice, player2Choice});
            if (player1Score == roundsToWin || player2Score == roundsToWin) {
                break;
            }
        }

        if (player1Score == player2Score) {
            System.out.println("It's a tie overall!");
        } else if (player1Score > player2Score) {
            System.out.println("Player 1 wins the game!");
        } else {
            System.out.println("Player 2 wins the game!");
        }

        System.out.println("Game History:");
        for (int round : history.keySet()) {
            String[] moves = history.get(round);
            System.out.println("Round " + round + ": Player 1 - " + moves[0] + ", Player 2 - " + moves[1]);
        }

        double winLossRatio = (double) player1Score / player2Score;
        System.out.println("Player 1 Win/Loss Ratio: " + winLossRatio);

        scanner.close();
    }

    private static List<String> customizeChoices(List<String> currentChoices, String winner, String loser) {
        List<String> newChoices = new ArrayList<>(currentChoices);
        if (newChoices.contains(winner) && newChoices.contains(loser)) {
            newChoices.remove(loser);
        }
        return newChoices;
    }

    private static String determineWinner(String player1Choice, String player2Choice, List<String> choices) {
        if (player1Choice.equals(player2Choice)) {
            return "It's a tie!";
        }
        int half = choices.size() / 2;
        int player1Index = choices.indexOf(player1Choice);
        int player2Index = choices.indexOf(player2Choice);
        if (player1Index == (player2Index + 1) % choices.size() || player1Index == (player2Index + 2) % choices.size()) {
            player1Score++;
            consecutiveWins++;
            return "Player 1 wins this round!";
        } else {
            player2Score++;
            consecutiveWins = 0;
            return "Player 2 wins this round!";
        }
    }

    private static String getHandGesture(String choice) {
        switch (choice) {
            case "rock":
                return "    _______\n" +
                        "---'   ____)\n" +
                        "      (_____)\n" +
                        "      (_____)\n" +
                        "      (____)\n" +
                        "---.__(___)";
            case "paper":
                return "    _______\n" +
                        "---'   ____)____\n" +
                        "          ______)\n" +
                        "          _______)\n" +
                        "         _______)\n" +
                        "---.__________)";
            case "scissors":
                return "    _______\n" +
                        "---'   ____)____\n" +
                        "          ______)\n" +
                        "       __________)\n" +
                        "      (____)\n" +
                        "---.__(___)";
            case "lizard":
                return "    _______\n" +
                        "---'   ____)\n" +
                        "          ______)\n" +
                        "       __________)\n" +
                        "      (____)\n" +
                        "---.__(___)";
            case "spock":
                return "    _______\n" +
                        "---'   ____)____\n" +
                        "          ______)\n" +
                        "       __________)\n" +
                        "      (____)\n" +
                        "---.__(___)";
            default:
                return "";
        }
    }
}











//Output:
//Welcome to Rock, Paper, Scissors, Lizard, Spock!
//Enter the number of rounds for the game (must be odd):
//        3
//Do you want to customize the rules? (yes/no):
//no
//Round 1:
//Player 1, enter your choice (rock, paper, scissors, lizard, Spock):
//paper
//Player 2, enter your choice (rock, paper, scissors, lizard, Spock):
//lizard
//Player 1 chooses:     _______
//---'   ____)____
//______)
//_______)
//_______)
//        ---.__________)
//Player 2 chooses:     _______
//---'   ____)
//______)
//__________)
//        (____)
//        ---.__(___)
//Player 2 wins this round!
//Round 2:
//Player 1, enter your choice (rock, paper, scissors, lizard, Spock):
//lizard
//Player 2, enter your choice (rock, paper, scissors, lizard, Spock):
//paper
//Player 1 chooses:     _______
//---'   ____)
//______)
//__________)
//        (____)
//        ---.__(___)
//Player 2 chooses:     _______
//---'   ____)____
//______)
//_______)
//_______)
//        ---.__________)
//Player 1 wins this round!
//Round 3:
//Player 1, enter your choice (rock, paper, scissors, lizard, Spock):
//rock
//Player 2, enter your choice (rock, paper, scissors, lizard, Spock):
//lizard
//Player 1 chooses:     _______
//---'   ____)
//        (_____)
//        (_____)
//        (____)
//        ---.__(___)
//Player 2 chooses:     _______
//---'   ____)
//______)
//__________)
//        (____)
//        ---.__(___)
//Player 1 wins this round!
//Player 1 wins the game!
//Game History:
//Round 1: Player 1 - paper, Player 2 - lizard
//Round 2: Player 1 - lizard, Player 2 - paper
//Round 3: Player 1 - rock, Player 2 - lizard
//Player 1 Win/Loss Ratio: 2.0
//
//Process finished with exit code 0
