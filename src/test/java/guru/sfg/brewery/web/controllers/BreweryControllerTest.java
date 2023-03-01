package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BreweryControllerTest extends BaseIT{
    @Test
    public void getBreweriesWithCustomerRoleTest() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                .with(httpBasic("scott", "tiger")))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/brewery/breweries")
                        .with(httpBasic("scott", "tiger")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getBreweriesWithNoCustomerRoleTest() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                        .with(httpBasic("user", "123")))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/brewery/breweries")
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getBreweriesWithoutAuth() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/brewery/breweries"))
                .andExpect(status().isUnauthorized());
    }
}
