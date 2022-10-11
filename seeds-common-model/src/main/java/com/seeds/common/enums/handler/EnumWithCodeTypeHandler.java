package com.seeds.common.enums.handler;

import com.seeds.common.enums.EnumWithCode;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumWithCodeTypeHandler<E extends Enum<E> & EnumWithCode> extends BaseTypeHandler<E> {
    private Class<E> type;

    public EnumWithCodeTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        final int code = rs.getInt(columnName);
        return EnumWithCode.fromCode(type, code);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        final int code = rs.getInt(columnIndex);
        return EnumWithCode.fromCode(type, code);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        final int code = cs.getInt(columnIndex);
        return EnumWithCode.fromCode(type, code);
    }
}
