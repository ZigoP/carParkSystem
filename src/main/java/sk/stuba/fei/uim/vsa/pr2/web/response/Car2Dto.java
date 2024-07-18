package sk.stuba.fei.uim.vsa.pr2.web.response;

import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;

import java.util.List;
import java.util.Objects;

public class Car2Dto extends Dto{

    private String brand;
    private String model;
    private String vrp;
    private String colour;
    private User2Dto owner;
    private List<Reservation2Dto> reservations;

    public Car2Dto() {
    }

    public Car2Dto(Long id, String brand, String model, String vrp, String colour, User2Dto owner, List<Reservation2Dto> reservations) {
        super(id);
        this.brand = brand;
        this.model = model;
        this.vrp = vrp;
        this.colour = colour;
        this.owner = owner;
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

    public User2Dto getOwner() {
        return owner;
    }

    public void setOwner(User2Dto owner) {
        this.owner = owner;
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
        Car2Dto car2Dto = (Car2Dto) o;
        return vrp.equals(car2Dto.vrp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vrp);
    }
}
