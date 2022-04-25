package tomberg.fun.spring.air_flights.entity;

import javax.persistence.*;
import java.util.Comparator;

@Entity
@Table(name = "places")
public class Place implements Comparable<Place>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column
    private boolean booked;

    @Column
    private int number;

    public Place(boolean booked, int number) {
        this.booked = booked;
        this.number = number;
    }

    public Place() {

    }

    @Override
    public int compareTo(Place o) {
        return number-o.number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Place{" +
                "booked=" + booked +
                ", number=" + number +
                '}';
    }
}