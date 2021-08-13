import java.util.Iterator;
import java.util.Set;

public class Find_E2 {
	ElementaryTable E1 = new ElementaryTable();	//記錄「 Temporal Erasable 1-itemsets 」
	ElementaryTable E2 = new ElementaryTable();	//記錄「 Temporal Erasable 2-itemsets 」

	//---------------------------------
	// 輸入參數設定
	double minGain =0 ;
	char[][] dbData;
	int[] transProfit;
	int[] period;
	int[] itemStartPeriod;
	int[] periodtotalgain;
	int maxitem;
	double totalGain=0d;
	int LSPall;

	//-------------------------------
	// 取得「處理結果」		
	int CK_Num=0;
	int EI_Num=0;
	int maxLength =0;

	//-------------------------------
	//取E2的使用的記憶體
	static	float freeMemory, totalMemory; 		//宣告未使用前記憶體狀況與使用後記憶體狀況，兩者相減就是使用記憶體量！
	static  int diffmemory_int;
	static	Runtime r = Runtime.getRuntime(); 	//這是宣告相關參數
	static	String usedStr;   			        //列印「使用記憶體量」字串		
	
	public Find_E2(char[][] dbData, int[] transProfit, double minGain, ElementaryTable E1, int [] period, int [] itemStartPeriod, int [] periodtotalgain, double totalGain, int LSPall)
	{
		try{
			//freeMemory = (float) r.freeMemory();//未使用前的記憶體！
			//------------------------------------------
			// 設定「初始值」
			this.minGain = minGain;
			this.dbData = dbData;
			this.transProfit = transProfit;
			this.period = period;
			this.itemStartPeriod = itemStartPeriod;
			this.periodtotalgain = periodtotalgain;
			this.E1 = E1;
			this.LSPall=LSPall;
			this.totalGain = totalGain;				    		
					
			//------------------------------------------
			// 01. get the set of E2
	 		Find_E2();
		
	    }
		catch(Exception ex)
		{
			System.out.println(" Error about finding Erasable 1-Itemsets information (in Find_E2.java):"+ex.getMessage());
	    }		
	}
	//------------------------------------------------------------------	
	//------------------------------------------------------------------	
	//回傳「相關結果資訊」
	public ElementaryTable getE2()
	{
		return E2;
	}
	
	public int getCK_Num()
	{
	    return CK_Num;	
	}
	
	public int getE2_memory()
	{
	    return diffmemory_int;
	}
	
