package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Thief;
import model.Priestess;
import model.Warrior;
import model.Dungeon;
import model.Hero;
import view.DungeonBoardPanel;

/**
 * Swing-based GUI for the Dungeoneer game.
 * Shows HUD (gold, health, pillars, inventory),
 * a main game panel, and a text dialog area.
 */
public class DungeoneerFrame extends JFrame implements GameView {

    private static final long serialVersionUID = 1L;

    // Model references
    private final Dungeon myDungeon;
    private final Hero myHero;

    // HUD components
    private final JLabel myGoldLabel;
    private final JProgressBar myHealthBar;
    private final JLabel myHpLabel;
    private final JLabel[] myPillarSlots;
    private final JLabel[] myInventorySlots;

    // Center game area
    private final DungeonBoardPanel myGamePanel;

    // Text dialog
    private final JTextArea myLogArea;

    public DungeoneerFrame(final Dungeon theDungeon, final Hero theHero) {
        super("Dungeoneer");
        myDungeon = theDungeon;
        myHero = theHero;

        // --- HUD top bar ---
        JPanel hudPanel = new JPanel();
        hudPanel.setLayout(new BoxLayout(hudPanel, BoxLayout.X_AXIS));
        hudPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Gold
        myGoldLabel = new JLabel("Gold: 0");
        myGoldLabel.setFont(myGoldLabel.getFont().deriveFont(Font.BOLD, 14f));
        hudPanel.add(myGoldLabel);
        hudPanel.add(Box.createHorizontalStrut(20));

        // Health bar + HP label
        JLabel healthTitle = new JLabel("Health: ");
        hudPanel.add(healthTitle);

        myHealthBar = new JProgressBar();
        myHealthBar.setPreferredSize(new Dimension(200, 16));
        hudPanel.add(myHealthBar);

        hudPanel.add(Box.createHorizontalStrut(5));
        myHpLabel = new JLabel("HP: 0 / 0");
        hudPanel.add(myHpLabel);

        hudPanel.add(Box.createHorizontalStrut(20));

        // Pillars
        hudPanel.add(new JLabel("Pillars: "));
        myPillarSlots = new JLabel[4];
        for (int i = 0; i < myPillarSlots.length; i++) {
            JLabel slot = createSquareSlot();
            myPillarSlots[i] = slot;
            hudPanel.add(slot);
            hudPanel.add(Box.createHorizontalStrut(3));
        }

        hudPanel.add(Box.createHorizontalStrut(20));

        // Inventory slots (e.g., 6 slots)
        hudPanel.add(new JLabel("Inventory: "));
        myInventorySlots = new JLabel[5];
        for (int i = 0; i < myInventorySlots.length; i++) {
            JLabel slot = createSquareSlot();
            myInventorySlots[i] = slot;
            hudPanel.add(slot);
            hudPanel.add(Box.createHorizontalStrut(3));
        }

     // --- Center game area: graphical board using tiles ----
        String heroSpritePath;
        if (theHero instanceof Warrior) {
            heroSpritePath = "Dungeoneer_Characters/warrior_down.png";
        } else if (theHero instanceof Thief) {
            heroSpritePath = "Dungeoneer_Characters/thief_down.png";
        } else if (theHero instanceof Priestess) {
            heroSpritePath = "Dungeoneer_Characters/priestess_down.png";
        } else {
            heroSpritePath = "Dungeoneer_Characters/warrior_down.png";
        }

        myGamePanel = new DungeonBoardPanel(heroSpritePath);
        add(myGamePanel, BorderLayout.CENTER);

        // --- Text dialog bottom ---
        myLogArea = new JTextArea(4, 40);
        myLogArea.setEditable(false);
        myLogArea.setLineWrap(true);
        myLogArea.setWrapStyleWord(true);
        JScrollPane logScroll = new JScrollPane(myLogArea);

        // --- Frame layout ---
        setLayout(new BorderLayout());
        add(hudPanel, BorderLayout.NORTH);
        add(myGamePanel, BorderLayout.CENTER);
        add(logScroll, BorderLayout.SOUTH);

        setupControls();
        
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize HUD with current hero/dungeon state
        refreshHeroStats();
        refreshDungeonView();
    }
    
    private void setupControls() {
        myGamePanel.setFocusable(true);
        myGamePanel.requestFocusInWindow();

        myGamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                int dx = 0;
                int dy = 0;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_I -> {
                        // Show inventory popup (I only, for now)
                        showInventoryDialog();
                        return; // don't move the hero
                    }

                    case KeyEvent.VK_UP    -> dy = -1;
                    case KeyEvent.VK_DOWN  -> dy =  1;
                    case KeyEvent.VK_LEFT  -> dx = -1;
                    case KeyEvent.VK_RIGHT -> dx =  1;

                    // Optional WASD:
                    case KeyEvent.VK_W -> dy = -1;
                    case KeyEvent.VK_S -> dy =  1;
                    case KeyEvent.VK_A -> dx = -1;
                    case KeyEvent.VK_D -> dx =  1;

                    default -> { return; }
                }

                if (dx != 0 || dy != 0) {
                    myGamePanel.moveHero(dx, dy);
                }
            }
        });
    }

    /**
     * Creates a small square label with a border,
     * used for pillars and inventory slots.
     */
    private JLabel createSquareSlot() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(24, 24));
        label.setMinimumSize(new Dimension(24, 24));
        label.setMaximumSize(new Dimension(24, 24));
        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        return label;
    }

    // --- GameView interface methods ---

    @Override
    public void showMessage(final String theMessage) {
        myLogArea.append(theMessage + "\n");
        myLogArea.setCaretPosition(myLogArea.getDocument().getLength());
    }

    @Override
    public void showDungeon(final Dungeon theDungeon) {
        myGamePanel.repaint();  // redraw tiles
    }

    @Override 
    public void showHeroStats(final Hero theHero) {
        refreshHeroStats();
    }

    private void refreshHeroStats() {
        // These depend on your Hero API; adjust if field names differ
        int currentHp = myHero.getHitPoints();
        int maxHp = currentHp;  // TODO: replace with real max HP once we have it

        myHealthBar.setMaximum(maxHp);
        myHealthBar.setValue(currentHp);
        myHpLabel.setText("HP: " + currentHp + " / " + maxHp);

        myGoldLabel.setText("Gold: " + myHero.getGold());

        // TODO: update pillars & inventory slots from your Inventory class
        // For now just leave them empty boxes.
    }
    
    /**
     * Shows a simple inventory popup.
     * For now it just displays the hero's gold; later we can
     * list real items from the Inventory model.
     */
    private void showInventoryDialog() {
        InventoryDialog dialog = new InventoryDialog(this, myHero);
        dialog.setVisible(true);  // blocks until closed
    }

    private void refreshDungeonView() {
        // Later: custom drawing of dungeon/hero in myGamePanel.
        // For now we do nothing here.
    }
}