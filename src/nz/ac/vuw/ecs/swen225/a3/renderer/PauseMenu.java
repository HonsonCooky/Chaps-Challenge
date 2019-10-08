package nz.ac.vuw.ecs.swen225.a3.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import nz.ac.vuw.ecs.swen225.a3.application.ChapsChallenge;

/**
 * Pause menu is a jpanel that replaces the game screen with a menu of buttons the user can chose.
 *
 * @author Harrison Cook 300402048, Zac Scott 300447976.
 */
class PauseMenu extends JPanel {

  public enum MenuType {
    PAUSE, DEATH, TIMEOUT, WINNER;
  }

  /**
   * Default serial number.
   */
  private static final long serialVersionUID = 1L;
  private ArrayList<MenuButton> menuButtons = new ArrayList<>();
  private CustomTextPane textPane;
  private ChapsChallenge application;
  private Color buttonForeground;
  private Color buttonBackground;
  private Color otherForeground;
  private Color otherBackground;
  private JPanel panel;
  private MenuType menuType;
  private SimpleAttributeSet centerAlign;

  /**
   * Constructor for the PauseMenu, by Default it is set to pause
   *
   * @param chapsChallenge - The chapsChallenge object for the game this object is in
   */
  PauseMenu(ChapsChallenge chapsChallenge) {
    application = chapsChallenge;
    menuType = MenuType.PAUSE;

    centerAlign = new SimpleAttributeSet();
    StyleConstants.setAlignment(centerAlign, StyleConstants.ALIGN_CENTER);

    setPreferredSize(new Dimension(Gui.screenWidth, Gui.screenHeight));
    setBackground(Gui.BACKGROUND_COLOUR.darker());
    setLayout(new BorderLayout());
    setVisible(true);

    buttonBackground = Gui.BACKGROUND_COLOUR.darker().darker();
    buttonForeground = new Color(193, 193, 193);
    otherBackground = buttonBackground.darker();
    otherForeground = buttonForeground.brighter();

    panel = new JPanel();
    panel.setPreferredSize(
        new Dimension(Gui.screenWidth / 4, Gui.screenHeight - (Gui.screenHeight / 10)));
    panel.setBackground(Gui.BACKGROUND_COLOUR);
    panel.setLayout(new GridBagLayout());
  }

