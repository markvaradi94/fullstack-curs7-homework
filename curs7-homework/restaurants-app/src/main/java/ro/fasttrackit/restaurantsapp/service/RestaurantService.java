package ro.fasttrackit.restaurantsapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.fasttrackit.restaurantsapp.domain.Restaurant;
import ro.fasttrackit.restaurantsapp.domain.RestaurantFilters;
import ro.fasttrackit.restaurantsapp.exception.ResourceNotFoundException;
import ro.fasttrackit.restaurantsapp.repository.RestaurantRepository;

import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository repository;
    private final RestaurantValidator validator;
    private final ObjectMapper mapper;

    public Page<Restaurant> getAll(RestaurantFilters filters, Pageable pageable) {
        if (!isEmpty(filters.getStars()) && filters.getCity() != null) {
            return repository.findByStarsInAndCityIgnoreCase(filters.getStars(), filters.getCity(), pageable);
        } else if (!isEmpty(filters.getStars())) {
            return repository.findByStarsIn(filters.getStars(), pageable);
        } else if (filters.getCity() != null) {
            return repository.findByCityIgnoreCase(filters.getCity(), pageable);
        } else {
            return repository.findAll(pageable);
        }
    }

    public Optional<Restaurant> findRestaurantById(Long restaurantId) {
        return repository.findById(restaurantId);
    }

    public Restaurant addRestaurant(Restaurant newRestaurant) {
        validator.validateNewThrow(newRestaurant);
        return repository.save(newRestaurant);
    }

    public Optional<Restaurant> deleteRestaurant(Long restaurantId) {
        Optional<Restaurant> restaurantOptional = repository.findById(restaurantId);
        restaurantOptional.ifPresent(repository::delete);
        return restaurantOptional;
    }

    public Restaurant replaceRestaurant(Long restaurantId, Restaurant newRestaurant) {
        newRestaurant.setId(restaurantId);
        validator.validateReplaceThrow(restaurantId, newRestaurant);
        Restaurant restaurantToUpdate = getOrThrow(restaurantId);

        copyRestaurant(newRestaurant, restaurantToUpdate);
        return repository.save(restaurantToUpdate);
    }

    @SneakyThrows
    public Restaurant patchRestaurant(Long restaurantId, JsonPatch patch) {
        validator.validateExistsOrThrow(restaurantId);
        Restaurant restaurantToPatch = getOrThrow(restaurantId);

        JsonNode patchedRestaurantJson = patch.apply(mapper.valueToTree(restaurantToPatch));
        Restaurant patchedRestaurant = mapper.treeToValue(patchedRestaurantJson, Restaurant.class);
        return replaceRestaurant(restaurantId, patchedRestaurant);
    }

    private Restaurant getOrThrow(Long restaurantId) {
        return repository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find restaurant with id "));
    }

    private void copyRestaurant(Restaurant newRestaurant, Restaurant dbRestaurant) {
        dbRestaurant.setName(newRestaurant.getName());
        dbRestaurant.setCity(newRestaurant.getCity());
        dbRestaurant.setStars(newRestaurant.getStars());
        dbRestaurant.setSince(newRestaurant.getSince());
    }
}
