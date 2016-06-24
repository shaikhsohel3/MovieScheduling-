import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class FinalMovieScheduling {

	//Storing each movie information 
	static int [] movieDuration = null;
	static int [][] Availability_Map = null;
	static int num,Movieseen=0;
	
	//To store generated schedule of movie shows
	static ArrayList<String> MovieOrder = new ArrayList<String>();
	
	//To track which movie is added to schedule 
	static ArrayList<Integer> MovieCovered = new ArrayList<Integer>();
	
	
	public static void main(String[] args) throws IOException {
	
		
		
		
		
		// TODO Auto-generated method stub

		/**********************************************************************************************
		  	Gathering Input Data:
		  	Variable list: Use
		  	1.num:Number of Distinct Movies 
		  	2.movieDuration[]: Show Duration for each movie in 1d array indexed with movie number
		  	3.show_count : Total number of shows of a movie in a day
		  	4.Start_time : Iteratively taking input for start time for each show of the movie
		  	
		  	
		  	Generating Knowledge Base :
		  	
		  	Availability_Map[Movie][Hour]:
		  	->Movie: index corresponds to a movie 
		  	->Hour: Corresponds to hour of day ranging from 0-23 (24 hour day format ) 
		  	
		  	
		  	Example :
	movie\hour->
	                0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23
 	 0			  	0  0  0  0  0  0  0  0  0  0  1  0  1  0  1  0  0  0  0  0  0  0  0  0
	 1 				0  0  0  0  0  0  0  0  0  0  0  1  1  0  0  1  0  0  0  0  0  0  0  0
	 2				0  0  0  0  0  0  0  0  1  0  0  0  1  0  0  0  1  0  0  0  0  0  0  0
		  	
		  	1-Movie available
		  	0-Movie not available
		  	
		 **********************************************************************************************/
		System.out.println("Enter Number Of Distinct Movies:");		
		@SuppressWarnings("resource")
		Scanner scn= new Scanner(System.in);
		num=scn.nextInt();
		if(num>0)
		{
			
			Availability_Map = new int[num][24];
			movieDuration = new int[num];
		}
		System.out.println("Enter the Duration of movie:");		
		for(int i=0;i<num;i++)
			{
				System.out.println("Movie "+(i+1));
				movieDuration[i]=scn.nextInt();
			}
				

		/***************************************************************************
		  GENERATING AVAILABILITY MAP. 
		 ***************************************************************************/

		for(int i=0;i<num;i++)
		{
			System.out.println("Enter the no. of show for Movie "+(i+1));
			int show_count =scn.nextInt();
			for(int j=0;j<show_count;j++)
			{ 
				System.out.println("Enter the start time of show" + j+"for Movie "+(i+1));
				int Start_time=scn.nextInt();
				Availability_Map[i][Start_time]=1;
			}
		}
		
		/***************************************************************************
		  DISPLAYING AVAILABILITY MAP. 
		 ***************************************************************************/
		for(int i=0;i<num;i++)
		{		for(int j=0;j<24;j++)
				{
					System.out.print(Availability_Map[i][j]+"");
				}
		System.out.println("");
		}
		
		
		System.out.println("Enter the start time of Akshay kumar");
		int startHour = scn.nextInt();
		
		while(startHour<24)
		{
			startHour=startHour+Movieseen;
			Movie_Select(startHour);
		}
		System.out.println(MovieOrder.toString());
		
	}

	/***************************************************************************
	  -: DEVICING SCHEDULE FROM KNOWLEDGE BASE (movieDuration[],Availability_Map[][]) :-
	  
	 ***************************************************************************/

	
	static void Movie_Select(int startHour) throws IOException
	{
		
		ArrayList<Integer> Available_Movie = new ArrayList<Integer>();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		
		int [] remaining_Show = new int[num];
		int minIndex = 0;
		
		
		for(int i=0;i<num;i++)
		{
			remaining_Show[i]=0;
		}
		
		for(int i=0;i<num;i++)
		{
			if (MovieCovered.contains(i)) {
				continue;
			} 
			else 
			{
				if(Availability_Map[i][startHour]==1)
				{
					Available_Movie.add(i);
					map.put(i, 0);
				}
					
			}		
		}
		
		
		

		/***************************************************************************
		  -: Decision Block:- If More Than One Movie At a Particular Hour :-
		  
		 ***************************************************************************/
		if(Available_Movie.size()>1)
		{
			
			Iterator<Integer> itr=Available_Movie.iterator();
			for(int j=startHour+1;j<24;j++)
			{
				while(itr.hasNext())
				{
					Object o=itr.next();
					Integer temp=(Integer)o;
					int tempint=temp.intValue();
					if(Availability_Map[tempint][j]==1)
						{remaining_Show[tempint]=remaining_Show[tempint]+1;map.put(tempint,remaining_Show[tempint]);}	
				}
			}

			int min=0,temp=0;

			for (int j = 0; j < num; j++){
				    if (remaining_Show[j]==1 ){
				   minIndex = j;
				   MovieCovered.add(minIndex);
				   MovieOrder.add("Movie "+(minIndex+1)+" Showtime :"+startHour+"-"+(startHour+movieDuration[minIndex]));
			
			 	   Movieseen=movieDuration[minIndex];
			 	   return;
				   
				  }
				}

				@SuppressWarnings("rawtypes")
				Iterator it = map.entrySet().iterator();

			    while (it.hasNext()) {
			        @SuppressWarnings("rawtypes")
					Map.Entry pair = (Map.Entry)it.next();
			        
			        temp=movieDuration[(int)pair.getKey()];
			        if(min<temp)
			        {
	        			min=temp;minIndex=(int)pair.getKey();
			        }
			        else
			        {
				        if(min==0)
				        {	min=temp;  	minIndex=(int)pair.getKey();}
			        }
			        
			    }
			MovieCovered.add(minIndex);
			MovieOrder.add("Movie "+(minIndex+1)+" Showtime :"+startHour+"-"+(startHour+movieDuration[minIndex]));
			
			Movieseen=movieDuration[minIndex];
			
		}
		/***************************************************************************
		 * If only one movie available at a particular hour then allocate movie show *
		 ***************************************************************************/
		else
		{
			if(Available_Movie.size()==1)
			{
				//only movie which is available
				MovieCovered.add(Available_Movie.get(0));
				MovieOrder.add("Movie "+(Available_Movie.get(0)+1)+" Showtime :"+startHour+"-"+(startHour+movieDuration[Available_Movie.get(0)]));
			
				Movieseen=movieDuration[Available_Movie.get(0)];
			}
		}
		
	}
}
