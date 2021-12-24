package akatsuki.restaurantsysteminformation.reports;

import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderRepository;
import akatsuki.restaurantsysteminformation.registereduser.RegisteredUser;
import akatsuki.restaurantsysteminformation.reports.dto.ReportDTO;
import akatsuki.restaurantsysteminformation.reports.dto.ReportItemDTO;
import akatsuki.restaurantsysteminformation.salary.Salary;
import akatsuki.restaurantsysteminformation.user.User;
import akatsuki.restaurantsysteminformation.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReportServiceIntegrationTest {

    @Autowired
    ReportServiceImpl reportService;

    @Test
    void getMonthlyReport_Valid_ReturnedValidReportObject() {
        ReportDTO foundReport = reportService.getMonthlyReport(2021);

        Assertions.assertEquals(5400, foundReport.getReportItems().get(0).getTotalOutcome());
        Assertions.assertEquals(1108, foundReport.getReportItems().get(0).getTotalIncome());
    }

    @Test
    void getQuarterlyReport_Valid_ReturnedValidReportObject() {
        ReportDTO foundReport = reportService.getQuarterlyReport(2021);
        Assertions.assertEquals(30300, foundReport.getReportItems().get(0).getTotalOutcome());
        Assertions.assertEquals(5708, foundReport.getReportItems().get(0).getTotalIncome());
    }

    @Test
    void getWeeklyReport_Valid_ReturnedValidReportObject() {
        ReportDTO foundReport = reportService.getWeeklyReport(1, 2021);
        Assertions.assertEquals(0, foundReport.getReportItems().get(0).getTotalOutcome());
        Assertions.assertEquals(1100, foundReport.getReportItems().get(0).getTotalIncome());
    }

}