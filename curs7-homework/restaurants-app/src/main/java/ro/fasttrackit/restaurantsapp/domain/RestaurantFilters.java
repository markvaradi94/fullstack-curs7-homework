package ro.fasttrackit.restaurantsapp.domain;

import lombok.Value;

import java.util.List;

@Value
public class RestaurantFilters {
    List<String> stars;
    String city;
}
