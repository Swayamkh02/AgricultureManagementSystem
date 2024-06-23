import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.border.Border;
import javax.swing.border.*;
import javax.swing.border.CompoundBorder;

public class AgricultureManagementSystem extends JFrame {
    private JTextField cropNameField;
    private JTextField cropQuantityField;
    private JTextField farmerNameField;
    private JTextField phoneNumberField;
    private JTextArea outputTextArea;
    private List<Crop> cropList;
    private JComboBox<String> groupByComboBox;
    private JButton sortButton;

    public AgricultureManagementSystem() {
        // Set up the main frame
        setTitle("Agriculture Management System");
        setSize(1000, 400); // Increased the window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize crop list
        cropList = new ArrayList<>();

        // Create components
        JLabel nameLabel = new JLabel("Crop Name:");
        JLabel quantityLabel = new JLabel("Crop Quantity:");
        JLabel farmerNameLabel = new JLabel("Farmer's Name:");
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        cropNameField = new JTextField(20);
        cropQuantityField = new JTextField(10);
        farmerNameField = new JTextField(20);
        phoneNumberField = new JTextField(15);
        JButton addButton = new JButton("Add Crop");
        JButton editButton = new JButton("Edit Crop");
        JButton deleteButton = new JButton("Delete Crop");
        JButton exportButton = new JButton("Export Data");
        outputTextArea = new JTextArea(10, 30);

        // Create a panel for input fields and buttons with a GridLayout
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2)); // Increased to accommodate the buttons
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        inputPanel.setBackground(new Color(224, 255, 255)); // Light blue background

        // Create and set borders for related fields
        cropNameField.setBorder(BorderFactory.createTitledBorder("Crop Name"));
        cropQuantityField.setBorder(BorderFactory.createTitledBorder("Crop Quantity"));
        farmerNameField.setBorder(BorderFactory.createTitledBorder("Farmer's Name"));
        phoneNumberField.setBorder(BorderFactory.createTitledBorder("Phone Number"));

        // Set background color for the input panel
        inputPanel.setBackground(new Color(224, 255, 255)); // Light blue background
        cropNameField.setBackground(new Color(255, 255, 240)); // Light yellow background
        cropQuantityField.setBackground(new Color(255, 255, 240)); // Light yellow background
        farmerNameField.setBackground(new Color(255, 255, 240)); // Light yellow background
        phoneNumberField.setBackground(new Color(255, 255, 240)); // Light yellow background
        Border roundedBorder = BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true); // Customize color and thickness
        cropNameField.setBorder(new CompoundBorder(roundedBorder, new EmptyBorder(0, 20, 0, 20)));
        cropQuantityField.setBorder(new CompoundBorder(roundedBorder, new EmptyBorder(0, 20, 0, 20)));
        farmerNameField.setBorder(new CompoundBorder(roundedBorder, new EmptyBorder(0, 20, 0, 20)));
        phoneNumberField.setBorder(new CompoundBorder(roundedBorder, new EmptyBorder(0, 20, 0, 20)));


        // Add components to the input panel
        inputPanel.add(nameLabel);
        inputPanel.add(cropNameField);
        inputPanel.add(quantityLabel);
        inputPanel.add(cropQuantityField);
        inputPanel.add(farmerNameLabel);
        inputPanel.add(farmerNameField);
        inputPanel.add(phoneNumberLabel);
        inputPanel.add(phoneNumberField);
        inputPanel.add(addButton);
        inputPanel.add(editButton);
        inputPanel.add(deleteButton);
        inputPanel.add(exportButton);

        // Create a scrollable output text area
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setBackground(new Color(255, 255, 240)); // Light yellow background

        // Create a panel for grouping and sorting
        JPanel groupSortPanel = new JPanel();
        groupSortPanel.setLayout(new FlowLayout());
        JLabel groupByLabel = new JLabel("Group By:");
        String[] groupByOptions = {"None", "Crop Name", "Farmer's Name"};
        groupByComboBox = new JComboBox<>(groupByOptions);
        groupByComboBox.setSelectedIndex(0); // Default: None
        sortButton = new JButton("Sort");
        groupSortPanel.add(groupByLabel);
        groupSortPanel.add(groupByComboBox);
        groupSortPanel.add(sortButton);

        // Add components to the main frame
        add(inputPanel, BorderLayout.WEST); // Changed to WEST
        add(scrollPane, BorderLayout.CENTER);
        add(groupSortPanel, BorderLayout.NORTH);
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background for the JFrame


        // Register an ActionListener for the "Edit Crop" button
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cropNameToEdit = cropNameField.getText();
                String farmerNameToEdit = farmerNameField.getText();
                for (Crop crop : cropList) {
                    if (crop.getName().equals(cropNameToEdit) && crop.getFarmerName().equals(farmerNameToEdit)) {
                        String newQuantityText = cropQuantityField.getText();
                        try {
                            int newQuantity = Integer.parseInt(newQuantityText);
                            crop.setQuantity(newQuantity);
                            updateOutputTextArea();
                            clearInputFields();
                            return;
                        } catch (NumberFormatException ex) {
                            showErrorMessage("Please enter a valid quantity.");
                            return;
                        }
                    }
                }
                showErrorMessage("Crop not found.");
            }
        });

        // Register an ActionListener for the "Delete Crop" button
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cropNameToDelete = cropNameField.getText();
                String farmerNameToDelete = farmerNameField.getText();
                Crop cropToRemove = null;
                for (Crop crop : cropList) {
                    if (crop.getName().equals(cropNameToDelete) && crop.getFarmerName().equals(farmerNameToDelete)) {
                        cropToRemove = crop;
                        break;
                    }
                }
                if (cropToRemove != null) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this crop?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        cropList.remove(cropToRemove);
                        updateOutputTextArea();
                        clearInputFields();
                    }
                } else {
                    showErrorMessage("Crop not found.");
                }
            }
        });

        // Register an ActionListener for the "Add Crop" button
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cropNameToAdd = cropNameField.getText();
                String quantityText = cropQuantityField.getText();
                String farmerName = farmerNameField.getText();
                String phoneNumber = phoneNumberField.getText();

                if (cropNameToAdd.isEmpty() || quantityText.isEmpty() || farmerName.isEmpty() || phoneNumber.isEmpty()) {
                    showErrorMessage("Please enter all required information.");
                    return;
                }

                try {
                    int quantity = Integer.parseInt(quantityText);
                    Crop newCrop = new Crop(cropNameToAdd, quantity, farmerName, phoneNumber);
                    cropList.add(newCrop);
                    updateOutputTextArea();
                    clearInputFields();
                } catch (NumberFormatException ex) {
                    showErrorMessage("Please enter a valid quantity.");
                }
            }
        });

        // Register an ActionListener for the "Export Data" button
        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (PrintWriter writer = new PrintWriter(new FileWriter("crop_data.csv"))) {
                    for (Crop crop : cropList) {
                        writer.println(crop.getName() + "," + crop.getQuantity() + "," + crop.getFarmerName() + "," + crop.getPhoneNumber());
                    }
                    showInfoMessage("Data exported to crop_data.csv.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    showErrorMessage("Error while exporting data.");
                }
            }
        });

        // Register ActionListener for the "Sort" button
        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedGroupBy = (String) groupByComboBox.getSelectedItem();
                if ("None".equals(selectedGroupBy)) {
                    // No grouping, simply update the output
                    updateOutputTextArea();
                } else {
                    // Group by the selected criteria
                    groupAndSortCropList(selectedGroupBy);
                }
            }
        });
    }

    private void groupAndSortCropList(String groupBy) {
        // Group the crop list based on the selected criteria
        List<List<Crop>> groupedLists = new ArrayList<>();
        List<String> groupLabels = new ArrayList<>();

        for (Crop crop : cropList) {
            boolean isGrouped = false;
            for (int i = 0; i < groupedLists.size(); i++) {
                List<Crop> group = groupedLists.get(i);
                String label = groupLabels.get(i);

                switch (groupBy) {
                    case "Crop Name":
                        if (crop.getName().equalsIgnoreCase(label)) {
                            group.add(crop);
                            isGrouped = true;
                        }
                        break;
                    case "Farmer's Name":
                        if (crop.getFarmerName().equalsIgnoreCase(label)) {
                            group.add(crop);
                            isGrouped = true;
                        }
                        break;
                    // Add more cases for other grouping criteria if needed
                }
            }
            if (!isGrouped) {
                List<Crop> newGroup = new ArrayList<>();
                newGroup.add(crop);
                groupedLists.add(newGroup);

                switch (groupBy) {
                    case "Crop Name":
                        groupLabels.add(crop.getName());
                        break;
                    case "Farmer's Name":
                        groupLabels.add(crop.getFarmerName());
                        break;
                    // Add more cases for other grouping criteria if needed
                }
            }
        }

        // Sort each group
        for (List<Crop> group : groupedLists) {
            group.sort(new Comparator<Crop>() {
                public int compare(Crop crop1, Crop crop2) {
                    switch (groupBy) {
                        case "Crop Name":
                            return crop1.getName().compareToIgnoreCase(crop2.getName());
                        case "Farmer's Name":
                            return crop1.getFarmerName().compareToIgnoreCase(crop2.getFarmerName());
                        // Add more cases for other grouping criteria if needed
                        default:
                            return 0; // Default case
                    }
                }
            });
        }

        // Update the output text area with grouped and sorted data
        outputTextArea.setText("");
        for (int i = 0; i < groupedLists.size(); i++) {
            List<Crop> group = groupedLists.get(i);
            String label = groupLabels.get(i);

            outputTextArea.append("Group: " + label + "\n");
            for (Crop crop : group) {
                outputTextArea.append("Crop Name: " + crop.getName() + ", Quantity: " + crop.getQuantity() + ", Farmer: " + crop.getFarmerName() + ", Phone: " + crop.getPhoneNumber() + "\n");
            }
            outputTextArea.append("\n");
        }
    }

    private void updateOutputTextArea() {
        outputTextArea.setText("");
        for (Crop crop : cropList) {
            outputTextArea.append("Crop Name: " + crop.getName() + ", Quantity: " + crop.getQuantity() + ", Farmer: " + crop.getFarmerName() + ", Phone: " + crop.getPhoneNumber() + "\n");
        }
    }

    private void clearInputFields() {
        cropNameField.setText("");
        cropQuantityField.setText("");
        farmerNameField.setText("");
        phoneNumberField.setText("");
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AgricultureManagementSystem app = new AgricultureManagementSystem();
                app.setVisible(true);
            }
        });
    }
}

class Crop implements Serializable {
    private String name;
    private int quantity;
    private String farmerName;
    private String phoneNumber;

    public Crop(String name, int quantity, String farmerName, String phoneNumber) {
        this.name = name;
        this.quantity = quantity;
        this.farmerName = farmerName;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}