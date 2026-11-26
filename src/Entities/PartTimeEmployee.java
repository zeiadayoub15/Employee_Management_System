package Entities;

// Parttime paid by hour
public class PartTimeEmployee extends Employee {

    private double hourlyRate;
    private double hoursWorked;

    public PartTimeEmployee(int id, String name, int age, String department,String position
                            , double hourlyRate, double hoursWorked) {
        super(id, name, age, department, position);
        setHourlyRate(hourlyRate);
        setHoursWorked(hoursWorked);
    }

    //Salary = Hourly Rate x Hours Worked
    @Override
    public double calculateSalary() {
        return hourlyRate * hoursWorked;
    }

    @Override
    public String getType() {
        return "Part-Time";
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        if (Helpers.isNonNegativeDouble(hourlyRate)) {
            this.hourlyRate = hourlyRate;
        }
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        if (Helpers.isNonNegativeDouble(hoursWorked)) {
            this.hoursWorked = hoursWorked;
        }
    }
}
