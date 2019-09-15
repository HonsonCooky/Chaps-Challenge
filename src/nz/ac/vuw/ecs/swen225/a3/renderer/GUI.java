package nz.ac.vuw.ecs.swen225.a3.renderer;

import nz.ac.vuw.ecs.swen225.a3.application.ChapsChallenge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * GUI class extends JFrame and is responsible with
 * maintain the Graphical Interface.
 */
public class GUI extends JFrame implements ComponentListener {
  // Nothing important
  private static final long serialVersionUID = 1L;


  // Dimension of the frame, based on screen size
  private static Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
  public static final int MENU_HEIGHT = screenDimension.height / 30;
  static int screenWidth = screenDimension.width;
  static int screenHeight = screenDimension.height - MENU_HEIGHT;
  static int canvasWidth = (screenDimension.width * 2) / 3;
  static int dashboardWidth = (screenDimension.width) / 3;

  // ChapsChallenge component fields.
  private Canvas canvas;
  private Dashboard dashboard;
  private JMenuBar menuBar;
  private ChapsChallenge application;


  // Layout object
  GridBagConstraints constraints = new GridBagConstraints();

  /**
   * Constructor: Creates a new JFrame and sets preferred sizes.
   * Creates and adds all relevant GUI components then redraws.
   */
  public GUI(ChapsChallenge chaps_challenge) {
    application = chaps_challenge;
    //Create & init the frame.
    setPreferredSize(screenDimension.getSize());
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    setResizable(true);
    setMinimumSize(new Dimension(screenDimension.width / 5, screenDimension.height / 5));
    setVisible(true);

    addComponentListener(this);

    // Add components.
    canvas = new Canvas(application);
    dashboard = new Dashboard(chaps_challenge);
    menuBar = new MenuOptions();


    // Set GridBag
    setLayout(new GridBagLayout());
    addLayoutComponents();

    // Render.
    redraw();
  }

  /**
   * Sets up GridBagLayout with all screen components.
   */
  public void addLayoutComponents() {
    // Add MenuBar.
    setJMenuBar(menuBar);
    constraints.fill = GridBagConstraints.BOTH;

    // Set Layout
    int padding = Math.min(screenHeight, canvasWidth) / 11;

    // Set Canvas.
    constraints.insets = new Insets(padding, padding, padding, padding / 2);
    constraints.weightx = 2;
    constraints.weighty = 2;
    add(canvas, constraints);

    // Set Dashboard.
    constraints.insets = new Insets(padding, padding / 2, padding, padding);
    constraints.weightx = 1;
    constraints.weighty = 1;
    add(dashboard, constraints);

    pack();
  }

  /**
   * exitGame:
   * - Invokes a pop up menu to confirm the player wants to exit the game.
   *
   * @return boolean - whether to exit or not
   */
  public void exitGame() {

    // Button options
    String[] options = {"Yes please", "Opps, wrong button"};

    // Create and display the JOptionPane
    int choice = JOptionPane.showOptionDialog(null,
        "Would you like to exit the game?\n",
        "QUIT?",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]);

    // (True) = Exit Game, (False) = Don't Exit Game
    if (choice == JOptionPane.CLOSED_OPTION || choice == 1) {
      return;
    }
    System.exit(0);
  }

  /**
   * Redraws the GUI JFrame.
   * * Revalidates.
   * * Repaints.
   */
  public void redraw() {
    revalidate();
    repaint();
  }


  /**
   * Resize dimensions and redraw window on
   * screen resize.
   *
   * @param e event.
   */
  @Override
  public void componentResized(ComponentEvent e) {
    screenDimension = getSize();

    screenWidth = screenDimension.width;
    screenHeight = screenDimension.height - MENU_HEIGHT;
    canvasWidth = (screenDimension.width * 2) / 3;
    dashboardWidth = (screenDimension.width) / 3;

    revalidate();
    repaint();
  }

  /**
   * Overridden but not utilized.
   *
   * @param e event.
   */
  @Override
  public void componentMoved(ComponentEvent e) {

  }

  /**
   * Overridden but not utilized.
   *
   * @param e event.
   */
  @Override
  public void componentShown(ComponentEvent e) {

  }

  /**
   * Overridden but not utilized.
   *
   * @param e event.
   */
  @Override
  public void componentHidden(ComponentEvent e) {

  }

}
