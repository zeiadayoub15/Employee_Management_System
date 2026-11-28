package Entities;
//el fullTime 3ndo salary + bonus
public class FullTimeEmployee extends Employee {

    private double basicSalary;
    private double bonus;

    public FullTimeEmployee(int id, String name, int age, String department, String position, double basicSalary, double bonus)
    {
        super(id, name, age, department, position);
        setBasicSalary(basicSalary);
        setBonus(bonus);
    }

    //Salary = Basic Salary + Bonus
    @Override
    public double calculateSalary() {
        return basicSalary + bonus;
    }

    @Override
    public String getType() {
        return "Full-Time";
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary)
    {
        if (Helpers.isNonNegativeDouble(basicSalary)) {
            this.basicSalary = basicSalary;
        }
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        if (Helpers.isNonNegativeDouble(bonus)) {
            this.bonus = bonus;
        }
    }
}
