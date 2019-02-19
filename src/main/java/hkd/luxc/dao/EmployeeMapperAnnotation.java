package hkd.luxc.dao;


import org.apache.ibatis.annotations.Select;

import hkd.luxc.entities.Employee;

public interface EmployeeMapperAnnotation {

	/**
	 * 也可以通过 @Select 注解 替代 Mapper映射文件
	 */
	@Select(value="select id,last_name lastName,email,gender from tbl_employee where id = #{id}")
	public Employee getEmpById(Integer id);
	
}
