import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.util.List;

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
    public SingleRoom() { super("Single Room", 1, 100.00); }
    @Override
    public String getFeatures() { return "Compact space, high-speed WiFi, Desk."; }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double Room", 2, 180.00); }
    @Override
    public String getFeatures() { return "Queen size beds, Mini-fridge, City view."; }
}

class ReservationRequest {
    private String guestName;
    private String roomType;

    public ReservationRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return "Request [Guest: " + guestName + ", Type: " + roomType + "]";
    }
}

class BookingQueueManager {
    private Queue<ReservationRequest> requestQueue;

    public BookingQueueManager() {
        this.requestQueue = new LinkedList<>();
    }

    public void addRequest(ReservationRequest request) {
        requestQueue.add(request);
        System.out.println("Enqueued: " + request);
    }

    public Queue<ReservationRequest> getRequestQueue() {
        return requestQueue;
    }

    public void displayQueueStatus() {
        System.out.println("\n--- Current Booking Queue (FIFO Order) ---");
        if (requestQueue.isEmpty()) {
            System.out.println("Queue is empty.");
        } else {
            int position = 1;
            for (ReservationRequest request : requestQueue) {
                System.out.println(position + ". " + request);
                position++;
            }
        }
        System.out.println("------------------------------------------\n");
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        BookingQueueManager queueManager = new BookingQueueManager();

        System.out.println("System: Receiving incoming booking requests...");

        queueManager.addRequest(new ReservationRequest("Alice", "Single Room"));
        queueManager.addRequest(new ReservationRequest("Bob", "Suite"));
        queueManager.addRequest(new ReservationRequest("Charlie", "Double Room"));
        queueManager.addRequest(new ReservationRequest("Diana", "Single Room"));

        queueManager.displayQueueStatus();

        System.out.println("System Note: Requests are queued and waiting for allocation logic.");
    }
}