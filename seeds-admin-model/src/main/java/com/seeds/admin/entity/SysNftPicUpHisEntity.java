package com.seeds.admin.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_nft_pic_up_his")
public class SysNftPicUpHisEntity extends BaseEntity {

    /**
     * NFT的类型：1:skin , 2:equip
     */
    @TableField("nft_type")
    private Integer nftType;

    /**
     * 上传NFT图片时的备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 未来NFT 发行的平台， 1,Magic Eden  2,Seeds
     */
    @TableField("platform")
    private Integer platform;
}
