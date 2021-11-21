package akatsuki.restaurantsysteminformation.reports;

import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderRepository;
import akatsuki.restaurantsysteminformation.reports.dto.ReportDTO;
import akatsuki.restaurantsysteminformation.reports.dto.ReportItemDTO;
import akatsuki.restaurantsysteminformation.salary.Salary;
import akatsuki.restaurantsysteminformation.user.User;
import akatsuki.restaurantsysteminformation.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
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
            double totalMonthIncome = getOrderPricesForPeriod(startDate, endDate.minusSeconds(1));
            double totalMonthOutcome = getSalariesForPeriod(users, endDate.minusSeconds(1));

            ReportItemDTO month = new ReportItemDTO(startDate.getMonth().toString(), totalMonthIncome, totalMonthOutcome);
            months.add(month);
            if(endDate.isAfter(today)) break;
            startDate = startDate.plusMonths(1);
        }
        return new ReportDTO(months);
    }

    private double getOrderPricesForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findAllByCreatedAtBetween(startDate, endDate);
        double totalMonthIncome = 0;
        for(Order o: orders) {
            totalMonthIncome += o.getTotalPrice();
        }
        return totalMonthIncome;
    }

    private double getSalariesForPeriod(List<User> users, LocalDateTime endDate) {
        double salariesForMonth = 0;
        for (User u : users) {
            List<Salary> salaries = new ArrayList<>();
            for (Salary s : u.getSalary()) {
                if (s.getCreatedAt().isBefore(endDate)) {
                    salaries.add(s);
                }
            }
            if(salaries.size() > 0) {
                salaries.sort((s1, s2) -> {
                    ZonedDateTime zdt = ZonedDateTime.of(s1.getCreatedAt(), ZoneId.systemDefault());
                    ZonedDateTime zdt2 = ZonedDateTime.of(s2.getCreatedAt(), ZoneId.systemDefault());
                    return zdt.toInstant().toEpochMilli() < zdt2.toInstant().toEpochMilli() ? -1 : 0;
                });
                salariesForMonth += salaries.get(salaries.size() - 1).getValue();
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

                totalQuarterIncome += getOrderPricesForPeriod(startDate, endDate.minusSeconds(1));
                totalQuarterOutcome += getSalariesForPeriod(users, endDate.minusSeconds(1));

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

    @Override
    public ReportDTO getWeeklyReport(int month, int year) {

        LocalDateTime today = LocalDateTime.now();
        List<ReportItemDTO> weeks = new ArrayList<>();

        LocalDateTime startDate = LocalDateTime.of(year, month,1,0,0);
        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        int daysLeft = daysInMonth - 28;
        boolean isEnd = false;
        for(int i = 0; i < 4; i++) {
            LocalDateTime endDate = startDate.plusDays(7);
            if(endDate.isAfter(today)) {
                isEnd = true;
                break;
            }
            double totalDayIncome = getOrderPricesForPeriod(startDate, endDate.minusSeconds(1));

            ReportItemDTO day = new ReportItemDTO(i*7 + " - " + (i+1)*7, totalDayIncome, 0);
            weeks.add(day);

            startDate = endDate;
        }

        LocalDateTime endDate = startDate.plusDays(daysLeft);
        if(!isEnd) {
            double totalDayIncome = getOrderPricesForPeriod(startDate, endDate.minusSeconds(1));
            ReportItemDTO day = new ReportItemDTO("28 - " + String.valueOf(daysLeft + 28), totalDayIncome, 0);
            weeks.add(day);
        }

        return new ReportDTO(weeks);
    }


}
