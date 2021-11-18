package akatsuki.restaurantsysteminformation.reports;

import akatsuki.restaurantsysteminformation.reports.dto.ReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Validated
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/monthly")
    public ReportDTO getMonthlyReport(@RequestParam("year") int year) {
        return reportService.getMonthlyReport(year);
    }

    @GetMapping("/quarterly")
    public ReportDTO getQuarterlyReport(@RequestParam("year") int year) {
        return reportService.getQuarterlyReport(year);
    }
}
