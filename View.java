import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

/*
 * Class that contains all the buttons and menus we will be using. 
 */
public class View implements ActionListener {
	public static final int WINDOW_HEIGHT = 720;
	public static final int WINDOW_WIDTH = 1080;
	static JMenuBar menubar = new JMenuBar();

	static JMenu villages = new JMenu("Villages");
	static JMenuItem addVillage = new JMenuItem("Add a Village");
	static JMenu removeVillage = new JMenu("Remove a Village");
	static JMenuItem nukeVillage = new JMenuItem("Nuke a Village");
	static JMenuItem razeVillage = new JMenuItem("Raze a Village");

	static JMenu roads = new JMenu("Roads");
	static JMenuItem addRoad = new JMenuItem("Add a Road");
	static JMenuItem removeRoad = new JMenuItem("Remove a Road");

	static JMenu gnomes = new JMenu("Gnomes");
	static JMenuItem addGnome = new JMenuItem("Add a Gnome");
	static JMenu gnomeActions = new JMenu("Gnome actions");
	static JMenuItem moveGnomeRan = new JMenuItem("Make Gnome Restless");
	static JMenuItem stopGnomeRan = new JMenuItem("Make Gnome Tired");
	static JMenuItem moveGnomeSpc = new JMenuItem("Move a Gnome to a VIllage");

	static JFrame mainWindow = new JFrame("The Land of Gnomenwald");
	static DisplayPanel graphPanel = new DisplayPanel();

	/**
	 * This method creates the Window and Panel that will be containing the
	 * villages and gnomes.
	 */
	public static void createAndShowGUI() {
		mainWindow.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		graphPanel.setBackground(new Color(102, 178, 255));
		mainWindow.add(graphPanel);
		mainWindow.add(graphPanel.add(createMenu()), BorderLayout.NORTH);
		mainWindow.setVisible(true);
		graphPanel.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}// end of method creatAndShowGUI

	
	/**
	 * Returns the menu with all its respective sub-menus.
	 * 
	 * @return
	 */
	public static JMenuBar createMenu() {

		menubar.add(villages);
		villages.add(addVillage);
		villages.add(removeVillage);
		removeVillage.add(nukeVillage);
		removeVillage.add(razeVillage);

		menubar.add(roads);
		roads.add(addRoad);
		roads.add(removeRoad);

		menubar.add(gnomes);
		gnomes.add(addGnome);
		gnomes.add(gnomeActions);
		gnomeActions.add(moveGnomeRan);
		gnomeActions.add(stopGnomeRan);
		gnomeActions.add(moveGnomeSpc);

		return menubar;
	}// end of method createMenu


