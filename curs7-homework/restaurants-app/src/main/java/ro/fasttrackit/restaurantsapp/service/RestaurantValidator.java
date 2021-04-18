package ro.fasttrackit.restaurantsapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ro.fasttrackit.restaurantsapp.domain.Country;
import ro.fasttrackit.restaurantsapp.domain.Restaurant;
import ro.fasttrackit.restaurantsapp.exception.ValidationException;
import ro.fasttrackit.restaurantsapp.repository.RestaurantRepository;

import java.util.Arrays;
import java.util.Optional;

import static java.time.LocalDate.now;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Component
@RequiredArgsConstructor
public class RestaurantValidator {
    private final RestaurantRepository restaurantRepository;
    private final Country country;
    private final Environment environment;

    public void validateNewThrow(Restaurant newRestaurant) {
        validate(newRestaurant, true)
                .ifPresent(validationException -> {
                    throw validationException;
                });
    }

    public void validateReplaceThrow(Long restaurantId, Restaurant newRestaurant) {
        exists(restaurantId)
                .or(() -> validate(newRestaurant, false))
                .ifPresent(validationException -> {
                    throw validationException;
                });
    }

    private Optional<ValidationException> validate(Restaurant restaurant, boolean newRestaurant) {
        if (restaurant.getName() == null) {
            return of(new ValidationException("Restaurant name cannot be null"));
        } else if (newRestaurant && restaurantRepository.existsByName(restaurant.getName())) {
            return of(new ValidationException("Name cannot be duplicate"));
        } else if (!newRestaurant && restaurantRepository.existsByNameAndIdNot(restaurant.getName(), restaurant.getId())) {
            return of(new ValidationException("Name cannot be duplicate"));
        } else if (!country.getMainCities().contains(restaurant.getCity())) {
            return of(new ValidationException("Restaurant must be from one of these cities: "
                    + Arrays.toString(environment.getActiveProfiles())));
        } else if (now().isBefore(restaurant.getSince())) {
            return of(new ValidationException("Since date cannot be before now for new restaurant"));
        } else {
            return empty();
        }
    }

    public void validateExistsOrThrow(Long restaurantId) {
        exists(restaurantId).ifPresent(validationException -> {
            throw validationException;
        });
    }

    private Optional<ValidationException> exists(Long restaurantId) {
        return restaurantRepository.existsById(restaurantId) ?
                empty() : of(new ValidationException("Restaurant with id " + restaurantId + " doesn't exist"));
    }
}
