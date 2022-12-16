package Controller;

import Model.*;
import edu.austral.ingsis.starships.ui.ElementColliderType;
import edu.austral.ingsis.starships.ui.ElementModel;
import javafx.collections.ObservableMap;
import javafx.scene.input.KeyCode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static Controller.Config.*;


public record GameController(GameState gameState) {

    public GameController moveElements() {
        if (!this.gameState.gamePaused()){
            Ship ship = this.gameState().shipController().ship();
            generateAsteroids();
            List<MovingEntity> entities = moveEntities(this.gameState.entities());
            List<MovingEntity> entitiesToRemove = entitiesToRemove(entities);
            return moveShip(ship, entities, entitiesToRemove);
        }
        else return new GameController(this.gameState);
    }

    private void generateAsteroids() {
        if (Math.random() < ASTEROID_SPAWN_CHANCE) {
            Position asteriodSpawnPosition = asteroidSpawnPosition();
            double asteroidSize = Math.random() * (ASTEROID_MAX_SIZE - ASTEROID_MIN_SIZE) + ASTEROID_MIN_SIZE;
            this.gameState.entities().add(new Asteroid("asteroid" + UUID.randomUUID(),
                    asteriodSpawnPosition, setAsteroidSpeed(asteroidSize), getDegreesFromSpawningPosition(asteriodSpawnPosition), asteroidSize, ElementColliderType.Elliptical));
        }
    }

    private double setAsteroidSpeed(double asteroidSize) {
        double a = ((ASTEROID_MIN_SPEED - ASTEROID_MAX_SPEED) / (ASTEROID_MAX_SIZE - ASTEROID_MIN_SIZE));
        return a * asteroidSize + (ASTEROID_MAX_SPEED - ASTEROID_MIN_SIZE * a);
    }

    private double getDegreesFromSpawningPosition(Position asteriodSpawnPosition) {
        if (asteriodSpawnPosition.x() == 0)
            return Math.random() * 179.9 + 0.01;
        else if (asteriodSpawnPosition.y() == Y_SIZE - Y_MARGIN)
            return Math.random() * 179.9 + 90.01;
        else if (asteriodSpawnPosition.x() == X_SIZE - X_MARGIN)
            return Math.random() * 179.9 + 180.01;
        else
            return Math.random() * 179.9 + 270.01;
    }

    private Position asteroidSpawnPosition() {
        double randomNumber = Math.random();
        if (randomNumber < 0.25)
            return new Position(0, Y_SIZE * Math.random());
        else if (randomNumber < 0.5)
            return new Position(X_SIZE * Math.random(), Y_SIZE - Y_MARGIN);
        else if (randomNumber < 0.75)
            return new Position(X_SIZE - X_MARGIN, Y_SIZE * Math.random());
        else
            return new Position(X_SIZE * Math.random(), 0);
    }

    @NotNull
    private GameController moveShip(Ship ship, List<MovingEntity> entities, List<MovingEntity> entitiesToRemove) {
        if (shipIsOutOfBound(ship)) {
            return new GameController(new GameState(new ShipController(ship), entities, entitiesToRemove, false));
        } else {
            return new GameController(new GameState(new ShipController(new Ship(ship.id(), ship.getTopSpeed(), ship.getAcceleration(), ship.speed(), ship.getWeapon(), ship.getLives(), new Position(getPosition(ship.position().x(), calculateXCoefficient(ship.degrees()), ship.speed()), getPosition(ship.position().y(), calculateYCoefficient(ship.degrees()), ship.speed())), ship.degrees(), ship.size(), ship.colliderType(), ship.getPoints())), entities, entitiesToRemove, false));
        }
    }


    private List<MovingEntity> moveEntities(List<MovingEntity> entities) {
        List<MovingEntity> newEntities = new ArrayList<>();
        for (MovingEntity entity : entities) {
            /*
            if (entity instanceof Bullet) {
                newEntities.add(new Bullet(entity.id(), new Position(getPosition(entity.position().x(), calculateXCoefficient(entity.degrees()), entity.speed()), getPosition(entity.position().y(), calculateYCoefficient(entity.degrees()), entity.speed())), entity.speed(), entity.degrees(), entity.size(), entity.colliderType()));
            } else {
                newEntities.add(new Asteroid(entity.id(), new Position(getPosition(entity.position().x(), calculateXCoefficient(entity.degrees()), entity.speed()), getPosition(entity.position().y(), calculateYCoefficient(entity.degrees()), entity.speed())), entity.speed(), entity.degrees(), entity.size(), entity.colliderType()));
            }*/
            newEntities.add(entity.move(getPosition(entity.position().x(), calculateXCoefficient(entity.degrees()), entity.speed()), getPosition(entity.position().y(), calculateYCoefficient(entity.degrees()), entity.speed())));
        }
        return newEntities;
    }

