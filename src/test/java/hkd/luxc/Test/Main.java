package hkd.luxc.Test;

import hkd.luxc.bean.EmpStatus;
import hkd.luxc.dao.DepartmentMapper;
import hkd.luxc.dao.EmployeeMapper;
import hkd.luxc.dao.EmployeeMapperAnnotation;
import hkd.luxc.dao.EmployeeMapperDynamicSql;
import hkd.luxc.dao.EmployeeMapperPlus;
import hkd.luxc.entities.Department;
import hkd.luxc.entities.Employee;
import hkd.luxc.entities.OraclePage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * 1、接口式编程
 * 	原生：		Dao		====>  DaoImpl
 * 	mybatis：	Mapper	====>  xxMapper.xml
 * 
 * 2、SqlSession代表和数据库的一次会话；用完必须关闭；
 * 3、SqlSession和connection一样她都是非线程安全。每次使用都应该去获取新的对象。
 * 4、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象。
 * 		（将接口和xml进行绑定）
 * 		EmployeeMapper empMapper =	sqlSession.getMapper(EmployeeMapper.class);
 * 5、两个重要的配置文件：
 * 		mybatis的全局配置文件：包含数据库连接池信息，事务管理器信息等...系统运行环境信息
 * 		sql映射文件：保存了每一个sql语句的映射信息：将sql抽取出来。	
 */
public class Main {
	
	private SqlSessionFactory sqlSessionFactory = null;
	private SqlSession sqlSession = null;
	
	/**
	 * 1、根据xml配置文件（全局配置文件）创建一个SqlSessionFactory对象 有数据源一些运行环境信息
	 * 2、sql映射文件；配置了每一个sql，以及sql的封装规则等。 3、将sql映射文件注册在全局配置文件中 4、写代码：
	 * 1）、根据全局配置文件得到SqlSessionFactory； 2）、使用sqlSession工厂，获取到sqlSession对象使用他来执行增删改查
	 * 一个sqlSession就是代表和数据库的一次会话，用完关闭
	 * 3）、使用sql的唯一标志来告诉MyBatis执行哪个sql。sql都是保存在sql映射文件中的。
	 */
	@Before
	public void init() throws IOException {
		InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		/**
		 * 获取的session不会自动提交
		 *    sqlSessionFactory.openSession()===》手动提交
		 *    sqlSessionFactory.openSession(true)===》自动提交
		 */
		sqlSession = sqlSessionFactory.openSession();
	}

	@After
	public void destory() {
		sqlSession.commit();
		sqlSession.close();
	}

	/**
	 * 获取sqlSession实例，能直接执行已经映射的sql语句
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSqlSession() throws IOException {
		System.out.println(sqlSession);
	}

	/**
	 * 通过xml映射文件直接查询
	 */
	@Test
	public void testXml() {
		/**
		 * 参数1: 映射文件的  namespace.id 唯一确定要执行的SQL 参数2: SQL参数
		 */
		Employee employee = sqlSession.selectOne("hkd.luxc.dao.EmployeeMapper.getEmpById", 1);
		System.out.println(employee);
	}

	/**
	 * 通过 XML映射 接口查询
	 */
	@Test
	public void testInterfaceXml() {
	 //获取接口的实现类对象
	 //Mybatis 会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
	 EmployeeMapper employeeMapper= sqlSession.getMapper(EmployeeMapper.class);
	 Employee employee=employeeMapper.getEmpById(1);
	 System.out.println(employee);
	}
	
	
	/**
	 * 通过 接口+注解 查询
	 */
	@Test
	public void testInterfaceAnnotation() {
	 EmployeeMapperAnnotation employeeMapperAnnotation= sqlSession.getMapper(EmployeeMapperAnnotation.class);
	 Employee employee=employeeMapperAnnotation.getEmpById(1);
	 System.out.println(employee);
	}
	
