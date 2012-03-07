package mbg.test.mb3.common;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class StringBooleanTypeHandler implements TypeHandler<Boolean> {

    public Boolean getResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String s = cs.getString(columnIndex);
        return "Y".equals(s);
    }

    public Boolean getResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return "Y".equals(s);
    }

    public void setParameter(PreparedStatement ps, int columnIndex, Boolean parameter,
            JdbcType jdbcType) throws SQLException {
        String s = parameter == null ? "N" : parameter.booleanValue() ? "Y" : "N";
        ps.setString(columnIndex, s);
    }

    public Boolean getResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return "Y".equals(s);
    }
}
