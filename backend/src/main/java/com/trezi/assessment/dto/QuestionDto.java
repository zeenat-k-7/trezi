package com.trezi.assessment.dto;

import java.util.List;
import java.util.UUID;

public class QuestionDto {
    private UUID id;
    private String questionText;
    private String topic;
    private String difficulty;
    private List<String> options;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
}
