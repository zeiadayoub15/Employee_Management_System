package Backend;

import Entities.Department;
import Entities.Employee;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Business logic for department CRUD and referential integrity. */
public class DepartmentManager
{
    private final List<Department> departments;
    private final List<Employee> employees;
    private final FileHandler fileHandler;

    public DepartmentManager(List<Employee> employees, FileHandler fileHandler)
    {
        this(employees, fileHandler, new ArrayList<Department>());
    }

    /** Uses a department list shared with EmployeeManager so both always see the same departments. */
    public DepartmentManager(List<Employee> employees, FileHandler fileHandler, List<Department> sharedDepartments)
    {
        if (employees == null || fileHandler == null || sharedDepartments == null)
            throw new IllegalArgumentException("Employees, FileHandler and departments are required.");
        this.employees = employees;
        this.fileHandler = fileHandler;
        this.departments = sharedDepartments;
    }
    public synchronized void loadDepartments(List<Department> loaded)
    {
        List<Department> source = loaded == departments ? new ArrayList<Department>(loaded) : loaded;
        departments.clear();
        if (source != null)
            for (Department d : source)
            {
            Validator.validateDepartment(d);
            ensureUnique(d.getId(), d.getName(), -1);
            departments.add(d);
            }
    }
    public synchronized void addDepartment(Department d) throws IOException
    {
        Validator.validateDepartment(d);
        ensureUnique(d.getId(), d.getName(), -1);
        departments.add(d);
        try {
            persist();
        } catch (IOException ex) {
            departments.remove(d); throw ex;
        }
    }
    public synchronized void updateDepartment(int originalId, Department updated) throws IOException
    {
        Validator.validateDepartment(updated);
        int index = indexOf(originalId);
        if (index < 0)
            throw new IllegalArgumentException("Department " + originalId + " was not found.");

        ensureUnique(updated.getId(), updated.getName(), originalId);
        Department previous = departments.set(index, updated);

        try {
            // Keep employee department assignments consistent if the department is renamed.
            if (!previous.getName().equals(updated.getName()))
            {
                for (Employee e : employees)
                    if (e.getDepartment().equalsIgnoreCase(previous.getName()))
                        e.setDepartment(updated.getName());
            }
            persist();

        } catch (IOException ex) {
            departments.set(index, previous);
            for (Employee e : employees)
                if (e.getDepartment().equalsIgnoreCase(updated.getName()))
                    e.setDepartment(previous.getName());
            throw ex;
        }
    }
    public synchronized void deleteDepartment(int id) throws IOException
    {
        int index = indexOf(id);
        if (index < 0)
            throw new IllegalArgumentException("Department " + id + " was not found.");

        Department d = departments.get(index);

        for (Employee e : employees)
            if (e.getDepartment().equalsIgnoreCase(d.getName()))
                throw new IllegalArgumentException("Cannot delete a department that still has employees assigned to it.");

        departments.remove(index);
        try {
            persist();
        } catch (IOException ex) {
            departments.add(index, d); throw ex;
        }
    }

    public synchronized List<Department> getAllDepartments()
    {
        return new ArrayList<Department>(departments);
    }

    public synchronized Department findDepartmentById(int id)
    {
        int i=indexOf(id);
        return i < 0 ? null : departments.get(i);
    }

    public synchronized Department findDepartmentByName(String name)
    {
        if (name == null)
            return null;
        for (Department d : departments)
            if (d.getName().equalsIgnoreCase(name.trim()))
                return d;
        return null;
    }

    private int indexOf(int id)
    {
        for (int i=0;i<departments.size();i++)
            if (departments.get(i).getId()==id)
                return i;
        return -1;
    }

    private void ensureUnique(int id, String name, int allowedExistingId)
    {
        for (Department d : departments)
        {
            if (d.getId()==id && d.getId()!=allowedExistingId)
                throw new IllegalArgumentException("Department ID " + id + " already exists.");
            if (d.getName().equalsIgnoreCase(name.trim()) && d.getId()!=allowedExistingId)
                throw new IllegalArgumentException("A department with this name already exists.");
        }
    }

    private void persist() throws IOException
    {
        fileHandler.save(employees, departments);
    }
}
