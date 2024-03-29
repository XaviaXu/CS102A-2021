package view;


import controller.GameController;
import model.ChessPiece;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public static GameController controller;
    private ChessBoardPanel chessBoardPanel;
    private StatusPanel statusPanel;

    public GameFrame(int frameSize) {

        this.setTitle("2021F CS102A Project Reversi");
        this.setLayout(null);

        //获取窗口边框的长度，将这些值加到主窗口大小上，这能使窗口大小和预期相符
        Insets inset = this.getInsets();
        this.setSize(frameSize + inset.left + inset.right, frameSize + inset.top + inset.bottom);

        this.setLocationRelativeTo(null);


        chessBoardPanel = new ChessBoardPanel((int) (this.getWidth() * 0.8), (int) (this.getHeight() * 0.7));
        chessBoardPanel.setLocation((this.getWidth() - chessBoardPanel.getWidth()) / 2, (this.getHeight() - chessBoardPanel.getHeight()) / 3);

        statusPanel = new StatusPanel((int) (this.getWidth() * 0.8), (int) (this.getHeight() * 0.1));
        statusPanel.setLocation((this.getWidth() - chessBoardPanel.getWidth()) / 2, 0);
        controller = new GameController(chessBoardPanel, statusPanel);
        controller.setGamePanel(chessBoardPanel);
        controller.setFrame(this);

        this.add(chessBoardPanel);
        this.add(statusPanel);


        JButton restartBtn = new JButton("Restart");
        restartBtn.setSize(120, 50);
        restartBtn.setLocation((this.getWidth() - chessBoardPanel.getWidth()) / 2, (this.getHeight() + chessBoardPanel.getHeight()) / 2);
        add(restartBtn);
        restartBtn.addActionListener(e -> {
            chessBoardPanel.restart();
            controller.init();
            System.out.println("click restart Btn");
        });

        JButton loadGameBtn = new JButton("Load");
        loadGameBtn.setSize(120, 50);
        loadGameBtn.setLocation(restartBtn.getX()+restartBtn.getWidth()+30, restartBtn.getY());
        add(loadGameBtn);
        loadGameBtn.addActionListener(e -> {
            System.out.println("clicked Load Btn");
            String filePath = JOptionPane.showInputDialog(this, "input the path here");
            controller.readFileData(filePath);
            controller.checkFinished();
            controller.continueGame();
        });

        JButton saveGameBtn = new JButton("Save");
        saveGameBtn.setSize(120, 50);
        saveGameBtn.setLocation(loadGameBtn.getX()+restartBtn.getWidth()+30, restartBtn.getY());
        add(saveGameBtn);
        saveGameBtn.addActionListener(e -> {
            System.out.println("clicked Save Btn");
            String filePath = JOptionPane.showInputDialog(this, "input the path here");
            controller.writeDataToFile(filePath);
        });

        JButton cheatBtn = new JButton("Cheating");
        cheatBtn.setSize(120, 50);
        cheatBtn.setLocation(saveGameBtn.getX()+restartBtn.getWidth()+30,restartBtn.getY());
        add(cheatBtn);
        cheatBtn.addActionListener(e -> {
            System.out.println("press cheating");
            String[] col = {"BLACK","WHITE"};
            if(!controller.getCheating()){
                int rep = JOptionPane.showOptionDialog(
                        null,
                        "select color",
                        "Cheating mode on",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        col,
                        col[0]);
                if(rep==0){
                    System.out.println("choosing black");
                    controller.setPlayer(ChessPiece.BLACK);
                }else{
                    System.out.println("choosing white");
                    controller.setPlayer(ChessPiece.WHITE);
                }
            }else{
                JOptionPane.showMessageDialog(this,"cheating mode off");
                //todo: check moves
                controller.checkFinished();
                controller.continueGame();
            }
            controller.changeCheatingStatus();
        });


        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void gameOver(String winner){
        String msg = String.format("Game over, %s",winner);
        JOptionPane.showMessageDialog(this,msg);
    }

    public void gameError(int code){
        String msg;
        if(code==101){
            msg = "Error type 101: board size error.";
        }else if(code==102){
            msg = "Error type 102: chess type error.";
        }else if(code == 103){
            msg = "Error type 103: current player unknown.";
        }else if(code == 104){
            msg = "Error type 104: file type error.";
        }else if(code == 105){
            msg = "Error type 105: move invalid.";
        }else {
            msg = "Error type 106: other errors.";
        }
        JOptionPane.showMessageDialog(this,msg);
    }
}
