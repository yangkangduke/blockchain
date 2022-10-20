package com.seeds.uc.enums.handler;

import com.seeds.uc.enums.ClientAuthTypeEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
* @author yk
 * @date 2020/8/26
 */
public class ClientAuthTypeHandler implements TypeHandler<ClientAuthTypeEnum> {
    @Override
    public void setParameter(PreparedStatement ps, int i, ClientAuthTypeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setShort(i, parameter.getCode());
    }

    @Override
    public ClientAuthTypeEnum getResult(ResultSet rs, String columnName) throws SQLException {
        return ClientAuthTypeEnum.from(rs.getShort(columnName));
    }

    @Override
    public ClientAuthTypeEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        return ClientAuthTypeEnum.from(rs.getShort(columnIndex));
    }

    @Override
    public ClientAuthTypeEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ClientAuthTypeEnum.from(cs.getShort(columnIndex));
    }
}
