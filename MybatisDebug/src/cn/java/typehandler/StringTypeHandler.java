package cn.java.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class StringTypeHandler extends BaseTypeHandler<String> {

	// -------------------参数绑定的时候有用----------------------
	/**
	 * org.apache.ibatis.scripting.defaults.DefaultParameterHandler.setParameters(...) ---> typeHandler.setParameter(....) ---> this.setNonNullParameter(...)
	 */
	 @Override
	  public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
	      throws SQLException {
	    ps.setString(i, parameter);
	  }

	
	  // -------------------获取结果集的时候有用----------------------
	  /**
	   * org.apache.ibatis.executor.resultset.DefaultResultSetHandler.createByConstructorSignature(...) ---> typeHandler.getResult(...) ---> this.getNullableResult(...)
	   */
	  @Override
	  public String getNullableResult(ResultSet rs, String columnName)
	      throws SQLException {
	    return rs.getString(columnName);
	  }

	  @Override
	  public String getNullableResult(ResultSet rs, int columnIndex)
	      throws SQLException {
	    return rs.getString(columnIndex);
	  }

	  @Override
	  public String getNullableResult(CallableStatement cs, int columnIndex)
	      throws SQLException {
	    return cs.getString(columnIndex);
	  }

}
