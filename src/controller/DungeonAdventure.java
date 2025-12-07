package controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import model.Dungeon;
import model.DungeonTile;
import model.DungeonGenerator;
import model.Hero;
import model.Priestess;
import model.Thief;
import model.Warrior;
import view.DungeoneerFrame;

/**
 * Entry point and top–level controller for the Dungeoneer game.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Show the welcome screen and help dialog.</li>
 *   <li>Prompt the player for hero, difficulty, and seed.</li>
 *   <li>Generate all dungeons (lobby + branch dungeons).</li>
 *   <li>Own the active {@link Hero}, current {@link Dungeon}, and GUI frame.</li>
 *   <li>Handle high-level events such as entering doors.</li>
 * </ul>
 * </p>
 *
 * This class is intentionally static because there is a single global game
 * controller in the current design.
 *
 * @author(s): Cristian, Skyler, Hiba
 * @version 23.0.1
 */
public class DungeonAdventure {

    /* ------------------------------------------------------------------
     * Constants
     * ------------------------------------------------------------------ */

    /**
     * Main Dungeoneer logo used on the welcome and help screens.
     */
    private static final ImageIcon DUNGEONEER_LOGO =
            loadScaledIcon("Dungeoneer_Icon.png", 64);

    /**
     * Text shown in the Help window on the welcome screen.
     */
    private static final String HELP_TEXT =
            "Controls:\n"
          + "W / ↑  -> Move Up\n"
          + "S / ↓  -> Move Down\n"
          + "A / ←  -> Move Left\n"
          + "D / →  -> Move Right\n"
          + "\n"
          + "I         -> Inventory / Selections\n"
          + "ENTER     -> Use Item\n"
          + "DELETE    -> Drop / Remove Item\n";

    /* ------------------------------------------------------------------
     * Global game state
     * ------------------------------------------------------------------ */

    /**
     * The active hero chosen by the player.
     */
    private static Hero myHero;

    /**
     * Main GUI window for the game.
     */
    private static DungeoneerFrame myGui;

    /**
     * Selected difficulty level (1–9).
     */
    private static int myDifficulty;

    /**
     * Base random seed typed by the user (or generated randomly).
     * Used to seed all dungeon generators.
     */
    private static long mySeed;

    /**
     * Lobby dungeon (central area with doors to all branch dungeons).
     */
    private static Dungeon lobbyDungeon;

    /**
     * Branch dungeon to the North of the lobby.
     */
    private static Dungeon northDungeon;

    /**
     * Branch dungeon to the South of the lobby.
     */
    private static Dungeon southDungeon;

    /**
     * Branch dungeon to the East of the lobby.
     */
    private static Dungeon eastDungeon;

    /**
     * Branch dungeon to the West of the lobby.
     */
    private static Dungeon westDungeon;

    /**
     * Currently active dungeon (shown in the GUI and used for movement logic).
     */
    private static Dungeon currentDungeon;

    /**
     * Current area identifier:
     * <ul>
     *   <li>'L' – Lobby</li>
     *   <li>'N' – North branch dungeon</li>
     *   <li>'S' – South branch dungeon</li>
     *   <li>'E' – East branch dungeon</li>
     *   <li>'W' – West branch dungeon</li>
     * </ul>
     */
    private static char currentArea;

    /**
     * Which branch dungeon contains the true OUT exit.
     * Must be one of 'N', 'S', 'E', or 'W'.
     */
    private static char exitDungeonKey;

    /* ------------------------------------------------------------------
     * Main
     * ------------------------------------------------------------------ */

    /**
     * Program entry point. Schedules the welcome screen to be shown
     * on the Swing event dispatch thread.
     *
     * @param theArgs command-line arguments (unused)
     */
    public static void main(final String[] theArgs) {
        SwingUtilities.invokeLater(DungeonAdventure::showWelcomeScreen);
    }

