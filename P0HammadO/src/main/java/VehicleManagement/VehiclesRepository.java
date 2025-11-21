package VehicleManagement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VehiclesRepository {

    private Gson gson;

    private String vehiclesFile = "Data/vehicles.json";
    private String usersFile = "data/users.json";
    private String incomingFile = "data/incoming_inventory.json";
    private String outgoingFile = "data/outgoing_inventory.json";
    private String movementsFile = "data/inventory_movements.json";

    public VehiclesRepository(){
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        initializeFiles();
    }

    private void initializeFiles(){
        createFileIfNotExists(vehiclesFile);
        createFileIfNotExists(usersFile);
        createFileIfNotExists(incomingFile);
        createFileIfNotExists(outgoingFile);
        createFileIfNotExists(movementsFile);
    }

    private void createFileIfNotExists(String filepath){
        File file = new File(filepath);
        if(!file.exists()){
            try{
                file.getParentFile().mkdirs();
                file.createNewFile();
                writeToFile(filepath, new ArrayList<>());
            } catch (IOException e){
                System.out.println("Error creating: " + filepath);
            }
        }
    }

    private <T> List<T> readFromFile(String filepath, Type type) {
        try(Reader reader = new FileReader(filepath)){
            List<T> data = gson.fromJson(reader, type);
            return data != null ? data : new ArrayList<>();
        } catch (IOException e){
            return new ArrayList<>();
        }
    }

    private <T> void writeToFile(String filepath, List<T> data){
        try(Writer writer = new FileWriter(filepath)){
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.out.println("Error writing to: " + filepath);
        }

    }

    private <T> int getNextId(List<T> list, java.util.function.ToIntFunction<T> getter){
        return list.stream().mapToInt(getter).max().orElse(0) + 1;
    }

    private <T> T findBy(List<T> list, java.util.function.Predicate<T> p){
        return list.stream().filter(p).findFirst().orElse(null);
    }

    public void saveVehicle(Vehicle vehicle){
        List<Vehicle> vehicles = getAllVehicles();
        vehicles.removeIf(v -> v.getVin().equals(vehicle.getVin()));
        vehicles.add(vehicle);
        writeToFile(vehiclesFile, vehicles);
    }

    public Vehicle getVehicle(String VIN){
        return findBy(getAllVehicles(), v -> v.getVin().equals(VIN));
    }

    public List<Vehicle> getAllVehicles(){
        return readFromFile(vehiclesFile, new TypeToken<List<Vehicle>>(){}.getType());
    }

    public void saveUser(User user){
        List<User> users = getAllUsers();
        users.removeIf(u -> u.getUserId() == user.getUserId());
        users.add(user);
        writeToFile(usersFile, users);
    }

    public User getUser(int userId){
        return findBy(getAllUsers(), u -> u.getUserId() == userId);
    }

    public List<User> getAllUsers(){
        return readFromFile(usersFile, new TypeToken<List<User>>(){}.getType());
    }

    public int getNextUserId(){
        return getNextId(getAllUsers(), User::getUserId);
    }

    public void saveIncoming(IncomingInventory incoming){
        List<IncomingInventory> records = getAllIncoming();
        records.removeIf(r -> r.getIncomingID() == incoming.getIncomingID());
        records.add(incoming);
        writeToFile(incomingFile, records);
    }

    public List<IncomingInventory> getAllIncoming(){
        return readFromFile(incomingFile, new TypeToken<List<IncomingInventory>>(){}.getType());
    }

    public int getNextIncomingId(){
        return getNextId(getAllIncoming(), IncomingInventory::getIncomingID);
    }
    public void saveOutgoing(OutgoingInventory outgoing){
        List<OutgoingInventory> records = getAllOutgoing();
        records.removeIf(r -> r.getOutgoingId() == outgoing.getOutgoingId());
        records.add(outgoing);
        writeToFile(outgoingFile, records);
    }

    public OutgoingInventory getOutgoingByVin(String VIN){
        return findBy(getAllOutgoing(), r -> r.getVIN().equals(VIN));
    }

    public List<OutgoingInventory> getAllOutgoing(){
        return readFromFile(outgoingFile, new TypeToken<List<OutgoingInventory>>(){}.getType());
    }

    public int getNextOutgoingId(){
        return getNextId(getAllOutgoing(), OutgoingInventory::getOutgoingId);

    }

    public void deleteOutgoing(int outgoingId){
        List<OutgoingInventory> records = getAllOutgoing();
        records.removeIf(r -> r.getOutgoingId() == outgoingId);
        writeToFile(outgoingFile, records);
    }

    public void saveMovement(InventoryMovements movement){
        List<InventoryMovements> movements = getAllMovements();
        movements.removeIf(m -> m.getMovementId() == movement.getMovementId());
        movements.add(movement);
        writeToFile(movementsFile, movements);
    }

    public List<InventoryMovements> getAllMovements(){
        return readFromFile(movementsFile, new TypeToken<List<InventoryMovements>>(){}.getType());
    }

    public List<InventoryMovements> getMovementsByUser(int userId){
        List<InventoryMovements> movements = getAllMovements();
        List<InventoryMovements> result = new ArrayList<>();
        for(InventoryMovements m : movements){
            if(m.getUserId() == userId){
                result.add(m);
            }
        }
        return result;
    }

    public int getNextMovementId(){
        return getNextId(getAllMovements(), InventoryMovements::getMovementId);
    }

}
