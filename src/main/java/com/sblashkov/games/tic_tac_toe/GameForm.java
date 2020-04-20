package com.sblashkov.games.tic_tac_toe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import com.sblashkov.games.tic_tac_toe.model.BoardState;
import com.sblashkov.games.tic_tac_toe.model.CompLevel;

/**
 * Main form for game on swing.
 *
 * @author Sergey Blashkov
 * @version 1.0 2015-06-09
 */
public class GameForm extends JFrame {

	private static final String EN_WINDOW_TITLE = "Tic Tac Toe";
	private static final String EN_B_START_NEW_GAME = "Start new game";
	private static final String EN_L_HARD = "Hard";
	private static final String EN_L_MEDIUM = "Medium";
	private static final String EN_L_EASY = "Easy";
	private static final String EN_L_MULTIPLAYER = "Multiplayer";
	private static final String EN_L_SINGLE_PLAYER = "Single player";
	private static final String EN_CB_COMPUTER_GOES_FIRST = "Computer goes first";
	private static final String EN_NO_GAME_STARTED_YET = "No game started yet\n";
	private static final String EN_LBL_PL2_NAME = "Player2 name";
	private static final String EN_LBL_PL1_NAME = "Player1 name";

	private static final String RU_WINDOW_TITLE = "Крестики-Нолики";
	private static final String RU_B_START_NEW_GAME = "Начать новую игру";
	private static final String RU_L_HARD = "Сложный";
	private static final String RU_L_MEDIUM = "Средний";
	private static final String RU_L_EASY = "Простой";
	private static final String RU_L_MULTIPLAYER = "Играть вдвоем";
	private static final String RU_L_SINGLE_PLAYER = "Против компьютера";
	private static final String RU_CB_COMPUTER_GOES_FIRST = "Комп начинает";
	private static final String RU_LBL_PL2_NAME = "Имя Игрока 1";
	private static final String RU_LBL_PL1_NAME = "Имя Игрока 2";

	private static final long serialVersionUID = 236810756991797349L;

	private JPanel rootPanel;
	private JLabel player1NameLbl;
	private JTextField player1Name;
	private JLabel player2NameLbl;
	private JTextField player2Name;
	private JTable tableBoard;
	private JTextArea output;
	private JCheckBox isCompFirstCB;
	private JList<String> modesList;
	private DefaultListModel<String> modesListModel;
	private JList<String> compLevelsList;
	private DefaultListModel<String> compLevelsListModel;
	private JList<String> languageList;
	private DefaultListModel<String> languageListModel;
	private JButton newGameBtn;
	private MyTableModel model = new MyTableModel();
	
	private Game game = null;

	public GameForm() {

		super(EN_WINDOW_TITLE);
		rootPanel = new JPanel(new BorderLayout());

		final JPanel names1 = new JPanel();
		names1.setLayout(new BoxLayout(names1, BoxLayout.X_AXIS));
		player1NameLbl = new JLabel(EN_LBL_PL1_NAME);
		player1Name = new JTextField(15);

		names1.add(player1NameLbl);
		names1.add(player1Name);

		final JPanel names2 = new JPanel();
		names2.setLayout(new BoxLayout(names2, BoxLayout.X_AXIS));
		player2NameLbl = new JLabel(EN_LBL_PL2_NAME);
		player2Name = new JTextField(15);

		names2.add(player2NameLbl);
		names2.add(player2Name);

		final JPanel bothNames = new JPanel();
		bothNames.setLayout(new BoxLayout(bothNames, BoxLayout.Y_AXIS));
		bothNames.add(names1);
		bothNames.add(names2);
		
		languageListModel = new DefaultListModel<String>();
		languageListModel.addElement("English");
		languageListModel.addElement("Русский");
		
		languageList = new JList<String>(languageListModel);
		languageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		languageList.setSelectedIndex(0);
		languageList.addListSelectionListener(new LanguageSelectorListener());

		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(bothNames);
		topPanel.add(languageList);

		tableBoard = new JTable() {

			private static final long serialVersionUID = -7263512123761347166L;

			@Override
            public Component prepareRenderer(
                TableCellRenderer renderer, int row, int col) {
                if (col == 0) {
                    return this.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(
                    		this, this.getValueAt(row, col), false, false, row, col
                    	);
                } else {
                    return super.prepareRenderer(renderer, row, col);
                }
            }
		};
		tableBoard.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableBoard.setCellSelectionEnabled(true);
		tableBoard.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableBoard.addMouseListener(new TableMouseListener());

        final JTableHeader header = tableBoard.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer(tableBoard));
		tableBoard.setModel(model);
		tableBoard.setFont(new Font("Courier New", Font.BOLD, 36));

