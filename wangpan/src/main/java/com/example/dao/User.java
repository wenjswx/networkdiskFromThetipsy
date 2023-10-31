package com.example.dao;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class User {

   //用户的储存地址不变 假设是 D:\\pan
   private String  panPath = "D:\\pan";
   //现在所处位置默认从储存地址的根目录开始 因为上传文件的是二进制流 不方便获取formData 就在它进出文件夹的时候记录
   /**
    * <p>设置这个参数的目的仅仅是为了解决二进制流的 FromData 接收不了(个人能力有限)<br>
    * 从而使得前端上传文件时携带的FromData里的参数读取不出来，从而使得后端不知道文件上传到哪<br>
    * 不然完全可以由前端告诉我用户当前所在的路径
    * </p>
    */
   private String nowPath = "";
}
