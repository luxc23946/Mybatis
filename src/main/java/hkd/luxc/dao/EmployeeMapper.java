package hkd.luxc.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import hkd.luxc.entities.Employee;
import hkd.luxc.entities.OraclePage;

public interface EmployeeMapper {

	/**
	 * 也可以通过 @Select 注解 替代 Mapper映射文件
	 */
	//@Select(value="select id,last_name lastName,email,gender from tbl_employee where id = #{id}")
	public Employee getEmpById(Integer id);
	
	public Employee getEmpByIdAndLastName(@Param("id")Integer id,@Param("lastName")String lastName);
	
	public Employee getEmpByMap(Map<String, Object> map);
	
	public List<Employee> getEmpsByLastNameLike(String last_name);
	
	/**
	 * 返回一条记录的Map, key就是列名, value就是对应值
	 */
	public Map<String, Object>getEmpByIdReturnMap(Integer id);
	
	/**
	 * 多条记录封装一个map：Map<Integer,Employee>:键是这条记录的主键，值是记录封装后的javaBean
	 * @MapKey:告诉mybatis封装这个map的时候使用哪个属性作为map的key
	 */
	@MapKey("id")
	public Map<Integer, Employee> getEmpByLastNameLikeReturnMap(String lastName);
	
	public Integer addEmp(Employee employee);
	
	public Integer updateEmp(Employee employee);
	
	public Integer deleteEmp(Integer id);
	
	/**
	 * 通过pagehelper实现分页
	 */
	public List<Employee>getEmps();
	
	/**
	 * 调用Oracle的存储过程实现分页查找
	 */
	public void getPageByProcedure(OraclePage page);
	
}
