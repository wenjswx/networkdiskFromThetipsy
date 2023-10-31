package com.example.controller;

import com.example.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;


@Controller
@RequestMapping("")
public class FolderController {

    @Autowired
    User user;

    /**
     * 当用户进入某个文件夹时会调用该方法
     * @param map
     * @return
     */
    @PostMapping("/getFolder")
    @ResponseBody
    public String[] getFolderFile(@RequestBody HashMap<String, String> map) {
        String path = map.get("path");
        String folderName = map.get("folderName");
        System.out.println("接收到请求进入文件夹:" + user.getPanPath() + "\\" + path + "\\"+ folderName);
        File file= new File(user.getPanPath() + "\\" + path + "\\"+ folderName);
        //记录用户当前所在位置
        user.setNowPath(path + "\\" + folderName);
        return file.list();
    }

    /**
     * 新建文件夹
     * @param map
     * @return
     */
    @PostMapping("/createFolder")
    @ResponseBody
    public String createFolder(@RequestBody HashMap<String, String> map) {
        //在哪个文件夹下
        String path = map.get("path");
        //创建一个什么名字的文件夹
        String folderName = map.get("folderName");
        System.out.println(user.getPanPath()+ "\\" + path);
        File file = new File(user.getPanPath()+ "\\" + path + "\\" + folderName);
        System.out.println( "在"+file.getName()+"下创建:"+file.mkdir());
        return " ";
    }

    /**
     *删除文件夹
     */
    @PostMapping("/deleteFile")
    @ResponseBody
    public String deleteFile(@RequestBody HashMap<String, String> map){
        String path = map.get("path");
        String folderName = map.get("folderName");
        System.out.println("开始删除文件:" + user.getPanPath() + "\\" + path+ "\\" + folderName);
        File file= new File(user.getPanPath() + "\\" + path+ "\\" + folderName);
        if (file.delete()) {
            return "删除成功";

        }
        return "删除失败";
    }
}
