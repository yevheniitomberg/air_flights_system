package tomberg.fun.spring.air_flights.entity.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "airports")
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column
    private String city;

    @Column
    private String country;

    @Column(unique = true)
    private String airportCode;

    @ManyToMany
    @Nullable
    private Set<Airport> connected_airports;


    public Airport() {

    }

    public Airport(String city, String country, String airportCode) {
        this.city = city;
        this.country = country;
        this.airportCode = airportCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String name) {
        this.city = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airport_code) {
        this.airportCode = airport_code;
    }

    @JsonIgnore
    public Set<Airport> getConnected_airports() {
        return connected_airports;
    }

    public void setConnected_airports(Set<Airport> connected_airports) {
        this.connected_airports = connected_airports;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", airportCode='" + airportCode + '\'' +
                '}';
    }
    public String getFullName() {
        return country + ", " + city + " ("+airportCode+")";
    }
}