	/**
	 * 测试增删改
	 * 1、mybatis允许增删改直接定义以下类型返回值
	 * 		Integer、Long、Boolean、void
	 * 2、我们需要手动提交数据
	 * 		sqlSessionFactory.openSession();===》手动提交
	 * 		sqlSessionFactory.openSession(true);===》自动提交
	 */
	@Test
	public void teestInsert() {
		Employee employee=new Employee(null,"Bob","bb@bb.com",'0',null);
		employee.setEmpStatus(EmpStatus.LOGOUT);
		EmployeeMapper employeeMapper=sqlSession.getMapper(EmployeeMapper.class);
		employeeMapper.addEmp(employee);
		System.out.println(employee);
	}
	
	@Test
	public void teestUpdate() {
		Employee employee=new Employee();
		employee.setId(7);
		employee.setEmail("cc@c.com");
		employee.setGender('1');
		employee.setLastName("Alice");
		EmployeeMapper employeeMapper=sqlSession.getMapper(EmployeeMapper.class);
		employeeMapper.updateEmp(employee);
	}
	
	@Test
	public void teestDelete() {
		EmployeeMapper employeeMapper=sqlSession.getMapper(EmployeeMapper.class);
		Integer result=employeeMapper.deleteEmp(6);
		System.out.println(result);
	}
	
	@Test
	public void testGetEmpByIdAndLastName() {
		EmployeeMapper employeeMapper= sqlSession.getMapper(EmployeeMapper.class);
		Employee employee=employeeMapper.getEmpByIdAndLastName(1, "tom");
		System.out.println(employee);
	}
	
	@Test
	public void testGetEmpByMap() {
		EmployeeMapper employeeMapper= sqlSession.getMapper(EmployeeMapper.class);
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("id", 1);
		map.put("lastName", "tom");
		map.put("gender", "0");
		Employee employee=employeeMapper.getEmpByMap(map);
		System.out.println(employee);
	}
	
	@Test
	public void testGetEmpsByLastNameLike() {
		EmployeeMapper employeeMapper= sqlSession.getMapper(EmployeeMapper.class);
		List<Employee>emps=employeeMapper.getEmpsByLastNameLike("%o%");
		System.out.println(emps);
	}
	
	@Test
	public void testGetEmpByIdReturnMap() {
		EmployeeMapper employeeMapper= sqlSession.getMapper(EmployeeMapper.class);
		Map<String, Object>map=employeeMapper.getEmpByIdReturnMap(1);
		System.out.println(map);
	}
	
	@Test
	public void testGetEmpByLastNameLikeReturnMap() {
		EmployeeMapper employeeMapper= sqlSession.getMapper(EmployeeMapper.class);
		Map<Integer, Employee>map=employeeMapper.getEmpByLastNameLikeReturnMap("%m%");
		System.out.println(map);
	}
	
	
	@Test
	public void testGetEmpByIdResultMap() {
		EmployeeMapperPlus employeeMapperPlus=sqlSession.getMapper(EmployeeMapperPlus.class);
		Employee employee=employeeMapperPlus.getEmpById(1);
		System.out.println(employee);
	}
	
	@Test
	public void testGetEmpAndDeptById() {
		EmployeeMapperPlus employeeMapperPlus=sqlSession.getMapper(EmployeeMapperPlus.class);
		Employee employee=employeeMapperPlus.getEmpAndDeptById(1);
		System.out.println(employee);
	}
	
	
	@Test
	public void testGetDeptById() {
		DepartmentMapper departmentMapper=sqlSession.getMapper(DepartmentMapper.class);
		Department department=departmentMapper.getDeptById(1);
		System.out.println(department);
	}
	
	@Test
	public void testGetEmpByIdStep() {
		EmployeeMapperPlus employeeMapperPlus=sqlSession.getMapper(EmployeeMapperPlus.class);
		Employee employee=employeeMapperPlus.getEmpByIdStep(1);
		System.out.println(employee);
	}
	
