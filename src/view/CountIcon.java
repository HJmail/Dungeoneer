package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;

/**
 * Icon decorator that draws a small "xN" counter on top of another icon.
 */
public final class CountIcon implements Icon {

    private final Icon myBase;
    private final String myText;

    public CountIcon(final Icon theBase, final int theCount) {
        myBase = theBase;
        myText = "x" + theCount;
    }

    @Override
    public int getIconWidth() {
        return myBase.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return myBase.getIconHeight();
    }

    @Override
    public void paintIcon(final Component c,
                          final Graphics g,
                          final int x,
                          final int y) {
        // draw original icon
        myBase.paintIcon(c, g, x, y);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 11f));

        FontMetrics fm = g2.getFontMetrics();
        int w = fm.stringWidth(myText);
        int h = fm.getAscent();

        int padding = 2;
        int bx = x + getIconWidth() - w - padding * 2;
        int by = y + getIconHeight() - h - padding;

        // dark rounded background for readability
        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRoundRect(bx - padding, by - h,
                         w + padding * 2, h + padding,
                         6, 6);

        // white text
        g2.setColor(Color.WHITE);
        g2.drawString(myText, bx, by);

        g2.dispose();
    }
}