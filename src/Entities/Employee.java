package Entities;
import java.io.Serializable; // bn7wel el object L bytes 3shan a5zen el file 3shan el data mtde3s

public abstract class Employee implements Serializable
{

    private int id;
    private String name;
    private int age;
    private String department;
    private String position;

    public Employee(int id, String name, int age, String department, String position)
    {
        setId(id);
        setName(name);
        setAge(age);
        setDepartment(department);
        setPosition(position);
    }

    //abstract 3shan kol employee 3ndo calSalary mo5talefa
    public abstract double calculateSalary();

    //FullTime wla PartTime
    public abstract String getType();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (Helpers.isPositiveInt(id)) {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (Helpers.isValidString(name)) {
            this.name = name.trim();
        }
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (Helpers.isPositiveInt(age)) {
            this.age = age;
        }
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        if (Helpers.isValidString(department)) {
            this.department = department.trim();
        }
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        if (Helpers.isValidString(position)) {
            this.position = position.trim();
        }
    }

    @Override // toString fn gahza fe java btrga3 esm el class w makano fe el memory
    public String toString() { //override 3shan trga3 el id name .. 
        return id + " - " + name + " (" + getType() + ", " + department + ") salary = " + calculateSalary();
    }
}
