package minesweeper;

import java.util.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  {@code Graphical} is class that implements playing the game
 *  graphically.
 *  @version 2021031700
 *  @author Trevor Watts
 */

public class Graphical {
	/**
	 *  Creates the game to play
	 *  @param arg arguments to the playing field
	 */
    public static void main(String arg[])
    {
        EventQueue.invokeLater(() -> {
                FieldFrame      frame;

                frame = new FieldFrame();

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setTitle("Minesweeper");
                frame.setVisible(true);
            });
    }
}

/**
 *  {@code FieldFrame} is the class that contains the {@code JFrame}
 *  of the field.
 *  @version 2021031800
 *  @author Trevor Watts
 */
class FieldFrame extends JFrame {
    private JLabel      	elapsedSecondsLabel;
    private JButton     	startStopButton;
    private SettingsDialog	settingsDialog;
    private JButton			newGame;

	/**
	 *  Constructs the frame to layout the game
	 */
    public FieldFrame() {
        JPanel                  panel;
        JPanel					buttonPanel;
        GridBagLayout           layout;
        GridBagConstraints      constraints;
        JMenuBar				menuBar;
        JMenuItem				settings;

        /*
         *  Create a JPanel to hold our label, field and button.
         */
        panel = new JPanel();
        buttonPanel = new JPanel();
        layout = new GridBagLayout();
        panel.setLayout(layout);
        buttonPanel.setLayout(layout);

        /*
         *  Create a JLabel to display the elapsed seconds.
         */
        elapsedSecondsLabel = new JLabel("0 seconds");
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(elapsedSecondsLabel, constraints);

        /*
         *  Create a JButton to start and stop the clock.
         *  and to create a new game
         */
        startStopButton = new JButton("Start");
        newGame = new JButton("New Game");

        /*
         *  Add the JComponent that displays our field.
         */
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new FieldComponent(this, elapsedSecondsLabel,
                                     startStopButton, newGame),
                  constraints);

        /*
         *	Add button panel to the bottom of panel
         */
        constraints = new GridBagConstraints();
		constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(buttonPanel, constraints);

		/*
		 *	Constraints to the buttonPanel
		 */
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0,0,0,5);
        buttonPanel.add(startStopButton, constraints);

        constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.insets = new Insets(0,5,0,0);
        buttonPanel.add(newGame, constraints);

        add(panel);

		/*
		 *	add settings menu to the panel
		 */
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        settings = new JMenuItem("Settings");
        settings.addActionListener(event -> {
				if(settingsDialog == null) {
					settingsDialog = new SettingsDialog(this);

					settingsDialog.setLocationRelativeTo(this);
				}

				settingsDialog.setVisible(true);
			});
		menuBar.add(settings);

        pack();

        /*
         *  Center the frame in the screen.
         */
        setLocationRelativeTo(null);
    }
}

/**
 *  {@code FieldComponent} is the class that contains the
 *  {@code JComponent} of the field.
 *  @version 2021032100
 *  @author Trevor Watts
 */
class FieldComponent extends JComponent {
    private Field       field;
    private Font        monospaceFont;
    private java.util.Timer     tickTimer;
    private JButton     startStopButton;
    private boolean     paused;
    private boolean     gameOver;
    private int         elapsedSeconds;
    private JLabel      elapsedSecondsLabel;
    private JButton		newGame;
	private JButton     buttonField[][];

    /*
     *  This class is so our timer has something to do.
     */
    private class Tick extends TimerTask
    {
        public void run()
        {
            if ((paused != false) || (gameOver != false)) {
                return;
            }

            ++elapsedSeconds;
            elapsedSecondsLabel.setText("" + elapsedSeconds +
                                        " seconds");
            elapsedSecondsLabel.repaint();
        }
    }

