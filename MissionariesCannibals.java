//Jose Saldivar
// 9/15/20
package Homework3;
import java.util.LinkedList;
import java.util.Queue;

/*
 
 * (States)
 - list all possible states that can be chosen
 - drew a state graph

 * (Build Tree) 
 - Tree Class
 - Node Class
 - Insert Class
 - isEmpty Class
 - Find Class
 - Successor Class
 - Print Graph or Solution Class

 * (BFS data structure)
 - To find shortest depth of getting to goal state

 */

class State
{
	private int CL;
	private int CR;
	private int ML;
	private int MR;
	private int boat;			//Either 	0 for Left side || 1 for Right side
	public State rightChild;
	public State leftChild;

	// Generate constructor
	public State(int cLeft, int mLeft, int boat, int cRight, int mRight) {
		super();
		this.CL = cLeft;
		this.CR = cRight;
		this.ML = mLeft;
		this.MR = mRight;
		this.boat = boat;
	}

	// Generate getters and setters
	public int getCL() {
		return CL;
	}

	public void setCL(int cL) {
		CL = cL;
	}

	public int getCR() {
		return CR;
	}

	public void setCR(int cR) {
		CR = cR;
	}

	public int getML() {
		return ML;
	}

	public void setML(int mL) {
		ML = mL;
	}

	public int getMR() {
		return MR;
	}

	public void setMR(int mR) {
		MR = mR;
	}

	public int getBoat() {
		return boat;
	}

	public void setBoat(int boat) {
		this.boat = boat;
	}

	@Override
	public String toString() {
		return "State < " + CL + " : " + ML + " : " + boat + " : " + CR + " : " + MR + " >";
	}

	// if all 3 cannibals and missionaries are on the right YAAYYYY!!
	public boolean goalState()
	{
		if(CL == 0 && ML == 0 && 
				CR == 3 && MR == 3 && boat == 1)
			return true;
		return false;
	}

	/*
	 Missionaries on left can not be less than Cannibals
	 Missionaries on right can not be less than Cannibals
	 */
	public boolean validMoves()
	{	
		if(ML >= 0 && ML >= CL && (MR >= 0 || MR >= CR))
			return true;
		return false;
	}
}
/////////////////////////////////////////////////////////////////////////////////

class Tree 
{
	private State root;
	Queue<State> q = new LinkedList<State>();
	//Constructor
	public Tree()
	{ root = null; }

	public boolean isEmpty()
	{
		if(root == null)
			return true;
		return false;
	}

	public int height(State root)
	{
		if(root == null)
			return 0;
		else
		{
			int L = height(root.leftChild);
			int R = height(root.rightChild);
			if (L > R)
				return L + 1;
			else
				return R + 1;
		}
	}

	// insert new STATE if validMoves() is TRUE
	//public void insert(State state)
	public void insert(int CL, int ML, int boat, int CR, int MR)
	{
		State newNode = new State(CL, ML, boat, CR, MR);    // make new node

		if(root==null)       // no node in root
		{
			root = newNode;
			q.add(newNode);
		}
		else                          // root occupied
		{
			State current = root;
			State parent;
			while(true)
			{
				parent = current;
				if(newNode.validMoves() == true)
				{
					current = current.leftChild;
					if(current == null)
					{
						parent.leftChild = newNode;
						q.add(newNode);
					}
					else
					{
						current = current.rightChild;
						if(current == null)
						{
							parent.rightChild = newNode;
							q.add(newNode);
						}
					}
					return;
				}
				else
					return;
			}//END WHILE
		}//END ELSE
	}  

