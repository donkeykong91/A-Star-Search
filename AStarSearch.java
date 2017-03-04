package com.cs420.project1;

import java.util.Scanner;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.lang.Integer;

public class AStarSearch {
	
	static List<Integer> path = new ArrayList<Integer>();
	static int[][] solution = new int[][]{{0, 1, 2},
										  {3, 4, 5},
										  {6, 7, 8}};
	public static void main(String[] args) {				
		System.out.print("A* Search\n\n" + 
							"1. A randomly generated 8-puzzle problem.\n" +
							"2. An entered 8-puzzle configuration for one puzzle.\n\n" +
							"Please choose an option: ");
		
		try (Scanner cin = new Scanner(System.in)) {
			while (true)
			{
				int input = cin.nextInt();
				
				if (input == 1) {
					System.out.println("Generating 101 random puzzles...");
					
					int[][] tables = generateTables();
					
					for(int i = 0; i < tables.length; i++) {
						if(isSolvable(tables[i])) {aStarSearch1(tables[i]);}
						else {System.out.println("\nNot Solvable");}
					}
					
					for(int i = 0; i < tables.length; i++) {
						if(isSolvable(tables[i])) {aStarSearch2(tables[i]);}
						else {System.out.println("\nNot Solvable");}
					}
					
					break;
				} else if (input == 2) {
					int[][] puzzle2D = generateSpecificTable();
					
					int[] puzzle1D = puzzle2DTo1D(puzzle2D);
					aStarSearch1(puzzle1D);
					aStarSearch2(puzzle1D);

					break;
				} else { System.out.print("\nPlease enter either \"1\" or \"2\": ");}
			}
		}
	}
	
	public static boolean isSolvable(int[] puzzle) {
		int inversions = 0;
		
		for (int i = 0; i < puzzle.length; i++) {
			for (int j = i + 1; j < puzzle.length; j++) {
				if (puzzle[i] > puzzle[j] && (puzzle[j] != 0 && puzzle[i] != 0)) {inversions++;}
			}
		}
		
		if(inversions % 2 == 1) {
			return false;
		} else {return true;}
	}
	
	public static int[][] generateTables() {
		Integer[][]randomIntegerPuzzles = new Integer[101][9]; 
		int[][] randomIntPuzzles = new int[101][9];
		for (int row = 0; row < randomIntegerPuzzles.length; row++) {
			for (int col = 0; col < randomIntegerPuzzles[row].length; col++) {
				randomIntegerPuzzles[row][col] = col;
			}
			Collections.shuffle(Arrays.asList(randomIntegerPuzzles[row]));
		}
		
		for (int row = 0; row < randomIntegerPuzzles.length; row++) {
			for (int col = 0; col < randomIntegerPuzzles[row].length; col++) {
				randomIntPuzzles[row][col] = randomIntegerPuzzles[row][col];
			}
		}
		
		return randomIntPuzzles;
	}
	
	public static int[][] generateSpecificTable() {
		System.out.print("\nPlease enter numbers 0 to 8 randomly: ");
		
		try (Scanner cin = new Scanner(System.in)) {
			String strPuzzle = cin.next();
			System.out.println();
			
			while(true) {
				
				if (strPuzzle.length() == 9) {
					int[] intPuzzle = new int[strPuzzle.length()];
					int[][] puzzle;
				
					for (int i = 0; i < strPuzzle.length(); i++) {
						intPuzzle[i] = strPuzzle.charAt(i) - '0';
					}
					
					if(isSolvable(intPuzzle)) {
						puzzle = puzzle1DTo2D(intPuzzle);
						
						for (int row = 0; row < puzzle.length; row++) {
							for (int col = 0; col < puzzle[row].length; col++) {
								System.out.print(puzzle[row][col] + " ");
							}
							System.out.println();
						}
						
						return puzzle;
					} else {
						System.out.print("The puzzle is unsolvable. Please try another combination: ");
						strPuzzle = cin.next();
						System.out.println();
					}
				
				} else {
					System.out.print("Please enter only nine numbers from 0 to 8 randomly: ");
					strPuzzle = cin.next();
					System.out.println();
				}
			}
		}
	}
	
