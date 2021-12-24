package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.dto.LoginDTO;
import akatsuki.restaurantsysteminformation.dto.TokenDTO;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserEssentialsDTO;
import akatsuki.restaurantsysteminformation.user.dto.UserTableDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UnregisteredUserControllerIntegrationTest {

    private static final String URL_PREFIX = "/api/unregistered-user";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UnregisteredUserService unregisteredUserService;

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
    public void getOne_ValidId_ObjectReturned() {
        ResponseEntity<UnregisteredUserDTO> res = restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.GET, new HttpEntity<>(headers), UnregisteredUserDTO.class);

        UnregisteredUserDTO dto = res.getBody();

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertNotNull(dto);
        Assertions.assertEquals("johncena@gmail.com", dto.getEmailAddress());
    }

    @Test
    public void getOne_InvalidId_ExceptionThrown() {
        ResponseEntity<UnregisteredUserDTO> responseEntity = restTemplate.exchange(URL_PREFIX + "/8000", HttpMethod.GET, new HttpEntity<>(headers), UnregisteredUserDTO.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getAllForRowInTable_Valid_ObjectsReturned() {
        ResponseEntity<UserTableDTO[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/table", HttpMethod.GET, new HttpEntity<>(headers), UserTableDTO[].class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<UserTableDTO> list = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        Assertions.assertEquals(8, list.size());
    }

    @Test
    void create_Valid_SavedObject() {
        int size = unregisteredUserService.getAll().size();
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX, HttpMethod.POST,
                new HttpEntity<>(new UnregisteredUserDTO("Jelena", "Stojanovic", "sekica@gmail.com",
                        "0691212237", 1000, UserType.WAITER, "7878"), headers), String.class);

        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());

        List<UnregisteredUser> users = unregisteredUserService.getAll();
        Assertions.assertEquals(size + 1, users.size());
        Assertions.assertEquals("sekica@gmail.com", users.get(users.size() - 1).getEmailAddress());

        unregisteredUserService.deleteById(Long.parseLong(res.getBody()));
    }

    @Test
    void create_InvalidPin_ExceptionThrown() {
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL_PREFIX, HttpMethod.POST, new HttpEntity<>(new UnregisteredUserDTO("Jelena", "Stojanovic",
                "sekica@gmail.com", "0691212237", 1000, UserType.WAITER, "1111"), this.headers), String.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void create_InvalidUserType_ExceptionThrown() {
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL_PREFIX, HttpMethod.POST, new HttpEntity<>(new UnregisteredUserDTO("Jelena", "Stojanovic",
                "sekica@gmail.com", "0691212237", 1000, UserType.MANAGER, "1111"), this.headers), String.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void update_Valid_SavedObject() {
        ResponseEntity<Void> res = restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.PUT, new HttpEntity<>(new UnregisteredUserDTO("Jelena",
                "Cena", "johncena@gmail.com", "0611111111", 1000, UserType.WAITER, "1111"), headers), Void.class);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        UnregisteredUser user = unregisteredUserService.getOne(1L);
        Assertions.assertEquals("Jelena", user.getFirstName());

        user.setFirstName("John");
        unregisteredUserService.save(user);
    }

    @Test
    void update_InvalidPin_ExceptionThrown() {
        ResponseEntity<Void> res = restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.PUT, new HttpEntity<>(new UnregisteredUserDTO("John", "Cena",
                "johncena@gmail.com", "0611111111", 1000, UserType.WAITER, "1112"), this.headers), Void.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    void update_InvalidEmail_ExceptionThrown() {
        ResponseEntity<Void> res = restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.PUT, new HttpEntity<>(new UnregisteredUserDTO("John", "Cena",
                "simonbaker@gmail.com", "0611111111", 1000, UserType.WAITER, "1111"), this.headers), Void.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    void update_InvalidPhoneNumber_ExceptionThrown() {
        ResponseEntity<Void> res = restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.PUT, new HttpEntity<>(new UnregisteredUserDTO("John", "Cena",
                "johncena@gmail.com", "0611111112", 1000, UserType.WAITER, "1111"), this.headers), Void.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    void update_InvalidUserType_ExceptionThrown() {
        ResponseEntity<Void> res = restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.PUT,
                new HttpEntity<>(new UnregisteredUserDTO("John", "Cena", "johncena@gmail.com",
                        "0611111111", 1000, UserType.MANAGER, "1111"), this.headers), Void.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }


    @Test
    void delete_Valid_SavedObject() {
        UnregisteredUser user = unregisteredUserService.create(new UnregisteredUserDTO("Marko", "Savic", "markos@gmail.com", "0611141111", 1000, UserType.WAITER, "1167"));
        int size = unregisteredUserService.getAll().size();

        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/" + user.getId(),
                HttpMethod.DELETE, new HttpEntity<>(this.headers), Void.class);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(size - 1, unregisteredUserService.getAll().size());
    }

    @Test
    void delete_UserActive_ExceptionThrown() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/1",
                HttpMethod.DELETE, new HttpEntity<>(this.headers), Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void checkPinCode_Valid_ReturnedObject() {
        UnregisteredUser foundUser = unregisteredUserService.checkPinCode("1111", UserType.WAITER);
        Assertions.assertNotNull(foundUser);

        ResponseEntity<UnregisteredUserEssentialsDTO> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/pin-code/1111?usertype=waiter", UnregisteredUserEssentialsDTO.class);
        UnregisteredUserEssentialsDTO dto = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(dto);
        Assertions.assertEquals("1111", dto.getPinCode());
    }

    @Test
    void checkPinCode_InvalidPinCode_ExceptionThrown() {
        ResponseEntity<UnregisteredUserDTO> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/pin-code/8761?usertype=waiter", UnregisteredUserDTO.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void checkPinCode_InvalidUserPinCode_ExceptionThrown() {
        ResponseEntity<UnregisteredUserDTO> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/pin-code/1112?usertype=waiter", UnregisteredUserDTO.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}