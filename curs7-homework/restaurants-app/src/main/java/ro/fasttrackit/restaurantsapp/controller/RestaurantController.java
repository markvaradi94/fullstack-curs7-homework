package ro.fasttrackit.restaurantsapp.controller;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.restaurantsapp.domain.Restaurant;
import ro.fasttrackit.restaurantsapp.domain.RestaurantFilters;
import ro.fasttrackit.restaurantsapp.exception.ResourceNotFoundException;
import ro.fasttrackit.restaurantsapp.service.CollectionResponse;
import ro.fasttrackit.restaurantsapp.service.PageInfo;
import ro.fasttrackit.restaurantsapp.service.RestaurantService;

@RestController
@RequiredArgsConstructor
@RequestMapping("restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping
    CollectionResponse<Restaurant> getAll(RestaurantFilters filters, Pageable pageable) {
        Page<Restaurant> productPage = restaurantService.getAll(filters, pageable);
        return CollectionResponse.<Restaurant>builder()
                .content(productPage.getContent())
                .pageInfo(PageInfo.builder()
                        .totalPages(productPage.getTotalPages())
                        .totalElements(productPage.getNumberOfElements())
                        .crtPage(pageable.getPageNumber())
                        .pageSize(pageable.getPageSize())
                        .build())
                .build();
    }

    @GetMapping("{restaurantId}")
    Restaurant getRestaurant(@PathVariable Long restaurantId) {
        return restaurantService.findRestaurantById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find restaurant with id " + restaurantId));
    }

    @PostMapping
    Restaurant addRestaurant(@RequestBody Restaurant newRestaurant) {
        return restaurantService.addRestaurant(newRestaurant);
    }

    @DeleteMapping("{restaurantId}")
    Restaurant deleteRestaurant(@PathVariable Long restaurantId) {
        return restaurantService.deleteRestaurant(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find restaurant with id " + restaurantId));
    }

    @PutMapping("{restaurantId}")
    Restaurant replaceRestaurant(@RequestBody Restaurant newRestaurant, @PathVariable Long restaurantId) {
        return restaurantService.replaceRestaurant(restaurantId, newRestaurant);
    }

    @PatchMapping("{restaurantId}")
    Restaurant patchRestaurant(@RequestBody JsonPatch patch, @PathVariable Long restaurantId) {
        return restaurantService.patchRestaurant(restaurantId, patch);
    }
}
