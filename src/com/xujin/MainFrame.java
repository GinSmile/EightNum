package com.xujin;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

//框架类，负责管理界面
class MainFrame extends JFrame{
	
	/************************************************初始化框架*****************************************************/
	
	public MainFrame(){
		Toolkit kit = Toolkit.getDefaultToolkit();
		   
		//设置默认窗口大小和位置
		Dimension screenSize = kit.getScreenSize();
		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
	    this.setBounds(screenWidth / 4, screenHeight / 4, screenWidth / 2, screenHeight / 2);
	    
	    //设置程序图标和程序名
	    Image imgIcon = kit.getImage("image/icon.png");
	    setIconImage(imgIcon);
		setTitle("八数码游戏");
	    
	    
	    //设置默认观感
	    try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(MainFrame.this);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	    
	    
	    //设置菜单
	    setMenu();
	    setJMenuBar(menuBar);
	    
	    //添加组件
	    add(new NorthPanel(), BorderLayout.NORTH);   
	    add(new WestPanel(), BorderLayout.WEST);
	    add(new EastPanel(),BorderLayout.EAST);
	    add(new CenterPanel(), BorderLayout.CENTER);
	    
	}
	
	
	/************************************************菜单相关*****************************************************/
	
	private void setMenu(){
		   JMenu gameMenu = new JMenu("游戏");
		   menuBar.add(gameMenu);	   
		   JMenuItem newGameMenu = new JMenuItem("开始新游戏");
		   newGameMenu.setIcon(new ImageIcon("image/start.png"));
		   gameMenu.add(newGameMenu);
		   newGameMenu.addActionListener(new ActionListener(){
			   @Override
			   public void actionPerformed(ActionEvent event){
				   restart();
			   }
		   });
		   JMenuItem exitGameMenu = new JMenuItem("退出游戏");
		   exitGameMenu.setIcon(new ImageIcon("image/exit.png"));
		   gameMenu.add(exitGameMenu);
		   exitGameMenu.addActionListener(new ActionListener(){
			   @Override
			   public void actionPerformed(ActionEvent event){
				   int returnValue = JOptionPane.showConfirmDialog(null, "确实要退出游戏吗？", "退出游戏", JOptionPane.YES_NO_OPTION);
				    if (returnValue == JOptionPane.YES_OPTION)
				      System.exit(0);
			   }
		   });
		   
		   
		   JMenu setMenu = new JMenu("设置");
		   menuBar.add(setMenu);
		   setLookMenu = new JMenu("设置观感");
		   setLookMenu.setIcon(new ImageIcon("image/look.png"));
		   setMenu.add(setLookMenu);
		   //设置观感选项
		    UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
		    for(UIManager.LookAndFeelInfo info: infos){
		    	addItem(info.getName(), info.getClassName());
		    	//System.out.println(info.getClassName());
		    }
		   
		    
		   JMenu helpMenu = new JMenu("帮助");
		   menuBar.add(helpMenu);	   
		   JMenuItem aboutMeMenu = new JMenuItem("关于本软件");
		   aboutMeMenu.setIcon(new ImageIcon("image/aboutMe.png"));
		   aboutMeMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
					JDialog dialog = null;
					if(dialog == null)
						dialog = new AboutDialog(MainFrame.this);
					dialog.setVisible(true);
			}
		   });
		   JMenuItem howToPlayGameMenu = new JMenuItem("游戏玩法");
		   howToPlayGameMenu.setIcon(new ImageIcon("image/help.png"));
		   howToPlayGameMenu.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event) {
						JDialog dialog = null;
						if(dialog == null)
							dialog = new HowToPlayDialog(MainFrame.this);
						dialog.setVisible(true);
				}
			   });
		   helpMenu.add(aboutMeMenu);
		   helpMenu.add(howToPlayGameMenu);
		   
		   
	   }
	
	
	
	public void addItem(String name, final String plafName){
		JMenuItem item = new JMenuItem(name);
		setLookMenu.add(item);
		
		item.addActionListener(new ActionListener(){
			@Override
	    	public void actionPerformed(ActionEvent event){
	    		try{
	    			UIManager.setLookAndFeel(plafName);
	    			SwingUtilities.updateComponentTreeUI(MainFrame.this);
	    		}
	    		catch(Exception e){
	    			e.printStackTrace();
	    		}
	    	}
		});
	}
	
	/************************************************面板设置*****************************************************/
	
	//北部的Panel用来显示欢迎语
	class NorthPanel extends JPanel{		
		public NorthPanel(){
			setLayout(new GridLayout(1, 2));
			add(new JLabel("~~欢迎来到八数码游戏~~   Just enjoy it !"),BorderLayout.CENTER);
		}
	}
	
	
	//西部的Panel用来显示初始状态以及提供按钮移动空格
	class WestPanel extends JPanel{
		public WestPanel(){			
			setLayout(new GridLayout(5, 1));
			//textAreaWest = new JTextArea(40, 30);
			//textAreaWest.setEditable(false);
			//add(textAreaWest);
			
			JPanel buttonPanelUp = new JPanel();
			JPanel buttonPanelCenter = new JPanel();
			JPanel buttonPanelDown = new JPanel();
			
			
			//九宫格界面由九个JTextField组成
			JPanel boxes = new JPanel();
			boxes.setLayout(new GridLayout(3, 3));			

			
			for(int i = 0; i < 9; i++){
				JTextField one = new JTextField(1);
				one.setEditable(false);
				box.add(one);
				boxes.add(one);
			}
			
			add(boxes);

			
			//添加上下左右四个按钮
			up.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int space = 0;
					//找出空格
					for(int i = 0; i < 9; i++){
						if(EightNumUI.currentStatus[i] == 0) 
							space = i;				
					}
					
					if(space / 3 != 0){
						EightNumUI.currentStatus[space] = EightNumUI.currentStatus[space - 3];
						EightNumUI.currentStatus[space - 3] = 0;
						EightNumUI.arrayToTextArea(EightNumUI.currentStatus);
						countLabel.setText("您已经走了的步数：" + ++count);
					}
					
					if(EightNumUI.finish(EightNumUI.currentStatus)){
						showGameOverDialog();
					}
					
					
					
					
				}
				
			});
			
			down.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int space = 0;
					//找出空格
					for(int i = 0; i < 9; i++){
						if(EightNumUI.currentStatus[i] == 0) 
							space = i;				
					}
					
					if(space / 3 != 2){
						EightNumUI.currentStatus[space] = EightNumUI.currentStatus[space + 3];
						EightNumUI.currentStatus[space + 3] = 0;
						EightNumUI.arrayToTextArea(EightNumUI.currentStatus);
						countLabel.setText("您已经走了的步数：" + ++count);
					}
					
					if(EightNumUI.finish(EightNumUI.currentStatus)){
						showGameOverDialog();
					}
					
					
					
				}
				
			});
			
			left.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int space = 0;
					//找出空格
					for(int i = 0; i < 9; i++){
						if(EightNumUI.currentStatus[i] == 0) 
							space = i;				
					}
					
					if(space % 3 != 0){
						EightNumUI.currentStatus[space] = EightNumUI.currentStatus[space - 1];
						EightNumUI.currentStatus[space - 1] = 0;
						EightNumUI.arrayToTextArea(EightNumUI.currentStatus);
						countLabel.setText("您已经走了的步数：" + ++count);
					}
					
					if(EightNumUI.finish(EightNumUI.currentStatus)){
						showGameOverDialog();
					}
					
					
					
				}
				
			});

			right.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int space = 0;
					//找出空格
					for(int i = 0; i < 9; i++){
						if(EightNumUI.currentStatus[i] == 0) 
							space = i;				
					}
					
					if(space % 3 != 2){
						EightNumUI.currentStatus[space] = EightNumUI.currentStatus[space + 1];
						EightNumUI.currentStatus[space + 1] = 0;
						EightNumUI.arrayToTextArea(EightNumUI.currentStatus);
						countLabel.setText("您已经走了的步数：" + ++count);
					}
					
					if(EightNumUI.finish(EightNumUI.currentStatus)){
						showGameOverDialog();
					}
					
					
					
				}
				
			});
			
			buttonPanelUp.add(up);
			add(buttonPanelUp);
			
			buttonPanelCenter.add(left);
			buttonPanelCenter.add(new JLabel("  移动空格  "));
			buttonPanelCenter.add(right);			
			add(buttonPanelCenter);
			
			buttonPanelDown.add(down);
			add(buttonPanelDown);		
			
			//加统计步数单元
			add(countLabel);
			
		}

		JButton up = new JButton("上");
		JButton down = new JButton("下");
		JButton left = new JButton("左");		
		JButton right = new JButton("右");
	}
	
	//东边的Panel用来提供答案
	class EastPanel extends JPanel{
		public EastPanel(){
			setLayout(new GridLayout(1, 1));
			textAreaEast = new JTextArea(12, 40);
			textAreaEast.setLineWrap(true);
			textAreaEast.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(textAreaEast);
			add(scrollPane);
			
		}
	}
	
	//中间的Panel用来按下按钮显示答案
	class CenterPanel extends JPanel{
		public CenterPanel(){
			//使用默认布局管理器
			okButton = new JButton("看答案");
			okButton.setIcon(new ImageIcon("image/ok.png"));
			okButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					EightNumUI.ResultOutput();
					
				}
			});
			add(okButton, BorderLayout.CENTER);
			/*
			add(okButton);
			label = new JLabel();
			label.setText("已用步数：" + Guess.countSteps);
			add(label);
			*/
		}
	}
	
	
	/************************************************重新开始游戏*****************************************************/
	
	public void restart(){
		count = 0;		
		countLabel.setText("您已经走了的步数：0");
		new EightNumUI().init();
	}
	
	/************************************************对话框Dialog*****************************************************/
	
	//游戏结束对话框
	public void showGameOverDialog(){
		JDialog dialog = null;
		if(dialog == null)
			dialog = new GameOverDialog(MainFrame.this);
		dialog.setVisible(true);
	}
	
	class GameOverDialog extends JDialog{
		public GameOverDialog(JFrame owner){
			super(owner, "游戏结束", true);
			add(new JLabel("<html><h2><i>恭喜你</i><h2><hr/><p>胜利！是否重新开始？</p></html>"), 
					BorderLayout.CENTER);
			JPanel panel = new JPanel();
			JButton ok = new JButton("重新开始");
			
			ok.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event) {
					setVisible(false);
					restart();
				}
			});
			
			panel.add(ok);
			add(panel, BorderLayout.SOUTH);
			this.setBounds(screenWidth * 3 / 8, screenHeight * 3 / 8, screenWidth /3, screenHeight/4); 
		}
	}
	
	//关于作者对话框
	class AboutDialog extends JDialog{
		public AboutDialog(JFrame owner){
			super(owner, "关于", true);
			setResizable(true);
			JLabel aboutLabel = new JLabel("<html><h4><i>关于</i></h4>" +
					"本软件为八数码游戏。\n" +
					"Version: 1.2\n" +					

					"<hr/><p>开发者：徐进 </p>" +
					"<p>Copyright:GinSmile   weibo.com/smilexujin</p>" +
					"<p>个人博客：<a href='http://ginsmile.github.io/'>http://ginsmile.github.io/</a></p></html>");
			add(aboutLabel);
			
			aboutLabel.addMouseListener(new MouseAdapter(){ 
				@Override 
				public void mouseClicked(MouseEvent e) { 
					Desktop desktop=Desktop.getDesktop(); 
					try { 
						desktop.browse(new URI("http://blog.csdn.net/xujinsmile")); 
					} catch (IOException e1) { 
						e1.printStackTrace(); 
					} catch (URISyntaxException e1) { 
						e1.printStackTrace(); 
					}	
				}
				}); 
			
			JPanel panel = new JPanel();
			JButton ok = new JButton("OK");
			
			ok.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event) {
					setVisible(false);
				}
			});
			
			panel.add(ok);
			add(panel, BorderLayout.SOUTH);
			
			this.setBounds(screenWidth * 3 / 8, screenHeight * 3 / 8, screenWidth /3, screenHeight/3); 
		}
	}
	
	//游戏玩法对话框
	class HowToPlayDialog extends JDialog{
		public HowToPlayDialog(JFrame owner){
			super(owner, "游戏玩法", true);
			add(new JLabel("<html><h2><i>游戏玩法</i><h2><hr/><p>按动上下左右按钮来移动空格，使整个九宫格变成如下就胜利了~~</p>" +
					"<p>1 2 3</p>" +
					"<p>4 5 6</p>" +
					"<p>7 8 </p></html>"), 
					BorderLayout.CENTER);
			JPanel panel = new JPanel();
			JButton ok = new JButton("OK");
			
			ok.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event) {
					setVisible(false);
				}
			});
			
			panel.add(ok);
			add(panel, BorderLayout.SOUTH);
			this.setBounds(screenWidth * 3 / 8, screenHeight * 3 / 8, screenWidth /3, screenHeight/3); 
		}
	}
	
	
	
	public int screenWidth;
	public int screenHeight;
	
	public JMenu setLookMenu;//观感
	public JMenuBar menuBar = new JMenuBar();
	public static JTextArea textAreaEast;//东边的，用来显示游戏说明以及答案
	public static ArrayList<JTextField> box = new ArrayList<JTextField>(); //九宫格的数据结构
	public JButton okButton;
	public JLabel countLabel = new JLabel("您已经走了的步数：0");
	public int count;//记录一共走了多少步
}
