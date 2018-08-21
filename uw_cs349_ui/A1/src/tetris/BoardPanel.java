package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;


public class BoardPanel extends JPanel {

    public Image[] tetrisBlocks;

    public static final int COL = 10;
    public static final int VISIBLE_ROW = 24;
    public static final int ROW = VISIBLE_ROW + 2;
    public static final int TILE_SIZE = 25;
    private static final int CENTER_X = COL * TILE_SIZE / 2;
    private static final int CENTER_Y = VISIBLE_ROW * TILE_SIZE / 2;
    public static final int PANEL_WIDTH = COL * TILE_SIZE;
    public static final int PANEL_HEIGHT = VISIBLE_ROW * TILE_SIZE;

    private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 16);

    private Tetris tetris;
    private TileType[][] tiles;

    public boolean isSelect = false;
    public int[] tileX,tileY;

    public BoardPanel(Tetris tetris) {
        this.tetris = tetris;
        this.tiles = new TileType[ROW][COL];

        setPreferredSize((new Dimension(PANEL_WIDTH, PANEL_HEIGHT)));

        setBackground(Color.darkGray);

        try {
            tetrisBlocks = ImageLoader.loadImage( TILE_SIZE);
        }
        catch(IOException e){
            System.out.println("Error loading image...");
            System.exit(1);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(isClick(e) && (isSelect)) {
                    isSelect = false;
                    tetris.loop = 0;
                }
                else if(isClick(e) && (!isSelect))
                    isSelect = true;
                else if ((!isClick(e)) && isSelect)
                    isSelect = true;
                else if ((!isClick(e)) && (!isSelect))
                    isSelect = false;

              //  isSelect = isClick(e) ^ isSelect;
                System.out.println(isSelect);

                }



        });


        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (isSelect) {
                    int shift = e.getX() / 25 - tetris.getPieceCol() - 2;
                    if(shift > 0 ){
                        for(int i = 0; i < shift ; i++)
                            tetris.right();
                    }
                    else if (shift < 0){
                        for(int i = 0; i > shift; i--)
                            tetris.left();
                    }
                }
            }

        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (isSelect) {
                    if(e.getWheelRotation() > 0)
                        tetris.rotateRight();
                    else if (e.getWheelRotation() < 0)
                        tetris.rotateLeft();
                }
            }
        });



    }

    public boolean isClick(MouseEvent e) {

        for (int i = 0; i < 4; i++) {
            if (((e.getX()-this.tileX[i]<25)&&(e.getX()>=this.tileX[i])&&(e.getY()-this.tileY[i]<25)&&(e.getY()>=this.tileY[i])))
                return true;
        }
        return false;
    }

    public void clear() {

        for(int i = 0; i < ROW; i++) {
            for(int j = 0; j < COL; j++) {
                tiles[i][j] = null;
            }
        }
    }

    public boolean isValidAndEmpty(TileType type, int x, int y, int rotation) {

        if(x < -type.getLeftInset(rotation) || x + 4 - type.getRightInset(rotation) >= COL)
            return false;

        if(y < -type.getTopInset(rotation) || y + 4 - type.getBottomInset(rotation) >= ROW)
            return false;

        for(int col = 0; col < 4; col++) {
            for(int row = 0; row < 4; row++) {
                if(type.isTile(col, row, rotation) && isOccupied(x + col, y + row))
                    return false;
            }
        }
        return true;
    }

    public void addPiece(TileType type, int x, int y, int rotation) {

        for(int col = 0; col < 4; col++) {
            for(int row = 0; row < 4; row++) {
                if(type.isTile(col, row, rotation))
                    setTile(col + x, row + y, type);
            }
        }
        isSelect = false;
        tetris.loop = 4;
    }

    public int checkLines() {

        int completedLines = 0;

        for(int row = 0; row < ROW; row++) {

            if(checkLine(row))
                completedLines++;
        }
        return completedLines;
    }

    private boolean checkLine(int line) {

        for(int col = 0; col < COL; col++) {
            if(!isOccupied(col, line))
                return false;
        }

        for(int row = line - 1; row >= 0; row--) {
            for(int col = 0; col < COL; col++)
                setTile(col, row + 1, getTile(col, row));
        }
        return true;
    }

    private boolean isOccupied(int x, int y) {

        return tiles[y][x] != null;
    }

    private void setTile(int  x, int y, TileType type) {

        tiles[y][x] = type;
    }

    private TileType getTile(int x, int y) {

        return tiles[y][x];
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(tetris.isPaused()) {
            g.setFont(LARGE_FONT);
            g.setColor(Color.WHITE);
            String msg = "PAUSED";
            g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, CENTER_Y);
        }
        else if(tetris.isNewGame() || tetris.isGameOver()) {
            g.setFont(LARGE_FONT);
            g.setColor(Color.WHITE);

            String msg = tetris.isNewGame() ? "TETRIS" : "GAME OVER";
            g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 150);
            msg = "Press Enter to Play" + (tetris.isNewGame() ? "" : " Again");
            g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 300);
        }
        else {

            for(int x = 0; x < COL; x++) {
                for(int y = 2; y < ROW; y++) {
                    TileType tile = getTile(x, y);
                    if(tile != null) {
                        drawTile(tile, x * TILE_SIZE, (y - 2) * TILE_SIZE, g);
                    }
                }
            }

            TileType type = tetris.getPieceType();
            int pieceCol = tetris.getPieceCol();
            int pieceRow = tetris.getPieceRow();
            int rotation = tetris.getPieceRotation();

            int i = 0;
            tileX = new int[]{0,0,0,0};
            tileY = new int[]{0,0,0,0};

            for(int col = 0; col < 4; col++) {
                for(int row = 0; row < 4; row++) {
                    if(pieceRow + row >= 2 && type.isTile(col, row, rotation)) {
                        tileX[i] = (pieceCol + col) * TILE_SIZE;
                        tileY[i] = (pieceRow + row - 2) * TILE_SIZE;
                        drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - 2) * TILE_SIZE, g);
                        i++;
                    }
                }
            }

            g.setColor(Color.black);
            for(int x = 0; x < COL; x++) {
                for(int y = 0; y < VISIBLE_ROW; y++) {
                    g.drawLine(0, y * TILE_SIZE, COL * TILE_SIZE, y * TILE_SIZE);
                    g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW * TILE_SIZE);
                }
            }
        }

        g.setColor(Color.WHITE);
        g.drawRect(0, 0, TILE_SIZE * COL, TILE_SIZE * VISIBLE_ROW);
    }

    private void drawTile(TileType type, int x, int y, Graphics g) {
        g.drawImage(tetrisBlocks[type.getNo()], x, y, 25, 25, null);
    }

}
