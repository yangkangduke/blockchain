package com.seeds.admin.service;

import com.seeds.admin.dto.response.GameFileResp;
import com.seeds.common.dto.GenericDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: hewei
 * @date 2022/12/17
 */
public interface GameFileService {
    GameFileResp upload(MultipartFile file);
}
