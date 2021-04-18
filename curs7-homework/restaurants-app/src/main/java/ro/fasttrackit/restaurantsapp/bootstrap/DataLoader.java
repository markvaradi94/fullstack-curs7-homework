package ro.fasttrackit.restaurantsapp.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.fasttrackit.restaurantsapp.domain.Restaurant;
import ro.fasttrackit.restaurantsapp.service.RestaurantService;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final RestaurantService restaurantService;

    @Override
    public void run(String... args) throws Exception {
        List<Restaurant> restaurants = List.of(
                Restaurant.builder()
                        .name("Ciresica")
                        .city("Oradea")
                        .since(LocalDate.now().minusYears(15))
                        .stars(4)
                .build(),
                Restaurant.builder()
                        .name("Corsarul")
                        .city("Oradea")
                        .since(LocalDate.now().minusYears(7))
                        .stars(5)
                .build(),
                Restaurant.builder()
                        .name("La Gyuri")
                        .city("Cluj")
                        .since(LocalDate.now().minusYears(4))
                        .stars(4)
                .build(),
                Restaurant.builder()
                        .name("Bullseye Bulzan")
                        .city("Oradea")
                        .since(LocalDate.now().minusYears(2))
                        .stars(5)
                .build(),
                Restaurant.builder()
                        .name("Ancora")
                        .city("Salonta")
                        .since(LocalDate.now().minusYears(3))
                        .stars(4)
                .build()
        );

        restaurants.forEach(restaurantService::addRestaurant);
    }
}
