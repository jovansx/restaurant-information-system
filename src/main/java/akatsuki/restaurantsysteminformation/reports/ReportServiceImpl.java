package akatsuki.restaurantsysteminformation.reports;

import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderRepository;
import akatsuki.restaurantsysteminformation.reports.dto.ReportDTO;
import akatsuki.restaurantsysteminformation.reports.dto.ReportItemDTO;
import akatsuki.restaurantsysteminformation.salary.Salary;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserRepository;
import akatsuki.restaurantsysteminformation.user.User;
import akatsuki.restaurantsysteminformation.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public ReportDTO getMonthlyReport(int year) {
        LocalDateTime today = LocalDateTime.now();
        List<ReportItemDTO> months = new ArrayList<>();
        List<User> users = userRepository.findAll();

        LocalDateTime startDate = LocalDateTime.of(year, 1,1,0,0);
        for(int i = 0; i < 12; i++) {
            LocalDateTime endDate = startDate.plusMonths(1);
            double totalMonthIncome = getOrderPricesForMonth(startDate, endDate.minusSeconds(1));
            double totalMonthOutcome = getSalariesForMonth(users, startDate, endDate.minusSeconds(1));

            ReportItemDTO month = new ReportItemDTO(startDate.getMonth().toString(), totalMonthIncome, totalMonthOutcome);
            months.add(month);
            if(endDate.isAfter(today)) break;
            startDate = startDate.plusMonths(1);
        }
        return new ReportDTO(months);
    }

    private double getOrderPricesForMonth(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findAllByCreatedAtBetween(startDate, endDate);
        double totalMonthIncome = 0;
        for(Order o: orders) {
            totalMonthIncome += o.getTotalPrice();
        }
        return totalMonthIncome;
    }

    private double getSalariesForMonth(List<User> users, LocalDateTime startDate, LocalDateTime endDate) {
        double salariesForMonth = 0;
        for (User u : users) {
            for (Salary s : u.getSalary()) {
                if((s.getCreatedAt().isAfter(startDate) && s.getCreatedAt().isBefore(endDate)) || s.getCreatedAt().isEqual(startDate)) {
                    salariesForMonth += s.getValue();
                    break;
                }
            }
        }
        return salariesForMonth;
    }
    @Override
    public ReportDTO getQuarterlyReport(int year) {
        LocalDateTime today = LocalDateTime.now();
        List<ReportItemDTO> quarters = new ArrayList<>();
        List<User> users = userRepository.findAll();

        LocalDateTime startDate = LocalDateTime.of(year, 1,1,0,0);
        for(int i = 0; i < 4; i++) {
            String monthName = startDate.getMonth().toString() + " - " + startDate.plusMonths(3).getMonth().toString();
            double totalQuarterIncome = 0;
            double totalQuarterOutcome = 0;
            boolean toEnd = false;
            for(int j = 0; j < 3; j++) {
                LocalDateTime endDate = startDate.plusMonths(1);

                totalQuarterIncome += getOrderPricesForMonth(startDate, endDate.minusSeconds(1));
                totalQuarterOutcome += getSalariesForMonth(users, startDate, endDate.minusSeconds(1));

                if(endDate.isAfter(today)) {
                    toEnd = true;
                    break;
                }
                startDate = startDate.plusMonths(1);
            }
            ReportItemDTO quarter = new ReportItemDTO(monthName, totalQuarterIncome, totalQuarterOutcome);
            quarters.add(quarter);
            if(toEnd) break;
        }
        return new ReportDTO(quarters);
    }


}