	@Test
	public void testGetDeptAndEmpsById() {
		DepartmentMapper departmentMapper=sqlSession.getMapper(DepartmentMapper.class);
		Department department=departmentMapper.getDeptAndEmpsById(1);
		System.out.println(department);
	}
	
	@Test
	public void testGetEmpsByDeptId() {
		EmployeeMapperPlus employeeMapperPlus=sqlSession.getMapper(EmployeeMapperPlus.class);
		List<Employee> emps=employeeMapperPlus.getEmpsByDeptId(1);
		System.out.println(emps);
	}
	
	@Test
	public void testGetDeptByIdStep() {
		DepartmentMapper departmentMapper=sqlSession.getMapper(DepartmentMapper.class);
		Department department=departmentMapper.getDeptAndEmpsByIdStep(1);
		System.out.println(department.getEmps());
	}
	
	@Test
	public void testGetEmpDisById() {
		EmployeeMapperPlus employeeMapperPlus=sqlSession.getMapper(EmployeeMapperPlus.class);
		Employee employee=employeeMapperPlus.getEmpDisById(2);
		System.out.println(employee);
	}
	
	@Test
	public void testGetEmpsByConditionIf() {
		Employee arg=new Employee("a", "a", '0');
		EmployeeMapperDynamicSql dynamicSql=sqlSession.getMapper(EmployeeMapperDynamicSql.class);
		List<Employee> emps=dynamicSql.getEmpsByConditionIf(arg);
		System.out.println(emps);
	}
	
	
	@Test
	public void testUpdateEmp() {
		Employee arg=new Employee();
		arg.setLastName("%o%");
		EmployeeMapperDynamicSql dynamicSql=sqlSession.getMapper(EmployeeMapperDynamicSql.class);
		List<Employee> emps=dynamicSql.getEmpsByConditionChoose(arg);
		System.out.println(emps);
	}
	
	 
	@Test
	public void testGetEmpsByConditionForeach() {
		List<Integer>list=Arrays.asList(1,2,3,4);
		EmployeeMapperDynamicSql dynamicSql=sqlSession.getMapper(EmployeeMapperDynamicSql.class);
		List<Employee> emps=dynamicSql.getEmpsByConditionForeach(list);
		System.out.println(emps);
	}
	
	
	@Test
	public void addEmps() {
		Integer result=0;
		List<Employee> emps = new ArrayList<Employee>();
		emps.add(new Employee(null, "xxx", "xx@xxx.com", '1',new Department(1)));
		emps.add(new Employee(null, "yyy", "yy@yyy.com", '0',new Department(2)));
		emps.add(new Employee(null, "zzz", "zz@zzz.com", '1',new Department(3)));
		EmployeeMapperDynamicSql dynamicSql=sqlSession.getMapper(EmployeeMapperDynamicSql.class);
		result=dynamicSql.addEmps(emps);
		System.out.println(result);
		
	}
	
	
	
	
	@Test
	public void getEmpsTestInnerParameter() {
		
	}
	
	
	
