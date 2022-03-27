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
			// System.out.println("case state board");
			// System.out.println(msgDetails.toString());
			// System.out.println(msgDetails.get("game-state"));

			this.gamegui.setGameState((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE));
			this.board = new Board();
			System.out.println("This is our initial board");
			board.printBoard();

			break;
		case GameMessage.GAME_ACTION_MOVE:
			this.white = (this.whiteUser.equals(this.userName)) ? true : false;
			System.out.println("initally updating game gui");
			this.gamegui.updateGameState(msgDetails);
			// If we are player one make a move
			if (white) {
				System.out.println("recieved a move");
				// recieve black's move
				ArrayList<Integer> QueenPosCurEnemey = (ArrayList<Integer>) msgDetails
						.get(AmazonsGameMessage.QUEEN_POS_CURR);
				ArrayList<Integer> QueenPosNewEnemey = (ArrayList<Integer>) msgDetails
						.get(AmazonsGameMessage.Queen_POS_NEXT);
				ArrayList<Integer> ArrowPosEnemey = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);

				System.out.println(
						"Enmey Queen's old position: " + QueenPosCurEnemey.get(0) + "," + QueenPosCurEnemey.get(1));
				System.out.println(
						"Enmey Queen's New position: " + QueenPosNewEnemey.get(0) + "," + QueenPosNewEnemey.get(1));
				System.out
						.println("Enmey Arrow's New position: " + ArrowPosEnemey.get(0) + "," + ArrowPosEnemey.get(1));
				// check if move was legal
				// boolean illegalMove = IllegalMoveFinder.illegalMoveFound(QueenPosCurEnemey,
				// QueenPosNewEnemey,
				// ArrowPosEnemey, this.board);
				HashMap<ArrayList<Integer>, ArrayList<Integer>> table = Board.makeHashTable();
				LegalMove moveGetter = new LegalMove();
				boolean illegalQueenMove = true;
				ArrayList<Position> moves = moveGetter.getLegalMove(
						new Queen(
								new Position(table.get(QueenPosCurEnemey).get(0), table.get(QueenPosCurEnemey).get(1))),
						this.board);
				for (Position position : moves) {
					if(position.getX()==table.get(QueenPosNewEnemey).get(0) && position.getY() == table.get(QueenPosNewEnemey).get(1)) {
						illegalQueenMove = false;
						break;}
				}

				if (illegalQueenMove) {
					System.out.println("opponenet made an illegal queen move");
					System.out.println(
							"*************************************************************************************************************************************************************************************************************");
					break;
				}
				
				boolean illegalArrowMove = true;
				LegalArrow arrowGetter = new LegalArrow();
				Board temp = (Board) this.board.clone();
				temp.updateGameBoard(temp, QueenPosCurEnemey, QueenPosNewEnemey, true);
				ArrayList<Position> arrowMoves = arrowGetter.getLegalArrow(table.get(QueenPosNewEnemey).get(0), table.get(QueenPosNewEnemey).get(1), temp);
				
				for (Position position2 : arrowMoves) {
					if(position2.getX()==table.get(ArrowPosEnemey).get(0) && position2.getY() == table.get(ArrowPosEnemey).get(1)) {
						illegalArrowMove = false;
						break;}
				}
				
				if (illegalArrowMove) {
					System.out.println("opponenet made an illegal arrow move");
					System.out.println(
							"*************************************************************************************************************************************************************************************************************");
					break;
				}
				
				

				// update local board storage
				this.board = this.board.updateGameBoard(board, QueenPosCurEnemey, QueenPosNewEnemey, ArrowPosEnemey,
						true);
				// clone board before move
				Board boardBeforeMove = (Board) board.clone();
				System.out.println("local board before move");
				boardBeforeMove.printBoard();

				Tree partial = new Tree();
				partial.getRoot().setBoard(boardBeforeMove);
				// TODO game tree lack states where the queen moves and shoots the square it was
				// just on and has states where queens don't move but do shoot
				System.out.println("Generating tree");
				partial.generatePartialGameTree(boardBeforeMove, white, 1, partial.getRoot());

				/*
				 * Board moveToMake = partial.getRoot().getChildren() .get(Math.max(0, ((int) (1
				 * + Math.random() * partial.getRoot().getChildren().size())) - 10))
				 * .getBoard(); System.out.println("move chosen"); moveToMake.printBoard();
				 */
				// Make move decision

				Board moveToMake = new Board();
				if (partial.getRoot().getChildren().size() == 0) {
					System.out.println("I am out of moves");
					break;
				}
				System.out.println("chossing move to make");
				moveToMake = minimax(partial.getRoot(), 1);
				System.out.println("Move to make board: ");
				moveToMake.printBoard();

				// Get Move Coords
				int[] oldWhiteQueenCoord = new int[2];
				int[] newWhiteQueenCoord = new int[2];
				int[] newArrowcoord = new int[2];

				for (int i = 0; i < boardBeforeMove.board.length; i++) {
					for (int j = 0; j < boardBeforeMove.board.length; j++) {

						if (moveToMake.board[i][j] == 3 && boardBeforeMove.board[i][j] != 3) {
							newArrowcoord[0] = i;
							newArrowcoord[1] = j;
							System.out.println("arrow called");
						}
						if (boardBeforeMove.board[i][j] == 0 && moveToMake.board[i][j] == 1) {
							newWhiteQueenCoord[0] = i;
							newWhiteQueenCoord[1] = j;
							System.out.println("newWHite called");
						}

						if (moveToMake.board[i][j] != 1 && boardBeforeMove.board[i][j] == 1) {
							System.out.println("***********************************");
							System.out.println("moveToMake taking coords: " + i + "," + j);
							oldWhiteQueenCoord[0] = i;
							oldWhiteQueenCoord[1] = j;
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

				System.out.println("coordinates before converting");
				System.out.println("QueenNew: " + QueenNew.get(0) + "," + QueenNew.get(1));
				System.out.println("QueenOld: " + QueenOld.get(0) + "," + QueenOld.get(1));
				System.out.println("Arrow: " + Arrow.get(0) + "," + Arrow.get(1));
				// Convert To sendable coords
				HashMap<ArrayList<Integer>, ArrayList<Integer>> gaoTable = Board.makeGaoTable();

				// Send Move and update GUI
				ArrayList<Integer> QueenPosCurSend = new ArrayList<>();
				QueenPosCurSend = gaoTable.get(QueenOld);
				ArrayList<Integer> QueenPosNewSend = new ArrayList<>();
				QueenPosNewSend = gaoTable.get(QueenNew);
				ArrayList<Integer> ArrowPosSend = new ArrayList<>();
				ArrowPosSend = gaoTable.get(Arrow);

				System.out.println("coordinates after converting");
				System.out.println("QueenNew: " + QueenPosNewSend.get(0) + "," + QueenPosNewSend.get(1));
				System.out.println("QueenOld: " + QueenPosCurSend.get(0) + "," + QueenPosCurSend.get(1));
				System.out.println("Arrow: " + ArrowPosSend.get(0) + "," + ArrowPosSend.get(1));

				System.out.println("locally stored board before the update");
				this.board.printBoard();

				this.board = this.board.updateGameBoard(this.board, QueenPosCurSend, QueenPosNewSend, ArrowPosSend,
						true);
				System.out.println("locally stored board after the update");
				this.board.printBoard();

				this.gamegui.updateGameState(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);
				gameClient.sendMoveMessage(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);

			} else if (!white) { // If we are player 2 wait to receive a move and then make
				// recieve black's move
				ArrayList<Integer> QueenPosCurEnemey = (ArrayList<Integer>) msgDetails
						.get(AmazonsGameMessage.QUEEN_POS_CURR);
				ArrayList<Integer> QueenPosNewEnemey = (ArrayList<Integer>) msgDetails
						.get(AmazonsGameMessage.Queen_POS_NEXT);
				ArrayList<Integer> ArrowPosEnemey = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
				// update local board storage
				this.board = this.board.updateGameBoard(board, QueenPosCurEnemey, QueenPosNewEnemey, ArrowPosEnemey,
						true);
				// clone board before move
				Board boardBeforeMove = (Board) board.clone();
				System.out.println("local board before move");
				boardBeforeMove.printBoard();
				// check if move was legal
				// boolean illegalMove = IllegalMoveFinder.illegalMoveFound(QueenPosCurEnemey,
				// QueenPosNewEnemey,
				// ArrowPosEnemey, this.board);
				HashMap<ArrayList<Integer>, ArrayList<Integer>> table = Board.makeHashTable();
				LegalMove moveGetter = new LegalMove();
				boolean illegalQueenMove = true;
				ArrayList<Position> moves = moveGetter.getLegalMove(
						new Queen(
								new Position(table.get(QueenPosCurEnemey).get(0), table.get(QueenPosCurEnemey).get(1))),
						this.board);
				for (Position position : moves) {
					if(position.getX()==table.get(QueenPosNewEnemey).get(0) && position.getY() == table.get(QueenPosNewEnemey).get(1)) {
						illegalQueenMove = false;
						break;}
				}

				if (illegalQueenMove) {
					System.out.println("opponenet made an illegal queen move");
					System.out.println(
							"*************************************************************************************************************************************************************************************************************");
					break;
				}
				
				boolean illegalArrowMove = true;
				LegalArrow arrowGetter = new LegalArrow();
				Board temp = (Board) this.board.clone();
				temp.updateGameBoard(temp, QueenPosCurEnemey, QueenPosNewEnemey, true);
				ArrayList<Position> arrowMoves = arrowGetter.getLegalArrow(table.get(QueenPosNewEnemey).get(0), table.get(QueenPosNewEnemey).get(1), temp);
				
				for (Position position2 : arrowMoves) {
					if(position2.getX()==table.get(ArrowPosEnemey).get(0) && position2.getY() == table.get(ArrowPosEnemey).get(1)) {
						illegalArrowMove = false;
						break;}
				}
				
				if (illegalArrowMove) {
					System.out.println("opponenet made an illegal arrow move");
					System.out.println(
							"*************************************************************************************************************************************************************************************************************");
					break;
				}
				
				
				Tree partial = new Tree();
				partial.getRoot().setBoard(boardBeforeMove);
				// TODO game tree lack states where the queen moves and shoots the square it was
				// just on and has states where queens don't move but do shoot

				partial.generatePartialGameTree(boardBeforeMove, white, 1, partial.getRoot());

				/*
				 * Board moveToMake = partial.getRoot().getChildren().get(0).getBoard();
				 * System.out.println("move chosen"); moveToMake.printBoard();
				 */
				// Make move decision

				Board moveToMake = new Board();
				if (partial.getRoot().getChildren().size() == 0) {
					System.out.println("I am out of moves");
					break;
				}
				System.out.println("board I am sending to minimax");
				partial.getRoot().getBoard().printBoard();
				moveToMake = minimax(partial.getRoot(), 1);
				System.out.println("Move to make board: ");
				moveToMake.printBoard();

				// Get Move Coords
				int[] oldBlackQueenCoord = new int[2];
				int[] newBlackQueenCoord = new int[2];
				int[] newArrowcoord = new int[2];

				for (int i = 0; i < boardBeforeMove.board.length; i++) {
					for (int j = 0; j < boardBeforeMove.board.length; j++) {

						if (moveToMake.board[i][j] == 3 && boardBeforeMove.board[i][j] != 3) {
							newArrowcoord[0] = i;
							newArrowcoord[1] = j;
							System.out.println("arrow called");
						}
						if (boardBeforeMove.board[i][j] == 0 && moveToMake.board[i][j] == 2) {
							newBlackQueenCoord[0] = i;
							newBlackQueenCoord[1] = j;
							System.out.println("newWHite called");
						}

						if (moveToMake.board[i][j] != 2 && boardBeforeMove.board[i][j] == 2) {
							System.out.println("***********************************");
							System.out.println("moveToMake taking coords: " + i + "," + j);
							oldBlackQueenCoord[0] = i;
							oldBlackQueenCoord[1] = j;
						}

					}
				}
				ArrayList<Integer> QueenNew = new ArrayList<Integer>();
				QueenNew.add(newBlackQueenCoord[0]);
				QueenNew.add(newBlackQueenCoord[1]);
				ArrayList<Integer> Arrow = new ArrayList<Integer>();
				Arrow.add(newArrowcoord[0]);
				Arrow.add(newArrowcoord[1]);
				ArrayList<Integer> QueenOld = new ArrayList<>();
				QueenOld.add(oldBlackQueenCoord[0]);
				QueenOld.add(oldBlackQueenCoord[1]);

				System.out.println("coordinates before converting");
				System.out.println("QueenNew: " + QueenNew.get(0) + "," + QueenNew.get(1));
				System.out.println("QueenOld: " + QueenOld.get(0) + "," + QueenOld.get(1));
				System.out.println("Arrow: " + Arrow.get(0) + "," + Arrow.get(1));
				// Convert To sendable coords
				HashMap<ArrayList<Integer>, ArrayList<Integer>> gaoTable = Board.makeGaoTable();

				// Send Move and update GUI
				ArrayList<Integer> QueenPosCurSend = new ArrayList<>();
				QueenPosCurSend = gaoTable.get(QueenOld);
				ArrayList<Integer> QueenPosNewSend = new ArrayList<>();
				QueenPosNewSend = gaoTable.get(QueenNew);
				ArrayList<Integer> ArrowPosSend = new ArrayList<>();
				ArrowPosSend = gaoTable.get(Arrow);

				System.out.println("coordinates after converting");
				System.out.println("QueenNew: " + QueenPosNewSend.get(0) + "," + QueenPosNewSend.get(1));
				System.out.println("QueenOld: " + QueenPosCurSend.get(0) + "," + QueenPosCurSend.get(1));
				System.out.println("Arrow: " + ArrowPosSend.get(0) + "," + ArrowPosSend.get(1));

				System.out.println("locally stored board before the update");
				this.board.printBoard();

				this.board = this.board.updateGameBoard(this.board, QueenPosCurSend, QueenPosNewSend, ArrowPosSend,
						true);
				System.out.println("locally stored board after the update");
				this.board.printBoard();

				this.gamegui.updateGameState(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);
				gameClient.sendMoveMessage(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);

			}

			t = 30;
			Timer timer = new Timer();
			timer.schedule(new countDown(), 0, 5000);
			break;

		case GameMessage.GAME_ACTION_START:
			this.firstPlayer = "white";
			this.blackUser = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK);
			this.whiteUser = (String) msgDetails.get(AmazonsGameMessage.PLAYER_WHITE);
			// Figure out who is player 1
			this.white = (this.whiteUser.equals(this.userName)) ? true : false;

			if (this.firstPlayer.equals("white") && this.white) {
				this.board = new Board();
				Tree partial = new Tree();
				partial.generatePartialGameTree(this.board, true, 1, partial.getRoot());

				Board moveToMake = new Board();
				moveToMake = minimax(partial.getRoot(), 1);
				Board boardBeforeMove = (Board) this.board.clone();

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

							if (moveToMake.board[i][j] != 1 && boardBeforeMove.board[i][j] == 1) {
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

				this.board.updateGameBoard(this.board, QueenPosCurSend, QueenPosNewSend, ArrowPosSend, true);
				System.out.println("updating game gui");
				this.gamegui.updateGameState(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);
				gameClient.sendMoveMessage(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);

			} else if (this.firstPlayer.equals("black") && !this.white) {
				Board boardBeforeMove = (Board) board.clone();
				System.out.println("board before the move");
				boardBeforeMove.printBoard();

				Tree partial = new Tree();

				// TODO game tree lack states where the queen moves and shoots the square it was
				// just on and has states where queens don't move but do shoot
				partial.generatePartialGameTree(boardBeforeMove, white, 1, partial.getRoot());

				/*
				 * Board moveToMake = partial.getRoot().getChildren().get(0).getBoard();
				 * System.out.println("move chosen"); moveToMake.printBoard();
				 */
				// Make move decision

				Board moveToMake = new Board();
				if (partial.getRoot().getChildren().size() == 0) {
					System.out.println("I am out of moves");
					break;
				}
				moveToMake = minimax(partial.getRoot(), 1);

				// Get Move Coords
				int[] oldBlackQueenCoord = new int[2];
				int[] newBlackQueenCoord = new int[2];
				int[] newArrowcoord = new int[2];

				for (int i = 0; i < boardBeforeMove.board.length; i++) {
					for (int j = 0; j < boardBeforeMove.board.length; j++) {

						if (moveToMake.board[i][j] == 3 && boardBeforeMove.board[i][j] != 3) {
							newArrowcoord[0] = i;
							newArrowcoord[1] = j;
							System.out.println("arrow called");
						}
						if (boardBeforeMove.board[i][j] == 0 && moveToMake.board[i][j] == 2) {
							newBlackQueenCoord[0] = i;
							newBlackQueenCoord[1] = j;
							System.out.println("newWHite called");
						}

						if (moveToMake.board[i][j] != 2 && boardBeforeMove.board[i][j] == 2) {
							System.out.println("***********************************");
							System.out.println("moveToMake taking coords: " + i + "," + j);
							oldBlackQueenCoord[0] = i;
							oldBlackQueenCoord[1] = j;
						}

					}
				}
				ArrayList<Integer> QueenNew = new ArrayList<Integer>();
				QueenNew.add(newBlackQueenCoord[0]);
				QueenNew.add(newBlackQueenCoord[1]);
				ArrayList<Integer> Arrow = new ArrayList<Integer>();
				Arrow.add(newArrowcoord[0]);
				Arrow.add(newArrowcoord[1]);
				ArrayList<Integer> QueenOld = new ArrayList<>();
				QueenOld.add(oldBlackQueenCoord[0]);
				QueenOld.add(oldBlackQueenCoord[1]);

				System.out.println("coordinates before converting");
				System.out.println("QueenNew: " + QueenNew.get(0) + "," + QueenNew.get(1));
				System.out.println("QueenOld: " + QueenOld.get(0) + "," + QueenOld.get(1));
				System.out.println("Arrow: " + Arrow.get(0) + "," + Arrow.get(1));
				// Convert To sendable coords
				HashMap<ArrayList<Integer>, ArrayList<Integer>> gaoTable = Board.makeGaoTable();

				// Send Move and update GUI
				ArrayList<Integer> QueenPosCurSend = new ArrayList<>();
				QueenPosCurSend = gaoTable.get(QueenOld);
				ArrayList<Integer> QueenPosNewSend = new ArrayList<>();
				QueenPosNewSend = gaoTable.get(QueenNew);
				ArrayList<Integer> ArrowPosSend = new ArrayList<>();
				ArrowPosSend = gaoTable.get(Arrow);

				System.out.println("coordinates after converting");
				System.out.println("QueenNew: " + QueenPosNewSend.get(0) + "," + QueenPosNewSend.get(1));
				System.out.println("QueenOld: " + QueenPosCurSend.get(0) + "," + QueenPosCurSend.get(1));
				System.out.println("Arrow: " + ArrowPosSend.get(0) + "," + ArrowPosSend.get(1));

				System.out.println("locally stored board before the update");
				this.board.printBoard();

				this.board = this.board.updateGameBoard(this.board, QueenPosCurSend, QueenPosNewSend, ArrowPosSend,
						true);
				System.out.println("locally stored board after the update");
				this.board.printBoard();

				this.gamegui.updateGameState(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);
				gameClient.sendMoveMessage(QueenPosCurSend, QueenPosNewSend, ArrowPosSend);
			}

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
				v = v2;
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
				v = v2;
				beta = Math.min(beta, v.value);
			}
			if (v.value <= alpha) {
				return v;
			}

		}

		return v;

	}

	/*
	 * public double minimax(Node current, int depth, double alpha, double beta,
	 * boolean maxPlayer) { // Node parent = current.getParent();
	 * 
	 * if (depth == 0) { return value(current.getBoard(), this.white); } if
	 * (maxPlayer) { double maxEval = Double.NEGATIVE_INFINITY; for (int i = 0; i <
	 * current.getChildren().size(); i++) { double eval =
	 * minimax(current.getChildren().get(i), depth - 1, alpha, beta, false); maxEval
	 * = Math.max(maxEval, eval); alpha = Math.max(alpha, eval);
	 * 
	 * if (beta <= alpha) {
	 * 
	 * break; }
	 * 
	 * } current.setValue(maxEval); return maxEval; } else { double minEval =
	 * Double.POSITIVE_INFINITY; for (int i = 0; i < current.getChildren().size();
	 * i++) { double eval = minimax(current.getChildren().get(i), depth - 1, alpha,
	 * beta, true); minEval = Math.min(minEval, eval); beta = Math.min(beta, eval);
	 * 
	 * if (beta <= alpha) {
	 * 
	 * break; }
	 * 
	 * } current.setValue(minEval); return minEval; }
	 * 
	 * }
	 */

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