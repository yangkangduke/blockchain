package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hewei
 * @date 2023/4/21
 */

@ApiModel(value = "sys_price_property")
@Data
@TableName("sys_price_property")
public class PricePropertyEntity {
    private Long id;
    private BigDecimal gasFee;
    /**
     * @see com.seeds.game.enums.NFTEnumConstant.NFTEventType
     */
    private Integer type;
}
