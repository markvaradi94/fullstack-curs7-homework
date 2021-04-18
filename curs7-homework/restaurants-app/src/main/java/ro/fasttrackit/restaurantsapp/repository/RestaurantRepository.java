package ro.fasttrackit.restaurantsapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.fasttrackit.restaurantsapp.domain.Restaurant;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Page<Restaurant> findByCityIgnoreCase(String city, Pageable pageable);

    Page<Restaurant> findByStarsInAndCityIgnoreCase(List<String> stars, String city, Pageable pageable);

    Page<Restaurant> findByStarsIn(List<String> stars, Pageable pageable);
}
