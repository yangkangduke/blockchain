package com.seeds.uc.enums.handler;

import com.seeds.uc.enums.ClientPlatformEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/26
 */
public class ClientPlatformEnumHandler implements TypeHandler<ClientPlatformEnum> {

    @Override
    public void setParameter(PreparedStatement ps, int i, ClientPlatformEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setShort(i, parameter.getCode());
    }

    @Override
    public ClientPlatformEnum getResult(ResultSet rs, String columnName) throws SQLException {
        return ClientPlatformEnum.from(rs.getShort(columnName));
    }

    @Override
    public ClientPlatformEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        return ClientPlatformEnum.from(rs.getShort(columnIndex));
    }

    @Override
    public ClientPlatformEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ClientPlatformEnum.from(cs.getShort(columnIndex));
    }
}
