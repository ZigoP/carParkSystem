package sk.stuba.fei.uim.vsa.pr2.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PARKING_SPOT")
public class ParkingSpot implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String identifier;

    //obojsmerna
    @ManyToOne
    @JoinColumn(name="CAR_PARK_FLOOR_FK", nullable = false)
    private CarParkFloor carParkFloor;

    private boolean free;

    @OneToMany(mappedBy="parkingSpot")
    private List<Reservation> reservations;

    public ParkingSpot() {
        this.free = true;
    }

    public ParkingSpot(String identifier) {
        this.identifier = identifier;
        this.free = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    //obojsmerna
    public CarParkFloor getCarParkFloor() {
        return carParkFloor;
    }

    public void setCarParkFloor(CarParkFloor carParkFloor) {
        this.carParkFloor = carParkFloor;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ParkingSpot{" +
                "id=" + id +
                ", identifier='" + identifier + '\'' +
                ", carParkFloor=" + carParkFloor +
                '}';
    }
}
