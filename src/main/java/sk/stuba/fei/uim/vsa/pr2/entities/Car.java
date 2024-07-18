package sk.stuba.fei.uim.vsa.pr2.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CAR")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String znacka;
    private String model;
    @Column(unique = true, nullable = false)
    private String ECV;
    private String farba;
    @ManyToOne
    @JoinColumn(name="USER_FK", nullable = false)
    private User user;
    @OneToMany(mappedBy="car")
    private List<Reservation> reservations;

    public Car() {
    }

    public Car(String znacka, String model, String ECV, String farba, User user) {
        this.znacka = znacka;
        this.model = model;
        this.ECV = ECV;
        this.farba = farba;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZnacka() {
        return znacka;
    }

    public void setZnacka(String znacka) {
        this.znacka = znacka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getECV() {
        return ECV;
    }

    public void setECV(String ECV) {
        this.ECV = ECV;
    }

    public String getFarba() {
        return farba;
    }

    public void setFarba(String farba) {
        this.farba = farba;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        Car auto = (Car) o;
        return id.equals(auto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", znacka='" + znacka + '\'' +
                ", model='" + model + '\'' +
                ", ECV='" + ECV + '\'' +
                ", farba='" + farba + '\'' +
                ", user=" + user +
                '}';
    }
}
