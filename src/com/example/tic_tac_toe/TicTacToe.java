package com.example.tic_tac_toe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;


public class TicTacToe {
	public static void main(String[] args) {
		String path = "TicTacToe.txt"; // change to the input file.
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			int testCasesCount = Integer.parseInt(in.readLine());
			for (int i = 0; i < testCasesCount; i++) {
				String[] baseInfo = in.readLine().split(" ");
				int n = Integer.parseInt(baseInfo[0]);
				int m = Integer.parseInt(baseInfo[1]);
				if (n == 0 || m == 0) {
					System.out.println("invalid input: board " + i);
					continue;
				}
				char[][] board = new char[n][n];
				for (int j = 0; j < n; j++) {
					String line = in.readLine();
					for (int k = 0; k < n; k++) {
						board[j][k] = line.charAt(k);
					}
				}
				System.out.println("\n========BOARD: " + (i+1) + "===============");
				for (int j = 0 ; j < board.length; j++){
					for (int k = 0; k<board[0].length; k++){
						System.out.print(board[j][k]);
					}
					System.out.println();
				}
				//My start
				System.out.println(m + " in a row to win");
				System.out.println(BoardAnalyzer(n, m, board));
				//My end
			}

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	static String BoardAnalyzer(int mBoardSize, int mNumToWin, char[][] mElement){

		char strResults='N';
		ArrayList<String> strObject = new ArrayList<String>();
		int i,j,count,OCount=0,XCount=0;
		boolean isProsess=false;
		HashMap <String,String> mWinlist = new HashMap<String,String>();
		for(int x=0; x<mBoardSize; x++){
			for(int y=0; y<mBoardSize; y++){
				if(mElement[x][y]=='.'){
					isProsess = true;
					continue;
				}
				else if(mElement[x][y]=='X')
					XCount++;
				else
					OCount++;					
				for(i=0,count=0;(y-i>=0)&&(mElement[x][y-i]==mElement[x][y]);i++){
					if(strResults!='E'&&++count==mNumToWin){
						strObject.clear();
						for(j=0; j<mNumToWin; j++)
							strObject.add(x+" "+(y-j));
						strResults = CheckIntersectOfWins(mWinlist, strObject, mElement[x][y]);
						
					}
				}
				for(i=0,count=0;(x-i>=0)&&(mElement[x-i][y]==mElement[x][y]);i++){
					if(strResults!='E'&&++count==mNumToWin)
					{
						strObject.clear();
						for(j=0; j<mNumToWin; j++)
							strObject.add(x-j+" "+y);
						if (strResults=='E') break;
						strResults = CheckIntersectOfWins(mWinlist, strObject, mElement[x][y]);
					}
				}
				for(i=0,count=0;(x-i>=0)&&(y-i>=0)&&(mElement[x-i][y-i]==mElement[x][y]);i++){
					if(strResults!='E'&&++count==mNumToWin)
					{
						strObject.clear();
						for(j=0; j<mNumToWin; j++)
							strObject.add(x-j+" "+(y-j));
						if (strResults=='E') break;
						strResults = CheckIntersectOfWins(mWinlist, strObject, mElement[x][y]);
					}
				}
				for(i=0,count=0;(x-i>=0)&&(y+i<mBoardSize)&&(mElement[x-i][y+i]==mElement[x][y]);i++){
					if(strResults!='E'&&++count==mNumToWin)
					{
						strObject.clear();
						for(j=0; j<mNumToWin; j++)
							strObject.add(x-j+" "+(y+j));
						if (strResults=='E') break;
						strResults = CheckIntersectOfWins(mWinlist, strObject, mElement[x][y]);
					}
				}
			}
		}
		if(OCount>XCount)
			return ("Invalid Board Too Many Os");
		if(XCount-OCount>1)
			return ("Invalid Board Too Many Xs");
		if(strResults=='N')
			if (isProsess)
				return ("Game in Progress");
			else
				return ("Game is Draw");
		if((strResults == 'X' && XCount==OCount)||(strResults == 'O' && XCount==OCount+1))
			return "Invalid Board Winner did not make last move";
		if(strResults=='E')
			return "Invalid Board Error Winning Sequence";
		else return strResults + " Wins";
	}
	
	static char CheckIntersectOfWins(HashMap <String,String> mWinlist, ArrayList<String> strList, char player){
		if(!mWinlist.containsKey("intersect")){
			mWinlist.put("player", ""+player);
			mWinlist.put("intersect",null);
			for(String s: strList)
				mWinlist.put(s,null);
			return player;
		}else if(mWinlist.get("intersect")==null)
		{
			if(player==(mWinlist.get("player").charAt(0)))
				for(String s: strList){
					if(mWinlist.containsKey(s)){
						mWinlist.put("intersect",s);
						return player;
					}
				}
		}else {
			if(player==(mWinlist.get("player").charAt(0)))
				for(String s: strList)
					if(s.equalsIgnoreCase(mWinlist.get("intersect")))
						return player;
		}
		return 'E';
	}
}
