package Model;

import java.io.Serializable;

public class Weapon implements Serializable {


    private final double damage;
    private final double fireRate;

    public Weapon(double damage, double fireRate){
        this.damage = damage;
        this.fireRate = fireRate;
    }

    public double getDamage() {
        return damage;
    }

    public double getFireRate() {
        return fireRate;
    }
}
