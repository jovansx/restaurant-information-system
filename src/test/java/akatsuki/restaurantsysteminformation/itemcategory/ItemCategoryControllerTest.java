package akatsuki.restaurantsysteminformation.itemcategory;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@SpringIntegrationTest
@WebAppConfiguration
class ItemCategoryControllerTest {

    private static final String URL_PREFIX = "/api/item-category";

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
    public void testGetOne_ValidId_ObjectIsReturned() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(jsonPath("$.name").value("Cocktails"));
    }

    @Test
    public void testGetOne_IdDoesNotExist_ErrorNotFoundReturned() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/44"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(mediaType));
    }

    @Test
    public void testGetOne_InvalidId_ErrorBadRequestReturned() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(mediaType));
    }

    @Test
    public void testGetAll__ObjectsReturned() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType));
//                .andExpect(jsonPath("$", hasSize(4)))
//                .andExpect(jsonPath("$.[*].name").value(hasItem("Cocktails")));
    }

    @Test
    public void create_ValidDto_ObjectIsCreated() throws Exception {
//        ItemCategoryDTO dto = new ItemCategoryDTO("Seafood");
//
//        mockMvc.perform(get(URL_PREFIX + "/"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(mediaType))
//                .andExpect(jsonPath("$", hasSize(4)));
//
//        mockMvc.perform(post(URL_PREFIX + "/")
//                        .contentType(mediaType)
//                        .content(asJsonString(dto)))
//                .andExpect(status().isCreated());
//
//        mockMvc.perform(get(URL_PREFIX + "/"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(mediaType))
//                .andExpect(jsonPath("$", hasSize(5)))
//                .andExpect(jsonPath("$.[*].name").value(hasItem("Seafood")));
    }

    @Test
    public void delete_ValidId_ObjectDeleted() throws Exception {
//        ItemCategoryDTO dto = new ItemCategoryDTO("Seafood");
//
//        mockMvc.perform(post(URL_PREFIX + "/")
//                        .contentType(mediaType)
//                        .content(asJsonString(dto)))
//                .andExpect(status().isCreated());
//
//        mockMvc.perform(get(URL_PREFIX + "/"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(mediaType))
//                .andExpect(jsonPath("$", hasSize(5)));
//
//        mockMvc.perform(delete(URL_PREFIX + "/{id}", "5"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get(URL_PREFIX + "/"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(mediaType))
//                .andExpect(jsonPath("$", hasSize(4)));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}