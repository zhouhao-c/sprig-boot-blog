package com.chinasoft.blog.controller.admin;

import com.chinasoft.blog.po.Blog;
import com.chinasoft.blog.po.Type;
import com.chinasoft.blog.po.User;
import com.chinasoft.blog.service.BlogService;
import com.chinasoft.blog.service.TagService;
import com.chinasoft.blog.service.TypeService;
import com.chinasoft.blog.util.FileUpload;
import com.chinasoft.blog.vo.BlogQuery;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    private static final String INPUT = "admin/create-blog";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 6,sort = {"updateTime"},
            direction = Sort.Direction.DESC)Pageable pageable, BlogQuery blog, Model model){
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        model.addAttribute("types",typeService.listType());
        return LIST;
    }
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 6,sort = {"updateTime"},
            direction = Sort.Direction.DESC)Pageable pageable, BlogQuery blog, Model model){
        if(blog.getTypeId() == 0){
            blog.setTypeId(null);
        }
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blogs :: blogList";
    }
    @GetMapping("/blogs/input")
    public String input(Model model){
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return INPUT;
    }
    private void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }
    @GetMapping("/blogs/{id}/delete")
    public String delete(RedirectAttributes attributes,@PathVariable Long id){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("msg","删除成功");
        return REDIRECT_LIST;
    }
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        System.out.println(blog.getTagIds());
        Blog b;
        if(blog.getId() == null){
            b = blogService.saveBlog(blog);
        }else {
            b = blogService.updateBlog(blog.getId(),blog);
        }
        if(b == null){
            attributes.addFlashAttribute("msg","操作失败");
        }else {
            attributes.addFlashAttribute("msg","操作成功");
        }
        return REDIRECT_LIST;
    }
    @RequestMapping("/imageUpload")
    public void imageUpload(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file,
                            HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter wirte = null;
        JSONObject json = new JSONObject();
        try {
            wirte = response.getWriter();
            //文件存放的路径
//            String path = request.getSession().getServletContext().getRealPath("upload");
            String path = "D:\\IDEAworkspace\\blog-image\\upload";
            String url = "http://localhost:8080"
                    + request.getContextPath()
                    + "//upload//"
                    + FileUpload.upload(request, file, path);
            json.put("success", 1);
            json.put("message", "hello");
            json.put("url", url);
        } catch (Exception e) {
        }finally{
            wirte.print(json);
            wirte.flush();
            wirte.close();
        }
    }
}
