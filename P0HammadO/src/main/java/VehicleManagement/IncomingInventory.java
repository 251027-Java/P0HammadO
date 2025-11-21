package VehicleManagement;

public class IncomingInventory{ // vehicles will start here when they arrive at office

    private int incomingID; //unique identifier for incoming vehicles
    private String VIN;
    private String issueDescription; //allows user to input description of vehicle
    private String dateReceived;

    //constructor
    public IncomingInventory(int incomingID, String VIN, String issueDescription){
        this.incomingID = incomingID;
        this.VIN = VIN;
        this.issueDescription = issueDescription;
        this.dateReceived = dateReceived;
    }

    //getters and setters


    public int getIncomingID() {
        return incomingID;
    }

    public void setIncomingID(int incomingID) {
        this.incomingID = incomingID;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

    //this is the method to check if vehicle has no issues
    public boolean hasNoIssues(){
        String desc = issueDescription.trim().toLowerCase();
        return desc.equals("n/a") ||
                desc.equals("no issues") ||
                desc.equals("none");

    }
    @Override
    public String toString(){
        return String.format("Incoming #%d | VIN: %s | Received: %s\nissues: %s",
                incomingID, VIN, dateReceived, issueDescription);
    }
}
