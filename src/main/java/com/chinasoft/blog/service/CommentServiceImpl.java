package com.chinasoft.blog.service;

import com.chinasoft.blog.dao.CommentRepository;
import com.chinasoft.blog.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.ASC,"createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if(parentCommentId != -1){
            comment.setParentComment(commentRepository.findById(parentCommentId).orElse(null));
        }else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments){
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        combineChildren(commentsView);
        return commentsView;
    }

    private void combineChildren(List<Comment> comments){
        for (Comment comment : comments){
            List<Comment> replys1 = comment.getReplayComents();
            for (Comment reply1 : replys1){
                recursively(reply1);
            }
            comment.setReplayComents(tempReplys);
            tempReplys = new ArrayList<>();
        }
    }

    private List<Comment> tempReplys = new ArrayList<>();

    private void recursively(Comment comment){
        tempReplys.add(comment);
        if(comment.getReplayComents().size()>0){
            List<Comment> replys = comment.getReplayComents();
            for (Comment reply : replys){
                tempReplys.add(reply);
                if(reply.getReplayComents().size()>0){
                    recursively(reply);
                }
            }
        }
    }
}
