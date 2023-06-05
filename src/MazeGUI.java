import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.event.*;


public class MazeGUI extends JFrame implements ActionListener, ChangeListener {

    // set up panels
    private JPanel buttonPanel;
    private JPanel controlPanel;
    private JPanel bottomPanel;

    // set up buttons
    private JButton generateButton;
    private JButton pauseButton;
    private JButton resetButton;
    private JButton resumeButton;

    // set up JCheckBox
    private JCheckBox showAnimation;

    // set up motion sliders
    private JSlider speedSlider;
    private JSlider heightSlider;
    private JSlider widthSlider;

    // set up labels
    private JLabel percentLabel;
    private JLabel speedLabel;
    private JLabel heightLabel;
    private JLabel widthLabel;

    private static DecimalFormat df = new DecimalFormat("0.0");

    Maze mazeGrid; // creates a new object called maze derived from the maze file

    private boolean isGenerating; //boolean variable to indicate that the maze is still generating

    private int timerSpeed; // variable to hold the speed of the timer

    public MazeGUI() {
        super("Maze Generation: Data Structure"); // adds the title for the program

        isGenerating = false;

        percentLabel = new JLabel("Visited: 0.0%", SwingConstants.CENTER);
        heightLabel = new JLabel("Height: 30", SwingConstants.CENTER);
        widthLabel = new JLabel("Width: 30", SwingConstants.CENTER);
        speedLabel = new JLabel("Speed", SwingConstants.CENTER);

        buttonPanel = new JPanel();
        controlPanel = new JPanel();
        bottomPanel = new JPanel();

        generateButton = new JButton("Generate Maze");
        generateButton.addActionListener(this);
        pauseButton = new JButton("Pause Maze");
        pauseButton.addActionListener(this);
        resetButton = new JButton("Reset Maze");
        resetButton.addActionListener(this);
        resumeButton = new JButton("Resume Maze");
        resumeButton.addActionListener(this);


        showAnimation = new JCheckBox("Show Animation", true);
        showAnimation.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(showAnimation);
        bottomPanel.add(percentLabel);

        speedSlider = new JSlider(1, 300);
        speedSlider.addChangeListener(this);
        heightSlider = new JSlider(10, 50);
        heightSlider.addChangeListener(this);
        heightSlider.setPaintTicks(true);
        heightSlider.setPaintLabels(true);
        heightSlider.setMajorTickSpacing(5);
        heightSlider.setMinorTickSpacing(1);
        heightSlider.setSnapToTicks(true);
        widthSlider = new JSlider(10, 50);
        widthSlider.addChangeListener(this);
        widthSlider.setPaintTicks(true);
        widthSlider.setPaintLabels(true);
        widthSlider.setMajorTickSpacing(5);
        widthSlider.setMinorTickSpacing(1);
        widthSlider.setSnapToTicks(true);

        controlPanel.setLayout(new GridLayout(3, 2));
        controlPanel.add(speedLabel);
        controlPanel.add(speedSlider);
        controlPanel.add(heightLabel);
        controlPanel.add(heightSlider);
        controlPanel.add(widthLabel);
        controlPanel.add(widthSlider);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(generateButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resumeButton);
        buttonPanel.add(resetButton);

        mazeGrid = new Maze();

        add(buttonPanel, BorderLayout.NORTH);
        add(mazeGrid, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 680);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(generateButton)) {
            if (!showAnimation.isSelected()) {
                mazeGrid.DepthFirstSearchAlgorithm();
                isGenerating = false;
                percentLabel.setText("Visited: 100.0%");
            }
            else {
                mazeGrid.startGeneration();
                isGenerating = true;
                timer.start();
            }
        }

        if (e.getSource().equals(pauseButton)) {
            timer.stop();
            isGenerating = false;
        }

        if(e.getSource().equals(resetButton)){
            mazeGrid.resetMaze();
            mazeGrid.repaint();
            isGenerating = false;
            percentLabel.setText("Visited: 0.0%");
        }

        if(e.getSource().equals(resumeButton)){
            timer.restart();
            isGenerating = true;
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(speedSlider)) {
            timerSpeed = speedSlider.getValue();
            timer.stop();
            timer.setDelay((301 - timerSpeed));
            timer.start();
        }
        if (e.getSource().equals(heightSlider)) {
            if (!isGenerating) {
                mazeGrid.setGridH(heightSlider.getValue());
                heightLabel.setText("Height: " + heightSlider.getValue());
                mazeGrid.resetMaze();
                mazeGrid.repaint();
            }
        }
        if (e.getSource().equals(widthSlider)) {
            if (!isGenerating) {
                mazeGrid.setGridW(widthSlider.getValue());
                widthLabel.setText("Width: " + widthSlider.getValue());
                mazeGrid.resetMaze();
                mazeGrid.repaint();
            }
        }
    }

    public double calculatePercentGenerated() {
        double numVisited = 0;
        for (int i = 0; i < (mazeGrid.getGridH() * mazeGrid.getGridW()); i++) {
            if (mazeGrid.cells[i].visited)
                numVisited++;
        }
        double percentVisited = (((numVisited / (mazeGrid.getGridH() * mazeGrid.getGridW()))) * 100);
        return percentVisited;
    }

    ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent t) {
            if (isGenerating && showAnimation.isSelected()) {
                percentLabel.setText("Visited: " + df.format(calculatePercentGenerated()) + "%");
                if (!mazeGrid.s.isEmpty()) {
                    mazeGrid.current.isCurrent = false;
                    if (mazeGrid.checkNeighbors(mazeGrid.current)) {
                        mazeGrid.s.push(mazeGrid.next);
                        mazeGrid.next.visited = true;
                        mazeGrid.next.isCurrent = true;
                        mazeGrid.removeWalls(mazeGrid.current, mazeGrid.next);
                        mazeGrid.repaint();
                        mazeGrid.current = mazeGrid.next;
                    }

                    else {
                        mazeGrid.current = mazeGrid.s.pop();
                        mazeGrid.current.isCurrent = true;
                        mazeGrid.repaint();
                    }

                } else {
                    isGenerating = false;
                    mazeGrid.doneGenerating = true;
                    mazeGrid.repaint();
                }
            }
        }
    };
    Timer timer = new Timer(200, taskPerformer);
}
