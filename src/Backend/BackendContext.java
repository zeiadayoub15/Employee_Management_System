package Backend;

import Entities.Department;
import Entities.Employee;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Creates the shared backend services and loads persistent data once at startup. */
public class BackendContext {
    private final FileHandler fileHandler;
    private final List<Employee> employees = new ArrayList<Employee>();
    private final List<Department> departments = new ArrayList<Department>();
    private final EmployeeManager employeeManager;
    private final DepartmentManager departmentManager;

    public BackendContext() throws IOException { this(new FileHandler()); }
    public BackendContext(FileHandler handler) throws IOException {
        if (handler == null) throw new IllegalArgumentException("FileHandler is required.");
        this.fileHandler = handler;
        FileHandler.Snapshot snapshot = handler.load();
        employees.addAll(snapshot.getEmployees());
        departments.addAll(snapshot.getDepartments());
        if (departments.isEmpty()) seedDefaultDepartments();
        this.employeeManager = new EmployeeManager(departments, handler, employees);
        this.departmentManager = new DepartmentManager(employees, handler, departments);
        employeeManager.loadEmployees(employees);
        departmentManager.loadDepartments(departments);
        // Persist seeded departments so first launch is deterministic.
        if (snapshot.getDepartments().isEmpty()) handler.save(employees, departments);
    }
    private void seedDefaultDepartments() {
        departments.add(new Department(1, "IT"));
        departments.add(new Department(2, "Human Resources"));
        departments.add(new Department(3, "Finance"));
        departments.add(new Department(4, "Marketing"));
        departments.add(new Department(5, "Sales"));
    }
    public EmployeeManager getEmployeeManager() { return employeeManager; }
    public DepartmentManager getDepartmentManager() { return departmentManager; }
    public FileHandler getFileHandler() { return fileHandler; }
}
