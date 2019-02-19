package hkd.luxc.dao;

import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

/**
 * 插件签名
 *    告诉MyBatis当前插件用来拦截哪个对象的哪个方法
 */
@Intercepts({
	@Signature(type=StatementHandler.class,method="parameterize",args=java.sql.Statement.class)
	})
public class MyFirstPlugin implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		//System.out.println("MyFirstPlugin...intercept():"+invocation.getMethod());
		
		//动态的改变一下sql运行的参数：以前1号员工，实际从数据库查询3号员工
		Object target = invocation.getTarget();//当前拦截到的对象
		//拿到：StatementHandler==>ParameterHandler===>parameterObject
		//拿到target的元数据
		MetaObject metaObject = SystemMetaObject.forObject(target);
		//修改完sql语句要用的参数
		metaObject.setValue("parameterHandler.parameterObject", 3);  //设置sql语句用的参数
		
		//执行目标方法  放行
		Object result=invocation.proceed();
		
		//返回执行后的结果
		return result;
	}

	/**
	 * 包装目标对象:为目标对象创建一个代理对象
	 */
	@Override
	public Object plugin(Object target) {
		
		//System.out.println("MyFirstPlugin...plugin():(Mybatis要包装的对象)"+target);
		
		//我们可以借助Plugin类的wrap方法来使用当前Interceptor包装我们目标对象
		Object wrap=Plugin.wrap(target, this);
		
		//返回为当前target创建的动态代理
		return wrap;
	}

	/**
	 * 将插件注册时的property属性设置进来
	 */
	@Override
	public void setProperties(Properties properties) {
		//System.out.println("插件配置的信息:"+properties);
	}

}
