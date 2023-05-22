package com.seeds.game.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.dto.response.GameItemImageResp;
import com.seeds.game.entity.ItemImage;
import com.seeds.game.enums.NftTypeEnum;
import com.seeds.game.mapper.ItemImageMapper;
import com.seeds.game.service.ItemImageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


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

    @Override
    public List<GameItemImageResp> treeList() {
        List<ItemImage> list = list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        Map<Integer, List<ItemImage>> itemImageMap = list.stream().collect(Collectors.groupingBy(ItemImage::getItemType));
        List<GameItemImageResp> respList = new ArrayList<>();
        for (Integer itemType : itemImageMap.keySet()) {
            GameItemImageResp parent = new GameItemImageResp();
            parent.setType(itemType.toString());
            List<ItemImage> itemImages = itemImageMap.get(itemType);
            if (NftTypeEnum.hero.getCode() == itemType) {
                Map<Long, List<ItemImage>> itemIdsMap = itemImages.stream().collect(Collectors.groupingBy(ItemImage::getItemId));
                for (Long itemId : itemIdsMap.keySet()) {
                    GameItemImageResp one = new GameItemImageResp();
                    one.setType(itemId.toString());
                    List<ItemImage> itemIds = itemIdsMap.get(itemId);
                    for (ItemImage itemImage : itemIds) {
                        GameItemImageResp two = new GameItemImageResp();
                        BeanUtils.copyProperties(itemImage, two);
                        one.setCareerName(itemImage.getCareerName());
                        one.getChildren().add(two);
                    }
                    parent.getChildren().add(one);
                }
            } else {
                for (ItemImage itemImage : itemImages) {
                    GameItemImageResp one = new GameItemImageResp();
                    BeanUtils.copyProperties(itemImage, one);
                    parent.getChildren().add(one);
                }
            }
            respList.add(parent);
        }
        return respList;
    }
}
