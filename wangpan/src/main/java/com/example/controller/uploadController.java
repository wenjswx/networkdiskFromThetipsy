package com.example.controller;

import ch.qos.logback.core.util.FileUtil;
import com.example.dao.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class uploadController {
    @Autowired
    User user;

    private final static String utf8 = "utf-8";

    @PostMapping("/upload")
//    @ResponseBody
    public void upload( HttpServletRequest request, HttpServletResponse response) throws Exception {
//        System.out.println("request:" + request);
        //设置响应编码
        response.setCharacterEncoding(utf8);
        //切片编号
        Integer schunk = null;
        //总切片数
        Integer schunks = null;
        //文件名称
        String name = null;
        //获取当前用户所在的地址
        String uploadPath = user.getPanPath()+"\\"+user.getNowPath();
        System.out.println("接收到了请求文件上传到:" + uploadPath);
        //创建输出流
        BufferedOutputStream os = null;
        try {
            //创建默认FileItem类工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //设置缓冲区(缓冲取作用:好比用桶子打相同的水，用大桶子(大于1024)比小桶子(默认1024)打的次数少从而提升效率，但小筒子装水倒水的速度比大桶子快，所以缓冲区不是越大越好)
            factory.setSizeThreshold(1024);
            //设置文件IO路径
            factory.setRepository(new File(uploadPath));
            //根据设置好的工厂创建一个upload(ServletFileUpload类:用于处理每个HTML小部件的多个文件)
            ServletFileUpload upload = new ServletFileUpload(factory);
            //单文件最大值5G
//            upload.setFileSizeMax(50 * 1024 * 1024 * 1024);
            //总文件大小10G
//            upload.setSizeMax(100 * 1024 * 1024 * 1024);
            //从request里取到的文件切片
            List<FileItem> items = upload.parseRequest(request);
//            System.out.println("List:" + items);

            /* 此时FileItem类有以下成员属性
             *  name 文件名只有切片才有
             *  StoreLocation 本地地址
             *  size 大小
             *  isFormField 是否为简单字段
             *  FieldName 文件类型
             *
             *  举例: 循环打印遍历的 items
             *  当前文件：name=null, StoreLocation=D:\pan/upload_3ec36c20_e9a9_44a3_9886_25f75b69e8c2_00000000.tmp, size=9 bytes, isFormField=true, FieldName=id
             *  当前文件：name=null, StoreLocation=D:\pan/upload_3ec36c20_e9a9_44a3_9886_25f75b69e8c2_00000001.tmp, size=16 bytes, isFormField=true, FieldName=name
             *  当前文件：name=null, StoreLocation=D:\pan/upload_3ec36c20_e9a9_44a3_9886_25f75b69e8c2_00000002.tmp, size=71 bytes, isFormField=true, FieldName=type
             *  当前文件：name=null, StoreLocation=D:\pan/upload_3ec36c20_e9a9_44a3_9886_25f75b69e8c2_00000003.tmp, size=54 bytes, isFormField=true, FieldName=lastModifiedDate
             *  当前文件：name=null, StoreLocation=D:\pan/upload_3ec36c20_e9a9_44a3_9886_25f75b69e8c2_00000004.tmp, size=6 bytes, isFormField=true, FieldName=size
             *  当前文件：name=xxx.docx, StoreLocation=D:\pan/upload_3ec36c20_e9a9_44a3_9886_25f75b69e8c2_00000005.tmp, size=465475 bytes, isFormField=false, FieldName=upload
             *
             *  isFormField=true 的对象为简单字段文件，里边包含接受文件的基础信息，FieldName=name 说明该文件里面包含的基础信息为文件名
             *
             * */
            for (FileItem item : items) {
                //判断取到的item是否为简单字段
                if (item.isFormField()) {
                    //判断当前的切片FieldName是否为chunk即当前切片编号
                    if ("chunk".equals(item.getFieldName())) {
                        schunk = Integer.parseInt(item.getString(utf8));
                    } else
                        //判断当前的切片FieldName是否为chunks即最大切片数
                        if ("chunks".equals(item.getFieldName())) {
                            schunks = Integer.parseInt(item.getString(utf8));
                        } else
                            //判断当前的切片FieldName是否为name
                            if ("name".equals(item.getFieldName())) {
                                name = item.getString(utf8);
                            }
                }
            }


            //遍历取到的文件(FileItem类表示在multipart/from-data POST请求中接收到的文件或表单项)
            for (FileItem item : items) {
//                System.out.println("当前文件："+item);
                //判断文件是否非简单字段
                if (!item.isFormField()) {
                    //如果文件没有名字设置文件名为本身
                    String itemFileName = name;
                    //判断是否有文件名
                    if (name != null) {
                        //有文件名
                        System.out.println("收到文件:" + name);
                        if (schunk != null) {
                            //有分片设置分片名方便后续合并
                            itemFileName = schunk + "_" + name;
                            System.out.println("收到分片:" + itemFileName);
                        }
                        //从本地获取该文件
                        File temFile = new File(uploadPath, itemFileName);
                        //判断该文件是否存在
                        if (!temFile.exists()) {
                            //如果存在就继续断点续传
                            item.write(temFile);
                        }
                        System.out.println("该文件不存在:" + itemFileName);
                    }
                }
            }


            //判断是否为最后一个分片，如果是就合并
            if (schunk != null && schunk.intValue() == schunks.intValue() - 1) {
                //创建一个文件作为最终总和的文件
                File tempFile = new File(uploadPath, name);
                //创建一个向tempFile输出的流
                os = new BufferedOutputStream(new FileOutputStream(tempFile));
                //开始合并，寻找文件分片，因为知道总分片数目为schunks,小于schunks的数字就为分片id
                for (int i = 0; i < schunks; i++) {
                    //从uploadPath地址创建当前名叫 i_name 的文件对象
                    File file = new File(uploadPath, i + "_" + name);
                    //判断该文件是否存在，因为多线程接受文件可能最后的文件接受到了，当前文件没接受到
                    while (!file.exists()) {
                        //让该线程睡眠100毫秒，目的是等待其他线程将该文件写入本地
                        Thread.sleep(100);
                        //再次创建该文件的File对象，然后再返回while判断该文件是否存在
                        file = new File(uploadPath, i + "_" + name);
                    }
                    //将文件读取到一个字节数组中
                    byte[] bytes = FileUtils.readFileToByteArray(file);
                    //将字节数组写入最终总和的文件
                    os.write(bytes);
                    //刷新流
                    os.flush();
                    //删掉生成的临时tmp文件
                    file.delete();
                }
                os.flush();
            }
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
//        return "hello world";
        System.out.println("接受完成" + name);
    }


}
