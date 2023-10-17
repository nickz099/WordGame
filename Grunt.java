package BookwormAdventures;

import java.awt.*;

public class Grunt extends Enemy {
    public Grunt(double health, double damage) {
        super(health, damage);
        this.name = "GRUNT";
    }

    @Override
    public void draw(Graphics g) {
        int x = 1100;
        int y = 200;
        int w = 50;
        int h = 100;
        g.setColor(Color.ORANGE);
        //torso
        g.fillOval(x, y, w, h);
        //arms
        g.drawLine(x + w / 2, y + h / 2, (x + w / 2) - w, y);
        g.drawLine(x + w / 2, y + h / 2, (x + w / 2) + w, y);
        //head
        g.fillOval(x, y - w, w, w);
        g.setColor(Color.BLACK);
        g.drawString("Grunt", x + (w / 2) - 15, y + h + 20);
    }
}
