package top.btmdc.hr.service.dto.report;

import java.io.Serializable;
import top.btmdc.hr.domain.enumeration.RequirementImportance;

public class ReportCriteria implements Serializable {

    private boolean includeOwners = true;
    private boolean includeCandidates = true;
    private RequirementImportance minImportance;

    public boolean isIncludeOwners() {
        return includeOwners;
    }

    public void setIncludeOwners(boolean includeOwners) {
        this.includeOwners = includeOwners;
    }

    public boolean isIncludeCandidates() {
        return includeCandidates;
    }

    public void setIncludeCandidates(boolean includeCandidates) {
        this.includeCandidates = includeCandidates;
    }

    public RequirementImportance getMinImportance() {
        return minImportance;
    }

    public void setMinImportance(RequirementImportance minImportance) {
        this.minImportance = minImportance;
    }
}
