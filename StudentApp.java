import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class StudentApp {
    public static void main(String[] args) {
        try {
            StudentController controller = new StudentController();
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println("\n=== STUDENT MANAGEMENT SYSTEM ===");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Student ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Name: ");
                        String name = sc.nextLine();

                        System.out.print("Department: ");
                        String dept = sc.nextLine();

                        System.out.print("Marks: ");
                        double marks = sc.nextDouble();

                        Student s = new Student(id, name, dept, marks);
                        controller.addStudent(s);
                        System.out.println("Student added successfully.");
                        break;

                    case 2:
                        List<Student> students = controller.getAllStudents();
                        System.out.println("\n--- Student Records ---");
                        for (Student stu : students) {
                            System.out.printf("ID: %d | Name: %s | Dept: %s | Marks: %.2f%n",
                                    stu.getStudentID(), stu.getName(), stu.getDepartment(), stu.getMarks());
                        }
                        break;

                    case 3:
                        System.out.print("Enter ID to update: ");
                        int uid = sc.nextInt();
                        sc.nextLine();
                        System.out.print("New Name: ");
                        String uname = sc.nextLine();
                        System.out.print("New Department: ");
                        String udept = sc.nextLine();
                        System.out.print("New Marks: ");
                        double umarks = sc.nextDouble();

                        Student updated = new Student(uid, uname, udept, umarks);
                        if (controller.updateStudent(updated))
                            System.out.println("Student updated successfully.");
                        else
                            System.out.println("Student not found.");
                        break;

                    case 4:
                        System.out.print("Enter ID to delete: ");
                        int did = sc.nextInt();
                        if (controller.deleteStudent(did))
                            System.out.println("Student deleted.");
                        else
                            System.out.println("Student not found.");
                        break;

                    case 5:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
