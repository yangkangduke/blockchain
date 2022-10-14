package com.seeds.account.mapper;

import com.seeds.account.model.AddressCollectOrderHis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 处理地址归集
 *
 * @author milo
 *
 */
@Mapper
public interface AddressCollectOrderHisMapper {
    /**
     * 删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);
    /**
     * 插入新的
     *
     * @param record
     * @return
     */
    int insert(AddressCollectOrderHis record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(AddressCollectOrderHis record);

    /**
     *
     * @param id
     * @return
     */
    AddressCollectOrderHis selectByPrimaryKey(Long id);

    /**
     * 获取所有
     * @return
     */
    List<AddressCollectOrderHis> selectAll();


    /**
     * 获取所有
     * @return
     */
    List<AddressCollectOrderHis> getList(@Param("chain") int chain, @Param("startTime") long startTime, @Param("endTime") long endTime,
                                         @Param("type") int type, @Param("address") String address, @Param("currency") String currency);

    /**
     *
     * @param status
     * @return
     */
    List<AddressCollectOrderHis> getByStatus(@Param("status") int status);
}