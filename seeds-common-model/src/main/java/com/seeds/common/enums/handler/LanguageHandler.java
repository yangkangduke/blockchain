package com.seeds.common.enums.handler;

import com.seeds.common.enums.Language;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/9/3
 */
public class LanguageHandler implements TypeHandler<Language> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Language parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public Language getResult(ResultSet rs, String columnName) throws SQLException {
        return Language.from(rs.getString(columnName));
    }

    @Override
    public Language getResult(ResultSet rs, int columnIndex) throws SQLException {
        return Language.from(rs.getString(columnIndex));
    }

    @Override
    public Language getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Language.from(cs.getString(columnIndex));
    }
}