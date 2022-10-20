package com.seeds.uc.enums.handler;

import com.seeds.uc.enums.ClientAppEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
* @author yk
 * @date 2020/7/26
 */
public class ClientAppEnumHandler implements TypeHandler<ClientAppEnum> {

    @Override
    public void setParameter(PreparedStatement ps, int i, ClientAppEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setShort(i, parameter.getCode());
    }

    @Override
    public ClientAppEnum getResult(ResultSet rs, String columnName) throws SQLException {
        return ClientAppEnum.from(rs.getShort(columnName));
    }

    @Override
    public ClientAppEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        return ClientAppEnum.from(rs.getShort(columnIndex));
    }

    @Override
    public ClientAppEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ClientAppEnum.from(cs.getShort(columnIndex));
    }
}