  /**
   * Creates the buttons and text necessary for the pause menu
   */
  void createPauseComponents() {
    removeAll();
    panel.removeAll();
    menuButtons.clear();

    // Create the text for the menu
    textPane = new CustomTextPane("PAUSED", centerAlign, null, foreground, false, Gui.screenWidth / 2, Gui.screenHeight / 3);

    // Create the width and height of the components
    int width = Gui.screenWidth / 2;
    int height = Gui.screenHeight / (3 * 2);

    // Create the buttons
    MenuButton resume = new MenuButton("Resume", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        application.resumeGame();
      }
    }, width, height);

    MenuButton restart = new MenuButton("Restart", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        application.resumeGame();
        application.restartGame();
      }
    }, width, height);

    MenuButton quit = new MenuButton("Quit", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        application.exitGame();
      }
    }, width, height);

    menuButtons.add(resume);
    menuButtons.add(restart);
    menuButtons.add(quit);
  }


  /**
   * Renders the pause menu.
   */
  void renderPauseMenu() {
    if(textPane == null)
      return;

    System.out.println(textPane.getFont().getSize());

    GridBagConstraints gbc = new GridBagConstraints();

    // Add the CustomTextPane
    gbc.weighty = 2;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridwidth = 4;
    panel.add(textPane, gbc);

    // Add the buttons
    gbc.gridx = 1;
    gbc.gridwidth = 2;

    for (int i = 0; i < menuButtons.size(); i++) {
      // Reset the color of the button
      menuButtons.get(i).setBackground(background);
      menuButtons.get(i).setForeground(foreground);
      gbc.gridy = i + 1;
      panel.add(menuButtons.get(i), gbc);
    }

    add(panel, BorderLayout.CENTER);
  }


  /**
   * Recalculates then resize components.
   */
  void resize() {
    if (menuType == MenuType.PAUSE) {
      createPauseComponents();
    } else if (menuType == MenuType.TIMEOUT) {

    }

    renderPauseMenu();
  }


  /**
   * A Private class that extends JButton. Allows for implementing more than one type of Menu with
   * PauseMenu.
   */
  private class MenuButton extends JButton {


    /**
     * MenuButton Constructor
     *
     * @param name   - Name and text of the button
     * @param action - An ActionListener to perform
     * @param width  - The buttons preferred width
     * @param height - The buttons preferred height
     */
    public MenuButton(String name, ActionListener action, int width, int height) {
      super(name);
      setBackground(background);
      setForeground(foreground);
      addActionListener(action);
      setPreferredSize(new Dimension(width, height));
      setFont(findFont(this, new Font("Ariel", Font.BOLD, 30), name));

      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
          setBackground(otherBackground);
          setForeground(otherForeground);
        }

        @Override
        public void mouseExited(MouseEvent e) {
          setBackground(background);
          setForeground(foreground);
        }
      });
    }
  }

  /**
   * Private class to create multiple custom JTextPanes for the DashBoard.
   */
  private class CustomTextPane extends JTextPane {

    /**
     * Default serial number.
     */
    private static final long serialVersionUID = 1L;


    /**
     * CustomTextPane Constructor, read params for instructions.
     *
     * @param text          - The Text in the text pane
     * @param textAlignment - The alignment of the text, if null, default used
     * @param background    - Color of the back ground
     * @param foreground    - Color of the foreground
     * @param border        - If true: Add a matte border of foreground color, if false, no border
     */
    private CustomTextPane(String text, SimpleAttributeSet textAlignment, Color background,
        Color foreground, boolean border, int preferredWidth, int preferredHeight) {

      // Basic setup:
      // - Not editable
      setEditable(false);
      // - Set the background color to the parsed in color
      setBackground(background);
      // - Set the foreground color to the parsed in color
      setForeground(foreground);
      // - Set the preferredSize
      setPreferredSize(new Dimension(preferredWidth, preferredHeight));
      // - Set the font, using a below method to find the font size
      setFont(findFont(this, new Font("Arial", Font.BOLD, 20), text));

      // - If the border boolean parsed in is true, make one
      if (border) {
        setBorder(
            BorderFactory.createMatteBorder(getFont().getSize() / 10, getFont().getSize() / 10,
                getFont().getSize() / 10, getFont().getSize() / 10, foreground));
      } else { //remove any presets
        setBorder(null);
      }

      // Try Align Text
      if (textAlignment != null) {
        try {
          // Create a Styled document (allows for the editing of JTextPane)
          StyledDocument doc = getStyledDocument();

          // Insert the text with the alignment
          doc.insertString(doc.getLength(), text, textAlignment);
          doc.setParagraphAttributes(doc.getLength(), 1, textAlignment, false);
        } catch (Exception e) { // If the right alignment fails, just insert it

          setText(text);
        }
      } else {
        setText(text);
      }
    }
  }


  /**
   * Takes a given font and attempts to find the correct size given some parameters.
   *
   * @param component containing font.
   * @param oldFont   old font to change.
   * @param text      to display
   * @return font with correct size and values.
   */
  private Font findFont(Component component, Font oldFont, String text) {
    // Get the size of the area the text can take up
    int boxWidth = (Gui.screenWidth / 2);
    int boxHeight = (Gui.screenHeight / 6);
    Dimension componentSize = new Dimension(boxWidth, boxHeight);

    // The default size and text if no size is found to fit
    Font savedFont = oldFont;

    // Cycle through font sizes, from 0 to 150 to find a fitting size
    for (int i = 0; i < 300; i++) {
      // Create a new font to test on, incrementing it's size with i
      Font newFont = new Font(savedFont.getFontName(), savedFont.getStyle(), i);

      // Get the approximate relative size the text will take up
      FontMetrics metrics = component.getFontMetrics(newFont);
      int height = metrics.getHeight();
      int width = metrics.stringWidth(text);
      // calculate the size of a box to hold the text with some padding.
      Dimension d = new Dimension(width + 2, height + 2);

      //
      if (componentSize.height - (componentSize.height / 3) < d.height) {
        return savedFont;
      }
      savedFont = newFont;
    }

    // If no sizes fit, resort back to the given font size
    return oldFont;
  }
}

