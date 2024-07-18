package sk.stuba.fei.uim.vsa.pr2.web.response;


import java.util.List;

public class ParkingSpotDto extends Dto{

    private String identifier;
    private String carParkFloor;
    private Long carPark;
    private boolean free;
    private List<Reservation2Dto> reservations;

    public ParkingSpotDto() {
    }

    public ParkingSpotDto(Long id, String identifier, String carParkFloor, Long carPark, boolean free, List<Reservation2Dto> reservations) {
        super(id);
        this.identifier = identifier;
        this.carParkFloor = carParkFloor;
        this.carPark = carPark;
        this.free = free;
        this.reservations = reservations;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCarParkFloor() {
        return carParkFloor;
    }

    public void setCarParkFloor(String carParkFloor) {
        this.carParkFloor = carParkFloor;
    }

    public Long getCarPark() {
        return carPark;
    }

    public void setCarPark(Long carPark) {
        this.carPark = carPark;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public List<Reservation2Dto> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation2Dto> reservations) {
        this.reservations = reservations;
    }
}
