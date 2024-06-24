package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.example.ExchangeRate;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Form1 extends JFrame {
    private HttpURLConnection conn;
    private long unixTimestamp;
    private ExchangeRate exchangeRate;
    private String selectedValue = "";
    private ArrayList<String> savedValues = new ArrayList<>();

    private JLabel label4;
    private JComboBox<String> comboBox2;
    private JComboBox<String> comboBox3;
    private JComboBox<String> comboBox4;
    private JList<String> listBoxResponse;
    private DefaultListModel<String> listModelResponse;
    private JList<String> listComparison;
    private DefaultListModel<String> listModelComparison;
    private JTextField textBox1;
    private JTextField textBox2;

    public Form1() {
        setTitle("Exchange API GUI");
        setSize(960, 540); // Adjusted size for both lists and the footer
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);

        // Dodaj JPanel na górze okna
        GradientPanel topPanel =  new GradientPanel(Color.BLUE, new Color(128, 0, 128), false, true);
        topPanel.setBounds(0, 0, 570, 40); // Ustawienie położenia i rozmiaru panelu
        add(topPanel, BorderLayout.NORTH);



        GradientPanel midPanel = new GradientPanel(Color.BLUE, new Color(128, 0, 128), true, false);
        midPanel.setBackground(Color.BLUE);
        midPanel.setBounds(570, 0, 40, 500);
        add(midPanel);

//        JPanel midPanel2 = new GradientPanel(Color.BLUE, new Color(128, 0, 128), false, false);
//        midPanel2.setBackground(Color.GRAY);
//        midPanel2.setBounds(570, 230, 40, 460);
//        add(midPanel2);

        // JLabel na topPanel do wyświetlania daty
        label4 = new JLabel("Day: ");
        label4.setForeground(Color.WHITE); // Ustaw kolor tekstu na biały
        label4.setBounds(10, 10, 200, 25);
        topPanel.add(label4);

        comboBox2 = new JComboBox<>();
        comboBox2.setBounds(10, 70, 120, 25);
        add(comboBox2);

        comboBox3 = new JComboBox<>();
        comboBox3.setBounds(10, 260, 120, 25);
        add(comboBox3);

        comboBox4 = new JComboBox<>();
        comboBox4.setBounds(10, 290, 120, 25);
        add(comboBox4);

        JButton button6 = new JButton("Add to Comparison");
        button6.setBounds(10, 100, 150, 25);
        button6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button6_Click_1();
            }
        });
        add(button6);

        JButton button4 = new JButton("Remove from Comparison");
        button4.setBounds(10, 130, 200, 25);
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button4_Click_1();
            }
        });
        add(button4);

        listModelResponse = new DefaultListModel<>();
        listBoxResponse = new JList<>(listModelResponse);
        JScrollPane scrollPane = new JScrollPane(listBoxResponse);
        scrollPane.setBounds(630, 20, 300, 430);
        add(scrollPane);

        JLabel labelCurrency = new JLabel("Currency Conversion:");
        labelCurrency.setBounds(10, 240, 150, 25);
        labelCurrency.setForeground(Color.WHITE);
        add(labelCurrency);

        JLabel labelAmount = new JLabel("Amount:");
        labelAmount.setBounds(240, 250, 100, 25);
        labelAmount.setForeground(Color.WHITE);
        add(labelAmount);

        JLabel labelResult = new JLabel("Result:");
        labelResult.setBounds(380, 250, 100, 25);
        labelResult.setForeground(Color.WHITE);
        add(labelResult);

        textBox1 = new JTextField();
        textBox1.setBounds(240, 275, 100, 25);
        add(textBox1);

        textBox2 = new JTextField();
        textBox2.setBounds(380, 275, 100, 25);
        add(textBox2);

        JButton button7 = new JButton("Calculate");
        button7.setBounds(280, 310, 120, 25);
        button7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button7_Click();
            }
        });
        add(button7);

        listModelComparison = new DefaultListModel<>();
        listComparison = new JList<>(listModelComparison);
        JScrollPane scrollPaneComparison = new JScrollPane(listComparison);
        scrollPaneComparison.setBounds(250, 70, 235, 130);
        add(scrollPaneComparison);

        JPanel footerPanel = new GradientPanel(Color.BLUE, new Color(128, 0, 128), true, true);
        footerPanel.setBackground(Color.GRAY);
        footerPanel.setBounds(0, 470, 610, 40);
        add(footerPanel);

        JLabel footerLabel = new JLabel("An Exchange Range App");
        footerLabel.setForeground(Color.WHITE); // Ustaw kolor tekstu na biały
        footerLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Ustawienie czcionki
        footerPanel.add(footerLabel);


        getApi();
    }

    private void getApi() {
        try {
            String call = "https://openexchangerates.org/api/latest.json?app_id=6799573090ce4862adfe6f9e89f1f54b";
            URL url = new URL(call);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(in);
            exchangeRate = new ExchangeRate(jsonObject);
            unixTimestamp = exchangeRate.getTimestamp();
            in.close();

            SwingUtilities.invokeLater(() -> {
                label4.setText("Day: " + exchangeRate.getFormattedDate());

                exchangeRate.getRates().forEach((key, value) -> {
                    comboBox2.addItem(key + ": " + value);
                    comboBox3.addItem(key);
                    comboBox4.addItem(key);
                });

                // Populate listBoxResponse after API call
                button_download_Click();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void button_download_Click() {
        listModelResponse.clear();
        String officialDate = exchangeRate.getFormattedDate();
        listModelResponse.addElement("Date: " + officialDate);
        for (Map.Entry<String, Double> rate : exchangeRate.getRates().entrySet()) {
            listModelResponse.addElement(rate.getKey() + ": " + rate.getValue());
        }
    }

    private void button6_Click_1() {
        if (comboBox2.getSelectedItem() != null) {
            selectedValue = comboBox2.getSelectedItem().toString();
            if (savedValues.contains(selectedValue)) {
                JOptionPane.showMessageDialog(this, "Selected item is already in the comparison list.", "Warning", JOptionPane.INFORMATION_MESSAGE);
            } else {
                savedValues.add(selectedValue);
                listModelComparison.addElement(selectedValue);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select an item from the list.", "Warning", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void button4_Click_1() {
        int selectedIndex = listComparison.getSelectedIndex();
        if (selectedIndex != -1) {
            String deletedValue = listModelComparison.getElementAt(selectedIndex);
            listModelComparison.removeElementAt(selectedIndex);

            if (savedValues.contains(deletedValue)) {
                savedValues.remove(deletedValue);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select an item from the comparison list to remove.", "Warning", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void button5_Click() {
        listModelComparison.clear();
        for (String value : savedValues) {
            listModelComparison.addElement(value);
        }
    }

    private void button7_Click() {
        if (comboBox3.getSelectedItem() != null && comboBox4.getSelectedItem() != null) {
            String item1 = comboBox3.getSelectedItem().toString();
            String item2 = comboBox4.getSelectedItem().toString();

            double calculateValue = exchangeRate.getRates().get(item1) / exchangeRate.getRates().get(item2);
            double amount;
            try {
                amount = Double.parseDouble(textBox1.getText());
            } catch (NumberFormatException e) {
                amount = 0;
            }

            calculateValue *= amount;
            calculateValue = Math.round(calculateValue * 100.0) / 100.0;

            textBox2.setText(String.valueOf(calculateValue));
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all values.", "Warning", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Form1 form = new Form1();
            form.setVisible(true);
        });
    }

}