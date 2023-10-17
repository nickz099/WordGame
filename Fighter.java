package BookwormAdventures;

public abstract class Fighter{
    private double health;
    private double healthMultiplier;
    private double damage;
    private double damageMultiplier;
    private boolean isAlive;
    private String description;

    Fighter(Difficulty d, double health, double damage) {
        this.health = health;
        this.damage = damage;
        switch (d) {
            case NORMAL: healthMultiplier = 1.0; damageMultiplier = 1.0; break;
            case HARD: healthMultiplier = 2.0; damageMultiplier = 2.0; break;
        }
        this.isAlive = true;
    }

    public double getHealth() {
        return this.health;
    }

    public void setHealth(double h) {
        this.health = h;
    }

    public double getTotalHealth() {
        return health * healthMultiplier;
    }

    public void attack(Fighter f) {
        double totalDamage = damage * damageMultiplier;
        f.setHealth(f.getHealth() - totalDamage);
    }

    public String speak() {
        return "Attack!";
    }

    public String getDescription() {
        return description;
    }

    public boolean checkAlive() {
        return isAlive;
    }

    public void die() {
        isAlive = false;
    }


}
/*
class Player extends Fighter {
    private double health;
    private double damage;
    private double damageMultiplier;
    private double healthMultiplier;
    public Player(Difficulty d, double health, double damage) {
        super(d, health, damage);
    }

    public void setHealth(double h) {
        health = h;
    }

    public double getHealth() {
        return health;
    }

    public void setDamageMultiplier(double d) {
        damageMultiplier = d;
    }

    public void attack(Fighter f) {
        super.attack(f);
    }

    public void die() {
        super.die();
    }

}

class Enemy extends Fighter {
    private double health;
    private double damage;
    private boolean isAlive;
    private double healthMultiplier;
    private double damageMultiplier;
    public Enemy(Difficulty d, double health, double damage) {
        super(d, health, damage);
    }

    public void setHealth() {

    }

    @Override
    public void attack(Fighter f) {
        super.attack(f);
    }

    public void die() {
        super.die();
    }
}*/
