package akatsuki.restaurantsysteminformation.reports;

import akatsuki.restaurantsysteminformation.reports.dto.ReportDTO;

public interface ReportService {

    ReportDTO getMonthlyReport(int year);
    ReportDTO getQuarterlyReport(int year);
}
