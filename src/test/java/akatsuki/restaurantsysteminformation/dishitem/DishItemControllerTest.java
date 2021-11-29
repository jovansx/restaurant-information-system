package akatsuki.restaurantsysteminformation.dishitem;

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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@SpringIntegrationTest
@WebAppConfiguration
public class DishItemControllerTest {
    private static final String URL_PREFIX = "/api/dish-item";

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
    public void testGetAllActiveDishItems() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[*].id").value(hasItem(2)))
                .andExpect(jsonPath("$.[*].initials").value(hasItem("E M")))
                .andExpect(jsonPath("$.[*].name").value(hasItem("Chicken breast")))
                .andExpect(jsonPath("$.[*].state").value(hasItem("PREPARATION")));
    }

    @Test
    public void testGetOneActive() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.notes").value("Grill it a little bit longer!"))
                .andExpect(jsonPath("$.state").value("PREPARATION"))
                .andExpect(jsonPath("$.chef").value("Elon Musk"))
                .andExpect(jsonPath("$.icon").value(""))
                .andExpect(jsonPath("$.itemList", hasSize(1)))
                .andExpect(jsonPath("$.itemList[0].itemName").value("Chicken breast"))
                .andExpect(jsonPath("$.itemList[0].amount").value(3));
    }

    @Test
    public void testGetOneActive_BadId() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(mediaType));
    }
}
