package com.example.tic_tac_toe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class T3BoardAnalyzer {
	final static char X_SYMBOL = 'X';
	final static char O_SYMBOL = 'O';
	final static char D_SYMBOL = '.';
	final static char ERROR = 'E';
	final static char NO_WINS = 'N';
	
	static HashMap <String,String> mWinlist; //Saving the first potential winer's symbols and intersection if any
	static int mNumIntersect = 0; //The times of Intersection
	static char [][]mElement;  //Saving the data of the board

	//List the output states of the Board Analyzer
	final static class BoardState{	
		final static int WIN_STATE = 0;
		final static int ERROR_TYPE_MORE_WINS = 1;
		final static int ERROR_TYPE_LONG_JOIN = 2;
		final static int ERROR_TYPE_NUM = 3;
		final static int ERROR_TYPE_WIN_STATE = 4;
		final static int IN_PROGRESS_STATE = 5;
		final static int DRAW_STATE = 6;
	  }
	
	
	public static void main(String []args){
		String outStr = TestFromFile("input.txt");
		OutputToFile("output.txt",outStr);
	}
	/**
	* Create T3 Board and analyze the state
	* 
	* @param filePath: test cases in a file
	* @return
	*/
	public static String TestFromFile(String filePath){
		StringBuffer outPutBuffer = new StringBuffer();
		BufferedReader br = null;
		int  mNumCases=0;
		try{
			br= new BufferedReader(new FileReader(filePath));			
			String str = null;
			if ((str = br.readLine()) != null) mNumCases = Integer.parseInt(str); // get the number of test cases
			{
				for(int i=0; i<mNumCases; i++)
				{
					outPutBuffer.append("==========BOARD: " + (i+1) + "==============\n");
					mWinlist = new HashMap<String,String>();
					mNumIntersect = 0;
					int mBoardSize=0, mNumToWin=0;
					if ((str = br.readLine()) != null)
					{
						String tmp1 = str.substring(0, str.indexOf(" "));
						String tmp2 = str.substring(str.indexOf(" ")+1);
						mBoardSize = Integer.parseInt(tmp1); // get the board size
						mNumToWin = Integer.parseInt(tmp2); // get the number of win
						//Create T3 Board by test case
						mElement=new char[mBoardSize][mBoardSize];						
						for(int x=0; x<mBoardSize; x++)
							if ((str = br.readLine()) != null){
								outPutBuffer.append(str+"\n");
								for(int y=0; y<mBoardSize; y++)
									mElement[x][y] = str.charAt(y); 
							}
					}
					outPutBuffer.append("\n"+ mNumToWin + " in a row to win\n");
					outPutBuffer.append(BoardAnalyzer(mBoardSize, mNumToWin)+"\n");
					outPutBuffer.append("===================================\n\n");
				}
				br.close();
			}
		}catch (IOException e){
			e.printStackTrace();
		}
		finally{
			br = null;
		}
		return outPutBuffer.toString();
	}
	/**
	* Analyze Board case
	* 
	* @param mBoardSize mNumToWin
	* @return
	*/
	static String BoardAnalyzer(int mBoardSize, int mNumToWin){
		char strResults=NO_WINS;
		ArrayList<String> strObject = new ArrayList<String>();
		int i,j,count;
		boolean isProsess=false;
		int OCount=0,XCount=0;
		for(int x=0; x<mBoardSize; x++){
			for(int y=0; y<mBoardSize; y++){
				char c = mElement[x][y];
				if(c==D_SYMBOL){
					isProsess = true;
					continue;
				}
				else if(c==X_SYMBOL)
					XCount++;
				else
					OCount++;					
				//Searching to left
				for(i=0,count=0;(y-i>=0)&&(mElement[x][y-i]==c);i++){
					if(++count==mNumToWin){
						strObject.clear();
						for(j=0; j<mNumToWin; j++)
							strObject.add(x+" "+(y-j));
						strResults = CheckIntersectOfWins(strObject, c);
						if(strResults==ERROR)return OutputState(BoardState.ERROR_TYPE_MORE_WINS,c);
					}
					if(count>=2*mNumToWin)return OutputState(BoardState.ERROR_TYPE_LONG_JOIN,c);
				}
				//Searching to top
				for(i=0,count=0;(x-i>=0)&&(mElement[x-i][y]==c);i++){
					if(++count==mNumToWin)
					{
						strObject.clear();
						for(j=0; j<mNumToWin; j++)
							strObject.add(x-j+" "+y);
						strResults = CheckIntersectOfWins(strObject, c);
						if(strResults==ERROR)return OutputState(BoardState.ERROR_TYPE_MORE_WINS,c);
					}
					if(count>=2*mNumToWin)return OutputState(BoardState.ERROR_TYPE_LONG_JOIN,c);
				}
				//Searching to top-left
				for(i=0,count=0;(x-i>=0)&&(y-i>=0)&&(mElement[x-i][y-i]==c);i++){
					if(++count==mNumToWin)
					{
						strObject.clear();
						for(j=0; j<mNumToWin; j++)
							strObject.add(x-j+" "+(y-j));
						strResults = CheckIntersectOfWins(strObject, c);
						if(strResults==ERROR)return OutputState(BoardState.ERROR_TYPE_MORE_WINS,c);
					}
					if(count>=2*mNumToWin)return OutputState(BoardState.ERROR_TYPE_LONG_JOIN,c);
				}
				//Searching to top-right
				for(i=0,count=0;(x-i>=0)&&(y+i<mBoardSize)&&(mElement[x-i][y+i]==c);i++){
					if(++count==mNumToWin)
					{
						strObject.clear();
						for(j=0; j<mNumToWin; j++)
							strObject.add(x-j+" "+(y+j));
						strResults = CheckIntersectOfWins(strObject, c);
						if(strResults==ERROR)return OutputState(BoardState.ERROR_TYPE_MORE_WINS,c);
					}
					if(count>=2*mNumToWin)return OutputState(BoardState.ERROR_TYPE_LONG_JOIN,c);
				}
			}	
		}
		if(OCount>XCount)
			return OutputState(BoardState.ERROR_TYPE_NUM,O_SYMBOL);
		if(XCount-OCount>1)
			return OutputState(BoardState.ERROR_TYPE_NUM,X_SYMBOL);
		if(strResults==NO_WINS)
			if (isProsess)
				return OutputState(BoardState.IN_PROGRESS_STATE,NO_WINS);
			else
				return OutputState(BoardState.DRAW_STATE,NO_WINS);
		if((strResults == X_SYMBOL && XCount==OCount))
			return OutputState(BoardState.ERROR_TYPE_WIN_STATE, X_SYMBOL);
		if (strResults == O_SYMBOL && XCount==OCount+1)
			return OutputState(BoardState.ERROR_TYPE_WIN_STATE, O_SYMBOL);
		return OutputState(BoardState.WIN_STATE,strResults);
	}
	/**
	* Check Intersection
	* 
	* @param ArrayList: coordinates of the symbols may win
	* @return
	*/	
	static char CheckIntersectOfWins(ArrayList<String> strList, char player){
		if(mNumIntersect==0){
			mNumIntersect++;
			mWinlist.put("player", ""+player);
			mWinlist.put("intersect",null);
			for(String s: strList)
				mWinlist.put(s,null);
			return player;
		}else if(mNumIntersect==1)
		{
			mNumIntersect++;
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
		return ERROR;
	}
	
	/**
	* Processing the result of analyzer
	* 
	* @param int,char
	* @return
	*/
	static String OutputState(int type, char symbol)
	{
		String output=null;
		switch(type){
		case BoardState.WIN_STATE:
			output = String.format("%1$s Wins",symbol);
			break;
		case BoardState.ERROR_TYPE_LONG_JOIN:
			output = String.format("Invalid Board More Than Twice Win Number %1$ss Jointed",symbol);
			break;
		case BoardState.ERROR_TYPE_MORE_WINS:
			output = String.format("Invalid Board More Than 2 Wins");
			break;
		case BoardState.ERROR_TYPE_NUM:
			output = String.format("Invalid Board Too Many %1$ss",symbol);
			break;
		case BoardState.ERROR_TYPE_WIN_STATE:
			char c = (symbol==X_SYMBOL)?O_SYMBOL:X_SYMBOL;
			output = String.format("Invalid Board %1$s Wins But Last Input May Be %2$s",symbol,c);
			break;
		case BoardState.IN_PROGRESS_STATE:
			output = "IN PROGRESS";
			break;
		case BoardState.DRAW_STATE:
			output = "DRAW";
			break;
		default:
			break;
		}
		return output;
	}
	/**
	* Write output to a file
	* 
	* @param filePath: test cases in a file
	* @return
	*/ 
	public static void OutputToFile(String filePath, String output) {
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(filePath));
            bw.write(output);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	bw = null;
		}
    }	
}
