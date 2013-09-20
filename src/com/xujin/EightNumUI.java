package com.xujin;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


/**
 * @version 1.2  
 * @author Xu Jin
 * 
 * Game：EightNum 
 * Date：2013-03-25 ~ 2013-04-01
 */



//八数码游戏的主类，用来控制整个游戏
public class EightNumUI {	
	public static void main(String...args){
		EightNumUI newGame = new EightNumUI();
		MainFrame mainFrame = new MainFrame();
		mainFrame.setResizable(true);
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				 int returnValue = JOptionPane.showConfirmDialog(null, "确实要退出游戏吗？", "退出游戏", JOptionPane.YES_NO_OPTION);
				    if (returnValue == JOptionPane.YES_OPTION)
				      System.exit(0);
			}
		});
		newGame.init();
		mainFrame.setVisible(true);
	}	
	
	
	public void init(){		
		currentStatus = new int[9];
		arrayRange = 9;
		MainFrame.textAreaEast.setText("游戏玩法:\n按动上下左右按钮来移动空格，使整个九宫格变成如下就胜利了：\n" +
					"   1 2 3\n" +
					"   4 5 6\n" +
					"   7 8 \n\nPS: 按下左边按钮，就会出现从目前局面开始的最佳解法\nPS：左边随机产生的局面确保有解 ");
		
		//产生一个随机的八数码
		//利用一个ArrayList，随机在其中选取一个0~9的数，选完就在ArrayList中删除，
		//然后继续在其中随机选取数字，直到9个全部选取完毕
		
		initStart();
		
		//逆序数为偶数才能有解，随机的排列不可以是结束状态
		while(reverse(currentStatus) % 2 == 1 ||finish(currentStatus)){
			initStart();
		}	
		
		arrayToTextArea(currentStatus);
	}
	
	//用来在一个textArea中输出答案
	public static void ResultOutput(){
			SolvePuzzle solvePuzzle = new SolvePuzzle(currentStatus);
			String result = solvePuzzle.solve();
			
			//打印到中间的textArea中
			MainFrame.textAreaEast.setText(result);
		}
	
	public static void arrayToTextArea(int[] currentStatus){
		/*
		String startString = "";
		for(int i = 0; i < 9; i++){
			if(currentStatus[i] == 0) startString += "  ";
			else{
				startString+= currentStatus[i] + " ";
				
			}
			if(i % 3 ==2) startString += "\r\n";
		}
		
		MainFrame.textAreaWest.setText(startString);
		*/
		
		int i = 0;
		for(JTextField sig: MainFrame.box){
			sig.setText(" " + currentStatus[i] + " ");
			if(currentStatus[i] == 0) sig.setText("");
			i++;
		}
		
	}
	
	//逆序数
	private int reverse(int arrange[]){
		int reverse = 0;
		for(int i = 1; i < 9; i++){
			if(arrange[i] != 0){
				for(int j = 0; j < i; j++){
					//arrange[j]表示前面的数
					if(arrange[j] > arrange[i]) reverse++;
				}
			}
			
		}
		return reverse;
	}
	
	private void initStart(){
		//初始化ArrayList
		arrayRange = 9;
		ArrayList<Integer> array = new ArrayList<Integer>();
		for(int i = 0; i < 10; i++)
			array.add(i);
		Random random = new Random();
		for(int i = 0; i < 9; i++){
			int singleNumIndex = random.nextInt(arrayRange--);			
			currentStatus[i] = array.get(singleNumIndex); 
			array.remove(singleNumIndex);
		}
	}

	//检查是否结束
	public static  boolean finish(int[] currentStatus){
		for(int i = 0; i < 8; i++)
			if(currentStatus[i] != i + 1)
				return false;
		return true;
	}	   
	
	public static int[] currentStatus;
	public int arrayRange;
}






