package Controller;

import Model.Ship;

public record ShipController(Ship ship) {

    public ShipController increaseSpeed() {
        if (ship.getSpeed() < ship.getTopSpeed()) {
            return new ShipController(new Ship(ship.getId(), ship.getTopSpeed(), ship.getAcceleration(), ship.getSpeed() + ship.getAcceleration(), ship.getWeapon(), ship.getLives(), ship.getPosition(), ship.getDegrees(), ship.getSize(), ship.getColliderType(), ship.getPoints()));
        } else {
            return this;
        }
    }

    public ShipController stop() {
        if (ship.getSpeed() > 0.0) {
            return new ShipController(new Ship(ship.getId(), ship.getTopSpeed(), ship.getAcceleration(), 0, ship.getWeapon(), ship.getLives(), ship.getPosition(), ship.getDegrees(), ship.getSize(), ship.getColliderType(), ship.getPoints()));
        } else {
            return this;
        }
    }

    public ShipController changeDirection(double degrees) {
        return new ShipController(new Ship(ship.getId(), ship.getTopSpeed(), ship.getAcceleration(), ship.getSpeed(), ship.getWeapon(), ship.getLives(), ship.getPosition(), ship.getDegrees() + degrees,  ship.getSize(), ship.getColliderType(), ship.getPoints()));
    }
}
