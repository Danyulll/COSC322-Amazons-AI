package ubc.cosc322;

import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import sfs2x.client.entities.Room;
import ubc.cosc322.Tree.Node;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * An example illustrating how to implement a GamePlayer
 * 
 * @author Yong Gao (yong.gao@ubc.ca) Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer {

	private GameClient gameClient = null;
	private BaseGameGUI gamegui = null;

	private String userName;
	private String passwd;

	public String whiteUser;
	public String blackUser;
	public Board board;

	/**
	 * The main method
	 * 
	 * @param args for name and passwd (current, any string would work)
	 */
	public static void main(String[] args) {
		COSC322Test player = new COSC322Test(args[0], args[1]);
		// HumanPlayer player = new HumanPlayer();

		if (player.getGameGUI() == null) {
			player.Go();
		} else {
			BaseGameGUI.sys_setup();
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					player.Go();
				}
			});
		}
	}

	/**
	 * Any name and passwd
	 * 
	 * @param userName
	 * @param passwd
	 */
	public COSC322Test(String userName, String passwd) {
		this.userName = userName;
		this.passwd = passwd;

		// To make a GUI-based player, create an instance of BaseGameGUI
		// and implement the method getGameGUI() accordingly
		this.gamegui = new BaseGameGUI(this);

	}

	@Override
	public void onLogin() {
		/*
		 * System.out.println("Congratualations!!! " +
		 * "I am called because the server indicated that the login is successfully");
		 * System.out.println("The next step is to find a room and join it: " +
		 * "the gameClient instance created in my constructor knows how!"); List<Room>
		 * rooms = this.gameClient.getRoomList(); for (Room room: rooms) {
		 * System.out.println(room); } this.gameClient.joinRoom(rooms.get(2).getName());
		 */
		this.userName = gameClient.getUserName();
		if (gamegui != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}

		System.out.println("inital board made");
	}

	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {

		// This method will be called by the GameClient when it receives a game-related
		// message
		// from the server.

		// For a detailed description of the message types and format,
		// see the method GamePlayer.handleGameMessage() in the game-client-api
		// document.
		System.out.println(messageType);
		System.out.println(msgDetails);
		switch (messageType) {
		case GameMessage.GAME_STATE_BOARD:
			// System.out.println("case state board");
			// System.out.println(msgDetails.toString());
			// System.out.println(msgDetails.get("game-state"));
			this.gamegui.setGameState((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE));
			this.board = new Board();
			System.out.println("This is our initial board");
			board.printBoard();

			break;
		case GameMessage.GAME_ACTION_MOVE:
			boolean white = (this.whiteUser.equals(this.userName)) ? true : false;
			// If we are player one make a move
			if (white) { // TODO for white we should not have to receive a move to calcualte our move
							// however currently the match does not start when 2 AI's sit in a room so we
							// are temporarily receiving the enemy move
				this.gamegui.updateGameState(msgDetails);

				// recieve black's move
				ArrayList<Integer> QueenPosCurEnemey = (ArrayList<Integer>) msgDetails
						.get(AmazonsGameMessage.QUEEN_POS_CURR);
				ArrayList<Integer> QueenPosNewEnemey = (ArrayList<Integer>) msgDetails
						.get(AmazonsGameMessage.Queen_POS_NEXT);
				ArrayList<Integer> ArrowPosEnemey = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
				this.board.updateGameBoard(board, QueenPosCurEnemey, QueenPosNewEnemey, ArrowPosEnemey, true);

				System.out.println("I am player one (white)");

				System.out.println("Cloned Board");
				Board boardBeforeMove = (Board) board.clone();
				boardBeforeMove.printBoard();

				// Construct initial Game tree (broken for white player)
				System.out.println("Constructing tree");
				Tree partial = new Tree();
				// partial = partial.generatePartialGameTree();

				// TODO game tree lack states where the queen moves and shoots the square it was
				// just on and has states where queens don't move but do shoot
				partial.generatePartialGameTree(boardBeforeMove, white, 2, partial.getRoot());

				// Make move decision
				System.out.println("Board before move:");
				boardBeforeMove.printBoard();
				double minimax = minimax(partial.getRoot(), 2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
						white);
				Board moveToMake = new Board();
				for (Node node : partial.getRoot().getChildren()) {
					if (minimax == node.getValue()) {
						moveToMake = node.getBoard();

					}
				}

				System.out.println("Minimax decision");
				moveToMake.printBoard();
				System.out.println("its heuristic board");
				TerritoryHeuristic heur = new TerritoryHeuristic();
				heur.printHeuristic(heur.closestQueen(moveToMake));
				System.out.println("Calculated value of state");
				System.out.println(heur.value(moveToMake));

				// Get Move Coords
				int[] oldWhiteQueenCoord = new int[2];
				int[] newWhiteQueenCoord = new int[2];
				int[] newArrowcoord = new int[2];

				for (int i = 0; i < boardBeforeMove.board.length; i++) {
					for (int j = 0; j < boardBeforeMove.board.length; j++) {
						if (moveToMake.board[i][j] != boardBeforeMove.board[i][j]) {
							if (moveToMake.board[i][j] == 3 && boardBeforeMove.board[i][j] != 3) {
								newArrowcoord[0] = i;
								newArrowcoord[1] = j;
							}
							if (boardBeforeMove.board[i][j] == 0 && moveToMake.board[i][j] == 1) {
								newWhiteQueenCoord[0] = i;
								newWhiteQueenCoord[1] = j;
							}

							if (moveToMake.board[i][j] == 0 && boardBeforeMove.board[i][j] == 1) {
								oldWhiteQueenCoord[0] = i;
								oldWhiteQueenCoord[1] = j;
							}
						}
					}
				}
				ArrayList<Integer> QueenNew = new ArrayList<Integer>();
				QueenNew.add(newWhiteQueenCoord[0]);
				QueenNew.add(newWhiteQueenCoord[1]);
				ArrayList<Integer> Arrow = new ArrayList<Integer>();
				Arrow.add(newArrowcoord[0]);
				Arrow.add(newArrowcoord[1]);
				ArrayList<Integer> QueenOld = new ArrayList<>();
				QueenOld.add(oldWhiteQueenCoord[0]);
				QueenOld.add(oldWhiteQueenCoord[1]);

				// Convert To sendable coords
				HashMap<ArrayList<Integer>, ArrayList<Integer>> gaoTable = Board.makeGaoTable();

				// Send Move and update GUI
				ArrayList<Integer> QueenPosCurSend = new ArrayList<>();
				QueenPosCurSend = gaoTable.get(QueenOld);
				ArrayList<Integer> QueenPosNewSend = new ArrayList<>();
				QueenPosNewSend = gaoTable.get(QueenNew);
				ArrayList<Integer> ArrowPosSend = new ArrayList<>();
				ArrowPosSend = gaoTable.get(Arrow);

				gameClient.sendMoveMessage(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);

				this.board.updateGameBoard(this.board, QueenPosCurSend, QueenPosNewSend, ArrowPosSend, true);
				this.gamegui.updateGameState(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);
			} else if (!white) { // If we are player 2 wait to receive a move and then make a move
				this.gamegui.updateGameState(msgDetails);
				System.out.println("I am player 2 (black)");

				// wait to receive move
				ArrayList<Integer> QueenPosCurEnemey = (ArrayList<Integer>) msgDetails
						.get(AmazonsGameMessage.QUEEN_POS_CURR);
				ArrayList<Integer> QueenPosNewEnemey = (ArrayList<Integer>) msgDetails
						.get(AmazonsGameMessage.Queen_POS_NEXT);
				ArrayList<Integer> ArrowPosEnemey = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
				this.board.updateGameBoard(board, QueenPosCurEnemey, QueenPosNewEnemey, ArrowPosEnemey, true);

				System.out.println("Currently tracked Board: ");
				this.board.printBoard();

				System.out.println("Cloned Board");
				Board boardBeforeMove = (Board) board.clone();
				boardBeforeMove.printBoard();

				// Construct initial Game tree
				System.out.println("Constructing tree");
				Tree partial = new Tree();
				partial.getRoot().setBoard(boardBeforeMove);
				// TODO game tree lack states where the queen moves and shoots the square it was
				// just on and has states where queens don't move but do shoot
				partial.generatePartialGameTree(boardBeforeMove, white, 2, partial.getRoot());

				// Make move decision
				System.out.println("Board before move:");
				boardBeforeMove.printBoard();
				double minimax = minimax(partial.getRoot(), 2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
						white);
				Board moveToMake = new Board();
				for (Node node : partial.getRoot().getChildren()) {
					if (minimax == node.getValue()) {
						moveToMake = node.getBoard();

					}
				}

				System.out.println("Minimax decision");
				moveToMake.printBoard();
				System.out.println("its heuristic board");
				TerritoryHeuristic heur = new TerritoryHeuristic();
				heur.printHeuristic(heur.closestQueen(moveToMake));
				System.out.println("Calculated value of state");
				System.out.println(heur.value(moveToMake));

				// Get Move Coords
				int[] oldWhiteQueenCoord = new int[2];
				int[] newWhiteQueenCoord = new int[2];
				int[] newArrowcoord = new int[2];

				for (int i = 0; i < boardBeforeMove.board.length; i++) {
					for (int j = 0; j < boardBeforeMove.board.length; j++) {
						if (moveToMake.board[i][j] != boardBeforeMove.board[i][j]) {
							if (moveToMake.board[i][j] == 3 && boardBeforeMove.board[i][j] != 3) {
								newArrowcoord[0] = i;
								newArrowcoord[1] = j;
							}
							if (boardBeforeMove.board[i][j] == 0 && moveToMake.board[i][j] == 2) {
								newWhiteQueenCoord[0] = i;
								newWhiteQueenCoord[1] = j;
							}

							if (moveToMake.board[i][j] == 0 && boardBeforeMove.board[i][j] == 2) {
								oldWhiteQueenCoord[0] = i;
								oldWhiteQueenCoord[1] = j;
							}
						}
					}
				}
				ArrayList<Integer> QueenNew = new ArrayList<Integer>();
				QueenNew.add(newWhiteQueenCoord[0]);
				QueenNew.add(newWhiteQueenCoord[1]);
				ArrayList<Integer> Arrow = new ArrayList<Integer>();
				Arrow.add(newArrowcoord[0]);
				Arrow.add(newArrowcoord[1]);
				ArrayList<Integer> QueenOld = new ArrayList<>();
				QueenOld.add(oldWhiteQueenCoord[0]);
				QueenOld.add(oldWhiteQueenCoord[1]);
				System.out.println("oldWhiteQueenCoord Array: " + oldWhiteQueenCoord[0] + "," + oldWhiteQueenCoord[1]);
				System.out.println("newWhiteQueenCoord Array: " + newWhiteQueenCoord[0] + "," + newWhiteQueenCoord[1]);
				System.out.println("newArrowcoord Array: " + newArrowcoord[0] + "," + newArrowcoord[1]);

				// Convert Coords for move to send
				HashMap<ArrayList<Integer>, ArrayList<Integer>> gaoTable = Board.makeGaoTable();
				ArrayList<Integer> QueenPosCurSend = new ArrayList<>();
				QueenPosCurSend = gaoTable.get(QueenOld);
				ArrayList<Integer> QueenPosNewSend = new ArrayList<>();
				QueenPosNewSend = gaoTable.get(QueenNew);
				ArrayList<Integer> ArrowPosSend = new ArrayList<>();
				ArrowPosSend = gaoTable.get(Arrow);

				// send game move and update gui
				gameClient.sendMoveMessage(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);
				this.board.updateGameBoard(this.board, QueenPosCurSend, QueenPosNewSend, ArrowPosSend, true);
				this.gamegui.updateGameState(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);
				System.out.println(
						"Current Queen Position being sent: " + QueenPosCurSend.get(0) + "," + QueenPosCurSend.get(1));
				System.out.println(
						"Neww Queen Position being sent: " + QueenPosNewSend.get(0) + "," + QueenPosNewSend.get(1));
				System.out.println("New Arrow being sent: " + ArrowPosSend.get(0) + "," + ArrowPosSend.get(1));
			}

			t = 30;
			Timer timer = new Timer();
			timer.schedule(new countDown(), 0, 5000);
			break;

		case GameMessage.GAME_ACTION_START:
//TODO send initial move in this case
			this.blackUser = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK);
			this.whiteUser = (String) msgDetails.get(AmazonsGameMessage.PLAYER_WHITE);
			// Figure out who is player 1
			 white = (this.whiteUser.equals(this.userName)) ? true : false;

			if (white) {
				this.board = new Board();
				Tree partial = new Tree();
				partial.generatePartialGameTree(this.board, white, 2, partial.getRoot());
				double minimax = minimax(partial.getRoot(), 2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
						white);
				Board moveToMake = new Board();
				for (Node node : partial.getRoot().getChildren()) {
					if (minimax == node.getValue()) {
						moveToMake = node.getBoard();

					}
				}
				Board boardBeforeMove = (Board )this.board.clone();

				// Get Move Coords
				int[] oldWhiteQueenCoord = new int[2];
				int[] newWhiteQueenCoord = new int[2];
				int[] newArrowcoord = new int[2];

				for (int i = 0; i < boardBeforeMove.board.length; i++) {
					for (int j = 0; j < boardBeforeMove.board.length; j++) {
						if (moveToMake.board[i][j] != boardBeforeMove.board[i][j]) {
							if (moveToMake.board[i][j] == 3 && boardBeforeMove.board[i][j] != 3) {
								newArrowcoord[0] = i;
								newArrowcoord[1] = j;
							}
							if (boardBeforeMove.board[i][j] == 0 && moveToMake.board[i][j] == 2) {
								newWhiteQueenCoord[0] = i;
								newWhiteQueenCoord[1] = j;
							}

							if (moveToMake.board[i][j] == 0 && boardBeforeMove.board[i][j] == 2) {
								oldWhiteQueenCoord[0] = i;
								oldWhiteQueenCoord[1] = j;
							}
						}
					}
				}
				ArrayList<Integer> QueenNew = new ArrayList<Integer>();
				QueenNew.add(newWhiteQueenCoord[0]);
				QueenNew.add(newWhiteQueenCoord[1]);
				ArrayList<Integer> Arrow = new ArrayList<Integer>();
				Arrow.add(newArrowcoord[0]);
				Arrow.add(newArrowcoord[1]);
				ArrayList<Integer> QueenOld = new ArrayList<>();
				QueenOld.add(oldWhiteQueenCoord[0]);
				QueenOld.add(oldWhiteQueenCoord[1]);
				System.out.println("oldWhiteQueenCoord Array: " + oldWhiteQueenCoord[0] + "," + oldWhiteQueenCoord[1]);
				System.out.println("newWhiteQueenCoord Array: " + newWhiteQueenCoord[0] + "," + newWhiteQueenCoord[1]);
				System.out.println("newArrowcoord Array: " + newArrowcoord[0] + "," + newArrowcoord[1]);

				// Convert Coords for move to send
				HashMap<ArrayList<Integer>, ArrayList<Integer>> gaoTable = Board.makeGaoTable();
				ArrayList<Integer> QueenPosCurSend = new ArrayList<>();
				QueenPosCurSend = gaoTable.get(QueenOld);
				ArrayList<Integer> QueenPosNewSend = new ArrayList<>();
				QueenPosNewSend = gaoTable.get(QueenNew);
				ArrayList<Integer> ArrowPosSend = new ArrayList<>();
				ArrowPosSend = gaoTable.get(Arrow);

				// send game move and update gui
				gameClient.sendMoveMessage(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);
				this.board.updateGameBoard(this.board, QueenPosCurSend, QueenPosNewSend, ArrowPosSend, true);
				this.gamegui.updateGameState(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);
				System.out.println(
						"Current Queen Position being sent: " + QueenPosCurSend.get(0) + "," + QueenPosCurSend.get(1));
				System.out.println(
						"Neww Queen Position being sent: " + QueenPosNewSend.get(0) + "," + QueenPosNewSend.get(1));
				System.out.println("New Arrow being sent: " + ArrowPosSend.get(0) + "," + ArrowPosSend.get(1));

			}
			// Print names of players and what color they are

			// System.out.println("case action start");
			// System.out.println(msgDetails.toString());
			// System.out.println((ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.GAME_STATE));
			// System.out.println(AmazonsGameMessage.PLAYER_BLACK);
			// System.out.println(AmazonsGameMessage.PLAYER_WHITE);

		default:
			break;
		}
		return true;
	}

	@Override
	public String userName() {
		return userName;
	}

	@Override
	public GameClient getGameClient() {
		// TODO Auto-generated method stub
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		// TODO Auto-generated method stub
		return this.gamegui;
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		gameClient = new GameClient(userName, passwd, this);
	}

	int t = 30;

	class countDown extends TimerTask {
		public void run() {
			if (t > 0) {
				System.out.println(t + " seconds left");
				t -= 5;
			}

		}

	}

	public double minimax(Node current, int depth, double alpha, double beta, boolean maxPlayer) {
		// Node parent = current.getParent();

		if (depth == 0) {
			TerritoryHeuristic heur = new TerritoryHeuristic();
			return heur.value(current.getBoard());
		}
		if (maxPlayer) {
			double maxEval = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < current.getChildren().size(); i++) {
				double eval = minimax(current.getChildren().get(i), depth - 1, alpha, beta, false);
				maxEval = Math.max(maxEval, eval);
				alpha = Math.max(alpha, eval);
				/*
				 * if (beta <= alpha) {
				 * 
				 * break; }
				 */
			}
			current.setValue(maxEval);
			return maxEval;
		} else {
			double minEval = Double.POSITIVE_INFINITY;
			for (int i = 0; i < current.getChildren().size(); i++) {
				double eval = minimax(current.getChildren().get(i), depth - 1, alpha, beta, true);
				minEval = Math.min(minEval, eval);
				beta = Math.min(beta, eval);
				/*
				 * if (beta <= alpha) {
				 * 
				 * break; }
				 */

			}
			current.setValue(minEval);
			return minEval;
		}

	}

	public double value(Board board) {
		TerritoryHeuristic heur = new TerritoryHeuristic();
		int[][] scoreBoard = heur.closestQueen(board);

		double sum = 0;
		for (int i = 0; i < scoreBoard.length; i++) {
			for (int j = 0; j < scoreBoard[i].length; j++) {
				if (scoreBoard[i][j] == 1) {
					sum += 1;
				} else if (scoreBoard[i][j] == 2) {
					sum -= 1;
				}
			}
		}
		return sum;
	}

}// end of class
