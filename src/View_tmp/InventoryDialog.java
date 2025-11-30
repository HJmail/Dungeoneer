package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Hero;

/**
 * Inventory window shown when the player presses 'I'.
 * Layout roughly matches Cristian's inventory sketch.
 */
public final class InventoryDialog extends JDialog {

  private static final long serialVersionUID = 1L;

  /** Hero reference (for gold, name, etc.). */
  private final Hero myHero;

  /** Gold label so we could refresh later if needed. */
  private final JLabel myGoldLabel;

  /** Pillar slots (#/4). */
  private final JLabel[] myPillarSlots;

  /** Item slots (weapons / potions). */
  private final JLabel[] myItemSlots;

  /** Info text area for selected item. */
  private final JTextArea myInfoArea;

  public InventoryDialog(final JFrame owner, final Hero theHero) {
    super(owner, "Inventory", true);  // modal dialog
    myHero = theHero;

    setLayout(new BorderLayout(10, 10));
    ((JPanel) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // ---- LEFT: character portrait + name ----
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BorderLayout(0, 5));

    // Placeholder character portrait (later you can draw hero sprite here)
    JPanel portraitPanel = new JPanel();
    portraitPanel.setPreferredSize(new Dimension(120, 200));
    portraitPanel.setBackground(new Color(230, 230, 230));
    portraitPanel.setBorder(BorderFactory.createTitledBorder("Character"));
        leftPanel.add(portraitPanel, BorderLayout.CENTER);

        // Hero name (assuming Hero has getName(); adjust if different)
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
        JLabel pillarTitle = new JLabel("Pillars (0/4):");
        pillarPanel.add(pillarTitle);
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
        for (int i = 0; i < myItemSlots.length; i++) {
            JLabel slot = createSquareSlot(50);
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

        pack();
        setLocationRelativeTo(owner);
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
}