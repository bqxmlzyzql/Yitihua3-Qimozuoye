package cn.edu.nenu.controller;

import cn.edu.nenu.config.HttpServlet;
import cn.edu.nenu.domain.Dictionary;
import cn.edu.nenu.domain.User;
import cn.edu.nenu.repository.UserRepository;
import cn.edu.nenu.service.UserService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static cn.edu.nenu.config.Constants.PAGE_SIZE;

/**
 * UserController Class
 *
 * @author <b>Oxidyc</b>, Copyright &#169; 2003
 * @version 1.0, 2020-03-04 22:54
 */
@CommonsLog
@Controller
@RequestMapping("/user")
public class UserController {

    private static final int PAGE_SIZE = 20;

    @Autowired
    public UserService userService;

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

//        String description = request.getParameter("s_LIKE_name");
//        System.out.println("a--"+description);
//        Map<String, Object> searchParams = HttpServlet.getParametersStartingWith(request, "s_");
//        Page<User> users = userService.getPage(pageNumber,PAGE_SIZE,searchParams);

          Map<String, Object> searchParams = HttpServlet.getParametersStartingWith(request, "s_");
          Page<User> users = userService.getEntityPage(searchParams, pageNumber, PAGE_SIZE, "sort");

        model.addAttribute("param",param);
        model.addAttribute("users", users);
        //model.addAttribute("sortType", sortType);
        model.addAttribute("PAGE_SIZE", PAGE_SIZE);
        model.addAttribute("searchParams", HttpServlet.encodeParameterStringWithPrefix(searchParams, "s_"));
        String path = "/WEB-INF/views/user/list.jsp";
        return "user/list"; //视图名，视图路径
    }//孙燕写的

    /**
     * 根据主键ID获取实体，获取详细信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public User get(@PathVariable("id")Long id){
        return userService.findById(id);
    }

    /**
     * 进入创建用户页面
     * @param model
     * @return
     */
//    @GetMapping("/create")
//    public String createForm(Model model){
//        return "user/form";
//    }
//    @GetMapping("/create")
//    public ModelAndView createFrm(Model model){
//        return new ModelAndView("user/form");
//    }

    /**
     * 进入新增页面
     */
    @GetMapping(value = "create")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("action", "create");
        return "user/form";
    }//孙燕写的
    /**
     * 填写用户信息后保存信息到数据库
     *
     * form表单标签属性name的值
     * user.username
     * user.password
     * user.createdAt
     * <input type="text" name="user.username" value=""/>
     *
     *
     * @param attributes
     * @return
     */
    @PostMapping("create")
    public String create(@Valid User newUser, RedirectAttributes attributes){
        userService.save(newUser);
        attributes.addAttribute("message","保存成功");
        //attributes.addAttribute("user",newUser);
        return "redirect:/user/";
    }

    //以下均是孙燕写的
    @GetMapping(value = "update/{id}")
    public String updateForm(@PathVariable("id") Long pkId, Model model){
        User user =  userService.findOne(pkId);
        model.addAttribute("user",user);
        model.addAttribute("action", "update");
        return "user/form";
    }

    /**
     * 页面编辑后，保存
     */
    @PostMapping(value = "update")
    public String update(@Valid  User user, RedirectAttributes redirectAttributes){
        Long pkId = user.getId();
        User newUser =  userService.findOne(pkId);
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setName(user.getName());
        newUser.setAge(user.getAge());
        newUser.setSex(user.getSex());

        userService.save(newUser);
        redirectAttributes.addFlashAttribute("message", "更改用户信息成功");
        return "redirect:/user/";
    }

    @GetMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") Long pkId, RedirectAttributes redirectAttributes) {
        String message = "删除用户成功";
        try {
            userService.remove(pkId);
        }catch (Exception e){
            message = "删除用户失败";
        }
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/user/";
    }

    /**
     * 批量删除
     */
    @PostMapping(value = "delete")
    public String deleteBatch(ServletRequest request){
        String[] chkIds = request.getParameterValues("chkIds");
        for (String id:chkIds){
            userService.remove(Long.valueOf(id));
        }
        return "redirect:/user/";
    }

    //@PostMapping
    //public String createJSON(@RequestBody String json, RedirectAttributes attributes){
    //    //对json进行发序列化，变成参数对象
    //    return "redirect:/user"; //视图路径
    //}
    //@PostMapping
    //public String createRequest(HttpServletRequest request, RedirectAttributes attributes){
    //    String username = request.getParameter("username");
    //    User newUser = new User();
    //    newUser.setUsername(username);
    //
    //    userService.save(newUser);
    //    return "redirect:/user"; //视图路径
    //}


}