	/**
	 * Method to check what action was performed e.g. add a village, add a gnome etc.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addVillage) {
			Dynamic dyno = new Dynamic();

			JFrame frame = new JFrame("Add a Village");

			Color color = null;
			Color green = new Color(204, 255, 204);
			Color purple = new Color(204, 204, 255);

			// popup window that allows user to explore "add village" options
			Object countries[] = { "Gnomenwald", "TrollLand" };
			String country = (String) JOptionPane.showInputDialog(frame,
					"In which country would you like to add your village?",
					"Add a Village", JOptionPane.PLAIN_MESSAGE, null,
					countries, countries[0]);
			// determining the color of the village based on country
			if ((country != null) && (country.length() > 0)) {
				if (country.equals("Gnomenwald")) {
					color = green;
				}
				if (country.equals("TrollLand")) {
					color = purple;
				}

				// deciding whether the village requires passports or not
				ArrayList<Village> passportStamps = new ArrayList<Village>();
				Object[] options = { "Yes, of course!", "No way!" };
				int yesorno = JOptionPane
						.showOptionDialog(frame,
								"Does this village require a passport?",
								"Passport", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[0]);

				// if the user does want the village to require passports...
				if (yesorno == 0) {
					JFrame passportframe = new JFrame();

					JPanel container = new JPanel();
					passportframe.add(container);
					container.setLayout(new FlowLayout());

					JLabel question = new JLabel(
							"Which villages are required by the passport?");
					container.add(question);

					ArrayList<JCheckBox> checkboxes = new ArrayList<JCheckBox>();

					for (int x = 0; x < Map.allVillages.size(); x++) {
						JCheckBox button = new JCheckBox("Village "
								+ Map.allVillages.get(x).getID());
						checkboxes.add(button);
					}
					for (int y = 0; y < checkboxes.size(); y++) {
						container.add(checkboxes.get(y));
					}

					// after they have checked their options, they can click ok
					JOptionPane.showConfirmDialog(null, container, null,
							JOptionPane.PLAIN_MESSAGE);

					// way of showing passport stamps on dyno as well?
					for (int z = 0; z < checkboxes.size(); z++) {
						if (checkboxes.get(z).isSelected() == true) {
							Village stamp = Map.allVillages.get(z);
							passportStamps.add(stamp);
						}
					}
				}// end of if yesorno

				Map.addVillage(color, passportStamps);
				dyno.addAdditionalVillage(graphPanel,
						Map.allVillages.get(Map.allVillages.size() - 1));
				graphPanel.validate();
			}
		}// add a village

		if (e.getSource() == nukeVillage) {
			JFrame frame = new JFrame("Nuke a village");
			if (Map.allVillages.size() > 0) {
				Object villages[] = new Object[Map.allVillages.size()];
				for (int i = 0; i < villages.length; i++) {
					villages[i] = Map.allVillages.get(i).toString();
				}
				String s = (String) JOptionPane.showInputDialog(frame,
						"Choose a Village to destroy, along with its Roads.",
						"Nuke Village", JOptionPane.PLAIN_MESSAGE, null,
						villages, villages[0]);

				if ((s != null) && (s.length() > 0)) {
					int ID = obtainVillageID(s);
					Village nuke = Map.findVillage(ID);
					Map.nukeVillage(nuke);
					graphPanel.remove(nuke);
					graphPanel.repaint();
					return;
				}
			} else {
				JOptionPane.showMessageDialog(frame,
						"No Villages to nuke!  Try adding some first.");
			}
		}// end of nuking a village

		if (e.getSource() == razeVillage) {
			JFrame razeframe = new JFrame("Raze a village");
			if (Map.allVillages.size() > 0) {
				Village[] villageString = new Village[Map.allVillages.size()];
				Map.allVillages.toArray(villageString);
				String[] villageNames = new String[villageString.length];
				for (int i = 0; i < villageNames.length; i++) {
					villageNames[i] = Map.allVillages.get(i).toString();
				}

				String s = (String) JOptionPane.showInputDialog(razeframe,
						"Choose which village meets a firey death: ",
						"Raze Village", JOptionPane.PLAIN_MESSAGE, null,
						villageNames, villageString[0]);

				if ((s != null) && (s.length() > 0)) {
					int ID = obtainVillageID(s);
					Village raze = null;
					for (Village v : Map.allVillages) {
						if (v.getID() == ID) {
							raze = v;
						}
					}
					Map.razeVillage(raze);
					graphPanel.remove(raze);
					graphPanel.repaint();
					return;
				}
			} else {
				JOptionPane.showMessageDialog(razeframe,
						"No Villages to raze!  Try adding some first.");
			}
		}// end of razing a village

		if (e.getSource() == addRoad) {
			JFrame frame = new JFrame("Add a Road");
			JPanel panel = new JPanel();
			JTextField xField = new JTextField(5);
			JTextField yField = new JTextField(5);
			JLabel label = new JLabel("Please enter only ID numbers.");

			panel.add(new JLabel("Village From:"));
			panel.add(xField);
			panel.add(Box.createHorizontalStrut(15)); // a spacer
			panel.add(new JLabel("Village To:"));
			panel.add(yField);
			panel.add(label, BorderLayout.SOUTH);

			Road addme = null;

			int result = JOptionPane.showConfirmDialog(null, panel,
					"Add a Road between these Villages:",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				try {
					int from = Integer.parseInt(xField.getText());
					int to = Integer.parseInt(yField.getText());
					Village start = Map.findVillage(from);
					Village end = Map.findVillage(to);
					addme = Map.findRoad(start, end);
					if (addme == null) { // check to see if a road already
											// exists between the villages
						if (start == null || end == null) { // check to see if
															// both villages
															// exist
							JOptionPane
									.showMessageDialog(frame,
											"Sorry. One of these villages does not exist");
						} else {
							Random generator = new Random();
							int i = generator.nextInt(6) + 1;
							Road newroad = new Road(start, end, i);
							Map.minSpanTree(newroad);
							// Map.allRoads.add(newroad);
							graphPanel.repaint();
						}
					} else {
						JOptionPane.showMessageDialog(frame,
								"This road already exists! Jolly good.");
					}
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(frame,
							"Please enter ID numbers.");
				}

			}
			panel.setVisible(true);
		}// end of adding a road

		if (e.getSource() == removeRoad) {
			JFrame frame = new JFrame("Remove a Road");
			JPanel panel = new JPanel();
			JTextField xField = new JTextField(5);
			JTextField yField = new JTextField(5);
			JLabel label = new JLabel("Please enter only ID numbers.");

			panel.add(new JLabel("Village From:"));
			panel.add(xField);
			panel.add(Box.createHorizontalStrut(15)); // a spacer
			panel.add(new JLabel("Village To:"));
			panel.add(yField);
			panel.add(label, BorderLayout.SOUTH);

			Road deleteme = null;

			int result = JOptionPane.showConfirmDialog(null, panel,
					"Delete a Road between this Villages:",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				try {
					int from = Integer.parseInt(xField.getText());
					int to = Integer.parseInt(yField.getText());
					Village start = Map.findVillage(from);
					Village end = Map.findVillage(to);
					deleteme = Map.findRoad(start, end);
					if (deleteme != null) {
						Map.allRoads.remove(deleteme);
						graphPanel.repaint();
					} else {
						JOptionPane.showMessageDialog(frame,
								"This road does not exist! Jolly good.");
					}
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(frame,
							"Please enter ID numbers.");
				}

			}
			panel.setVisible(true);

		}// end of removing a road

		if (e.getSource() == addGnome) {
			JFrame frame = new JFrame("Add a Gnome");
			Object villages[] = new Object[Map.allVillages.size()];
			for (int i = 0; i < villages.length; i++) {
				villages[i] = Map.allVillages.get(i).toString();
			}
			String s = (String) JOptionPane.showInputDialog(frame,
					"Choose a Village where your Gnome will be born.",
					"Add a Gnome", JOptionPane.PLAIN_MESSAGE, null, villages,
					villages[0]);
			if ((s != null) && (s.length() > 0)) {
				int ID = obtainVillageID(s);
				Village birthplace = Map.findVillage(ID);
				Gnome g = new Gnome(birthplace);
				Map.allGnomes.add(g);
				// new Thread(g).start();
				/*
				 * graphPanel.remove(nuke); graphPanel.repaint(); return;
				 */
			}

		}// end of adding a gnome

		if (e.getSource() == moveGnomeRan) {
			JFrame frame = new JFrame("Make Gnome Restless");
			if (Map.allGnomes.size() > 0) {
				Object gnomes[] = new Object[Map.allGnomes.size()];
				for (int i = 0; i < gnomes.length; i++) {
					gnomes[i] = Map.allGnomes.get(i).toString();
				}
				String s = (String) JOptionPane.showInputDialog(frame,
						"Choose Gnome to make restless.",
						"Make Gnome Restless", JOptionPane.PLAIN_MESSAGE, null,
						gnomes, gnomes[0]);
				if ((s != null) && (s.length() > 0)) {
					int ID = obtainGnomeID(s);
					Gnome gnark = Map.allGnomes.get(ID - 1);
					try {
						gnark.startGnome();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			} else {
				JOptionPane
						.showMessageDialog(frame,
								"No Gnomes currently reside here!  Try adding some first.");
			}

		}// end of moving Gnome Randomly

		if (e.getSource() == stopGnomeRan) {
			JFrame frame = new JFrame("Make Gnome Tired");
			if (Map.allGnomes.size() > 0) {
				Object gnomes[] = new Object[Map.allGnomes.size()];
				for (int i = 0; i < gnomes.length; i++) {
					gnomes[i] = Map.allGnomes.get(i).toString();
				}
				String s = (String) JOptionPane.showInputDialog(frame,
						"Choose Gnome to make sleepy.", "Make Gnome Tired",
						JOptionPane.PLAIN_MESSAGE, null, gnomes, gnomes[0]);
				if ((s != null) && (s.length() > 0)) {
					int ID = obtainGnomeID(s);
					try {
						Map.allGnomes.get(ID - 1).stopGnome();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} else {
				JOptionPane
						.showMessageDialog(frame,
								"No Gnomes currently reside here!  Try adding some first.");

			}// end of stopping Gnome
		}

		if (e.getSource() == moveGnomeSpc) {
			JFrame moveframe = new JFrame("Move a Gnome to a specific Village");

			if (Map.allGnomes.size() > 0) {
				JPanel panel = new JPanel();
				JTextField xField = new JTextField(5);
				JTextField yField = new JTextField(5);
				JLabel label = new JLabel("Please enter only ID numbers.");

				panel.add(new JLabel("Move Gnome:"));
				panel.add(xField);
				panel.add(Box.createHorizontalStrut(15)); // a spacer
				panel.add(new JLabel("To Village:"));
				panel.add(yField);
				panel.add(label, BorderLayout.SOUTH);

				int result = JOptionPane.showConfirmDialog(null, panel,
						"Move Gnome to Village:", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					try {
						int gnome = Integer.parseInt(xField.getText());
						int to = Integer.parseInt(yField.getText());
						Gnome gnark = Map.findGnome(gnome);
						Village end = Map.findVillage(to);
						if (gnark != null & end != null) {
							gnark.destiny = end;
							gnark.nonAdj = true;
							if (gnark.current == gnark.destiny) {
								gnark.nonAdj = false;
								JOptionPane.showMessageDialog(moveframe,
										"You are already there!");
							}
							// gnark.start();
						} else if (gnark == null) {
							JOptionPane
									.showMessageDialog(moveframe,
											"This gnome does not exist.  Please enter a correct ID.");
						} else if (end == null) {
							JOptionPane.showMessageDialog(moveframe,
									"Needs a valid Village");
						}

					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(moveframe,
								"Please enter ID numbers.");
					}

				}
				panel.setVisible(true);
			} else {
				JOptionPane
						.showMessageDialog(moveframe,
								"No Gnomes currently reside here.  Try adding some first.");
			}
	
		}// end of moving a gnome to specific village
		
	}// end of method actionPerformed
	

	/**
	 * Returns the ID of a village given a String with the village's name.
	 * @param village
	 * @return
	 */
	public int obtainVillageID(String village) {
		int space = village.indexOf(" ");
		String idNum = village.substring(space + 1);
		int villageID = Integer.parseInt(idNum);
		return villageID;
	}

	
	/**
	 * Returns the ID of a gnome given a String with the gnome's name. 
	 * @param gnome
	 * @return
	 */
	public int obtainGnomeID(String gnome) {
		int space = gnome.indexOf(" ");
		String idNum = gnome.substring(space + 1);
		int gnomeID = Integer.parseInt(idNum);
		return gnomeID;
	}
	
	
	// constructor for View
		public View() {
			createAndShowGUI();
			Map gnomenwald = new Map();   // These are used
			Dynamic dyno = new Dynamic(); // if we set default
			                                 // villages
			 for (Village v : Map.allVillages) {
				 dyno.addVillagetoGraph(graphPanel);
			 }
			mainWindow.setVisible(true);
			addVillage.addActionListener(this);
			nukeVillage.addActionListener(this);
			razeVillage.addActionListener(this);
			addRoad.addActionListener(this);
			removeRoad.addActionListener(this);
			addGnome.addActionListener(this);
			moveGnomeSpc.addActionListener(this);
			stopGnomeRan.addActionListener(this);
			moveGnomeRan.addActionListener(this);

		}// end of constructor


}
