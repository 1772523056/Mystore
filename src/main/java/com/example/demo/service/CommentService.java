package com.example.demo.service;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.enums.CommentTypeEnums;
import com.example.demo.enums.NotificationStatusEnums;
import com.example.demo.enums.NotificationTypeEnums;
import com.example.demo.exception.CustomizeErrorCode;
import com.example.demo.exception.CustomizeException;
import com.example.demo.mapper.*;
import com.example.demo.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    QuestionExtMapper questionExtMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentExtMapper commentExtMapper;
    @Autowired
    NotificationMapper notificationMapper;

    public void updateLikeCount (Integer id) {
        Comment comment=new Comment ();
        comment.setId (id);
        comment.setLikeCount (1L);
        commentExtMapper.updateLikeCount (comment);
    }

    @Transactional
    public void insert (Comment comment,User user) {
        if (comment.getParentId () == null || comment.getParentId () == 0) {
            throw new CustomizeException (CustomizeErrorCode.TARGET_PARENT_NOT_FOUND);
        }
        if (comment.getType () == null || !CommentTypeEnums.isExist (comment.getType ())) {
            throw new CustomizeException (CustomizeErrorCode.TYPE_PARAM_ERROR);
        }
        if (comment.getType () == CommentTypeEnums.COMMENT.getType ()) {
            Comment dbComment = commentMapper.selectByPrimaryKey (comment.getParentId ());
            if (dbComment == null) {
                throw new CustomizeException (CustomizeErrorCode.COMMENT_PARAM_ERROR);
            }
            commentMapper.insert (comment);
            Question question = questionMapper.selectByPrimaryKey (dbComment.getParentId ());
            //增加评论的回复数
            Comment comment1 = new Comment ();
            comment1.setId (comment.getParentId ());
            comment1.setCommentCount (1);
            commentExtMapper.updateCommentCount (comment1);
            Integer receiver = dbComment.getCommentator ();
            //创建通知
            createNotify (comment, receiver,user.getName (),question.getTitle (),
                    question.getId (), NotificationTypeEnums.COMMENT_REPLY);
        } else {

            Question question = questionMapper.selectByPrimaryKey (comment.getParentId ());
            if (question == null) {
                throw new CustomizeException (CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert (comment);
            //增加问题的回复数
            question.setCommentCount (1);
            questionExtMapper.updateCommentCount (question);
            //创建通知
            Integer receiver = Integer.valueOf (question.getCreator ());
            createNotify (comment, receiver,user.getName (),question.getTitle (),
                    question.getId (),NotificationTypeEnums.QUESTION_REPLY);
        }
    }


    private void createNotify (Comment comment, Integer receiver,String notifierName, String title,
                               Integer questionId, NotificationTypeEnums reply) {
        //评论人id 接受评论的人的id 评论人的名字 评论人评论问题的标题 评论人评论的问题的id message
        Notification notification = new Notification ();
        notification.setNotifier (comment.getCommentator ());
        notification.setType (reply.getType ());
        notification.setTimeCreate (System.currentTimeMillis ());
        notification.setOuterId (questionId);
        notification.setStatus (NotificationStatusEnums.UNREAD.getStatus ());
        notification.setReceiver (receiver);
        notification.setOuterTitle (title);
        notification.setNotifierName (notifierName);
        notificationMapper.insert (notification);
    }

    public List<CommentDTO> listByTargetId (Integer id, CommentTypeEnums question) {
        CommentExample commentExample = new CommentExample ();
        commentExample.createCriteria ()
                .andParentIdEqualTo (id)
                .andTypeEqualTo (question.getType ());
        commentExample.setOrderByClause ("time_create desc");
        List<Comment> comments = commentMapper.selectByExample (commentExample);
        if (comments.size () == 0) {
            return new ArrayList<> ();
        }
        //降低时间复杂度 如果不使用这种方法则第一重for循环用来循环comments 第二重for循环用来循环通过comments的commentator找到user

//        //获取不重复的评论人的userId
        Set<Integer> commentators = comments.stream ().map (comment -> comment.getCommentator ()).collect (Collectors.toSet ());
        List<Integer> userId = new ArrayList<> ();
        userId.addAll (commentators);
        //使用UserId获取user转化为map
        UserExample userExample = new UserExample ();
        userExample.createCriteria ()
                .andIdIn (userId);
        List<User> users = userMapper.selectByExample (userExample);
        Map<Integer, User> userMap = users.stream ().collect (Collectors.toMap (user -> user.getId (), user -> user));

        //讲comment转化为commentDTO
        List<CommentDTO> commentDTOs = comments.stream ().map (comment -> {
            CommentDTO commentDTO = new CommentDTO ();
            BeanUtils.copyProperties (comment, commentDTO);
            commentDTO.setUser (userMap.get (comment.getCommentator ()));
            return commentDTO;
        }).collect (Collectors.toList ());
        //此种方法为常规方法
//        List<CommentDTO> commentDTOs=new ArrayList<> ();
//        for (Comment comment : comments) {
//            CommentDTO commentDTO = new CommentDTO ();
//            BeanUtils.copyProperties (comment, commentDTO);
//            User user = userMapper.selectByPrimaryKey (comment.getCommentator ());
//            commentDTO.setUser (user);
//            commentDTOs.add (commentDTO);
//        }
        return commentDTOs;
    }

    public Long getLikeCount (Integer commentId) {
        Comment comment=commentMapper.selectByPrimaryKey (commentId);
        return comment.getLikeCount ();
    }
}
