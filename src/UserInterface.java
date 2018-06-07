
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.postgis.PGbox3d;
import org.postgis.PGgeometry;
import org.postgis.Point;

import javax.swing.JSeparator;

public class UserInterface implements ActionListener {

	JLabel labelCreate;
	JLabel labelCreateOutput;
	JTextField textfieldFilePath;
	JButton buttonSave;

	JLabel labelQueryLLB;
	JLabel labelQueryURT;
	JLabel labelQueryOutput; 
	JTextField textfieldLLBX;//lower left bottom corner of the box as a Point object
	JButton buttonFindIntersecting;
	private JTextField textfieldLLBY;
	private JTextField textfieldLLBZ;
	private JTextField textfieldURTX;
	private JTextField textfieldURTY;
	private JTextField textfieldURTZ;

	UserInterface() {
		JFrame f = new JFrame("Database Interface");
		// submit button
		buttonSave = new JButton("Save Objects");
		buttonSave.setBounds(297, 88, 140, 40);
		// enter name label
		labelCreate = new JLabel();
		labelCreate.setText("Enter file path :");
		labelCreate.setBounds(20, 47, 100, 40);
		// empty label which will show event after button clicked
		labelCreateOutput = new JLabel();
		labelCreateOutput.setBounds(155, 131, 450, 40);
		// textfield to enter name
		textfieldFilePath = new JTextField();
		textfieldFilePath.setText("/Users/bhumi/Documents/Capstone/testFiles/MyMeshes0352.txt");
		textfieldFilePath.setBounds(155, 57, 450, 30);

		//for query 
		buttonFindIntersecting = new JButton("Find Intersecting Objects");
		buttonFindIntersecting.setBounds(222, 387, 236, 40);
		// enter name label
		labelQueryLLB = new JLabel();
		labelQueryLLB.setText("Enter Lower Left Bottom of BB :");
		labelQueryLLB.setBounds(20, 273, 207, 48);

		labelQueryURT = new JLabel();
		labelQueryURT.setText("Enter Upper Right Top of BB :");
		labelQueryURT.setBounds(20, 326, 200, 40);

		// empty label which will show event after button clicked
		labelQueryOutput = new JLabel();
		labelQueryOutput.setBounds(108, 439, 678, 32);
		// textfield to enter name
		textfieldLLBX = new JTextField();
		textfieldLLBX.setBounds(222, 283, 71, 30);

		// add to frame
		f.getContentPane().add(labelCreateOutput);
		f.getContentPane().add(textfieldFilePath);
		f.getContentPane().add(labelCreate);
		f.getContentPane().add(buttonSave);
		f.getContentPane().add(labelQueryLLB);
		f.getContentPane().add(textfieldLLBX);
		f.getContentPane().add(labelQueryURT);
		f.getContentPane().add(buttonFindIntersecting);
		f.getContentPane().add(labelQueryOutput);

		f.setSize(792, 526);
		f.getContentPane().setLayout(null);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 183, 792, 12);
		f.getContentPane().add(separator);

		textfieldLLBY = new JTextField();
		textfieldLLBY.setBounds(305, 283, 71, 30);
		f.getContentPane().add(textfieldLLBY);

		textfieldLLBZ = new JTextField();
		textfieldLLBZ.setBounds(388, 285, 71, 30);
		f.getContentPane().add(textfieldLLBZ);

		JLabel lblX = new JLabel("X");
		lblX.setBounds(245, 255, 24, 16);
		f.getContentPane().add(lblX);

		JLabel lblY = new JLabel("Y");
		lblY.setBounds(333, 255, 32, 16);
		f.getContentPane().add(lblY);

		JLabel lblZ = new JLabel("Z");
		lblZ.setBounds(420, 257, 32, 16);
		f.getContentPane().add(lblZ);

		textfieldURTX = new JTextField();
		textfieldURTX.setBounds(225, 333, 71, 30);
		f.getContentPane().add(textfieldURTX);

		textfieldURTY = new JTextField();
		textfieldURTY.setBounds(308, 333, 71, 30);
		f.getContentPane().add(textfieldURTY);

		textfieldURTZ = new JTextField();
		textfieldURTZ.setBounds(391, 333, 71, 30);
		f.getContentPane().add(textfieldURTZ);

		JLabel lblNewLabel = new JLabel("Find Intersecting Objects");
		lblNewLabel.setBounds(20, 207, 261, 16);
		f.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Store Objects into the Database");
		lblNewLabel_1.setBounds(20, 6, 290, 16);
		f.getContentPane().add(lblNewLabel_1);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// action listener
		buttonSave.addActionListener(this);
		buttonFindIntersecting.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(buttonSave)) {
			String filePath = textfieldFilePath.getText();
			if(filePath.isEmpty() || filePath.length()==0){
				labelCreateOutput.setText("Please enter a file path for the input file");
			}
			else{
				//				System.out.println(filePath);
				Preporcessing obj = new Preporcessing();			
				File file = new File(filePath);
				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(file));
				} catch (FileNotFoundException e1) {

					labelCreateOutput.setText("Path or File not valid");
				}

				String st = new String();
				StringBuffer metadata = new StringBuffer();
				try {

					while ((st = br.readLine()) != null) {
						if (st.length() != 0 && st.charAt(0) == 'v') {
							obj.storeCoordinates(st);
						} else if (st.length() != 0 && st.charAt(0) == 'f') {
							obj.mapVertexToCoord(st);
						} else if (st.length() != 0 && st.charAt(0) == 'o') {
							GenerateCSV.writeFile(metadata);
							obj.coordinateList = new ArrayList<String>();
							metadata = new StringBuffer();
						}
						metadata.append(st+"\\n");
					}
				} catch (IOException e1) {

					labelCreateOutput.setText("Path or File not valid");

				}
				// write last record
				GenerateCSV.writeFile(metadata);
				GenerateCSV.addFileTermination("\\.");

				try {
					labelCreateOutput.setText("Copy Started");
					WriteToDB.writeToTable();
					//					labelCreateOutput.setText("Copy Started");
					br.close();
				} catch (ClassNotFoundException | SQLException | IOException e1) {
					labelCreateOutput.setText("Path or File not valid");
				}
				labelCreateOutput.setText("Copy Successful");
			}
		}
		else if (e.getSource().equals(buttonFindIntersecting)) {
			SpatialQuery q = new SpatialQuery();
			PGbox3d inputbox = null;
			try{

				double x=Double.parseDouble(textfieldLLBX.getText());
				double y=Double.parseDouble(textfieldLLBY.getText());
				double z=Double.parseDouble(textfieldLLBZ.getText());

				double x1=Double.parseDouble(textfieldURTX.getText());
				double y1=Double.parseDouble(textfieldURTY.getText());
				double z1=Double.parseDouble(textfieldURTZ.getText());

				//			PGgeometry geom = new PGgeometry();

				inputbox = new PGbox3d(new Point(x,y,z), new Point(x1,y1,z1));
				long startTime = System.nanoTime();
				q.findIntersectingObjs(inputbox);
				long endTime = (System.nanoTime() - startTime) / 1000000;
				labelQueryOutput.setText("Time taken = "+ endTime +" millisecs      Intersecting objects are stored in file-"+ q.queryOutputFile);
			}
			catch (NumberFormatException ignore) {
				labelQueryOutput.setText("Invalid Input - please enter valid values for bounding box");
			} catch (ClassNotFoundException e1) {
				labelQueryOutput.setText("Error finding the intersection");
			}

		}
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new UserInterface();
			}
		});
	}
}