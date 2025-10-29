import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ResultClient extends JFrame {
    private JTextField nameField, regField, deptField, marksField;
    private JComboBox<String> subjectBox;
    private DefaultTableModel model;

    public ResultClient() {
        setTitle("🎓 Student Result Management System");
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Gradient background panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(80, 120, 255), 0, getHeight(), new Color(140, 70, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);
        add(panel);

        JLabel title = new JLabel("Enter Student Details");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(300, 20, 400, 30);
        panel.add(title);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(100, 80, 100, 25);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(250, 80, 200, 25);
        panel.add(nameField);

        JLabel regLabel = new JLabel("Reg No:");
        regLabel.setForeground(Color.WHITE);
        regLabel.setBounds(100, 120, 100, 25);
        panel.add(regLabel);

        regField = new JTextField();
        regField.setBounds(250, 120, 200, 25);
        panel.add(regField);

        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setForeground(Color.WHITE);
        deptLabel.setBounds(100, 160, 100, 25);
        panel.add(deptLabel);

        deptField = new JTextField();
        deptField.setBounds(250, 160, 200, 25);
        panel.add(deptField);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setForeground(Color.WHITE);
        subjectLabel.setBounds(100, 200, 100, 25);
        panel.add(subjectLabel);

        subjectBox = new JComboBox<>(new String[]{
            "Mathematics", "Digital Principles", "Computer Organization",
            "Artificial Intelligence", "OOPs", "DBMS"
        });
        subjectBox.setBounds(250, 200, 200, 25);
        panel.add(subjectBox);

        JLabel marksLabel = new JLabel("Marks:");
        marksLabel.setForeground(Color.WHITE);
        marksLabel.setBounds(100, 240, 100, 25);
        panel.add(marksLabel);

        marksField = new JTextField();
        marksField.setBounds(250, 240, 200, 25);
        panel.add(marksField);

        JButton addBtn = new JButton("Add Result");
        addBtn.setBounds(480, 240, 120, 30);
        addBtn.addActionListener(e -> addResult());
        panel.add(addBtn);

        JButton viewBtn = new JButton("View Results");
        viewBtn.setBounds(620, 240, 120, 30);
        viewBtn.addActionListener(e -> fetchResults());
        panel.add(viewBtn);

        model = new DefaultTableModel(new String[]{"Reg No", "Name", "Department", "Subject", "Marks", "Result", "Grade"}, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(100, 300, 640, 200);
        panel.add(scroll);

        setVisible(true);
    }

    private void addResult() {
        try (Socket socket = new Socket("localhost", 5000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("ADD");
            out.writeObject(nameField.getText());
            out.writeObject(regField.getText());
            out.writeObject(deptField.getText());
            out.writeObject(subjectBox.getSelectedItem().toString());
            out.writeObject(Integer.parseInt(marksField.getText()));
            out.flush();

            Object response = in.readObject();
            JOptionPane.showMessageDialog(this, response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Connection Error: " + e.getMessage());
        }
    }

    private void fetchResults() {
        model.setRowCount(0);
        try (Socket socket = new Socket("localhost", 5000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("VIEW");
            out.flush();

            String data = (String) in.readObject();
            if (data != null && !data.isEmpty()) {
                for (String row : data.split(";")) {
                    model.addRow(row.split(","));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Error fetching results!");
        }
    }

    public static void main(String[] args) {
        new ResultClient();
    }
}