	public static void getPath(Node node) {
		if (node.getPredecessor() == null) {System.out.print(node.getNodeId() + " -->  ");}
		else {
			getPath(node.getPredecessor());
			System.out.print(node.getNodeId() + " -->  ");
		}
	}
	
	public static int[][] puzzle1DTo2D(int[] puzzle) {
		int[][] puzzle2D = new int[3][3];
		int index = 0;
		
		for(int row = 0; row < puzzle2D.length; row++) {
			for(int col = 0; col < puzzle2D[row].length; col++) {
				puzzle2D[row][col] = puzzle[index++];
			}
		}
		return puzzle2D;
	}
	
	public static int[] puzzle2DTo1D(int[][] puzzle2D) {
		int[] puzzle1D = new int[9];
		List<Integer> puzzleList1D = new ArrayList<Integer>();
		
		for (int row = 0; row < puzzle2D.length; row++) {
			for (int col = 0; col < puzzle2D[row].length; col++) {
				puzzleList1D.add(puzzle2D[row][col]);
			}
		}
		
		for (int i = 0; i < puzzle1D.length; i++) {
			puzzle1D[i] = puzzleList1D.get(i);
		}
		return puzzle1D;
	}
	
	public static int getRow(int[][] solution, int value) {
		for (int row = 0; row < solution.length; row++) {
			for (int col = 0; col < solution[row].length; col++) {
				if (solution[row][col] == value) {return row;}
			}
		}
		return -1;
	}
	
	public static int getCol(int[][] solution, int value) {
		for (int row = 0; row < solution.length; row++) {
			for (int col = 0; col < solution[row].length; col++) {
				if (solution[row][col] == value) {return col;}
			}
		}
		return -1;
	}
	
	public static int[][] nextState(int index, List<Integer[][]> checkList) {
		Integer[][] integerCheckList = checkList.remove(index);
		int[][] nextState = new int[3][3];
		
		for (int row = 0; row < integerCheckList.length; row++) {
			for (int col = 0; col < integerCheckList[row].length; col++) {
				nextState[row][col] = integerCheckList[row][col];
			}
		}
		
		return nextState;
	}
	
	public static void listSuccessorStates(int[][] puzzle2D, 
			List<Integer[][]> checkList, String nodeId, int[] puzzle1D) {
		int zeroPosition = 0;
		
		for (int row = 0; row < puzzle2D.length; row++) {
			for (int col = 0; col < puzzle2D[row].length; col++) {
				if (puzzle2D[row][col] == 0) {
					zeroPosition = Integer.parseInt(new String("" + row + col));
					
					switch (zeroPosition) {
						case 11:
							checkList = fourMovesState(zeroPosition, puzzle2D, checkList);
							break;
						
						case 01: case 10:
						case 12: case 21:
							checkList =  threeMovesState(zeroPosition, puzzle2D, checkList);
							break;
						
						case 02: case 22: 
						case 20: case 00:
							checkList =  twoMovesState(zeroPosition, puzzle2D, checkList);
							break;
						
						default:
							System.out.println("Something went wrong...");
							break;
					}
					break;
				}
			}
			if(zeroPosition > 0) {break;}
		}
	}
	
