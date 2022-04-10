package ubc.cosc322;

import java.util.*;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

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
	public boolean white;
	public String firstPlayer;

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

			this.gamegui.setGameState((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE));
			this.board = new Board();
			System.out.println("This is our initial board");
			board.printBoard();

			break;
		case GameMessage.GAME_ACTION_MOVE:
			this.white = (this.whiteUser.equals(this.userName)) ? true : false;
			this.gamegui.updateGameState(msgDetails);
			// If we are player one make a move
			ArrayList<Integer> QueenPosCurEnemey = (ArrayList<Integer>) msgDetails
					.get(AmazonsGameMessage.QUEEN_POS_CURR);
			ArrayList<Integer> QueenPosNewEnemey = (ArrayList<Integer>) msgDetails
					.get(AmazonsGameMessage.Queen_POS_NEXT);
			ArrayList<Integer> ArrowPosEnemey = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
			this.board=this.board.updateGameBoard(this.board,QueenPosCurEnemey, QueenPosNewEnemey, ArrowPosEnemey, true);
			System.out.println("Board before our move");
			this.board.printBoard();
			Agent agent = new Agent(QueenPosCurEnemey, QueenPosNewEnemey, ArrowPosEnemey, board, white);
	
			//TODO this doesn't owrk right now
			//agent.legalityCheck();
			agent.makeMove();
			System.out.println("Board after move");
			agent.getBoard().printBoard();
			this.gamegui.updateGameState(agent.getQueenPosCurSend(), agent.getQueenPosNewSend(),
					agent.getArrowPosSend());
			gameClient.sendMoveMessage(agent.getQueenPosCurSend(), agent.getQueenPosNewSend(), agent.getArrowPosSend());

			break;

		case GameMessage.GAME_ACTION_START:
			this.firstPlayer = "white";
			this.blackUser = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK);
			this.whiteUser = (String) msgDetails.get(AmazonsGameMessage.PLAYER_WHITE);
			// Figure out who is player 1
			this.white = (this.whiteUser.equals(this.userName)) ? true : false;
			System.out.println("Board before move");
			this.board.printBoard();
			Agent agentF = new Agent(new Board(), this.white);
			if (this.firstPlayer.equals("white") && this.white) {
				agentF.makeMove();
				System.out.println("Board after move");
				agentF.getBoard().printBoard();
				this.board=this.board.updateGameBoard(this.board, agentF.getQueenPosCurSend(), agentF.getQueenPosNewSend(), agentF.getArrowPosSend(), true);
				this.gamegui.updateGameState(agentF.getQueenPosCurSend(), agentF.getQueenPosNewSend(),
						agentF.getArrowPosSend());
				gameClient.sendMoveMessage(agentF.getQueenPosCurSend(), agentF.getQueenPosNewSend(),
						agentF.getArrowPosSend());		
			} else if (this.firstPlayer.equals("black") && !this.white) {
				agentF.makeMove();
				System.out.println("Board after move");
				agentF.getBoard().printBoard();
				this.board=this.board.updateGameBoard(this.board, agentF.getQueenPosCurSend(), agentF.getQueenPosNewSend(), agentF.getArrowPosSend(), true);
				this.gamegui.updateGameState(agentF.getQueenPosCurSend(), agentF.getQueenPosNewSend(),
						agentF.getArrowPosSend());
				gameClient.sendMoveMessage(agentF.getQueenPosCurSend(), agentF.getQueenPosNewSend(),
						agentF.getArrowPosSend());

			}
		
			break;

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

	public Board minimax(Node current, int depth) {
		Node move = MaxValue(current, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		System.out.println("Board minimax is about to return");
		move.getBoard().printBoard();
		return move.getBoard();
	}

	public Node MaxValue(Node current, int depth, double alpha, double beta) {
		if (depth == 0) {
			current.value = value(current.getBoard(), this.white);

			return current;
		}
		Node v = new Node(null); // v has a default board
		v.value = Double.NEGATIVE_INFINITY;
		for (Node node : current.getChildren()) {
			Node v2 = MinValue(node, depth - 1, alpha, beta);
			if (v2.value > v.value) {
				v.value = v2.value;
				v.setBoard(node.getBoard());
				alpha = Math.max(alpha, v.value);
			}
			if (v.value >= beta) {
				return v;
			}

		}

		return v;

	}

	public Node MinValue(Node current, int depth, double alpha, double beta) {
		if (depth == 0) {
			current.value = value(current.getBoard(), this.white);

			return current;
		}
		Node v = new Node(null);
		v.value = Double.POSITIVE_INFINITY;
		for (Node node : current.getChildren()) {
			Node v2 = MaxValue(node, depth - 1, alpha, beta);
			if (v2.value < v.value) {
				v.value = v2.value;
				v.setBoard(node.getBoard());
				beta = Math.min(beta, v.value);
			}
			if (v.value <= alpha) {
				return v;
			}

		}

		return v;

	}

	public double value(Board board, boolean max) {
		TerritoryHeuristic heur = new TerritoryHeuristic();
		int[][] scoreBoard = heur.closestQueen(board);

		double sum = 0;
		for (int i = 0; i < scoreBoard.length; i++) {
			for (int j = 0; j < scoreBoard[i].length; j++) {
				if (scoreBoard[i][j] == 1) {
					if (max) {
						sum += 1;
					} else {
						sum -= 1;
					}
				} else if (scoreBoard[i][j] == 2) {
					if (max) {
						sum -= 1;
					} else {
						sum += 1;
					}
				}
			}
		}
		return sum;
	}

}// end of class