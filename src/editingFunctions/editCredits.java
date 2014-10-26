package editingFunctions;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;
import javax.swing.JComboBox;


public class editCredits {

	private JButton _jbPreview;
	private JButton _jbChoose;
	private JButton _jbCreditsPage;
	private JTextField _userInfo;
	private JTextField _creditsName;
	private JTextField _mp4Choice;
	private JTextField _creditsText;
	private JTextField _mp4Display;
	private JTextField _fontSelection;
	private JTextField _fontSizeSelection;
	private JTextField _creditsTextInformer;
	private JTextField _creditsTextRaw;
	private JComboBox fonts;
	private JComboBox _fontSize;
	private String _fontName;
	private String _fontSizeString;
	private String _drawText = " -vf drawtext=\"fontfile=/usr/share/fonts/truetype/freefont/Free";
	private String _drawTextVideo = " -strict experimental -vf drawtext=\"fontfile=/usr/share/fonts/truetype/freefont/Free";
	private String _drawTextVideo2 = ".ttf: text='";
	private String _drawTextVideo3 = "':x=(w-text_w)/2:y=150: fontsize=";
	private String _drawTextVideo4;
	private String _drawText2 = ".ttf: text=";
	private String _newFile;
	protected File _creditsInputFile;	
	private String _avconvFontSize;
	private String _outputName;
	private String _duration;
	private Object _selectedTitleText;
	private File _previewPicture;
	private int _exitStatus;
	SpringLayout _layout = new SpringLayout();

	//method to insert credits tab into JTabbedPane
	public void insertCreditPageTab(final JTabbedPane pane){
		JPanel creditPagePanel = new JPanel();
		pane.addTab("Create Credits Page", creditPagePanel);
		setCreditsPageFeatures(creditPagePanel);
		_fontName = "Sans";
		_fontSizeString = "46";
	}

