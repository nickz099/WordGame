package BookwormAdventures;
import java.awt.*;

import javax.swing.*;
public class Letter extends JComponent {

    private final int ROW;
    private final int COL;
    private final String LETTER;
    private boolean isInArray;
    private boolean isPressed;
    private boolean isFrozen;
    private final int width;
    private final int height;
    private Color color;
    private boolean on;
    private int px;
    private int py;

    public Letter(String letter, int row, int col, boolean isInArray) {
        this.ROW = row;
        this.COL = col;
        this.LETTER = letter;
        this.isInArray = isInArray;
        isPressed = false;
        isFrozen = false;
        this.width = 50;
        this.height = 50;
        this.on = true;
        px = 100 * ROW;
        py = 100 * COL;
    }

    public int getRow() {
        return ROW;
    }

    public int getCol() {
        return COL;
    }

    public String getLETTER() {
        return LETTER;
    }

    public boolean getPressed() {
        return isPressed;
    }

    public void setPressed(boolean p) {
        isPressed = p;
    }

    public void setFreeze(boolean f) {
        isFrozen = f;
    }

    public boolean getFrozen() {
        return isFrozen;
    }

    public boolean getIsInArray() {
        return isInArray;
    }

    public void setIsInArray(boolean b) {
        isInArray = b;
    }

    public int getPx() {
        return px;
    }
    public void setPx(int i) {
        px = i;
    }

    public int getPy() {
        return py;
    }

    public void setPy(int i) {
        py = i;
    }

    public void move() {
        isInArray = !isInArray;
    }

    public JButton getButton() {
        //JButton b = new JButton(LETTER);
        return new JButton(LETTER);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    //unsure if use this or just draw() method


    //these two methods are for testing purposes
    public void setOn(boolean b) {
        on = b;
    }

    public boolean getOn() {
        return on;
    }

    public void draw(Graphics g) {
        /*if (isFrozen) {
            this.color = color.CYAN;
        } else {
            //this.color = color.WHITE;
            this.color = color.YELLOW;
        }*/
        if (on) {
            this.color = color.YELLOW;
        } else {
            this.color = color.WHITE;
        }
        g.setColor(color);
        g.fillRect(px, py, width, height);
        g.setColor(color.BLACK);
        g.drawString(LETTER, px + (width / 2), py + (height / 2));
        Point upperLeft = new Point(px, py);
        Point lowerRight = new Point(px + width, py + height);
        g.drawString("" + px + ", " + py, px, py);
    }

    public boolean isWithinBounds(Point p) {
        Point upperLeft = new Point(px, py);
        Point lowerRight = new Point(px + width, py + height);
        if (p.x >= upperLeft.x && p.x <= lowerRight.x && p.y >= upperLeft.y && p.y <= lowerRight.y) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        this.draw(g);
    }

    public Dimension getPreferredSize() {
        return new Dimension(100, 100);
    }


}
