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
	// Attributes
	private GameClient gameClient = null;
	private BaseGameGUI gamegui = null;
	private String userName;
	private String passwd;
	private String whiteUser;
	private String blackUser;
	private Board board;
	private Board previous;
	private boolean white;
	private String firstPlayer;

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

	// Constructors
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

	// Getters and Setters

	public BaseGameGUI getGamegui() {
		return gamegui;
	}

	public void setGamegui(BaseGameGUI gamegui) {
		this.gamegui = gamegui;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getWhiteUser() {
		return whiteUser;
	}

	public void setWhiteUser(String whiteUser) {
		this.whiteUser = whiteUser;
	}

	public String getBlackUser() {
		return blackUser;
	}

	public void setBlackUser(String blackUser) {
		this.blackUser = blackUser;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Board getPrevious() {
		return previous;
	}

	public void setPrevious(Board previous) {
		this.previous = previous;
	}

	public boolean isWhite() {
		return white;
	}

	public void setWhite(boolean white) {
		this.white = white;
	}

	public String getFirstPlayer() {
		return firstPlayer;
	}

	public void setFirstPlayer(String firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

	public void setGameClient(GameClient gameClient) {
		this.gameClient = gameClient;
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

	// Methods
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

			this.getGameGUI().setGameState((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE));
			this.setBoard(new Board());
			System.out.println("This is our initial board");
			this.getBoard().printBoard();

			break;
		case GameMessage.GAME_ACTION_MOVE:
			
			this.getGameGUI().updateGameState(msgDetails);

			ArrayList<Integer> QueenPosCurEnemey = (ArrayList<Integer>) msgDetails
					.get(AmazonsGameMessage.QUEEN_POS_CURR);
			ArrayList<Integer> QueenPosNewEnemey = (ArrayList<Integer>) msgDetails
					.get(AmazonsGameMessage.Queen_POS_NEXT);
			ArrayList<Integer> ArrowPosEnemey = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
			this.setBoard(this.getBoard().updateGameBoard(this.board, QueenPosCurEnemey, QueenPosNewEnemey,
					ArrowPosEnemey, true));
			System.out.println("Board before our move");
			this.getBoard().printBoard();
			Agent agent = new Agent(QueenPosCurEnemey, QueenPosNewEnemey, ArrowPosEnemey, this.getBoard(), this.isWhite());

			agent.legalityCheck(this.getPrevious());
			agent.makeMove();
			if (agent.isOutOfMoves() == true) {
				System.out.println("I am out of moves");
				break;
			}
			System.out.println("Board after move");
			agent.getBoard().printBoard();
			this.setBoard(this.getBoard().updateGameBoard(this.board, agent.getQueenPosCurSend(), agent.getQueenPosNewSend(),
					agent.getArrowPosSend(), true)); 
			this.getGameGUI().updateGameState(agent.getQueenPosCurSend(), agent.getQueenPosNewSend(),
					agent.getArrowPosSend());
			this.getGameClient().sendMoveMessage(agent.getQueenPosCurSend(), agent.getQueenPosNewSend(), agent.getArrowPosSend());
			this.setPrevious((Board) this.board.clone());
			break;

		case GameMessage.GAME_ACTION_START:
			// Set who is white and black and who moves first
			this.setFirstPlayer("white");
			this.setBlackUser((String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK));
			this.setWhiteUser((String) msgDetails.get(AmazonsGameMessage.PLAYER_WHITE));
			this.setWhite((this.whiteUser.equals(this.userName)) ? true : false);
			// Make first move if your colo moves first and you are that color
			System.out.println("Board before move");
			this.board.printBoard();
			Agent agentF = new Agent(new Board(), this.white);
			if (this.firstPlayer.equals("white") && this.white) {
				agentF.makeMove();
				System.out.println("Board after move");
				agentF.getBoard().printBoard();
				this.board = this.board.updateGameBoard(this.board, agentF.getQueenPosCurSend(),
						agentF.getQueenPosNewSend(), agentF.getArrowPosSend(), true);
				this.gamegui.updateGameState(agentF.getQueenPosCurSend(), agentF.getQueenPosNewSend(),
						agentF.getArrowPosSend());
				gameClient.sendMoveMessage(agentF.getQueenPosCurSend(), agentF.getQueenPosNewSend(),
						agentF.getArrowPosSend());
			} else if (this.firstPlayer.equals("black") && !this.white) {
				agentF.makeMove();
				System.out.println("Board after move");
				agentF.getBoard().printBoard();
				this.board = this.board.updateGameBoard(this.board, agentF.getQueenPosCurSend(),
						agentF.getQueenPosNewSend(), agentF.getArrowPosSend(), true);
				this.gamegui.updateGameState(agentF.getQueenPosCurSend(), agentF.getQueenPosNewSend(),
						agentF.getArrowPosSend());
				gameClient.sendMoveMessage(agentF.getQueenPosCurSend(), agentF.getQueenPosNewSend(),
						agentF.getArrowPosSend());

			}
			// Keep track of board before any move to compare boards for legality
			this.previous = (Board) this.board.clone();

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
	public void connect() {
		// TODO Auto-generated method stub
		gameClient = new GameClient(userName, passwd, this);
	}

}// end of class