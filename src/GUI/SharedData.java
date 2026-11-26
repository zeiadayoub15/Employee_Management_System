package GUI;

import Entities.*;
import java.util.ArrayList;
import java.util.List;

public class SharedData {

    private static final List<Employee> employees = new ArrayList<>();
    private static final List<Department> departments = new ArrayList<>();
    private static int nextEmployeeId = 105;

    static {
        departments.add(new Department(1, "IT"));
        departments.add(new Department(2, "Human Resources"));
        departments.add(new Department(3, "Finance"));
        departments.add(new Department(4, "Marketing"));
        departments.add(new Department(5, "Sales"));

        employees.add(new FullTimeEmployee(
                101, "Ahmed Hassan", 28, "IT", "Software Engineer", 15000.0, 2000.0
        ));
        employees.add(new PartTimeEmployee(
                102, "Mona Adel", 24, "Marketing", "Content Specialist", 100.0, 80.0
        ));
        employees.add(new FullTimeEmployee(
                103, "Karim Samir", 32, "Finance", "Accountant", 18000.0, 1500.0
        ));
        employees.add(new PartTimeEmployee(
                104, "Nourhan Tarek", 26, "Human Resources", "Recruiter", 120.0, 60.0
        ));
    }

    public static List<Employee> getEmployees() {
        return employees;
    }

    public static List<Department> getDepartments() {
        return departments;
    }

    public static int generateNextEmployeeId() {
        while (findEmployeeById(nextEmployeeId) != null) {
            nextEmployeeId++;
        }
        return nextEmployeeId++;
    }

    public static Employee findEmployeeById(int id) {
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }

    public static Department findDepartmentById(int id) {
        for (Department dept : departments) {
            if (dept.getId() == id) {
                return dept;
            }
        }
        return null;
    }

    public static Department findDepartmentByName(String name) {
        if (name == null) {
            return null;
        }
        for (Department dept : departments) {
            if (dept.getName() != null && dept.getName().equalsIgnoreCase(name.trim())) {
                return dept;
            }
        }
        return null;
    }

    public static void addEmployee(Employee emp) {
        if (emp != null) {
            employees.add(emp);
        }
    }

    public static void addDepartment(Department dept) {
        if (dept != null && Helpers.isValidDepartment(dept)) {
            departments.add(dept);
        }
    }
}