package hkd.luxc.dao;

import hkd.luxc.entities.Department;

public interface DepartmentMapper {

	/**
	 * 测试简单查询
	 */
	public Department getDeptById(Integer id);
	
	
	/**
	 * 测试 级联查询
	 */
	public Department getDeptAndEmpsById(Integer id);
	
	/**
	 * 测试 级联查询 分布
	 */
	public Department getDeptAndEmpsByIdStep(Integer id);
	
}
