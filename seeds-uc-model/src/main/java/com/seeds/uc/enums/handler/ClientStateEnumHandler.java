package com.seeds.uc.enums.handler;

import com.seeds.uc.enums.ClientStateEnum;
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
public class ClientStateEnumHandler implements TypeHandler<ClientStateEnum> {

    @Override
    public void setParameter(PreparedStatement ps, int i, ClientStateEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setShort(i, parameter.getCode());
    }

    @Override
    public ClientStateEnum getResult(ResultSet rs, String columnName) throws SQLException {
        return ClientStateEnum.from(rs.getShort(columnName));
    }

    @Override
    public ClientStateEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        return ClientStateEnum.from(rs.getShort(columnIndex));
    }

    @Override
    public ClientStateEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ClientStateEnum.from(cs.getShort(columnIndex));
    }
}
