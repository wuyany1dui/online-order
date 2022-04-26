package com.design.onlineorder.utils;

import com.design.onlineorder.exception.MyException;
import org.apache.poi.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Created by DrEAmSs on 2022-04-26 11:36
 */
public class FileUtils {

    /**
     * 上传文件
     */
    public static String uploadFile(MultipartFile file, String filePath, String fileName) {
        FileOutputStream fileOutputStream = null;
        InputStream is = null;
        File target;
        try {
            is = file.getInputStream();
            target = new File(filePath + File.separator + fileName);
            if (!target.exists()) {
                boolean create = target.createNewFile();
                if (!create) {
                    throw new MyException(500, "创建文件失败");
                }
            }
            fileOutputStream = new FileOutputStream(filePath + File.separator + fileName);
            IOUtils.copy(is, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return target.getAbsolutePath();
    }
}
