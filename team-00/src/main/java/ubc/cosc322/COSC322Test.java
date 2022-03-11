package ubc.cosc322;

import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;
import java.util.Timer;
import java.util.TimerTask;

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

			break;
		case GameMessage.GAME_ACTION_MOVE:

			this.gamegui.updateGameState(msgDetails);
			// Figure out who is player 1
			boolean white = (this.whiteUser.equals(this.userName)) ? true : false;

			Board board = new Board();
			
			// If we are player one make a move
			if (white) {
				System.out.println("I am player one (white)");
				Board boardBeforeMove = (Board) board.clone();
				//Construct initial Game tree
				System.out.println("Constructing tree");
				Tree partial = new Tree();
				partial = partial.generatePartialGameTree(board, white);
				System.out.println("Selecting Board");
				
				//Make move decision
				
				System.out.println("original board:");
				boardBeforeMove.printBoard();
				
				Board moveToMake = partial.getRoot().getChildren().get(100).getBoard();
				System.out.println("printing board move chosen");
				moveToMake.printBoard();
				
				//TODO turn this chosen board into coords that I can send to the server
				//Get Move Coords
				int[] oldWhiteQueenCoord = new int[2]; 
				int[] newWhiteQueenCoord = new int[2];			
				int[] newArrowcoord = new int[2];
				
				for (int i = 0; i < boardBeforeMove.board.length; i++) {
					for (int j = 0; j < boardBeforeMove.board.length; j++) {
						if(moveToMake.board[i][j] != boardBeforeMove.board[i][j]) {
							if(moveToMake.board[i][j]==3) {
								newArrowcoord[0] = i;
								newArrowcoord[1] = j;
							}
							else if(boardBeforeMove.board[i][j] ==0 && moveToMake.board[i][j]==1) {
								newWhiteQueenCoord[0] = i;
								newWhiteQueenCoord[1] = j;
							}
						
						if(moveToMake.board[i][j]==0 && boardBeforeMove.board[i][j]==1) {
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
				//System.out.println("New ArrowCoord = " + newArrowcoord[0] + ","+newArrowcoord[1]);
				//System.out.println("New QueenCoord = " + newWhiteQueenCoord[0] + ","+newWhiteQueenCoord[1]);
				//Convert To sendable coords
				HashMap<ArrayList<Integer>,ArrayList<Integer>> gaoTable = Board.makeGaoTable();
				
				//Send Move
				ArrayList<Integer> QueenPosCurSend = new ArrayList<>();
				QueenPosCurSend = gaoTable.get(QueenOld);
				
				ArrayList<Integer> QueenPosNewSend = new ArrayList<>();
				QueenPosNewSend = gaoTable.get(QueenNew);
				
				ArrayList<Integer> ArrowPosSend = new ArrayList<>();
				ArrowPosSend = gaoTable.get(Arrow);
				
				
				
				
				gameClient.sendMoveMessage(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);
				this.gamegui.updateGameState(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);
			} else if (!white) { // If we are player 2 wait to recieve a move and then make a move
				System.out.println("I am player 2 (black)");
				
				//wait to recieve move
				
				//construct game tree
				
				//make move decision
				
				//send move
			}

			/*ArrayList<Integer> QueenPosCurEnemey = (ArrayList<Integer>) msgDetails
					.get(AmazonsGameMessage.QUEEN_POS_CURR);
			ArrayList<Integer> QueenPosNewEnemey = (ArrayList<Integer>) msgDetails
					.get(AmazonsGameMessage.Queen_POS_NEXT);
			ArrayList<Integer> ArrowPosEnemey = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);

			//board.printBoard();

			// Calculate Heuristic and print
			TerritoryHeuristic heur = new TerritoryHeuristic();
			TerritoryHeuristic.printHeuristic(heur.closestQueen(board));
			// Update game board
			board.updateGameBoard(board, QueenPosCurEnemey, QueenPosNewEnemey, ArrowPosEnemey,true);

			// Our positions to send

			ArrayList<Integer> QueenPosCurSend = new ArrayList<>();
			ArrayList<Integer> QueenPosNewSend = new ArrayList<>();
			ArrayList<Integer> ArrowPosSend = new ArrayList<>();

			QueenPosCurSend.add(4); // row [4,1]
			QueenPosCurSend.add(1); // col

			QueenPosNewSend.add(5);
			QueenPosNewSend.add(2); // [5,2]

			ArrowPosSend.add(5);
			ArrowPosSend.add(3);

			// print the board after all the moves have been made
			board.updateGameBoard(board, QueenPosCurSend, QueenPosNewSend, ArrowPosSend,true);
			board.printBoard();

			// Calculate Heuristic and print
			TerritoryHeuristic.printHeuristic(heur.closestQueen(board));

			// Sending the positions
			gameClient.sendMoveMessage(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);

			this.gamegui.updateGameState(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);
			*/t = 30;
			Timer timer = new Timer();
			timer.schedule(new countDown(), 0, 5000);
			break;

		case GameMessage.GAME_ACTION_START:

			// Print names of players and what color they are

			// System.out.println("case action start");
			// System.out.println(msgDetails.toString());
			// System.out.println((ArrayList<Integer>)msgDetails.get(AmazonsGameMessage.GAME_STATE));
			// System.out.println(AmazonsGameMessage.PLAYER_BLACK);
			// System.out.println(AmazonsGameMessage.PLAYER_WHITE);

			this.blackUser = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK);
			this.whiteUser = (String) msgDetails.get(AmazonsGameMessage.PLAYER_WHITE);
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

}// end of class