	/**
	 * 两级缓存：
	 * 一级缓存：（本地缓存）：sqlSession级别的缓存。一级缓存是一直开启的；SqlSession级别的一个Map
	 * 		与数据库同一次会话期间查询到的数据会放在本地缓存中。
	 * 		以后如果需要获取相同的数据，直接从缓存中拿，没必要再去查询数据库；
	 * 
	 * 		一级缓存失效情况（没有使用到当前一级缓存的情况，效果就是，还需要再向数据库发出查询）：
	 * 		1、sqlSession不同。
	 * 		2、sqlSession相同，查询条件不同.(当前一级缓存中还没有这个数据)
	 * 		3、sqlSession相同，两次查询之间执行了增删改操作(这次增删改可能对当前数据有影响)
	 * 		4、sqlSession相同，手动清除了一级缓存（缓存清空）
	 * 
	 * 二级缓存：（全局缓存）：基于namespace级别的缓存：一个namespace对应一个二级缓存：
	 * 		工作机制：
	 * 		1、一个会话，查询一条数据，这个数据就会被放在当前会话的一级缓存中；
	 * 		2、如果会话关闭；一级缓存中的数据会被保存到二级缓存中；新的会话查询信息，就可以参照二级缓存中的内容；
	 * 		3、sqlSession===EmployeeMapper==>Employee
	 * 						DepartmentMapper===>Department
	 * 			不同namespace查出的数据会放在自己对应的缓存中（map）
	 * 			效果：数据会从二级缓存中获取
	 * 				查出的数据都会被默认先放在一级缓存中。
	 * 				只有会话提交或者关闭以后，一级缓存中的数据才会转移到二级缓存中
	 * 		使用：
	 * 			1）、开启全局二级缓存配置：<setting name="cacheEnabled" value="true"/>
	 * 			2）、去mapper.xml中配置使用二级缓存：
	 * 				<cache></cache>
	 * 			3）、我们的POJO需要实现序列化接口
	 * 	
	 * 和缓存有关的设置/属性：
	 * 			1）、cacheEnabled=true：false：关闭缓存（二级缓存关闭）(一级缓存一直可用的)
	 * 			2）、每个select标签都有useCache="true"：
	 * 					false：不使用缓存（一级缓存依然使用，二级缓存不使用）
	 * 			3）、【每个增删改标签的：flushCache="true"：（一级二级都会清除）】
	 * 					增删改执行完成后就会清楚缓存；
	 * 					测试：flushCache="true"：一级缓存就清空了；二级也会被清除；
	 * 					查询标签：flushCache="false"：
	 * 						如果flushCache=true;每次查询之后都会清空缓存；缓存是没有被使用的；
	 * 			4）、sqlSession.clearCache();只是清楚当前session的一级缓存；
	 * 			5）、localCacheScope：本地缓存作用域：（一级缓存SESSION）；当前会话的所有数据保存在会话缓存中；
	 * 								STATEMENT：可以禁用一级缓存；		
	 * 				
	 *第三方缓存整合：
	 *		1）、导入第三方缓存包即可；
	 *		2）、导入与第三方缓存整合的适配包；官方有；
	 *		3）、mapper.xml中使用自定义缓存
	 *		<cache type="org.mybatis.caches.ehcache.EhcacheCache"></cache>
	 */
	@Test
	public void testSecondLevelCache(){
		SqlSession openSession = sqlSessionFactory.openSession();
		SqlSession openSession2 = sqlSessionFactory.openSession();
			//1、
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			EmployeeMapper mapper2 = openSession2.getMapper(EmployeeMapper.class);
			
			Employee emp01 = mapper.getEmpById(1);
			System.out.println(emp01);
			openSession.close();
			
			//第二次查询是从二级缓存中拿到的数据，并没有发送新的sql
			//mapper2.addEmp(new Employee(null, "aaa", "nnn", "0"));
			Employee emp02 = mapper2.getEmpById(1);
			System.out.println(emp02);
			openSession2.close();		
	}
	
	
	@Test
	public void testSecondLevelCache02(){
		SqlSession openSession = sqlSessionFactory.openSession();
		SqlSession openSession2 = sqlSessionFactory.openSession();
		//1、
		DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);
		DepartmentMapper mapper2 = openSession2.getMapper(DepartmentMapper.class);
		
		Department deptById = mapper.getDeptById(1);
		System.out.println(deptById);
		openSession.close();
		
		
		
