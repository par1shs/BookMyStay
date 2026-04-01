import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;
    private double basePrice;

    public Reservation(String guestName, String roomType, String roomId, double basePrice) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.basePrice = basePrice;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
    public double getBasePrice() { return basePrice; }

    @Override
    public String toString() {
        return String.format("ID: %s | Guest: %-10s | Type: %-12s | Price: $%.2f",
                roomId, guestName, roomType, basePrice);
    }
}

class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void recordBooking(Reservation reservation) {
        history.add(reservation);
    }

    public List<Reservation> getHistory() {
        return Collections.unmodifiableList(history);
    }
}

class BookingReportService {
    private BookingHistory historyStore;

    public BookingReportService(BookingHistory historyStore) {
        this.historyStore = historyStore;
    }

    public void generateSummaryReport() {
        List<Reservation> records = historyStore.getHistory();
        double totalRevenue = 0;

        System.out.println("======= SYSTEM AUDIT: CONFIRMED BOOKINGS =======");
        if (records.isEmpty()) {
            System.out.println("No records found.");
        } else {
            for (Reservation res : records) {
                System.out.println(res);
                totalRevenue += res.getBasePrice();
            }
        }
        System.out.println("================================================");
        System.out.println("Total Bookings: " + records.size());
        System.out.printf("Total Revenue:  $%.2f%n", totalRevenue);
        System.out.println("================================================");
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        BookingHistory historyStore = new BookingHistory();
        BookingReportService reportService = new BookingReportService(historyStore);

        historyStore.recordBooking(new Reservation("Alice", "Single Room", "S-101", 100.00));
        historyStore.recordBooking(new Reservation("Bob", "Double Room", "D-202", 180.00));
        historyStore.recordBooking(new Reservation("Charlie", "Suite", "ST-303", 350.00));

        reportService.generateSummaryReport();
    }
}