package com.online.exam.service.impl;

import com.online.exam.model.Questions;
import com.online.exam.model.SubmittedAnswer;
import com.online.exam.model.UserModel;
import com.online.exam.repository.QuestionRepository;
import com.online.exam.repository.SubmittedAnswerRepository;
import com.online.exam.repository.UserModelRepository;
import com.online.exam.service.QuestionService;
import com.online.exam.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final SubmittedAnswerRepository submittedAnswerRepository;
    private final UserModelRepository userModelRepository;
    private final ReportService reportService;
    private final EmailService emailService;
    @Override
    public List<Questions> allQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Questions addQuestion(Questions questions) {
        return questionRepository.save(questions);
    }

    @Override
    public List<Questions> addAllQuestion(List<Questions> questions){
        List<Questions> questionList = new ArrayList<>();
        try {
           questionList = questionRepository.saveAll(questions);

        }catch (Exception e){
            e.printStackTrace();
        }
        return questionList;
    }

    @Override
    public String deleteQuestion(Long questionId){
        String result = "";
        try {
            if(questionRepository.findById(questionId).isPresent()){
                questionRepository.deleteById(questionId);
                result =questionId+" deleted successfully";
            }else{
                result =questionId+" is not available";
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Questions> nNumberOfQuestions(Integer rowCount) {
        return questionRepository.nNumberOfQuestions(rowCount);
    }

    @Override
    public List<Questions> nNumberOfQuestionsWithStream(Integer rowCount,String stream) {
        return questionRepository.getQuestionsByStreamWithCount(rowCount,stream);
    }

    @Override
    public Map<String,Object> mark(List<Questions> questionSet, List<String> givenAnswers,Integer eachQuestionMark) throws Exception {
        List<SubmittedAnswer> submittedAnswers = new ArrayList<>();
        Long id = submittedAnswerRepository.getExamId();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel byUsername = userModelRepository.findByUsername(name).get();
        for (int i=0;i<questionSet.size();i++){
            Questions question = questionSet.get(i);
            SubmittedAnswer submittedAnswer = new SubmittedAnswer();
            submittedAnswer.setSubmittedQuestionId(question.getQuestionId());
            submittedAnswer.setSubmittedAnswer(givenAnswers.get(i));
            submittedAnswer.setExamId(id);

            submittedAnswer.setUserId(byUsername.getUserId());
            submittedAnswers.add(submittedAnswer);
        }
        submittedAnswerRepository.saveAll(submittedAnswers);
        List<String> realAnswers = questionSet.stream().map(questions -> questions.getAnswer().getAnswer()).toList();
        String questionStream = questionSet.get(0).getQuestionStream();
        long answered = givenAnswers.stream().filter(i -> i != "").count();
        Long correctAnswers = IntStream.range(0, Math.min(realAnswers.size(), givenAnswers.size()))
                .filter(i -> realAnswers.get(i).equals(givenAnswers.get(i)))
                .count();
        Long securedMark = correctAnswers*eachQuestionMark;
        int size = questionSet.size();
        long totalQuestion  = size;
        String fName = byUsername.getFirstName()!=null?byUsername.getFirstName():"" ;
        String lName = byUsername.getLastName()!=null? byUsername.getLastName():"";
        Map<String,Object> response = new HashMap<>();
        response.put("questionAttended", (long) givenAnswers.size());
        response.put("correctAnswers", correctAnswers);
        response.put("securedMark",securedMark);
        response.put("byUsername",fName+" "+lName);
        response.put("questionStream",questionStream);
        response.put("totalQuestion",totalQuestion);
        response.put("answered",answered);
        response.put("examId",id);
        response.put("email",byUsername.getUsername());

        return response;
    }

    @Override
    public List<String> getStreamLov() {
        return questionRepository.getStreamLov();
    }
}
