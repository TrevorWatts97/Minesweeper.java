package minesweeper;

import java.util.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  {@code SettingsDialog} implements a settings
 *  dialog box for the graphical frame laout
 *  @version 20211031600
 *  @author Trevor Watts
 */
public class SettingsDialog extends JDialog
{
	/*
	 *  JavaDoc complained that I needed comments
	 *  for every single instance variable in this class
	 */
	/**
	 *  Text box for size of field
	 */
	private JTextField		sizeTextBox;
	/**
	 *  Text box for bomb count in field
	 */
	private JTextField  	bombCountTextBox;
	/**
	 *  Jbutton for the ok actionlistener
	 */
	private JButton         okButton;
	/**
	 *  Jbutton for the cancel lamda event
	 */
	private JButton     	cancelButton;
	/**
	 *  Label that displays defaults
	 */
	private JLabel      	defaultsLabel;
	/**
	 *  Label that displays field size
	 */
	private JLabel      	fieldSizeLabel;
	/**
	 *  Label that displays bomb count
	 */
	private JLabel      	bombCountLabel;
	/**
	 *  new panel for settings menu
	 */
	JPanel          		panel;
	/**
	 *  Layout for settings menu
	 */
	GridBagLayout           layout;
	/**
	 *  Constraints for layout within settings menu
	 */
    GridBagConstraints      constraints;
    /**
	 *  int variable to take user input for size of field
	 */
	private int 			numSize;
	/**
	 *  int variable to take user input for bomb count
	 */
	private int 			numBombCount;

	private class OKaction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			numSize = Integer.parseInt(sizeTextBox.getText());
			numBombCount = Integer.parseInt
									  (bombCountTextBox.getText());

			if(numSize < 1){
				setValues();
				return;
			}
			else{
				Field.setDefaultSize(numSize);
				Field.setDefaultBombCount(numBombCount);
				setVisible(false);
			}
		}
	}

	/**
	 *  Sets default values into text fields
	 */
	public void setValues()
	{
		sizeTextBox.setText("" + Field.getDefaultSize());
		bombCountTextBox.setText("" + Field.getDefaultBombCount());
	}

	/**
	 *  Construct a new settings menu
     *  on our game panel.
     *  @param ourFrame passes through our initial game board
	 */
	public SettingsDialog(JFrame ourFrame)
	{
		/*
		 *  Initializing variables
		 */
		super(ourFrame, "Settings", true);
		panel = new JPanel();
		layout = new GridBagLayout();
        panel.setLayout(layout);

		/*
		 *	Create a JLabel named Defaults
		 *	and add it to the panel
		 */
		defaultsLabel = new JLabel("Defaults");
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(5,5,5,5);
		constraints.gridx = 0;
		constraints.gridwidth = 2;
		constraints.gridy = 0;
        panel.add(defaultsLabel, constraints);

		/*
		 *	Field Size JLabel
		 */
		fieldSizeLabel = new JLabel("Field Size");
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(5,5,5,5);
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(fieldSizeLabel, constraints);


		/*
		 *  Field size text box
		 */
		sizeTextBox = new JTextField("" +
									 Field.getDefaultSize(),2);
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(5,5,5,5);
		constraints.gridx = 0;
		constraints.gridy = 2;
		panel.add(sizeTextBox, constraints);

		/*
		 *  OK BUTTON
		 */
		okButton = new JButton("OK");
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(5,5,5,5);
		constraints.gridx = 0;
		constraints.gridy = 3;
		okButton.addActionListener(new OKaction());
		panel.add(okButton, constraints);

		/*
		 *  Bomb Count JLabel
		 */
		bombCountLabel = new JLabel("Bomb Count");
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(5,5,5,5);
		constraints.gridx = 1;
		constraints.gridy = 1;
		panel.add(bombCountLabel, constraints);


		/*
		 *  Bomb count text box
		 */
		bombCountTextBox = new JTextField("" +
										Field.getDefaultBombCount(),2);
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(5,5,5,5);
		constraints.gridx = 1;
		constraints.gridy = 2;
		panel.add(bombCountTextBox, constraints);

		/*
		 *  CANCEL BUTTON
		 */
		cancelButton = new JButton("Cancel");
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(5,5,5,5);
		constraints.gridx = 1;
		constraints.gridy = 3;
		cancelButton.addActionListener(event -> setVisible(false));
		panel.add(cancelButton, constraints);

		add(panel);

        pack();
	}
}