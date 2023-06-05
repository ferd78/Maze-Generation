public class Cell {
    // variables to describe its location in the grid:
    public int x;
    public int y;

    public boolean top, bottom, left, right; // boolean variables for walls
    public boolean visited, isCurrent; // condition variable for the current position of cell

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