	public static List<Integer[][]> fourMovesState(int zeroPosition, int[][] puzzle, List<Integer[][]> checkList) {
		Integer[][] newState1 = new Integer[3][3];
		Integer[][] newState2 = new Integer[3][3];
		Integer[][] newState3 = new Integer[3][3];
		Integer[][] newState4 = new Integer[3][3];
		int newRow = zeroPosition / 10;
		int newCol = zeroPosition % 10;
		
		for (int row = 0; row < puzzle.length; row++) {
			for (int col = 0; col < puzzle[row].length; col++) {
				newState1[row][col] = puzzle[row][col];
			}
		}
		
		for (int row = 0; row < puzzle.length; row++) {
			for (int col = 0; col < puzzle[row].length; col++) {
				newState2[row][col] = puzzle[row][col];
			}
		}
		
		for (int row = 0; row < puzzle.length; row++) {
			for (int col = 0; col < puzzle[row].length; col++) {
				newState3[row][col] = puzzle[row][col];
			}
		}
		
		for (int row = 0; row < puzzle.length; row++) {
			for (int col = 0; col < puzzle[row].length; col++) {
				newState4[row][col] = puzzle[row][col];
			}
		}
		
		newState1[0][1] = puzzle[newRow][newCol];
		newState1[1][1] = puzzle[0][1];
		
		newState2[1][0] = puzzle[newRow][newCol];
		newState2[1][1] = puzzle[1][0];
		
		newState3[1][2] = puzzle[newRow][newCol];
		newState3[1][1] = puzzle[1][2];
		
		newState4[2][1] = puzzle[newRow][newCol];
		newState4[1][1] = puzzle[2][1];
		
		checkList.add(newState1);
		checkList.add(newState2);
		checkList.add(newState3);
		checkList.add(newState4);
		
		return checkList;
	}
	
