package com.online.exam.service;

import com.online.exam.model.Questions;

import java.util.List;
import java.util.Map;

public interface QuestionService {
    public List<Questions> allQuestions();
    public Questions addQuestion(Questions questions);
    public List<Questions> addAllQuestion(List<Questions> questions);
    public String deleteQuestion(Long questionId);
    public List<Questions> nNumberOfQuestions(Integer rowCount);
    public List<Questions> nNumberOfQuestionsWithStream(Integer rowCount,String stream);

    public Map<String,Object> mark(List<Questions> questionSet, List<String> answers, Integer eachQuestionMark) throws Exception;
    public List<String> getStreamLov();
}
