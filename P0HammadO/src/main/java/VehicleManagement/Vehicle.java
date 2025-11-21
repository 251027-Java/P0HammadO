package VehicleManagement;

public class Vehicle {

    private String make;
    private String model;
    private String VIN; //this is the unique identifier for each vehicle(usually 17 digits)
    private int year;
    private VehicleStatus status; // creates a status variable to allocate process of the vehicles history with dealership

    public enum VehicleStatus {
        IN_OFFICE, //location of vehicle is in office
        OUTGOING, //location of vehicle will be described as outgoing(body shops, mechanic, pdr)
        AT_AUCTION, // which auction has the vehicle moved to?
        IN_AUDIT // audited vehicle just means it will be called for in office inventory
    }

    //constructor
    public Vehicle(String VIN, String make, String model, int year){
        this.VIN = VIN;
        this.make = make;
        this.model = model;
        this.year = year;
        this.status = VehicleStatus.IN_OFFICE;
    }

    //Getters and Setters
    public String getVin(){
        return VIN;
    }

    public void setVin(String VIN){
        this.VIN = VIN;
    }

    public String getMake(){
        return make;
    }

    public void setMake(String make){
        this.make = make;
    }

    public String getModel(){
        return model;
    }

    public void setModel(String model){
        this.model = model;
    }

    public int getYear(){
        return year;
    }

    public void setYear(int year){
        this.year = year;
    }

    public VehicleStatus getStatus(){
        return status;
    }

    public void setStatus(VehicleStatus status){
        this.status = status;
    }

    @Override
    public String toString(){
        return String.format("VIN: %s | %d %s %s | Status: %s", VIN, year, make, model, status);

    }
}