	public static List<Integer[][]> threeMovesState(int zeroPosition, int[][] puzzle, List<Integer[][]> checkList) {
		Integer[][] newState1 = new Integer[3][3];
		Integer[][] newState2 = new Integer[3][3];
		Integer[][] newState3 = new Integer[3][3];
		int newRow = zeroPosition / 10;
		int newCol = zeroPosition % 10;
		
		if (zeroPosition == 01) {
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState1[row][col] = puzzle[row][col];
				}
			}
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState2[row][col] = puzzle[row][col];
				}
			}
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState3[row][col] = puzzle[row][col];
				}
			}
			
			newState1[0][0] = puzzle[newRow][newCol]; //zero
			newState1[0][1] = puzzle[0][0];
			
			newState2[1][1] = puzzle[newRow][newCol];
			newState2[0][1] = puzzle[1][1];
			
			newState3[0][2] = puzzle[newRow][newCol];
			newState3[0][1] = puzzle[0][2];
			
			checkList.add(newState1);
			checkList.add(newState2);
			checkList.add(newState3);
			
			return checkList;
			
		} else if (zeroPosition == 10) {
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState1[row][col] = puzzle[row][col];
				}
			}
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState2[row][col] = puzzle[row][col];
				}
			}
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState3[row][col] = puzzle[row][col];
				}
			}
			
			newState1[0][0] = puzzle[newRow][newCol];
			newState1[1][0] = puzzle[0][0];
			
			newState2[1][1] = puzzle[newRow][newCol];
			newState2[1][0] = puzzle[1][1];
			
			newState3[2][0] = puzzle[newRow][newCol];
			newState3[1][0] = puzzle[2][0];
			
			checkList.add(newState1);
			checkList.add(newState2);
			checkList.add(newState3);
			
			return checkList;
			
		} else if (zeroPosition == 12) {
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState1[row][col] = puzzle[row][col];
				}
			}
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState2[row][col] = puzzle[row][col];
				}
			}
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState3[row][col] = puzzle[row][col];
				}
			}
			
			newState1[0][2] = puzzle[newRow][newCol];
			newState1[1][2] = puzzle[0][2];
			
			newState2[1][1] = puzzle[newRow][newCol];
			newState2[1][2] = puzzle[1][1];
			
			newState3[2][2] = puzzle[newRow][newCol];
			newState3[1][2] = puzzle[2][2];
			
			checkList.add(newState1);
			checkList.add(newState2);
			checkList.add(newState3);
			
			return checkList;
			
		} else if (zeroPosition == 21) {
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState1[row][col] = puzzle[row][col];
				}
			}
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState2[row][col] = puzzle[row][col];
				}
			}
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState3[row][col] = puzzle[row][col];
				}
			}
			
			newState1[2][0] = puzzle[newRow][newCol];
			newState1[2][1] = puzzle[2][0];
			
			newState2[1][1] = puzzle[newRow][newCol];
			newState2[2][1] = puzzle[1][1];
			
			newState3[2][2] = puzzle[newRow][newCol];
			newState3[2][1] = puzzle[2][2];
			
			checkList.add(newState1);
			checkList.add(newState2);
			checkList.add(newState3);
			
			return checkList;
		}
		return null;
	}
	
	public static List<Integer[][]> twoMovesState(int zeroPosition, int[][] puzzle, List<Integer[][]> checkList) {
		Integer[][] newState1 = new Integer[3][3];
		Integer[][] newState2 = new Integer[3][3];
		int newRow = zeroPosition / 10;
		int newCol = zeroPosition % 10;
		
		if (zeroPosition == 02) {
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState1[row][col] = puzzle[row][col];
				}
			}
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState2[row][col] = puzzle[row][col];
				}
			}
			
			newState1[0][1] = puzzle[newRow][newCol];
			newState1[0][2] = puzzle[0][1];
			
			newState2[1][2] = puzzle[newRow][newCol];
			newState2[0][2] = puzzle[1][2];
			
			checkList.add(newState1);
			checkList.add(newState2);
			
			return checkList;
			
		} else if (zeroPosition == 22) {
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState1[row][col] = puzzle[row][col];
				}
			}
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState2[row][col] = puzzle[row][col];
				}
			}
			
			newState1[2][1] = puzzle[newRow][newCol];
			newState1[2][2] = puzzle[2][1];
			
			newState2[1][2] = puzzle[newRow][newCol];
			newState2[2][2] = puzzle[1][2];
			
			checkList.add(newState1);
			checkList.add(newState2);
			
			return checkList;
			
		} else if (zeroPosition == 20) {
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState1[row][col] = puzzle[row][col];
				}
			}
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState2[row][col] = puzzle[row][col];
				}
			}
			
			newState1[1][0] = puzzle[newRow][newCol];
			newState1[2][0] = puzzle[1][0];
			
			newState2[2][1] = puzzle[newRow][newCol];
			newState2[2][0] = puzzle[2][1];
			
			checkList.add(newState1);
			checkList.add(newState2);
			
			return checkList;
			
		} else if (zeroPosition == 00) {
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState1[row][col] = puzzle[row][col];
				}
			}
			
			for (int row = 0; row < puzzle.length; row++) {
				for (int col = 0; col < puzzle[row].length; col++) {
					newState2[row][col] = puzzle[row][col];
				}
			}
			
			newState1[0][1] = puzzle[newRow][newCol];
			newState1[0][0] = puzzle[0][1];
			
			newState2[1][0] = puzzle[newRow][newCol];
			newState2[0][0] = puzzle[1][0];
			
			checkList.add(newState1);
			checkList.add(newState2);
			
			return checkList;
			
		}
		return null;
	}
	
	public static Node addSuccessor(int[][] puzzle, int h, Node predecessor) {
		Node node = new Node(puzzle, predecessor.getG() + 1, h, predecessor);
		predecessor.getSuccessors().add(node);
		
		return node;
	}
	
	public static int firstHeuristic(int[] puzzle) {
		int count = 0;
		
		for (int i = 1; i < puzzle.length; i++) {
			if (puzzle[i] != i) {count++;}
		}
		return count;
	}
	
	public static int secondHeuristic(int[] puzzle) {
		int[][] puzzle2D = puzzle1DTo2D(puzzle);
		int count = 0;
		int expected = 0;
		int value;
		
		for(int row = 0; row < puzzle2D.length; row++) {
			for(int col = 0; col < puzzle2D[row].length; col++) {
				value = puzzle2D[row][col];
				
				if(value != 0 && value != expected++) {
					count += Math.abs(row 
							- getRow(solution, value))
							+ Math.abs(col
									- getCol(solution, value));
				}
			}
		}
		return count;
	}
	
	public static void aStarSearch1(int[] puzzle1D) {
		System.out.println("\nFirst Heuristics: ");
		
		Queue<Node> frontier = new PriorityQueue<Node>(11, new NodeComparator());
		Set<String> exploredSet = new HashSet<String>();
		List<Integer[][]> checkList = new ArrayList<Integer[][]>();
		int[][] puzzle2D = puzzle1DTo2D(puzzle1D);
		int g = 0, index = 0;
		
		frontier.add(new Node(puzzle2D, g, firstHeuristic(puzzle1D), null));
		
		while (!frontier.isEmpty()) {
			Node node = frontier.poll();
			
			if(!exploredSet.contains(node.getNodeId())) {
				exploredSet.add(node.getNodeId());
				listSuccessorStates(node.getPuzzle(), checkList, node.getNodeId(), node.getPuzzle1D());
				
				if (Arrays.deepEquals(node.getPuzzle(), solution)) {
					getPath(node);
					System.out.print("Done!");
					System.out.println();
					System.out.println();
					break;
				} else {
					while (!checkList.isEmpty()) {
						frontier.add(addSuccessor(nextState(index, 
								checkList), firstHeuristic(puzzle1D), node));
					}
				}
			} else {continue;}
		}
	}
	
	public static void aStarSearch2(int[] puzzle1D) {
		System.out.println("\nSecond Heuristics: ");
		
		Queue<Node> frontier = new PriorityQueue<Node>(11, new NodeComparator());
		Set<String> exploredSet = new HashSet<String>();
		List<Integer[][]> checkList = new ArrayList<Integer[][]>();
		int[][] puzzle2D = puzzle1DTo2D(puzzle1D);
		int g = 0, index = 0;
		
		frontier.add(new Node(puzzle2D, g, secondHeuristic(puzzle1D), null));
		
		while (!frontier.isEmpty()) {
			Node node = frontier.poll();
			
			if(!exploredSet.contains(node.getNodeId())) {
				exploredSet.add(node.getNodeId());
				listSuccessorStates(node.getPuzzle(), checkList, node.getNodeId(), node.getPuzzle1D());
				
				if (Arrays.deepEquals(node.getPuzzle(), solution)) {
					getPath(node);
					System.out.print("Done!");
					System.out.println();
					System.out.println();
					break;
				} else {
					while (!checkList.isEmpty()) {
						frontier.add(addSuccessor(nextState(index, 
								checkList), secondHeuristic(puzzle1D), node));
					}
				}
			} else {continue;}
		}
	}
}

