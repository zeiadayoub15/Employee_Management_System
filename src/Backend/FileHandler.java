package Backend;

import Entities.Department;
import Entities.Employee;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/** Persists the real Employee subclass objects and departments in one serialized file. */
public class FileHandler
{
    private final Path dataFile;

    public FileHandler()
    {
        this(Paths.get("data", "ems-data.ser"));
    }

    public FileHandler(String filePath)
    {
        this(Paths.get(filePath));
    }

    public FileHandler(Path filePath)
    {
        if (filePath == null)
            throw new IllegalArgumentException("Data file path cannot be null.");
        this.dataFile = filePath;
    }

    public static final class Snapshot implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private final ArrayList<Employee> employees;
        private final ArrayList<Department> departments;

        public Snapshot(List<Employee> employees, List<Department> departments)
        {
            this.employees = employees == null ? new ArrayList<Employee>() : new ArrayList<Employee>(employees);
            this.departments = departments == null ? new ArrayList<Department>() : new ArrayList<Department>(departments);
        }

        public List<Employee> getEmployees()
        {
            return new ArrayList<Employee>(employees);
        }

        public List<Department> getDepartments()
        {
            return new ArrayList<Department>(departments);
        }
    }

    /** A missing file means first launch and returns empty collections. */
    public synchronized Snapshot load() throws IOException
    {
        if (!Files.exists(dataFile))
            return new Snapshot(null, null);
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(dataFile))))
        {
            Object object = in.readObject();
            if (!(object instanceof Snapshot))
                throw new IOException("Data file has an unsupported format.");
            return (Snapshot) object;
        }
        catch (ClassNotFoundException ex) {
            throw new IOException("A saved class could not be loaded. Check that model classes have not been renamed.", ex);
        }
        catch (InvalidClassException ex) {
            throw new IOException("Saved data is incompatible with the current model classes.", ex);
        }
    }

    /** Writes to a temporary file first, then replaces the previous data file. */
    public synchronized void save(List<Employee> employees, List<Department> departments) throws IOException
    {
        Path parent = dataFile.toAbsolutePath().getParent();

        if (parent != null)
            Files.createDirectories(parent);

        Path temp = dataFile.resolveSibling(dataFile.getFileName().toString() + ".tmp");
        Snapshot snapshot = new Snapshot(employees, departments);
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(temp)))) {
            out.writeObject(snapshot);
            out.flush();
        }try {
            Files.move(temp, dataFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException ex) {
            Files.move(temp, dataFile, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    public String getFilePath()
    {
        return dataFile.toString();
    }
}
