package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BeerRestControllerIT extends BaseIT {

    @Test
    public void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/dc0891cc-0633-483c-82b1-8025a8f519e9")
                        .header("Api-Key", "user").header("Api-Secret", "123"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteBeerWithUrlParamFilter() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/dc0891cc-0633-483c-82b1-8025a8f519e9")
                        .param("Api-Key", "user")
                        .param("Api-Secret", "123"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteBeerWithUrlParamFilterBadCred() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/dc0891cc-0633-483c-82b1-8025a8f519e9")
                        .param("Api-Key", "user")
                        .param("Api-Secret", "321"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteBeerBadCred() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/dc0891cc-0633-483c-82b1-8025a8f519e9")
                        .header("Api-Key", "user").header("Api-Secret", "321"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteBeerHttpBasic() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/dc0891cc-0633-483c-82b1-8025a8f519e9")
                        .with(httpBasic("user", "123")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/dc0891cc-0633-483c-82b1-8025a8f519e9"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/dc0891cc-0633-483c-82b1-8025a8f519e9"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                .andExpect(status().isOk());
    }
}
