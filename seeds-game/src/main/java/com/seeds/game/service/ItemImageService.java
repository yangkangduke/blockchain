package com.seeds.game.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.dto.response.GameItemImageResp;
import com.seeds.game.entity.ItemImage;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author hewei
 * @since 2023-03--6
 */
public interface ItemImageService extends IService<ItemImage> {

    String queryImgByItemId(Long itemId);

    List<GameItemImageResp>  treeList();

}
