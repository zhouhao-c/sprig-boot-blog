package com.chinasoft.blog.controller.admin;

import com.chinasoft.blog.po.Tag;
import com.chinasoft.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private TagService tagService;

    private static Long id = new Long(1);

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 6,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                       Model model){
        model.addAttribute("page",tagService.listTag(pageable));
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(){
        return "admin/tag-input";
    }

    @GetMapping("/tags/saveId")
    @ResponseBody
    public Tag saveId(Long id){
        Tag tag = tagService.getTag(id);
        this.id = id;
//        return "redirect:/admin/types";
        return tag;
    }

    @PostMapping("/tags")
    public String post(Tag tag, RedirectAttributes attributes){

        Tag t1 = tagService.getTagByName(tag.getName());
        if(t1 != null){
            attributes.addFlashAttribute("msg","提交失败,标题重复!");
            return "redirect:/admin/tags/input";
        }else {
            Tag t = tagService.saveTag(tag);
            if(t == null){
                attributes.addFlashAttribute("msg","提交失败!");
                return "redirect:/admin/tags/input";
            }else {
                attributes.addFlashAttribute("msg","提交成功");
                return "redirect:/admin/tags";
            }
        }
    }

    @PostMapping("/tags/edit")
    public String edit(Tag tag, RedirectAttributes attributes){
        Tag t1 = tagService.getTagByName(tag.getName());
        tag.setId(this.id);
        if(t1 != null){
            attributes.addFlashAttribute("msg","修改失败，标题重复!");
        }else {
            Tag t = tagService.updateTag(this.id,tag);
            attributes.addFlashAttribute("msg","修改成功!");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("msg","删除成功!");
        return "redirect:/admin/tags";
    }
}
