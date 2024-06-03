import java.util.*;

public class QuizGame {
    private static final String[] QUESTIONS = {
            "What is the capital of France?",
            "Which planet is known as the Red Planet?",
            "Who wrote the play 'Romeo and Juliet'?",
            "What is the largest mammal in the world?",
            "What is the tallest mountain in the world?",
    };

    private static final String[][] OPTIONS = {
            {"A. London", "B. Paris", "C. Rome", "D. Berlin"},
            {"A. Venus", "B. Mars", "C. Jupiter", "D. Saturn"},
            {"A. William Shakespeare", "B. Charles Dickens", "C. Jane Austen", "D. Mark Twain"},
            {"A. Elephant", "B. Blue Whale", "C. Giraffe", "D. Hippopotamus"},
            {"A. Mount Kilimanjaro", "B. Mount Everest", "C. K2", "D. Mount Fuji"},
    };

    private static final int[] ANSWERS = {1, 1, 0, 1, 1};
    private static final int QUESTION_TIME_LIMIT_SECONDS = 10;
    private static final int MAX_SCORE_MULTIPLIER = 3;
    private static final int CORRECT_ANSWER_SCORE = 10;
    private static final int LIFELINES = 2;

    private static final String[] DAILY_CHALLENGE_QUESTIONS = {
            "What is the largest continent in the world?",
            "Which planet is closest to the sun?",
            "Who painted the Mona Lisa?",
            "What is the largest organ in the human body?",
            "What is the capital of Japan?",
    };

    private static final String[][] DAILY_CHALLENGE_OPTIONS = {
            {"A. Asia", "B. Africa", "C. Europe", "D. North America"},
            {"A. Mercury", "B. Venus", "C. Earth", "D. Mars"},
            {"A. Leonardo da Vinci", "B. Vincent van Gogh", "C. Pablo Picasso", "D. Michelangelo"},
            {"A. Heart", "B. Skin", "C. Liver", "D. Brain"},
            {"A. Kyoto", "B. Osaka", "C. Tokyo", "D. Hiroshima"},
    };

    private static final int[] DAILY_CHALLENGE_ANSWERS = {0, 0, 0, 1, 2};

    private static final String[] ACHIEVEMENTS = {
            "First Try: Get the first answer correct in your first attempt.",
            "Quiz Master: Score 100% in a quiz.",
            "Lucky Guess: Get a correct answer with less than 3 seconds remaining.",
            "Consistent: Answer 5 consecutive questions correctly.",
            "Explorer: Play the game for 10 consecutive days."
    };

    private static final int[] ACHIEVEMENT_SCORES = {50, 100, 75, 50, 100};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int score = 0;
        int consecutiveCorrectAnswers = 0;
        int lifelinesRemaining = LIFELINES;

        // Create a list of players
        List<Player> players = new ArrayList<>();

