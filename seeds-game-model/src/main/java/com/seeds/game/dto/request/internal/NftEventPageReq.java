package com.seeds.game.dto.request.internal;

import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: hewei
 * @date 2022/9/21
 */
@Data
@ApiModel(value = "nftEvent列表请求入参")
public class NftEventPageReq extends PageReq {

    @ApiModelProperty(value = "userId")
    private Long userId;

    @ApiModelProperty("通知类型 1.mint  2. compound 3.other")
    private Integer type;

    @ApiModelProperty("排序条件列表")
    private List<Sort> sorts;

    @ApiModel("排序条件")
    @Data
    public static class Sort {
        @ApiModelProperty("排序字段")
        private String sort;

        @ApiModelProperty("sortType: asc、desc ")
        private String sortType;
    }

    public static String getOrderByStatement(List<Sort> sorts) {
        StringBuilder statement = new StringBuilder();
        if (!CollectionUtils.isEmpty(sorts)) {
            for (Sort sort : sorts) {
                if (StringUtils.isNotBlank(sort.getSort()) && StringUtils.isNotBlank(sort.getSortType())) {
                    statement.append(sort.getSort());
                    statement.append(" ");
                    statement.append(sort.getSortType());
                    statement.append(",");
                }
            }
        }
        return StringUtils.isNotBlank(statement.toString()) ? " order by " + statement.toString().substring(0, statement.toString().length() - 1) : "";
    }
}
