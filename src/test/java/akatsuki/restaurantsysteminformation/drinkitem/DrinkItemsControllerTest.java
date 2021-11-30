package akatsuki.restaurantsysteminformation.drinkitem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@SpringIntegrationTest
@WebAppConfiguration
public class DrinkItemsControllerTest {
    private static final String URL_PREFIX = "/api/drink-items";

    private final MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testGetAllActiveDrinkItems() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$", hasSize(4)));
//                .andExpect(jsonPath("$.[*].id").value(hasItem(5)))
//                .andExpect(jsonPath("$.[*].initials").value(hasItem("S B")))
//                .andExpect(jsonPath("$.[*].name").value(hasItem("Apple juice,Orange juice")))
//                .andExpect(jsonPath("$.[*].state").value(hasItem("PREPARATION")));
    }

    @Test
    public void testGetOneActive() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/active/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType));
//                .andExpect(jsonPath("$.id").value(5))
//                .andExpect(jsonPath("$.notes").value(nullValue()))
//                .andExpect(jsonPath("$.state").value("ON_HOLD"))
//                .andExpect(jsonPath("$.bartender").value(""))
//                .andExpect(jsonPath("$.itemList", hasSize(1)))
//                .andExpect(jsonPath("$.itemList[0].id").value(3))
//                .andExpect(jsonPath("$.itemList[0].itemName").value("Apple juice"))
//                .andExpect(jsonPath("$.itemList[0].amount").value(3));
    }

    @Test
    public void testGetOneActive_BadId() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/active/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(mediaType));
    }
}
