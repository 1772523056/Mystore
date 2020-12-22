package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public void createOrUpdate (User user) {
        UserExample example = new UserExample ();
        example.createCriteria ()
                .andAccountIdEqualTo (user.getAccountId ());
        List<User> users = userMapper.selectByExample (example);
        if(users.size ()==0){
            user.setTimeCreate (System.currentTimeMillis ());
            user.setTimeModify (user.getTimeCreate ());
           userMapper.insert (user);
        }else{
            User dbUser=users.get(0);
            User updateUser= new User ();
            updateUser.setTimeCreate (System.currentTimeMillis ());
            updateUser.setAccountId (user.getAccountId ());
            updateUser.setTimeModify (user.getTimeCreate ());
            updateUser.setAvatarUrl (user.getAvatarUrl ());
            updateUser.setName (user.getName ());
            updateUser.setToken (user.getToken ());
            UserExample example1=new UserExample ();
            example1.createCriteria ()
                    .andIdEqualTo (dbUser.getId ());
            userMapper.updateByExampleSelective (updateUser, example1);
        }
    }
}
