package tomberg.fun.spring.air_flights.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "baggage")
public class Baggage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Nullable
    private int weight;

    @Nullable
    private int additionalFee;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(int additionalFee) {
        this.additionalFee = additionalFee;
    }

    public String baggageString() {
        return weight + " kg (additional fee " + additionalFee +" $)";
    }
}