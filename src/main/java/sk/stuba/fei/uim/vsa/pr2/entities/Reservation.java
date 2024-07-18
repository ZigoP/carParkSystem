package sk.stuba.fei.uim.vsa.pr2.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "RESERVATION")
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Car car;
    @ManyToOne
    private ParkingSpot parkingSpot;
    private Date zaciatok;
    private Date koniec;
    private Double cena;
    @ManyToMany
    private List<Holiday> sviatky;

    public Reservation() {
    }

    public Reservation(Car car, ParkingSpot parkingSpot, Date zaciatok, Date koniec, Double cena) {
        this.car = car;
        this.parkingSpot = parkingSpot;
        this.zaciatok = zaciatok;
        this.koniec = koniec;
        this.cena = cena;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public Date getZaciatok() {
        return zaciatok;
    }

    public void setZaciatok(Date zaciatok) {
        this.zaciatok = zaciatok;
    }

    public Date getKoniec() {
        return koniec;
    }

    public void setKoniec(Date koniec) {
        this.koniec = koniec;
    }

    public Double getCena() {
        return cena;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }

    public List<Holiday> getSviatky() {
        return sviatky;
    }

    public void setSviatky(List<Holiday> sviatky) {
        this.sviatky = sviatky;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", car=" + car +
                ", parkingSpot=" + parkingSpot +
                ", zaciatok=" + zaciatok +
                ", koniec=" + koniec +
                ", cena=" + cena +
                '}';
    }
}
