/*
 * Created by JFormDesigner on Wed Aug 12 02:32:51 IST 2020
 */

package com.company;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.*;

/**
 * @author Harmapreet kaur
 */
public class calculator extends JFrame {
//    public calculator() {
//        initComponents();
//    }

    DefaultTableModel model;
    DefaultTableModel model1;

    Connection con;
    Statement stmt;

    LoanP loanP;


    public calculator() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost/loan", "root", "");
        stmt = con.createStatement();
        initComponents();

        model = new DefaultTableModel();
        model1 = new DefaultTableModel();

        model.addColumn("Number");
        model.addColumn("Name");
        model.addColumn("Amount");
        model.addColumn("Years");
        model.addColumn("Type of Loan");

        model1.addColumn("Payment");
        model1.addColumn("Principal");
        model1.addColumn("Interest");
        model1.addColumn("Monthly Payment");
        model1.addColumn("Balance");

        comboBox1.addItem("Personal");
        comboBox1.addItem("Business");

//        updateTable();
    }

    private void button1ActionPerformed(ActionEvent e) throws ClassNotFoundException, SQLException {

        if(textField1.getText().isEmpty() || textField2.getText().isEmpty() ||
                textField3.getText().isEmpty() || textField4.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Pls fill all the fields");
            return;
        }
        if (Integer.parseInt(textField4.getText()) < 1) {
            JOptionPane.showMessageDialog(null,"No of years should be greater than equal to 1");
            return;
        }

        String customerNumber = textField1.getText().toString();
        String customerName = textField2.getText().toString();
        double loanAmount = Double.parseDouble(textField3.getText());
        int noOfYears = Integer.parseInt(textField4.getText());
        String typeOfLoan = comboBox1.getSelectedItem().toString();

        String query;

        if (typeOfLoan.equals("Personal")) {
            loanP = new Personal(customerNumber, customerName,typeOfLoan, noOfYears, loanAmount);
        } else {
            loanP = new Business(customerNumber, customerName,typeOfLoan, noOfYears, loanAmount);
        }

        query = "insert into loantable(clientno, clientname, loanamount, years, loantype) " +
                "values('" + loanP.clientNumber + "','" + loanP.clientName + "'," + loanP.loanAmount +
                "," + loanP.noOfYears + ",'"+ loanP.loanType+"');";

        table1.setModel(model);

        stmt.executeUpdate(query);

        JOptionPane.showMessageDialog(null,"Record Added");
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");


//        ResultSet rs = stmt.executeQuery("SELECT * FROM loantable");
//
//        while (rs.next()) {
//            String clientNo = rs.getString("clientno");
//            String clientName = rs.getString("clientname");
//            double amount = rs.getDouble("loanamount");
//            int years = rs.getInt("years");
//            String type = rs.getString("loantype");
//            Object[] row = { clientNo, clientName, amount, years, type };
//
//            model.addRow(row);
//
//        }
        updateTable();

        table2.setModel(model1);
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                String customerNumber = table1.getValueAt(table1.getSelectedRow(), 0).toString();
                String customerName = table1.getValueAt(table1.getSelectedRow(), 1).toString();
                double loanAmount = Double.parseDouble(table1.getValueAt(table1.getSelectedRow(), 2).toString());
                int noOfYears = Integer.parseInt(table1.getValueAt(table1.getSelectedRow(), 3).toString());
                String typeOfLoan = table1.getValueAt(table1.getSelectedRow(), 4).toString();

                textField1.setText(table1.getValueAt(table1.getSelectedRow(), 0).toString());
                textField2.setText(table1.getValueAt(table1.getSelectedRow(), 1).toString());
                textField3.setText(table1.getValueAt(table1.getSelectedRow(), 2).toString());
                textField4.setText(table1.getValueAt(table1.getSelectedRow(), 3).toString());

                System.out.println(typeOfLoan);

                ArrayList<LoanPayment> loanPayments;

                if (typeOfLoan.equals("Personal")) {
                    Personal personal = new Personal(customerNumber, customerName, typeOfLoan, noOfYears, loanAmount);
                    loanPayments = personal.generateTable();
                } else {
                    Business business = new Business(customerNumber, customerName, typeOfLoan, noOfYears, loanAmount);
                    loanPayments = business.generateTable();
                }
                model1.setRowCount(0);
                for (int i = 0; i < loanPayments.size(); i++) {
                    Object[] row = { loanPayments.get(i).payment, loanPayments.get(i).principal,
                            loanPayments.get(i).interest, loanPayments.get(i).monthlyPayment, loanPayments.get(i).balance };

                    model1.addRow(row);
                }
                textField5.setText(String.valueOf(loanPayments.get(1).monthlyPayment));
                textField5.setEditable(false);
            }
        });

    }

    public void updateTable() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM loantable");

        while (rs.next()) {
            String clientNo = rs.getString("clientno");
            String clientName = rs.getString("clientname");
            double amount = rs.getDouble("loanamount");
            int years = rs.getInt("years");
            String type = rs.getString("loantype");
            Object[] row = { clientNo, clientName, amount, years, type };

            model.addRow(row);

        }

    }

    private void button2ActionPerformed(ActionEvent e) throws ClassNotFoundException, SQLException {
        stmt.executeUpdate("update loantable set clientno='"+textField1.getText()+"', " +
                "clientname='"+textField2.getText()+"',loanamount="+Double.parseDouble(textField3.getText())+",years="+
                Integer.parseInt(textField4.getText())+" where clientno = '" + textField1.getText() + "';");
        JOptionPane.showMessageDialog(null,"data edited succesfully");
        updateTable();
    }

    public void button3ActionPerformed(ActionEvent e) throws ClassNotFoundException, SQLException {
        stmt.executeUpdate("delete from loantable where clientno='" + textField1.getText() + "';");
        JOptionPane.showMessageDialog(null,"data deleted succesfully");
        updateTable();
    }

    private void initComponents() {
        label1 = new JLabel();
        textField1 = new JTextField();
        label2 = new JLabel();
        textField2 = new JTextField();
        label3 = new JLabel();
        textField3 = new JTextField();
        label4 = new JLabel();
        textField4 = new JTextField();
        label5 = new JLabel();
        comboBox1 = new JComboBox();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        label6 = new JLabel();
        textField5 = new JTextField();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- label1 ----
        label1.setText("Enter Client Number");
        contentPane.add(label1, "cell 1 1");
        contentPane.add(textField1, "cell 5 1 14 1");

        //---- label2 ----
        label2.setText("Enter Client Name");
        contentPane.add(label2, "cell 1 2");
        contentPane.add(textField2, "cell 5 2 14 1");

        //---- label3 ----
        label3.setText("Enter Customer Loan Amount");
        contentPane.add(label3, "cell 1 3");
        contentPane.add(textField3, "cell 5 3 15 1");

        //---- label4 ----
        label4.setText("Enter Number of Years To pay");
        contentPane.add(label4, "cell 1 4");
        contentPane.add(textField4, "cell 5 4 16 1");

        //---- label5 ----
        label5.setText("ENter The loan type");
        contentPane.add(label5, "cell 1 5");
        contentPane.add(comboBox1, "cell 5 5 15 1");

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(table1);
        }
        contentPane.add(scrollPane1, "cell 1 7 23 1");

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(table2);
        }
        contentPane.add(scrollPane2, "cell 1 7 23 1");

        //---- button1 ----
        button1.setText("Add");
        button1.addActionListener(e -> {
            try {
                button1ActionPerformed(e);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        contentPane.add(button1, "cell 1 8");

        //---- button2 ----
        button2.setText("Edit");
        button2.addActionListener(e -> {
            try {
                button2ActionPerformed(e);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        contentPane.add(button2, "cell 1 8");

        //---- button3 ----
        button3.setText("Delete");
        button3.addActionListener(e -> {
            try {
                button3ActionPerformed(e);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        contentPane.add(button3, "cell 4 8");

        //---- label6 ----
        label6.setText("Monthly Payment");
        contentPane.add(label6, "cell 13 8");
        contentPane.add(textField5, "cell 16 8");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
    private JLabel label1;
    private JTextField textField1;
    private JLabel label2;
    private JTextField textField2;
    private JLabel label3;
    private JTextField textField3;
    private JLabel label4;
    private JTextField textField4;
    private JLabel label5;
    private JComboBox comboBox1;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JLabel label6;
    private JTextField textField5;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
