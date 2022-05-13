package com.design.onlineorder.controller;

import ch.qos.logback.core.util.FileUtil;
import com.design.onlineorder.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Created by DrEAmSs on 2022-05-13 10:21
 */
@Api(tags = "文件")
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${onlineOrder.filePath}")
    private String filePath;

    @ApiOperation("文件下载")
    @GetMapping("/download/{fileName}")
    public void download(HttpServletResponse response, @PathVariable String fileName) {
        try {
            String contentType = "application/octet-stream";
            if (fileName.toLowerCase().endsWith("jpg")) {
                contentType = "image/jpeg";
            } else if (fileName.toLowerCase().endsWith("png")) {
                contentType = "image/png";
            }
            response.setContentType(contentType);
            response.addHeader("Content-Disposition", "inline;fileName=" +
                    URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()));
            FileUtils.downloadFile(filePath + File.separator + fileName, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
