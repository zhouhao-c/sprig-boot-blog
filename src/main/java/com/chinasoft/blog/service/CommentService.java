package com.chinasoft.blog.service;

import com.chinasoft.blog.po.Blog;
import com.chinasoft.blog.po.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> listCommentByBlogId(Long blogId);
    Comment saveComment(Comment comment);
}
