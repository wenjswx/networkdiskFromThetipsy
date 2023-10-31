package com.example.controller;

import com.example.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Controller
public class DownloadController {

    @Autowired
    User user;

    private final static String utf8 = "utf-8";

    /**
     * 下载某路径下的某文件
     * @param request
     * @param response
     * @param path 文件路径
     * @param downloadFileName 要下载的文件名
     * @throws Exception
     */
    @GetMapping("/download/{path}/{downloadFileName}")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, @PathVariable String path,@PathVariable String downloadFileName) throws Exception {
        response.setCharacterEncoding(utf8);
        //目标文件的地址
        System.out.println(user.getPanPath()+"\\"+path+"\\"+downloadFileName);
        //新建一个目标文件对象
        File file = new File(user.getPanPath()+"\\"+path+"\\"+downloadFileName);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            //以utf8编码获取文件名(防止中文乱码)
            String fileName = URLEncoder.encode(file.getName(), utf8);
            //获取文件的Type
            String fileType = request.getServletContext().getMimeType(fileName);
            //设置文件的Type
            response.setContentType(fileType);
            //设置一个响应头，无论是否被浏览器解析，都下载
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("application/octet-stream");
            //从response获取输出流
            outputStream = response.getOutputStream();
            //获取目标的文件输入流
            inputStream = new FileInputStream(file);
            //设置一个byte[]从inputStream写入outputStream
            byte[] buffer = new byte[1024];
            //用一个int类型判断inputStream读出的字节数
            int len = 0;
            System.out.println("开始传输文件:" + fileName);
            //将len赋值等于inputStream读取出来的buffer里的数据字节数，并判断其长度是否大于0也就是是否读完，大于0就是没读完
            while ((len = inputStream.read(buffer)) > 0) {
                //将buffer里的数据写入outputStream
                outputStream.write(buffer, 0, len);
            }
            System.out.println("传输完成");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    /**
     * 下载根目录下的文件
     * @param request
     * @param response
     * @param downloadFileName
     * @throws Exception
     * <p>因为当请求的url为 /download//ll.txt
     * 这类的时候上面的方法就没效果了会报 No mapping for GET
     * 因为spring把 两个 // 当成一个 / 解读了
     * </p>
     */
    @GetMapping("/download/{downloadFileName}")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response,@PathVariable String downloadFileName) throws Exception {
        response.setCharacterEncoding(utf8);
        //目标文件的地址
        System.out.println(user.getPanPath()+"\\"+downloadFileName);
        //新建一个目标文件对象
        File file = new File(user.getPanPath()+"\\"+downloadFileName);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            //以utf8编码获取文件名(防止中文乱码)
            String fileName = URLEncoder.encode(file.getName(), utf8);
            //获取文件的Type
            String fileType = request.getServletContext().getMimeType(fileName);
            //设置文件的Type
            response.setContentType(fileType);
            //设置一个响应头，无论是否被浏览器解析，都下载
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("application/octet-stream");
            //从response获取输出流
            outputStream = response.getOutputStream();
            //获取目标的文件输入流
            inputStream = new FileInputStream(file);
            //设置一个byte[]从inputStream写入outputStream
            byte[] buffer = new byte[1024];
            //用一个int类型判断inputStream读出的字节数
            int len = 0;
            System.out.println("开始传输文件:" + fileName);
            //将len赋值等于inputStream读取出来的buffer里的数据字节数，并判断其长度是否大于0也就是是否读完，大于0就是没读完
            while ((len = inputStream.read(buffer)) > 0) {
                //将buffer里的数据写入outputStream
                outputStream.write(buffer, 0, len);
            }
            System.out.println("传输完成");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }
}
