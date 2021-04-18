package ro.fasttrackit.restaurantsapp.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private Integer stars;

    private String city;

    private LocalDate since;

    public Restaurant(String name, Integer stars, String city, LocalDate since) {
        this.name = name;
        this.stars = stars;
        this.city = city;
        this.since = since;
    }
}
