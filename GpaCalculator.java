/*
 * John Kim, jfk2dd
 * I have received lots of help online* in order to understand 
 * JTable and JScrollPane. I have also used some of the GUI from
 * in class (CS 2110) examples as reference to look at.
 * 
 * (*online references include Java's API's of JTable, DefaultTableModel,
 * JScrollPane, and tutorials of those on JAVA Oracle, and on YouTube.
 * StackExchanged was also used for information on using the DefaultTableModel
 * as the parameter for JTable)
 * 
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.util.*;

public class GpaCalculator {
	
	private JFrame frameCourses; // frame for courses
	private JFrame frameSummary; // frame for summary
	
	private JPanel tablePanel; // table panel
	private JPanel courseInputs; // panel for inputs and add
	private JPanel miscItems; // panel for remove, target gpa, and add
	private JPanel summaryPanel; // panel for summary
	
	private JTextField optCourse; // to input optional course name
	private JTextField targetGPA; // to input target gpa
	
	private JLabel currentGPA; // to show user the calculated current gpa
	private JLabel targetGPAValue; // to show inputted target gpa for summary
	private JLabel requiredGPALabel; //req GPA Label
	
	private JTable display; // to display all courses
	private DefaultTableModel tableMod; // for JTable display
	
	private JButton switchSumToCour; //button to show cour
	private JButton switchCourToSum; // button to show sum
	private JButton add; //adds course to the display
	private JButton clearAll; //clears, or refreshes, the classes when pressed
	private JButton remove; //removes the course from JComboBox courses when pressed
	private JButton addFifBlank; //adds 15 blank
	
	private JComboBox<String> credHours; //to input credit hours
	private JComboBox<String> optGrade; // to input optional grade
	
	private float gpaAvg; // gpa in float for currentGPA
	private String targetGPAStr; // getText to string to store in variable
	private float requiredGPA; // displays gpa needed to reach target gpa from current gpa
	
	
	// constructor to build the GUI
	public GpaCalculator() {
		
		frameCourses = new JFrame();
		frameSummary = new JFrame();
		
		frameCourses.setTitle("GPA Calculator");
		frameSummary.setTitle("Summary");
		
		frameCourses.setBounds(550, 100, 300, 500);
		frameSummary.setBounds(550, 100, 300, 500);
		
		GridLayout frameGrid = new GridLayout(3,0);
		frameCourses.setLayout(frameGrid);
		
		frameCourses.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameSummary.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		buildTablePanel();
		buildPanelCourses();
		buildPanelMisc();
		
		courseInputs.setLayout(new GridLayout(4,0));
		summaryPanel.setLayout(new GridLayout(4,0));
		
		frameCourses.add(tablePanel);
		frameCourses.add(courseInputs);
		frameCourses.add(miscItems);
		frameSummary.add(summaryPanel);
		
		frameCourses.setVisible(true);
		frameSummary.setVisible(false);
	}
	
	//makes the courses panel for courses frame
	private void buildPanelCourses() {
		// options for JComboBox
		String[] hours = {"0", "1", "2", "3", "4", "5"};
		String[] grades = {null,  "4.0", "3.7", "3.3", "3.0", "2.7", "2.3", "2.0", "1.7", "1.3", "1.0", "0.7", "0.0"}; 
		
		// making elements for courseInput
		JLabel credHrLabel = new JLabel("Select Credit Hours: ");
		JLabel optCrseLabel = new JLabel("Enter Course Name: ");
		JLabel optGradeLabel = new JLabel("Select Grade: ");
		this.credHours = new JComboBox<String>(hours);
		this.optCourse = new JTextField(20);
		this.optGrade = new JComboBox<String>(grades);
		this.add = new JButton("add");
		
		// this button adds a row to the table depending on the user's inputs
		this.add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (optCourse.getText().length() > 0 && optGrade.getSelectedItem() != null) {
					Object[] newCourse = {credHours.getSelectedItem(), optCourse.getText(), optGrade.getSelectedItem()};
					tableMod.addRow(newCourse);
				}
				else if (optCourse.getText().length() > 0) {
					Object[] newCourse = {credHours.getSelectedItem(), optCourse.getText(), null};
					tableMod.addRow(newCourse);
				}
				else if (optGrade.getSelectedItem() != null) {
					Object[] newCourse = {credHours.getSelectedItem(), null, optGrade.getSelectedItem()};
					tableMod.addRow(newCourse);
				}
				else {
					Object[] newCourse = {credHours.getSelectedItem(), null, null};
					tableMod.addRow(newCourse);
				}
				credHours.setSelectedIndex(0);
				optCourse.setText("");
				optGrade.setSelectedIndex(0);
				frameCourses.validate();
				frameCourses.repaint();
				frameSummary.validate();
				frameSummary.repaint();
			}
				
		});
		
		//panel adding
		this.courseInputs = new JPanel();
		this.courseInputs.add(credHrLabel);
		this.courseInputs.add(this.credHours);
		this.courseInputs.add(optCrseLabel);
		this.courseInputs.add(this.optCourse);
		this.courseInputs.add(optGradeLabel);
		this.courseInputs.add(this.optGrade);
		this.courseInputs.add(new JLabel("")); // this is used to put the add button on the right side
		this.courseInputs.add(this.add);
	}
	
	//makes the misc panel for courses frame
	private void buildPanelMisc() {
		// making elements for miscInputs
		JLabel targetGPALabel = new JLabel("Enter Target GPA: ");
		addFifBlank = new JButton("Add Fifteen Blank Credits");
		targetGPA = new JTextField(20);
		remove = new JButton("Remove Selected");
		clearAll = new JButton("Clear All");
		switchCourToSum = new JButton("Results");
		
		//built panel in buildPanel2() so the calculations would work
		buildPanelSummary();
		
		// button action listeners
		//adds 15 blank when pressed
		addFifBlank.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Object[] blank = {3, null, null};
				for (int i = 0; i < 5; i++) {
					tableMod.addRow(blank);
				}
				frameCourses.validate();
				frameCourses.repaint();
				frameSummary.validate();
				frameSummary.repaint();
			}
		});
		//removes selected/clicked row from table
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				tableMod.removeRow(display.getSelectedRow());
				frameCourses.validate();
				frameSummary.repaint();
				frameSummary.validate();
				frameSummary.repaint();
			}
		});
		//clear all button
		clearAll.addActionListener(new ActionListener() {
			/* the purpose of having all these for loops
			 * is because from having 1 to 10 rows, only half were removed
			 * adding a second one ensures up to 9 rows are removed
			 * but doesn't allow anything from 10-20 to be removed
			 * 
			 * There are 5 for loops because i don't expect people
			 * to take more that 50 classes total in uva
			 * considering one can take a minimum of 120 credits
			 * and those credits should mostly be 3 credits
			 * 
			 */
			@Override
			public void actionPerformed(ActionEvent event) {
				for (int i = 0; i < tableMod.getRowCount(); i++) {
					tableMod.removeRow(0);
				}
				for (int i = 0; i < tableMod.getRowCount(); i++) {
					tableMod.removeRow(0);
				}
				for (int i = 0; i < tableMod.getRowCount(); i++) {
					tableMod.removeRow(0);
				}
				for (int i = 0; i < tableMod.getRowCount(); i++) {
					tableMod.removeRow(0);
				}
				for (int i = 0; i < tableMod.getRowCount(); i++) {
					tableMod.removeRow(0);
				}
				// just in case there is an extra
				if (tableMod.getRowCount() > 0) {
					tableMod.removeRow(0);
				}
				frameCourses.validate();
				frameCourses.repaint();
				frameSummary.validate();
				frameSummary.repaint();
			}
		});
		
		// button to switch between cour and sum. this is also like a "calculate" button for summary for instant feedback
		switchCourToSum.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				targetGPAStr = targetGPA.getText();
				
				// for calculating current GPA
				float gpaTotal = 0;
				int totalCred = 0;
				int totalEmptyCred = 0;
				for (int i = 0; i < tableMod.getRowCount(); i++) {
					if (tableMod.getValueAt(i, 2) != null) {
						float gpa = Float.parseFloat(tableMod.getValueAt(i, 2).toString());
						int cred = Integer.parseInt(tableMod.getValueAt(i, 0).toString());
						gpaTotal = gpaTotal + (gpa * cred);
					}
				}
				for (int j = 0; j < tableMod.getRowCount(); j++) {
					if (tableMod.getValueAt(j, 2) != null) {
						totalCred = totalCred + Integer.parseInt(tableMod.getValueAt(j, 0).toString());
					}
					else {
						totalEmptyCred += Integer.parseInt(tableMod.getValueAt(j, 0).toString());
					}
				}
				
				
				gpaAvg = (gpaTotal / totalCred);
				
				// for calculating required gpa
				float gpaTarget = 0;
				if (targetGPA.getText().length() > 0 ) {
					gpaTarget = Float.parseFloat(targetGPAStr);
					requiredGPA = (((gpaTarget*(totalCred + totalEmptyCred)) - gpaTotal) / totalEmptyCred);
				}
				else {
					requiredGPA = 0; // in case if they didn't input a target gpa
				}
				
				if (requiredGPA > 4.0) {
					JOptionPane.showMessageDialog(null, "The required gpa is higher than 4.0. Go back and try adding more credits and recalculating.");
				}
				if (requiredGPA < 2.0 && requiredGPA != 0) {
					JOptionPane.showMessageDialog(null, "Your required gpa is less than 2.0. You can take less credits if you wish.");
				}
				
				currentGPA.setText("Current GPA:" + gpaAvg);
				targetGPAValue.setText("Target GPA: " + targetGPAStr);
				requiredGPALabel.setText("Required GPA: " + requiredGPA + " average for " + totalEmptyCred + " credits.");
				
				frameCourses.validate();
				frameCourses.repaint();
				frameSummary.validate();
				frameSummary.repaint();
				frameCourses.setVisible(false);
				frameSummary.setVisible(true);
			}
		});
		
		//adding to panel
		miscItems = new JPanel();
		miscItems.add(targetGPALabel);
		miscItems.add(targetGPA);
		miscItems.add(addFifBlank);
		miscItems.add(remove);
		miscItems.add(clearAll);
		miscItems.add(switchCourToSum);
	}
	
	// a panel for the summary
	public void buildPanelSummary() {
		summaryPanel = new JPanel();
		
		currentGPA = new JLabel("Current GPA: " + this.gpaAvg);
		targetGPAValue = new JLabel("Target GPA: " + this.targetGPA.getText());
		requiredGPALabel = new JLabel("Required GPA: " + this.requiredGPA);
		switchSumToCour = new JButton("Back to Editing");
		
		// this button switches between summary to course
		switchSumToCour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frameCourses.validate();
				frameCourses.repaint();
				frameSummary.validate();
				frameSummary.repaint();
				frameSummary.setVisible(false);
				frameCourses.setVisible(true);
			}
		});
		
		summaryPanel.add(currentGPA);
		summaryPanel.add(targetGPAValue);
		summaryPanel.add(requiredGPALabel);
		summaryPanel.add(switchSumToCour);
		
		
	}
	
	//building table
	public void buildTablePanel() {
		tablePanel = new JPanel();
		tableMod = new DefaultTableModel(new Object[]{"Credits", "Courses", "Grade"},0);
		display = new JTable(tableMod);
		JScrollPane scrollPane = new JScrollPane(display);
		
		display.setFillsViewportHeight(true);
		display.setPreferredScrollableViewportSize(new Dimension(250, 120));

		tablePanel.add(scrollPane, BorderLayout.CENTER);
		
	}	
	
	public static void main(String[] args) {
		new GpaCalculator();
	}
}