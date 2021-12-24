package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.dto.LoginDTO;
import akatsuki.restaurantsysteminformation.dto.TokenDTO;
import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.itemcategory.dto.ItemCategoryDTO;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserChangePasswordDTO;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDetailsDTO;
import akatsuki.restaurantsysteminformation.user.dto.UserTableDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisteredUserControllerIntegrationTest {

    private static final String URL_PREFIX = "/api/registered-user";

    @Autowired
    RegisteredUserService registeredUserService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;

    @BeforeEach
    public void login() {
        if (headers == null) {
            ResponseEntity<TokenDTO> responseEntity =
                    restTemplate.postForEntity("/api/authenticate",
                            new LoginDTO("michaeldouglas", "michaeldouglas"),
                            TokenDTO.class);
            String accessToken = Objects.requireNonNull(responseEntity.getBody()).getToken();
            this.headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
        }
    }

    @Test
    public void getOne_ValidId_ReturnedObject() {
        ResponseEntity<RegisteredUserDetailsDTO> responseEntity = restTemplate.exchange(URL_PREFIX + "/10", HttpMethod.GET, new HttpEntity<>(headers), RegisteredUserDetailsDTO.class);
        RegisteredUserDetailsDTO dto = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(dto);
        Assertions.assertEquals("michaeldouglas@gmail.com", dto.getEmailAddress());
    }

    @Test
    public void getOne_InvalidId_ExceptionThrown() {
        ResponseEntity<RegisteredUserDetailsDTO> responseEntity = restTemplate.exchange(URL_PREFIX + "/8000", HttpMethod.GET, new HttpEntity<>(headers), RegisteredUserDetailsDTO.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getAllSystemAdminsForRowInTable_Valid_ObjectsReturned() {
        ResponseEntity<UserTableDTO[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/system-admin/table", HttpMethod.GET, new HttpEntity<>(headers), UserTableDTO[].class);
        List<UserTableDTO> list = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void create_ValidEntity_SavedObject() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 13, UserType.MANAGER, "michael", "lock");
        int size = registeredUserService.getAll().size();
        HttpEntity<RegisteredUserDTO> entity = new HttpEntity<>(user, headers);
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX, HttpMethod.POST, entity, String.class);

        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());

        List<RegisteredUser> users = registeredUserService.getAll();
        Assertions.assertEquals(size + 1, users.size());
        Assertions.assertEquals("michaellock@gmail.com", users.get(users.size() - 1).getEmailAddress());

        registeredUserService.deleteById(Long.parseLong(res.getBody()));
        Assertions.assertEquals(size, registeredUserService.getAll().size());
    }

    @Test
    void create_AlreadyExistUsername_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 12, UserType.MANAGER, "bradpitt", "lock");
        HttpEntity<RegisteredUserDTO> entity = new HttpEntity<>(user, headers);
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX, HttpMethod.POST, entity, String.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    void create_InvalidPhoneNumber_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "064567882", 12, UserType.MANAGER, "bradpitt", "lock");
        HttpEntity<RegisteredUserDTO> entity = new HttpEntity<>(user, headers);
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX, HttpMethod.POST, entity, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void create_InvalidUserType_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 12, UserType.ADMIN, "michael123", "lock");
        HttpEntity<RegisteredUserDTO> entity = new HttpEntity<>(user, headers);
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX, HttpMethod.POST, entity, String.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void update_ValidEntityAndId_SavedObject() {
        RegisteredUser original = registeredUserService.getOne(11L);
        RegisteredUserDTO user = new RegisteredUserDTO("Liamy", "Neeson", "liamneeson@gmail.com",
                "0611111122", 1000, UserType.SYSTEM_ADMIN, "michael123", "lock");

        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/11", HttpMethod.PUT,
                        new HttpEntity<>(user, headers),
                        Void.class);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        RegisteredUser foundUser = registeredUserService.getOne(11L);
        Assertions.assertEquals("Liamy", foundUser.getFirstName());

        foundUser = original;
        registeredUserService.save(foundUser);
    }

    @Test
    public void update_ChangedUserType_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 11, UserType.MANAGER, "michael123", "lock");
        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/10", HttpMethod.PUT,
                        new HttpEntity<>(user, headers),
                        Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void update_UsernameAlreadyExist_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 12, UserType.SYSTEM_ADMIN, "bradpitt", "lock");

        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/11", HttpMethod.PUT,
                        new HttpEntity<>(user, headers),
                        Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void update_EmailAlreadyExist_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "calikikoki@gmail.com",
                "0645678822", 12, UserType.SYSTEM_ADMIN, "michael123", "lock");
        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/11", HttpMethod.PUT,
                        new HttpEntity<>(user, headers),
                        Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void update_PhoneNumberAlreadyExist_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0611111114", 12, UserType.SYSTEM_ADMIN, "michael123", "lock");
        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/11", HttpMethod.PUT,
                        new HttpEntity<>(user, headers),
                        Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void changePassword_ValidEntityAndId_SavedObject() {
        RegisteredUser original = registeredUserService.getOne(11L);
        RegisteredUserChangePasswordDTO dto = new RegisteredUserChangePasswordDTO("liamneeson", "liamneesonstronger");

        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/change-password/11", HttpMethod.PUT,
                        new HttpEntity<>(dto, headers),
                        Void.class);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        RegisteredUser foundUser = registeredUserService.getOne(11L);
        Assertions.assertTrue(encoder.matches("liamneesonstronger", foundUser.getPassword()));

        foundUser = original;
        registeredUserService.save(foundUser);
    }

    @Test
    public void changePassword_PasswordsNotMatches_ExceptionThrown() {
        RegisteredUserChangePasswordDTO dto = new RegisteredUserChangePasswordDTO("liamneeson2", "liamneesonstronger");

        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/change-password/11", HttpMethod.PUT,
                        new HttpEntity<>(dto, headers),
                        Void.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void delete_ValidId_SavedObject() {
        RegisteredUser user = registeredUserService.create(new RegisteredUserDTO("Marko", "Savic", "markos@gmail.com", "0611141111", 13, UserType.SYSTEM_ADMIN, "username", "password"));
        int size = registeredUserService.getAll().size();

        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/" + user.getId(),
                HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(size - 1, registeredUserService.getAll().size());
    }

    @Test
    public void delete_InvalidType_ExceptionThrown() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/10",
                HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }
}