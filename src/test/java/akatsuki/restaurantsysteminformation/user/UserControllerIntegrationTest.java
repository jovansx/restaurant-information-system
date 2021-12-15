package akatsuki.restaurantsysteminformation.user;

import akatsuki.restaurantsysteminformation.dto.LoginDTO;
import akatsuki.restaurantsysteminformation.dto.TokenDTO;
import akatsuki.restaurantsysteminformation.user.dto.UserTableDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    private static final String URL_PREFIX = "/api/user";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    private HttpHeaders headers = null;

    @BeforeEach
    public void login() {
        if (headers == null) {
            ResponseEntity<TokenDTO> responseEntity =
                    restTemplate.postForEntity("/api/authenticate",
                            new LoginDTO("bradpitt", "bradpitt"),
                            TokenDTO.class);
            String accessToken = Objects.requireNonNull(responseEntity.getBody()).getToken();
            this.headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
        }
    }

    @Test
    public void getAllForRowInTable_FetchRows_ReturnedList() {
        ResponseEntity<UserTableDTO[]> res = restTemplate.exchange(URL_PREFIX + "/table", HttpMethod.GET, new HttpEntity<>(headers), UserTableDTO[].class);

        List<UserTableDTO> list = Arrays.asList(Objects.requireNonNull(res.getBody()));
        Assertions.assertEquals(9, list.size());
    }

}