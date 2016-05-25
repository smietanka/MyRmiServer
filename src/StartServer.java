import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class StartServer {
	public static JTextArea myLogBox;
	public static JButton runServButton, stopServButton;
	public static JFrame myFrame;
	public static IMyServer myServer;

	public StartServer() {
		myFrame = new JFrame("Moj serwer");

		myFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(myFrame,
						"Wychodzac z programu wylaczysz rowniez serwer. Napewno tego chcesz?", "Napewno?",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		JPanel mainPanel = new JPanel();
		JPanel topPanel = new JPanel();
		JPanel centerPanel = new JPanel();

		myLogBox = new JTextArea();

		mainPanel.setLayout(new BorderLayout(5, 5));
		topPanel.setLayout(new GridLayout(1, 0, 5, 5));
		centerPanel.setLayout(new BorderLayout(5, 5));
		centerPanel.add(new JScrollPane(myLogBox), BorderLayout.CENTER);
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		runServButton = new JButton("Uruchom serwer");
		stopServButton = new JButton("Zatrzymaj serwer");

		topPanel.add(runServButton);
		topPanel.add(stopServButton);

		runServButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runServer();
			}
		});

		stopServButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					myServer.stopServer();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		myFrame.setContentPane(mainPanel);
		myFrame.setSize(600, 200);
		myFrame.setVisible(true);
	}

	public static void runServer() {
		try {
			myServer = new MyServer(myLogBox);
			myServer.runServer();
		} catch (Exception e) {
			System.out.println("[System] Wystapil blad: " + e);
			myLogBox.append("[System] Wystapil blad: " + e + "\n");
		}
	}

	public static void main(String[] args) {
		StartServer myUi = new StartServer();
	}
}