
import java.lang.*;
import java.util.*;
import java.io.*;


public class Output_Info{

	int EI_num=0;
	String [] itemset;

	public Output_Info(String FilePath, double minGain, ElementaryTable[] EI, double totalGain){
	
		try{
			

			
			BufferedWriter bw = new BufferedWriter(new FileWriter(FilePath+"_EIs_Output"+minGain+".txt"));
			for(int i=0;i<EI.length;i++)
			{
				if(EI[i]==null)break;
				if(EI[i].size()==0)continue;
				
				Set set1 = EI[i].ElementSet();
     				Iterator iter1 = set1.iterator();
     				Element nx1;
     
     				while (iter1.hasNext())    
     				{
     	   				nx1 = (Element)iter1.next();
						
					String OutputInfo="";																			
					
					String itemset_name ="";
						
					// 01.  取得「項目集名稱」
					for(int j=0;j<nx1.getKey().length-1;j++){
						itemset_name +=(int)nx1.getKey()[j]+",";
						
					}	
					
					
					itemset_name+=(int)nx1.getKey()[nx1.getKey().length-1];
					// 02.  將「項目集名稱」放至「輸出字串內容」
					OutputInfo+= itemset_name;
					
		
					// 03.  取的「Actual Gain 值」					
					OutputInfo+= "____"+(nx1.getValue() /totalGain);
					

					// 04.  輸出「字串內容至文字檔」
					bw.write(OutputInfo); 
					bw.newLine(); 
						
						
					EI_num++;
				}

			}
			bw.close();
				
				
		}catch(Exception e){
			System.out.println(" Error about outputing information file (in Output_Info.java):"+e.getMessage());
		}
	}


	/*public Output_Info(String FilePath, double minGain, ElementaryTable EI1, double totalGain){
	
		try{
				BufferedWriter bw = new BufferedWriter(new FileWriter(FilePath+"_EIs_Output_"+minGain+".txt"));
		

				
				Set set1 = EI1.ElementSet();
     				Iterator iter1 = set1.iterator();
     				Element nx1;
     
     				while (iter1.hasNext())    
     				{
     	   				nx1 = (Element)iter1.next();
						
						String OutputInfo="";																			
					
						String itemset_name ="";
						
						// 01.  取得「項目集名稱」
						for(int j=0;j<nx1.getKey().length-1;j++){
							itemset_name +=(int)nx1.getKey()[j]+",";								
						}
						itemset_name+=(int)nx1.getKey()[nx1.getKey().length-1];
						
						//System.out.println(" itemset: "+itemset_name +"  ||  actual utility = "+nx1.getValue());
						
						// 02.  將「項目集名稱」放至「輸出字串內容」
						OutputInfo+= itemset_name;
		
		
						// 03.  取的「Actual Gain 值」					
						OutputInfo+= "_"+(nx1.getValue() /totalGain );


						// 04.  輸出「字串內容至文字檔」
						bw.write(OutputInfo); 
						bw.newLine(); 
						
						
						EI_num++;
				}


			bw.close();
				
				
		}catch(Exception e){
			System.out.println(" Error about outputing information file (in Output_Info.java):"+e.getMessage());
		}
	}*/



	public int getEINum(){
		return 	EI_num;
	}
	public String getE1(ElementaryTable[] EI)
	{
		Set set1 = EI[0].ElementSet();
		Iterator iter1 = set1.iterator();
		Element nx1;
		String test ="";
		while (iter1.hasNext())    
		{
			nx1 = (Element)iter1.next();
			
			String OutputInfo="";																			
		
			String itemset_name ="";
			
			// 01.  取得「項目集名稱」
			for(int j=0;j<nx1.getKey().length-1;j++)
			{
				itemset_name +=(int)nx1.getKey()[j]+",";
			}	
			itemset_name+=(int)nx1.getKey()[nx1.getKey().length-1];
			test=test+itemset_name+",";
			

		}
		return test;
	}
}