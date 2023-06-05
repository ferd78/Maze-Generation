import javax.swing.*;
import java.util.*;
import java.awt.*;

public class Maze extends JPanel {

    public Cell cells[]; // an array of cells

    private int gridH; //grid height
    private int gridW; //grid width

    private int pixelPerH; //indicates the amount of pixels 1 height represents
    private int pixelPerW; //indicates the amount of pixels 1 width value represents

    public Cell current; //condition variable defined in cell class
    public Cell next; //

    public boolean doneGenerating; // boolean variable to see when the maze is done being generated

    public Stack<Cell> s; // create a new stack data structure later used for dfs algorithm

    public Maze() {
        gridH = 30;
        gridW = 30;
        resetMaze();
    }

    // setters and getters for the grid height and grid width
    public void setGridH(int newGridH) { gridH = newGridH; }
    public int getGridH() { return gridH; }
    public void setGridW(int newGridW) { gridW = newGridW; }
    public int getGridW() { return gridW; }


    public void resetMaze() {
        pixelPerH = 550/ gridH;
        pixelPerW = 550/ gridW;

        doneGenerating = false;

        cells = new Cell[gridH * gridW];
        int i = 0;
        for (int y = 0; y < gridH; y++) {
            for (int x = 0; x < gridW; x++) {
                cells[i] = new Cell(x,y);
                i++;
            }
        }
    }

    public void startGeneration() {
        resetMaze();
        current = cells[0];
        current.visited = true;

        s = new Stack<>();

        s.push(current);
    }

    public void DepthFirstSearchAlgorithm(){
        resetMaze();
        current = cells[0];
        current.visited = true;
        s = new Stack<>();
        s.push(current);

        while(!s.isEmpty()){
            current.isCurrent = false;
            if(checkNeighbors(current)){
                s.push(next);
                next.visited = true;
                removeWalls(current, next);
                current = next;
                repaint();
            }
            else {
                current = s.pop();
                current.isCurrent = true;
                repaint();
            }
        }
        doneGenerating = true;
    }

    public int findIndex(int x, int y) {
        if (x < 0 || y < 0 || x > gridW - 1 || y > gridH - 1) {
            return -1;
        }
        else
            return (x + y * gridW);
    }


    public boolean checkNeighbors(Cell currCell) {
        LinkedList<Cell> neighbors = new LinkedList<>();

        if (findIndex(currCell.x, currCell.y - 1) != -1) {
            Cell top = cells[findIndex(currCell.x, currCell.y - 1)];
            if (!top.visited) {
                neighbors.add(top);
            }
        }
        if (findIndex(currCell.x + 1, currCell.y) != -1) {
            Cell right = cells[findIndex(currCell.x + 1, currCell.y)];
            if (!right.visited) {
                neighbors.add(right);
            }
        }
        if (findIndex(currCell.x, currCell.y + 1) != -1) {
            Cell bottom = cells[findIndex(currCell.x, currCell.y + 1)];
            if (!bottom.visited) {
                neighbors.add(bottom);
            }
        }
        if (findIndex(currCell.x - 1, currCell.y) != -1) {
            Cell left = cells[findIndex(currCell.x - 1, currCell.y)];
            if (!left.visited) {
                neighbors.add(left);
            }
        }

        if (neighbors.size() > 0) {
            int r = (int)Math.floor(Math.random() * neighbors.size());
            next = neighbors.get(r);
            return true;
        }
        else
            return false;
    }

    public void removeWalls(Cell currCell, Cell nextCell) {
        int x = currCell.x - nextCell.x;
        if (x == 1) {
            currCell.left = false;
            nextCell.right = false;
        }
        else if (x == -1) {
            currCell.right = false;
            nextCell.left = false;
        }
        int y = currCell.y - nextCell.y;
        if (y == 1) {
            currCell.top = false;
            nextCell.bottom = false;
        }
        else if (y == -1) {
            currCell.bottom = false;
            nextCell.top = false;
        }
    }



    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int offset = 10;

        for (int i = 0; i < gridH * gridW; i++) {
            if (cells[i].isCurrent) {
                g.setColor(Color.green.brighter());
                g.fillRect(((cells[i].x * pixelPerW) + offset), ((cells[i].y * pixelPerH) + offset), pixelPerW, pixelPerH);
                g.setColor(Color.black);
            } else if (cells[i].visited) {
                g.setColor(Color.cyan);
                g.fillRect(((cells[i].x * pixelPerW) + offset), ((cells[i].y * pixelPerH) + offset), pixelPerW, pixelPerH);
                g.setColor(Color.black);
            }

            if (cells[i].top) {
                g.drawLine(((cells[i].x * pixelPerW) + offset), ((cells[i].y * pixelPerH) + offset), (((cells[i].x + 1) * pixelPerW) + offset), ((cells[i].y * pixelPerH) + offset));
            }
            if (cells[i].bottom) {
                g.drawLine(((cells[i].x * pixelPerW) + offset), (((cells[i].y + 1) * pixelPerH) + offset), (((cells[i].x + 1) * pixelPerW) + offset), (((cells[i].y + 1) * pixelPerH) + offset));
            }
            if (cells[i].left) {
                g.drawLine(((cells[i].x * pixelPerW) + offset), ((cells[i].y * pixelPerH) + offset), ((cells[i].x * pixelPerW) + offset), (((cells[i].y + 1) * pixelPerH) + offset));
            }
            if (cells[i].right) {
                g.drawLine((((cells[i].x + 1) * pixelPerW) + offset), ((cells[i].y * pixelPerH) + offset), (((cells[i].x + 1) * pixelPerW) + offset), (((cells[i].y + 1) * pixelPerH) + offset));
            }
        }
        if (doneGenerating) {
            g.setColor(Color.red);
            g.fillRect(((cells[gridH * gridW - 1].x * pixelPerW) + offset), ((cells[gridH * gridW - 1].y * pixelPerH) + offset), pixelPerW, pixelPerH);
        }
    }
}

