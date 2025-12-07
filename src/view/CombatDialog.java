package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
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

public class CombatDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final Hero myHero;
    private final List<Monster> myMonsters;

    // Hero UI
    private JProgressBar myHeroHpBar;
    private JLabel myHeroHpLabel;
    private JLabel myHeroWeaponLabel;

    // Monsters UI
    private JProgressBar[] myMonsterHpBars;
    private JLabel[] myMonsterLabels;

    // store max HPs for bars
    private final int myHeroMaxHp;
    private final int[] myMonsterMaxHp;

    // Center sprites
    private JLabel myHeroSpriteLabel;
    private JLabel[] myMonsterSpriteLabels;

    // Battle inventory row
    private JLabel[] myInvSlots;
    private Item[] myInvItems;
    private int mySelectedInvIndex = -1;

    // Inventory icons
    private final Icon myHealingIcon;
    private final Icon myVisionIcon;
    private final Icon myStickIcon;
    private final Icon mySpearIcon;
    private final Icon myFlailIcon;
    private final Icon myFalchionIcon;
    private final Icon myMorningStarIcon;
    private final Icon myVictoryIcon;

    // Text log
    private final JTextArea myLogArea;
    
    private final DungeoneerFrame myOwner;


    public CombatDialog(final JFrame owner,
                        final Hero theHero,
                        final List<Monster> theMonsters) {

        super(owner, "Battle!", true);
        
        setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        myHero = theHero;
        myMonsters = theMonsters;
        
     // NEW: remember the main frame so we can trigger death menu
        if (owner instanceof DungeoneerFrame df) {
            myOwner = df;
        } else {
            myOwner = null;
        }

        // remember starting HPs
        myHeroMaxHp = myHero.getMaxHitPoints();
        myMonsterMaxHp = new int[myMonsters.size()];
        for (int i = 0; i < myMonsters.size(); i++) {
            myMonsterMaxHp[i] = myMonsters.get(i).getHitPoints();
        }

        // inventory icons (32x32)
        myHealingIcon     = loadItemIcon("Dungeoneer_Items/potion_healing.png", 32);
        myVisionIcon      = loadItemIcon("Dungeoneer_Items/potion_vision.png", 32);
        myStickIcon       = loadItemIcon("Dungeoneer_Items/stick.png", 32);
        mySpearIcon       = loadItemIcon("Dungeoneer_Items/spear.png", 32);
        myFlailIcon       = loadItemIcon("Dungeoneer_Items/flail.png", 32);
        myFalchionIcon    = loadItemIcon("Dungeoneer_Items/falchion.png", 32);
        myMorningStarIcon = loadItemIcon("Dungeoneer_Items/morning_star.png", 32);
        myVictoryIcon = loadScaled("Dungeoneer Icon.png", 96, 96);

        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== TOP: HP bars ===========================================
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));

        // Hero HP
        JPanel heroHpPanel = new JPanel();
        heroHpPanel.setLayout(new BoxLayout(heroHpPanel, BoxLayout.Y_AXIS));
        JLabel heroTitle = new JLabel(myHero.getName());
        heroTitle.setFont(heroTitle.getFont().deriveFont(Font.BOLD, 14f));

        myHeroHpBar = new JProgressBar();
        myHeroHpBar.setPreferredSize(new Dimension(200, 16));
        
        myHeroHpLabel = new JLabel();
        myHeroWeaponLabel = new JLabel();

        heroHpPanel.add(heroTitle);
        heroHpPanel.add(Box.createVerticalStrut(4));
        heroHpPanel.add(myHeroHpBar);
        heroHpPanel.add(Box.createVerticalStrut(2));
        heroHpPanel.add(myHeroHpLabel);       // HP text only
        
        // Monsters HP
        int monsterCount = myMonsters.size();
        myMonsterHpBars = new JProgressBar[monsterCount];
        myMonsterLabels = new JLabel[monsterCount];

        JPanel monstersHpPanel = new JPanel();
        monstersHpPanel.setLayout(new BoxLayout(monstersHpPanel, BoxLayout.Y_AXIS));
        monstersHpPanel.setBorder(BorderFactory.createTitledBorder("Monsters"));

        for (int i = 0; i < monsterCount; i++) {
            Monster m = myMonsters.get(i);

            JLabel nameLabel = new JLabel(m.getName());
            JProgressBar hpBar = new JProgressBar();
            hpBar.setPreferredSize(new Dimension(180, 14));

            myMonsterLabels[i] = nameLabel;
            myMonsterHpBars[i] = hpBar;

            JPanel row = new JPanel(new BorderLayout(5, 0));
            row.add(nameLabel, BorderLayout.WEST);
            row.add(hpBar, BorderLayout.CENTER);

            monstersHpPanel.add(row);
            monstersHpPanel.add(Box.createVerticalStrut(4));
        }

        topPanel.add(heroHpPanel, BorderLayout.WEST);
        topPanel.add(monstersHpPanel, BorderLayout.EAST);

        // ===== CENTER: sprites =======================================
     // ===== CENTER: sprites =======================================
        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));

        myHeroSpriteLabel = new JLabel();
        myHeroSpriteLabel.setHorizontalAlignment(JLabel.CENTER);
        myHeroSpriteLabel.setVerticalAlignment(JLabel.CENTER);
        myHeroSpriteLabel.setPreferredSize(new Dimension(160, 160));

        JPanel heroSpritePanel = new JPanel(new BorderLayout());
        heroSpritePanel.add(myHeroSpriteLabel, BorderLayout.CENTER);

        // put the weapon label under the sprite
        myHeroWeaponLabel.setHorizontalAlignment(JLabel.CENTER);
        heroSpritePanel.add(myHeroWeaponLabel, BorderLayout.SOUTH);

        myMonsterSpriteLabels = new JLabel[monsterCount];
        JPanel monstersSpritePanel = new JPanel();
        monstersSpritePanel.setLayout(new GridLayout(monsterCount, 1, 5, 5));

        for (int i = 0; i < monsterCount; i++) {
            JLabel spriteLabel = new JLabel();
            spriteLabel.setHorizontalAlignment(JLabel.CENTER);
            spriteLabel.setVerticalAlignment(JLabel.CENTER);
            spriteLabel.setPreferredSize(new Dimension(120, 80));
            myMonsterSpriteLabels[i] = spriteLabel;

            JPanel cell = new JPanel(new BorderLayout());
            cell.add(spriteLabel, BorderLayout.CENTER);
            monstersSpritePanel.add(cell);
        }

        // Labels row: "Hero" on the left, "Monsters" on the right
        JPanel labelsRow = new JPanel(new BorderLayout());
        JLabel heroLabel = new JLabel("Hero");
        heroLabel.setFont(heroLabel.getFont().deriveFont(Font.BOLD, 14f));
        JLabel monstersLabel = new JLabel("Monsters");
        monstersLabel.setFont(monstersLabel.getFont().deriveFont(Font.BOLD, 14f));

        labelsRow.add(heroLabel, BorderLayout.WEST);
        labelsRow.add(monstersLabel, BorderLayout.EAST);

        centerPanel.add(labelsRow, BorderLayout.NORTH);
        centerPanel.add(heroSpritePanel, BorderLayout.WEST);
        centerPanel.add(monstersSpritePanel, BorderLayout.EAST);

        // ===== BOTTOM: inventory row + buttons + log =================
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

        // inventory strip
        JPanel invRow = new JPanel();
        invRow.setLayout(new BoxLayout(invRow, BoxLayout.X_AXIS));
        invRow.add(new JLabel("Inventory: "));
        invRow.add(Box.createHorizontalStrut(8));

        myInvSlots = new JLabel[5];
        myInvItems = new Item[myInvSlots.length];

        for (int i = 0; i < myInvSlots.length; i++) {
            final int index = i;
            JLabel slot = createInvSlotLabel(32);
            slot.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    handleInventoryClick(index);
                }
            });
            myInvSlots[i] = slot;
            invRow.add(slot);
            invRow.add(Box.createHorizontalStrut(4));
        }

        // buttons row
        JPanel buttonRow = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton attackButton = new JButton("Attack");
        JButton specialButton = new JButton("Special");

        attackButton.setAction(new AbstractAction("Attack") {
            private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(final ActionEvent e) {
                attackAction();
            }
        });

        specialButton.setAction(new AbstractAction("Special") {
            private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(final ActionEvent e) {
                specialAction();   // <-- we call fakeSpecial(), not specialAction()
            }
        });

        buttonRow.add(attackButton);
        buttonRow.add(specialButton);

        // log
        myLogArea = new JTextArea(5, 40);
        myLogArea.setEditable(false);
        myLogArea.setLineWrap(true);
        myLogArea.setWrapStyleWord(true);
        JScrollPane logScroll = new JScrollPane(myLogArea);

        bottomPanel.add(invRow,    BorderLayout.NORTH);
        bottomPanel.add(buttonRow, BorderLayout.CENTER);
        bottomPanel.add(logScroll, BorderLayout.SOUTH);

        // ===== assemble whole dialog =================================
        add(topPanel,    BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshHeroHp();
        refreshMonsterHp();
        loadSprites();
        refreshBattleInventory();

        pack();
        setLocationRelativeTo(owner);
    }

    // ===== Inventory helpers =========================================

    private JLabel createInvSlotLabel(final int size) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(size, size));
        label.setMinimumSize(new Dimension(size, size));
        label.setMaximumSize(new Dimension(size, size));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        label.setOpaque(true);
        label.setBackground(new Color(245, 245, 245));
        return label;
    }

    private ImageIcon loadItemIcon(final String path, final int size) {
        return loadScaled(path, size, size);
    }

    private void refreshBattleInventory() {
        Inventory inv = myHero.getInventory();
        if (inv == null) {
            for (int i = 0; i < myInvSlots.length; i++) {
                myInvSlots[i].setIcon(null);
                myInvSlots[i].setText("");
                myInvItems[i] = null;
            }
            return;
        }

        java.util.List<Item> allItems = inv.getInventory();
        java.util.List<Item> nonPillars = new java.util.ArrayList<>();

        for (Item item : allItems) {
            if (!(item instanceof Pillar)) {
                nonPillars.add(item);
            }
        }

        // clear
        for (int i = 0; i < myInvSlots.length; i++) {
            myInvSlots[i].setIcon(null);
            myInvSlots[i].setText("");
            myInvItems[i] = null;
            myInvSlots[i].setBackground(new Color(245, 245, 245));
        }

        // fill
        for (int i = 0; i < myInvSlots.length && i < nonPillars.size(); i++) {
            Item item = nonPillars.get(i);
            myInvItems[i] = item;

            javax.swing.Icon baseIcon = getInventoryIconForItem(item);
            javax.swing.Icon finalIcon = baseIcon;

            if (item instanceof Potion && baseIcon != null) {
                java.util.Map<String, Integer> stacks = inv.getPotionStacks();
                String key = item.getClass().getSimpleName();
                int count = stacks.getOrDefault(key, 1);
                if (count > 1) {
                    finalIcon = new CountIcon(baseIcon, count);
                }
            }

            myInvSlots[i].setIcon(finalIcon);
            myInvSlots[i].setBackground(Color.WHITE);
        }

        updateInventorySelectionHighlight();
    }

    private javax.swing.Icon getInventoryIconForItem(final Item item) {
        if (item instanceof HealingPotion) return myHealingIcon;
        if (item instanceof VisionPotion)  return myVisionIcon;

        if (item instanceof Weapon weapon) {
            String name = weapon.getName().toLowerCase();
            if (name.contains("stick"))        return myStickIcon;
            if (name.contains("spear"))        return mySpearIcon;
            if (name.contains("flail"))        return myFlailIcon;
            if (name.contains("falchion"))     return myFalchionIcon;
            if (name.contains("morning star")) return myMorningStarIcon;
        }
        return null;
    }

    private void handleInventoryClick(final int index) {
        Inventory inv = myHero.getInventory();
        if (inv == null) return;
        if (index < 0 || index >= myInvItems.length) return;

        Item item = myInvItems[index];
        if (item == null) return;

        mySelectedInvIndex = index;
        updateInventorySelectionHighlight();

        // potions
        if (item instanceof Potion) {
            String key = item.getClass().getSimpleName();

            if (item instanceof HealingPotion && myHero.getHitPoints() >= myHeroMaxHp) {
                log("Your health is already full.");
                return;
            }

            inv.useItem(key);
            log(myHero.getName() + " uses " + key + "!");
            refreshHeroHp();
            refreshBattleInventory();

            // End of hero's turn: monster gets a turn (if any are alive)
            Monster attacker = getFirstAliveMonster();
            monsterCounterAttack(attacker);
            return;

        }

     // --- Weapons: equip for future attacks ---
        if (item instanceof Weapon weapon) {
            if (myHero.getEquippedWeapon() == weapon) {
                log(weapon.getName() + " is already equipped.");
                return;
            }
            myHero.equipWeapon(weapon);
            log(myHero.getName() + " equips " + weapon.getName() + ".");

            // update combat UI + main HUD
            refreshHeroHp();
            if (myOwner != null) {
                myOwner.refreshHeroStats();
            }
        }
    }

    private void updateInventorySelectionHighlight() {
        for (int i = 0; i < myInvSlots.length; i++) {
            if (i == mySelectedInvIndex && myInvItems[i] != null) {
                myInvSlots[i].setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            } else {
                myInvSlots[i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            }
        }
    }

    // ===== Combat actions ============================================

    /** One attack turn: hero vs the first alive monster only. */
    private void attackAction() {
        Monster target = getFirstAliveMonster();

        if (target == null) {
            log("All monsters are already defeated!");
            return;
        }

        String heroMsg = myHero.attack(target);
        log(heroMsg);
        refreshMonsterHp();

        if (myOwner != null) {
            myOwner.refreshHeroStats();
        }

        if (target.getHitPoints() <= 0) {
            log(target.getName() + " is defeated!");
            refreshMonsterSprites();

            boolean anyAlive = false;
            for (Monster m : myMonsters) {
                if (m.getHitPoints() > 0) {
                    anyAlive = true;
                    break;
                }
            }
            if (!anyAlive) {
                log("All monsters are already defeated!");
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "You defeated all the monsters!",
                        "Victory!",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE,
                        myVictoryIcon
                );
                if (myOwner != null) {
                    myOwner.refreshHeroStats();
                }
                dispose();
                return;
            }
            return; // hero used their turn, no counter-attack from a dead monster
        }

        // Monster counter-attack
        monsterCounterAttack(target);
    }

    /** One turn where the hero uses their specialSkill. */
    private void specialAction() {
        // Priestess can still heal even if all monsters are dead,
        // but for Warrior/Thief we want a target.
        Monster target = getFirstAliveMonster();

        // If no monsters alive AND hero’s special is offensive, just say so.
        if (target == null && !(myHero instanceof Priestess)) {
            log("All monsters are already defeated!");
            return;
        }

        // Call the polymorphic specialSkill:
        // Warrior / Thief: damage the target
        // Priestess: heals herself (ignores the target argument)
        String heroMsg = myHero.specialSkill(
                (target != null) ? target : myHero  // Priestess case
        );
        log(heroMsg);

        // Both hero and monsters might have changed HP
        refreshHeroHp();
        refreshMonsterHp();
        refreshMonsterSprites();

        if (myOwner != null) {
            myOwner.refreshHeroStats();
        }

        // If we actually had a target, check for its death & possible victory
        if (target != null && target.getHitPoints() <= 0) {
            log(target.getName() + " is defeated!");
            refreshMonsterSprites();

            boolean anyAlive = false;
            for (Monster m : myMonsters) {
                if (m.getHitPoints() > 0) {
                    anyAlive = true;
                    break;
                }
            }

            if (!anyAlive) {
                log("All monsters are already defeated!");

                JOptionPane.showMessageDialog(
                        this,
                        "You defeated all the monsters!",
                        "Victory!",
                        JOptionPane.INFORMATION_MESSAGE,
                        myVictoryIcon
                );

                if (myOwner != null) {
                    myOwner.refreshHeroStats();
                }
                dispose();
                return;   // battle ended
            }
        }

        // If there is still at least one living monster, it gets a counter-attack
        monsterCounterAttack(getFirstAliveMonster());
    }

    private void log(final String text) {
        myLogArea.append(text + "\n");
        myLogArea.setCaretPosition(myLogArea.getDocument().getLength());
    }

    // ===== HP + sprites ==============================================

    private void refreshHeroHp() {
        int current = myHero.getHitPoints();
        int max = myHeroMaxHp;

        // clamp HP for safety
        if (current < 0) current = 0;
        if (current > max) current = max;

        myHeroHpBar.setMaximum(max);
        myHeroHpBar.setValue(current);
        myHeroHpLabel.setText("HP: " + current + " / " + max);

        // --- Weapon + rarity line ---
        Weapon weapon = myHero.getEquippedWeapon();
        if (weapon != null) {
            Rarity rarity = weapon.getRarity();
            String rarityName = (rarity != null) ? rarity.name() : "UNKNOWN";

            String text = String.format(
                "%s (%s, Damage: %d)",
                weapon.getName(),
                rarityName,
                weapon.getDamage()
            );

            myHeroWeaponLabel.setText(text);
            myHeroWeaponLabel.setForeground(colorForRarity(rarity));
        } else {
            myHeroWeaponLabel.setText("Weapon: none");
            myHeroWeaponLabel.setForeground(Color.BLACK);
        }
    }
    
    /** Same rarity colors as the inventory. */
    private Color colorForRarity(final Rarity rarity) {
        if (rarity == null) {
            return Color.BLACK;
        }

        return switch (rarity) {
            case COMMON    -> Color.BLACK;
            case UNCOMMON  -> new Color(0, 128, 0);      // green
            case RARE      -> Color.BLUE;                // blue
            case EPIC      -> new Color(128, 0, 128);    // purple
            case LEGENDARY -> new Color(255, 140, 0);    // orange/gold
        };
    }
    
    private void refreshMonsterHp() {
        for (int i = 0; i < myMonsters.size(); i++) {
            Monster m = myMonsters.get(i);
            int current = Math.max(0, m.getHitPoints());
            int max = myMonsterMaxHp[i];

            JProgressBar bar = myMonsterHpBars[i];
            bar.setMaximum(max);
            bar.setValue(current);
        }
    }
    
    /** Returns the first monster that is still alive, or null if none. */
    private Monster getFirstAliveMonster() {
        for (Monster m : myMonsters) {
            if (m.getHitPoints() > 0) {
                return m;
            }
        }
        return null;
    }

    /** One monster turn: the given monster attacks the hero and we handle death/UI. */
    private void monsterCounterAttack(final Monster attacker) {
        if (attacker == null || myHero.getHitPoints() <= 0) {
            return;  // nothing to do
        }

        String monsterMsg = attacker.attack(myHero);
        log(monsterMsg);
        refreshHeroHp();

        if (myOwner != null) {
            myOwner.refreshHeroStats();
        }

        // Hero died from this attack
        if (myHero.getHitPoints() <= 0) {
            log(myHero.getName() + " has been defeated!");
            refreshHeroHp();   // show 0 / max in bar

            if (myOwner != null) {
                dispose();                          // close combat window
                myOwner.handleHeroDeathFromCombat(attacker.getName()); // custom death text
            }
        }
    }

    private void loadSprites() {
        // hero facing right (same as you already have)
        String heroPath;
        if (myHero instanceof Warrior) {
            heroPath = "Dungeoneer_Characters/warrior_right.png";
        } else if (myHero instanceof Thief) {
            heroPath = "Dungeoneer_Characters/thief_right.png";
        } else if (myHero instanceof Priestess) {
            heroPath = "Dungeoneer_Characters/priestess_right.png";
        } else {
            heroPath = "Dungeoneer_Characters/hero_right.png";
        }
        myHeroSpriteLabel.setIcon(loadScaled(heroPath, 128, 128));

        // monsters (initially all alive)
        refreshMonsterSprites();
    }
    
    /** Updates all monster sprites, using dead sprites for monsters at 0 HP. */
    private void refreshMonsterSprites() {
        for (int i = 0; i < myMonsters.size(); i++) {
            Monster m = myMonsters.get(i);
            boolean isDead = m.getHitPoints() <= 0;
            String path = getMonsterSpritePath(m, isDead);
            myMonsterSpriteLabels[i].setIcon(loadScaled(path, 96, 96));
        }
    }

    private ImageIcon loadScaled(final String path,
                                 final int w,
                                 final int h) {
        ImageIcon raw = new ImageIcon(path);
        Image scaled = raw.getImage()
                          .getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
    
    /** Returns the sprite path for a monster, choosing alive/dead. */
    private String getMonsterSpritePath(final Monster m, final boolean isDead) {
        // adjust filenames to match your actual PNGs
        if (m instanceof Gremlin) {
            return isDead
                    ? "Dungeoneer_Characters/gremlin_dead.png"
                    : "Dungeoneer_Characters/gremlin_left.png";
        } else if (m instanceof Ogre) {
            return isDead
                    ? "Dungeoneer_Characters/orge_dead.png"
                    : "Dungeoneer_Characters/orge_left.png";
        } else if (m instanceof Skeleton) {
            return isDead
                    ? "Dungeoneer_Characters/skeleton_dead.png"
                    : "Dungeoneer_Characters/skeleton_left.png";
        } else {
            // fallback
            return "Dungeoneer_Characters/hero_right.png";
        }
    }
}