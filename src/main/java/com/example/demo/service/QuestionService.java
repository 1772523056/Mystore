package com.example.demo.service;

import com.example.demo.DTO.PagesDTO;
import com.example.demo.DTO.QuestionDTO;
import com.example.demo.exception.CustomizeErrorCode;
import com.example.demo.exception.CustomizeException;
import com.example.demo.mapper.QuestionExtMapper;
import com.example.demo.mapper.QuestionMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Question;
import com.example.demo.model.QuestionExample;
import com.example.demo.model.User;
import com.oracle.xmlns.internal.webservices.jaxws_databinding.ExistingAnnotationsType;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public QuestionDTO getById (Integer id) {
        Question question = questionMapper.selectByPrimaryKey (id);
        if(question==null){
            throw new CustomizeException (CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO ();
        User user = userMapper.selectByPrimaryKey (Integer.valueOf (question.getCreator ()));
        BeanUtils.copyProperties (question, questionDTO);
        questionDTO.setUser (user);
        return questionDTO;
    }

    public PagesDTO list (Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        QuestionExample example = new QuestionExample ();
        example.setOrderByClause ("time_create desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds (example, new RowBounds (offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<> ();
        PagesDTO pagesDTO = new PagesDTO ();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey (Integer.valueOf (question.getCreator ()));
            QuestionDTO questionDTO = new QuestionDTO ();
            BeanUtils.copyProperties (question, questionDTO);
            questionDTO.setUser (user);
            questionDTOList.add (questionDTO);
        }
        pagesDTO.setQuestionList (questionDTOList);
        Integer count = (int) questionMapper.countByExample (new QuestionExample ());
        pagesDTO.setPages (page, size, count);
        return pagesDTO;
    }

    public PagesDTO list (Integer id, Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        QuestionExample example1 = new QuestionExample ();
        example1.createCriteria ()
                .andIdEqualTo (id);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds (example1, new RowBounds (offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<> ();
        PagesDTO pagesDTO = new PagesDTO ();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey (Integer.valueOf (question.getCreator ()));
            QuestionDTO questionDTO = new QuestionDTO ();
            BeanUtils.copyProperties (question, questionDTO);
            questionDTO.setUser (user);
            questionDTOList.add (questionDTO);
        }
        pagesDTO.setQuestionList (questionDTOList);
        QuestionExample example = new QuestionExample ();
        example.createCriteria ()
                .andIdEqualTo (id);
        Integer count = (int) questionMapper.countByExample (example);
        pagesDTO.setPages (page, size, count);
        return pagesDTO;
    }

    public void createOrUpdate (Question question) {
        if (question.getId () == null) {
            question.setTimeModify (question.getTimeCreate ());
            question.setCommentCount (0);
            question.setViewCount (0);
            question.setLikeCount (0);
            questionMapper.insert (question);
        } else {
            Question updateQuestion = new Question ();
            updateQuestion.setTimeModify (System.currentTimeMillis ());
            updateQuestion.setDescription (question.getDescription ());
            updateQuestion.setTitle (question.getTitle ());
            updateQuestion.setTag (question.getTag ());
            updateQuestion.setId (question.getId ());
            QuestionExample example = new QuestionExample ();
            example.createCriteria ()
                    .andIdEqualTo (question.getId ());
            int update = questionMapper.updateByExampleSelective (updateQuestion, example);
            if(update!=1){
                throw new CustomizeException (CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void updateViewCount (Integer id) {
       Question question=new Question ();
       question.setId (id);
       question.setViewCount (1);
       questionExtMapper.updateView (question);
    }
}
