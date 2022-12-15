package FileManager;

import Model.MovingEntity;
import Model.Ship;

import java.io.*;
import java.util.List;

public class SaveGame {

    private final String fileName;

    public SaveGame(String fileName) {
        this.fileName = fileName;
    }

    public void saveGame(Ship ship, List<MovingEntity> entities) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        ObjectOutputStream o = new ObjectOutputStream(fileOutputStream);
        saveShipState(o, ship);
        o.writeObject(entities.size());
        saveEntities(o, entities);
    }

    private void saveShipState(ObjectOutputStream fileWriter, Ship ship) throws IOException {
        fileWriter.writeObject(ship);
    }

    private void saveEntities(ObjectOutputStream o, List<MovingEntity> entities) throws IOException {
        for (MovingEntity entity : entities){
            o.writeObject(entity);
        }
    }
}
