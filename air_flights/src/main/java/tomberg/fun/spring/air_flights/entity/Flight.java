package tomberg.fun.spring.air_flights.entity;

import tomberg.fun.spring.air_flights.entity.location.City;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Date departure_date;

    @Column
    private Date arrive_date;

    @ManyToOne
    @JoinColumn(name = "plane_id")
    private Plane plane;

    @ManyToOne
    @JoinColumn(name = "city_from_id")
    private City city_from;

    @ManyToOne
    @JoinColumn(name = "city_to_id")
    private City city_to;

    public City getCity_to() {
        return city_to;
    }

    public City getCity_from() {
        return city_from;
    }


    public Plane getPlane() {
        return plane;
    }

    public User getUser() {
        return user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getArrive_date() {
        return arrive_date;
    }

    public void setArrive_date(Date arrive_date) {
        this.arrive_date = arrive_date;
    }


    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public void setCity_from(City city_from) {
        this.city_from = city_from;
    }

    public void setCity_to(City city_to) {
        this.city_to = city_to;
    }

    public Date getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(Date departure_date) {
        this.departure_date = departure_date;
    }
}