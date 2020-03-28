package com.chinasoft.blog.service;

import com.chinasoft.blog.po.Blog;
import com.chinasoft.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);
    Page<Blog> listBlog(Pageable pageable);
    Page<Blog> listBlog(Pageable pageable,Long tagId);
    Page<Blog> listBlog(Pageable pageable,String query);
    Blog saveBlog(Blog blog);
    Blog updateBlog(Long id,Blog blog);
    void deleteBlog(Long id);
    List<Blog> listBlogTop(Integer size);
    Blog getAndConvert(Long id);
    Map<String,List<Blog>> archiveBlog();
    Long countBlog();
}
