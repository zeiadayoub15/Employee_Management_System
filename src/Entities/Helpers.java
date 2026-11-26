package Entities;

public final class Helpers {
    // private constructor 3shan m7desh y3mel new object mnha w el methods fe helpers static(shared)
    private Helpers() { 
    }

    public static boolean isValidString(String value) {

    if (value == null) {
        return false;
    }

    if (value.trim().length() == 0) { //trim 3shan mykonsh fe space aw fadya aw fe space fe el awal&a5er
        return false;
    }

    return true;
}
    public static boolean isPositiveInt(int value) {

        if (value > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNonNegativeInt(int value) {

        if (value >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNonNegativeDouble(double value) {

        if (Double.isNaN(value)) {
            return false;
        } else if (Double.isInfinite(value)) {
            return false;
        } else if (value >= 0.0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidDepartment(Department department) {

        if (department == null) {
            return false;
        } else if (!isPositiveInt(department.getId())) {
            return false;
        } else if (!isValidString(department.getName())) {
            return false;
        } else {
            return true;
        }
    }
}