		DefaultTableCellRenderer rightRenderer = new MyTableRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.CENTER);
		rightRenderer.setVerticalAlignment(JLabel.CENTER);
		for (int i = 0; i < tableBoard.getColumnModel().getColumnCount(); i++) {
			tableBoard.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
		}

		final int fieldSize = tableBoard.getColumnModel().getColumn(0).getWidth();
		header.setPreferredSize(new Dimension(fieldSize, fieldSize));
		tableBoard.setRowHeight(fieldSize);
		
		tableBoard.setEnabled(false);
		final JScrollPane tablePane = new JScrollPane(tableBoard);
		tablePane.setPreferredSize(new Dimension(fieldSize * 4 + 3, fieldSize * 4 + 3));

		final JPanel field = new JPanel();
		field.setLayout(new BoxLayout(field, BoxLayout.Y_AXIS));

		field.add(topPanel);
		field.add(tablePane);

		output = new JTextArea();
		output.setAutoscrolls(true);
        output.setEditable(false);
        output.append(EN_NO_GAME_STARTED_YET);

        final JScrollPane textPane = new JScrollPane(output);
        textPane.setPreferredSize(new Dimension(fieldSize * 4, fieldSize));

        field.add(textPane);

        isCompFirstCB = new JCheckBox(EN_CB_COMPUTER_GOES_FIRST);
		isCompFirstCB.setMnemonic(KeyEvent.VK_C);
		isCompFirstCB.setSelected(true);

		modesListModel = new DefaultListModel<String>();
		modesListModel.addElement(EN_L_SINGLE_PLAYER);
		modesListModel.addElement(EN_L_MULTIPLAYER);
		
		modesList = new JList<String>(modesListModel);
		modesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        modesList.setSelectedIndex(0);
        modesList.addListSelectionListener(new ModeSelectorListener());
        modesList.setVisibleRowCount(2);

        compLevelsListModel = new DefaultListModel<String>();
        compLevelsListModel.addElement(EN_L_EASY);
        compLevelsListModel.addElement(EN_L_MEDIUM);
        compLevelsListModel.addElement(EN_L_HARD);
		
        compLevelsList = new JList<String>(compLevelsListModel);
        compLevelsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        compLevelsList.setSelectedIndex(1);
        compLevelsList.setVisibleRowCount(3);

        newGameBtn = new JButton(EN_B_START_NEW_GAME);
        newGameBtn.setVerticalTextPosition(AbstractButton.CENTER);
        newGameBtn.setHorizontalTextPosition(AbstractButton.CENTER);
        newGameBtn.addActionListener(new NewGameButtonListener());

        rootPanel.add(field, BorderLayout.NORTH);
        rootPanel.add(new JScrollPane(modesList), BorderLayout.WEST);
        rootPanel.add(new JScrollPane(compLevelsList), BorderLayout.CENTER);
        rootPanel.add(new JScrollPane(isCompFirstCB), BorderLayout.EAST);
        rootPanel.add(new JScrollPane(newGameBtn), BorderLayout.SOUTH);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        rootPanel.setOpaque(true);
	}

	private class TableMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {

	        int row = tableBoard.rowAtPoint(e.getPoint());
	        int col = tableBoard.columnAtPoint(e.getPoint());

	        if (row >= 0 && col > 0) {
	        	if (game != null && game.getBoardState() == BoardState.IN_PROGRESS) {
	        		game.makeUserTurn(row, col - 1);
	        		drawTable();
	        	}
	        }
		}
	}

	private void drawTable() {

		if (game.getBoardState() != BoardState.IN_PROGRESS && game.getBoardState() != BoardState.DRAW) {
			model.setWinning(game.getWinRow());
			output.append(getWinText());
		} else if (game.getBoardState() == BoardState.DRAW) {
			output.append(getDrawText());
		}

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				final String let = game.getSignAt(x, y);
		    	tableBoard.setValueAt(let, x, y + 1);
			}
		}
	}

	private String getDrawText() {
		if (languageList.getSelectedValue().equalsIgnoreCase("English")) {
			return "It's a draw, nobody won\n";
		} else {
			return "Ничья, никто не победил\n";
		}
	}

	private String getWinText() {
		if (languageList.getSelectedValue().equalsIgnoreCase("English")) {
			return game.getNotCurPlayerName() + " won! Congratulations!\n";
		} else {
			return game.getNotCurPlayerName() + " победил! Поздравляем!\n";
		}
	}

	private class HeaderRenderer implements TableCellRenderer {

        private TableCellRenderer renderer;

        public HeaderRenderer(JTable table) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }

        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int col) {
            return renderer.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, col);
        }
    }

	private class MyTableRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 3901551793031120606L;

		@Override
        public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int col) {
        	Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, col);

        	if (model.isWin(row, col - 1)) {
        		c.setBackground(Color.GREEN.darker());
        	} else {
        		c.setBackground(Color.WHITE);
        	}
            return c;
        }
    }

    private class LanguageSelectorListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {

			if (e.getValueIsAdjusting() == false) {
	        	 
	            if (languageList.getSelectedIndex() == 0) {
	            	setTitle(EN_WINDOW_TITLE);
	            	newGameBtn.setText(EN_B_START_NEW_GAME);
	            	compLevelsListModel.setElementAt(EN_L_EASY, 0);
	            	compLevelsListModel.setElementAt(EN_L_MEDIUM, 1);
	            	compLevelsListModel.setElementAt(EN_L_HARD, 2);
	            	modesListModel.setElementAt(EN_L_SINGLE_PLAYER, 0);
	            	modesListModel.setElementAt(EN_L_MULTIPLAYER, 1);
	            	isCompFirstCB.setText(EN_CB_COMPUTER_GOES_FIRST);
	            	player1NameLbl.setText(EN_LBL_PL2_NAME);
	            	player2NameLbl.setText(EN_LBL_PL1_NAME);
	            } else {
	            	setTitle(RU_WINDOW_TITLE);
	            	newGameBtn.setText(RU_B_START_NEW_GAME);
	            	compLevelsListModel.setElementAt(RU_L_EASY, 0);
	            	compLevelsListModel.setElementAt(RU_L_MEDIUM, 1);
	            	compLevelsListModel.setElementAt(RU_L_HARD, 2);
	            	modesListModel.setElementAt(RU_L_SINGLE_PLAYER, 0);
	            	modesListModel.setElementAt(RU_L_MULTIPLAYER, 1);
	            	isCompFirstCB.setText(RU_CB_COMPUTER_GOES_FIRST);
	            	player1NameLbl.setText(RU_LBL_PL2_NAME);
	            	player2NameLbl.setText(RU_LBL_PL1_NAME);
	            }
	        }
		}
	}

    private class ModeSelectorListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {

			if (e.getValueIsAdjusting() == false) {
	        	 
	            if (modesList.getSelectedIndex() == 0) {
	                isCompFirstCB.setEnabled(true);
	                compLevelsList.setEnabled(true);
	            } else {
	            	isCompFirstCB.setEnabled(false);
	                compLevelsList.setEnabled(false);
	            }
	        }
		}
	}

	private class NewGameButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			final boolean isCompFirst = isCompFirstCB.isSelected();
			CompLevel level = null;
			try {
				if (compLevelsList.getSelectedIndex() == 0) {
					level = CompLevel.valueOf(EN_L_EASY.toUpperCase());
				} else if (compLevelsList.getSelectedIndex() == 1) {
					level = CompLevel.valueOf(EN_L_MEDIUM.toUpperCase());
				} else if (compLevelsList.getSelectedIndex() == 2) {
					level = CompLevel.valueOf(EN_L_HARD.toUpperCase());
				}
			} catch (IllegalArgumentException e) {
				output.append(getDificultyLevelError());
			}
			final boolean vsComp = modesList.getSelectedValue().equals(EN_L_SINGLE_PLAYER)
							    || modesList.getSelectedValue().equals(RU_L_SINGLE_PLAYER);

			String pl1Name = null;
			if (player1Name.getText() != null && !player1Name.getText().trim().equals("")) {
				pl1Name = player1Name.getText().trim();
			}

			String pl2Name = null;
			if (player2Name.getText() != null && !player2Name.getText().trim().equals("")) {
				pl2Name = player2Name.getText().trim();
			}

			game = new Game(isCompFirst, level, vsComp, pl1Name, pl2Name);
			model = new MyTableModel();
			drawTable();
			output.append(getGameStartMsg());
		}

		private String getGameStartMsg() {
			if (languageList.getSelectedValue().equalsIgnoreCase("English")) {
				return "The game between " + game.getPlayer1Name() + "(X) and " + game.getPlayer2Name() + "(O) started\n";
			} else {
				return "Начинаем новую игру " + game.getPlayer1Name() + "(X) и " + game.getPlayer2Name() + "(O)\n";
			}
		}

		private String getDificultyLevelError() {
			if (languageList.getSelectedValue().equalsIgnoreCase("English")) {
				return "Incorrect difficulty level, the default one will be used\n";
			} else {
				return "Неправильный уровень сложности, использую стандартный\n";
			}
		}
	}

	private class MyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = -731431570472728743L;
		private int[][] winning = null;

		private String[] columnNames = {"", "1", "2", "3"};
		private Object[][] data = {
				{"1", null,null,null},
				{"2", null,null,null},
				{"3", null,null,null}
		};

		public void setWinning(final int[][] winRow) {
			winning = winRow;
		}

		public boolean isWin(final int x, final int y) {

			boolean result = false;
			if (winning != null) {
				if (winning[0][0] == x && winning[0][1] == y) {
					result = true;
				}
				if (winning[1][0] == x && winning[1][1] == y) {
					result = true;
				}
				if (winning[2][0] == x && winning[2][1] == y) {
					result = true;
				}
			}

			return result;
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			return data.length;
		}

        @Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

        @Override
		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

        @Override
        public void setValueAt(Object obj, int row, int col) {
        	data[row][col] = obj;
        	fireTableCellUpdated(row, col);
        }
	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	public void createAndShowGUI() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(rootPanel);

		pack();
		setVisible(true);
	}
}
