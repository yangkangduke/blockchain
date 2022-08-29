package com.seeds.uc.enums.handler;

import com.seeds.uc.enums.ClientTypeEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/2
 */
public class ClientTypeEnumHandler implements TypeHandler<ClientTypeEnum> {

    @Override
    public void setParameter(PreparedStatement ps, int i, ClientTypeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setShort(i, parameter.getCode());
    }

    @Override
    public ClientTypeEnum getResult(ResultSet rs, String columnName) throws SQLException {
        return ClientTypeEnum.from(rs.getShort(columnName));
    }

    @Override
    public ClientTypeEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        return ClientTypeEnum.from(rs.getShort(columnIndex));
    }

    @Override
    public ClientTypeEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ClientTypeEnum.from(cs.getShort(columnIndex));
    }
}
