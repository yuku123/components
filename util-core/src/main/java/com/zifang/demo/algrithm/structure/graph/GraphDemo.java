package com.zifang.demo.algrithm.structure.graph;

import java.util.Scanner;

public class GraphDemo {
	private final static int MAX_VERTEX_NUM = 20;
	
	public static void greateGraph(MGraph graph){
		System.out.println("请输入图的定点数和边数，用逗号隔开：");
		Scanner sc = new Scanner(System.in);
		graph.vexnum=sc.nextInt();
		graph.arcnum=sc.nextInt();
		
		System.out.println("请输入顶点信息");
		for(int i=0; i<graph.vexnum; i++){
			graph.vexs[i] = sc.next();
			
		}
		
		for(int i=0; i<graph.vexnum; i++){
			for (int j = 0; j < graph.vexnum; j++) {
				graph.arcs[i][j] = 0;
			}
		}
		
		System.out.println("请输入邻接的两个顶点（i,j）:");
		String a,b;
		int x,y;
		for(int i=0; i<graph.arcnum; i++){
			System.out.println("第"+(i+1)+"组：");
			a = sc.next();
			x = locate(graph, a);
			b = sc.next();
			y = locate(graph, b);
			graph.arcs[x][y] = 1;
			graph.arcs[y][x] = 1;
		}
	}
	
	public static int locate(MGraph graph, String s){
		for (int i = 0; i < graph.vexnum; i++) {
			if(graph.vexs[i].equals(s)){
				return i;
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		greateGraph(new MGraph());
	}
	
	static class MGraph{
		String[] vexs = new String[MAX_VERTEX_NUM];
		int[][] arcs = new int[MAX_VERTEX_NUM][MAX_VERTEX_NUM];
		int vexnum, arcnum;
	}
}
