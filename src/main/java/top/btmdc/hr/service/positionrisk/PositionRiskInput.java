package top.btmdc.hr.service.positionrisk;

import top.btmdc.hr.domain.enumeration.DocumentStatus;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;

public class PositionRiskInput {

    private final Long positionId;
    private final String positionName;
    private final boolean keyPosition;
    private final int ownerCount;
    private final int minimumOwnerCount;
    private final int substitutableOwnerCount;
    private final boolean hasSubstitute;
    private final DocumentStatus documentStatus;
    private final ImportanceLevel customerOrSystemDependency;
    private final ReadinessLevel successionReadiness;

    public PositionRiskInput(
        Long positionId,
        String positionName,
        boolean keyPosition,
        int ownerCount,
        int minimumOwnerCount,
        int substitutableOwnerCount,
        boolean hasSubstitute,
        DocumentStatus documentStatus,
        ImportanceLevel customerOrSystemDependency,
        ReadinessLevel successionReadiness
    ) {
        this.positionId = positionId;
        this.positionName = positionName;
        this.keyPosition = keyPosition;
        this.ownerCount = ownerCount;
        this.minimumOwnerCount = minimumOwnerCount;
        this.substitutableOwnerCount = substitutableOwnerCount;
        this.hasSubstitute = hasSubstitute;
        this.documentStatus = documentStatus;
        this.customerOrSystemDependency = customerOrSystemDependency;
        this.successionReadiness = successionReadiness;
    }

    public Long getPositionId() {
        return positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public boolean isKeyPosition() {
        return keyPosition;
    }

    public int getOwnerCount() {
        return ownerCount;
    }

    public int getMinimumOwnerCount() {
        return minimumOwnerCount;
    }

    public int getSubstitutableOwnerCount() {
        return substitutableOwnerCount;
    }

    public boolean isHasSubstitute() {
        return hasSubstitute;
    }

    public DocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    public ImportanceLevel getCustomerOrSystemDependency() {
        return customerOrSystemDependency;
    }

    public ReadinessLevel getSuccessionReadiness() {
        return successionReadiness;
    }
}
