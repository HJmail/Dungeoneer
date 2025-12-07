package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

import model.Hero;
import model.Inventory;
import model.Item;
import model.Rarity;
import model.Weapon;
import model.HealingPotion;
import model.VisionPotion;
import model.Potion;

/**
 * Shopkeeper GUI dialog.
 *
 * Main screen:
 *  - Shows hero portrait, shopkeeper sprite.
 *  - Shows current gold.
 *  - Buttons: Gamble, Offers.
 *
 * Each button switches to a different "card" view inside the dialog:
 *  - Offers: list of offers (simple random items for gold).
 *  - Gamble: pay gold to roll for a random reward.
 *
 * Bottom area: hero inventory row + text log.
 */
public class ShopDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final Hero myHero;
    private final Icon myHeroIcon;
    private final Icon myShopIcon;
    
 // Small HUD-style icons for the inventory row
    private final Icon myHudHealingIcon;
    private final Icon myHudVisionIcon;
    private final Icon myHudStickIcon;
    private final Icon myHudSpearIcon;
    private final Icon myHudFlailIcon;
    private final Icon myHudFalchionIcon;
    private final Icon myHudMorningStarIcon;
    private final Icon myHudAbsPillarIcon;
    private final Icon myHudEncapPillarIcon;
    private final Icon myHudInherPillarIcon;
    private final Icon myHudPolyPillarIcon;
    private final Icon myGambleIcon;
    private final Icon myOffersIcon;
    private final Icon myNoGoldIcon;
    private final Icon myInvFullIcon;



    private final JLabel myGoldLabel;
    private final JTextArea myTextArea;

    private final CardLayout myCardLayout;
    private final JPanel myCenterCards;

    private final Random myRand = new Random();

    // Offers
    private final List<Offer> myTradeOffers = new ArrayList<>();
    private JButton[] myTradeButtons;

    // Inventory row at the bottom of the dialog
    private final JLabel[] myInventorySlots = new JLabel[5];
    
    private int mySelectedInventoryIndex = -1;   // which slot is selected, -1 = none

    /** Represents a shop offer: item + price. */
    private static class Offer {
        final Item item;
        final int price;
        Offer(final Item theItem, final int thePrice) {
            item = theItem;
            price = thePrice;
        }
    }
    
    public ShopDialog(final JFrame owner,
                      final Hero hero,
                      final Icon heroIcon,
                      final Icon shopIcon) {
         super(owner, "Shop", true);
         
         setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
         setAlwaysOnTop(true);
         setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
         
         myHero = hero;
         myHeroIcon = heroIcon;
         myShopIcon = shopIcon;
         
         setLayout(new BorderLayout(8, 8));
         setDefaultCloseOperation(DISPOSE_ON_CLOSE);
         setMinimumSize(new Dimension(600, 450));

         // --- NEW: load UI icons (adjust paths to match your project) ---
         myGambleIcon = loadHudIcon("Dungeoneer_icons/gamble_icon.png");
         myOffersIcon = loadHudIcon("Dungeoneer_icons/offers_icon.png");
         myNoGoldIcon = loadHudIcon("Dungeoneer_icons/no_gold_icon.png");
         myInvFullIcon = loadDialogIcon("Dungeoneer_icons/inventory_full_icon.png");

         // --- 1) INIT ICONS FIRST ---
         myHudHealingIcon     = loadHudIcon("Dungeoneer_Items/potion_healing.png");
         myHudVisionIcon      = loadHudIcon("Dungeoneer_Items/potion_vision.png");

         myHudStickIcon       = loadHudIcon("Dungeoneer_Items/stick.png");
         myHudSpearIcon       = loadHudIcon("Dungeoneer_Items/spear.png");
         myHudFlailIcon       = loadHudIcon("Dungeoneer_Items/flail.png");
         myHudFalchionIcon    = loadHudIcon("Dungeoneer_Items/falchion.png");
         myHudMorningStarIcon = loadHudIcon("Dungeoneer_Items/morning_star.png");

         myHudAbsPillarIcon   = loadHudIcon("Dungeoneer_Items/abstraction_pillar.png");
         myHudEncapPillarIcon = loadHudIcon("Dungeoneer_Items/encapsulation_pillar.png");
         myHudInherPillarIcon = loadHudIcon("Dungeoneer_Items/inheritance_Pillar.png");
         myHudPolyPillarIcon  = loadHudIcon("Dungeoneer_Items/polymorphism_pillar.png");

         // --- 2) BUILD UI LAYOUT ---
         setLayout(new BorderLayout(8, 8));
         setDefaultCloseOperation(DISPOSE_ON_CLOSE);
         setMinimumSize(new Dimension(600, 450));

         JPanel top = new JPanel();
         top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
         top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
         
         top.add(new JLabel("Gold: "));
         myGoldLabel = new JLabel();
         top.add(myGoldLabel);
         top.add(Box.createHorizontalGlue());
         
         add(top, BorderLayout.NORTH);

         myCardLayout = new CardLayout();
         myCenterCards = new JPanel(myCardLayout);
         
         myCenterCards.add(createMainMenuPanel(), "MAIN");
         myCenterCards.add(createTradePanel(), "OFFERS");
         myCenterCards.add(createGamblePanel(), "GAMBLE");
         
         add(myCenterCards, BorderLayout.CENTER);

         myTextArea = new JTextArea(3, 40);
         myTextArea.setLineWrap(true);
         myTextArea.setWrapStyleWord(true);
         myTextArea.setEditable(false);
         myTextArea.setBorder(BorderFactory.createTitledBorder("Text Dialog"));
         
         JScrollPane scroll = new JScrollPane(myTextArea);
         scroll.setPreferredSize(new Dimension(100, 90));

         JPanel bottomPanel = new JPanel(new BorderLayout());
         bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

         // --- inventory row + drop button in one line ---
         JPanel invRowPanel = new JPanel(new BorderLayout());
         invRowPanel.add(createInventoryPanel(), BorderLayout.CENTER);

         JButton dropButton = new JButton("Drop Selected");
         dropButton.addActionListener(e -> dropSelectedItem());
         invRowPanel.add(dropButton, BorderLayout.EAST);

         bottomPanel.add(invRowPanel, BorderLayout.NORTH);
         bottomPanel.add(scroll, BorderLayout.CENTER);

         add(bottomPanel, BorderLayout.SOUTH);

         refreshGoldLabel();
         refreshInventoryRow();
         appendText(myHero.getName() + " interacts with the shopkeeper!");

         pack(); 
       }
    
    /** Drops the currently selected non-pillar item from the hero's inventory. */
    private void dropSelectedItem() {
        if (mySelectedInventoryIndex < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "No item selected to drop.",
                    "Drop Item",
                    JOptionPane.PLAIN_MESSAGE   // <- no icon
            );
            return;
        }

        Item toDrop = getDisplayItemAt(mySelectedInventoryIndex);
        if (toDrop == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "That slot is empty.",
                    "Drop Item",
                    JOptionPane.PLAIN_MESSAGE   // <- no icon
            );
            mySelectedInventoryIndex = -1;
            updateInventorySelectionBorders();
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Drop " + toDrop.getName() + "?",
                "Drop Item",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE      // <- no icon
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        Inventory inv = myHero.getInventory();
        inv.dropItem(toDrop);

        appendText("You dropped " + toDrop.getName() + " in the shop.");
        mySelectedInventoryIndex = -1;
        refreshInventoryRow();
    }

    
    private Icon loadDialogIcon(final String path) {
        ImageIcon raw = new ImageIcon(path);
        int size = 64; // popup size
        Image scaled = raw.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // -----------------------------------------------------------------
    // MAIN MENU PANEL
    // -----------------------------------------------------------------
    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left: hero portrait
        JPanel heroPanel = new JPanel(new BorderLayout());
        heroPanel.setBorder(BorderFactory.createTitledBorder("Hero"));
        heroPanel.add(new JLabel(myHeroIcon), BorderLayout.CENTER);
        panel.add(heroPanel, BorderLayout.WEST);

        // Center: buttons (Gamble / Offers) – centered both ways
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton gambleBtn = new JButton("Gamble", myGambleIcon);
        JButton offersBtn = new JButton("Offers", myOffersIcon);

        // put text below the icon (looks nice for big buttons)
        gambleBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
        gambleBtn.setVerticalTextPosition(SwingConstants.CENTER);
        offersBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
        offersBtn.setVerticalTextPosition(SwingConstants.CENTER);

        gambleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        offersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        gambleBtn.addActionListener(e -> {
            myCardLayout.show(myCenterCards, "GAMBLE");
            appendText("You decide to gamble with the shopkeeper.");
        });

        offersBtn.addActionListener(e -> {
            generateTradeOffers();
            updateTradeButtons();
            myCardLayout.show(myCenterCards, "OFFERS");
            appendText("You browse the shopkeeper's offers.");
        });

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(gambleBtn);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(offersBtn);
        buttonPanel.add(Box.createVerticalGlue());

        panel.add(buttonPanel, BorderLayout.CENTER);

        // Right: shopkeeper sprite
        JPanel shopPanel = new JPanel(new BorderLayout());
        shopPanel.setBorder(BorderFactory.createTitledBorder("Shopkeeper"));
        shopPanel.add(new JLabel(myShopIcon), BorderLayout.CENTER);
        panel.add(shopPanel, BorderLayout.EAST);

        return panel;
    }

    // -----------------------------------------------------------------
    // OFFERS (TRADE) PANEL
    // -----------------------------------------------------------------
    private JPanel createTradePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel center = new JPanel(new GridLayout(3, 1, 5, 5));
        center.setBorder(BorderFactory.createTitledBorder("Shopkeeper's Offers"));

        myTradeButtons = new JButton[3];
        for (int i = 0; i < myTradeButtons.length; i++) {
            final int index = i;
            JButton btn = new JButton("Empty Slot");
            btn.addActionListener(e -> buyOffer(index));
            myTradeButtons[i] = btn;
            center.add(btn);
        }

        panel.add(center, BorderLayout.CENTER);

        // Right: rarity label (simple explanation)
        JPanel rarityPanel = new JPanel();
        rarityPanel.setLayout(new BoxLayout(rarityPanel, BoxLayout.Y_AXIS));
        rarityPanel.setBorder(BorderFactory.createTitledBorder("Rarity"));

        rarityPanel.add(new JLabel("Common"));
        rarityPanel.add(new JLabel("Uncommon"));
        rarityPanel.add(new JLabel("Rare"));
        rarityPanel.add(new JLabel("Epic"));
        rarityPanel.add(new JLabel("Legendary"));

        panel.add(rarityPanel, BorderLayout.EAST);

        // Bottom: back / okay
        JPanel bottom = new JPanel();
        JButton back = new JButton("Back");
        JButton ok   = new JButton("Okay");

        back.addActionListener(e -> myCardLayout.show(myCenterCards, "MAIN"));
        ok.addActionListener(e -> dispose());

        bottom.add(back);
        bottom.add(ok);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void generateTradeOffers() {
        myTradeOffers.clear();
        for (int i = 0; i < 3; i++) {
            Item item = randomShopItem();
            int price = 20 + myRand.nextInt(41); // 20–60 gold
            myTradeOffers.add(new Offer(item, price));
        }
    }

    private void updateTradeButtons() {
        for (int i = 0; i < myTradeButtons.length; i++) {
            JButton btn = myTradeButtons[i];
            if (i < myTradeOffers.size()) {
                Offer o = myTradeOffers.get(i);

                String label = o.item.getName();

                // Only weapons have rarity text
                if (o.item instanceof Weapon w) {
                    label += " (" + w.getRarity() + ")";
                }
                label += " – " + o.price + "g";

                // --- NEW: icon for this item ---
                Icon icon = getHudIconForItem(o.item);
                btn.setIcon(icon);

                // Text & icon layout
                btn.setText(label);
                btn.setHorizontalAlignment(SwingConstants.LEFT);        // whole content left
                btn.setHorizontalTextPosition(SwingConstants.RIGHT);    // text to the right of icon
                btn.setIconTextGap(12);                                 // space between icon and text

                btn.setEnabled(true);
            } else {
                btn.setIcon(null);
                btn.setText("Empty Slot");
                btn.setEnabled(false);
            }
        }
    }

    private void buyOffer(final int index) {
        if (index < 0 || index >= myTradeOffers.size()) {
            return;
        }
        Offer offer = myTradeOffers.get(index);

        int gold = myHero.getGold();
        if (gold < offer.price) {
            appendText("You don't have enough gold to buy " + offer.item.getName() + ".");
            JOptionPane.showMessageDialog(
                    this,
                    "Not enough gold!",
                    "Shop",
                    JOptionPane.WARNING_MESSAGE,
                    myNoGoldIcon
            );
            return;
        }


        Inventory inv = myHero.getInventory();
        boolean added = inv.addItem(offer.item);
        if (!added) {
            appendText("Your inventory is full. You can't carry " + offer.item.getName() + ".");
            JOptionPane.showMessageDialog(this,
                    "Inventory is full!",
                    "Shop",
                    JOptionPane.WARNING_MESSAGE,
                    myInvFullIcon
            		);
            return;
        }

        myHero.addGold(-offer.price);
        refreshGoldLabel();
        refreshInventoryRow();
        appendText("You purchased " + offer.item.getName() + " for "
                   + offer.price + " gold.");

        // Remove the offer so it can't be bought again
        myTradeOffers.remove(index);
        updateTradeButtons();
    }

 // -----------------------------------------------------------------
 // GAMBLE PANEL
 // -----------------------------------------------------------------
 private JPanel createGamblePanel() {
     JPanel panel = new JPanel(new BorderLayout(10, 10));
     panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

     JPanel center = new JPanel();
     center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
     center.setBorder(BorderFactory.createTitledBorder("Gamble"));

     JLabel amountLabel = new JLabel("Gold amount to wager (min 50):");
     JTextField amountField = new JTextField(10);

     JButton rollButton = new JButton("Roll!");

     rollButton.addActionListener((ActionEvent e) -> {
    	    String text = amountField.getText().trim();
    	    int bet;
    	    try {
    	        bet = Integer.parseInt(text);
    	    } catch (NumberFormatException ex) {
    	        JOptionPane.showMessageDialog(this,
    	                "Please enter a valid number.",
    	                "Gamble",
    	                JOptionPane.WARNING_MESSAGE);
    	        return;
    	    }

    	    // minimum bet 50
    	    if (bet < 50) {
    	        JOptionPane.showMessageDialog(this,
    	                "Minimum bet is 50 gold.",
    	                "Gamble",
    	                JOptionPane.WARNING_MESSAGE);
    	        return;
    	    }

    	    // --- NEW: don't gamble if inventory row is full ---
    	    if (isShopInventoryFull()) {
    	    	// in gamble panel
    	    	JOptionPane.showMessageDialog(
    	    	        this,
    	    	        "Your inventory is full. You can't gamble for more items.\nUse or drop something first.",
    	    	        "Gamble",
    	    	        JOptionPane.PLAIN_MESSAGE,
    	    	        myInvFullIcon
    	                );
    	        return;
    	    }

    	    if (bet > myHero.getGold()) {
    	        JOptionPane.showMessageDialog(
    	                this,
    	                "You don't have that much gold.",
    	                "Gamble",
    	                JOptionPane.WARNING_MESSAGE,
    	                myNoGoldIcon
    	        );
    	        return;
    	    }

    	    // Take the gold (this is what actually deducts it)
    	    myHero.addGold(-bet);
    	    refreshGoldLabel();

    	    // 50% chance to win a random item
    	    if (myRand.nextBoolean()) {
    	        Item reward = randomShopItem();
    	        boolean added = myHero.getInventory().addItem(reward);

    	        String rarityLabel = (reward instanceof Weapon w)
    	                ? " (" + w.getRarity() + ")"
    	                : "";

    	        if (added) {
    	            appendText("You gambled " + bet + " gold and won a "
    	                    + reward.getName() + rarityLabel + "!");
    	            refreshInventoryRow();
    	        } else {
    	            appendText("You won " + reward.getName()
    	                    + " but your inventory is full.");
    	        }
    	    } else {
    	        appendText("You gambled " + bet + " gold and lost.");
    	    }
    	});
     
     amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
     amountField.setMaximumSize(new Dimension(120, 25));
     amountField.setAlignmentX(Component.CENTER_ALIGNMENT);
     rollButton.setAlignmentX(Component.CENTER_ALIGNMENT);

     center.add(amountLabel);
     center.add(amountField);
     center.add(Box.createVerticalStrut(10));
     center.add(new JLabel("Random weapon or potion if you win."));
     center.add(Box.createVerticalStrut(10));
     center.add(rollButton);

     panel.add(center, BorderLayout.CENTER);

     JPanel bottom = new JPanel();
     JButton back = new JButton("Back");
     JButton ok   = new JButton("Okay");

     back.addActionListener(_ -> myCardLayout.show(myCenterCards, "MAIN"));
     ok.addActionListener(_ -> dispose());

     bottom.add(back);
     bottom.add(ok);
     panel.add(bottom, BorderLayout.SOUTH);

     return panel;
 }

    // -----------------------------------------------------------------
    // Bottom inventory helpers
    // -----------------------------------------------------------------

    /** Creates the hero inventory row shown above the text dialog. */
    private JPanel createInventoryPanel() {
	    JPanel panel = new JPanel();
	    panel.setBorder(BorderFactory.createTitledBorder("Hero's Inventory"));
	    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

	    panel.add(Box.createHorizontalGlue());
	    for (int i = 0; i < myInventorySlots.length; i++) {
	        JLabel slot = createInventorySlot(i);   // pass index
	        myInventorySlots[i] = slot;
	        panel.add(slot);
	        if (i < myInventorySlots.length - 1) {
	            panel.add(Box.createHorizontalStrut(5));
	        }
	    }
	    panel.add(Box.createHorizontalGlue());

	    return panel;
	}

    private JLabel createInventorySlot(final int index) {
        JLabel slot = new JLabel();
        slot.setPreferredSize(new Dimension(40, 40));
        slot.setMinimumSize(new Dimension(40, 40));
        slot.setMaximumSize(new Dimension(40, 40));
        slot.setHorizontalAlignment(JLabel.CENTER);
        slot.setVerticalAlignment(JLabel.CENTER);
        slot.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        slot.setOpaque(false);

        slot.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleInventorySlotClick(index);
            }
        });

        return slot;
    }

    /** Refreshes the inventory row from the hero's current inventory. */
    private void refreshInventoryRow() {
        Inventory inv = myHero.getInventory();

        // Build a list of NON-pillar items only (pillars stay hidden in shop HUD)
        List<Item> nonPillars = new ArrayList<>();
        Map<String, Integer> stacks = null;

        if (inv != null) {
            for (Item it : inv.getInventory()) {
                if (!isPillarItem(it)) {
                    nonPillars.add(it);
                }
            }
            stacks = inv.getPotionStacks();
        }

        // Fill slots and reset borders
        for (int i = 0; i < myInventorySlots.length; i++) {
            JLabel slot = myInventorySlots[i];

            slot.setIcon(null);
            slot.setText("");
            slot.setToolTipText(null);
            slot.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

            if (i < nonPillars.size()) {
                Item item = nonPillars.get(i);

                Icon baseIcon = getHudIconForItem(item);
                Icon finalIcon = baseIcon;

                // show stack counts for potions
                if (item instanceof Potion && stacks != null && baseIcon != null) {
                    String key = item.getClass().getSimpleName();
                    int count = stacks.getOrDefault(key, 1);
                    if (count > 1) {
                        finalIcon = new CountIcon(baseIcon, count);
                    }
                }

                if (finalIcon != null) {
                    slot.setIcon(finalIcon);
                } else {
                    slot.setText(item.getName()); // fallback if no icon
                }

                slot.setToolTipText(item.getDescription());
            }
        }

        // Re-apply selection highlight using mySelectedInventoryIndex
        updateInventorySelectionBorders();
    }

    
    /** Returns the Item shown in slot n (skipping pillars), or null if none. */
    private Item getDisplayItemAt(final int displayIndex) {
        Inventory inv = myHero.getInventory();
        if (inv == null || displayIndex < 0) {
            return null;
        }

        List<Item> nonPillars = new ArrayList<>();
        for (Item it : inv.getInventory()) {
            if (!isPillarItem(it)) {
                nonPillars.add(it);
            }
        }

        if (displayIndex >= nonPillars.size()) {
            return null;
        }
        return nonPillars.get(displayIndex);
    }



    // -----------------------------------------------------------------
    // Misc helpers
    // -----------------------------------------------------------------
    
    /** Called when the user clicks one of the inventory slots. */
    private void handleInventorySlotClick(final int index) {
        Item item = getDisplayItemAt(index);
        if (item == null) {
            // empty slot: clear selection
            mySelectedInventoryIndex = -1;
        } else {
            mySelectedInventoryIndex = index;
            appendText("Selected " + item.getName() + ".");
        }
        updateInventorySelectionBorders();
    }

    /** Updates borders so the selected slot is highlighted. */
    private void updateInventorySelectionBorders() {
        for (int i = 0; i < myInventorySlots.length; i++) {
            JLabel slot = myInventorySlots[i];
            if (i == mySelectedInventoryIndex) {
                slot.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
            } else {
                slot.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            }
        }
    }
    
    /** Opens the normal inventory dialog, then refreshes the shop inventory row. */
    private void openInventoryDialogFromShop() {
        // owner was passed into the ShopDialog constructor
        JFrame owner = (JFrame) getOwner();

        InventoryDialog dialog =
                new InventoryDialog(owner, myHero, myHero.getInventory());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);   // blocks until the player closes the dialog

        // after they used/dropped items, refresh the row
        refreshInventoryRow();
    }

    /** 
     * Returns true if all visible shop slots are occupied
     * (non-pillar items >= number of HUD slots).
     */
    private boolean isShopInventoryFull() {
        Inventory inv = myHero.getInventory();
        if (inv == null) {
            return true;
        }

        int nonPillarCount = 0;
        for (Item it : inv.getInventory()) {
            if (!isPillarItem(it)) {
                nonPillarCount++;
            }
        }

        return nonPillarCount >= myInventorySlots.length;
    }

    
    private void refreshGoldLabel() {
        myGoldLabel.setText(String.valueOf(myHero.getGold()));
    }

    private void appendText(final String text) {
        myTextArea.append(text + "\n");
        myTextArea.setCaretPosition(myTextArea.getDocument().getLength());
    }

    /** Random rarity helper for items. */
    private Rarity randomRarity() {
        int roll = myRand.nextInt(100);
        if (roll < 50) return Rarity.COMMON;      // 50%
        if (roll < 80) return Rarity.UNCOMMON;    // 30%
        if (roll < 92) return Rarity.RARE;        // 12%
        if (roll < 98) return Rarity.EPIC;        // 6%
        return Rarity.LEGENDARY;                  // 2%
    }

    /** Random shop item: either weapon or potion, using your existing Weapon factories. */
    private Item randomShopItem() {
        int type = myRand.nextInt(4); // 0/1 = weapon, 2 = heal, 3 = vision
        if (type <= 1) {
            // random weapon
            Rarity r = randomRarity();
            int which = myRand.nextInt(5);
            return switch (which) {
                case 0 -> Weapon.createSpear(r);
                case 1 -> Weapon.createFalchion(r);
                case 2 -> Weapon.createFlail(r);
                case 3 -> Weapon.createMorningStar(r);
                default -> Weapon.createStick(r);
            };
        } else if (type == 2) {
            return new HealingPotion();
        } else {
            return new VisionPotion();
        }
    }
    
 // --- Icon helpers ---------------------------------------------------

    /** Load a small icon for the inventory row (24x24). */
    private Icon loadHudIcon(final String path) {
        ImageIcon raw = new ImageIcon(path);
        int size = 24;
        Image scaled = raw.getImage()
                          .getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /** Is this inventory item one of the four OO pillars? */
    private boolean isPillarItem(final Item item) {
        if (item == null) return false;
        if (item instanceof model.Pillar) return true;

        String desc = item.getDescription().toLowerCase();
        return desc.contains("pillar of");
    }

    /** Decide which icon to use for a given item in the shop inventory row. */
    private Icon getHudIconForItem(final Item item) {
        if (item instanceof HealingPotion) {
            return myHudHealingIcon;
        }
        if (item instanceof VisionPotion) {
            return myHudVisionIcon;
        }

        if (item instanceof Weapon weapon) {
            String name = weapon.getName().toLowerCase();

            if (name.contains("stick"))        return myHudStickIcon;
            if (name.contains("spear"))        return myHudSpearIcon;
            if (name.contains("flail"))        return myHudFlailIcon;
            if (name.contains("falchion"))     return myHudFalchionIcon;
            if (name.contains("morning star")) return myHudMorningStarIcon;
        }

        String desc = item.getDescription().toLowerCase();
        if (desc.contains("abstraction"))   return myHudAbsPillarIcon;
        if (desc.contains("encapsulation")) return myHudEncapPillarIcon;
        if (desc.contains("inheritance"))   return myHudInherPillarIcon;
        if (desc.contains("polymorphism"))  return myHudPolyPillarIcon;

        return null; // fallback: no icon
    }
}