package cn.edu.nenu.controller;

import cn.edu.nenu.config.HttpServlet;
import cn.edu.nenu.domain.Post;
import cn.edu.nenu.domain.User;
import cn.edu.nenu.repository.UserRepository;
import cn.edu.nenu.service.PostService;
import cn.edu.nenu.service.UserService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@CommonsLog
@Controller
@RequestMapping("/post")
public class PostController {

    private static final int PAGE_SIZE = 20;

    @Autowired
    public PostService postService;
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
          Page<Post> posts = postService.getEntityPage(searchParams, pageNumber, PAGE_SIZE, "sort");
        model.addAttribute("param",param);
        model.addAttribute("posts", posts);
        //model.addAttribute("sortType", sortType);
        model.addAttribute("PAGE_SIZE", PAGE_SIZE);
        model.addAttribute("searchParams", HttpServlet.encodeParameterStringWithPrefix(searchParams, "s_"));
        String path = "/WEB-INF/views/post/list.jsp";
        return "post/list"; //视图名，视图路径
    }//孙燕写的


    @GetMapping("/{id}")
    public Post get(@PathVariable("id")Integer id){
        return postService.findById(id);
    }

    /**
     * 进入新增页面
     */
    @GetMapping(value = "create")
    public String createForm(Model model) {
        Map<String,Object> param=new HashMap();
        Page<User> users = userService.getPage(1, 20, param);
        //System.out.println("aaa"+users.getSize());
        model.addAttribute("users", users);

        model.addAttribute("post", new Post());
        model.addAttribute("action", "create");

        return "post/form";
    }//孙燕写的

    @PostMapping("create")
    public String create(@Valid Post newPost, ServletRequest request,RedirectAttributes attributes){
        int id = Integer.parseInt(request.getParameter("name1"));

        User obj=userService.findOne((long)id);
        newPost.setCreator(obj);
        postService.save(newPost);
        attributes.addAttribute("message","保存成功");
        return "redirect:/post/";
    }

    //以下均是孙燕写的
    @GetMapping(value = "update/{id}")
    public String updateForm(@PathVariable("id") Integer pkId, Model model){
        Post post =  postService.findOne(pkId);
        model.addAttribute("post",post);
        model.addAttribute("action", "update");
        return "post/form";
    }

    /**
     * 页面编辑后，保存
     */
    @PostMapping(value = "update")
    public String update(@Valid  Post post, RedirectAttributes redirectAttributes){
        Integer pkId = post.getId();
        Post newPost =  postService.findOne(pkId);
        newPost.setTitle(post.getTitle());


        postService.save(newPost);
        redirectAttributes.addFlashAttribute("message", "更改用户信息成功");
        return "redirect:/post/";
    }

    @GetMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") Integer pkId, RedirectAttributes redirectAttributes) {
        String message = "删除用户成功";
        try {
            postService.remove(pkId);
        }catch (Exception e){
            message = "删除用户失败";
        }
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/post/";
    }

    /**
     * 批量删除
     */
    @PostMapping(value = "delete")
    public String deleteBatch(ServletRequest request){
        String[] chkIds = request.getParameterValues("chkIds");
        for (String id:chkIds){
            postService.remove(Integer.valueOf(id));
        }
        return "redirect:/post/";
    }

}
