public class ConcreteMove extends Move {
    private int row;
    private int col;

    public ConcreteMove(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
