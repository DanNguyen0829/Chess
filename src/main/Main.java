package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("ChessGame");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GamePanel gp = new GamePanel();
        window.add(gp); //adds the game panel to the window
        window.pack();

        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);

        gp.startGameThread(); //runs the game
    }
}
