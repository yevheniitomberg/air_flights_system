package tomberg.fun.spring.air_flights.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column
    private LocalDate depDate;

    @Column
    private LocalDate arrDate;

    @Column
    private Time depTime;

    @Column
    private Time arrTime;

    @Column
    private int price;

    @ManyToOne
    @JoinColumn(name = "plane_id")
    private Plane plane;

    @ManyToMany
    @Nullable
    private Set<Place> places;

    @ManyToOne
    @JoinColumn(name = "regulator_id")
    private Regulator regulator;

    public Regulator getRegulator() {
        return regulator;
    }

    public Plane getPlane() {
        return plane;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Time getDepTime() {
        return depTime;
    }

    public void setDepTime(Time depTime) {
        this.depTime = depTime;
    }

    public Time getArrTime() {
        return arrTime;
    }

    public void setArrTime(Time arrTime) {
        this.arrTime = arrTime;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public Set<Place> getPlaces() {
        return places;
    }

    public void setPlaces(Set<Place> places) {
        this.places = places;
    }

    public LocalDate getDepDate() {
        return depDate;
    }

    public void setDepDate(LocalDate depDate) {
        this.depDate = depDate;
    }

    public LocalDate getArrDate() {
        return arrDate;
    }

    public void setArrDate(LocalDate arrDate) {
        this.arrDate = arrDate;
    }

    public void setRegulator(Regulator regulator) {
        this.regulator = regulator;
    }
}