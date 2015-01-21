package com.xujin;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

//解答类，用来产生解
class SolvePuzzle {
	public SolvePuzzle(int[] startStatus){
		this.startStatus = new Node(startStatus); //将初始的数组转化为初始node
	}

	
	public String solve(){
		return process(startStatus);
	}
	
	
	
	//A* search
	public String process(Node node){
		
		node.preNode = null;
		open.add(node);
		Node parentNode;
		Node nextOpen;
		int space = 0;
		
		label:
		while(!open.isEmpty()){
			parentNode = open.peek();
			close.add(parentNode);
			open.remove();
			
			
			//找出空格
			for(int i = 0; i < 9; i++){
				if(parentNode.arrange[i] == 0) 
					space = i;				
			}
			
			
			for(int i = 0; i < 4; i++){
				nextOpen = new Node();
				for(int m = 0; m < 9; m++)
					nextOpen.arrange[m] = parentNode.arrange[m];
				//空格上移
				if(i == 0){
					if(space/3 == 0) //在第0行
						continue;
					
					//交换空格和另一个
					nextOpen.arrange[space] = nextOpen.arrange[space - 3];
					nextOpen.arrange[space - 3] = 0;
					
					nextOpen.preOper = "up";
				}
				//下移
				else if(i == 1){
					if(space/3 == 2) //在第2行
						continue;
					
					//交换空格和另一个					
					nextOpen.arrange[space] = nextOpen.arrange[space + 3];
					nextOpen.arrange[space + 3] = 0;	
					
					nextOpen.preOper = "down";
				}
				//左移
				else if(i == 2){
					if(space%3 == 0) //在第0列
						continue;
					
					//交换空格和另一个
					nextOpen.arrange[space] = nextOpen.arrange[space - 1];
					nextOpen.arrange[space - 1] = 0;	
					
					nextOpen.preOper = "left";
				}
				//右移
				else if(i == 3){
					if(space%3 == 2) //在第2列
						continue;
					
					//交换空格和另一个
					nextOpen.arrange[space] = nextOpen.arrange[space + 1];
					nextOpen.arrange[space + 1] = 0;
					
					nextOpen.preOper = "right";
				}				
							
				//若close表不包含nextOpen状态，那么将其加入close表
				if(!close.contains(nextOpen)){
					nextOpen.stepts = parentNode.stepts + 1;
					nextOpen.preNode = parentNode;					
					open.add(nextOpen);
				}	
				
				//查找是否找到
				if(EightNumUI.finish(nextOpen.arrange)) {
					nextOpen.preNode = parentNode;
					result += "A*搜索之后，最少需要的步数为：" + nextOpen.stepts + "\n\n";
					print(nextOpen);
					break label;
				}	
				
			}//for
			
		}//while	
		return result;
	}
	
	    
	
	private void print(Node node){
		if(node.preNode != null)
			print(node.preNode);
		result +=  "第"  + node.stepts + "步：" + node.preOper + "\n" + node;
	}

	private Node startStatus;
	private String result = "";
	private Queue<Node> open = new LinkedList<Node>();
	//private Queue<Node> open = new PriorityQueue<Node>();
	private HashSet<Node> close = new HashSet<Node>();
}



class Node implements Comparable<Node>{
	public Node(int[] arrange){
		this.arrange = arrange;
	}
	
	public Node(){}
	
	public int compareTo(Node other){
		if(f(this) - f(other) > 0) return 1;
		else if(f(this) - f(other) < 0) return -1;
		else return 0;
	}
	
	//A* 关键
	private int f(Node node){
		return node.stepts + manhattan(node);
	}
	
	//求状态a与目标状态的曼哈顿距离
	private int manhattan(Node node){
		int[] a = node.arrange;
		int startX, startY;
		int endX, endY;                                                                                                                                    
		int manhattanDistance = 0;
		for(int i = 0; i < 9; i++){
			if(a[i] == 0) continue;
			startX = i / 3;
			startY = i % 3;
			endX = (a[i] - 1) / 3;
			endY = (a[i] - 1) % 3;
			manhattanDistance += Math.abs(startX - endX) + Math.abs(startY - endY);
		}		
		return manhattanDistance;
	}
	
	@Override
	public int hashCode(){
		int sum = 0;
		for(int i = 0; i < 9 ;i++){
			sum =  sum * 10 + arrange[i];
		}
		return sum;
	}

	@Override
	public boolean equals(Object node){
		//System.out.println("ok,ok");
		if(this.hashCode() == node.hashCode()) return true;
		return false;
	}
	
	@Override
	public String toString(){
		String result = "";
		for(int i = 0; i < 9 ;i++){
			result = result + arrange[i] + " ";
			if(i % 3 == 2) result += "\n";
		}
		return result + "\n";
	}
	
	public int[] arrange = new int[9];
	public Node preNode;
	public int stepts = 0;
	public String preOper = "";//记录上次的操作是把空格挪到哪里
}
