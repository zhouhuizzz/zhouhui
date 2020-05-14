package edu.cn.controller;

import edu.cn.pojo.User;
import edu.cn.redis.RedisService;
import edu.cn.redis.UserKey;
import edu.cn.result.Result;
import edu.cn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static javafx.scene.input.KeyCode.T;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","zh");
        return "hello";
    }

    @RequestMapping("/")
    @ResponseBody
    public String home(){

        return "Hello World!";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet(){
        return Result.success(userService.getByID(1));
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet(){

        User user = redisService.get(UserKey.getById, ""+1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){

        User user = new User();
        user.setId(1);
        user.setName("1111");
        redisService.set(UserKey.getById, ""+1, user);
        return Result.success(true);
    }


}
