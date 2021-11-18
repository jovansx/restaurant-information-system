package akatsuki.restaurantsysteminformation.reports;

import akatsuki.restaurantsysteminformation.reports.dto.ReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Validated
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    public ReportDTO getMonthlyReport() {
        return reportService.getMonthlyReport();
    }
}
