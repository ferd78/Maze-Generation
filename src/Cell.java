public class Cell {
    public int x; //coordinates of the cell while in the grid
    public int y;

    public boolean top, bottom, left, right; // boolean variables which represents each side of a wall
    public boolean visited, isCurrent; // condition variable for the current position of cell in the grid

    public Cell(int xNew, int yNew) {
        top = true;
        bottom = true;
        left = true;
        right = true;

        visited = false;
        isCurrent = false;

        x = xNew;
        y = yNew;
    }


}
