import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentController {
    private final String DB_URL = "jdbc:mysql://localhost:3306/your_database";
    private final String USER = "your_username";
    private final String PASS = "your_password";
    private Connection conn;

    public StudentController() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public void addStudent(Student s) throws SQLException {
        String sql = "INSERT INTO Student (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, s.getStudentID());
            stmt.setString(2, s.getName());
            stmt.setString(3, s.getDepartment());
            stmt.setDouble(4, s.getMarks());
            stmt.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student(
                    rs.getInt("StudentID"),
                    rs.getString("Name"),
                    rs.getString("Department"),
                    rs.getDouble("Marks")
                );
                students.add(s);
            }
        }
        return students;
    }

    public boolean updateStudent(Student s) throws SQLException {
        String sql = "UPDATE Student SET Name = ?, Department = ?, Marks = ? WHERE StudentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getName());
            stmt.setString(2, s.getDepartment());
            stmt.setDouble(3, s.getMarks());
            stmt.setInt(4, s.getStudentID());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM Student WHERE StudentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
