package VehicleManagement;

import java.util.List;


public class InventoryService {
    private VehiclesRepository repo;

    public InventoryService(VehiclesRepository repo){
        this.repo = repo;

    }

    public Vehicle addVehicle(String VIN, String make, String model, int year){
        if(VIN.length() != 17){
            System.out.println("VIN must be 17 characters");
            return null;
        }
        if(repo.getVehicle(VIN) != null){
            System.out.println("Vehicle already exists");
            return null;
        }

        Vehicle v = new Vehicle(VIN, make, model, year);
        repo.saveVehicle(v);
        return v;

    }
    public List<Vehicle> getAllVehicles(){return repo.getAllVehicles();}
    public Vehicle getVehicle(String VIN){return repo.getVehicle(VIN);}
    public List<Vehicle> getAllVehicleStatus(){return repo.getAllVehicles();}

    public void updateVehicleStatus(String VIN, String status){
        Vehicle vehicle = repo.getVehicle(VIN);
        if(vehicle != null) {
            vehicle.setStatus(Vehicle.VehicleStatus.valueOf(status));
            repo.saveVehicle(vehicle);
        }

    }

    public User addUser(String name, String role){
        int nextId = repo.getNextUserId();
        User user = new User(nextId, name, role);
        repo.saveUser(user);
        return user;
    }

    public User getUser(int userId){
        return repo.getUser(userId);
    }

    public List<User> getAllUsers(){
        return repo.getAllUsers();
    }

    public IncomingInventory processIncoming(String VIN, String issueDescription){
        if(repo.getVehicle(VIN) == null){
            System.out.println("Vehicle not found");
            return null;
        }

        int nextId = repo.getNextIncomingId();
        IncomingInventory incoming = new IncomingInventory(nextId, VIN, issueDescription);
        repo.saveIncoming(incoming);

        if(incoming.hasNoIssues()){
            updateVehicleStatus(VIN, "IN_OFFICE");
            System.out.println("Vehicle marked as IN_OFFICE (no issues)");
        }else{
            System.out.println("Vehicle has issues - needs shop assignment");
        }
        return incoming;
    }

    public List<IncomingInventory> getAllIncoming(){
        return repo.getAllIncoming();
    }

    public OutgoingInventory sendToShop(String VIN, String issueDescription, String shopType){
        if(repo.getVehicle(VIN) == null){
            System.out.println("Vehicle not in system.");
            return null;
        }

        int nextId = repo.getNextOutgoingId();
        OutgoingInventory outgoing = new OutgoingInventory(nextId, VIN, issueDescription, shopType);
        repo.saveOutgoing(outgoing);

        updateVehicleStatus(VIN, "OUTGOING");
        System.out.println("Vehicle sent to " + shopType);
        System.out.println("Expected Return: " + outgoing.getExpectedReturn());

        return outgoing;
    }

    public void markReturned(String VIN){
        OutgoingInventory outgoing = repo.getOutgoingByVin(VIN);
        if(outgoing == null){
            System.out.println("Vehicle is back in office, onto the next steps!");
            return;
        }

        updateVehicleStatus(VIN, "IN_OFFICE");
        repo.deleteOutgoing(outgoing.getOutgoingId());
        System.out.println("Vehicle returned to office");
    }

    public List<OutgoingInventory> getAllOutgoing(){
        return repo.getAllOutgoing();
    }

    public InventoryMovements recordMovement(String VIN, int userId, String destination, double gasCharge){
        Vehicle vehicle = repo.getVehicle(VIN);
        if(vehicle == null){
            System.out.println("Vehicle not found");
            return null;
        }

        User user = repo.getUser(userId);
        if(user == null){
            System.out.println("User not found");
            return null;
        }
        int nextId = repo.getNextMovementId();
        InventoryMovements movement = new InventoryMovements(nextId, VIN, userId, destination, gasCharge);
        repo.saveMovement(movement);

        if(movement.isAuction()){
            updateVehicleStatus(VIN, "AT_AUCTION");
            System.out.println(user.getName() + " successfully dropped off vehicle to " + destination +
                    " and charged the vehicle $" + gasCharge + " in gas. LETS GET IT SOLD!");
        }else{
            updateVehicleStatus(VIN, "IN_OFFICE");
            System.out.println(user.getName() + " moved vehicle to Audit");
        }

        return movement;

    }

    public List<InventoryMovements> getAllMovements(){
        return repo.getAllMovements();
    }

    public double calculateUserEarnings(int userId){
        List<InventoryMovements> movements = repo.getMovementsByUser(userId);
        double total = 0;
        for (InventoryMovements m : movements){
            total += m.getGasCharge();
        }
        return total;
    }
}


