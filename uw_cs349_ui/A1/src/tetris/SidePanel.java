package tetris;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SidePanel extends JPanel {

    public Image[] tetrisBlocks;
    private static final int TILE_SIZE = BoardPanel.TILE_SIZE;

    private Tetris tetris;

    public SidePanel(Tetris tetris) {
        this.tetris = tetris;

        setPreferredSize(new Dimension(200, BoardPanel.PANEL_HEIGHT));
        setBackground(Color.BLACK);

        try {
            tetrisBlocks = ImageLoader.loadImage( TILE_SIZE);
        } catch (IOException e) {
            System.out.println("Error loading image...");
            System.exit(1);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(128, 192, 128));

        g.setFont(new Font("Tahoma", Font.BOLD, 16));
        g.drawString("Score: " + tetris.getScore(), 40, 350);
        g.drawString("Next Piece:", 20, 60);

        TileType type = tetris.getNextPieceType();
        if (!tetris.isGameOver() && type != null) {

            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    if (type.isTile(col, row, 0)) {
                        drawTile(type, 50 + (col * TILE_SIZE), 80 + (row  * TILE_SIZE), g);
                    }
                }
            }
        }
    }

    private void drawTile(TileType type, int x, int y, Graphics g) {
        g.drawImage(tetrisBlocks[type.getNo()], x, y, TILE_SIZE, TILE_SIZE, null);


    }
}
