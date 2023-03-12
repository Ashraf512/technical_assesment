package com.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import com.model.Department;
import com.model.Employee;
import com.services.EmployeeService;

import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Named;

@Setter
@Getter
@ManagedBean(name = "dtBasicView")
@SessionScoped
public class CrudView implements Serializable {

	private static final long serialVersionUID = -7958165645137129519L;

	private List<Employee> emlpoyees;
	private Employee selectedEmployee;



	@PostConstruct
	public void init() {
		emlpoyees = getAllEmployees();
	}



	
	public static List<Employee> getAllEmployees() {

		SessionFactory sessionFactory = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
		Session session = sessionFactory.openSession();

		Transaction t = session.beginTransaction();
		 Query qry = session.createQuery("from Employee");
		 List<Employee> employees =qry.list();
		System.out.println("successfully saved");
		session.close();
		
		return employees;
	}
	
    public void onRowEdit(RowEditEvent<Employee> event) {
        FacesMessage msg = new FacesMessage("Product Edited", String.valueOf(event.getObject().getGender()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent<Employee> event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", String.valueOf(event.getObject().getGender()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public String getRowID(int x) {
        System.out.println("ID: " + x );
        
    	SessionFactory sessionFactory = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
		Session session = sessionFactory.openSession();

		Transaction t = session.beginTransaction();
		Employee e1 = session.find(Employee.class, x );
		session.delete(e1);
		t.commit();
		System.out.println("Deleted...! ");
		session.close();
		
		return "employeesList";
    }
	public List<Employee> getEmlpoyees() {
		return emlpoyees;
	}
	public void setEmlpoyees(List<Employee> emlpoyees) {
		this.emlpoyees = emlpoyees;
	}

	public Employee getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(Employee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}
}
