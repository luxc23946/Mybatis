package hkd.luxc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import hkd.luxc.entities.Employee;

public interface EmployeeMapperDynamicSql {
	
	/**
	 * IF 动态SQL拼装
	 * 携带了哪个字段查询条件就带上这个字段的值
	 */
	public List<Employee> getEmpsByConditionIf(Employee employee);
	
	/**
	 * Trim 动态SQL拼装
	 */
	public List<Employee> getEmpsByConditionTrim(Employee employee);
	
	/**
	 * Choose 动态SQL拼装
	 */
	public List<Employee> getEmpsByConditionChoose(Employee employee);
	
	/**
	 * Set 动态SQL拼装 修改
	 * @param employee
	 */
	public Integer updateEmp(Employee employee);
	
	
	/**
	 * 查询员工id在给定集合中的
	 */
	public List<Employee> getEmpsByConditionForeach(@Param("ids")List<Integer> ids);
	
	/**
	 * 批量添加员工
	 */
	public Integer addEmps(@Param("emps")List<Employee> emps);
	
	/**
	 * 测试内置参数
	 */
	public List<Employee> getEmpsTestInnerParameter(Employee employee);

}
