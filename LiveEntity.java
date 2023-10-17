package BookwormAdventures;

import java.awt.*;

public interface LiveEntity {
    public double getHealth();
    public void setHealth(double h);
    public void setDamage(double d);
    public double getDamage();
    public boolean checkAlive();
    public void die();
    //public void attack();
    public void takeDamage(double d);
    public String speak();
}
