package com.chinasoft.blog.service;

import com.chinasoft.blog.po.User;


public interface UserService {
    User checkUser(String username,String password);
}