    /* ------------------------------------------------------------------
     * Welcome screen & help
     * ------------------------------------------------------------------ */

    /**
     * Shows the initial welcome window with:
     * <ul>
     *   <li>New Game</li>
     *   <li>Load Game (currently disabled)</li>
     *   <li>Help</li>
     * </ul>
     * Selecting New Game will close this window and start the setup flow.
     */
    private static void showWelcomeScreen() {
        JFrame frame = new JFrame("Dungeoneer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use logo as the window icon, if it loaded correctly
        if (DUNGEONEER_LOGO != null) {
            frame.setIconImage(DUNGEONEER_LOGO.getImage());
        }

        JPanel root = new JPanel(new BorderLayout(10, 10));

        // Vertical stack: logo, title, buttons
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // Logo
        JLabel logoLabel = new JLabel(DUNGEONEER_LOGO);
        logoLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        // Title
        JLabel title = new JLabel("Welcome to Dungeoneer!", SwingConstants.CENTER);
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        // Buttons row
        JPanel buttons = new JPanel();
        JButton newGameButton = new JButton("New Game");
        JButton loadGameButton = new JButton("Load Game");
        JButton helpButton    = new JButton("Help");

        // Load Game is grayed out for now
        loadGameButton.setEnabled(false);

        newGameButton.addActionListener(_ -> {
            frame.dispose();   // close the welcome window
            setupGame();       // start main game setup
        });

        helpButton.addActionListener(_ -> showHelpDialog(frame));

        buttons.add(newGameButton);
        buttons.add(loadGameButton);
        buttons.add(helpButton);
        buttons.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        // Build the vertical layout
        center.add(Box.createVerticalStrut(8));
        center.add(logoLabel);
        center.add(Box.createVerticalStrut(4));
        center.add(title);
        center.add(Box.createVerticalStrut(8));
        center.add(buttons);
        center.add(Box.createVerticalStrut(8));

        root.add(center, BorderLayout.CENTER);

        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Displays a modal help dialog with basic controls and optionally
     * the Dungeoneer logo.
     *
     * @param parent the parent frame to center the dialog on
     */
    private static void showHelpDialog(final JFrame parent) {
        JTextArea area = new JTextArea(HELP_TEXT, 8, 30);
        area.setEditable(false);
        area.setOpaque(false);
        area.setFocusable(false);

        // Build a panel with logo on the left and text on the right
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        if (DUNGEONEER_LOGO != null) {
            panel.add(new JLabel(DUNGEONEER_LOGO), BorderLayout.WEST);
        }
        panel.add(area, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
                parent,
                panel,
                "Dungeoneer Help",
                JOptionPane.PLAIN_MESSAGE   // no default Duke icon
        );
    }

    /* ------------------------------------------------------------------
     * Game setup
     * ------------------------------------------------------------------ */

    /**
     * Performs the game setup sequence:
     * <ol>
     *   <li>Prompts the player to choose hero, difficulty, and seed.</li>
     *   <li>Generates lobby + branch dungeons using deterministic seeds.</li>
     *   <li>Randomly chooses which branch contains the true OUT exit.</li>
     *   <li>Creates and shows the main game GUI.</li>
     * </ol>
     */
    private static void setupGame() {

        // Single setup dialog – also sets mySeed & myDifficulty
        myHero = promptHero();

        // Generate lobby + four branch dungeons using different seeds
        lobbyDungeon = DungeonGenerator.generate(new Random(mySeed), myDifficulty, myHero);
        northDungeon = DungeonGenerator.generate(new Random(mySeed + 1), myDifficulty, myHero);
        southDungeon = DungeonGenerator.generate(new Random(mySeed + 2), myDifficulty, myHero);
        eastDungeon  = DungeonGenerator.generate(new Random(mySeed + 3), myDifficulty, myHero);

        // NOTE: westDungeon could be generated here as well when implemented.
        // Example (uncomment when ready):
        // westDungeon = DungeonGenerator.generate(new Random(mySeed + 4), myDifficulty, myHero);

        // Randomly choose which branch is the "true exit" dungeon
        char[] dirs = {'N', 'S', 'E', 'W'};
        exitDungeonKey = dirs[new Random(mySeed).nextInt(dirs.length)];

        currentArea = 'L';
        currentDungeon = lobbyDungeon;

        // Create and show the GUI window
        myGui = new DungeoneerFrame(currentDungeon, myHero, myDifficulty);
        myGui.setVisible(true);

        myGui.showMessage("Welcome to Dungeoneer!");
        myGui.showDungeon(currentDungeon);
    }

    /**
     * Shows a single setup dialog where the player:
     * <ul>
     *   <li>chooses a hero class (with sprite),</li>
     *   <li>chooses difficulty (1–9),</li>
     *   <li>enters hero name,</li>
     *   <li>optionally provides a dungeon seed.</li>
     * </ul>
     * This method sets {@link #myDifficulty} and {@link #mySeed} and
     * returns the newly created {@link Hero}.
     *
     * @return the {@link Hero} selected and configured by the player
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

        // Warrior is the default selection
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

        diffSlider.addChangeListener(_ ->
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
            // Player canceled setup → exit the application
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
                // Fallback to random if parsing fails
                mySeed = new Random().nextLong();
            }
        }

        // Create hero based on selected class
        Hero hero;
        if (priestessButton.isSelected()) {
            hero = new Priestess(heroName);
        } else if (thiefButton.isSelected()) {
            hero = new Thief(heroName);
        } else {
            hero = new Warrior(heroName);
        }

        return hero;
    }

    /* ------------------------------------------------------------------
     * Door / dungeon transitions
     * ------------------------------------------------------------------ */

    /**
     * Called by {@link view.DungeoneerFrame} / {@link view.DungeonBoardPanel}
     * when the hero steps onto a door tile and confirms entering it.
     * <p>
     * This method delegates to {@link #enterBranchDungeon(char)} based
     * on the door tile direction.
     * </p>
     *
     * @param door the door tile type the hero stepped on
     */
    public static void handleDoorEvent(final DungeonTile door) {
        switch (door) {
            case DOOR_N -> enterBranchDungeon('N');
            case DOOR_S -> enterBranchDungeon('S');
            case DOOR_E -> enterBranchDungeon('E');
            case DOOR_W -> enterBranchDungeon('W');
            default     -> myGui.showMessage("Unknown door entered.");
        }
    }

    /**
     * Switches {@link #currentDungeon} to the appropriate branch dungeon
     * based on the given side and updates the GUI.
     * <p>
     * Also updates {@link #currentArea} and displays flavor messages,
     * including a hint if this dungeon contains the true OUT exit.
     * </p>
     *
     * @param side the side/direction: 'N', 'S', 'E', or 'W'
     */
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

    /* ------------------------------------------------------------------
     * Utility helpers
     * ------------------------------------------------------------------ */

    /**
     * Converts a direction character into a user-friendly name.
     *
     * @param side direction character ('N', 'S', 'E', 'W')
     * @return the human-readable direction name, or "?" if unknown
     */
    private static String directionName(final char side) {
        return switch (side) {
            case 'N' -> "North";
            case 'S' -> "South";
            case 'E' -> "East";
            case 'W' -> "West";
            default  -> "?";
        };
    }

    /**
     * Loads an image from the given path and scales it to a square icon
     * of the given size using smooth scaling.
     *
     * @param path the classpath or file-system path to the image
     * @param size the width and height in pixels of the scaled icon
     * @return a scaled {@link ImageIcon}. If loading fails, the icon may be empty.
     */
    private static ImageIcon loadScaledIcon(final String path, final int size) {
        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /**
     * Returns a descriptive text label for a given difficulty setting.
     *
     * @param diff difficulty value (1–9)
     * @return a user-friendly description of that difficulty
     */
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