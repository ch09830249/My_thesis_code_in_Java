import java.lang.*;
import java.util.*;

public class Find_E1
{
	ElementaryTable E1 = new ElementaryTable();	          //記錄「 Temporal Erasable 1-itemsets 」	  
	
	//---------------------------------
	// 輸入參數設定	
	double minGain =0;
	char[][] dbData;
	int[] transProfit;
	int[] period;
	int[] itemStartPeriod;
	int[] periodtotalgain;
	double totalGain=0;
	int LSPall;
	
	//-------------------------------
	// 取得「處理結果」		
	int CK_Num=0;
	int EI_Num=0;
	int maxLength =0;      //最長的交易含有的item數量
	
	//-------------------------------
	//取E1的使用的記憶體
	static	float freeMemory, totalMemory;			
	static  int diffmemory_int;
	static	Runtime r = Runtime.getRuntime();		//這是宣告相關參數
	static	String usedStr;							//列印「使用記憶體量」字串
			
	public Find_E1(char[][] dbData, int[] transProfit, double minGain, int [] period, int [] itemStartPeriod, int [] periodtotalgain, double totalGain, int LSPall)
	{
		try
		{	
			//------------------------------------------
			// 設定「初始值」
			this.minGain = minGain;
			this.dbData = dbData;
			this.transProfit = transProfit;
			this.period = period;
			this.itemStartPeriod = itemStartPeriod;
			this.periodtotalgain = periodtotalgain;
			this.totalGain = totalGain;
			this.LSPall = LSPall;
							
			
			//------------------------------------------
			// 01. get the set of E1
		 	Find_E1();			
									
			}
			catch(Exception ex)
		    {
				System.out.println(" Error about finding Erasable 1-Itemsets information (in Find_E1.java):"+ex.getMessage());
		    }		
	  }
	
	
	  //------------------------------------------------------------------	
	  //------------------------------------------------------------------	
	  //回傳「相關結果資訊」
	  public ElementaryTable getE1()
	  {	// get the set of E1
		return E1;
	  }

	  public int getCK_Num()
	  {
		return CK_Num;	
	  }	

	  public int getMaxLength()
	  {
		return maxLength;
	  }

	  public int getE1_memory()
	  {
		return diffmemory_int;
	  }
		
    // find the set of erasable 1-itemsets from db
    public void Find_E1()
    {
		try
		{
			//------------------------------------------
			ElementaryTable C1 = new ElementaryTable();		//記錄「 Candidate 1-itemsets 」
													
		    //------------------------------------------
			// (1)Calculate the gain value of each candidate 1-itemset
			for(int i=0;i<dbData.length;i++)
			{	
				// get the maximum length of transactions
				if(maxLength < dbData[i].length)			
				{
					maxLength =dbData[i].length;
				}
				
				if(period[i]<LSPall)continue;        //LSPall時間內的產品
				
				for(int j=0;j<dbData[i].length;j++)
				{
					C1.add(dbData[i][j], transProfit[i]);      //第i筆交易的第j個item,加上此profit
				}	
				
			}
			CK_Num = C1.size();				
			
			//以上是算出所有candidate item的Gain
			
			//------------------------------------------						
			// 取得「Erasable 1-Itemsets  」							
			Set set1 = C1.ElementSet();
     		Iterator iter1 = set1.iterator();
			Element nx1;
			while (iter1.hasNext())    
			{
				nx1 = (Element)iter1.next();
				
				//取得「項目item名稱」
				//int item = (int)nx1.getKey()[0];                //取得1-itemset數值
				//int itemperiod = itemStartPeriod[item];			//取得其開始出現period
				

				//System.out.print(" itemset: "+ (int)nx1.getKey()[0]+", ");
                //System.out.println(" gain: "+ (int)nx1.getValue());
                //System.out.println(" temporal total gain: "+ totalGain);
                //System.out.println(" temporal gain ratio: "+ (double)nx1.getValue()/totalGain);
                //System.out.println();
				
				double eGain = ((double)nx1.getValue()/totalGain);
				if(eGain<=minGain)
				{
					E1.add(nx1.getKey(), nx1.getValue());
				}
				/*System.out.print(" itemset: "+ (int)nx1.getKey()[0]+", ");
                System.out.println(" gain: "+ (int)nx1.getValue());
                System.out.println(" temporal total gain: "+ totalGain);
                System.out.println(" temporal gain ratio: "+ eGain);
                System.out.println();*/
                    
			}
		 	r.gc();
			totalMemory = (float) r.totalMemory(); 
			freeMemory = (float) r.freeMemory(); 
			diffmemory_int = ((int) (totalMemory - freeMemory)/1024);
			//釋放空間!
			C1=null;
		}
		catch(Exception e)
		{
			System.out.println(" Error about finding the erasable 1-itemsets: "+e.getMessage());	
        }		
    }

	  //------------------------------------------------------------------	
	  //------------------------------------------------------------------			
	  //列印區
	  /*public void Print(ElementaryTable E1)
	  {
		    try{
			      System.out.println("----------------------------------");
			      System.out.println(" Print E1 ");
			      System.out.println();
			      //編號(第幾筆)
			      int tid=1;			
			
			      Set set1 = E1.ElementSet();
			      Iterator iter1 = set1.iterator();
			      Element nx1;
			
			      while(iter1.hasNext())
			      {				
				        nx1 = (Element)iter1.next();				
				        
				        System.out.println("------------------------------------");
				        System.out.print(" itemset = ");
				        for(int i=0;i<nx1.getKey().length;i++)
				        {
					          System.out.print( (int)nx1.getKey()[i] + " gain = "+nx1.getValue());
				        }
				        System.out.println();
				
				        System.out.println();
				        System.out.println("------------------------------------");
				        System.out.println();
				
				        tid++;
			      }			
			      System.out.println();
			      System.out.println("----------------------------------");
		    }catch(Exception ex)
		    {
			      System.out.println(" Error about printing E1  (in Find_E1.java):"+ex.getMessage());
		    }
	  }	*/
}