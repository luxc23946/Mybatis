package hkd.luxc.typehandler;

import hkd.luxc.bean.EmpStatus;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;


/**
 * 1、实现TypeHandler接口。或者继承BaseTypeHandler
 */
public class EmpStatusTypeHandler implements TypeHandler<EmpStatus> {

	/**
	 * 定义当前数据如何保存到数据库中
	 */
	@Override
	public void setParameter(PreparedStatement ps, int i, EmpStatus parameter,
			JdbcType jdbcType) throws SQLException {
		ps.setInt(i, parameter.getCode());
	} 
    
	/**
	 * 需要根据从数据库中拿到的枚举的状态码返回一个枚举对象
	 */
	@Override
	public EmpStatus getResult(ResultSet rs, String columnName)
			throws SQLException {
		int code = rs.getInt(columnName);
		EmpStatus status = EmpStatus.getEmpStatusByCode(code);
		return status;
	}

	@Override
	public EmpStatus getResult(ResultSet rs, int columnIndex)
			throws SQLException {
		int code = rs.getInt(columnIndex);
		EmpStatus status = EmpStatus.getEmpStatusByCode(code);
		return status;
	}

	@Override
	public EmpStatus getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		int code = cs.getInt(columnIndex);
		System.out.println("从数据库中获取的状态码："+code);
		EmpStatus status = EmpStatus.getEmpStatusByCode(code);
		return status;
	}

}
