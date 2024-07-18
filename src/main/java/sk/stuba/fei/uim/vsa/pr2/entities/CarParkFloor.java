package sk.stuba.fei.uim.vsa.pr2.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CAR_PARK_FLOOR")
public class CarParkFloor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String identifier;
    ///obojsmerna
    @ManyToOne
    @JoinColumn(name="CAR_PARK_FK", nullable = false)
    private CarPark carPark;

    /*@OneToMany(cascade=CascadeType.REMOVE, orphanRemoval=true)
    @JoinColumn(name="CAR_PARK_FLOOR_FK")*/
    ///obojsmerna
    @OneToMany(mappedBy = "carParkFloor", cascade=CascadeType.REMOVE, orphanRemoval=true)
    private List<ParkingSpot> parkovacieMiesta;


    public CarParkFloor() {
    }

    public CarParkFloor(String identifier, List<ParkingSpot> parkovacieMiesta) {
        this.identifier = identifier;
        this.parkovacieMiesta = parkovacieMiesta;
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

    ///obojsmerna
    public CarPark getCarPark() {
        return carPark;
    }

    public void setCarPark(CarPark carPark) {
        this.carPark = carPark;
    }

    public List<ParkingSpot> getParkovacieMiesta() {
        return parkovacieMiesta;
    }

    public void setParkovacieMiesta(List<ParkingSpot> parkovacieMiesta) {
        this.parkovacieMiesta = parkovacieMiesta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarParkFloor that = (CarParkFloor) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CarParkFloor{" +
                "id=" + id +
                ", identifier='" + identifier + '\'' +
                ", carPark=" + carPark +
                '}';
    }
}
