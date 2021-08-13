import java.lang.*;
import java.util.*;


public class Find_ErasableItemsets
{
	  double minGain =0;
	  char[][] dbData;
	  int[] transProfit;
	  int[] period;
	  int[] itemStartPeriod;
	  int[] periodtotalgain;
	  double totalGain=0d;
	  ElementaryTable E1 = new ElementaryTable();	
	  ElementaryTable E2 = new ElementaryTable();	
	  
	  int maxLength =0;
	
	  ElementaryTable[] EI;		// the set of temporal erasable itemsets of each phase
	  int Current_Len = 0;		// the current itemset length
	  int CK_Num =0;
	  int EI_Num =0;
	  
	  
	  static float freeMemory, totalMemory;		//記憶體用量	
	  static  int diffmemory_int;
      static Runtime r = Runtime.getRuntime();		
	
	public Find_ErasableItemsets(char[][] dbData, int[] transProfit, double minGain,  double totalGain, ElementaryTable E1, ElementaryTable E2, int maxLength, int[] period, int[] itemStartPeriod, int[] periodtotalgain)
    {
	      try{			
				    //---------------------------
				    //宣告變數區
				    this.dbData=dbData;
				    this.transProfit = transProfit;
				    this.minGain = minGain;
					this.period = period;
					this.itemStartPeriod = itemStartPeriod;
					this.periodtotalgain = periodtotalgain;
				    this.totalGain = 0;
				    this.E1 = E1;
				    this.E2 = E2;
				    this.maxLength  = maxLength ;  //itemset最長長度
			
				    //Print_Parameters();
			
				    initializeEiTables();
			
				    EI[0]=E1;
				    EI[1]=E2;			
				
				    //System.out.println(" EI[0].size() = " + EI[0].size() );
				    //System.out.println(" EI[1].size() = " + EI[1].size() );
				
				    Current_Len = 3; //設定長度為 3
									
 				    if(EI[0].size()==0 || EI[1].size()==0)return;
				
				    EI[0].size();
				    EI[1].size();
					
				    //---------------------
				    //執行探勘程序
				    //System.out.println("*********************************");				
				    //System.out.println("*********************************");								
				    //System.out.println(" Find the TEIs of the "+Current_Len+"-th phase");								
				    //---------------------
				    //執行探勘程序
				    while(Current_Len<=maxLength)
				    {	  //System.out.println("		------------------------------");
				    	  System.out.println("		performing candidate length :"+Current_Len+"	");
				    	  //System.out.println(" 		Find the TEIs of the "+Current_Len+"-th phase");
				    	  //System.out.println("		------------------------------");
					      //System.out.println(" performing candidate length :"+Current_Len+"	");
				    	  //排序目的: 方便看前k-1個item是否相同
					      Sort_C Sort_CI = new Sort_C(EI[Current_Len-2]);//先進行排序動作！ 因為長度為current的itemset存在EI[Current_Len-1]中
		
					      // generate the new candidates (k+1)-itemsets
					      Gen_C Gen_AC = new Gen_C(Sort_CI.get_Sort_CItemsets(), EI[Current_Len-2], Current_Len, itemStartPeriod);					
					
					      // Save the new candidates (k+1)-itemsets in the EI tables
					      EI[Current_Len-1] = Gen_AC.get_CItemsets();
					      CK_Num+=EI[Current_Len-1].size();
					
					      //System.out.println(" Total number of CK: "+EI[Current_Len-1].size() );
					
					      // calculate the gain of each candidate (k+1)-itemset by their bitmap information
					      findGainInfo_and_findEI();
										
					      EI_Num +=EI[Current_Len-1].size();
					
					     // if the number of erasable itemsets in the current k-th phase is the value of 0, then stop the finding EI procedure.
					     if(EI[Current_Len-1].size()==0)
					     {
						       break;
					     }
					
					     // if the number of erasable itemsets in the current k-th phase is less than the length of the current k-itemset, then stop the finding EI procedure.
					     if(EI[Current_Len-1].size()<Current_Len)
					     {
						       break;
					     }				
					     Current_Len++;					
				    }
		    }catch(Exception e)
		    {
			      System.out.println(" Error about finding out the high average-utility itemsets (in Find_HAUI_Itemsets.java): "+e.getMessage());
		    }
    }

    //--------------------------------------------------
	  //--------------------------------------------------		
	  // 回傳 處理結果 
	
	  public ElementaryTable[] getEI(){	// 回傳「最後的 HAUI 資訊」(HMUPUI已移除未滿足門檻值的itemsets)
		    return EI;
	  }

	  public int getEINum(){
		    return EI_Num;
	  }

	  public int getCKNum(){
		    return CK_Num;
	  }
	  
	  public int getEK_memory()
	  {
		return diffmemory_int;
	  }
	  //--------------------------------------------------
	  //--------------------------------------------------
	  //--------------------------------------------------
	  //--------------------------------------------------

