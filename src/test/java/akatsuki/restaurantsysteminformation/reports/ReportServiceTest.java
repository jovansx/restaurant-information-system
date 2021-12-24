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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @InjectMocks
    ReportServiceImpl reportService;

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    OrderRepository orderRepositoryMock;

    @Test
    void getMonthlyReport_Valid_ReturnedValidReportObject() {
        User user = new RegisteredUser();
        List<Salary> salaries = new ArrayList<>();
        salaries.add(new Salary(LocalDateTime.of(2020, 11, 1, 0, 0), 800));
        user.setSalary(salaries);
        List<User> list = Collections.singletonList(user);
        Mockito.when(userRepositoryMock.findAll()).thenReturn(list);
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setTotalPrice(100);
        orders.add(order);
        Mockito.when(orderRepositoryMock.findAllByCreatedAtBetween(Mockito.any(), Mockito.any())).thenReturn(orders);

        List<ReportItemDTO> months = new ArrayList<>();
        ReportItemDTO month = new ReportItemDTO("", 100, 800);
        months.add(month);

        ReportDTO foundReport = reportService.getMonthlyReport(2021);

        Assertions.assertEquals(foundReport.getReportItems().get(0).getTotalOutcome(), months.get(0).getTotalOutcome());
        Assertions.assertEquals(foundReport.getReportItems().get(0).getTotalIncome(), months.get(0).getTotalIncome());

    }

    @Test
    void getQuarterlyReport_Valid_ReturnedValidReportObject() {
        User user = new RegisteredUser();
        List<Salary> salaries = new ArrayList<>();
        salaries.add(new Salary(LocalDateTime.of(2020, 11, 1, 0, 0), 800));
        user.setSalary(salaries);
        List<User> list = Collections.singletonList(user);

        Mockito.when(userRepositoryMock.findAll()).thenReturn(list);
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setTotalPrice(100);
        orders.add(order);
        Mockito.when(orderRepositoryMock.findAllByCreatedAtBetween(Mockito.any(), Mockito.any())).thenReturn(orders);

        List<ReportItemDTO> quarters = new ArrayList<>();
        ReportItemDTO quarter = new ReportItemDTO("", 300, 2400);
        quarters.add(quarter);

        ReportDTO foundReport = reportService.getQuarterlyReport(2021);
        Assertions.assertEquals(foundReport.getReportItems().get(0).getTotalOutcome(), quarters.get(0).getTotalOutcome());
        Assertions.assertEquals(foundReport.getReportItems().get(0).getTotalIncome(), quarters.get(0).getTotalIncome());

    }

    @Test
    void getWeeklyReport_Valid_ReturnedValidReportObject() {
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setTotalPrice(100);
        orders.add(order);
        Mockito.when(orderRepositoryMock.findAllByCreatedAtBetween(Mockito.any(), Mockito.any())).thenReturn(orders);

        List<ReportItemDTO> weeks = new ArrayList<>();

        ReportItemDTO day = new ReportItemDTO("", 100, 0);
        weeks.add(day);

        ReportDTO foundReport = reportService.getWeeklyReport(1, 2021);
        Assertions.assertEquals(foundReport.getReportItems().get(0).getTotalOutcome(), weeks.get(0).getTotalOutcome());
        Assertions.assertEquals(foundReport.getReportItems().get(0).getTotalIncome(), weeks.get(0).getTotalIncome());

    }
}