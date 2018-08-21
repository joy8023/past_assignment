package tetris;

import javax.swing.*;

/**
 * Created by bwbecker on 2016-09-19.
 */
public class TetrisMain {

    public static void main(String[] args) {
        JWindow window = new JWindow();
        window.getContentPane().add(new JLabel(new ImageIcon("res/splash.png"),SwingConstants.CENTER));
        window.setSize(371, 500);
        window.setBounds(0, 0, 371, 500);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.setVisible(false);
        window.dispose();

        System.out.println("Hello, Tetris!");

        try {
            ProgramArgs a = ProgramArgs.parseArgs(args);
           // System.out.println(a.getFPS());
            //System.out.println(a.getSpeed());
            //System.out.println(a.getSequence());
            Tetris tetris = new Tetris(a.getFPS(), a.getSpeed(), a.getSequence());
            tetris.startGame();

        }catch(IllegalArgumentException e) {
            System.out.println(e);
        }
    }


}


