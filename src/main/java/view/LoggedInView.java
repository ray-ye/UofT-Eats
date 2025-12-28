package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.random_restauarant.RandomRestaurantController;
import interface_adapter.view_restaurant.ViewRestaurantController;
import interface_adapter.view_restaurant.ViewRestaurantViewModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * The View displayed after successful login. Shows user information, provides logout functionality,
 * and displays searchable restaurant list.
 */
public class LoggedInView extends JPanel implements PropertyChangeListener {

  public static final String VIEW_NAME = "logged in";

  // ViewModels for observing changes
  private final LoggedInViewModel loggedInViewModel;
  // UI Components for LoggedInView specific elements
  private final JLabel welcomeLabel;
  private final JLabel uidLabel;
  private final JButton logoutButton;
  private final JButton randomRestaurantButton;
  // UI Components for the search/restaurant list


  // Controllers for actions
  private LogoutController logoutController;
  private ViewRestaurantController viewRestaurantController;
  private RandomRestaurantController randomRestaurantController;
  private ViewManagerModel viewManagerModel;
  private ViewRestaurantViewModel viewRestaurantViewModel;

  private RestaurantPanel.HeartClickListener heartListener;

  private String filterViewName;

  public LoggedInView(LoggedInViewModel loggedInViewModel) {
    this.loggedInViewModel = loggedInViewModel;
    this.loggedInViewModel.addPropertyChangeListener(this);

    final JLabel title = new JLabel(LoggedInViewModel.TITLE_LABEL);
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    title.setFont(new Font("Arial", Font.BOLD, 24));

    welcomeLabel = new JLabel();
    welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));

    uidLabel = new JLabel();
    uidLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    uidLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    uidLabel.setForeground(Color.GRAY);

    logoutButton = new JButton("Logout");
    logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    logoutButton.addActionListener(evt -> {
      if (logoutController != null) {
        logoutController.execute();
      } else {
        JOptionPane.showMessageDialog(this, "Logout Controller not initialized.");
      }
    });

    randomRestaurantButton = new JButton("Random Restaurant");
    randomRestaurantButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    randomRestaurantButton.addActionListener(ect -> {
      if (viewRestaurantController != null) {
        try {
          randomRestaurantController.execute();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      if (viewManagerModel != null) {
        System.out.println(
            "Changing viewManager state to: " + viewRestaurantViewModel.getViewName());
        viewManagerModel.setState(viewRestaurantViewModel.getViewName());
        viewManagerModel.firePropertyChange();
      } else {
        JOptionPane.showMessageDialog(this, "ViewManager not initialized");
      }
    });

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(Box.createVerticalStrut(50));
    this.add(title);
    this.add(Box.createVerticalStrut(20));
    this.add(welcomeLabel);
    this.add(Box.createVerticalStrut(10));
    this.add(uidLabel);
    this.add(Box.createVerticalStrut(30));
    this.add(randomRestaurantButton);
    this.add(Box.createVerticalStrut(10));
    this.add(logoutButton);


    updateLoggedInView(loggedInViewModel.getState());
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {

  }

  private void updateLoggedInView(LoggedInState state) {
    if (state.getNickname() != null && !state.getNickname().isEmpty()) {
      welcomeLabel.setText("Welcome, " + state.getNickname() + "!");
    } else {
      welcomeLabel.setText("Welcome!");
    }

    if (state.getUid() != null && !state.getUid().isEmpty()) {
      uidLabel.setText("User ID: " + state.getUid());
    } else {
      uidLabel.setText("");
    }
  }


  public String getViewName() {
    return VIEW_NAME;
  }


  public void setLogoutController(LogoutController logoutController) {
    this.logoutController = logoutController;
  }

  public void setRandomRestaurantController(RandomRestaurantController randomRestaurantController) {
    this.randomRestaurantController = randomRestaurantController;
  }

  public ViewRestaurantController getViewRestaurantController() {
    return viewRestaurantController;
  }

  public void setViewRestaurantController(ViewRestaurantController viewRestaurantController) {
    this.viewRestaurantController = viewRestaurantController;
  }

  public void setViewManagerModel(ViewManagerModel viewManagerModel) {
    this.viewManagerModel = viewManagerModel;
  }

  public void setViewRestaurantViewModel(ViewRestaurantViewModel viewRestaurantViewModel) {
    this.viewRestaurantViewModel = viewRestaurantViewModel;
  }




}