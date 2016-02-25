package microsim.gui.shell;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import microsim.data.db.DatabaseUtils;
import microsim.engine.SimulationEngine;

import org.h2.tools.Console;

/**
 * Not of interest for users. The frame that controls engine parameters. It is
 * shown when the 'Show engine status' menu item of the Control Panel is
 * choosen.
 * 
 * <p>
 * Title: JAS
 * </p>
 * <p>
 * Description: Java Agent-based Simulation library
 * </p>
 * <p>
 * Copyright (C) 2002 Michele Sonnessa
 * </p>
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 * 
 * @author Michele Sonnessa
 *         <p>
 */
public class DatabaseExplorerFrame extends JInternalFrame {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	ImageIcon imageMiniPreferences = new ImageIcon(getClass().getResource(
			"/microsim/gui/icons/db.gif"));
	
	JButton jBtnClose = null;
	JButton jBtnApply = null;
	JButton jBtnInit = null;
	JPanel jPanelProperties = null;
	JPanel jPanelButtons = null;
	
	JList jList = null;
	
	private javax.swing.JPanel mainContentPane = null;

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            The simulation engine to edit.
	 * @param controlPanel
	 *            A refrence to an instance of ControlPanel.
	 */
	public DatabaseExplorerFrame(SimulationEngine engine) {
		initialize();	
	}	

	private void initialize() {
		// setIconImage(imageMiniPreferences.getImage());
		JScrollPane scrollPane = new JScrollPane(getMainContentPane());
		this.setContentPane(scrollPane);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		setSize(new Dimension(450, 338));
		setTitle("H2 Database Explorer");
		this.setResizable(true);

	}

	private JPanel getJPanelProperties() {
		if (jPanelProperties == null) {
			jPanelProperties = new JPanel();			
			jPanelProperties.setLayout(new BorderLayout());
			
			jPanelProperties.add(new JLabel("Remember to disconnect database to come back"), BorderLayout.NORTH);
			jPanelProperties.add(getJList(), BorderLayout.CENTER);
			
		}
		return jPanelProperties;
	}

	private JList getJList() {
		if (jList == null) {
			
			File outputDir = new File("output");
			File[] dirs = outputDir.listFiles();
			if (dirs == null)
				dirs = new File[0];
			String[] outDirs = new String[dirs.length + 1];
			outDirs[0] = "INPUT";
			for (int i = 0; i < dirs.length; i++) {
				File file = dirs[i];
				outDirs[i + 1] = file.getName();
			}
			
			jList = new JList(outDirs);			
		}
		return jList;
	}
	
	void jBtnApply_actionPerformed(ActionEvent e) {
		if (jList.getSelectedValue() == null)
			return;
		
		try {
			if (jList.getSelectedIndex() == 0)
				new Console().runTool(new String[] {"-url", "jdbc:h2:input/input;AUTO_SERVER=TRUE", "-user", "sa", "-password", ""});
			else
				new Console().runTool(new String[] {"-url", "jdbc:h2:output/" + jList.getSelectedValue().toString() + "/database/out;AUTO_SERVER=TRUE", "-user", "sa", "-password", ""});
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	void jBtnClose_actionPerformed(ActionEvent e) {
		dispose();
	}
	
	void jBtnInit_actionPerformed(ActionEvent e) {
		DatabaseUtils.databaseInputUrl = "input/input";
		DatabaseUtils.inputSchemaUpdateEntityManger();
		try {
			new Console().runTool(new String[] {"-url", "jdbc:h2:input/input;AUTO_SERVER=TRUE", "-user", "sa", "-password", ""});
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private javax.swing.JPanel getMainContentPane() {
		if (mainContentPane == null) {
			mainContentPane = new javax.swing.JPanel();
			mainContentPane.setLayout(new java.awt.BorderLayout());
			mainContentPane.add(getJPanelProperties(), java.awt.BorderLayout.CENTER);
			mainContentPane.add(getJPanelButtons(), java.awt.BorderLayout.SOUTH);
		}
		return mainContentPane;
	}

	private javax.swing.JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new javax.swing.JPanel();
			jPanelButtons.add(getJBtnInit(), null);
			jPanelButtons.add(getJBtnApply(), null);
			jPanelButtons.add(getJBtnClose(), null);
		}
		return jPanelButtons;
	}

	private javax.swing.JButton getJBtnClose() {
		if (jBtnClose == null) {
			jBtnClose = new javax.swing.JButton();
			jBtnClose.setText("Close");
			jBtnClose.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnClose_actionPerformed(e);
				}
			});
		}
		return jBtnClose;
	}

	private javax.swing.JButton getJBtnApply() {
		if (jBtnApply == null) {
			jBtnApply = new javax.swing.JButton();
			jBtnApply.setText("Show database");
			jBtnApply.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnApply_actionPerformed(e);
				}
			});
		}
		return jBtnApply;
	}

	private javax.swing.JButton getJBtnInit() {
		if (jBtnInit == null) {
			jBtnInit = new javax.swing.JButton();
			jBtnInit.setText("Init input database");
			jBtnInit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jBtnInit_actionPerformed(e);
				}
			});
		}
		return jBtnInit;
	}
}