package Backend;

import Entities.Department;
import Entities.Employee;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Business logic for employee CRUD, search, filtering and ID generation. */
public class EmployeeManager
{
    private final List<Employee> employees;
    private final List<Department> departments;
    private final FileHandler fileHandler;
    private int nextId = 1;

    public EmployeeManager(List<Department> departments, FileHandler fileHandler)
    {
        this(departments, fileHandler, new ArrayList<Employee>());
    }

    public EmployeeManager(List<Department> departments, FileHandler fileHandler, List<Employee> sharedEmployees)
    {
        if (departments == null || fileHandler == null || sharedEmployees == null)
            throw new IllegalArgumentException("Departments, employees and FileHandler are required.");
        this.departments = departments;
        this.fileHandler = fileHandler;
        this.employees = sharedEmployees;
    }

    public synchronized void loadEmployees(List<Employee> loaded)
    {
        List<Employee> source = loaded == employees ? new ArrayList<Employee>(loaded) : loaded;
        employees.clear();

        if (source != null)
        {
            for (Employee e : source)
            {
                Validator.validateEmployee(e);
                ensureUniqueId(e.getId(), -1);
                employees.add(e);
            }
        }
        recalculateNextId();
    }

    public synchronized Employee addEmployee(Employee employee) throws IOException
    {
        Validator.validateEmployee(employee);
        ensureDepartmentExists(employee.getDepartment());
        ensureUniqueId(employee.getId(), -1);
        employees.add(employee);

        try {
            persist();
        } catch (IOException ex) {
            employees.remove(employee);
            throw ex;
        }

        recalculateNextId();
        return employee;
    }

    /** Creates a positive unused ID; useful for GUIs that do not ask users to enter one. */
    public synchronized int generateNextEmployeeId()
    {
        while (findEmployeeById(nextId) != null)
            nextId++;
        return nextId++;
    }

    public synchronized void updateEmployee(int originalId, Employee updated) throws IOException
    {
        if (updated == null)
            throw new IllegalArgumentException("Updated employee cannot be null.");

        int index = indexOf(originalId);

        if (index < 0)
            throw new IllegalArgumentException("Employee " + originalId + " was not found.");

        Validator.validateEmployee(updated);
        ensureDepartmentExists(updated.getDepartment());
        ensureUniqueId(updated.getId(), originalId);
        Employee previous = employees.set(index, updated);
        try {
            persist();
        } catch (IOException ex) {
            employees.set(index, previous); throw ex;
        }
        recalculateNextId();
    }

    public synchronized void deleteEmployee(int id) throws IOException
    {
        int index = indexOf(id);
        if (index < 0)
            throw new IllegalArgumentException("Employee " + id + " was not found.");
        Employee removed = employees.remove(index);
        try {
            persist();
        } catch (IOException ex) {
            employees.add(index, removed); throw ex;
        }
    }

    public synchronized Employee findEmployeeById(int id) {
        int index = indexOf(id);
        return index < 0 ? null : employees.get(index);
    }

    public synchronized List<Employee> getAllEmployees()
    {
        return new ArrayList<Employee>(employees);
    }

    public synchronized List<Employee> searchById(int id)
    {
        Employee e = findEmployeeById(id);
        List<Employee> result = new ArrayList<Employee>();

        if (e != null)
            result.add(e);
        return result;
    }

    public synchronized List<Employee> searchByName(String query)
    {
        Validator.requireText(query, "Search text");
        String q = query.trim().toLowerCase();
        List<Employee> result = new ArrayList<Employee>();

        for (Employee e : employees)
            if (e.getName().toLowerCase().contains(q))
                result.add(e);
        return result;
    }

    public synchronized List<Employee> filterByDepartment(String departmentName)
    {
        Validator.requireText(departmentName, "Department");
        List<Employee> result = new ArrayList<Employee>();

        for (Employee e : employees)
            if (e.getDepartment().equalsIgnoreCase(departmentName.trim()))
                result.add(e);
        return result;
    }

    public synchronized double calculateSalary(int employeeId)
    {
        Employee e = findEmployeeById(employeeId);

        if (e == null)
            throw new IllegalArgumentException("Employee " + employeeId + " was not found.");
        return e.calculateSalary();
    }

    public synchronized int getEmployeeCount()
    {
        return employees.size();
    }

    private int indexOf(int id)
    {
        for (int i = 0; i < employees.size(); i++)
            if (employees.get(i).getId() == id)
                return i;
        return -1;
    }

    private void ensureUniqueId(int id, int allowedExistingId)
    {
        for (Employee e : employees)
            if (e.getId() == id && e.getId() != allowedExistingId)
              throw new IllegalArgumentException("Employee ID " + id + " already exists.");
    }

    private void ensureDepartmentExists(String name)
    {
        for (Department d : departments)
            if (d.getName().equalsIgnoreCase(name.trim()))
                return;
        throw new IllegalArgumentException("Select an existing department. Unknown department: " + name);
    }

    private void recalculateNextId()
    {
        nextId = 1;
        for (Employee e : employees)
            if (e.getId() >= nextId)
                nextId = e.getId() + 1;
    }

    private void persist() throws IOException
    {
        fileHandler.save(employees, departments);
    }
}
