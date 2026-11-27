package Backend;

import Entities.Department;
import Entities.Employee;
import Entities.FullTimeEmployee;
import Entities.PartTimeEmployee;

/** Central validation. Throws IllegalArgumentException with a GUI-friendly message. */
public final class Validator {
    private Validator() { }

    public static void validateEmployee(Employee e) {
        if (e == null) throw new IllegalArgumentException("Employee cannot be null.");
        if (e.getId() <= 0) throw new IllegalArgumentException("Employee ID must be a positive integer.");
        requireText(e.getName(), "Full name");
        if (e.getAge() <= 0) throw new IllegalArgumentException("Age must be a positive integer.");
        requireText(e.getDepartment(), "Department");
        requireText(e.getPosition(), "Position");
        if (e instanceof FullTimeEmployee) {
            FullTimeEmployee ft = (FullTimeEmployee) e;
            requireNonNegative(ft.getBasicSalary(), "Basic salary");
            requireNonNegative(ft.getBonus(), "Bonus");
        } else if (e instanceof PartTimeEmployee) {
            PartTimeEmployee pt = (PartTimeEmployee) e;
            requireNonNegative(pt.getHourlyRate(), "Hourly rate");
            requireNonNegative(pt.getHoursWorked(), "Hours worked");
        } else {
            throw new IllegalArgumentException("Unsupported employee type.");
        }
        requireNonNegative(e.calculateSalary(), "Calculated salary");
    }

    public static void validateDepartment(Department d) {
        if (d == null) throw new IllegalArgumentException("Department cannot be null.");
        if (d.getId() <= 0) throw new IllegalArgumentException("Department ID must be a positive integer.");
        requireText(d.getName(), "Department name");
    }

    public static void requireText(String value, String field) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException(field + " is required.");
    }

    public static void requireNonNegative(double value, String field) {
        if (Double.isNaN(value) || Double.isInfinite(value) || value < 0.0)
            throw new IllegalArgumentException(field + " must be a valid non-negative number.");
    }
}
