package sk.stuba.fei.uim.vsa.pr2.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CAR_PARK")
public class CarPark implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String nazov;
    private String adresa;
    @Column(nullable = false)
    private Integer cenaZaHodinu;
    /*@OneToMany(cascade=CascadeType.REMOVE, orphanRemoval=true)
    @JoinColumn(name="CAR_PARK_FK")*/

    ///obojsmerna
    @OneToMany(mappedBy = "carPark", cascade=CascadeType.REMOVE, orphanRemoval=true)
    private List<CarParkFloor> parkovaciePoschodie;


    public CarPark() {
    }

    public CarPark(String nazov, String adresa, Integer cenaZaHodinu) {
        this.nazov = nazov;
        this.adresa = adresa;
        this.cenaZaHodinu = cenaZaHodinu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public Integer getCenaZaHodinu() {
        return cenaZaHodinu;
    }

    public void setCenaZaHodinu(Integer cenaZaHodinu) {
        this.cenaZaHodinu = cenaZaHodinu;
    }

    public List<CarParkFloor> getParkovaciePoschodie() {
        return parkovaciePoschodie;
    }

    public void setParkovaciePoschodie(List<CarParkFloor> parkovaciePoschodie) {
        this.parkovaciePoschodie = parkovaciePoschodie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarPark carPark = (CarPark) o;
        return id.equals(carPark.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CarPark{" +
                "id=" + id +
                ", nazov='" + nazov + '\'' +
                ", adresa='" + adresa + '\'' +
                ", cenaZaHodinu=" + cenaZaHodinu +
                '}';
    }
}
