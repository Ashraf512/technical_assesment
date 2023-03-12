package com.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.model.Department;
import com.model.Employee;

@ManagedBean()
public class EmployeeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String gender;
	private String selectedGender;
    private String selectedDepartment;

	private List<Department> DepartmentList;
    private String[] genderList= {"Male","Female"} ;

	@PostConstruct
	public void init() {
		DepartmentList = getDepartmentList();
		System.out.println(DepartmentList.get(0).getDepartmentName());
	}
	public List<String> completeText(String query) {
        String queryLowerCase = query.toLowerCase();
        List<String> deptList = new ArrayList<String>();
        for (Department country : DepartmentList) {
        	deptList.add(country.getDepartmentName());
        }

        return deptList.stream().filter(t -> t.toLowerCase().startsWith(queryLowerCase)).collect(Collectors.toList());
    }

	public String printData() {
		SessionFactory sessionFactory = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		Transaction t = session.beginTransaction();
		
		Query query= sessionFactory.getCurrentSession().
		        createQuery("from Department where departmentName=:name");
		query.setParameter("name", selectedDepartment);
		Department dept = (Department) query.uniqueResult();
		
		Employee e1 = new Employee();
		e1.setGender(getName());
		e1.setName(getSelectedGender());
		e1.setDept(dept);
		session.save(e1);
		t.commit();
		System.out.println("successfully saved");
		session.close();
		
		return "employeesList";
	}
	
	public void printData1() {
		
		SessionFactory sessionFactory = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
		Session session = sessionFactory.openSession();
		
		Transaction t = session.beginTransaction();
		Employee e1 = session.find(Employee.class, 2 );
		System.out.println( " name : " + e1.getName() + "   gender: " + e1.getGender() + "  Department: " + e1.getDept().getDepartmentName() );
		t.commit();
		System.out.println("successfully saved");
		session.close();
	}
    
	public static List<Department> getDepartmentList() {

		SessionFactory sessionFactory = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
		Session session = sessionFactory.openSession();

		Transaction t = session.beginTransaction();
		 Query qry = session.createQuery("from Department");
		 List<Department> DepartmentList =qry.list();
		System.out.println("successfully saved");
		session.close();
		
		return DepartmentList;
	}

	public void setDepartmentList(List<Department> departmentList) {
		DepartmentList = departmentList;
	}
	public String[] getGenderList() {
		return genderList;
	}
	public void setGenderList(String[] genderList) {
		this.genderList = genderList;
	}
	public String getSelectedGender() {
		return selectedGender;
	}
	public void setSelectedGender(String selectedGender) {
		this.selectedGender = selectedGender;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getSelectedDepartment() {
		return selectedDepartment;
	}
	public void setSelectedDepartment(String selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	}

}
