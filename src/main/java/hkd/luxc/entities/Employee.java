package hkd.luxc.entities;

import hkd.luxc.bean.EmpStatus;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 *  @Alias 给某个类起别名
 */
@Alias(value="employee")
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String lastName;
	
	private String email;
	
	private Character gender;
	
	private EmpStatus empStatus=EmpStatus.LOGOUT;
	
	
	private Department department;
	
	
	public Employee() {}
	
	
	
	public Employee(String lastName, String email, Character gender) {
		super();
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
	}

	public Employee(Integer id, String lastName, String email, Character gender,
			Department dept) {
		this.id = id;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
		this.department = dept;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}
	

	public Department getDepartment() {
		return department;
	}


	public void setDepartment(Department department) {
		this.department = department;
	}



	public EmpStatus getEmpStatus() {
		return empStatus;
	}



	public void setEmpStatus(EmpStatus empStatus) {
		this.empStatus = empStatus;
	}



	@Override
	public String toString() {
		return "Employee [id=" + id + ", lastName=" + lastName + ", email="
				+ email + ", gender=" + gender + ", empStatus=" + empStatus
				+ ", department=" + department + "]";
	}

	
	
}
