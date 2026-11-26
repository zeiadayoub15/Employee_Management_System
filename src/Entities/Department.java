package Entities;

import java.io.Serializable;// interface save file

public class Department implements Serializable {

    private int id;
    private String name;

    public Department(int id, String name) {
        setId(id);
        setName(name);
    }

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

    // 3shan ytla3 el name gowa JComboBox fe el Gui
    @Override
    public String toString() {
        return name;
    }
}
