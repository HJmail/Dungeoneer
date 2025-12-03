package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;

import view.GameView;
import model.Hero;
import model.Priestess;
import model.Thief;
import model.Warrior;
import model.Inventory;
import model.Item;
import model.Weapon;
import model.Pillar;
import model.Potion;
import model.HealingPotion;
import model.VisionPotion;

import view.CountIcon;


/**
 * Inventory window shown when the player presses 'I'.
 * Layout roughly matches Cristian's inventory sketch.
 */
public final class InventoryDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    /** Hero reference (for gold, name, etc.). */
    private final Hero myHero;

    /** Optional model reference – may be null if not wired yet. */
    private final Inventory myInventory;
    
    /** Back-reference to the main game view (for HUD refresh). */
    private final GameView myGameView;

    /** Gold label so we could refresh later if needed. */
    private final JLabel myGoldLabel;

    /** "Pillars (#/4)" label so we can update the number. */
    private final JLabel myPillarsTitleLabel;

    /** Pillar slots (#/4). */
    private final JLabel[] myPillarSlots;

    /** Item slots (weapons / potions). */
    private final JLabel[] myItemSlots;

    /** Items currently shown in each slot (may be null). */
    private final Item[] myDisplayedItems;

    /** Which item slot is currently selected (-1 = none). */
    private int mySelectedIndex = -1;

    /** Info text area for selected item. */
    private final JTextArea myInfoArea;
    
 // Potion icons
    private final Icon myHealingPotionIcon;
    private final Icon myVisionPotionIcon;

    // Pillar icons
    private final Icon myAbsPillarIcon;
    private final Icon myEncapPillarIcon;
    private final Icon myInherPillarIcon;
    private final Icon myPolyPillarIcon;

    // Weapon icons for inventory dialog
    private final Icon myStickIcon;
    private final Icon mySpearIcon;
    private final Icon myFlailIcon;
    private final Icon myFalchionIcon;
    private final Icon myMorningStarIcon;

    /**
     * Old constructor: still works, just shows empty slots.
     */
    public InventoryDialog(final JFrame owner, final Hero theHero) {
        this(owner, theHero, null);
    }

    /**
     * New constructor: pass in Inventory to actually show items/pillars.
     */
    public InventoryDialog(final JFrame owner,
                           final Hero theHero,
                           final Inventory theInventory) {
    	

        super(owner, "Inventory", true);  // modal dialog
        
        if (owner instanceof GameView gv) {
            myGameView = gv;
        } else {
            myGameView = null;
        }
        
     // potions
        myHealingPotionIcon = loadItemIcon("Dungeoneer_Items/potion_healing.png");
        myVisionPotionIcon  = loadItemIcon("Dungeoneer_Items/potion_vision.png");

        // pillars
        myAbsPillarIcon   = loadItemIcon("Dungeoneer_Items/abstraction_pillar.png");
        myEncapPillarIcon = loadItemIcon("Dungeoneer_Items/encapsulation_pillar.png");
        myInherPillarIcon = loadItemIcon("Dungeoneer_Items/inheritance_Pillar.png");
        myPolyPillarIcon  = loadItemIcon("Dungeoneer_Items/polymorphism_pillar.png");

        // weapons
        myStickIcon       = loadItemIcon("Dungeoneer_Items/stick.png");
        mySpearIcon       = loadItemIcon("Dungeoneer_Items/spear.png");
        myFlailIcon       = loadItemIcon("Dungeoneer_Items/flail.png");
        myFalchionIcon    = loadItemIcon("Dungeoneer_Items/falchion.png");
        myMorningStarIcon = loadItemIcon("Dungeoneer_Items/morning_star.png");

        myHero = theHero;
        myInventory = theInventory;

        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- LEFT: character portrait + name ----
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout(0, 5));

        JPanel portraitPanel = new JPanel(new BorderLayout());
        portraitPanel.setPreferredSize(new Dimension(120, 200));
        portraitPanel.setBackground(new Color(230, 230, 230));
        portraitPanel.setBorder(BorderFactory.createTitledBorder("Character"));

        // Pick the correct sprite based on the hero class
        String spritePath = null;
        if (myHero instanceof Warrior) {
            spritePath = "Dungeoneer_Characters/warrior_down.png";
        } else if (myHero instanceof Thief) {
            spritePath = "Dungeoneer_Characters/thief_down.png";
        } else if (myHero instanceof Priestess) {
            spritePath = "Dungeoneer_Characters/priestess_down.png";
        }

        if (spritePath != null) {
            ImageIcon rawIcon = new ImageIcon(spritePath);
            int targetWidth  = 96;
            int targetHeight = 96;
            Image scaledImg = rawIcon.getImage().getScaledInstance(
                    targetWidth, targetHeight, Image.SCALE_SMOOTH);

            JLabel portraitLabel = new JLabel(new ImageIcon(scaledImg));
            portraitLabel.setHorizontalAlignment(JLabel.CENTER);
            portraitLabel.setVerticalAlignment(JLabel.CENTER);
            portraitPanel.add(portraitLabel, BorderLayout.CENTER);
        }

        leftPanel.add(portraitPanel, BorderLayout.CENTER);

        String heroName = (myHero != null) ? myHero.getName() : "Hero";
        JLabel nameLabel = new JLabel(heroName, JLabel.CENTER);
        leftPanel.add(nameLabel, BorderLayout.SOUTH);

        // ---- RIGHT: gold, pillars, items ----
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // Gold row
        JPanel goldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel goldTitle = new JLabel("Gold:");
        myGoldLabel = new JLabel(String.valueOf(myHero.getGold()));
        goldPanel.add(goldTitle);
        goldPanel.add(Box.createHorizontalStrut(10));
        goldPanel.add(myGoldLabel);

        // Pillars row
        JPanel pillarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        myPillarsTitleLabel = new JLabel("Pillars (0/4):");
        pillarPanel.add(myPillarsTitleLabel);
        pillarPanel.add(Box.createHorizontalStrut(10));

        myPillarSlots = new JLabel[4];
        for (int i = 0; i < myPillarSlots.length; i++) {
            JLabel slot = createSquareSlot(40);
            pillarPanel.add(slot);
            myPillarSlots[i] = slot;
        }

        // Items row (weapons / potions)
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        JLabel itemTitle = new JLabel("Items:");
        itemPanel.add(itemTitle);
        itemPanel.add(Box.createHorizontalStrut(10));

        myItemSlots = new JLabel[5]; // match your HUD count
        myDisplayedItems = new Item[myItemSlots.length];

        for (int i = 0; i < myItemSlots.length; i++) {
            final int index = i;
            JLabel slot = createSquareSlot(50);

            // Clicking still works – selects this slot and shows info.
            slot.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    handleItemSlotClicked(index);
                }
            });

            itemPanel.add(slot);
            myItemSlots[i] = slot;
        }

        // Add the rows to the right panel
        rightPanel.add(goldPanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(pillarPanel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(itemPanel);

        // ---- BOTTOM: description text area ----
        myInfoArea = new JTextArea(4, 40);
        myInfoArea.setEditable(false);
        myInfoArea.setLineWrap(true);
        myInfoArea.setWrapStyleWord(true);
        myInfoArea.setText("Weapon/Potion info will appear here when you select an item.");
        JScrollPane infoScroll = new JScrollPane(myInfoArea);

        // Put left & right into a main center panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));
        centerPanel.add(leftPanel, BorderLayout.WEST);
        centerPanel.add(rightPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(infoScroll, BorderLayout.SOUTH);

     // Fill UI from hero + (optional) inventory
        refreshFromModel();

        // Set up arrow-key navigation for items
        setupKeyNavigation();

        // NEW: allow closing the dialog with I or ESC
        setupCloseKeyBinding();

        pack();
        setLocationRelativeTo(owner);
        getRootPane().requestFocusInWindow();
    }

    /** Helper to create a square inventory/pillar slot. */
    private JLabel createSquareSlot(final int size) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(size, size));
        label.setMinimumSize(new Dimension(size, size));
        label.setMaximumSize(new Dimension(size, size));
        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(245, 245, 245));
        return label;
    }

    private void refreshFromModel() {
        // Gold always comes from the hero
        myGoldLabel.setText(String.valueOf(myHero.getGold()));

        if (myInventory == null) {
            myPillarsTitleLabel.setText("Pillars (0/4):");
            mySelectedIndex = -1;
            updateSelectionHighlight();
            return;
        }

        // All items currently in the inventory
        java.util.List<Item> allItems = myInventory.getInventory();

        // ---------- PILLARS ROW ----------
        // Clear all pillar slots first
        for (JLabel slot : myPillarSlots) {
            slot.setIcon(null);
            slot.setBackground(new Color(245, 245, 245));
        }

        int pillarCount = 0;
        for (Item item : allItems) {
            if (isPillar(item)) {
                if (pillarCount < myPillarSlots.length) {
                    JLabel slot = myPillarSlots[pillarCount];
                    slot.setBackground(new Color(210, 255, 210)); // light green

                    Icon icon = getIconForItem(item);
                    if (icon != null) {
                        slot.setIcon(icon);
                    }
                }
                pillarCount++;
            }
        }
        myPillarsTitleLabel.setText("Pillars (" + pillarCount + "/4):");

        // ---------- ITEMS ROW (no pillars here) ----------
        List<Item> nonPillarItems = new ArrayList<>();
        for (Item item : allItems) {
            if (!isPillar(item)) {
                nonPillarItems.add(item);
            }
        }

        for (int i = 0; i < myItemSlots.length; i++) {
            JLabel slot = myItemSlots[i];
            Item item = (i < nonPillarItems.size()) ? nonPillarItems.get(i) : null;
            myDisplayedItems[i] = item;

            // Clear old visuals
            slot.setIcon(null);
            slot.setText("");

            if (item == null) {
                slot.setToolTipText(null);
                slot.setBackground(new Color(245, 245, 245));
            } else {
                slot.setBackground(Color.WHITE);
                slot.setToolTipText(item.getDescription());

                Icon baseIcon = getIconForItem(item);
                Icon finalIcon = baseIcon;

                // Overlay "xN" for potions
                if (item instanceof Potion && myInventory != null) {
                    Map<String, Integer> stacks = myInventory.getPotionStacks();
                    String key = item.getClass().getSimpleName();
                    int count = stacks.getOrDefault(key, 1);
                    if (count > 1 && baseIcon != null) {
                        finalIcon = new CountIcon(baseIcon, count);
                    }
                }

                if (finalIcon != null) {
                    slot.setIcon(finalIcon);
                } else {
                    String simple = item.getClass().getSimpleName();
                    slot.setText(simple.substring(0, 1));
                }
            }
        }

        // Auto-select first non-empty *non-pillar* item
        mySelectedIndex = findFirstNonEmptyIndex();
        updateSelectionHighlight();
        updateInfoForSelected();
    }

    /** Called when the user clicks one of the item slots. */
    private void handleItemSlotClicked(final int index) {
        if (myInventory == null) {
            return;
        }
        if (myDisplayedItems[index] == null) {
            myInfoArea.setText("Empty slot.");
            return;
        }
        selectItemIndex(index);
    }

    /** Set up arrow key bindings: LEFT/UP = previous, RIGHT/DOWN = next. */
    /** Set up arrow key bindings: LEFT/UP = previous, RIGHT/DOWN = next. */
    private void setupKeyNavigation() {
        JComponent root = getRootPane();

        // Use the same InputMap for all bindings
        var im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        var am = root.getActionMap();

        // Arrow keys -> move selection
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,  0), "invLeft");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "invRight");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,    0), "invLeft");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,  0), "invRight");

        am.put("invLeft", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                moveSelection(-1);
            }
        });

        am.put("invRight", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                moveSelection(1);
            }
        });

        // Enter key to "use" the selected item
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "useSelected");
        am.put("useSelected", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                useSelectedItem();
            }
        });

        // Delete / Backspace to drop the selected item
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "dropSelected");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "dropSelected");
        am.put("dropSelected", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dropSelectedItem();
            }
        });
    }

    /** Move selection left/right (direction -1 or +1), wrapping across slots. */
    private void moveSelection(final int direction) {
        if (myInventory == null) {
            return;
        }

        if (mySelectedIndex == -1) {
            mySelectedIndex = findFirstNonEmptyIndex();
            updateSelectionHighlight();
            updateInfoForSelected();
            return;
        }

        int len = myDisplayedItems.length;
        if (len == 0) {
            return;
        }

        int start = mySelectedIndex;
        int idx = mySelectedIndex;

        do {
            idx = (idx + direction + len) % len;
            if (myDisplayedItems[idx] != null) {
                mySelectedIndex = idx;
                updateSelectionHighlight();
                updateInfoForSelected();
                return;
            }
        } while (idx != start);
    }
    
    /** Use the currently selected item (for now: potions only). */
    /** Use the currently selected item (for now: potions only). */
    private void useSelectedItem() {
        if (myInventory == null) return;
        if (mySelectedIndex < 0 || mySelectedIndex >= myDisplayedItems.length) return;

        Item item = myDisplayedItems[mySelectedIndex];
        if (!(item instanceof Potion)) {
            // Only potions are usable for now
            return;
        }

        // --- Special rule: don't use HealingPotion if HP is already full ---
        if (item instanceof HealingPotion) {
            int currentHp = myHero.getHitPoints();
            int maxHp     = myHero.getMaxHitPoints();

            if (currentHp >= maxHp) {
                myInfoArea.setText("Your health is already full. "
                        + "You don't need a Healing Potion right now.");
                return;  // don't consume the potion
            }
        }

        String key = item.getClass().getSimpleName();  // "HealingPotion", "VisionPotion", ...
        try {
            myInventory.useItem(key);   // consume 1 from the stack

            // Tell the main game view that hero stats changed (HP bar!)
            if (myGameView != null) {
                myGameView.showHeroStats(myHero);
            }

            refreshFromModel();         // rebuild icons & counts in the dialog
        } catch (Exception ex) {
            myInfoArea.setText("Could not use item: " + ex.getMessage());
        }
    }
    
    /**
     * Drops (discards) the currently selected item.
     * For potions, only one is removed from the stack.
     * Pillars cannot be dropped.
     */
    private void dropSelectedItem() {
        if (myInventory == null) return;
        if (mySelectedIndex < 0 || mySelectedIndex >= myDisplayedItems.length) return;

        Item item = myDisplayedItems[mySelectedIndex];
        if (item == null) return;

        // Don't allow dropping pillars
        if (item instanceof Pillar) {
            myInfoArea.setText("You cannot bring yourself to throw away a Pillar of OO.");
            return;
        }

        String key = item.getClass().getSimpleName();  // e.g. "HealingPotion"

        try {
            myInventory.dropItem(key);       // update model (stacks + list)

            // Refresh HUD in main window (gold / HP / inventory bar)
            if (myGameView != null) {
                myGameView.showHeroStats(myHero);
            }

            // Refresh the dialog’s own icons and counts
            refreshFromModel();

            myInfoArea.setText("You dropped a " + key + ".");
        } catch (Exception ex) {
            myInfoArea.setText("Could not drop item: " + ex.getMessage());
        }
    }

    /** Select a specific slot index and update UI. */
    private void selectItemIndex(final int index) {
        if (index < 0 || index >= myDisplayedItems.length) {
            return;
        }
        if (myDisplayedItems[index] == null) {
            return;
        }
        mySelectedIndex = index;
        updateSelectionHighlight();
        updateInfoForSelected();
    }

    /** Find first non-empty item slot index, or -1 if none. */
    private int findFirstNonEmptyIndex() {
        for (int i = 0; i < myDisplayedItems.length; i++) {
            if (myDisplayedItems[i] != null) {
                return i;
            }
        }
        return -1;
    }

    /** Update borders so the selected slot has a blue outline. */
    private void updateSelectionHighlight() {
        for (int i = 0; i < myItemSlots.length; i++) {
            JLabel slot = myItemSlots[i];
            if (i == mySelectedIndex && myDisplayedItems[i] != null) {
                slot.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            } else {
                slot.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            }
        }
    }

    /** Update the bottom info text based on the selected item. */
    private void updateInfoForSelected() {
        if (mySelectedIndex == -1 || myDisplayedItems[mySelectedIndex] == null) {
            myInfoArea.setText("Weapon/Potion info will appear here when you select an item.");
            return;
        }

        Item item = myDisplayedItems[mySelectedIndex];
        String displayName = item.getClass().getSimpleName();
        if (item instanceof Weapon weapon) {
            displayName = weapon.getName();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(displayName).append("\n\n");
        sb.append(item.getDescription());

        myInfoArea.setText(sb.toString());
    }
    
 // --- Helpers to choose an icon for each item type ---

    /** Decide which icon to use for an inventory item in the dialog. */
    private Icon getIconForItem(final Item item) {
        if (item instanceof HealingPotion) {
            return myHealingPotionIcon;   // whatever you called the cached icon
        }
        if (item instanceof VisionPotion) {
            return myVisionPotionIcon;
        }

        // --- Weapons: choose icon based on weapon name ---
        if (item instanceof Weapon weapon) {
            String name = weapon.getName().toLowerCase();

            if (name.contains("stick")) {
                return myStickIcon;
            } else if (name.contains("spear")) {
                return mySpearIcon;
            } else if (name.contains("flail")) {
                return myFlailIcon;
            } else if (name.contains("falchion")) {
                return myFalchionIcon;
            } else if (name.contains("morning")) {  // morning star
                return myMorningStarIcon;
            } else {
                // fallback: some default weapon icon
                return myMorningStarIcon;
            }
        }

        // --- Pillars by description text (same as before) ---
        String desc = item.getDescription().toLowerCase();
        if (desc.contains("abstraction")) {
            return myAbsPillarIcon;
        }
        if (desc.contains("encapsulation")) {
            return myEncapPillarIcon;
        }
        if (desc.contains("inheritance")) {
            return myInherPillarIcon;
        }
        if (desc.contains("polymorphism")) {
            return myPolyPillarIcon;
        }

        return null;
    }

    private Icon loadItemIcon(final String path) {
        ImageIcon raw = new ImageIcon(path);
        // scale a bit smaller than the slot (your item slots are 50x50)
        int size = 40;
        Image scaled = raw.getImage()
                          .getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
    
    /** True if this inventory item is one of the four OO pillars. */
    private boolean isPillar(final Item item) {
        if (item == null) {
            return false;
        }
        // If you have a Pillar class:
        if (item instanceof Pillar) {
            return true;
        }
        // Fallback using description text
        String desc = item.getDescription().toLowerCase();
        return desc.contains("pillar of");
    }
    
    /** Allow closing the inventory dialog by pressing I or ESC. */
    private void setupCloseKeyBinding() {
        JComponent root = getRootPane();

        // ESC works (key-pressed style)
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ESCAPE"), "closeInventory");

        // Use key-pressed VK_I instead of char 'I'
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "closeInventory");

        root.getActionMap().put("closeInventory", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dispose();  // closes the dialog
            }
        });
    }
}