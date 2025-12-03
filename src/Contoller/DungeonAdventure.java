package controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Random;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import model.Dungeon;
import model.DungeonTile;
import model.DungeonGenerator;
import model.Hero;
import model.Priestess;
import model.Thief;
import model.Warrior;
import view.ConsoleView;
import view.DungeonBoardPanel;
import view.DungeoneerFrame;

/**
 * Main controller / entry point for the Dungeoneer game.
 */
public class DungeonAdventure {

    /** The active hero. */
    private static Hero myHero;

    /** Console view used for debugging / logging. */
    private static ConsoleView myConsoleView;

    /** Main GUI window. */
    private static DungeoneerFrame myGui;

    /** Global difficulty (1–9). */
    private static int myDifficulty;

    /** Base seed typed by the user (or random). */
    private static long mySeed;

    /* ---------- Multiple dungeons ---------- */

    private static Dungeon lobbyDungeon;
    private static Dungeon northDungeon;
    private static Dungeon southDungeon;
    private static Dungeon eastDungeon;
    private static Dungeon westDungeon;

    /** Currently active dungeon (shown in GUI & used for logic). */
    private static Dungeon currentDungeon;

    /** 'L' = Lobby, 'N','S','E','W' = branch dungeons. */
    private static char currentArea;

    /** Which branch dungeon contains the true OUT exit. */
    private static char exitDungeonKey;

    /* ---------- Main ---------- */

    public static void main(final String[] theArgs) {
        setupGame();
    }

    /* ---------- Setup ---------- */

    private static void setupGame() {

        myConsoleView = new ConsoleView();
        myConsoleView.showMessage("Starting Dungeoneer...");

        // Single setup dialog – also sets mySeed & myDifficulty
        myHero = promptHero();

        // Generate lobby + four branch dungeons using different seeds
        lobbyDungeon = DungeonGenerator.generate(new Random(mySeed), myDifficulty, myHero);
        northDungeon = DungeonGenerator.generate(new Random(mySeed + 1), myDifficulty, myHero);
        southDungeon = DungeonGenerator.generate(new Random(mySeed + 2), myDifficulty, myHero);
        eastDungeon  = DungeonGenerator.generate(new Random(mySeed + 3), myDifficulty, myHero);

        // Randomly choose which branch is the "true exit" dungeon
        char[] dirs = {'N', 'S', 'E', 'W'};
        exitDungeonKey = dirs[new Random(mySeed).nextInt(dirs.length)];

        currentArea = 'L';
        currentDungeon = lobbyDungeon;

        // Create and show the GUI window
        myGui = new DungeoneerFrame(currentDungeon, myHero);
        myGui.setVisible(true);

        myGui.showMessage("Welcome to Dungeoneer!");
        myGui.showDungeon(currentDungeon);
    }

    /* ---------- Hero setup dialog ---------- */

