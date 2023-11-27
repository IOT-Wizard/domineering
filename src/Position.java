abstract public class Position {
    protected String[][] boardState;

    public Position(String[][] boardState) {
        this.boardState = boardState;
    }

    public String[][] getBoardState() {
        return boardState;
    }

    public void setBoardState(String[][] boardState) {
        this.boardState = boardState;
    }

}
