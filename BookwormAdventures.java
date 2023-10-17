package BookwormAdventures;

import java.io.BufferedReader;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

public class BookwormAdventures {
    private List<Letter> word;
    private List<String> englishWords;
    private List<Enemy> enemies;
    private List<Enemy> defeatedEnemies;
    private Player player;
    private Enemy enemy;
    //an array of artifacts called inventory, at most 3 artifacts at any time
    private int numTurns;
    private boolean player1;
    private boolean gameOver;
    private LetterArray l;
    //private String[][] letterArray;
    private boolean pressedAttack;
    private boolean inFight;
    private Difficulty difficulty;
    private int effectDuration;
    private boolean vampireDefeated;
    private boolean playing;
    private boolean playerWon;
    boolean skipTurn;
    private boolean advanceCalled;
    private  int numEnemies;
    //private GameBoard board;
    Random g = new Random();


    public BookwormAdventures (Difficulty difficulty) {
        this.difficulty = difficulty;
        reset();
    }

    public void initializeEnglishWords() {
        List<String> eLetters = new LinkedList<>();
        for (String s: l.getLETTER_ARRAY()) {
            eLetters.add(s);
        }
        //initialize english words
        BufferedReader br = FileLineIterator.fileToReader("files/words.csv");
        FileLineIterator f = new FileLineIterator(br);
        englishWords = new LinkedList<>();
        //System.out.println(eLetters.contains("A"));
        while (f.hasNext()) {
            boolean isGoodWord = true;
            String s = f.next();
            s = s.toUpperCase();
            for (int i = 0; i < s.length(); i++) {
                String c = s.substring(i, i+1);
                if (!eLetters.contains(c)) {
                    //System.out.println("eLetters contains: " + c + eLetters.contains(c));
                    isGoodWord = false;
                }
            }
            if (isGoodWord && s.length() >= 3) {
                englishWords.add(s);
            }
        }

    }


    public List<String> getEnglishWords() {
        return englishWords;
    }


