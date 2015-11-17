package linukGamePackage;

public class Node extends AI{
	byte[] nextMove = null;
	byte[] nextBlock = null;

	public Node(byte[][] nodeBoard, byte[] player1Location, byte[] player2Location, byte[] nextMove, byte[] nextBlock) {
		super(nodeBoard, player1Location, player2Location);
	
		// TODO Auto-generated constructor stub
		this.nextMove = nextMove.clone();
		this.nextBlock = nextBlock.clone();
		checkBoardIntegrity();
	}

	public Node(byte[][] boardMatrix, byte[] player1Location, byte[] player2Location) {
		// TODO Auto-generated constructor stub
		super(boardMatrix, player1Location, player2Location);
	}

	public byte[] getNextMove() {
		return nextMove;
	}
	public byte[] getNextBlock() {
		return nextBlock;
	}
	public byte[] getPlayerLocation ( byte playerType ) {
		return ( playerType == PLAYER1 ) ? getPlayer1Location() : getPlayer2Location();
	}
	public byte[] getOtherPlayerLocation( byte playerType ) {
		return ( playerType == PLAYER1 ) ? getPlayer2Location() : getPlayer1Location();
	}
}
