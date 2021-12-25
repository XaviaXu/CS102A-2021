package controller;

import model.ChessPiece;
import view.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class GameController {


    private ChessBoardPanel gamePanel;
    private StatusPanel statusPanel;
    private ChessPiece currentPlayer;
    private int blackScore;
    private int whiteScore;
    private boolean isCheating = false;

    private final int BLACK = 1;
    private final int WHITE = -1;



    public GameController(ChessBoardPanel gamePanel, StatusPanel statusPanel) {
        this.gamePanel = gamePanel;
        this.statusPanel = statusPanel;
        this.currentPlayer = ChessPiece.BLACK;
        blackScore = 2;
        whiteScore = 2;
    }

    public void init(){
        this.currentPlayer = ChessPiece.BLACK;
        blackScore = 2;
        whiteScore = 2;
        countScore();
        statusPanel.setPlayerText(currentPlayer.name());
        statusPanel.setScoreText(blackScore, whiteScore);
    }

    public void swapPlayer() {
        countScore();
        currentPlayer = (currentPlayer == ChessPiece.BLACK) ? ChessPiece.WHITE : ChessPiece.BLACK;
        //todo: check swapping
        statusPanel.setPlayerText(currentPlayer.name());
        statusPanel.setScoreText(blackScore, whiteScore);
    }

    public void countScore() {
        blackScore = gamePanel.getScore(BLACK);
        whiteScore = gamePanel.getScore(WHITE);
    }


    public ChessPiece getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessBoardPanel getGamePanel() {
        return gamePanel;
    }


    public void setGamePanel(ChessBoardPanel gamePanel) {
        this.gamePanel = gamePanel;
    }


    public void readFileData(String fileName) {
        //todo: read date from file
        List<String> fileData = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileData.add(line);
            }
            fileData.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDataToFile(String fileName) {
        try {
            File file = new File(fileName);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file)));
            writer.write(gamePanel.saveBoard());
            int col = currentPlayer==ChessPiece.BLACK?BLACK:WHITE;
            writer.write(Integer.toString(col));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean canClick(int row, int col) {
        return gamePanel.canClickGrid(row, col, currentPlayer);
    }

    public void updateBoard(int ini_x,int ini_y,int color){
        gamePanel.flipChess(ini_x,ini_y,color);
    }
}
