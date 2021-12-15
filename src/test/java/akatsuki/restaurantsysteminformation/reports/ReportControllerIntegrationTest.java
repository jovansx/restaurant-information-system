package akatsuki.restaurantsysteminformation.reports;

import akatsuki.restaurantsysteminformation.dto.LoginDTO;
import akatsuki.restaurantsysteminformation.dto.TokenDTO;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.RegisteredUser;
import akatsuki.restaurantsysteminformation.registereduser.RegisteredUserService;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import akatsuki.restaurantsysteminformation.reports.dto.ReportDTO;
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
class ReportControllerIntegrationTest {

    private static final String URL_PREFIX = "/api/report";
    @Autowired
    ReportService reportService;
    @Autowired
    private TestRestTemplate restTemplate;

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
    public void getMonthlyReport_Valid_ReturnedValidReportObject() {
        ResponseEntity<ReportDTO> res = restTemplate.exchange(URL_PREFIX + "/monthly?year=2021",HttpMethod.GET, new HttpEntity<>(headers), ReportDTO.class);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(5400, res.getBody().getReportItems().get(0).getTotalOutcome());
        Assertions.assertEquals(1108, res.getBody().getReportItems().get(0).getTotalIncome());
    }


    @Test
    public void getQuarterlyReport_Valid_ReturnedValidReportObject() {
        ResponseEntity<ReportDTO> res = restTemplate.exchange(URL_PREFIX + "/quarterly?year=2021",HttpMethod.GET, new HttpEntity<>(headers), ReportDTO.class);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(30300, res.getBody().getReportItems().get(0).getTotalOutcome());
        Assertions.assertEquals(5708, res.getBody().getReportItems().get(0).getTotalIncome());
    }

    @Test
    public void getWeeklyReport_Valid_ReturnedValidReportObject() {
        ResponseEntity<ReportDTO> res = restTemplate.exchange(URL_PREFIX + "/weekly?year=2021&month=1",HttpMethod.GET, new HttpEntity<>(headers), ReportDTO.class);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(0, res.getBody().getReportItems().get(0).getTotalOutcome());
        Assertions.assertEquals(1100, res.getBody().getReportItems().get(0).getTotalIncome());
    }

}