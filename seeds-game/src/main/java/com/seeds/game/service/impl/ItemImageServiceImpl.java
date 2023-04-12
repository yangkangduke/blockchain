package com.seeds.game.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.ItemImage;
import com.seeds.game.mapper.ItemImageMapper;
import com.seeds.game.service.ItemImageService;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 * @author: hewei
 * @date 2023/4/12
 */
@Service
public class ItemImageServiceImpl extends ServiceImpl<ItemImageMapper, ItemImage> implements ItemImageService {

    @Override
    public String queryImgByItemId(Long itemId) {
        String imgUrl = "";
        ItemImage one = getOne(new LambdaQueryWrapper<ItemImage>().eq(ItemImage::getItemId, itemId));
        if (Objects.nonNull(one)) {
            imgUrl = one.getImgUrl();
        }
        return imgUrl;
    }
}
