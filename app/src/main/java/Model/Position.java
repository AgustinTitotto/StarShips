package Model;

import java.io.Serializable;

public class Position implements Serializable {

    private final double x;
    private final double y;

    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