    /*
     *	creates a new instance of the game
     */
    private void newGame(FieldFrame ourFrame)
    {
		int     size;
		int     row;

		/*
		 *  Assume the game is not over
		 *  and reinitialize variables back to default.
		 */
		paused = true;
		startStopButton.setText("Start");
		gameOver = false;
		elapsedSeconds = 0;
		elapsedSecondsLabel.setText("0 seconds");

		/*
		 *  Create a field.
		 */
		field = new Field();
		removeAll();
		/*
		 *  Figure out how big the field is, layout the grid for
		 *  buttons and allocate the array that holds the buttons.
		 */
		size = field.getSize();
		setLayout(new GridLayout(size, size));
		buttonField = new JButton[size][size];
		/*
		 *  Instantiate a button for each cell.
		 */
		for (row = 0; (row < size); ++row) {
			int         column;

			for (column = 0; (column < size); ++column) {
				char    displayChar;

				/*
				 *  Figure out what should be displayed for
				 *  this cell.
				 */
				displayChar = field.getType(row, column).charAt(0);
				if ((displayChar == '0') ||
					(displayChar == Cell.unmark.charAt(0))) {
					/*
					 *  Make cells with no adjacent bombs and
					 *  unmarked cells blank.
					 */
					displayChar = ' ';
				}
				/*
				 *  Instantiate and configure this button.
				 */
				buttonField[row][column] =
								new JButton("" + displayChar);
				buttonField[row][column].
					   addMouseListener(new MouseHandler(row, column));
				buttonField[row][column].setFont(monospaceFont);
				add(buttonField[row][column]);
			}
        }
		ourFrame.pack();
	}

    /*
     *  Start ticking.
     */
    private void startTicks()
    {
        if (paused == false) {
            /*
             *  We're already ticking.
             */
            return;
        }

        startStopButton.setText("Pause");
        paused = false;
    }

    /*
     *  Stop ticking.
     */
    private void stopTicks()
    {
        if (paused == true) {
            /*
             *  We're not ticking now.
             */
            return;
        }

        startStopButton.setText("Resume");
        paused = true;
    }

    /*
     *  This is the class to toggle the start/stop button.
     */
    private class StartStopAction implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            if (gameOver == true) {
                return;
            }

