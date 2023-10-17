package BookwormAdventures;
import java.util.Random;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.*;

// the array of letters available to the player
public class LetterArray extends JComponent {

    private Letter[][] letters;
    private Difficulty difficulty;
    private int vowelLimit;
    private int vowelCount;
    private Random r = new Random();
    private final String[] LETTER_ARRAY = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}; //for normal mode
    private Map<String, Double> freq;
    public LetterArray(Difficulty difficulty) {
        if (difficulty == null) {
            this.difficulty = Difficulty.NORMAL;
        } else {
            this.difficulty = difficulty;
        }
        letters = new Letter[4][4];
        vowelCount = 0;
        freq = new TreeMap<String, Double>();
    }

    public void setVowelLimit() {
        switch (difficulty) {
            case NORMAL: vowelLimit = 4; break;
            case HARD: vowelLimit = 0;
        }
    }


    //for easy and hard mode
    public void setProbabilityDistributions() {
        freq.put("E", 12.02);
        freq.put("T", 9.10);
        freq.put("A", 8.12);
        freq.put("O", 7.68);
        freq.put("I", 7.31);
        freq.put("N", 6.95);
        freq.put("S", 6.28);
        freq.put("R", 6.02);
        freq.put("H", 5.92);
        freq.put("D", 4.32);
        freq.put("L", 3.98);
        freq.put("U", 2.88);
        freq.put("C", 2.71);
        freq.put("M", 2.61);
        freq.put("F", 2.30);
        freq.put("Y", 2.11);
        freq.put("W", 2.09);
        freq.put("G", 2.03);
        freq.put("P", 1.82);
        freq.put("B", 1.49);
        freq.put("V", 1.11);
        freq.put("K", 0.69);
        freq.put("X", 0.17);
        freq.put("Q", 0.11);
        freq.put("J", 0.10);
        freq.put("Z", 0.07);
        //values obtained from http://pi.math.cornell.edu/~mec/2003-2004/cryptography/subs/frequencies.html
    }

    //based off probability distribution from HW 8
    //pick a string based off frequency from an index
    private String pick(double index) {
        if (index > 99.98999999999998 || index < 0.0) {
            throw new IllegalArgumentException(
                    "Index has to be less than or " +
                            "equal to the total " + "number of records in the PD"
            );
        }

        String s = "";
        double currentIndex = 0.0;
        for (Map.Entry<String, Double> entry: freq.entrySet()) {
            String letter = entry.getKey();
            double frequency = entry.getValue();
            if (currentIndex + frequency > index) {
                return letter;
            }
            currentIndex += frequency;

        }
        throw new IllegalArgumentException("Something went wrong here dumbass");
    }

    private boolean isVowel (String s) {
        if (s == null) {
            throw new IllegalArgumentException("cannot be null");
        } else if (s.length() != 1) {
            throw new IllegalArgumentException("string must be of length 1 ");
        }
        else {
            String str = s.toUpperCase();
            if (str.equals("A") || str.equals("E") || str.equals("I") || str.equals("O") || str.equals("U")) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    private String getVowel() {
        int chance = r.nextInt(5);
        String v = "";
        switch (chance) {
            case 0: v = "A"; break;
            case 1: v = "E"; break;
            case 2: v = "I"; break;
            case 3: v = "O"; break;
            case 4: v = "U"; break;
        }
        return v;
    }

    private String getLetter() {
        return LETTER_ARRAY[r.nextInt(26)];
    }

    public Letter[][] buildArray() {
        setVowelLimit();
        setProbabilityDistributions();
        vowelCount = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                String s = getLetter();
                if (isVowel(s)) {
                    //don't want to oversaturate with vowels
                    if (vowelLimit == 0) {
                        while (isVowel(s)) {
                            s = getLetter();
                        }
                    } else {
                        vowelLimit--;
                        vowelCount++;
                    }
                }
                letters[row][col] = new Letter(s, row, col, true);
            }
        }
        reVowel();
        return letters;
    }

    public void reVowel() {
        //want at least 2 vowels in the array at all times
        while (vowelCount < 2) {
            int row = r.nextInt(4);
            int col = r.nextInt(4);
            if (!isVowel(letters[row][col].getLETTER())) {
                letters[row][col] = new Letter(getVowel(), row, col, true);
                vowelCount++;
            }
        }
    }

    //think about implementing a shuffle command, not necessary but would be nice

    public Letter[][] getLetters() {
        return this.letters;
    }

    public Letter[][] buildEmptyArray() {
        this.buildArray();
        for (int row = 0; row < letters.length; row++) {
            for (int col = 0; col < letters[row].length; col++) {
                letters[row][col].setIsInArray(false);
            }
        }
        return letters;
    }

    public String[] getLETTER_ARRAY() {
        return LETTER_ARRAY;
    }

    public Map<String, Double> getFreq() {
        return freq;
    }

    public static void main(String[] args) {
        LetterArray l = new LetterArray(Difficulty.NORMAL);
        l.buildArray();
        for (int i = 0; i < 10; i++) {
            System.out.println(l.isVowel(l.getVowel()));
        }

        Map<String, Double> frequencies = l.getFreq();
        double sum = 0.0;
        for (Map.Entry<String, Double> entry: frequencies.entrySet()) {
            sum += entry.getValue();
        }
        System.out.println(sum);

    }
}
