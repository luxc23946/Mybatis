package hkd.luxc.dao;

import java.util.List;

import hkd.luxc.entities.Employee;

public interface EmployeeMapperPlus {
	
	/**
	 * 测试简单查询
	 * @param id
	 * @return
	 */
	public Employee getEmpById(Integer id);
	
	
	/**
	 * 测试 级联查询
	 */
	public Employee getEmpAndDeptById(Integer id );
	
	
	/**
	 * 测试 分步查询
	 */
	public Employee getEmpByIdStep(Integer id);
	
	/**
	 * 测试  返回集合
	 */
	public List<Employee>getEmpsByDeptId(Integer id);
	
	
	/**
	 * 测试 discriminator 鉴别器
	 */
	public Employee getEmpDisById(Integer id);

}
