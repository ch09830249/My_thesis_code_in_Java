import java.util.Iterator;
import java.util.Set;

public class Find_E2 {
	ElementaryTable E1 = new ElementaryTable();	//記錄「 Erasable 1-itemsets 」
	ElementaryTable E2 = new ElementaryTable();	//記錄「 Erasable 2-itemsets 」

	//---------------------------------
	// 輸入參數設定
	double minGain =0 ;
	char[][] dbData;
	int[] transProfit;
	double totalGain=0d;	  

	//-------------------------------
	// 取得「處理結果」		
	int CK_Num=0;
	int EI_Num=0;
	int maxLength =0;

	//-------------------------------
	//取E2的使用的記憶體
	static	float freeMemory, totalMemory; 		//宣告未使用前記憶體狀況與使用後記憶體狀況，兩者相減就是使用記憶體量！
	static  int diffmemory_int;
	static	Runtime r = Runtime.getRuntime(); //這是宣告相關參數
	static	String usedStr;   			          //列印「使用記憶體量」字串		
	
	public Find_E2(char[][] dbData, int[] transProfit, double minGain, double totalGain, ElementaryTable E1)
	{
		try{
			//------------------------------------------
			// 設定「初始值」
			this.minGain = minGain;
			this.dbData = dbData;
			this.transProfit = transProfit;
			this.totalGain = totalGain;
			this.E1 = E1;				    		
			
			//System.out.println(" E1.size():"+E1.size());
							
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
	{				// get the set of E1
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
	// find the set of erasable 1-itemsets from db
	public void Find_E2()
	{		
		try{			
			//------------------------------------------
			//ElementaryTable C1 = new ElementaryTable();		//記錄「 Candidate 1-itemsets 」
			ElementaryTable C2 = new ElementaryTable();		    //記錄「 Candidate 2-itemsets 」			
			ElementaryTable Kept_Items = new ElementaryTable();	// 記錄 candidate itemsets存在哪些 erasable 1-item															

			//(1)首先透過E1，產生candidate erasable 2-itemsets
			//(2)計算candidate erasable 2-itemsets的gain值
			//(3)candidate erasable 2-itemsets的gain值在與threshold相比，得到E2            																	

			//------------------------------------------						
			// (1) generate all possible candidate 2-itemsets from E1
			char[] E1_arr = new char[E1.size()];
			int pos=0;
				
			Set set0 = E1.ElementSet();
 			Iterator iter0 = set0.iterator();
 			Element nx0;
 			while (iter0.hasNext())    
 			{
 	   		    nx0 = (Element)iter0.next();
				E1_arr[pos] = nx0.getKey()[0];
				pos++;
 			}				
 			// sort erasable 1-itemset in E1 (order by item id)
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
			}
			 */
			
			// generate all possible candidate 2-itemsets from E1
			//以及計算all possible candidate 2-itemsets的所有利潤值總和
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
									
			//------------------------------------------						
			Set set3 = C2.ElementSet();
 			Iterator iter3 = set3.iterator();
 			Element nx3;
     		while (iter3.hasNext())    
 			{
     			//nx1 = (Element)iter1.next();
				//E1_arr[pos] = nx1.getKey()[0];
				//pos++;
				Kept_Items.clear();
				nx3 = (Element)iter3.next();						    
					    
				//1.取出itemset有含哪些1-item，並放置Kept_Items中
				for (int k=0; k < nx3.getKey().length; k++)
   			    {
   			        char[] checked_itemset = new char[1];
   			        checked_itemset[0] = nx3.getKey()[k];
   			        Kept_Items.add(checked_itemset, 0);
   			        //System.out.println("Kept_Items.size()" + Kept_Items.size());
   			    }						    
					    
				//the gain value of each candidate 2-itemset					          					        
				//找產品中含A或B的所有利潤值總和
				int gain = 0;
				for(int a=0;a<dbData.length;a++)
				{															
					for(int b=0;b<dbData[a].length;b++)
					{							
						// check the item whether the item is a valid erasable item
						//只要檢查產品中有含A或B原料，則該產品利潤值則是itemset的gain值
						if(Kept_Items.ContainsKey(dbData[a][b]))
						{							  							  
							gain += transProfit[a];                            
							b = dbData[a].length; //主要使用精神是為了跳出for迴圈
							}							              
						}
				}
					    
				//System.out.print(" itemset: "+ (int)nx3.getKey()[0]+", "+(int)nx3.getKey()[1]);
				//System.out.println(" gain: "+ gain);
					    
				double eGain = ((double)gain/totalGain);
				/*System.out.print("itemset: ");
				for(int l=0;l<nx3.getKey().length-1;l++)
					System.out.print((int)nx3.getKey()[l]+", ");
				System.out.print((int)nx3.getKey()[nx3.getKey().length-1]);
                System.out.println(" gain: "+ gain);*/
											
				if(eGain<=minGain)
				{
					E2.add(nx3.getKey(), gain);
					}
				}
				   
     		//Print_E2(E2);

     		//System.out.println(" E2.size() = "+E2.size());						
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