    public void initializeEnemies() {
        enemies.clear();
        defeatedEnemies.clear();
        word = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            if (i < 2) {
                //always have first 2 enemies be grunts
                enemies.add(new Grunt( 100.0, 20.0));
            }
            if (i == 2) {
                //the third will always be a vampire, so to unlock the vampire artifact for healing
                enemies.add(new Vampire(100.0, 15.0));
            }
            if (i > 2 && i < 10 - 1) {
                int n = g.nextInt(3);
                //randomly choose between spider, siren, or snowman
                Enemy e;
                switch (n) {
                    case 0: e = new Vampire(100.0, 20.0);
                    break;
                    case 1: e = new Siren(100.0, 15.0);
                    break;
                    case 2: e = new Spider( 100.0, 20.0);
                    break;
                    default: e = new Grunt(100.0, 20.0);
                    break;
                }
                enemies.add(e);
            }
            if (i == 10 - 1) {
                enemies.add(new Boss (500.0, 20.0));
            }
        }
        numEnemies = enemies.size();
    }

    //sets game to initial state
    public void reset() {
        l = new LetterArray(difficulty);
        l.buildArray();
        enemies = new LinkedList<>();
        defeatedEnemies = new LinkedList();
        initializeEnglishWords();
        word = new LinkedList<>();
        player = new Player(100.0, 15.0, "Player");
        this.initializeEnemies();
        numTurns = 0;
        effectDuration = player.getEffectDuration();
        player1 = true;
        gameOver = false;
        inFight = false;
        playerWon = false;
        advanceCalled = false;
        enemy = enemies.get(0);
        skipTurn = false;
    }

    public void changeDifficulty(Difficulty d) {
        difficulty = d;
        reset();
    }

    public List<Enemy> getEnemies() {
        return this.enemies;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    //advances onto the next enemy
    public void advance() {
        //keep track of enemies defeated
        //reset the current enemy
        player1 = true;
        gameOver = false;
        inFight = false;
        word.clear();
        advanceCalled = true;
        effectDuration = player.getEffectDuration();
        enemy = enemies.get(0);
        if (defeatedEnemies.size() >= 3) {
            player.setVampireEquipped(true);
        }
        //redraw board or something
    }

    public List<Enemy> getDefeatedEnemies() {
        return defeatedEnemies;
    }

    public Letter[][] getLetterArray() {
        return l.getLetters();
    }

    public boolean getAdvanceCalled() {
        return advanceCalled;
    }

    public List<Letter> getWord() {
        return word;
    }

    public LetterArray getL() {
        return l;
    }

    private String buildWord() {
        String w = "";
        for (Letter s: word) {
            w += s.getLETTER();
        }
        if (englishWords.contains(w)) {
            return w;
        } else {
            throw new IllegalArgumentException("Word does not exist!");
        }

    }

    //transfers letters from grid to word list
    public void transfer(Letter letter) {
        if (letter == null) {
            throw new IllegalArgumentException("cannot have null parameter");
        }
        Letter[][] letters = l.getLetters();
        Letter a = letters[letter.getRow()][letter.getCol()];
        if (a != letter && a != null) {
            System.out.println(a.getLETTER());
            System.out.println(letter.getLETTER());
            throw new IllegalArgumentException("The letters are not equal!");
        }
        if (letter.getIsInArray() && !letter.getFrozen()) {
            //be careful here, unsure if aliasing will transfer into the array properly
            letter.setIsInArray(false);
            word.add(letter);
            letters[letter.getRow()][letter.getCol()] = letter;
        } else {
            if (word.contains(letter)) {
                word.remove(letter);
                letter.setIsInArray(true);
                letters[letter.getRow()][letter.getCol()] = letter;
            }
        }
    }

    public double getDamage(String s) {
        //need to implement stuff for artifacts too
        int length = s.length();
        double damage = 0.0;
        if (length >= 3) {
            //+5 damage for each letter
            // double damage for greater than 8 letters
            if (englishWords.contains(s)) {
                damage = 15.0 + ((length - 3) * 5);
                if (length >= 8) {
                    damage *= 2;
                }
                if (length >= 12) {
                    damage *= 2;
                    //should quadruple damage if greater than 12
                }
            } else {
                System.out.println("word does not exist (getDamage)");
            }
            //if halfway through, 50% damage boost
            if (defeatedEnemies.size() >= 5) {
                damage *= 1.5;
            }
        }
        return damage;
    }

    public void skipTurn () {
        if (getCurrentPlayer()) {
            skipTurn = true;
        } else {
            skipTurn = false;
        }
    }

    //run this method when the player tries to advance with his word
    //each player gets 3 tries to push through a word
    //words must be at least 3 letters in length
    public boolean playTurn() {
        advanceCalled = false;
        if (gameOver) {
            return false;
        }
        if (player1 && !player.getStunned() && !skipTurn) {
            //build a new array every time it's player 1
            //think about word.clear() condition
            double damage;
            String str = "";
            str = buildWord();
            //damage = getDamage(str);
            if (str.equals("")) {
                //do something to let player know he's an idiot
                System.out.println("need to enter words");
                //numTurns++;
                return false;
            } else {
                //str = buildWord();
                damage = getDamage(str);
                if (damage == 0.0) {
                    System.out.println("NO damage is being done");
                    //numTurns++;
                    return false;
                }
                System.out.println(str);
                //player.setDamage(damage);
                player.attack(enemy, damage);
                System.out.println("Dealt " + damage + " damage to " + enemy.getName());
                System.out.println("Enemy is at " + enemy.getHealth() + "HP");
                //build new array after player turn
                l.buildArray();
            }
            //do damage to enemy based on size of word linkedlist
        } else {
            if (!skipTurn) {
                enemy.attack(player);
                System.out.println("Player took " + 20.0 + " damage!");
                System.out.println("Player is at " + player.getHealth() + " HP");
            }
        }
        System.out.println("Now checking winner:");
        switch (checkWinner()) {
            case 1: enemy.die(); defeatedEnemies.add(enemies.remove(0));
                if (enemies.size() == 0) {
                    gameOver = true;
                    playerWon = true;
                    System.out.println("You've beaten the game!");
                } else {
                    advance(); System.out.println("Nice Job! Keep going!");
                    if (!player.getVampireEquipted()) {
                        player.setHealth(100.0);
                    }
                }
            break;
            case 2: inFight = false; player.die(); System.out.println("You've lost!");
                gameOver = true;
            break;
            case 0:
                if (player1) {
                    //if enemy's turn, make an empty array
                    l.buildEmptyArray();
                } else {
                    //if your turn is next, build a new array
                    l.buildArray();
                }
                player.takeStunDamage();
                if (!player.getStunned()) {
                    player1 = !player1;
                } else {
                    System.out.println("stunned for " + player.getEffectDuration() + " turns (BW)");
                }; System.out.println("keep fighting!"); break;
        }
        skipTurn = false;
        word.clear();
        return true;
    }

    // return 0 if nobody has won yet, 1 if player1 wins, 2 if enemy wins, 3 if game is beaten
    // win condition is if enemy or player health is completely depleted
    public int checkWinner() {
        if (gameOver && playerWon) {
            return 3;
        }
        if (player.getHealth() <= 0.0 && enemy.getHealth() > 0.0) {
            //enemy wins
            System.out.println("Game over! You've lost!");
            return 2;
        } else if (player.getHealth() > 0.0 && enemy.getHealth() <= 0.0) {
            //player wins
            return 1;
        } else {
            return 0;
        }
    }

    public int getNumEnemies() {
        return numEnemies;
    }

    public boolean getPlayerWon() {
        return playerWon;
    }

    public boolean getCurrentPlayer() {
        return player1;
    }

    public int getNumTurns() {
        return numTurns;
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    //function to manually set word for testing purposes
    public void setWord(String s) {
        word.clear();
        for (int i = 0; i < s.length(); i++) {
            String c = s.substring(i, i+1);
            word.add(new Letter(c.toUpperCase(), 0, i, false));
        }
    }

    //function to manually set enemies for testing purposes
    public void setEnemies(List<Enemy> e) {
        enemies = e;
        numEnemies = enemies.size();
        enemy = e.get(0);
    }

    public static void main(String[] args) {
        List<String> h = new LinkedList<>();
        h.add("S");
        h.get(0);
        System.out.println(h.remove(0));
    }

}
