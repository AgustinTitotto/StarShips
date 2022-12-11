package Controller;

import FileManager.MyFileReader;
import FileManager.MyFileWriter;
import Model.Bullet;
import Model.MovingEntity;
import edu.austral.ingsis.starships.ui.ElementColliderType;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.util.List;;
import java.util.UUID;

import static Controller.Config.*;


public record GameState(ShipController shipController, List<MovingEntity> entities,
                        List<MovingEntity> entitiesToRemove, boolean gamePaused) {


    public GameState handleKeyPressed(KeyCode keyCode) throws IOException {
        ShipController newShipController;
        if (gamePaused){
            switch (keyCode) {
                case P -> {
                    newShipController = this.shipController;
                    return new GameState(newShipController, this.entities, this.entitiesToRemove, false);
                }
                case S -> {
                    MyFileWriter fileWriter = new MyFileWriter(System.getProperty("user.dir") + "/app/src/main/java/FileManager/SavedGameFile.txt");
                    fileWriter.saveGame(this.shipController.ship(), this.entities);
                }
                case H -> {
                    MyFileReader fileReader = new MyFileReader(System.getProperty("user.dir") + "/app/src/main/java/FileManager/SavedGameFile.txt");
                    try {
                        fileReader.loadGame();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return this;
        }
        else {
            switch (keyCode) {
                case UP -> newShipController = shipController.increaseSpeed();
                case DOWN -> newShipController = shipController.stop();
                case LEFT -> newShipController = shipController.changeDirection(-ROTATION_SPEED);
                case RIGHT -> newShipController = shipController.changeDirection(+ROTATION_SPEED);
                case SPACE -> {
                    newShipController = this.shipController;
                    entities.add(new Bullet("bullet" + UUID.randomUUID(), this.shipController.ship().getPosition(), this.shipController().ship().getTopSpeed() + BULLET_SPEED_MARGIN, this.shipController.ship().getDegrees(), NORMAL_BULLET_SIZE, ElementColliderType.Rectangular));
                }
                case P -> {
                    newShipController = this.shipController;
                    return new GameState(newShipController, this.entities, this.entitiesToRemove, true);
                }
                default -> newShipController = this.shipController;
            }
            return new GameState(newShipController, this.entities, this.entitiesToRemove, gamePaused);
        }

    }
}