package sk.stuba.fei.uim.vsa.pr2.web.response;

import sk.stuba.fei.uim.vsa.pr2.entities.CarPark;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

public class CarParkFloorDto extends Dto{

    private String identifier;
    private Long carPark;
    private List<ParkingSpotDto> spots;

    public CarParkFloorDto() {
    }

    public CarParkFloorDto(Long id, String identifier, Long carPark, List<ParkingSpotDto> spots) {
        super(id);
        this.identifier = identifier;
        this.carPark = carPark;
        this.spots = spots;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getCarPark() {
        return carPark;
    }

    public void setCarPark(Long carPark) {
        this.carPark = carPark;
    }

    public List<ParkingSpotDto> getSpots() {
        return spots;
    }

    public void setSpots(List<ParkingSpotDto> spots) {
        this.spots = spots;
    }
}
