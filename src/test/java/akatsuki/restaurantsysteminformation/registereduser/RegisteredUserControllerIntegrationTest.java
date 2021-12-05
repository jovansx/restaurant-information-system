package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import akatsuki.restaurantsysteminformation.registereduser.exception.RegisteredUserDeleteException;
import akatsuki.restaurantsysteminformation.role.Role;
import akatsuki.restaurantsysteminformation.salary.Salary;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisteredUserControllerIntegrationTest {

    private static final String URL_PREFIX = "/api/registered-user";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    RegisteredUserService registeredUserService;

    @Test
    public void getOne_ValidId_ReturnedObject() {
        ResponseEntity<RegisteredUser> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/10", RegisteredUser.class);
        RegisteredUser dto = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(dto);
        Assertions.assertEquals("michaeldouglas@gmail.com", dto.getEmailAddress());
    }

    @Test
    public void getOne_InvalidId_ExceptionThrown() {
        ResponseEntity<RegisteredUser> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/8000", RegisteredUser.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getAll_Valid_ObjectsReturned() {
        ResponseEntity<RegisteredUser[]> responseEntity = restTemplate.getForEntity(URL_PREFIX, RegisteredUser[].class);
        List<RegisteredUser> list = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        Assertions.assertEquals(3, list.size());
    }

    @Test
    void create_ValidEntity_SavedObject() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 13, UserType.MANAGER, "michael", "lock");
        int size = registeredUserService.getAll().size();
        ResponseEntity<String> res = restTemplate.postForEntity(URL_PREFIX, user, String.class);

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
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL_PREFIX, user, String.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void create_InvalidPhoneNumber_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "064567882", 12, UserType.MANAGER, "bradpitt", "lock");
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL_PREFIX, user, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void create_InvalidUserType_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 12, UserType.ADMIN, "michael123", "lock");
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL_PREFIX, user, String.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void update_ValidEntityAndId_SavedObject() {
        RegisteredUser original = registeredUserService.getOne(11L);
        RegisteredUserDTO user = new RegisteredUserDTO("Liamy", "Neeson", "liamneeson@gmail.com",
                "0611111122", 1000, UserType.SYSTEM_ADMIN, "michael123", "lock");

        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/11", HttpMethod.PUT,
                        new HttpEntity<>(user),
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
                        new HttpEntity<>(user),
                        Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void update_UsernameAlreadyExist_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 12, UserType.SYSTEM_ADMIN, "bradpitt", "lock");

        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/11", HttpMethod.PUT,
                        new HttpEntity<>(user),
                        Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void update_EmailAlreadyExist_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "calikikoki@gmail.com",
                "0645678822", 12, UserType.SYSTEM_ADMIN, "michael123", "lock");
        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/11", HttpMethod.PUT,
                        new HttpEntity<>(user),
                        Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void update_PhoneNumberAlreadyExist_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0611111114", 12, UserType.SYSTEM_ADMIN, "michael123", "lock");
        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/11", HttpMethod.PUT,
                        new HttpEntity<>(user),
                        Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void delete_ValidId_SavedObject() {
        RegisteredUser user = registeredUserService.create(new RegisteredUserDTO("Marko", "Savic", "markos@gmail.com", "0611141111", 13, UserType.SYSTEM_ADMIN, "username", "password"));
        int size = registeredUserService.getAll().size();

        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/" + user.getId(),
                HttpMethod.DELETE, new HttpEntity<>(null), Void.class);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(size - 1, registeredUserService.getAll().size());
    }

    @Test
    public void delete_InvalidType_ExceptionThrown() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/10",
                HttpMethod.DELETE, new HttpEntity<>(null), Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }
}