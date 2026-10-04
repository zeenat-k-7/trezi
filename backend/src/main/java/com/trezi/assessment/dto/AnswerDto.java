package com.trezi.assessment.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.UUID;

public class AnswerDto {
    private UUID questionId;

    @JsonAlias("selectedOptionId")
    private String selectedAnswer;

    public UUID getQuestionId() { return questionId; }
    public void setQuestionId(UUID questionId) { this.questionId = questionId; }
    public String getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer; }
}