    private List<MovingEntity> entitiesToRemove(List<MovingEntity> entities) {
        List<MovingEntity> newEntitiesToRemove = new ArrayList<>();
        for (MovingEntity entity : entities) {
            if (entityIsOutOfBound(entity.position().x(), entity.position().y())) {
                newEntitiesToRemove.add(entity);
            }
        }
        return newEntitiesToRemove;
    }

    private static boolean entityIsOutOfBound(double posX, double posY) {
        return posX >= X_SIZE - X_MARGIN || posY >= Y_SIZE - Y_MARGIN ||
                posX <= 0 || posY <= 0;
    }

    private boolean shipIsOutOfBound(Ship ship) {
        return entityIsOutOfBound(getPosition(ship.position().x(), calculateXCoefficient(ship.degrees()), ship.speed()), getPosition(ship.position().y(), calculateYCoefficient(ship.degrees()), ship.speed()));
    }

    private double getPosition(double position, double coefficient, double speed) {
        return position + coefficient * speed;
    }

    public GameController handleKeyPressed(KeyCode keyCode) throws IOException {
        return new GameController(gameState.handleKeyPressed(keyCode));
    }

    private double calculateYCoefficient(double rotation) {
        return -Math.cos(Math.toRadians(rotation));
    }

    private double calculateXCoefficient(double rotation) {
        return Math.sin(Math.toRadians(rotation));
    }

    @NotNull
    public GameController handleCollision(@NotNull String element1Id, @NotNull String element2Id, ObservableMap<String, ElementModel> elements) {
        MovingEntity element1 = findEntityById(element1Id);
        MovingEntity element2 = findEntityById(element2Id);
        if (/*element1.id().startsWith("s") && element2.id().startsWith("a") && */element1 instanceof Ship && element2 instanceof Asteroid) {
            return shipAsteroidCollision();
        } else if (/*element1.id().startsWith("a") && element2.id().startsWith("b") && */element1 instanceof Asteroid && element2 instanceof Bullet) {
            return asteroidBulletCollision(elements, element1, element2);
        } else return this;
    }

    @NotNull
    private GameController asteroidBulletCollision(ObservableMap<String, ElementModel> elements, MovingEntity element1, MovingEntity element2) {
        List<MovingEntity> entities = gameState.entities();
        entities.remove(element1);
        entities.remove(element2);
        if ((element1.size() - gameState.shipController().ship().getWeapon().damage()) < ASTEROID_MIN_SIZE) {
            elements.remove(element1.id());
        } else {
            entities.add(new Asteroid(element1.id(), element1.position(), element1.speed(), element1.degrees(), element1.size() - gameState.shipController().ship().getWeapon().damage(), element1.colliderType()));
        }
        elements.remove(element2.id());
        Ship ship = gameState.shipController().ship();
        return new GameController(new GameState(new ShipController(new Ship(ship.id(), ship.getTopSpeed(), ship.getAcceleration(), ship.speed(), ship.getWeapon(), ship.getLives(), ship.position(), ship.degrees(), ship.size(), ship.colliderType(), ship.getPoints() + 20)), entities, gameState.entitiesToRemove(), false));
    }

    @NotNull
    private GameController shipAsteroidCollision() {
        Ship ship = gameState.shipController().ship();
        return new GameController(new GameState(new ShipController(new Ship(ship.id(), ship.getTopSpeed(), ship.getAcceleration(), ship.speed(), ship.getWeapon(), ship.getLives() - 1, ship.position(), ship.degrees(), ship.size(), ship.colliderType(), ship.getPoints())), gameState.entities(), gameState.entitiesToRemove(), false));
    }

    @NotNull
    private MovingEntity findEntityById(String elementId) {
        for (int i = 0; i < gameState.entities().size(); i++) {
            if (gameState.entities().get(i).id().equalsIgnoreCase(elementId)) {
                return gameState.entities().get(i);
            }
        }
        return gameState.shipController().ship();
    }

    @NotNull
    public GameController cleanElements(GameController gameController) {
        List<MovingEntity> newMovingEntities = new ArrayList<>();
        for (MovingEntity entity : gameController.gameState.entities()) {
            if (!gameController.gameState.entitiesToRemove().contains(entity)) {
                newMovingEntities.add(entity);
            }
        }
        return new GameController(new GameState(gameController.gameState.shipController(), newMovingEntities, gameController.gameState.entitiesToRemove(), gameState.gamePaused()));
    }
}
