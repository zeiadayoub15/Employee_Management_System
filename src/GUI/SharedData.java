package GUI;

import Backend.BackendContext;
import Backend.DepartmentManager;
import Backend.EmployeeManager;
import Entities.Department;
import Entities.Employee;

import java.io.IOException;
import java.util.List;

/** Single access point to the backend. One BackendContext for the whole app session. */
public class SharedData {

    private static BackendContext backend;

    /** Call once at startup, before opening any window. */
    public static void init() throws IOException {
        if (backend == null) {
            backend = new BackendContext();
        }
    }

    public static EmployeeManager employees() {
        return backend.getEmployeeManager();
    }

    public static DepartmentManager departments() {
        return backend.getDepartmentManager();
    }

    // Read-only helpers (they return COPIES, so never modify these lists directly)
    public static List<Employee> getEmployees() {
        return employees().getAllEmployees();
    }

    public static List<Department> getDepartments() {
        return departments().getAllDepartments();
    }

    public static Employee findEmployeeById(int id) {
        return employees().findEmployeeById(id);
    }

    public static Department findDepartmentById(int id) {
        return departments().findDepartmentById(id);
    }

    public static Department findDepartmentByName(String name) {
        return departments().findDepartmentByName(name);
    }
}