        // Get number of players
        System.out.print("Enter number of players: ");
        int numPlayers = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Create players and customize avatars
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter name for Player " + (i + 1) + ": ");
            String name = scanner.nextLine();
            System.out.print("Enter avatar for Player " + (i + 1) + ": ");
            String avatar = scanner.nextLine();
            players.add(new Player(name, avatar));
        }

        // Shuffle questions
        List<Integer> questionIndexes = new ArrayList<>();
        for (int i = 0; i < QUESTIONS.length; i++) {
            questionIndexes.add(i);
        }
        Collections.shuffle(questionIndexes);

        for (int i = 0; i < questionIndexes.size(); i++) {
            int questionIndex = questionIndexes.get(i);
            System.out.println("Question " + (i + 1) + ": " + QUESTIONS[questionIndex]);
            for (String option : OPTIONS[questionIndex]) {
                System.out.println(option);
            }

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Time's up! Moving to the next question.");
                    synchronized (this) {
                        this.cancel();
                    }
                }
            };

            timer.schedule(task, QUESTION_TIME_LIMIT_SECONDS * 1000);

            long startTime = System.currentTimeMillis();
            System.out.print("Enter your answer (A, B, C, or D), or use a lifeline (L): ");
            String answer = scanner.nextLine().toUpperCase();

            task.cancel(); // Cancel the timer since the user has answered the question
            long endTime = System.currentTimeMillis();

            if (answer.equals("A") || answer.equals("B") || answer.equals("C") || answer.equals("D")) {
                int chosenAnswerIndex = answer.charAt(0) - 'A';
                int timeTaken = (int) ((endTime - startTime) / 1000);

                int scoreMultiplier = calculateScoreMultiplier(consecutiveCorrectAnswers, timeTaken);
                int questionScore = CORRECT_ANSWER_SCORE * scoreMultiplier;

                if (chosenAnswerIndex == ANSWERS[questionIndex]) {
                    System.out.println("Correct! You earned " + questionScore + " points.");
                    score += questionScore;
                    consecutiveCorrectAnswers++;
                } else {
                    System.out.println("Incorrect. The correct answer was: " + OPTIONS[questionIndex][ANSWERS[questionIndex]]);
                    consecutiveCorrectAnswers = 0;
                }
            } else if (answer.equals("L") && lifelinesRemaining > 0) {
                useLifeline(questionIndex, lifelinesRemaining);
                lifelinesRemaining--;
            } else {
                System.out.println("Invalid input. Skipping question.");
            }
        }

        // Add the player's final score to the list of scores
        for (Player player : players) {
            player.setScore(score);
        }

        // Sort the players by score
        Collections.sort(players, (p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        // Display the leaderboard
        System.out.println("\nLeaderboard:");
        for (int i = 0; i < players.size(); i++) {
            System.out.println((i + 1) + ". " + players.get(i).getName() + " - " + players.get(i).getScore());
        }

        // Display achievements
        System.out.println("\nAchievements:");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.println(player.getName() + ":");
            for (int j = 0; j < ACHIEVEMENTS.length; j++) {
                if (player.getScore() >= ACHIEVEMENT_SCORES[j]) {
                    System.out.println("- " + ACHIEVEMENTS[j]);
                }
            }
        }
    }

    private static int calculateScoreMultiplier(int consecutiveCorrectAnswers, int timeTaken) {
        int multiplier = Math.min(consecutiveCorrectAnswers, MAX_SCORE_MULTIPLIER);
        if (timeTaken <= 5) {
            multiplier++;
        }
        return multiplier;
    }

    private static void useLifeline(int questionIndex, int lifelinesRemaining) {
        Random random = new Random();
        int optionToKeep = ANSWERS[questionIndex];
        int optionToRemove;
        do {
            optionToRemove = random.nextInt(OPTIONS[questionIndex].length);
        } while (optionToRemove == optionToKeep);

        System.out.println("Using 50/50 lifeline...");
        System.out.println("The options remaining are: ");
        for (int i = 0; i < OPTIONS[questionIndex].length; i++) {
            if (i == optionToKeep || i == optionToRemove) {
                System.out.println(OPTIONS[questionIndex][i]);
            }
        }
    }

    static class Player {
        private String name;
        private String avatar;
        private int score;

        public Player(String name, String avatar) {
            this.name = name;
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public String getAvatar() {
            return avatar;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}
//Enter number of players: 2
//Enter name for Player 1: Alice
//Enter avatar for Player 1: Avatar1
//Enter name for Player 2: Bob
//Enter avatar for Player 2: Avatar2
//
//Question 1: What is the capital of France?
//A. London
//B. Paris
//C. Rome
//D. Berlin
//Enter your answer (A, B, C, or D), or use a lifeline (L): B
//Correct! You earned 10 points.
//
//        Question 2: Which planet is known as the Red Planet?
//A. Venus
//B. Mars
//C. Jupiter
//D. Saturn
//Enter your answer (A, B, C, or D), or use a lifeline (L): B
//Incorrect. The correct answer was: A. Venus
//
//...
//
//Leaderboard:
//        1. Bob - 20
//        2. Alice - 10
//
//Achievements:
//Bob:
//        - First Try: Get the first answer correct in your first attempt.
//- Lucky Guess: Get a correct answer with less than 3 seconds remaining.
//        - Consistent: Answer 5 consecutive questions correctly.
