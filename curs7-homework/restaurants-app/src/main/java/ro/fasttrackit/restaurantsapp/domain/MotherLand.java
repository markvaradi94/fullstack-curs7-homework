package ro.fasttrackit.restaurantsapp.domain;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("hungary")
public class MotherLand implements Country {
    @Override
    public List<String> getMainCities() {
        return List.of(
                "Budapest",
                "Debrecen",
                "Gyor",
                "Sopron",
                "Nyiregyhaza"
        );
    }
}
