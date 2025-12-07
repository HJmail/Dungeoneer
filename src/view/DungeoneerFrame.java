package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import model.Direction;
import model.Dungeon;
import model.DungeonTile;
import model.GameConfig;
import model.Gremlin;
import model.HealingPotion;
import model.Hero;
import model.Inventory;
import model.Item;
import model.Monster;
import model.Ogre;
import model.Pillar;
import model.Potion;
import model.Priestess;
import model.Rarity;
import model.Skeleton;
import model.Thief;
import model.VisionPotion;
import model.Warrior;
import model.Weapon;


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
    
  /** Random number generator used for item rarities and events. */
  private final Random myRandom = new Random();
  private ImageIcon myDeadIcon;
    
  // --- Cached HUD icons so we don't reload/scale images every time ---
  private final Icon myHudHealingIcon;
  private final Icon myHudVisionIcon;

  // one icon per weapon type
  private final Icon myHudStickIcon;
  private final Icon myHudSpearIcon;
  private final Icon myHudFlailIcon;
  private final Icon myHudFalchionIcon;
  private final Icon myHudMorningStarIcon;

  private final Icon myHudAbsPillarIcon;
  private final Icon myHudEncapPillarIcon;
  private final Icon myHudInherPillarIcon;
  private final Icon myHudPolyPillarIcon;
    
  // Portrait icons for dialogs
  private final Icon myHeroPortraitIcon;
  private final Icon myShopkeeperIcon;

    
  // Track max HP separately so the bar ratio is correct
  private final int myMaxHitPoints;
  private boolean myCanMove = true;

  // Center game area
  private final DungeonBoardPanel myGamePanel;

  // Text dialog
  private final JTextArea myLogArea;
  
  /** Size (in pixels) of the Dungeoneer logo in the Exit dialog. */
  private static final int EXIT_DIALOG_ICON_SIZE = 64;

  public DungeoneerFrame(final Dungeon theDungeon, final Hero theHero) {
    super("Dungeoneer");
    myDungeon = theDungeon;
    myHero = theHero;
    myMaxHitPoints = myHero.getHitPoints();  // starting HP = max HP
    myHero.getInventory().setView(this);

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
        String deadSpritePath;

        if (theHero instanceof Warrior) {
            heroSpritePath = "Dungeoneer_Characters/warrior_down.png";
            deadSpritePath = "Dungeoneer_Characters/warrior_dead.png";
        } else if (theHero instanceof Thief) {
            heroSpritePath = "Dungeoneer_Characters/thief_down.png";
            deadSpritePath = "Dungeoneer_Characters/thief_dead.png";
        } else if (theHero instanceof Priestess) {
            heroSpritePath = "Dungeoneer_Characters/priestess_down.png";
            deadSpritePath = "Dungeoneer_Characters/priestess_dead.png";
        } else {
            heroSpritePath = "Dungeoneer_Characters/warrior_down.png";
            deadSpritePath = "Dungeoneer_Characters/warrior_dead.png";
        }
        
        // Hero portrait for dialogs
        Image heroImg = new ImageIcon(heroSpritePath).getImage()
                .getScaledInstance(96, 96, Image.SCALE_SMOOTH);
        myHeroPortraitIcon = new ImageIcon(heroImg);

        // Shopkeeper face icon for dialogs
        Image shopImg = new ImageIcon("Dungeoneer_NPCs/shopkeeper_down.png")
                .getImage()
                .getScaledInstance(96, 96, Image.SCALE_SMOOTH);
        myShopkeeperIcon = new ImageIcon(shopImg);

        // store the icon for the dialog
        myDeadIcon = new ImageIcon(deadSpritePath);

        // pass BOTH paths into the board
        myGamePanel = new DungeonBoardPanel(heroSpritePath, deadSpritePath);
        
     // store a *scaled* icon for the dialog
        ImageIcon rawDeadIcon = new ImageIcon(deadSpritePath);

        // choose whatever target size you like (e.g. 128x128)
        int targetSize = 128;
        Image scaled = rawDeadIcon.getImage().getScaledInstance(
                targetSize, targetSize, Image.SCALE_SMOOTH);

        myDeadIcon = new ImageIcon(scaled);
        
     // Cache HUD icons (loaded & scaled once)
        myHudHealingIcon     = loadHudIcon("Dungeoneer_Items/potion_healing.png");
        myHudVisionIcon      = loadHudIcon("Dungeoneer_Items/potion_vision.png");

        // weapons
        myHudStickIcon       = loadHudIcon("Dungeoneer_Items/stick.png");
        myHudSpearIcon       = loadHudIcon("Dungeoneer_Items/spear.png");
        myHudFlailIcon       = loadHudIcon("Dungeoneer_Items/flail.png");
        myHudFalchionIcon    = loadHudIcon("Dungeoneer_Items/falchion.png");
        myHudMorningStarIcon = loadHudIcon("Dungeoneer_Items/morning_star.png");

        // pillars
        myHudAbsPillarIcon   = loadHudIcon("Dungeoneer_Items/abstraction_pillar.png");
        myHudEncapPillarIcon = loadHudIcon("Dungeoneer_Items/encapsulation_pillar.png");
        myHudInherPillarIcon = loadHudIcon("Dungeoneer_Items/inheritance_Pillar.png");
        myHudPolyPillarIcon  = loadHudIcon("Dungeoneer_Items/polymorphism_pillar.png");


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
        setResizable(false);
        setLocationRelativeTo(null);

        // We want to handle the close ourselves:
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Hook into the close (red X) event
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                confirmExitGame();
            }
        });

        // Initialize HUD with current hero/dungeon state
        refreshHeroStats();
        refreshDungeonView();
    }
  
  /** Ask the player if they want to save before exiting the game window. */
  private void confirmExitGame() {
      // Load & scale the Dungeoneer icon
      ImageIcon icon = null;
      try {
          Image img = new ImageIcon("Dungeoneer_Icon.png")
                  .getImage()
                  .getScaledInstance(64, 64, Image.SCALE_SMOOTH);
          icon = new ImageIcon(img);
      } catch (Exception ignored) {
          // If the icon fails, we'll just show text only
      }

      String message = "Do you want to save your progress?";
      String title   = "Exit Dungeoneer";

      String[] options = {
          "Cancel",
          "Exit without saving",
          "Save and Exit"
      };

      int choice = JOptionPane.showOptionDialog(
              this,
              message,
              title,
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.PLAIN_MESSAGE,
              icon,
              options,
              options[0]       // default = Cancel
      );

      if (choice == JOptionPane.CLOSED_OPTION || choice == 0) {
          // Cancel or closed dialog: do nothing, stay in game
          return;
      }

      if (choice == 2) { // "Save and Exit"
          saveGame();    // TODO: real save implementation
      }

      // Either "Exit without saving" or "Save and Exit"
      dispose();
      System.exit(0);
  }
  
  /**
   * Placeholder for saving the game.
   * TODO: hook this into your real save system.
   */
  private void saveGame() {
      // For now, just log to console or show a small message.
      System.out.println("Saving game... (not implemented yet)");
      // You could also show a quick dialog:
      // JOptionPane.showMessageDialog(this, "Game saved (stub).");
  }

    
    private void setupControls() {
        myGamePanel.setFocusable(true);
        myGamePanel.requestFocusInWindow();

        myGamePanel.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {

                if (!myCanMove) {
                    return;  // ignore input if dead / in respawn state
                }

                int dx = 0;
                int dy = 0;

                switch (e.getKeyCode()) {
                
                case KeyEvent.VK_C -> {
                    testCombatDialog();
                    return;
                }
                    case KeyEvent.VK_I -> {
                        // Show inventory popup
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
                    int oldX = myGamePanel.getHeroX();
                    int oldY = myGamePanel.getHeroY();
                    
                    // First: if we're trying to walk INTO the shopkeeper, open the dialog instead
                    if (myGamePanel.isShopkeeperAt(oldX + dx, oldY + dy)) {
                        checkShopkeeperInteraction(dx, dy);
                        return; // don't move onto the NPC tile
                    }

                    myGamePanel.moveHero(dx, dy);

                    boolean movedToNewTile =
                            (myGamePanel.getHeroX() != oldX
                             || myGamePanel.getHeroY() != oldY);

                    // --- PIT DAMAGE ---
                    if (movedToNewTile && myGamePanel.isHeroOnPit()) {

                        Random rand = new Random();
                        int damage = rand.nextInt(20) + 1;   // 1–20 damage

                        int newHp = myHero.getHitPoints() - damage;

                        // Apply damage first
                        myHero.setHitPoints(newHp);
                        refreshHeroStats();

                        if (newHp <= 0) {
                            // Clamp display to 0 and show death messages
                            showMessage(myHero.getName() + " fell into a pit and took "
                                        + damage + " damage! HP now: 0");
                            showMessage(myHero.getName() + " has died in the pit!");

                            handlePlayerDeath();   // this will set HP back to myMaxHitPoints
                            return;                // IMPORTANT: don't continue after respawn
                        } else {
                            // Still alive – just report the damage
                            showMessage(myHero.getName() + " fell into a pit and took "
                                        + damage + " damage! HP now: " + myHero.getHitPoints());
                        }
                    }
                    
                 // --- Item / pillar pickups (only if we’re still alive) ---
                    DungeonTile tile = myGamePanel.getTileUnderHero();

                    switch (tile) {
                    case GOLD -> {
                        myHero.addGold(25);
                        refreshHeroStats();
                        myGamePanel.clearTileUnderHero();
                        showMessage("You picked up some gold!");
                    }

                    case HEALING_POTION -> {
                        boolean added = myHero.getInventory().addItem(
                                new HealingPotion());

                        if (added) {
                            myGamePanel.clearTileUnderHero();
                            showMessage("You picked up a Healing Potion!");
                            refreshHudFromInventory();
                        } else {
                            showMessage("Your inventory is full. You leave the Healing Potion.");
                        }
                    }

                    case VISION_POTION -> {
                        boolean added = myHero.getInventory().addItem(
                                new VisionPotion());

                        if (added) {
                            myGamePanel.clearTileUnderHero();
                            showMessage("You picked up a Vision Potion!");
                            refreshHudFromInventory();
                        } else {
                            showMessage("Your inventory is full. You leave the Vision Potion.");
                        }
                    }

                    // any weapon tile (1–5 in the map)
                    case SPEAR, FALCHION, FLAIL, MORNING_STAR, STICK -> {
                        Rarity rarity = randomRarity();  // new helper in this class

                        Weapon weapon = switch (tile) {
                            case SPEAR        -> Weapon.createSpear(rarity);
                            case FALCHION     -> Weapon.createFalchion(rarity);
                            case FLAIL        -> Weapon.createFlail(rarity);
                            case MORNING_STAR -> Weapon.createMorningStar(rarity);
                            case STICK        -> Weapon.createStick(rarity);
                            default           -> new Weapon("Rusty Sword", 10, rarity);
                        };

                        boolean added = myHero.getInventory().addItem(weapon);

                        if (added) {
                            myHero.equipWeapon(weapon);
                            myGamePanel.clearTileUnderHero();
                            showMessage("You picked up a " 
                                        + weapon.getRarity() + " " + weapon.getName()
                                        + " (Damage " + weapon.getDamage() + ")!");
                            refreshHudFromInventory();
                        } else {
                            showMessage("Your inventory is full. You leave the "
                                        + weapon.getName() + ".");
                        }
                    }

                    case ABSTRACTION_PILLAR -> {
                        boolean added = myHero.getInventory().addItem(new Pillar('A'));
                        if (added) {
                            myGamePanel.clearTileUnderHero();
                            showMessage("You obtained the Pillar of Abstraction!");
                            refreshHudFromInventory();
                        } else {
                            showMessage("You already carry all the pillars you need.");
                        }
                    }

                    case ENCAPSULATION_PILLAR -> {
                        boolean added = myHero.getInventory().addItem(new Pillar('E'));
                        if (added) {
                            myGamePanel.clearTileUnderHero();
                            showMessage("You obtained the Pillar of Encapsulation!");
                            refreshHudFromInventory();
                        } else {
                            showMessage("You already carry all the pillars you need.");
                        }
                    }

                    case INHERITANCE_PILLAR -> {
                        boolean added = myHero.getInventory().addItem(new Pillar('I'));
                        if (added) {
                            myGamePanel.clearTileUnderHero();
                            showMessage("You obtained the Pillar of Inheritance!");
                            refreshHudFromInventory();
                        } else {
                            showMessage("You already carry all the pillars you need.");
                        }
                    }

                    case POLYMORPHISM_PILLAR -> {
                        boolean added = myHero.getInventory().addItem(new Pillar('P'));
                        if (added) {
                            myGamePanel.clearTileUnderHero();
                            showMessage("You obtained the Pillar of Polymorphism!");
                            refreshHudFromInventory();
                        } else {
                            showMessage("You already carry all the pillars you need.");
                        }
                    }
                    
                    case EXIT -> {
                        handleExitTile();
                    }

                    default -> {
                        // nothing special on this tile
                    }
                }

              } // end if dx/dy != 0
            }     // end keyPressed
        });        // end addKeyListener
    }


    /**
     * Called whenever the hero steps onto the Exit tile.
     * Shows a win dialog if all four Pillars are collected,
     * otherwise lists which pillars remain – with 4 pillar slots.
     */
    private void handleExitTile() {
        Inventory inv = myHero.getInventory();
        if (inv == null) {
            return;
        }

        // ---------------- Shared UI pieces ----------------
        // Dungeoneer icon on the left
        JLabel iconLabel = null;
        try {
            Image img = new ImageIcon("Dungeoneer_Icon.png")
                    .getImage()
                    .getScaledInstance(EXIT_DIALOG_ICON_SIZE,
                                       EXIT_DIALOG_ICON_SIZE,
                                       Image.SCALE_SMOOTH);
            iconLabel = new JLabel(new ImageIcon(img));
        } catch (Exception ignored) {
            // If it fails, we'll just skip the icon
        }

        // Build the row of 4 pillar slots (same look as HUD)
        JPanel pillarsRow = new JPanel();
        pillarsRow.setLayout(new BoxLayout(pillarsRow, BoxLayout.X_AXIS));
        pillarsRow.add(new JLabel("Pillars: "));
        pillarsRow.add(Box.createHorizontalStrut(5));

        addPillarSlot(pillarsRow, inv.hasAbstractionPillar(),   myHudAbsPillarIcon);
        addPillarSlot(pillarsRow, inv.hasEncapsulationPillar(), myHudEncapPillarIcon);
        addPillarSlot(pillarsRow, inv.hasInheritancePillar(),   myHudInherPillarIcon);
        addPillarSlot(pillarsRow, inv.hasPolymorphismPillar(),  myHudPolyPillarIcon);

        // ---------------- Winning vs locked text ----------------
        boolean hasAll = inv.canExit();

        String text;
        String title;
        if (hasAll) {
            text =
                "You step onto the Exit carrying all four Pillars of OO!\n"
              + "The magical barrier fades and the way out opens.\n\n"
              + "Congratulations, " + myHero.getName() + " you escaped the dungeon!";
            title = "You Win!";
        } else {
            int collected = inv.getCollectedPillarsCount();
            text =
                "A shimmering barrier blocks your path...\n"
              + "You must collect all four Pillars of OO to leave!\n\n"
              + "Pillars collected: " + collected + "/4";
            title = "Exit Locked";
        }

     // Use fixed rows/columns so the dialog packs at a nice size
        JTextArea textArea = new JTextArea(5, 28);
        textArea.setText(text);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Main panel: icon on the left, text in center, pillar slots below
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(textArea);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(pillarsRow);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        if (iconLabel != null) {
            root.add(iconLabel, BorderLayout.WEST);
        }
        root.add(centerPanel, BorderLayout.CENTER);

        JOptionPane optionPane = new JOptionPane(
                root,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION
        );

        // Build a pinned dialog from the option pane
        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setModal(true);
        dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        dialog.setAlwaysOnTop(true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        dialog.setVisible(true);  // blocks until the player clicks OK/close

        // If all pillars collected, end the game after showing dialog
        if (hasAll) {
            gameOver();
        } else {
            //  Still playing: restore focus so movement works immediately
            refocusGamePanel();
        }
    }
    
    /**
     * Adds a single pillar slot to the given panel.
     * If the pillar is found, shows the pillar icon.
     * If missing, shows an empty bordered slot.
     */
    private void addPillarSlot(final JPanel parent,
                               final boolean found,
                               final Icon pillarIcon) {

        JLabel slot = createSquareSlot();   // same size / border as HUD slots

        if (found && pillarIcon != null) {
            slot.setIcon(pillarIcon);
        } else {
            // keep it empty; the border + empty slot visually means "missing"
            slot.setIcon(null);
        }

        parent.add(slot);
        parent.add(Box.createHorizontalStrut(3));
    }
    
 // default HUD size 24x24
    private JLabel createSquareSlot() {
        return createSquareSlot(24);
    }

    private JLabel createSquareSlot(final int size) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(size, size));
        label.setMinimumSize(new Dimension(size, size));
        label.setMaximumSize(new Dimension(size, size));
        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setOpaque(false); // key: no filled background
        return label;
    }
    
    private void checkShopkeeperInteraction(final int dx, final int dy) {
        int heroX = myGamePanel.getHeroX();
        int heroY = myGamePanel.getHeroY();
        int targetX = heroX + dx;
        int targetY = heroY + dy;

        // Only trigger if the tile we're trying to walk into IS the shopkeeper
        if (!myGamePanel.isShopkeeperAt(targetX, targetY)) {
            return;
        }

        // Face hero toward that direction (without moving)
        myGamePanel.faceHeroTowards(dx, dy);
        myGamePanel.updateShopkeeperFacing();

        // --- pinned confirm dialog using JOptionPane ---
        JOptionPane pane = new JOptionPane(
                "Do you want to interact with the shopkeeper?",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                myShopkeeperIcon
        );

        JDialog dialog = pane.createDialog(this, "Shopkeeper");
        dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        dialog.setAlwaysOnTop(true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setVisible(true);   // blocks here until Yes/No is clicked

        Object value = pane.getValue();
        int choice = (value instanceof Integer)
                ? (Integer) value
                : JOptionPane.CLOSED_OPTION;

        if (choice == JOptionPane.YES_OPTION) {
            // Open the full shop GUI
            ShopDialog shopDialog = new ShopDialog(
                    this,
                    myHero,
                    myHeroPortraitIcon,
                    myShopkeeperIcon
            );
            shopDialog.setLocationRelativeTo(this);
            shopDialog.setVisible(true);     // blocks until closed

            // After shop closes, refresh HUD
            refreshHeroStats();
            myGamePanel.repaint();
            refocusGamePanel();
        }
    }
  
    /** Random rarity helper for weapons picked up on the map. */
    private Rarity randomRarity() {
        int roll = myRandom.nextInt(100); // or new Random() if you prefer
        if (roll < 50) return Rarity.COMMON;
        if (roll < 80) return Rarity.UNCOMMON;
        if (roll < 95) return Rarity.RARE;
        if (roll < 98) return Rarity.EPIC; 
        return Rarity.LEGENDARY;
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
    
    /** Convenience: refresh both pillar + inventory icons in the HUD. */
    private void refreshHudFromInventory() {
        refreshPillarHUD();
        refreshInventoryHUD();
    }

    public void refreshHeroStats() {
        int currentHp = myHero.getHitPoints();
        int maxHp = myMaxHitPoints;

        if (currentHp < 0) currentHp = 0;
        if (currentHp > maxHp) currentHp = maxHp;

        myHealthBar.setMaximum(maxHp);
        myHealthBar.setValue(currentHp);
        myHpLabel.setText("HP: " + currentHp + " / " + maxHp);

        myGoldLabel.setText("Gold: " + myHero.getGold());

        // keep HUD icons up-to-date
        refreshPillarHUD();
        refreshInventoryHUD();
    }
    
    /** Updates the top HUD pillar slots based on the hero's inventory. */
    private void refreshPillarHUD() {
        Inventory inv = myHero.getInventory();
        // Clear all pillar slots first
        for (JLabel slot : myPillarSlots) {
            slot.setIcon(null);
            
            //slot.setBackground(Color.WHITE);
        }
        if (inv == null) {
            return;
        }

        List<Item> allItems = inv.getInventory();
        int pillarIndex = 0;

        for (Item item : allItems) {
            if (isPillarItem(item) && pillarIndex < myPillarSlots.length) {
                JLabel slot = myPillarSlots[pillarIndex];
                Icon icon = getHudIconForItem(item);
                if (icon != null) {
                    slot.setIcon(icon);
                }
               // slot.setBackground(new Color(210, 255, 210)); // light green
                pillarIndex++;
            }
        }
    }

    /** Updates the top HUD inventory slots (right side) with non-pillar items. */
    private void refreshInventoryHUD() {
        Inventory inv = myHero.getInventory();

        // Clear all slots if no inventory
        if (inv == null) {
            for (JLabel slot : myInventorySlots) {
                slot.setIcon(null);
                slot.setText("");
            }
            return;
        }

        List<Item> allItems = inv.getInventory();

        // Build list of non-pillar items
        List<Item> nonPillars = new ArrayList<>();
        for (Item item : allItems) {
            if (!isPillarItem(item)) {
                nonPillars.add(item);
            }
        }

        for (int i = 0; i < myInventorySlots.length; i++) {
            JLabel slot = myInventorySlots[i];
            slot.setIcon(null);
            slot.setText("");  // clear old text

            if (i < nonPillars.size()) {
                Item item = nonPillars.get(i);

                Icon baseIcon = getHudIconForItem(item);
                Icon finalIcon = baseIcon;

                // Overlay xN for potions using inventory stack counts
                if (item instanceof Potion) {
                    Map<String, Integer> stacks = inv.getPotionStacks();
                    String key = item.getClass().getSimpleName();
                    int count = stacks.getOrDefault(key, 1);
                    if (count > 1 && baseIcon != null) {
                        finalIcon = new CountIcon(baseIcon, count);
                    }
                }

                if (finalIcon != null) {
                    slot.setIcon(finalIcon);
                }

                slot.setBackground(Color.WHITE);
            }
        }
    }
    
    /** 
     * Ensures the dungeon board has keyboard focus after a dialog closes.
     */
    private void refocusGamePanel() {
        SwingUtilities.invokeLater(() -> {
            if (myGamePanel != null) {
                myGamePanel.requestFocusInWindow();
                myGamePanel.grabFocus();
            }
        });
    }
    
    /**
     * Shows a simple inventory popup.
     * For now it just displays the hero's gold; later we can
     * list real items from the Inventory model.
     */
    private void showInventoryDialog() {
        InventoryDialog dialog =
            new InventoryDialog(this, myHero, myHero.getInventory());
        dialog.setVisible(true);  // blocks until closed
        
        refocusGamePanel();
    }

    private void refreshDungeonView() {
        // Later: custom drawing of dungeon/hero in myGamePanel.
        // For now we do nothing here.
    }
    
    /** Shows a pinned Respawn/Exit dialog and returns 0=Respawn, 1=Exit. */
    private int showPinnedDeathDialog(final String message) {
        String[] options = {"Respawn", "Exit"};

        JOptionPane pane = new JOptionPane(
                message,
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                myDeadIcon,
                options,
                options[0]
        );

        JDialog dialog = pane.createDialog(this, "You Died");
        dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        dialog.setAlwaysOnTop(true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setVisible(true);  // blocks until a button is clicked

        Object value = pane.getValue();
        int choice = 1; // default to "Exit" just in case

        if (value != null) {
            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(value)) {
                    choice = i;
                    break;
                }
            }
        }
        return choice; // 0 = Respawn, 1 = Exit
    }

    
    public void handlePlayerDeath() {
        myCanMove = false;  // stop further movement
        myGamePanel.setHeroDead(true);  // show dead sprite on the board

        String message = myHero.getName()
                + " has fallen into a pit and died.\n"
                + "You will respawn at the entrance or you can exit the dungeon.";
        
        int choice = showPinnedDeathDialog(message);  // <-- use helper

        if (choice == 0) { // Respawn
            myHero.setHitPoints(myMaxHitPoints);
            refreshHeroStats();

            myGamePanel.resetHeroToEntrance();
            myGamePanel.setHeroDead(false);   // back to normal sprite

            showMessage("You respawn at the dungeon entrance.");
            myCanMove = true;
        } else {
            dispose();
            System.exit(0);
        }
    }
    
 // Called ONLY when hero dies in combat
    public void handleHeroDeathFromCombat(final String killerName) {
        myCanMove = false;
        myGamePanel.setHeroDead(true);

        String message = myHero.getName()
                + " was slain by " + killerName + ".\n"
                + "You will respawn at the entrance or you can exit the dungeon.";
        
        int choice = showPinnedDeathDialog(message);  // <-- use helper

        if (choice == 0) { // Respawn
            myHero.setHitPoints(myMaxHitPoints);
            refreshHeroStats();

            myGamePanel.resetHeroToEntrance();
            myGamePanel.setHeroDead(false);

            showMessage("You respawn at the dungeon entrance.");
            myCanMove = true;
        } else {
            dispose();
            System.exit(0);
        }
    }
    

    
 // === HUD helper methods ============================================

    /** Is this inventory item one of the four OO pillars? */
    private boolean isPillarItem(final Item item) {
        if (item == null) return false;
        if (item instanceof Pillar) return true;

        String desc = item.getDescription().toLowerCase();
        return desc.contains("pillar of");
    }

    /** Load a small icon for the HUD (24x24). */
    private Icon loadHudIcon(final String path) {
        ImageIcon raw = new ImageIcon(path);
        int size = 24;
        Image scaled = raw.getImage()
                          .getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /** Decide which icon to use for a given item in the HUD. */
    private Icon getHudIconForItem(final Item item) {
        if (item instanceof HealingPotion) {
            return myHudHealingIcon;
        }
        if (item instanceof VisionPotion) {
            return myHudVisionIcon;
        }

        // --- Weapon -> specific weapon icon ---
        if (item instanceof Weapon weapon) {
            String name = weapon.getName().toLowerCase();

            if (name.contains("stick"))        return myHudStickIcon;
            if (name.contains("spear"))        return myHudSpearIcon;
            if (name.contains("flail"))        return myHudFlailIcon;
            if (name.contains("falchion"))     return myHudFalchionIcon;
            if (name.contains("morning star")) return myHudMorningStarIcon;
        }

        // pillars by description text
        String desc = item.getDescription().toLowerCase();
        if (desc.contains("abstraction")) {
            return myHudAbsPillarIcon;
        }
        if (desc.contains("encapsulation")) {
            return myHudEncapPillarIcon;
        }
        if (desc.contains("inheritance")) {
            return myHudInherPillarIcon;
        }
        if (desc.contains("polymorphism")) {
            return myHudPolyPillarIcon;
        }

        return null;
    }
    
    @Override
    public Direction askDirection() {
        // GUI version does not use console-based direction input.
        // Movement is handled by key listeners instead.
        return null;
    }
    
 // ---------------------------------------------------------------------
 // GameView REQUIRED METHODS (GUI implementations)
 // ---------------------------------------------------------------------

    @Override
    public GameConfig askGameConfig() {
        // For now, return a simple default config:
        // difficulty = 1, seed = random
        long seed = System.currentTimeMillis();
        return new GameConfig(myHero, 1, seed);
    }

 @Override
 public void showShopItems(final ArrayList<Item> theItems) {
     StringBuilder sb = new StringBuilder("Shop Inventory:\n");
     for (int i = 0; i < theItems.size(); i++) {
         Item item = theItems.get(i);
         sb.append(i + 1).append(". ").append(item.getDescription()).append("\n");
     }

     JOptionPane.showMessageDialog(
             this,
             sb.toString(),
             "Shopkeeper's Wares",
             JOptionPane.INFORMATION_MESSAGE
     );
 }

 @Override
 public int askShop() {
     String input = JOptionPane.showInputDialog(
             this,
             "Enter the number of the item you want to buy (or 0 to exit):",
             "Shop",
             JOptionPane.QUESTION_MESSAGE
     );

     if (input == null) return 0; // user canceled
     try {
         return Integer.parseInt(input.trim());
     } catch (NumberFormatException e) {
         return 0;
     }
 }

 @Override
 public void showPit(final int thePitDmg) {
     JOptionPane.showMessageDialog(
             this,
             "You fell into a pit and took " + thePitDmg + " damage!",
             "Pit Damage",
             JOptionPane.WARNING_MESSAGE
     );
 }

 @Override
 public void showPillar(final char theChar) {
     JOptionPane.showMessageDialog(
             this,
             "You found the Pillar of " + pillarName(theChar) + "!",
             "Pillar Found",
             JOptionPane.INFORMATION_MESSAGE
     );
 }

 private String pillarName(final char c) {
     return switch (Character.toUpperCase(c)) {
         case 'A' -> "Abstraction";
         case 'E' -> "Encapsulation";
         case 'I' -> "Inheritance";
         case 'P' -> "Polymorphism";
         default   -> "Unknown Pillar";
     };
 }

 public void gameOver() {
	    // Load and scale the Dungeoneer icon for the dialog
	    ImageIcon icon = null;
	    try {
	        Image img = new ImageIcon("Dungeoneer_Icon.png")
	                .getImage()
	                .getScaledInstance(64, 64, Image.SCALE_SMOOTH);
	        icon = new ImageIcon(img);
	    } catch (Exception ignored) {
	        // If loading fails, we'll just show the text without a custom icon
	    }

	    String message = "Game Over! Thanks for playing!";

	    if (icon != null) {
	        JOptionPane.showMessageDialog(
	                this,
	                message,
	                "Game Over",
	                JOptionPane.PLAIN_MESSAGE,  // no Duke icon
	                icon
	        );
	    } else {
	        JOptionPane.showMessageDialog(
	                this,
	                message,
	                "Game Over",
	                JOptionPane.PLAIN_MESSAGE
	        );
	    }

	    dispose();
	    System.exit(0);
	}
 
   private List<Monster> createRandomMonsters() {
	    Random rand = new Random();
	    int count = 1 + rand.nextInt(3); // 1–3 monsters

	    List<Monster> list = new java.util.ArrayList<>();

	    for (int i = 0; i < count; i++) {
	        int which = rand.nextInt(3); // 0=Gremlin, 1=Ogre, 2=Skeleton
	        Monster m;
	        switch (which) {
	            case 0 -> m = new Gremlin(
	                    "Gremlin",
	                    70,   // hp
	                    15,   // min dmg
	                    30,   // max dmg
	                    4,    // attack speed
	                    0.8,  // chance to hit
	                    0.4,  // chance to heal
	                    20,   // min heal
	                    40    // max heal
	            );
	            case 1 -> m = new Ogre(
	                    "Ogre",
	                    120,
	                    25,
	                    45,
	                    3,
	                    0.7,
	                    0.3,
	                    30,
	                    50
	            );
	            default -> m = new Skeleton(
	                    "Skeleton",
	                    60,
	                    10,
	                    20,
	                    5,
	                    0.75,
	                    0.25,
	                    15,
	                    30
	            );
	        }
	        list.add(m);
	    }
	    return list;
	}

   private void testCombatDialog() {
	    List<Monster> monsters = createRandomMonsters();

	    CombatDialog dialog = new CombatDialog(this, myHero, monsters);
	    dialog.setVisible(true);   // <-- blocks until player closes the battle window

	    // ===== After combat: sync HUD with current hero + inventory state =====
	    refreshHeroStats();        // updates HP bar, gold, pillars, inventory icons
	    refreshDungeonView();      // optional: redraw board if needed
	    myGamePanel.repaint();     // extra-safe repaint of the tile panel
	    refocusGamePanel();
	}
}