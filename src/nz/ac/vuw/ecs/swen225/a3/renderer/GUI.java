package nz.ac.vuw.ecs.swen225.a3.renderer;

import nz.ac.vuw.ecs.swen225.a3.persistence.AssetManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;

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

  // Main component fields.
  private Canvas canvas;
  private Dashboard dashboard;
  private JMenuBar menuBar;

  /**
   * Constructor: Creates a new JFrame and sets preferred sizes.
   * Creates and adds all relevant GUI components then redraws.
   */
  public GUI() {
    //Create & init the frame.
    setPreferredSize(screenDimension.getSize());
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    setResizable(true);
    setMinimumSize(new Dimension(screenDimension.width / 5, screenDimension.height / 5));
    setVisible(true);

    addComponentListener(this);

    // Load Assets
    try {
      AssetManager.loadAssets();
    } catch (IOException e) {
      System.out.println("Failed to load assets " + e);
    }

    // Add components.
    canvas = new Canvas();
    dashboard = new Dashboard();
    menuBar = new MenuOptions();
    addLayoutComponents();

    // Render.
    redraw();
  }

  /**
   * Testing invocation point to check GUI.
   *
   * @param args (ignored).
   */
  public static void main(String[] args) {
    GUI gui = new GUI();
    System.out.printf("Screen width %d, height %d\n", canvasWidth, screenHeight);
    System.out.printf("Dashboard width %d, height %d\n", dashboardWidth, screenHeight);
  }

  /**
   * Sets up GridBagLayout with all screen components.
   */
  public void addLayoutComponents() {
    // Add MenuBar.
    setJMenuBar(menuBar);

    // Set GridBag
    setLayout(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();

    // Set Layout
    int padding = Math.min(screenHeight, canvasWidth) / 11;

    // Set Canvas.
    constraints.insets = new Insets(padding, padding, padding, padding / 2);
    constraints.weightx = 1;
    constraints.weighty = 1;

    constraints.fill = GridBagConstraints.BOTH;
    add(canvas, constraints);

    // Set Dashboard.
    constraints.insets = new Insets(padding, padding / 2, padding, padding);

    constraints.weightx = 1;
    constraints.weighty = 1;

    constraints.fill = GridBagConstraints.BOTH;
    add(dashboard, constraints);

    pack();
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
   * @param e event.
   */
  @Override
  public void componentMoved(ComponentEvent e) {

  }

  /**
   * Overridden but not utilized.
   * @param e event.
   */
  @Override
  public void componentShown(ComponentEvent e) {

  }

  /**
   * Overridden but not utilized.
   * @param e event.
   */
  @Override
  public void componentHidden(ComponentEvent e) {

  }
}