class Node {
	private String id;
	private int f, g, h;
	private int[][] puzzle;
	private final Node predecessor;
	private final List<Node> successors = new ArrayList<>();
	
	public Node (int[][] puzzle, int g, int h, Node predecessor) {
		this.id = generateNodeId(puzzle);
		this.puzzle = puzzle;
		this.predecessor = predecessor;
		this.g = g;
		this.h = h;
		calcF();
	}
	
	public String getNodeId() {return id;}
	
	public Node getPredecessor() {return predecessor;}
	
	public List<Node> getSuccessors() {return successors;}
	
	public int getG() {return g;}
	
	public int getH() {return h;}
	
	public int getF() {return f;}
	
	public int[][] getPuzzle() {return puzzle;}
	
	public int[] getPuzzle1D() {return puzzle2DTo1D(puzzle);}
	
	private void calcF() {this.f = g + h;}
	
	private int[] puzzle2DTo1D (int[][] puzzle2D) {
		int[] puzzle1D = new int[9];
		List<Integer> puzzleList1D = new ArrayList<Integer>();
		
		for (int row = 0; row < puzzle2D.length; row++) {
			for (int col = 0; col < puzzle2D[row].length; col++) {
				puzzleList1D.add(puzzle2D[row][col]);
			}
		}
		
		for (int i = 0; i < puzzle1D.length; i++) {
			puzzle1D[i] = puzzleList1D.get(i);
		}
		return puzzle1D;
	}
	
	private String generateNodeId(int[][] puzzle) {
		return Arrays.toString(puzzle2DTo1D(puzzle));
	}
}

class NodeComparator implements Comparator<Node> {
    public int compare(Node nodeFirst, Node nodeSecond) {
        if (nodeFirst.getF() > nodeSecond.getF()) return 1;
        if (nodeSecond.getF() > nodeFirst.getF()) return -1;
        return 0;
    }
} 













































