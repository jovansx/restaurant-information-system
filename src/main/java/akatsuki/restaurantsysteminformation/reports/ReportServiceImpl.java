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

    // Testni slucajevi:
    // Outcome: za jedan mesec npr: uzmi plate svih u tom periodu, tako da napravi dve plate u jednom mesecu
    @Override
    public ReportDTO getMonthlyReport() {
        int year = LocalDate.now().getYear();
        List<ReportItemDTO> months = new ArrayList<>();
        List<User> users = userRepository.findAll();

        LocalDateTime startDate = LocalDateTime.of(year, 1,1,0,0);
        for(int i = 0; i < 11; i++) {
            LocalDateTime endDate = startDate.plusMonths(1);
            double totalMonthIncome = getOrderPricesForMonth(startDate, endDate);
            double totalMonthOutcome = getSalariesForMonth(users, startDate, endDate);

            ReportItemDTO month = new ReportItemDTO(startDate.getMonth().toString(), totalMonthIncome, totalMonthOutcome);
            months.add(month);
            startDate = startDate.plusMonths(1);
        }
        ReportDTO monthsReportDTO = new ReportDTO(months);
        return monthsReportDTO;
    }

    private double getOrderPricesForMonth(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO proveri da li ukljucuje datum
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
    public ReportDTO getQuarterlyReport() {
        return null;
    }


}