		Department deptById2 = mapper2.getDeptById(1);
		System.out.println(deptById2);
		openSession2.close();
		//第二次查询是从二级缓存中拿到的数据，并没有发送新的sql
			
	}
	
	/**
	 * 1、获取sqlSessionFactory对象:
	 * 		解析文件的每一个信息保存在Configuration中，返回包含Configuration的DefaultSqlSession；
	 * 		注意：【MappedStatement】：代表一个增删改查的详细信息
	 * 
	 * 2、获取sqlSession对象
	 * 		返回一个DefaultSQlSession对象，包含Executor和Configuration;
	 * 		这一步会创建Executor对象；
	 * 
	 * 3、获取接口的代理对象（MapperProxy）
	 * 		getMapper，使用MapperProxyFactory创建一个MapperProxy的代理对象
	 * 		代理对象里面包含了，DefaultSqlSession（Executor）
	 * 4、执行增删改查方法
	 * 
	 * 总结：
	 * 	1、根据配置文件（全局，sql映射）初始化出Configuration对象
	 * 	2、创建一个DefaultSqlSession对象，
	 * 		他里面包含Configuration以及
	 * 		Executor（根据全局配置文件中的defaultExecutorType创建出对应的Executor）
	 *  3、DefaultSqlSession.getMapper（）：拿到Mapper接口对应的MapperProxy；
	 *  4、MapperProxy里面有（DefaultSqlSession）；
	 *  5、执行增删改查方法：
	 *  		1）、调用DefaultSqlSession的增删改查（Executor）；
	 *  		2）、会创建一个StatementHandler对象。
	 *  			（同时也会创建出ParameterHandler和ResultSetHandler）
	 *  		3）、调用StatementHandler预编译参数以及设置参数值;
	 *  			使用ParameterHandler来给sql设置参数
	 *  		4）、调用StatementHandler的增删改查方法；
	 *  		5）、ResultSetHandler封装结果
	 *  注意：
	 *  	四大对象每个创建的时候都有一个interceptorChain.pluginAll(parameterHandler);
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSource() {
		EmployeeMapper employeeMapper= sqlSession.getMapper(EmployeeMapper.class);
		Employee employee=employeeMapper.getEmpById(1);
		System.out.println(employee);
	}
	
	
	/**
	 * 插件原理
	 * 在四大对象创建的时候
	 * 1、每个创建出来的对象不是直接返回的，而是
	 * 		interceptorChain.pluginAll(parameterHandler);
	 * 2、获取到所有的Interceptor（拦截器）（插件需要实现的接口）；
	 * 		调用interceptor.plugin(target);返回target包装后的对象
	 * 3、插件机制，我们可以使用插件为目标对象创建一个代理对象；AOP（面向切面）
	 * 		我们的插件可以为四大对象创建出代理对象；
	 * 		代理对象就可以拦截到四大对象的每一个执行；
	 * 
	 *	public Object pluginAll(Object target) {
	 *	    for (Interceptor interceptor : interceptors) {
	 *	      target = interceptor.plugin(target);
	 *	    }
	 *	    return target;
	 *	  }
	 *	
	 *
	 * 插件编写：
	 * 1、编写Interceptor的实现类
	 * 2、使用@Intercepts注解完成插件签名
	 * 3、将写好的插件注册到全局配置文件中
	 * 
	 * 测试pagehelper
	 */
	@Test
	public void testPlugIn() {
		EmployeeMapper employeeMapper= sqlSession.getMapper(EmployeeMapper.class);
		List<Employee>emps=employeeMapper.getEmps();
		for(Employee emp:emps) {
			System.out.println(emp);
		}
	}
	
	/**
	 * 分页1
	 */
	@Test
	public void testPageHelper() {
		EmployeeMapper employeeMapper= sqlSession.getMapper(EmployeeMapper.class);
		Page<Object>page=PageHelper.startPage(1,5);
		List<Employee>emps=employeeMapper.getEmps();
		for(Employee emp:emps) {
			System.out.println(emp);
		}
		System.out.println("当前页码:"+page.getPageNum());
		System.out.println("总页码:"+page.getPages());
		System.out.println("每页记录数:"+page.getPageSize());
		System.out.println("总记录数:"+page.getTotal());
	}
	
	
	/**
	 * 分页2
	 */
	@Test
	public void testPageInfo() {
		//获取第1页，10条内容，默认查询总数count
		PageHelper.startPage(1, 10);
		EmployeeMapper employeeMapper= sqlSession.getMapper(EmployeeMapper.class);
		List<Employee> list = employeeMapper.getEmps();
		//用PageInfo对结果进行包装
		PageInfo<Employee>page = new PageInfo<>(list);
		//测试PageInfo全部属性
		//PageInfo包含了非常全面的分页属性
		System.out.println(page.getPageNum());
		System.out.println(page.getPageSize());
		System.out.println(page.getStartRow());
		System.out.println(page.getEndRow());
		System.out.println(page.getTotal());
		System.out.println(page.getPages());
		System.out.println(page.getFirstPage());
		System.out.println(page.getLastPage());
		System.out.println(page.isIsFirstPage());
		System.out.println(page.isIsLastPage());
		System.out.println(page.isHasPreviousPage());
		System.out.println(page.isHasNextPage());
	}
	
	/**
	 * myBatis 批量操作
	 * 1.只需在openSession时传入一个ExecutorType.BATCH常量即可
	 * 2.也可以通过修改全局配置文件的方式 使openSession返回一个可以批量操作的会话  不建议这样做
	 * 通过修改  defaultExecutorType=BATCH  默认值 SIMPLE 
	 * 3.在和Spring整合时可以在IOC容器中创建两个SqlSessionFactory 一个为普通的  一个为可以批量操作的
	 */
	@Test
	public void testBatchInsert() {
		//获取批量操作的sqlSession
		SqlSession	batchSqlSession=sqlSessionFactory.openSession(ExecutorType.BATCH);
		EmployeeMapper employeeMapper=batchSqlSession.getMapper(EmployeeMapper.class);
		long start=System.currentTimeMillis();
		try {
			Employee employee=null;
			for(int i=0;i<10000;i++) {
				employee=new Employee(String.valueOf(i), i+"@"+i+".com",String.valueOf((i % 2)).charAt(0));
				employeeMapper.addEmp(employee);
			}
		}finally {
			batchSqlSession.commit();
			batchSqlSession.close();
		}
		long end=System.currentTimeMillis();
		
		System.out.println(end-start);
		
	}
	
	
	
	/**
	 * oracle分页：
	 * 		借助rownum：行号；子查询；
	 * 存储过程包装分页逻辑
	 * @throws IOException 
	 */
	public void testProcedure() throws IOException{
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
		OraclePage page = new OraclePage(1,5);
		mapper.getPageByProcedure(page);
		
		System.out.println("总记录数："+page.getCount());
		System.out.println("查出的数据："+page.getEmps().size());
		System.out.println("查出的数据："+page.getEmps());
	}
	
	
	/**
	 * 枚举回顾
	 */
	@Test
	public void testEnumUse(){
		EmpStatus login = EmpStatus.LOGIN;
		
		System.out.println("枚举的索引："+login.ordinal());
		System.out.println("枚举的名字："+login.name());
		System.out.println("枚举的状态码："+login.getCode());
		System.out.println("枚举的提示消息："+login.getMsg());
	}
	
	/**
	 * 默认mybatis在处理枚举对象的时候保存的是枚举的名字：EnumTypeHandler
	 * 改变使用：EnumOrdinalTypeHandler：可以在全局配置文件中设置
	 */
	@Test
	public void testEnum() {
		Employee employee=new Employee("Bob","bb@bb.com",'0');
		employee.setEmpStatus(EmpStatus.LOGOUT);
		EmployeeMapper employeeMapper=sqlSession.getMapper(EmployeeMapper.class);
		employeeMapper.addEmp(employee);
	}
	
	
}