	//Set out the features in the credits JPanel
	private void setCreditsPageFeatures(final JPanel panel){	
		panel.setLayout(_layout);
		addVideoChooser(panel);
		addPanelFeatures(panel);
		addFontOptions(panel);
		addFontSizeOptions(panel);

		_jbPreview = new JButton("Show Preview");
		_jbCreditsPage = new JButton("Set Credits Page");
		panel.add(_jbCreditsPage);
		panel.add(_jbPreview);


		//move the text box containing user file choice button to its location
		_layout.putConstraint(SpringLayout.NORTH, _jbPreview, 130, SpringLayout.NORTH, _mp4Choice);
		_layout.putConstraint(SpringLayout.WEST, _jbPreview, 15, SpringLayout.WEST, panel);
		_layout.putConstraint(SpringLayout.NORTH, _jbCreditsPage, 130, SpringLayout.NORTH, _mp4Choice);
		_layout.putConstraint(SpringLayout.WEST, _jbCreditsPage, 160, SpringLayout.WEST, panel);
		_layout.putConstraint(SpringLayout.NORTH,fonts,77,SpringLayout.NORTH,_mp4Choice);
		_layout.putConstraint(SpringLayout.WEST,fonts,105,SpringLayout.WEST,panel);
		_layout.putConstraint(SpringLayout.NORTH,_fontSelection,80,SpringLayout.NORTH,_mp4Choice);
		_layout.putConstraint(SpringLayout.WEST, _fontSelection, 15, SpringLayout.WEST, panel);
		_layout.putConstraint(SpringLayout.NORTH,_fontSize,77,SpringLayout.NORTH,_mp4Choice);
		_layout.putConstraint(SpringLayout.WEST, _fontSize, 300, SpringLayout.WEST, panel);
		_layout.putConstraint(SpringLayout.NORTH, _fontSizeSelection, 80, SpringLayout.NORTH, _mp4Choice);
		_layout.putConstraint(SpringLayout.WEST,_fontSizeSelection,187,SpringLayout.WEST,panel);



		_jbPreview.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getCreditsScreenshotSW creditsScreenShotSW = new getCreditsScreenshotSW();
				try {
					creditsScreenShotSW.execute();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}

		});

		_jbCreditsPage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addTextCreditsSW atcSW = new addTextCreditsSW();
				try {
					atcSW.execute();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}//end action performed
		}); // end add action listener
	}

	//Method to add font options
	private void addFontOptions(JPanel panel){
		String[] fontOptions = {"Sans","Mono","Serif"};
		fonts = new JComboBox(fontOptions);
		panel.add(fonts);
		_fontSelection = new JTextField();
		_fontSelection.setText("Select Font: ");
		_fontSelection.setEditable(false);
		panel.add(_fontSelection);
		fonts.setSelectedIndex(0);
		fonts.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				_fontName = (String)cb.getSelectedItem();
			}

		});

	}

	//Method to get the duration of the mp4
	private void getDuration() throws IOException{
		String file2 = _creditsInputFile.getAbsolutePath();

		//bash command from http://askubuntu.com/questions/224237/how-to-check-how-long-a-video-mp4-is-using-the-shell
		String command = "avconv -i " + file2 + " 2>&1 | grep Duration | cut -d ' ' -f 4 | sed s/,//";
		ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c",command);

		Process process = builder.start();

		//redirects input and error streams
		InputStream stdout = process.getInputStream();
		InputStream stderr = process.getErrorStream();
		BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
		String line = null;
		StringBuilder sb = new StringBuilder();

		//sends the dot progress bar to the process function for processing
		while ((line = stdoutBuffered.readLine()) != null ) {	
			sb.append(line);
		}
		String duration = sb.toString();
		String[] strArr = duration.split(":");
		int hours = Integer.parseInt(strArr[0]);
		int minutes = Integer.parseInt(strArr[1]);
		Double seconds = Double.parseDouble(strArr[2]);
		seconds = (hours * 60.0 * 60.0) + (minutes * 60.0 + seconds);
		seconds = seconds - 10;
		int time = (int) seconds.doubleValue();

		StringBuilder strb = new StringBuilder();
		strb.append("");
		strb.append(time);
		String now = strb.toString();
		_duration = now;
	}


	// Method to add font size options
	private void addFontSizeOptions(JPanel panel){
		String[] fontSizeOptions = {"28","36","40","46","52","58","64"};
		_fontSize = new JComboBox(fontSizeOptions);
		panel.add(_fontSize);
		_fontSizeSelection = new JTextField();
		_fontSizeSelection.setText("Select Font Size: ");
		_fontSizeSelection.setEditable(false);
		panel.add(_fontSizeSelection);
		_fontSize.setSelectedIndex(3);
		_fontSize.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cbSize = (JComboBox)e.getSource();
				_fontSizeString = (String)cbSize.getSelectedItem();

			}

		});


	}

	// Swingworker that adds text as credits to mp4
	private class addTextCreditsSW extends SwingWorker<Integer,String>{

		@Override
		protected Integer doInBackground() throws Exception {

			getDuration();
			_drawTextVideo4 = ": draw='lt("+_duration+",t)'\"";
			_outputName = _creditsName.getText() +".mp4";
			String file2 = _creditsInputFile.getAbsolutePath();
			_selectedTitleText = _creditsTextRaw.getText();
			String command = "avconv -i " + file2 + _drawTextVideo + _fontName + _drawTextVideo2 + _selectedTitleText + _drawTextVideo3 +_fontSizeString+ _drawTextVideo4 + " " + _outputName;
			//avconv -i ay.mp4 -strict experimental -vf "drawtext=fontfile=/usr/share/fonts/truetype/freefont/FreeSans.ttf: text='TESTING': fontsize=58: draw='lt(t,10)'" output.mp4
			ProcessBuilder builder3 = new ProcessBuilder("/bin/bash","-c",command);
			_userInfo.setText("Adding Credits...");
			Process process3 = builder3.start();
			_exitStatus = process3.waitFor();
			return null;
		}

	}


	// Swingworker that gets a screenshot of mp4
	private class getCreditsScreenshotSW extends SwingWorker<Integer,String>{



		protected void done(){
			if (_exitStatus == 0){
				addTextSW atSW = new addTextSW();
				try {
					atSW.execute();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				_userInfo.setText("Preview Failed");
			}


		}


		@Override
		protected Integer doInBackground() throws Exception {
			_avconvFontSize = ":x=(w-text_w)/2:y=150: fontsize="+_fontSizeString;
			_selectedTitleText = _creditsTextRaw.getText();
			String file = _creditsInputFile.getAbsolutePath();
			String[] cmd = {"avconv","-i",file,"-vframes","1","-an","-s","800x444","-ss","30","CreditsScreenShot.jpg"};
			ProcessBuilder builder = new ProcessBuilder(cmd);
			Process process = builder.start();
			_exitStatus = process.waitFor();
			_previewPicture = new File("CreditsScreenShot.jpg");
			return null;

		}


	}

	// Swingworker that adds text to the screenshot for preview
	protected class addTextSW extends SwingWorker<Integer,String>{

		protected void done(){
			
			if (_exitStatus == 0){
			
			
			Desktop test = Desktop.getDesktop();
			File previewPicture = new File("previewCredits.jpg");
			try {
				test.open(previewPicture);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			_userInfo.setText("Showing Preview");
			}
			else {
				_userInfo.setText("Preview Failed");
			}
		}

		@Override
		protected Integer doInBackground() throws Exception {
			_newFile = _previewPicture.getAbsolutePath();
			String cmd2 = "avconv -i " + _newFile + _drawText + _fontName + _drawText2+_selectedTitleText+"'"+_avconvFontSize+"\" " + "previewCredits.jpg";
			ProcessBuilder builder2 = new ProcessBuilder("/bin/bash","-c",cmd2);
			Process process2 = builder2.start();
			_exitStatus = process2.waitFor();
			return null;
		}

	}

	// Adds panel features
	private void addPanelFeatures(final JPanel panel){
		//add text box requesting user to input an output name
		_creditsText = new JTextField();
		_creditsText.setText("Output file name :");
		_creditsText.setPreferredSize(new Dimension(115,20));
		_creditsText.setEditable(false);
		panel.add(_creditsText);
		//move the title text to its location on the screen
		_layout.putConstraint(SpringLayout.WEST, _creditsText, 15, SpringLayout.WEST, panel);
		_layout.putConstraint(SpringLayout.NORTH, _creditsText, 42, SpringLayout.NORTH, panel);

		//add the user input box for the title of the output file
		_creditsName = new JTextField();
		_creditsName.setPreferredSize(new Dimension(115,20));
		panel.add(_creditsName);
		_layout.putConstraint(SpringLayout.WEST, _creditsName, 125, SpringLayout.WEST, _creditsText);
		_layout.putConstraint(SpringLayout.NORTH, _creditsName, 42, SpringLayout.NORTH, panel);


		_mp4Display = new JTextField();
		_mp4Display.setText(".mp4");
		_mp4Display.setEditable(false);
		_creditsName.setPreferredSize(new Dimension(115,20));
		panel.add(_mp4Display);
		//move the choose file button to its location
		_layout.putConstraint(SpringLayout.WEST, _mp4Display, 115, SpringLayout.WEST, _creditsName);
		_layout.putConstraint(SpringLayout.NORTH, _mp4Display, 42, SpringLayout.NORTH, panel);

		//add textbox for error messages to be conveyed to the user
		_userInfo = new JTextField();
		_userInfo.setPreferredSize(new Dimension(275,20));
		_userInfo.setEditable(false);

		//move the text box containing user file choice button to its location
		_layout.putConstraint(SpringLayout.WEST, _userInfo, 15, SpringLayout.WEST, panel);
		_layout.putConstraint(SpringLayout.NORTH, _userInfo, 117, SpringLayout.NORTH, panel);
		panel.add(_userInfo);

		_creditsTextInformer = new JTextField();
		_creditsTextInformer.setText("Please specify text to add to video: ");
		_creditsTextInformer.setEditable(false);
		_layout.putConstraint(SpringLayout.NORTH, _creditsTextInformer, 68,SpringLayout.NORTH, panel);
		_layout.putConstraint(SpringLayout.WEST, _creditsTextInformer, 15, SpringLayout.WEST, panel);
		panel.add(_creditsTextInformer);

		_creditsTextRaw = new JTextField();
		_creditsTextRaw.setPreferredSize(new Dimension (300,20));
		panel.add(_creditsTextRaw);
		_layout.putConstraint(SpringLayout.NORTH, _creditsTextRaw, 68,SpringLayout.NORTH, panel);
		_layout.putConstraint(SpringLayout.WEST, _creditsTextRaw, 240, SpringLayout.WEST, _jbChoose);

	}

	// Metho to allow choosing of files
	private void addVideoChooser(final JPanel panel){
		//create and add functionality for file choosing button
		_jbChoose = new JButton("Choose File");
		_jbChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Opens JFileChooser when button pressed
				JFileChooser jfile = new JFileChooser();
				int response = jfile.showOpenDialog(null);
				if (response == JFileChooser.APPROVE_OPTION) {
					_creditsInputFile = jfile.getSelectedFile();
					String chosenFile = jfile.getSelectedFile().toString();
					_mp4Choice.setText(chosenFile);
				}
				jfile.setVisible(true);
			}
		});
		panel.add(_jbChoose);


		//move the choose file button to its location
		_layout.putConstraint(SpringLayout.WEST, _jbChoose, 15, SpringLayout.WEST, panel);
		_layout.putConstraint(SpringLayout.NORTH, _jbChoose, 10, SpringLayout.NORTH, panel);

		//creates text field to store which file the user input
		_mp4Choice = new JTextField();
		_mp4Choice.setPreferredSize(new Dimension(150,20));
		_mp4Choice.setEditable(false);
		panel.add(_mp4Choice);

		//move the text box containing user file choice button to its location
		_layout.putConstraint(SpringLayout.WEST, _mp4Choice, 125, SpringLayout.WEST, _jbChoose);
		_layout.putConstraint(SpringLayout.NORTH, _mp4Choice, 12, SpringLayout.NORTH, panel);



	}
}