	  public void findGainInfo_and_findEI()
	  {		
		    try{							   
				    //(1)檢查EI[Current_Len-1]裡的itemsets是否存在產品資料庫dbData的每一筆產品，是的話取出gain值
				    //Example:EI[Current_Len-1] = {A,B}, {C,D},.....
				    //{A,B} => 檢查每筆產品是否涵蓋A or B，有的話，該比產品利潤值為該itemset{A,B}的gain值
				    //(2)延續(1)的做法，如下:
				    //1.取出itemset有含哪些1-item，並放置Kept_Items中
				    //2.在產品資料庫，檢查每筆產品是否有存在Kept_Items，有的話，該筆產品的利潤值為該itemset的gain值
				    //3.檢查每ㄧitemsets的gain值是否滿足threshold，是的話，為erasable itemset
				    
				    ElementaryTable Kept_Items = new ElementaryTable();	// 記錄 candidate itemsets存在哪些 erasable 1-item				    				    
				    int itemsetStartPeriod;
				    //System.out.println(" 刪除前 - "+  EI[Current_Len-1].size()+" 個 candidate itemsets");
													
	       			Set set = EI[Current_Len-1].ElementSet(); 
	       			Iterator iter = set.iterator();
	       			for (int j=0; iter.hasNext(); j++) 
	       			{   itemsetStartPeriod=0;    					
	       			    Kept_Items.clear();
	       			    Element nx=(Element)iter.next();
	       			    
	       			    //1.取出itemset有含哪些1-item，並放置Kept_Items中
	       			    for (int k=0; k < nx.getKey().length; k++)
	       			    {
	       			        char[] checked_itemset = new char[1];
	       			        checked_itemset[0] = nx.getKey()[k];
	       			        
	       			        int item = (int)checked_itemset[0];
	       			        if(itemStartPeriod[item]>itemsetStartPeriod)    	//記錄此candidate itemset的起始時間
	    			        	itemsetStartPeriod = itemStartPeriod[item];
	       			        
	       			        Kept_Items.add(checked_itemset, 0);
	       			        //System.out.println("Kept_Items.size()" + Kept_Items.size());
	       			    }
       			    
	       			    //2.在產品資料庫，檢查每筆產品內的原料是否有存在Kept_Items，有的話，該筆產品的利潤值為該itemset的gain值
					    for(int m=0;m<dbData.length;m++)
					    {		
								if(period[m]<itemsetStartPeriod)	//只抓出此itemset的生命週期中的交易
									continue;//下一筆交易
						        for(int n=0;n<dbData[m].length;n++)
						        {																
							          // 確認是否存在「這個 item」，只要有一個既可，因此該產品利潤值既為itemset的gain值
									  if(Kept_Items.ContainsKey(dbData[m][n]))
									  {											                            
										  EI[Current_Len-1].add(nx.getKey(), transProfit[m]);                            
										  n = dbData[m].length; //主要使用精神是為了跳出for迴圈
							          }							          
						        }							    
					    }
					     
					     //System.out.println(itemsetStartPeriod);
					     this.totalGain = 0;
					     for(int i=itemsetStartPeriod;i<=periodtotalgain.length-1;i++) {	//加總此itemset出現期間其totalgain
								this.totalGain += periodtotalgain[i];
						 }
					     
					     
					      
					     //3.檢查每ㄧitemsets的gain值是否滿足threshold，是的話，為temporal erasable itemset
					     double gain = (double)nx.getValue();
					     double eGain = ((double)nx.getValue()/totalGain);
					     
					     //印為candidate itemset資訊
					     /*System.out.print(" itemset: ");
					     for(int i=0; i<Current_Len;i++) {
					    	 if(i!=Current_Len-1)
					    		 System.out.print((int)nx.getKey()[i]+", ");
					    	 else
					    		 System.out.print((int)nx.getKey()[i]);
					     }
						 System.out.println(" gain: "+ gain);
						 System.out.println(" temporal total gain: "+ totalGain);
						 System.out.println(" temporal gain ratio: "+ eGain);
						 System.out.println();*/
  						  
  						  if(eGain<=minGain)
  						  {
  						  }
  						  else
  						  {
  							    EI[Current_Len-1].remove(nx.getKey());
					      }					      		
	       				}
	       				int temp_diffmemory_int;
	       				r.gc();
	       				totalMemory = (float) r.totalMemory(); 
	       				freeMemory = (float) r.freeMemory(); 
	       				temp_diffmemory_int = ((int) (totalMemory - freeMemory)/1024);
	       				if(temp_diffmemory_int>diffmemory_int)
	       					diffmemory_int = temp_diffmemory_int;
				    //System.out.println(" 刪除後 - "+  EI[Current_Len-1].size()+" 個 candidate itemsets");				
		    }
		    catch(Exception e)	
		    {
			      System.out.println(" Error about finding the gain value of each candidate (in Find_ErasableItemsets.java): "+e.getMessage());
		    }
	  }

	  public void initializeEiTables()
	  {		
		    long t1 = System.currentTimeMillis();	
			EI = new ElementaryTable[maxLength];	//記錄「每階段的 high transaction itemsets」
			long t2 = System.currentTimeMillis();	
		    //System.out.println(" time of initialling hashtables :"+Double.toString((t2-t1)/1000.0)+"s");
	  }
	
	  /*public void Print_Parameters()
	  {		
		  try{
			  
			  System.out.println("---------------------------------");
			  System.out.println(" dbData.length: "+dbData.length);
			  System.out.println(" transProfit.length: "+transProfit.length);
			  System.out.println(" minGain: "+minGain);
				    System.out.println(" totalGain: "+ totalGain);
				    System.out.println(" E1.size(): "+E1.size());
				    System.out.println(" E2.size(): "+E2.size());				
				    System.out.println(" maxLength: "+maxLength);
				    System.out.println("---------------------------------");
		    }catch(Exception e)	
		    {
			      System.out.println(" Error about printing the parameters (in Find_ErasableItemsets.java): "+e.getMessage());
		    }
	  }*/
}