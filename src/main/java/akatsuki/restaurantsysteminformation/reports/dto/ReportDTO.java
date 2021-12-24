package akatsuki.restaurantsysteminformation.reports.dto;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ReportDTO {
    private List<ReportItemDTO> reportItems;

    public ReportDTO(List<ReportItemDTO> reportItems) {
        this.reportItems = reportItems;
    }

    public List<ReportItemDTO> getReportItems() {
        return reportItems;
    }

    public void setReportItems(List<ReportItemDTO> reportItems) {
        this.reportItems = reportItems;
    }
}
