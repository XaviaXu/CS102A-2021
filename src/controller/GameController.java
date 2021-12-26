package controller;

import model.ChessPiece;
import view.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class GameController {
    private GameFrame frame;

    private ChessBoardPanel gamePanel;
    private StatusPanel statusPanel;
    private ChessPiece currentPlayer;

    private int blackScore;
    private int whiteScore;
    private boolean isCheating = false;

    private final int BLACK = 1;
    private final int WHITE = -1;

    private final int CHEAT_ON = 2;
    private final int CHEAT_OFF = -2;


    public void setFrame(GameFrame frame){this.frame = frame;}

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
        this.isCheating = false;
    }

    public void loadStatus(String data){
        String[] temp = data.split(" ");
        if(temp.length==0){
            frame.gameError(103);
            return;
        }else if(temp.length==1){
            int player = Integer.parseInt(temp[0]);
            if(player!=BLACK&&player!=WHITE){
                frame.gameError(103);
                return;
            }
            frame.gameError(105);
            return;
        }
        int player = Integer.parseInt(temp[0]);
        int cheating = Integer.parseInt(temp[1]);
        if(player!=BLACK&&player!=WHITE){
            frame.gameError(103);
            return;
        }
        if(cheating!=CHEAT_OFF&&cheating!=CHEAT_ON){
            frame.gameError(105);
            return;
        }

        this.currentPlayer = player==BLACK?ChessPiece.BLACK:ChessPiece.WHITE;
        this.isCheating = cheating == CHEAT_ON;
        statusPanel.setPlayerText(currentPlayer.name());
        statusPanel.setCheatLable(isCheating);
        statusPanel.setScoreText(blackScore, whiteScore);
    }

    public void swapPlayer() {
        updateScore();
        ChessPiece next = (currentPlayer == ChessPiece.BLACK) ? ChessPiece.WHITE : ChessPiece.BLACK;
        //todo: check swapping
        if(canSwap(next)){
            //swap player
            currentPlayer = next;
        }
        checkFinished();
        statusPanel.setPlayerText(currentPlayer.name());

    }

    public void updateScore(){
        countScore();
        statusPanel.setScoreText(blackScore, whiteScore);
    }

    public void continueGame(){
        if(!gamePanel.canMove(currentPlayer)){
            swapPlayer();
        }
    }

    public void checkFinished(){
        ChessPiece next = (currentPlayer == ChessPiece.BLACK) ? ChessPiece.WHITE : ChessPiece.BLACK;
        if(!canSwap(next)&&(!canSwap(currentPlayer))){
            //finished
            if(blackScore>whiteScore){
                frame.gameOver("BLACK wins!");
            }else if(blackScore==whiteScore){
                frame.gameOver("NO winner");
            }else{
                frame.gameOver("WHITE wins!");
            }
        }

    }

    public boolean canSwap(ChessPiece next){
         return gamePanel.canMove(next);
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
        List<String> fileData = new ArrayList<>();
        if(!fileName.endsWith(".txt")){
            frame.gameError(104);
            return;
        }
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileData.add(line);
            }

            fileData.forEach(System.out::println);
            for (int i = 0; i <fileData.size() ; i++) {
                if(i==0){
                    int size = Integer.parseInt(fileData.get(i));
                    if(size!=ChessBoardPanel.CHESS_COUNT){
                        frame.gameError(101);
                        return;
                    }
                }
                else if(i<=8){
                    //board status
                    if(!gamePanel.loadBoard(fileData.get(i),i-1)){
                        frame.gameError(102);
                        return;
                    }
                }
                else{
                    loadStatus(fileData.get(i));
                }
            }
        } catch (IOException e) {
            frame.gameError(106);
        }
    }

    public void writeDataToFile(String fileName) {
        try {
            File file = new File(fileName);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file)));
            writer.write(String.format("%d\n",ChessBoardPanel.CHESS_COUNT));
            writer.write(gamePanel.saveBoard());
            int player = currentPlayer==ChessPiece.BLACK?BLACK:WHITE;
            int flag = isCheating?CHEAT_ON:CHEAT_OFF;
            writer.write(String.format("%d %d",player,flag));
            writer.close();

        } catch (IOException e) {
            frame.gameError(106);
        }
    }

    public boolean canClick(int row, int col) {
        return gamePanel.canClickGrid(row, col, currentPlayer);
    }

    public void updateBoard(int ini_x,int ini_y,int color){
        gamePanel.flipChess(ini_x,ini_y,color);
    }

    public boolean getCheating(){return this.isCheating;}

    public void changeCheatingStatus(){
        this.isCheating = !this.isCheating;
        statusPanel.setCheatLable(this.isCheating);
    }

    public void setPlayer(ChessPiece cp){
        this.currentPlayer  = cp;
        statusPanel.setPlayerText(currentPlayer.name());
    }
}
