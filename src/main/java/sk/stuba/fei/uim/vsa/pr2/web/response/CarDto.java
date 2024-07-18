package sk.stuba.fei.uim.vsa.pr2.web.response;

import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;

import java.util.List;
import java.util.Objects;

public class CarDto extends Dto{

    private String brand;
    private String model;
    private String vrp;
    private String colour;
    private List<Reservation2Dto> reservations;

    public CarDto() {
    }

    public CarDto(Long id, String brand, String model, String vrp, String colour, List<Reservation2Dto> reservations) {
        super(id);
        this.brand = brand;
        this.model = model;
        this.vrp = vrp;
        this.colour = colour;
        this.reservations = reservations;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVrp() {
        return vrp;
    }

    public void setVrp(String vrp) {
        this.vrp = vrp;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public List<Reservation2Dto> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation2Dto> reservations) {
        this.reservations = reservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarDto carDto = (CarDto) o;
        return vrp.equals(carDto.vrp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vrp);
    }
}
