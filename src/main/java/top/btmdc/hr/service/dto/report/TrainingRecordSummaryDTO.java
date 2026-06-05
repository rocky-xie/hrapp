package top.btmdc.hr.service.dto.report;

import java.io.Serializable;
import java.time.LocalDate;
import top.btmdc.hr.domain.enumeration.TrainingType;

public class TrainingRecordSummaryDTO implements Serializable {

    private Long id;
    private LocalDate trainingDate;
    private TrainingType trainingType;
    private String topic;
    private String mentorName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }
}