            if (paused == true) {
                startTicks();
            } else {
                stopTicks();
            }
        }
    }

    /*
     *  This class is to catch window events.
     */
    private class FrameActive extends WindowAdapter {
        private boolean         pausedWhenDeactivated   = true;

        public void windowActivated(WindowEvent event)
        {
            if (paused == false) {
                /*
                 *  We're already ticking.
                 */
                return;
            }

            if (pausedWhenDeactivated == false) {
                /*
                 *  We were running when deactivated.  So, now
                 *  that we activated, restart the ticks.
                 */
                startTicks();
            }
        }

        public void windowDeactivated(WindowEvent event)
        {
            /*
             *  Remember whether we were stopped when deactivated.
             */
            pausedWhenDeactivated = paused;

            if (paused == true) {
                /*
                 *  We're not ticking now.
                 */
                return;
            }

            stopTicks();
        }
    }

    /*
     *  This is the class that manages a button.
     */
    private class MouseHandler extends MouseAdapter
    {
        private int     row;
        private int     column;
        private int     whichMark;

        public MouseHandler(int row, int column)
        {
            /*
             *  Remember what button we're associated with.
             */
            this.row = row;
            this.column = column;
        }

        /*
         *  What to do when the mouse buttons are clicked.
         */
        public void mouseClicked(MouseEvent event)
        {
            if (gameOver == true) {
                return;
            }

            if (event.getButton() == MouseEvent.BUTTON1) {
                String  status;

                /*
                 *  Left button pressed.  Expose the associated
                 *  cell.  The method tells us if the game is
                 *  over.
                 */
                gameOver = gameOver || field.expose(row, column);
                if ((status = field.getStatus()) != null) {
                    if (status.startsWith("Boom") == true) {
                        /*
                         *  The player exposed a bomb.
                         */
                        buttonField[row][column].
                                        setBackground(Color.RED);
                    }
                }
            } else if (whichMark == 0) {
                /*
                 *  Right button pressed.
                 *  The cell has no mark so flag it.
                 */
                field.flag(row, column);
                whichMark = 1;
            } else if (whichMark == 1) {
                /*
                 *  Right button pressed.
                 *  The cell is flagged so mark it.
                 */
                field.mark(row, column);
                whichMark = 2;
            } else if (whichMark == 2) {
                /*
                 *  Right button pressed.
                 *  The cell is marked so clear all marks.
                 */
                field.clearMark(row, column);
                whichMark = 0;
            }

            startTicks();
            /*
             *  Tell Swing we changed something.
             */
            repaint();
        }
    }

    /**
     *  Construct the Component that contains and manages
     *  the game.
     *  @param ourFrame our main display for the game
     *  @param elapsedSecondsLabel label to display timer
     *  @param startStopButton button to start and stop timer
     *  @param newGame button to create a new game based off
     *  settings menu
     */
    public FieldComponent(FieldFrame ourFrame,
                          JLabel elapsedSecondsLabel,
                          JButton startStopButton,
                          JButton newGame)
    {
        int     size;
        int     row;

        /*
         *  Some things to remember.
         */
        this.elapsedSecondsLabel = elapsedSecondsLabel;
        this.startStopButton = startStopButton;
        this.newGame = newGame;

        /*
         *  Assume the game is not over.
         */
        paused = true;
        gameOver = false;

        /*
         *  Create a font we'll use for our field.
         */
        monospaceFont = new Font("Monospaced", Font.BOLD, 18);

        /*
         *  Get a timer to keep track of elapsed time.
         */
        tickTimer = new java.util.Timer();
        tickTimer.schedule(new Tick(), 1000, 1000);

        /*
         *  Create an action for our start/stop button and
         *  register it.
         */
        startStopButton.addActionListener(new StartStopAction());

        /*
         *  Creates an action for the newGame button
         */
        newGame.addActionListener(event -> newGame(ourFrame));

        /*
         *  Listen for window events.
         */
        ourFrame.addWindowListener(new FrameActive());

        /*
         *  Create a field.
         */
        field = new Field();
        /*
         *  Figure out how big the field is, layout the grid for
         *  buttons and allocate the array that holds the buttons.
         */
        size = field.getSize();
        setLayout(new GridLayout(size, size));
        buttonField = new JButton[size][size];
        /*
         *  Instantiate a button for each cell.
         */
        for (row = 0; (row < size); ++row) {
            int         column;

            for (column = 0; (column < size); ++column) {
                char    displayChar;

                /*
                 *  Figure out what should be displayed for
                 *  this cell.
                 */
                displayChar = field.getType(row, column).charAt(0);
                if ((displayChar == '0') ||
                    (displayChar == Cell.unmark.charAt(0))) {
                    /*
                     *  Make cells with no adjacent bombs and
                     *  unmarked cells blank.
                     */
                    displayChar = ' ';
                }
                /*
                 *  Instantiate and configure this button.
                 */
                buttonField[row][column] =
                                new JButton("" + displayChar);
                buttonField[row][column].
                       addMouseListener(new MouseHandler(row, column));
                buttonField[row][column].setFont(monospaceFont);
                add(buttonField[row][column]);
            }
        }
        newGame(ourFrame);
    }

    /**
     *  Provide Swing a way to redraw our playing field.
     */
    public void paintComponent(Graphics graphics)
    {
        int     size;
        int     row;
        Font    originalFont;

        /*
         *  Remember the font we were given and install the font
         *  we want for the game.
         */
        originalFont = graphics.getFont();
        graphics.setFont(monospaceFont);

        if (gameOver == true) {
            /*
             *  When the game is over expose everything.
             */
            field.exposeAll();
        }

        /*
         *  Figure out what should be displayed for
         *  this cell.
         */
        size = field.getSize();
        for (row = 0; (row < size); ++row) {
            int         column;

            for (column = 0; (column < size); ++column) {
                char    displayChar;

                if (gameOver == true) {
                    /*
                     *  When the game's over disable the button.
                     */
                    buttonField[row][column].setEnabled(false);
                }

                displayChar = field.getType(row, column).charAt(0);
                if (displayChar == Cell.unmark.charAt(0)) {
                    /*
                     *  Change unmarks to blanks.
                     */
                    displayChar = ' ';
                } else if (Character.isDigit(displayChar) == true) {
                    /*
                     *  If we're displaying a digit, the cell
                     *  must be exposed so disable the button.
                     */
                    buttonField[row][column].setEnabled(false);
                    if (displayChar == '0') {
                        /*
                         *  Change cells with no adjacent bombs
                         *  to blank.
                         */
                        displayChar = ' ';
                    }
                }
                buttonField[row][column].setText("" + displayChar);
                buttonField[row][column].setFocusPainted(false);
                startStopButton.setFocusPainted(false);
                newGame.setFocusPainted(false);
            }
        }

        /*
         *  Restore the original font.
         */
        graphics.setFont(originalFont);
    }
}
