package com.online.exam.repository;

import com.online.exam.model.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Questions,Long> {
    @Query(value = "SELECT * FROM questions ORDER BY RAND() LIMIT :numberOfRows", nativeQuery = true)
    public List<Questions> nNumberOfQuestions(int numberOfRows);

    @Query(value = "SELECT * FROM questions WHERE question_stream=:stream ORDER BY RAND() LIMIT :numberOfRows", nativeQuery = true)
    public List<Questions> getQuestionsByStreamWithCount(int numberOfRows,String stream);
    @Query("SELECT questionStream from Questions q group by questionStream")
    public List<String> getStreamLov();
    public List<Questions> findByQuestionIn(List<String> questions);
}
