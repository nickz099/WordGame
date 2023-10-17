package BookwormAdventures;
import java.awt.*;
public class Player implements LiveEntity {
    private double health;
    private double damage;
    private double maxHealth;
    private double damageMultiplier;
    private double healthMultiplier;
    private boolean isAlive;
    private boolean isPoisoned;
    private boolean vampireEquipped;
    //private boolean isAttacked;
    private boolean isStunned;
    private int effectDuration;
    private String name;

    public Player (double health, double damage, String name) {
        this.health = health;
        this.damage = damage;
        damageMultiplier = 1.0;
        healthMultiplier = 1.0;
        isAlive = true;
        //isAttacked = false;
        isPoisoned = false;
        isStunned = false;
        this.name = name;
        effectDuration = 3;
        vampireEquipped = false;
        maxHealth = this.health * this.healthMultiplier;
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

    public void setMaxHealth(double d) {
        maxHealth = d;
    }

    public double getMaxHealth() {
        return maxHealth;
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

    public String getName() {
        return name;
    }

    public void setEffectDuration(int n) {
        effectDuration = n;
    }

    public int getEffectDuration() {
        return effectDuration;
    }

    public void setStunned(boolean s) {
        isStunned = s;
    }

    public boolean getStunned() {
        return isStunned;
    }

    public void setVampireEquipped(boolean b) {
        vampireEquipped = b;
    }

    public boolean getVampireEquipted() {
        return vampireEquipped;
    }

    public double getTotalDamage() {
        return damage * damageMultiplier;
    }

    public boolean getPoinsoned() {
        return isPoisoned;
    }

    public void setPoisoned(boolean b) {
        isPoisoned = b;
    }

    public double attack(Enemy e, double d) {
        e.takeDamage(d);
        //if defeated a vapmire, gain the vampire ability to heal
        if (vampireEquipped) {
            //dont't need to go above 200 HP
            if (health + d > 200.0) {
                health = 200.0;
            } else {
                health += d;
            }
        }
        return d;
    }

    public void takeDamage(double d) {
        health = health - d;
    }

    public void takePoisonDamage() {
        if (isPoisoned) {
            if (effectDuration == 0) {
                damageMultiplier = 1.0;
                isPoisoned = false;
            } else {
                damageMultiplier = 0.5;
                effectDuration--;
            }
        }
        //look for ways to keep track of poison turns
    }

    public void takeStunDamage() {
        if (isStunned) {
            if (effectDuration == 0) {
                isStunned = false;
            }
            effectDuration--;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillOval(200, 200, 50, 100);
        g.fillOval(200, 150, 50, 50);
        g.fillOval(200, 200 - 50, 50, 50);
        if (isStunned) {
            g.setColor(Color.BLACK);
            g.drawString("STUNNED", 200, 250);
        }
        if (isPoisoned) {
            g.setColor(Color.BLACK);
            g.drawString("POISONED", 200, 250);
        }
    }

    public void drawHealthBar(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(100, 50, 100, 50);
        g.setColor(Color.RED);
        double h = health / maxHealth * 100;
        g.fillRect(100, 50, (int) h, 50);
        g.setColor(Color.BLACK);
        if (health > 100.0) {
            g.drawString("" + health + " / " + health, 105, 50 + 25);
        } else {
            g.drawString("" + health + " / " + maxHealth, 105, 50 + 25);
        }
    }
}
