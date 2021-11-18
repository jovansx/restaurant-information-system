package akatsuki.restaurantsysteminformation.reports.dto;

public class ReportItemDTO {
    private String month;
    private double totalIncome;
    private double totalOutcome;

    public ReportItemDTO(String month, double totalIncome, double totalOutcome) {
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalOutcome = totalOutcome;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalOutcome() {
        return totalOutcome;
    }

    public void setTotalOutcome(double totalOutcome) {
        this.totalOutcome = totalOutcome;
    }
}
