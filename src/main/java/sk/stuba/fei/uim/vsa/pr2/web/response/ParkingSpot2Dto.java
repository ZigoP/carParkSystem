package sk.stuba.fei.uim.vsa.pr2.web.response;

public class ParkingSpot2Dto extends Dto{

    private String identifier;
    private String carParkFloor;
    private Long carPark;
    private boolean free;

    public ParkingSpot2Dto() {
    }

    public ParkingSpot2Dto(Long id, String identifier, String carParkFloor, Long carPark, boolean free) {
        super(id);
        this.identifier = identifier;
        this.carParkFloor = carParkFloor;
        this.carPark = carPark;
        this.free = free;
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
}
