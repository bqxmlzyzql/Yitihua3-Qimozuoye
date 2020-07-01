package cn.edu.nenu.controller;

import cn.edu.nenu.config.HttpServlet;
import cn.edu.nenu.domain.Category;
import cn.edu.nenu.domain.User;
import cn.edu.nenu.service.CategoryService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * UserController Class
 *
 * @author <b>Oxidyc</b>, Copyright &#169; 2003
 * @version 1.0, 2020-03-04 22:54
 */
@CommonsLog
@Controller
@RequestMapping("/category")
public class CategoryController {

    private static final int PAGE_SIZE = 20;

    @Autowired
    public CategoryService categoryService;

    /**
     * 列表页面，涉及到分页
     * @param pageNumber
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("")
    public String list(@RequestParam(value = "page",defaultValue = "1")int pageNumber,
                       Model model, ServletRequest request){
        String param = request.getParameter("param");
          Map<String, Object> searchParams = HttpServlet.getParametersStartingWith(request, "s_");
          Page<Category> categorys = categoryService.getEntityPage(searchParams, pageNumber, PAGE_SIZE, "sort");
        model.addAttribute("param",param);
        model.addAttribute("categorys", categorys);
        //model.addAttribute("sortType", sortType);
        model.addAttribute("PAGE_SIZE", PAGE_SIZE);
        model.addAttribute("searchParams", HttpServlet.encodeParameterStringWithPrefix(searchParams, "s_"));
        String path = "/WEB-INF/views/category/list.jsp";
        return "category/list"; //视图名，视图路径
    }//孙燕写的

    /**
     * 根据主键ID获取实体，获取详细信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Category get(@PathVariable("id")Integer id){
        return categoryService.findById(id);
    }

    /**
     * 进入新增页面
     */
    @GetMapping(value = "create")
    public String createForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("action", "create");
        return "category/form";
    }//孙燕写的

    @PostMapping("create")
    public String create(@Valid Category newCategory, RedirectAttributes attributes){
        categoryService.save(newCategory);
        attributes.addAttribute("message","保存成功");
        return "redirect:/category/";
    }

    //以下均是孙燕写的
    @GetMapping(value = "update/{id}")
    public String updateForm(@PathVariable("id") Integer pkId, Model model){
        Category category =  categoryService.findOne(pkId);
        model.addAttribute("category",category);
        model.addAttribute("action", "update");
        return "category/form";
    }

    /**
     * 页面编辑后，保存
     */
    @PostMapping(value = "update")
    public String update(@Valid  Category category, RedirectAttributes redirectAttributes){
        Integer pkId = category.getId();
        Category newCategory =  categoryService.findOne(pkId);
        newCategory.setName(category.getName());
        newCategory.setSort(category.getSort());
        newCategory.setStatus(category.getStatus());

        categoryService.save(newCategory);
        redirectAttributes.addFlashAttribute("message", "更改用户信息成功");
        return "redirect:/category/";
    }

    @GetMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") Integer pkId, RedirectAttributes redirectAttributes) {
        String message = "删除用户成功";
        try {
            categoryService.remove(pkId);
        }catch (Exception e){
            message = "删除用户失败";
        }
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/category/";
    }

    /**
     * 批量删除
     */
    @PostMapping(value = "delete")
    public String deleteBatch(ServletRequest request){
        String[] chkIds = request.getParameterValues("chkIds");
        for (String id:chkIds){
            categoryService.remove(Integer.valueOf(id));
        }
        return "redirect:/category/";
    }

}