	public void runTree()
	{
		/*	Can either send 2 cannibals over on the boat across the river OR 1 cannibals and 1 missionary 
		 *	for first move, unless you wanted to send to missionaries over the river but would leave 1 missionary
		 *  alone with 3 cannibals thus not being a valid choice to use.
			( CL, ML, boat, CR, MR )		
		*/
		State s0 = new State(3, 3, 0, 0, 0); 			// ROOT
		s0.leftChild = s0.rightChild = null;
		q.add(s0);
		root = q.peek();

		// boat --> 2 cannibals
		State s1 = new State(1,3,1,2,0);				// 1.1 state transition 
		s0.leftChild = s1;
		s1.leftChild = s1.rightChild = null;
		q.add(s1);

		// boat --> 1 missionary & 1 cannibal
		State s2 = new State(2,2,1,1,1);				// 1.2 state transition
		s0.rightChild = s2;
		s2.leftChild = s2.rightChild = null;
		q.add(s2);

		// boat <-- missionary drives boat back and cannibal stays on other side of river
		State s3 = new State(2,3,0,1,0);				// 2.1 state transition
		s2.leftChild = s3;
		s3.leftChild = s3.rightChild = null;
		q.add(s3);

		// boat --> 2 missionaries drive across to other side
		State s4 = new State(0,3,1,3,0);				// 3.1 state transition
		s3.leftChild = s4;
		s4.leftChild = s4.rightChild = null;
		q.add(s4);

		//boat <-- cannibal drives back and leaves a total of 2 cannibals on right side
		State s5 = new State(1,3,0,2,0);				// 4.1 state transition
		s4.rightChild = s5;
		s5.leftChild = s5.rightChild = null;
		q.add(s5);

		// boat --> 2 missionaries cross the river. Total of 2 cannibals & 2 missionaries on right side
		State s6 = new State(1,1,1,2,2);				// 5.1 state transition
		s5.rightChild = s6;
		s6.leftChild = s6.rightChild = null;
		q.add(s6);

		// boat <-- 1 missionary & 1 cannibal cross the river to the left side
		State s7 = new State(2,2,0,1,1);				// 6.1 state transition
		s6.leftChild = s7;
		s7.leftChild = s7.rightChild = null;
		q.add(s7);

		// boat --> 2 missionaries cross the river. Total of 3 missionaries & 1 cannibal on right side.
		State s8 = new State(2,0,1,1,3);				// 7.1 state transition
		s7.leftChild = s8;
		s8.leftChild = s8.rightChild = null;
		q.add(s8);

		// boat <-- 1 cannibal crosses the river on the boat. Total of 3 missionaries on right side
		State s9 = new State(3,0,0,0,3);				// 8.1 state transition
		s8.rightChild = s9;
		s9.leftChild = s9.rightChild = null;
		q.add(s9);

		// boat --> 2 cannibals crosses river. Total of 3 missionaries & 2 cannibals on the right side.
		State s10 = new State(1,0,1,2,3);				// 9.1 state transition
		s9.rightChild = s10;
		s10.leftChild = s10.rightChild = null;
		q.add(s10);

		// boat <-- 1 cannibal crosses river. Total of 3 missionaries & 1 cannibals on right side
		State s11 = new State(2,0,0,1,3);				// 10.1 state transition
		s10.rightChild = s11;
		s11.leftChild = s11.rightChild = null;
		q.add(s11);

		// boat --> 2 cannibals cross over. Total of 3 missionaries and 3 cannibals arrive safely
		// to right side of the river which is the goal state.
		State s12 = new State(0,0,1,3,3);
		s11.leftChild = s12;
		s12.leftChild = s12.rightChild = null;
		q.add(s12);

		if(s12.goalState() == true)
		{
			System.out.println("Reached the goal state!!!\n");
		}
		bfs();
	}
	
	public void bfs()
	{
		// find the amount of levels in the tree
		int h = height(root) - 1;
		System.out.println("HEIGHT = " + h + "\n");
		
		Queue<State> BFS = new LinkedList<State>();
		if(root == null)
			return;
		
		BFS.add(root);
		while(!BFS.isEmpty())
		{
			State newNode = (State)BFS.remove();
			System.out.println(newNode.toString());
			if(newNode.leftChild != null)
				BFS.add(newNode.leftChild);
			if(newNode.rightChild != null)
				BFS.add(newNode.rightChild);
		}
	}

	public void print()
	{
		Queue<State> globalQ = new LinkedList<State>();
		globalQ.add(root);
		boolean empty = false;

		while(empty == false)
		{
			Queue<State> localQ = new LinkedList<State>();
			empty = true;
			while(globalQ.isEmpty() == false)
			{
				State temp = (State) globalQ.poll();
				if(temp != null)
				{
					System.out.println(temp.toString());	
					localQ.add(temp.leftChild);
					localQ.add(temp.rightChild);

					if(temp.leftChild != null || temp.rightChild != null)
						empty = false;
				}
			}
			while(localQ.isEmpty() == false)
				globalQ.add(localQ.poll());
		}//END WHILE
	}//END WHILE

}	// END TREE CLASS


/////////////////////////////////////////////////////////////////////////////////////////////////
public class MissionariesCannibals {


	public static void main(String[] args) {

		Tree tree = new Tree();
		tree.runTree();		// checks goalState() function and uses BFS()

		
		// First scheme was thinking of going with
		
		/*
		int[][] arr = new int[2][3];
		// 3 Missionaries and 3 Cannibals
		arr[0][0] = 3;
		arr[1][0] = 3;

		while(arr[0][1] == 0 || arr[1][1] == 0)
		{
			arr[0][1] = arr[0][0] -1;
			arr[1][1] = arr[1][0] -1;
		}

		for(int i=0; i<2; i++)
		{
			for(int j=0; j<3; j++)
			{
				System.out.print(arr[i][j] + " || ");
			}
			System.out.println("\n");
		}//end for
		 */
	}
}
