package VehicleManagement;

import java.time.LocalDateTime;

public class InventoryMovements {
    private int movementId;
    private String VIN;
    private int userId; //this will be the foreign key from the User class. representing a many-many
    private String destination;
    private double gasCharge;
    private String movementDate;

    public enum MovementDestination {
        REVAUCTION("REVauction"),
        COGAUCTION("COGauction"),
        METROAUCTION("Metroauction"),
        AUDIT("Audit");

        private final String displayName;

        MovementDestination(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public boolean isAuction() {
            return this == REVAUCTION || this == COGAUCTION || this == METROAUCTION;
        }
    }

    // constructors
    public InventoryMovements(int movementId, String VIN, int userName, String destination, double gasCharge) {
        this.movementId = movementId;
        this.VIN = VIN;
        this.userId = userId;
        this.destination = destination;
        this.gasCharge = gasCharge;
        this.movementDate = LocalDateTime.now().toString();
    }

    //getters and setters
    public int getMovementId() {
        return movementId;
    }

    public void setMovementId(int movementId) {
        this.movementId = movementId;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getGasCharge() {
        return gasCharge;
    }

    public void setGasCharge(double gasCharge) {
        this.gasCharge = gasCharge;
    }

    public String getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(String movementDate) {
        this.movementDate = movementDate;
    }

    //we need to generate a success message, basically letting the user know or manager of the system,
    // the final update on the vehicle(whether it is in auction or in office until further notice
    public boolean isAuction(){
        return destination.equals("REVauction") ||
                destination.equals("COGauction") ||
                destination.equals("Metroauction");

    }

    @Override
    public String toString(){
        return "Movement #" + movementId + " | VIN: " + VIN + " | User: " + userId + " | To: "
                + destination + " | Gas: $" + gasCharge;
    }
}
