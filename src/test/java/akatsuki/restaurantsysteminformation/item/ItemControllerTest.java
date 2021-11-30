package akatsuki.restaurantsysteminformation.item;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@SpringIntegrationTest
@WebAppConfiguration
public class ItemControllerTest {
    private static final String URL_PREFIX = "/api/item";

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
    public void testGetOneActive() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.itemCategory.name").value("Juices"))
                .andExpect(jsonPath("$.prices", hasSize(1)))
                .andExpect(jsonPath("$.prices[0].value").value(7));
    }

    @Test
    public void testGetAllActiveItems() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$.[*].itemCategory.name").value(hasItem("Sandwich")));
    }

    @Test
    public void getAllByCategoryItems() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/category/Juices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType));
//                .andExpect(jsonPath("$", hasSize(5)))
//                .andExpect(jsonPath("$.[*].itemCategory.name").value(hasItem("Sandwich")));
    }
}
