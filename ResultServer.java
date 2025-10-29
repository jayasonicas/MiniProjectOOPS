import java.io.*;
import java.net.*;
import java.sql.*;

public class ResultServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("✅ Server started on port 5000...");

            // Database connection
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/studentdb", "root", "12345"
            );

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket, conn)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket, Connection conn) {
        try (
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            String action = (String) in.readObject();

            if ("ADD".equals(action)) {
                String name = (String) in.readObject();
                String reg = (String) in.readObject();
                String dept = (String) in.readObject();
                String subject = (String) in.readObject();
                int marks = (int) in.readObject();

                String result = marks >= 50 ? "Pass" : "Fail";
                String grade = getGrade(marks);

                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO results (name, reg_no, department, subject, marks, result, grade) VALUES (?, ?, ?, ?, ?, ?, ?)"
                );
                ps.setString(1, name);
                ps.setString(2, reg);
                ps.setString(3, dept);
                ps.setString(4, subject);
                ps.setInt(5, marks);
                ps.setString(6, result);
                ps.setString(7, grade);
                ps.executeUpdate();

                out.writeObject("✅ Record added successfully!");
                out.flush();
            }

            else if ("VIEW".equals(action)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM results");

                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append(rs.getString("reg_no")).append(",")
                      .append(rs.getString("name")).append(",")
                      .append(rs.getString("department")).append(",")
                      .append(rs.getString("subject")).append(",")
                      .append(rs.getInt("marks")).append(",")
                      .append(rs.getString("result")).append(",")
                      .append(rs.getString("grade")).append(";");
                }

                out.writeObject(sb.toString());
                out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private static String getGrade(int marks) {
        if (marks >= 90) return "A+";
        else if (marks >= 80) return "A";
        else if (marks >= 70) return "B";
        else if (marks >= 60) return "C";
        else if (marks >= 50) return "D";
        else return "F";
    }
}
