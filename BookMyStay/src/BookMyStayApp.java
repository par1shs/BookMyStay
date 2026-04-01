abstract class Room {
    private String type;
    private int numBeds;
    private double pricePerNight;

    public Room(String type, int numBeds, double pricePerNight) {
        this.type = type;
        this.numBeds = numBeds;
        this.pricePerNight = pricePerNight;
    }

    public String getType() { return type; }
    public int getNumBeds() { return numBeds; }
    public double getPricePerNight() { return pricePerNight; }

    public abstract String getFeatures();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 100.00);
    }

    @Override
    public String getFeatures() {
        return "Compact space, high-speed WiFi, Desk.";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 180.00);
    }

    @Override
    public String getFeatures() {
        return "Queen size beds, Mini-fridge, City view.";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite", 2, 350.00);
    }

    @Override
    public String getFeatures() {
        return "King beds, Living area, Complimentary Spa access.";
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        Room single = new SingleRoom();
        Room doubleRm = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 1;

        System.out.println("--- BookMyStay Inventory System ---");

        displayRoomInfo(single, singleAvailability);
        displayRoomInfo(doubleRm, doubleAvailability);
        displayRoomInfo(suite, suiteAvailability);
    }

    public static void displayRoomInfo(Room room, int count) {
        System.out.println("\nType: " + room.getType());
        System.out.println("Beds: " + room.getNumBeds());
        System.out.println("Price: $" + room.getPricePerNight());
        System.out.println("Features: " + room.getFeatures());
        System.out.println("Rooms Available: " + count);
        System.out.println("-----------------------------------");
    }
}