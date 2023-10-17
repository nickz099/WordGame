package BookwormAdventures;

import java.util.Random;
import java.awt.*;

public class Siren extends Enemy {
    private double stunChance;
    public Siren(double health, double damage) {
        super(health, damage);
        stunChance = 0.4;
        this.name = "SIREN";
    }

    @Override
    public String getName() {
        return super.getName();
    }

    public void setStunChance(double d) {
        stunChance = d;
    }

    @Override
    public double attack(Player p) {
        Random g = new Random();
        double d = g.nextDouble();
        if (d <= stunChance) {
            //if stunChance, then simply stun player instead of doing damage
            System.out.println(d);
            p.setStunned(true);
            p.setEffectDuration(1);
        }
        return super.attack(p);
    }

    public void die(Player p) {
        super.die();
        p.setStunned(false);
        p.setEffectDuration(3);
    }

    @Override
    public void draw(Graphics g) {
        int x = 1100;
        int y = 200;
        int w = 50;
        int h = 100;
        g.setColor(Color.PINK);
        //torso
        g.fillOval(x, y, w, h);
        //arms
        g.drawLine(x + w / 2, y + h / 2, (x + w / 2) - w, y);
        g.drawLine(x + w / 2, y + h / 2, (x + w / 2) + w, y);
        //head
        g.fillOval(x, y - w, w, w);
        g.setColor(Color.BLACK);
        g.drawString("SIREN", x + (w / 2) - 15, y + h + 20);
    }
}
