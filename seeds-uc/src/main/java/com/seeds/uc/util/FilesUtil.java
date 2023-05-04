package com.seeds.uc.util;

import com.seeds.common.exception.SeedsException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

/**
 * @author hang.yu
 * @date 2023/5/4
 */
public class FilesUtil {

    /**
     * 文件后缀 支持的类型
     */
    private static final String[] FILE_SUFFIX_SUPPORT = {
            ".jpg", ".jpeg", ".png"};

    /**
     * 文件大小 2MB
     */
    private static final long FILE_SIZE = 5;

    /**
     * 文件大小 2MB
     */
    private static final long SIZE_U = 1024;

    /**
     * 上传文件校验大小、名字、后缀
     * @param multipartFile multipartFile
     */
    public static void uploadVerify(MultipartFile multipartFile) {
        // 校验文件是否为空
        if (multipartFile == null) {
            throw new SeedsException("File cannot be empty");
        }

        // 校验文件大小
        long size = multipartFile.getSize();
        if(size > FILE_SIZE * SIZE_U * SIZE_U){
            throw new SeedsException("File size cannot exceed " + FILE_SIZE + "M");
        }

        // 校验文件名字
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new SeedsException("File name cannot be empty");
        }

        // 校验文件后缀
        if (!originalFilename.contains(".")) {
            throw new SeedsException("File cannot be without a suffix");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        boolean flag = true;
        for (String s : FILE_SUFFIX_SUPPORT) {
            if (s.equals(suffix.toLowerCase(Locale.ROOT))) {
                flag = false;
                break;
            }
        }
        if(flag){
            throw new SeedsException("File format not supported");
        }
    }

}
