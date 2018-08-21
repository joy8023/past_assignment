package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Tetris extends JFrame {

    public int fps;
    public String seq;
    public String seqAgain;
    public double speed;

    private long FRAME_TIME;

    private BoardPanel board;
    private SidePanel side;

    private boolean isPaused;
    private boolean isNewGame;
    private boolean isGameOver;

    private int score;
    private static String shapes = "ILJSZOT";

    private TileType currentType;
    private TileType nextType;

    private int currentCol;
    private int currentRow;
    private int currentRotation;

    public Tetris(int fps, double speed, String sequence) {

        super("Tetris");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        this.fps = fps;
        this.seq = sequence;
        this.seqAgain = sequence;
        this.FRAME_TIME = 1000L / fps;
        this.speed = speed / 24 * 1000 /4;

        this.board = new BoardPanel(this);
        this.side = new SidePanel(this);
        add(board, BorderLayout.CENTER);
        add(side, BorderLayout.EAST);

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        down();
                        break;
                    case KeyEvent.VK_8:
                        down();
                        break;
                    case KeyEvent.VK_LEFT:
                        left();
                        break;
                    case KeyEvent.VK_4:
                        left();
                        break;
                    case KeyEvent.VK_RIGHT:
                        right();
                        break;
                    case KeyEvent.VK_6:
                        right();
                        break;
                    case KeyEvent.VK_UP:
                        rotateRight();
                        break;
                    case KeyEvent.VK_X:
                        rotateRight();
                        break;
                    case KeyEvent.VK_1:
                        rotateRight();
                        break;
                    case KeyEvent.VK_5:
                        rotateRight();
                        break;
                    case KeyEvent.VK_9:
                        rotateRight();
                        break;
                    case KeyEvent.VK_CONTROL:
                        rotateRight();
                        break;
                    case KeyEvent.VK_Z:
                        rotateLeft();
                        break;
                    case KeyEvent.VK_3:
                        rotateLeft();
                        break;
                    case KeyEvent.VK_7:
                        rotateLeft();
                        break;
                    case KeyEvent.VK_P:
                        pause();
                        break;

                    case KeyEvent.VK_ENTER:
                        if (isGameOver || isNewGame) {
                            resetGame();
                        }
                        break;
                    //  default:System.exit(0);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

                switch (e.getKeyCode()) {

                    case KeyEvent.VK_SPACE:
                        loop = 4;
                        break;
                }

            }

        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void down() {
        if (!isPaused )
            loop = 0;
    }

    public void left() {
        if (!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation))
            currentCol--;
    }

    public void right() {
        if (!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation))
            currentCol++;
    }

    public void rotateLeft() {
        if (!isPaused)
            rotatePiece((currentRotation == 0) ? 3 : currentRotation - 1);
    }

    public void rotateRight() {
        if (!isPaused)
            rotatePiece((currentRotation == 3) ? 0 : currentRotation + 1);
    }

    public void pause() {
        if (!isGameOver && !isNewGame) {
            isPaused = !isPaused;
        }
    }

    public int counter = 0;
    public int loop = 4;

    ActionListener update = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (counter < loop)
                counter++;
            else {
                updateGame();
                counter = 0;
            }
        }
    };

        ActionListener render = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.repaint();
                side.repaint();
            }
        };

        public void startGame() {

            isNewGame = true;
            this.score = 0;
            this.nextType = TileType.values()[shapes.indexOf(seq.charAt(0))];
            if (seq.length() != 1)
                seq = seq.substring(1);
            else
                seq = seqAgain;
            board.clear();
            spawnPiece();

            Timer timerFPS = new Timer((int) FRAME_TIME, render);
            Timer timerUpdate = new Timer((int) speed, update);

            timerFPS.start();
            timerUpdate.start();
        }

       private void updateGame() {

            if (board.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)) {
                currentRow++;
            } else {
                board.addPiece(currentType, currentCol, currentRow, currentRotation);

                int cleared = board.checkLines();
                if (cleared > 0) {
                    score += 50 << cleared;
                }
                spawnPiece();
            }
        }

        private void resetGame() {
            this.score = 0;
            this.nextType = TileType.values()[shapes.indexOf(seq.charAt(0))];
            if (seq.length() != 1)
                seq = seq.substring(1);
            else
                seq = seqAgain;
            this.isNewGame = false;
            this.isGameOver = false;
            board.clear();
            spawnPiece();
        }

        private void spawnPiece() {

            this.currentType = nextType;
            this.currentCol = currentType.getSpawnColumn();
            this.currentRow = currentType.getSpawnRow();
            this.currentRotation = 0;
            this.nextType = TileType.values()[shapes.indexOf(seq.charAt(0))];
            if (seq.length() != 1)
                seq = seq.substring(1);
            else
                seq = seqAgain;

            if (!board.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)) {
                this.isGameOver = true;

            }
        }

        private void rotatePiece(int newRotation) {

            int newColumn = currentCol;
            int newRow = currentRow;

            int left = currentType.getLeftInset(newRotation);
            int right = currentType.getRightInset(newRotation);
            int top = currentType.getTopInset(newRotation);
            int bottom = currentType.getBottomInset(newRotation);

            if (currentCol < -left) {
                newColumn -= currentCol - left;
            } else if (currentCol + 4 - right >= BoardPanel.COL) {
                newColumn -= (currentCol + 4 - right) - BoardPanel.COL + 1;
            }

            if (currentRow < -top) {
                newRow -= currentRow - top;
            } else if (currentRow + 4 - bottom >= BoardPanel.ROW) {
                newRow -= (currentRow + 4 - bottom) - BoardPanel.ROW + 1;
            }

            if (board.isValidAndEmpty(currentType, newColumn, newRow, newRotation)) {
                currentRotation = newRotation;
                currentRow = newRow;
                currentCol = newColumn;
            }
        }

        public boolean isPaused() {
            return isPaused;
        }

        public boolean isGameOver() {
            return isGameOver;
        }

        public boolean isNewGame() {
            return isNewGame;
        }

        public int getScore() {
            return score;
        }

        public TileType getPieceType() {
            return currentType;
        }

        public TileType getNextPieceType() {
            return nextType;
        }

        public int getPieceCol() {
            return currentCol;
        }

        public int getPieceRow() {
            return currentRow;
        }

        public int getPieceRotation() {
            return currentRotation;
        }

    }
