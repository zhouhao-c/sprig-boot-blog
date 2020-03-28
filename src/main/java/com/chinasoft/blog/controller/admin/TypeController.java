package com.chinasoft.blog.controller.admin;

import com.chinasoft.blog.po.Type;
import com.chinasoft.blog.service.TypeService;
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
public class TypeController {

    @Autowired
    private TypeService typeService;

    private static Long id = new Long(1);

    @GetMapping("/types")
    public String types(@PageableDefault(size = 6,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(){
        return "admin/type-input";
    }

    @GetMapping("/types/saveId")
    @ResponseBody
    public Type saveId(Long id){
        Type type = typeService.getType(id);
        this.id = id;
//        return "redirect:/admin/types";
        return type;
    }

    @PostMapping("/types")
    public String post(Type type, RedirectAttributes attributes){

        Type type1 = typeService.getTypeByName(type.getName());
        if(type1 != null){
            attributes.addFlashAttribute("msg","提交失败,标题重复!");
            return "redirect:/admin/types/input";
        }else {
            Type t = typeService.saveType(type);
            if(t == null){
                attributes.addFlashAttribute("msg","提交失败!");
                return "redirect:/admin/types/input";
            }else {
                attributes.addFlashAttribute("msg","提交成功");
                return "redirect:/admin/types";
            }
        }
    }

    @PostMapping("/types/edit")
    public String edit(Type type, RedirectAttributes attributes){
        Type type1 = typeService.getTypeByName(type.getName());
        type.setId(this.id);
        if(type1 != null){
            attributes.addFlashAttribute("msg","修改失败，标题重复!");
        }else {
            Type t = typeService.updateType(this.id,type);
            attributes.addFlashAttribute("msg","修改成功!");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("msg","删除成功!");
        return "redirect:/admin/types";
    }
}
