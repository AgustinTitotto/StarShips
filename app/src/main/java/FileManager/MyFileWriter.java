package FileManager;

import Model.MovingEntity;
import Model.Ship;

import java.io.*;
import java.util.List;

public class MyFileWriter {

    private final String fileName;

    public MyFileWriter(String fileName) {
        this.fileName = fileName;
    }

    public void saveGame(Ship ship, List<MovingEntity> entities) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        ObjectOutputStream o = new ObjectOutputStream(fileOutputStream);
        saveShipState(o, ship);
        o.writeObject(entities.size());
        saveEntities(o, entities);
        //FileWriter fileWriter = new FileWriter(fileName);
        /*saveEntities(fileWriter, entities);
        saveEntitiesToRemove(fileWriter, entitiesToRemove);
        fileWriter.close();*/
    }

    private void saveShipState(ObjectOutputStream fileWriter, Ship ship) throws IOException {
        fileWriter.writeObject(ship);
    }

    private void saveEntities(ObjectOutputStream o, List<MovingEntity> entities) throws IOException {
        for (MovingEntity entity : entities){
            o.writeObject(entity);
        }
    }


    /*private void saveShipState(FileWriter fileWriter, Ship ship) throws IOException {
        fileWriter.write(ship.getId() + "/");
        fileWriter.write(ship.getTopSpeed() + "/");
        fileWriter.write(ship.getAcceleration() + "/");
        fileWriter.write(ship.getSpeed() + "/");
        fileWriter.write(ship.getWeapon().getDamage() + "/");
        fileWriter.write(ship.getLives() + "/");
        fileWriter.write(ship.getPosition().getX() + "/");
        fileWriter.write(ship.getPosition().getY() + "/");
        fileWriter.write(ship.getDegrees() + "/");
        fileWriter.write(ship.getSize() + "/");
        fileWriter.write(ship.getColliderType().toString() + "/");
        fileWriter.flush();
    }

    private void saveEntities(FileWriter fileWriter, List<MovingEntity> entities) throws IOException {
        for (MovingEntity entity : entities) {
            writeEntityArguments(fileWriter, entity);
        }
        fileWriter.write("ETR" + "/");
        fileWriter.flush();
    }

    private void saveEntitiesToRemove(FileWriter fileWriter, List<MovingEntity> entitiesToRemove) throws IOException {
        for (MovingEntity entity : entitiesToRemove) {
            writeEntityArguments(fileWriter, entity);
        }
    }

    private void writeEntityArguments(FileWriter fileWriter, MovingEntity entity) throws IOException {
        fileWriter.write(entity.getId() + "/");
        fileWriter.write(entity.getPosition().getX() + "/");
        fileWriter.write(entity.getPosition().getY() + "/");
        fileWriter.write(entity.getDegrees() + "/");
        fileWriter.write(entity.getSpeed() + "/");
        fileWriter.write(entity.getSize() + "/");
        fileWriter.write(entity.getColliderType() + "/");
        fileWriter.flush();
    }*/
}
