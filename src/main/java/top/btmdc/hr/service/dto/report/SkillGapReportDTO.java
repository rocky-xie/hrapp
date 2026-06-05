package top.btmdc.hr.service.dto.report;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class SkillGapReportDTO implements Serializable {

    private LocalDate reportDate;
    private int totalPositions;
    private List<PositionGapDTO> positions;

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public int getTotalPositions() {
        return totalPositions;
    }

    public void setTotalPositions(int totalPositions) {
        this.totalPositions = totalPositions;
    }

    public List<PositionGapDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionGapDTO> positions) {
        this.positions = positions;
    }
}
