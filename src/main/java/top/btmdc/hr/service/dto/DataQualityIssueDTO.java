package top.btmdc.hr.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class DataQualityIssueDTO implements Serializable {

    private String entityType;

    private String severity;

    private String field;

    private String message;

    private Long entityId;

    private String entityLabel;

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityLabel() {
        return entityLabel;
    }

    public void setEntityLabel(String entityLabel) {
        this.entityLabel = entityLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataQualityIssueDTO)) return false;
        DataQualityIssueDTO that = (DataQualityIssueDTO) o;
        return Objects.equals(entityType, that.entityType) && Objects.equals(field, that.field) && Objects.equals(entityId, that.entityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityType, field, entityId);
    }

    @Override
    public String toString() {
        return "DataQualityIssueDTO{entityType='" + entityType + "', severity='" + severity + "', message='" + message + "'}";
    }
}
