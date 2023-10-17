package BookwormAdventures;

import java.util.Random;
import java.awt.*;
import javax.swing.*;

public abstract class Enemy extends JComponent implements LiveEntity {
    private double health;
    private double damage;
    private boolean isAlive;
    protected String name;
    private final double MAX_HEALTH;
    public Enemy (double health, double damage) {

        this.health = health;
        this.damage = damage;
        this.MAX_HEALTH = this.health;
        this.name = "Enemy";
        isAlive = true;
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public void setHealth(double h) {
        this.health = h;
    }

    @Override
    public void setDamage(double d) {
        damage = d;
    }

    @Override
    public double getDamage() {
        return damage;
    }


    @Override
    public boolean checkAlive() {
        return isAlive;
    }

    @Override
    public void die() {
        isAlive = false;
    }

    @Override
    public String speak() {
        return null;
    }

    public double attack(Player p) {
        p.takeDamage(damage);
        return damage;
    }

    public String getName() {
        return name;
    }

    public void takeDamage(double d) {
        health = health - d;
    }

    public void draw(Graphics g) {};

    public void drawHealthBar(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(1200, 50, 100, 50);
        g.setColor(Color.RED);
        double h = health / MAX_HEALTH * 100;
        g.fillRect(1200, 50, (int) h, 50);
        g.setColor(Color.BLACK);
        g.drawString("" + health + " / " + MAX_HEALTH, 1205, 50 + 25);
    }
}


/*types of enemies: spider (poison), siren (stun for a turn), grunts, vampire (drain health and heal,
vampire easter egg is that "sun" or garlic one-shots them, snowman (freezes letters), and bosses
 */


class Spider extends Enemy {
    public Spider(double health, double damage) {
        super(health, damage);
        this.name = "Spider";
    }

    @Override
    public double attack (Player p) {
        double d = super.attack(p);
        Random g = new Random();
        if (g.nextDouble() < 0.4) {
            p.setPoisoned(true);
            p.setEffectDuration(3);
        }
        return d;
    }

    public void die(Player p) {
        super.die();
        //dying removes poison effects
        p.setPoisoned(false);
    }

    @Override
    public void draw(Graphics g) {
        int x = 1100;
        int y = 200;
        int w = 100;
        int h = 50;
        g.setColor(Color.BLACK);
        //torso
        g.fillOval(x, y, w, h);
        //arms
        g.drawLine(x + w / 2, y + h / 2, (x + w / 2) - w, y + h);
        g.drawLine(x + w / 2, y + h / 2, (x + w / 2) - w + 30, y + h + 10);
        g.drawLine(x + w / 2, y + h / 2, (x + w / 2) - w + 120, y + h + 10);
        g.drawLine(x + w / 2, y + h / 2, (x + w / 2) - w + 210, y + h);
        //head
        g.fillOval(x, y - 30, h -10, h - 10);
        g.drawString("Spider", x + (w / 2) - 15, y + h + 20);
    }
}

class Vampire extends Enemy {

    public Vampire(double health, double damage) {
        super(health, damage);
        this.name = "Vampire";
    }

    @Override
    public double attack (Player p) {
        double d = super.attack(p);
        double h = super.getHealth();
        super.setHealth(h + d);
        return d;
    }

    @Override
    public void draw(Graphics g) {
        int x = 1100;
        int y = 200;
        int w = 50;
        int h = 100;
        g.setColor(Color.BLACK);
        //torso
        g.fillOval(x, y, w, h);
        //arms
        g.drawLine(x + w / 2, y + h / 2, (x + w / 2) - w, y);
        g.drawLine(x + w / 2, y + h / 2, (x + w / 2) + w, y);
        //head
        g.setColor(Color.WHITE);
        g.fillOval(x, y - w, w, w);
        g.setColor(Color.BLACK);
        g.drawString("Vampire", x + (w / 2) - 15, y + h + 20);
    }
}

class Boss extends Enemy {

    public Boss(double health, double damage) {
        super(health, damage);
        this.name = "WORD MONSTER";
    }

    @Override
    public double attack(Player p) {
        Random g = new Random();
        double d = g.nextDouble();
        double damage = 0.0;
        //stun
        if (d < 0.25) {
            System.out.println(d);
            p.setStunned(true);
            p.setEffectDuration(1);
            //poison
        } else if (d < 0.5 && d >= 0.25) {
            p.setPoisoned(true);
            p.setEffectDuration(3);
        }
        //slight chance to vampire heal
        if (d < 0.1) {
            super.setHealth(super.getHealth() + damage);
        }
        return super.attack(p);
    }

    @Override
    public void draw(Graphics g) {
        int x = 1100;
        int y = 200;
        int w = 50;
        int h = 100;
        g.setColor(Color.RED);
        //torso
        g.fillOval(x, y, 100, 150);
        //arms
        g.drawLine(x + w / 2, y + h / 2, (x + w / 2) - w, y);
        g.drawLine(x + w / 2, y + h / 2, (x + w / 2) + w, y);
        //head
        g.fillOval(x + 20, y - w, w, w);
    }
}