package com.design.onlineorder.utils;

import com.design.onlineorder.exception.MyException;
import org.apache.poi.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

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

    /**
     * 文件下载
     */
    public static void downloadFile(String remotePath, OutputStream outputStream) {
        try (InputStream inputStream = new FileInputStream(remotePath)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