    /**
     * Shows a single setup dialog where the player:
     *  - chooses a hero class (with sprite)
     *  - chooses difficulty (1–9)
     *  - enters hero name
     *  - optionally provides a dungeon seed
     *
     * Sets myDifficulty and mySeed, and returns the created Hero.
     */
    private static Hero promptHero() {

        // Hero sprites and labels
        ImageIcon warriorIcon   = loadScaledIcon("Dungeoneer_Characters/warrior_down.png", 128);
        ImageIcon thiefIcon     = loadScaledIcon("Dungeoneer_Characters/thief_down.png", 128);
        ImageIcon priestessIcon = loadScaledIcon("Dungeoneer_Characters/priestess_down.png", 128);

        JLabel warriorLabel = new JLabel("Warrior", warriorIcon, SwingConstants.CENTER);
        warriorLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        warriorLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

        JLabel thiefLabel = new JLabel("Thief", thiefIcon, SwingConstants.CENTER);
        thiefLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        thiefLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

        JLabel priestessLabel = new JLabel("Priestess", priestessIcon, SwingConstants.CENTER);
        priestessLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        priestessLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

        // Radio buttons
        JRadioButton warriorButton   = new JRadioButton("Warrior");
        JRadioButton thiefButton     = new JRadioButton("Thief");
        JRadioButton priestessButton = new JRadioButton("Priestess");

        ButtonGroup heroGroup = new ButtonGroup();
        heroGroup.add(warriorButton);
        heroGroup.add(thiefButton);
        heroGroup.add(priestessButton);

        warriorButton.setSelected(true);

        JPanel warriorPanel = new JPanel(new BorderLayout());
        warriorPanel.add(warriorLabel, BorderLayout.CENTER);
        warriorPanel.add(warriorButton, BorderLayout.SOUTH);

        JPanel thiefPanel = new JPanel(new BorderLayout());
        thiefPanel.add(thiefLabel, BorderLayout.CENTER);
        thiefPanel.add(thiefButton, BorderLayout.SOUTH);

        JPanel priestessPanel = new JPanel(new BorderLayout());
        priestessPanel.add(priestessLabel, BorderLayout.CENTER);
        priestessPanel.add(priestessButton, BorderLayout.SOUTH);

        JPanel heroesRow = new JPanel(new GridLayout(1, 3, 10, 0));
        heroesRow.add(warriorPanel);
        heroesRow.add(thiefPanel);
        heroesRow.add(priestessPanel);

        // Difficulty slider
        JSlider diffSlider = new JSlider(1, 9, 5);
        diffSlider.setMajorTickSpacing(1);
        diffSlider.setPaintTicks(true);
        diffSlider.setPaintLabels(true);
        diffSlider.setSnapToTicks(true);

        JLabel diffLabel = new JLabel(difficultyDescription(diffSlider.getValue()));
        diffLabel.setHorizontalAlignment(SwingConstants.CENTER);

        diffSlider.addChangeListener(e ->
                diffLabel.setText(difficultyDescription(diffSlider.getValue())));

        JPanel diffPanel = new JPanel(new BorderLayout());
        diffPanel.add(new JLabel("Difficulty (1–9):", SwingConstants.CENTER),
                      BorderLayout.NORTH);
        diffPanel.add(diffSlider, BorderLayout.CENTER);
        diffPanel.add(diffLabel, BorderLayout.SOUTH);

        // Hero Name
        JTextField nameField = new JTextField(12);
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.add(new JLabel("Hero Name: "), BorderLayout.WEST);
        namePanel.add(nameField, BorderLayout.CENTER);

        // Dungeon Seed
        JTextField seedField = new JTextField(12);
        JPanel seedPanel = new JPanel(new BorderLayout());
        seedPanel.add(new JLabel("Dungeon Seed: "), BorderLayout.WEST);
        seedPanel.add(seedField, BorderLayout.CENTER);

        // Main stacked panel
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(Box.createVerticalStrut(5));

        JLabel title = new JLabel("Choose your Hero and Difficulty", SwingConstants.CENTER);
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        content.add(title);

        content.add(Box.createVerticalStrut(10));
        content.add(heroesRow);
        content.add(Box.createVerticalStrut(10));
        content.add(diffPanel);
        content.add(Box.createVerticalStrut(10));
        content.add(namePanel);
        content.add(Box.createVerticalStrut(10));
        content.add(seedPanel);

        int result = JOptionPane.showConfirmDialog(
                null,
                content,
                "Dungeoneer Setup",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            System.exit(0);
        }

        // Difficulty
        myDifficulty = diffSlider.getValue();

        // Hero name
        String heroName = nameField.getText().trim();
        if (heroName.isEmpty()) {
            heroName = "Hero";
        }

        // Seed
        String seedInput = seedField.getText().trim();
        if (seedInput.isEmpty()) {
            mySeed = new Random().nextLong();
        } else {
            try {
                mySeed = Long.parseLong(seedInput);
            } catch (NumberFormatException ex) {
                mySeed = new Random().nextLong();
            }
        }

        // Create hero
        Hero hero;
        if (priestessButton.isSelected()) {
            hero = new Priestess(heroName);
        } else if (thiefButton.isSelected()) {
            hero = new Thief(heroName);
        } else {
            hero = new Warrior(heroName);
        }

        myConsoleView.showMessage(heroName + " has been created!");
        return hero;
    }

    /* ---------- Door handling from the board ---------- */

    /**
     * Called by DungeoneerFrame / DungeonBoardPanel when the hero steps onto a
     * door tile and presses OK in the confirmation dialog.
     */
    public static void handleDoorEvent(DungeonTile door) {
        switch (door) {
            case DOOR_N -> enterBranchDungeon('N');
            case DOOR_S -> enterBranchDungeon('S');
            case DOOR_E -> enterBranchDungeon('E');
            case DOOR_W -> enterBranchDungeon('W');
            default     -> myGui.showMessage("Unknown door entered.");
        }
    }

    /** Switches currentDungeon to the correct branch and updates the GUI. */
    private static void enterBranchDungeon(final char side) {
        switch (side) {
            case 'N' -> currentDungeon = northDungeon;
            case 'S' -> currentDungeon = southDungeon;
            case 'E' -> currentDungeon = eastDungeon;
            case 'W' -> currentDungeon = westDungeon;
            default  -> { return; }
        }

        currentArea = side;

        myGui.showMessage("You step through the " + directionName(side) + " door...");

        if (exitDungeonKey == side) {
            myGui.showMessage("You sense that the OUT exit is somewhere in this dungeon.");
        }

        // Update the board to show the new dungeon
        myGui.showDungeon(currentDungeon);
    }

    /* ---------- Helpers ---------- */

    private static String directionName(final char side) {
        return switch (side) {
            case 'N' -> "North";
            case 'S' -> "South";
            case 'E' -> "East";
            case 'W' -> "West";
            default  -> "?";
        };
    }

    /** Loads an image and scales it to the given size (square). */
    private static ImageIcon loadScaledIcon(final String path, final int size) {
        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /** Returns a text description for a given difficulty value. */
    private static String difficultyDescription(final int diff) {
        if (diff <= 2) {
            return diff + " - Beginner. New to Dungeoneer!";
        } else if (diff <= 3) {
            return diff + " - Easy. Not too hard and not too easy.";
        } else if (diff <= 5) {
            return diff + " - Regular. The original game.";
        } else if (diff <= 8) {
            return diff + " - Hardened. For the experienced to challenge themselves.";
        } else {
            return diff + " - Veteran. You won't survive...";
        }
    }
}