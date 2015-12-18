
import java.io.Serializable;
import java.util.HashMap;

/**
 * Represents the computer opponent and stores a hashmap of user input patterns.
 * Makes predictions of the user's next moves based on the patterns stored.
 *
 * @author Nicholas Grant
 *
 */
public class Computer implements Serializable {

    /**
     * Stores user input patterns
     */
    private HashMap<Pattern, Integer> patterns;

    /**
     * Default computer constructor initializes a hashmap
     */
    public Computer() {
        patterns = new HashMap<Pattern, Integer>();
    }

    /**
     * Gets the hashmap of patterns
     *
     * @return	patterns hashmap
     */
    public HashMap<Pattern, Integer> getPatterns() {
        return patterns;
    }

    /**
     * Takes in a pattern of size 4 and checks to see if that pattern exists. If
     * it does, it adds R S P to the pattern to see if that pattern exists. If
     * not, it looks for a smaller pattern until it finds one. If a pattern is
     * not found, a random move is made.
     *
     * @param pattern	User's last four choices
     * @return	The computer's move
     */
    public char makePrediction(Pattern pattern) {

        int rock = 0, paper = 0, scissors = 0;
        String predict = pattern.getPattern();
        String test;
        char computerMove = 'R';
        int i = 0;
        boolean patternFound = false;

        while ((predict.length() - i) > 1 && !patternFound) {
            test = predict.substring(1, predict.length() - i);
            if (patterns.containsKey(new Pattern(test))) {
                test = predict.substring(0, predict.length() - i) + 'R';
                if (patterns.containsKey(new Pattern(test))) {
                    rock = patterns.get(new Pattern(test));
                }
                test = predict.substring(0, predict.length() - i) + 'P';
                if (patterns.containsKey(new Pattern(test))) {
                    paper = patterns.get(new Pattern(test));
                }
                test = predict.substring(0, predict.length() - i) + 'S';
                if (patterns.containsKey(new Pattern(test))) {
                    scissors = patterns.get(new Pattern(test));
                }
                patternFound = true;
            } else {
                System.out.println("Random chosen.");
                int move = (int) ((Math.random() * 3) + 1);
                if (move == 1) {
                    computerMove = 'R';
                } else if (move == 2) {
                    computerMove = 'P';
                } else {
                    computerMove = 'S';
                }
                break;
            }

            if (rock > paper && rock > scissors) {
                computerMove = 'P';
            } else if (paper > rock && paper > scissors) {
                computerMove = 'S';
            } else if (scissors > rock && scissors > paper) {
                computerMove = 'R';
            } else if (rock == paper && rock > scissors) {
                computerMove = 'P';
            } else if (rock == scissors && rock > paper) {
                computerMove = 'R';
            } else if (paper == scissors && paper > rock) {
                computerMove = 'S';
            }
            i++;
        }
        return computerMove;
    }

    /**
     * If the pattern already exists, the value of the pattern is incremented.
     * Otherwise, the pattern is placed in the hashmap and given a value of 1.
     *
     * @param pattern
     */
    public void storePattern(Pattern pattern) {
        if (patterns.containsKey(pattern)) {
            patterns.put(pattern, patterns.get(pattern) + 1);
        } else {
            patterns.put(pattern, 1);
        }
    }
}
