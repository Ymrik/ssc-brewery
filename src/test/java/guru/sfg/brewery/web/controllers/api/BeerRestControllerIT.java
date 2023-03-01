package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BeerRestControllerIT extends BaseIT {
    @Autowired
    private BeerRepository beerRepository;

    public Beer beerToDelete() {
        Random random = new Random();

        return beerRepository.saveAndFlush(Beer.builder()
                .beerName("Delete me beer")
                .beerStyle(BeerStyleEnum.IPA)
                .minOnHand(12)
                .quantityToBrew(200)
                .upc(String.valueOf(random.nextInt(99999999)))
                .build());
    }

    @Test
    public void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                        .header("Api-Key", "user").header("Api-Secret", "123"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteBeerWithUrlParamFilter() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                        .param("Api-Key", "user")
                        .param("Api-Secret", "123"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteBeerWithUrlParamFilterBadCred() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                        .param("Api-Key", "user")
                        .param("Api-Secret", "321"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteBeerBadCred() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" +  beerToDelete().getId())
                        .header("Api-Key", "user").header("Api-Secret", "321"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteBeerHttpBasicWithUserRole() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                        .with(httpBasic("user", "123")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteBeerHttpBasicWithCustomerRole() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                        .with(httpBasic("scott", "tiger")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteBeerHttpBasic() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/" + beerToDelete().getId()))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/" + beerToDelete().getUpc()))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerFormAdmin() throws Exception {
        mockMvc.perform(get("/beers").param("beerName", "")
                .with(httpBasic("spring", "guru")))
                .andExpect(status().isOk());
    }
}
