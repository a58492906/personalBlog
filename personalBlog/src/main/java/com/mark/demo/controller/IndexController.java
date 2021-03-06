package com.mark.demo.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mark.demo.entity.User;
import com.mark.demo.service.UserService;

import io.swagger.annotations.ApiOperation;

@Controller
public class IndexController {
  
    @Autowired
    private UserService userService;

  //  @Autowired
  //  private TagService tagService;

  //  @Autowired
  //  private HostHolder hostHolder;

   // @Autowired
   // private JedisService jedisService;
/**
    @RequestMapping(path = {"/","/index"})
    public String index(Model model){
       // List<ViewObject> vos = new ArrayList<>();
        List<Article> articles = articleService.getLatestArticles(0,4);
        for (Article article:articles){
            ViewObject vo = new ViewObject();
            List<Tag> tags = tagService.getTagByArticleId(article.getId());
            String clickCount = jedisService.get(RedisKeyUntil.getClickCountKey("/article/"+article.getId()));
            if (clickCount==null)
                clickCount = "0";
            vo.set("clickCount",clickCount);
            vo.set("article",article);
            vo.set("tags",tags);
            vos.add(vo);
        }
        model.addAttribute("vos",vos);

        List<Tag> tags = tagService.getAllTag();
        model.addAttribute("tags",tags);

        ViewObject pagination = new ViewObject();
        int count = articleService.getArticleCount();
        User user = hostHolder.getUser();
        if (user==null||"admin".equals(user.getRole())){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
        }
        pagination.set("current",1);
        pagination.set("nextPage",2);
        pagination.set("lastPage",count/4+1);
        model.addAttribute("pagination",pagination);

        ViewObject categoryCount = new ViewObject();
        for (String category: JblogUtil.categorys){
            String categoryKey = RedisKeyUntil.getCategoryKey(category);
            String num = jedisService.get(categoryKey);
            if (num!=null)
                categoryCount.set(JblogUtil.categoryMap.get(category),num);
            else
                categoryCount.set(JblogUtil.categoryMap.get(category),0);
        }
        model.addAttribute("categoryCount",categoryCount);

        ViewObject clickCount = new ViewObject();
        String countStr1 = jedisService.get(RedisKeyUntil.getClickCountKey("/"));
        String countStr2 = jedisService.get(RedisKeyUntil.getClickCountKey("/index"));
        String countStr3 = jedisService.get(RedisKeyUntil.getClickCountKey("/page/1"));
        String currentPage = String.valueOf(Integer.parseInt(countStr1==null?"0":countStr1)
                + Integer.parseInt(countStr2==null?"0":countStr2)+ Integer.parseInt(countStr3==null?"0":countStr3));
        String sumPage = jedisService.get(RedisKeyUntil.getClickCountKey("SUM"));
        clickCount.set("currentPage",currentPage);
        clickCount.set("sumPage",sumPage);
        model.addAttribute("clickCount",clickCount);

        List<Article> hotArticles = new ArrayList<>();
        Set<String> set = jedisService.zrevrange("hotArticles",0,6);
        for (String str : set){
            int articleId = Integer.parseInt(str.split(":")[1]);
            Article article = articleService.getArticleById(articleId);
            hotArticles.add(article);
        }
        model.addAttribute("hotArticles",hotArticles);

        return "index";
    }

    @RequestMapping("/in")
    public String in(Model model,@RequestParam(value = "next",required = false)String next){
        model.addAttribute("next",next);
        return "login";
    }
    **/
    @ApiOperation(value="注册", notes="没有")
    @RequestMapping("/register")
    public String register(Model model, HttpServletResponse httpResponse,
                           @RequestParam String username, @RequestParam String password
            ,@RequestParam(value = "next",required = false)String next){
        Map<String,String> map = userService.register(username,password);
        if (map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",map.get("ticket"));
            cookie.setPath("/");
            httpResponse.addCookie(cookie);

            if (StringUtils.isNotBlank(next))
                return "redirect:"+next;
            else
                return "redirect:/";
        }else {
            model.addAttribute("msg",map.get("msg"));
            return "login";
        }
    }

    @RequestMapping("/login")
    public String login(Model model, HttpServletResponse httpResponse,
                        @RequestParam String username,@RequestParam String password,@RequestParam(value = "next",required = false)String next){
        Map<String,String> map = userService.login(username,password);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket",map.get("ticket"));
            cookie.setPath("/");
            httpResponse.addCookie(cookie);

            if (StringUtils.isNotBlank(next)){
                return "redirect:"+next;
            }

            return "redirect:/";
        }else {
            model.addAttribute("msg", map.get("msg"));
            return "login";
        }
    }

    @RequestMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }
	@RequestMapping(value = "/showUser", method = RequestMethod.POST)
	@ResponseBody()
    public Object test(int id){
    	User user= new User();
       user= userService.seletById(id);
        return user;
    }
/**
    @RequestMapping("/create")
    public String create(Model model){
        User user = hostHolder.getUser();
        if (user==null||"admin".equals(user.getRole())){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
        }
        return "create";
    }
**/


}