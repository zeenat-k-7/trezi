package com.trezi.assessment.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;

public class SubmitAssessmentRequest {
    @JsonAlias("answers")
    private List<AnswerDto> responses;

    public List<AnswerDto> getResponses() { return responses; }
    public void setResponses(List<AnswerDto> responses) { this.responses = responses; }
}
