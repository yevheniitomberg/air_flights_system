package tomberg.fun.spring.air_flights.entity;

import tomberg.fun.spring.air_flights.entity.location.Airport;

import javax.persistence.*;

@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "airport_from_id")
    private Airport airportFrom;

    @ManyToOne
    @JoinColumn(name = "airport_to_id")
    private Airport airportTo;

    public Airport getAirportTo() {
        return airportTo;
    }

    public Airport getAirportFrom() {
        return airportFrom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAirportFrom(Airport airportFrom) {
        this.airportFrom = airportFrom;
    }

    public void setAirportTo(Airport airportTo) {
        this.airportTo = airportTo;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", airportFrom=" + airportFrom +
                ", airportTo=" + airportTo +
                '}';
    }
}