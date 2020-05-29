package com.changgou.file.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.file.util.FastDFSClient;
import com.changgou.file.util.FastDFSFile;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by wq120 on 3/11/2020.
 */
@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * upload file
     * @param file
     * @return
     */
    @PostMapping("/upload")
    Result upload(@RequestParam("file") MultipartFile file) {
        try {
            //get file name
            String originalFilename = file.getOriginalFilename();

            if (StringUtils.isEmpty(originalFilename)) {
                throw new RuntimeException("The file does not exist");
            }

            //get extension
            String ext = originalFilename.substring(originalFilename.lastIndexOf('.'));

            //get content of file
            byte[] content = file.getBytes();

            //create file object
            FastDFSFile fastDFSFile = new FastDFSFile(originalFilename, content, ext);

            //upload the file
            String[] result = FastDFSClient.upload(fastDFSFile);

            String url = FastDFSClient.getTrackerUrl() + result[0] + "/" + result[1];

            return new Result(true, StatusCode.OK, "Upload file sucess", url);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Result(false, StatusCode.ERROR, "Upload file error");
    }
}