	//------------------------------------------------------------------	
	//------------------------------------------------------------------
	// find the set of temporal erasable 2-itemsets from db
	public void Find_E2()
	{		
		try{			
			//------------------------------------------
			ElementaryTable C1 = new ElementaryTable();			//記錄「 Candidate 1-itemsets 」
			ElementaryTable C2 = new ElementaryTable();			//記錄「 Candidate 2-itemsets 」			
			ElementaryTable Kept_Items = new ElementaryTable();	//記錄 candidate itemsets存在哪些 erasable 1-item															

			//(1)首先透過排序過後的C1(依照ISP)，產生candidate 2-itemsets
			//(2)計算candidate 2-itemsets的 temporal gain ratio值
			//(3)temporal gain ratio值在與threshold相比，得到E2            																	

			//------------------------------------------						
			// (1) generate all possible candidate 2-itemsets
			
			//將E1放到E1_arr
			char[] E1_arr = new char[E1.size()];
			int pos=0;	
			Set set0 = E1.ElementSet();
 			Iterator iter0 = set0.iterator();
 			Element nx0;  			  //暫存element的參考
 			
 			while (iter0.hasNext())   //E1放所有E1的陣列
 			{
 	   		    nx0 = (Element)iter0.next();
				E1_arr[pos] = nx0.getKey()[0];   //取出item名稱
				pos++;
 			}
 						
 			// sort items in E1 (order by item id) 由小到大
			for(int i=0;i<E1_arr.length-1;i++)
			{
				for(int j=i+1;j<E1_arr.length;j++)
				{
					if((int)E1_arr[i]>(int)E1_arr[j])
					{
						char item = E1_arr[j];
						E1_arr[j] = E1_arr[i];
						E1_arr[i] = item;
					}
				}
			}
						
 			/*print檢查E1_arr和C1_arr
			for(int i=0;i<E1_arr.length;i++) {
				System.out.println((int)E1_arr[i]);
			}
			
			System.out.println();
			System.out.println();*/

			/*for(int i=0;i<C1_arr.length;i++) {   //印出candidate item怎麼排
				System.out.println((int)C1_arr[i]);
			}*/
			
			//在產品資料庫，檢查每筆產品內的原料是否有存在Kept_Items，有的話，該筆產品的利潤值為該itemset的gain值
			/*
			for(int m=0;m<dbData.length;m++)
			{						
				for(int n=0;n<dbData[m].length;n++)
				{
					// 確認是否存在「這個 item」，只要有一個既可，因此該產品利潤值既為itemset的gain值
					 if(Kept_Items.ContainsKey(dbData[m][n]))
					 {											                            
					 	C2.add(dbData[m][n]), transProfit[m]);
					 	n = dbData[m].length; //主要使用精神是為了跳出for迴圈
					 }							          
				}							    
			}*/
				
			//generate all possible candidate 2-itemsets from E1
			for(int i=0;i<E1_arr.length-1;i++)
			{
				for(int j=i+1;j<E1_arr.length;j++)
				{							
					char[] items2 = new char[2];
					items2[0] = E1_arr[i];
					items2[1] = E1_arr[j];										
					C2.add(items2, 0);								  			
				}
			}	
 			
			
			//System.out.println(" C2.size():"+C2.size());
			CK_Num = C2.size();
				  
			//Print_E2(C2);	
									
			//計算all possible candidate 2-itemsets的所有利潤值總和						
			Set set3 = C2.ElementSet();
 			Iterator iter3 = set3.iterator();
 			Element nx3;
			//int itemsetStartPeriod;							//記錄此2-itemset的起始時間
			
     		while (iter3.hasNext())    
 			{	//itemsetStartPeriod=0;
				Kept_Items.clear();
				nx3 = (Element)iter3.next();						    
					    
				//1.取出itemset有含哪些1-item，並放置Kept_Items中
				for (int k=0; k < nx3.getKey().length; k++)
   			    {	
   			        char[] checked_itemset = new char[1];
   			        checked_itemset[0] = nx3.getKey()[k];
   			        //int item = (int)checked_itemset[0];
   			        /*if(itemStartPeriod[item]>itemsetStartPeriod)    	//記錄此2-itemset的起始時間
   			        	itemsetStartPeriod = itemStartPeriod[item];*/		
   			        Kept_Items.add(checked_itemset, 0);    		 //引數為item字元和gain等於0
   			        //System.out.println("Kept_Items.size()" + Kept_Items.size());
   			    }
					    
				//the temporal gain ratio of each candidate 2-itemset					          					        
				//找產品中含A或B的所有利潤值總和
				int gain = 0;
				for(int a=0;a<dbData.length;a++)		//抓出一筆交易
				{	
					if(period[a]<LSPall)	//只抓出此itemset在LSPall中
						continue;//不在就跳過到下一筆交易
					for(int b=0;b<dbData[a].length;b++)	//交易的item逐一去看
					{	
						// check the item whether the item is a valid erasable item
						//只要檢查產品中有含A或B原料，則該產品利潤值則是itemset的gain值
						if(Kept_Items.ContainsKey(dbData[a][b]))
						{							  							  
							gain += transProfit[a]; //此交易加到此itemset的gain                           
							b = dbData[a].length;   //主要使用精神是為了跳出for迴圈, 找到item就跳出
						}							              
					}
				}
				
				
				
				/*this.totalGain=0;
				for(int i=itemsetStartPeriod;i<=periodtotalgain.length-1;i++) {	//加總此itemset出現期間其totalgain
					this.totalGain += periodtotalgain[i];
				}*/	

				
				double eGain = ((double)gain/totalGain);
				//印出2-itemset資訊
				/*System.out.print(" itemset: "+ (int)nx3.getKey()[0]+", "+(int)nx3.getKey()[1]);
				System.out.println(" gain: "+ gain);
				System.out.println(" temporal total gain: "+ totalGain);
				System.out.println(" temporal gain ratio: "+ eGain);
				System.out.println();*/
				if(eGain<=minGain)
				{	//排序大到小 (讓數字大的item放前面)
					int item1,item2;
					item1 = (int)nx3.getKey()[0];
					item2 = (int)nx3.getKey()[1];
					if(item1<item2) {
						char[] temp = new char[2];
						temp[0] = (char)item2;
						temp[1] = (char)item1;
						E2.add(temp, gain);
					}
					else
					E2.add(nx3.getKey(), gain);
				}
			}
     		r.gc();
     		totalMemory = (float) r.totalMemory();
     		freeMemory = (float) r.freeMemory();
     		diffmemory_int = ((int) (totalMemory - freeMemory)/1024);
     		//釋放空間!
     		C2=null;								
     		}
		catch(Exception e)
		{
			System.out.println(" Error about finding the erasable 2-itemsets: "+e.getMessage());	
	    }		
  }

  //------------------------------------------------------------------	
  //------------------------------------------------------------------			
  //列印區
  public void Print(ElementaryTable E1)
  {
	  try
	  {
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
			        System.out.println(" itemset = ");
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
  }	

  //------------------------------------------------------------------			
  //列印區 E2
  public void Print_E2(ElementaryTable E2)
  {				
	    try{
		      System.out.println("----------------------------------");
		      System.out.println(" Print E2 ");
		      System.out.println();
		      //編號(第幾筆)
		      int tid=1;			
		
		      Set set1 = E2.ElementSet();
		      Iterator iter1 = set1.iterator();
		      Element nx1;
		
		      while(iter1.hasNext())
		      {
		    	  nx1 = (Element)iter1.next();

		    	  System.out.println("------------------------------------");
		    	  System.out.print(" itemset = ");
			      for(int i=0;i<nx1.getKey().length;i++)
			      {
			    	  System.out.print((int)nx1.getKey()[i]);
			      }
			      System.out.println(" gain = "+nx1.getValue());
			      System.out.println();
			
			      System.out.println();
			      System.out.println("------------------------------------");
			      System.out.println();
			

			      tid++;
		     }			
		      System.out.println();
		      System.out.println("----------------------------------");
		     }
	    catch(Exception ex)
	    {
	    	System.out.println(" Error about printing E1  (in Find_E1.java):"+ex.getMessage());
	    }
  }	
}
