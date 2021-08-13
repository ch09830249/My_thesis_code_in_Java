import java.lang.*;
import java.math.BigDecimal;
import java.util.*;
import java.io.*;

public class UTETC2 
{
	//-------------------------------
    // 門檻值設定區
    static	double	minGain=0d;		//記錄「minimum erasable itemset mining threshold」
    //-------------------------------
    // 宣告 input data 相關參數	
    static	char[][] dbData;    				  // 存放「原始交易資料」
    static	int[] transProfit;					  // 記錄「每筆交易的 profit 值」	
    static  int[] period;
    static  int[][] lifespan;					  //紀錄item的起始和結束時間
    static  int[] periodtotalgain;
    static  int[] periodproductnum;
    static  int lastperiod;
    static 	int maxitem;
    static	double totalGain = 0;
    static String FilePath;
    // 記錄「全部產品的 profit 值總和」
                        
    //-------------------------------
    //記憶體所需參數區
    static	String usedStr;   			        //列印「使用記憶體量」字串
    static	float freeMemory, totalMemory; 		//宣告未使用前記憶體狀況與使用後記憶體狀況，兩者相減就是使用記憶體量！
    static	Runtime r = Runtime.getRuntime(); 	//這是宣告相關參數
	
	//-------------------------------
	// 取得「處理結果」		
	static int CK_Num=0;
	static int EI_Num=0;
	
	static int E1_Num=0;
	static int E2_Num=0;
	static int E_Num=0;
	
	static int NE1_Num=0;
	static int NE2_Num=0;
	
	static ElementaryTable E1 = new ElementaryTable();	//記錄「 High Maximum Upper-Bound Utility 1-itemsets 」
	static ElementaryTable E2 = new ElementaryTable();	//記錄「 High Maximum Upper-Bound Utility 2-itemsets 」
	//static ElementaryTable E3 = new ElementaryTable();	//記錄「 High Maximum Upper-Bound Utility 3-itemsets 」
	
	static int E1_memory = 0;
	static int E2_memory = 0;	
	static int E3_memory = 0;
	static int total_memory = 0;
	
	static String E1_items="";
	public static double getThreshold(double threshold)
	{            //設定minGain
		minGain=threshold;
		return minGain;
		
	}
	public static String getDatabase(String filepath)
	{
		FilePath=filepath;
		return FilePath;
	}
	
	public static void main(String[] args)
    {
		try
        {	//紀錄記憶體
			r.gc();	//先gc
			freeMemory = (float) r.freeMemory();
			totalMemory = (float) r.totalMemory();
			int diffmemory_int1 = ((int) (totalMemory - freeMemory)/1024);
			
			minGain=getThreshold(0.2);
			System.out.println("Temporal Erasable Itemset Mining Algorithm");
 			System.out.println("=====================================");
			System.out.println("minGain: "+ minGain);
			FilePath=getDatabase("TT_new");
			System.out.println("File: "+ FilePath + ".txt");

 			//System.out.println("-------------------------------------------------------------");
 			//System.out.println("		Original	Erasable Mining Algorithm ");     	
 			//System.out.println();
 			//System.out.println("");
            
            long t1 =System.currentTimeMillis(); 
	
 			//----------------------------------
 			// 01.
 			// 連結資料庫
 			ConnectionTextFile db = new ConnectionTextFile(FilePath);
				
			dbData = db.getdbData();
			transProfit = db.getTransProfit();
			period = db.getPeriod();
			periodtotalgain = db.getPeriodTotalGain();
			lifespan = db.getlifespan();
			lastperiod = db.getLastPeriod();
			totalGain = db.getTotalGain();
			totalGain = db.getTotalGain();
			maxitem = db.getmaxitem();
			periodproductnum = db.getperiodproductnum();
			
		    long t2 =System.currentTimeMillis();
		    System.out.println(" Used Memory (Before executing program): "+diffmemory_int1);
		    System.out.println("Time of connecting database: "+ Double.toString((t2-t1)/1000.0)+"s");//紀錄讀取資料庫擷取資訊所需時間
 			System.out.println("=====================================");
		    System.out.println();
			//2-phase Temporal Erasable Itemset Mining
			//收集每個period的erasable itemsets當作最後的candidate itemsets
			ElementaryTable allcandidate = new ElementaryTable();
			
			
			System.out.println("First phase: Find erasable itemsets in individual periods");
			System.out.println();
			for(int currentperiod= 0;currentperiod<=lastperiod;currentperiod++) {
				//currentperiod: 現在處理的period
				ElementaryTable C1 = new ElementaryTable();   //記錄所有在此period會出現的1-itemset(要看lifespan此item可不可能出現)
				ElementaryTable[] En = null;				  //紀錄暫存此period所有Erasable itemsets
				
				System.out.println("	*** Erasable Itemset Mining in "+ currentperiod+"-th period ***");
				
				//01.
				//記錄所有在此period會出現的1-itemset放入C1(看此period此否在生命週期內)  item[i][] 起始時間比較早且存在於此DB
				for(int i=0;i<lifespan.length;i++) {
					if(lifespan[i][0]<=currentperiod && lifespan[i][0]!=-1) //&& lifespan[i][1]>=currentperiod) //更改
						C1.add((char)i, 0);
				}
		
				//02.
				//抓出每個時期的子資料庫(subdbData和subtransProfit)(抓第currentperiod週期)
				char [][] subdbData = new char [periodproductnum[currentperiod]][];		//第幾個周期的子資料庫 此週期有periodproductnum[currentperiod]個product
				int [] subtransProfit = new int [periodproductnum[currentperiod]];
				int pos = 0;												//子資料庫從第0個開始存
				for(int h=0;h<dbData.length;h++) {
					if(period[h]==currentperiod) {
						subdbData[pos] = new char[dbData[h].length];
						subdbData[pos] = dbData[h];
						subtransProfit[pos] = transProfit[h];
						pos++;
					}	
				}
				/*for(int a=0;a<subdbData.length;a++) {
					for(int b=0;b<subdbData[a].length;b++) {
						int item = (int)subdbData[a][b];
						System.out.print(""+item+" ");
					}
					System.out.print(": ");
					System.out.print(subtransProfit[a]);
					System.out.println();
				}*/
				
				
				//03.
				//印出此period的資訊
				/*System.out.println("*****************************");
				System.out.println("Current period: "+currentperiod);
				System.out.println("The number of products in this period: "+subdbData.length);
				//System.out.println("subtransProfit.length: "+subtransProfit.length);
				System.out.println("The totalgain of this period: "+periodtotalgain[currentperiod]);
				System.out.println("*****************************");*/
	 		
			    //System.out.println(" time of connecting database: "+ Double.toString((t2-t1)/1000.0)+"s");    				
					
				//freeMemory = (float) r.totalMemory() - (float) r.freeMemory(); //未使用前的記憶體！ 				
				
	 			//----------------------------------
	 			// 02.
	 			// 計算「minimum erasable itemset mining threshold」  
	 			// 直接使用「百分比」   		
				  //System.out.println(" minGain: " + minGain);		
				
				//----------------------------------
	 			// 03.
	 			// get the set of E1 in this period
	 			// get Erasable 1-itemset
	 			//System.out.println("*** Temporal erasable 1-itemset in "+ currentperiod+"-th period ***");
	 			long t3 =System.currentTimeMillis(); 
				Find_E1  fe1 = new Find_E1(subdbData, subtransProfit, minGain, periodtotalgain[currentperiod], C1);
				E1 = fe1.getE1();                                      //取得 Erasable 1-itemsets
				//E1_memory = fe1.getE1_memory();						   //取得使用的記憶體使用量		
				CK_Num = fe1.getCK_Num();							   //取得Candidate 1-itemsets的數量
				EI_Num = fe1.getE1().size();						   //取得Erasable 1-itemsets的數量
				NE1_Num=CK_Num-EI_Num; // NE1=CE1-E1
				E1_Num=EI_Num;
				//System.out.println("E1 used memory: "+E1_memory);
				/*System.out.println("CK_Num ="+CK_Num);
				System.out.println("E1_Num ="+E1_Num);
				System.out.println("NE1_Num ="+NE1_Num);
				System.out.println("=======================================");*/
				//fe1.Print(E1);
				//System.out.println(EI_Num);
				long t4 =System.currentTimeMillis(); 
				//System.out.println(" Total time of getting Erasable 1-itemset: "+ Double.toString((t4-t3)/1000.0)+"s"); 
				//System.out.println(" Consumming total memory of getting E1 itemset (Create C1 => get E1=> delete C1)，共使用: "+ E1_memory + "K used");           
				//System.out.println("=======================================");
				if(E1.size()!=0)
				{
					//----------------------------------
					// 03.
					// get the set of E2
					// get Erasable 2-itemset
					//System.out.println("=======================================");
					//System.out.println("*** Temporal erasable 2-itemset in "+ currentperiod+"-th period ***");
					long t5 =System.currentTimeMillis(); 
					Find_E2  fe2 = new Find_E2(subdbData, subtransProfit, minGain, periodtotalgain[currentperiod], E1);
					E2 = fe2.getE2();
					//E2_memory = fe2.getE2_memory();   						//取得使用的記憶體使用量
					E2_Num=fe2.getE2().size();
					NE2_Num=((CK_Num*(CK_Num-1))/2)-E2_Num; //NE2 = CE2 - E2
					/*System.out.println("E2_Num ="+E2_Num);
					System.out.println("NE2_Num ="+NE2_Num);*/
					
					CK_Num = CK_Num + fe2.getCK_Num();
					EI_Num = EI_Num + fe2.getE2().size();
	
					//System.out.println("E2 used memory: "+E2_memory);
					//System.out.println(EI_Num);
					long t6 =System.currentTimeMillis(); 
					//System.out.println(" Total time of getting Erasable 2-itemset: "+ Double.toString((t6-t5)/1000.0)+"s");    
					//System.out.println(" Consumming total memory of getting E2 itemset (Create C2 => get E2=> delete C2)，共使用: "+ E2_memory + "K used");     
					//System.out.println("=======================================");
					
					//----------------------------------
					// 04.
					// get the set of E3
					// get Erasable 3-itemset
					/*
					System.out.println("=======================================");
					long t8 =System.currentTimeMillis(); 
					Find_E3  fe3 = new Find_E3(dbData, transProfit, minGain, totalGain, E1);
					E3 = fe3.getE3();
					E3_memory = fe3.getE3_memory();
					CK_Num = CK_Num + fe3.getCK_Num();
					EI_Num = EI_Num + fe3.getE3().size();
					System.out.println(EI_Num);
					long t9 =System.currentTimeMillis(); 
					//System.out.println(" Total time of getting Erasable 3-itemset: "+ Double.toString((t9-t8)/1000.0)+"s");    
					//System.out.println(" Consumming total memory of getting E3 itemset (Create C3 => get E3=> delete C3)，共使用: "+ E3_memory + "K used");      
					//System.out.println("=======================================");
					*/
					//System.out.println("*** Temporal erasable k-itemset (k>=3) in "+ currentperiod+"-th period ***");
					Find_ErasableItemsets efi = new Find_ErasableItemsets(subdbData, subtransProfit, minGain, periodtotalgain[currentperiod], E1, E2, fe1.getCK_Num());
					EI_Num = EI_Num + efi.getEINum();
					CK_Num = CK_Num + efi.getCKNum();
					//E3_memory = efi.getEK_memory();
					E_Num=efi.getEINum();
					En = efi.getEI();   				//將此period的所有階段的erasable itemsets存起來
					//System.out.println("EK used memory: "+E3_memory);
					//System.out.println("3以上的erasable itemset ="+E_Num);
					//CK_Num = efi.getCKNum();
					//EI_Num = efi.getEINum();
					//efi.getEI();
				
					//----------------------------------
					// 07.     			
					// 輸出「所有的 HAUI Itemsets 資訊」於文字檔裡！     			
					//Output_Info oi = new Output_Info(FilePath , minGain, efi.getEI(), totalGain);
					//E1_items=oi.getE1(efi.getEI());
				}

				
				//System.out.println("=======================================");
				
				//記錄此周期的erasable itemsets
				int number = 0;   //看這個period加入多少個itemset
				if(En!=null) {
					for(int i=0;i<En.length;i++)   //En[0]: erasable 1-itemset     En[1]: erasable 2-itemset......
					{
						if(En[i]==null)break;
						if(En[i].size()==0)continue;
						
						Set set1 = En[i].ElementSet();
		     			Iterator iter1 = set1.iterator();
		     			Element nx1;
		     			//一個個取出並放到allcandidate這個elementary table內
		     			while (iter1.hasNext())    
		     			{
		     	   			nx1 = (Element)iter1.next();
		     	   			allcandidate.add(nx1.getKey(), 0);
		     	   			number++;
		     	   			/*for(int j=0;j<nx1.getKey().length;j++){
								System.out.print((int)nx1.getKey()[j]+",");
							}*/	
						}//System.out.println();
					}
				}
				//System.out.println("Temporal Erasable Itemsets Mining  在記憶體te方面，共使用: "+total_memory); 
				//System.out.println();
				//System.out.println("加入幾個itemsets:" + number);
				//System.out.println("allcandidate已收集:" + allcandidate.size());
			}
			
 			System.out.println("=====================================");
			System.out.println("Second phase: Scan DB and calculate the temporal gain ratio");
			//Second phase: 將所有all candidate itemset依照生命週期去掃資料庫算其gain ratio
			
			r.gc();	//先gc
			freeMemory = (float) r.freeMemory();
			totalMemory = (float) r.totalMemory();
			total_memory = ((int) (totalMemory - freeMemory)/1024); 
			usedStr = ""+ total_memory + "K used";  
			
			CK_Num = allcandidate.size();
			ElementaryTable Kept_Items = new ElementaryTable();
			Set set3 = allcandidate.ElementSet();
 			Iterator iter3 = set3.iterator();
 			Element nx3;
			int itemsetStartPeriod;							//記錄此k-itemset的起始時間
			int itemsetLastPeriod;							//記錄此k-itemset的結束時間
     		while (iter3.hasNext())    
 			{	
     			itemsetStartPeriod=0;									//初始起始時間
   			    itemsetLastPeriod= period[period.length-1];				//初始結束時間(此資料庫最後一個period)
   			    Kept_Items.clear();
				nx3 = (Element)iter3.next();						    
					    
				//取出itemset含有哪些1-item，並放置Kept_Items中並抓出此itemset的生命週期
				for (int k=0; k < nx3.getKey().length; k++)
   			    {	
   			    	char[] checked_itemset = new char[1];
   			        checked_itemset[0] = nx3.getKey()[k];
   			        //找出itemset's lifespan
   			        int item = (int)checked_itemset[0];
   			        if(lifespan[item][0]>itemsetStartPeriod)    	//記錄此k-itemset的起始時間
			        	itemsetStartPeriod = lifespan[item][0];
   			        if(lifespan[item][1]<itemsetLastPeriod)    		//記錄此k-itemset的結束時間
   			        	itemsetLastPeriod = lifespan[item][1];
   			        Kept_Items.add(checked_itemset, 0);    		 //引數為item字元和gain等於0
   			        //System.out.print(""+item+", ");
   			    }
				/*System.out.println();
				System.out.println("起始時間"+itemsetStartPeriod);
				System.out.println("結束時間"+itemsetLastPeriod);
				System.out.println();*/
							
				
					    
				//the temporal gain ratio of each candidate k-itemset					          					        
				//找產品中含有itemset中的items並且此交易要在生命週期內
				int gain = 0;
				for(int a=0;a<dbData.length;a++)		//抓出一筆交易
				{	
					if(period[a]<itemsetStartPeriod)// || period[a]>itemsetLastPeriod)	//只抓出此itemset的生命週期中的交易 更改
						continue;//下一筆交易
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
				
				
				//算其生命週期內的分母值
				int totalGain=0;
				for(int i=itemsetStartPeriod;i<=lastperiod;i++){//i<=itemsetLastPeriod;i++) {	//加總此itemset出現期間其totalgain //更改
					totalGain += periodtotalgain[i];
				}
				//System.out.println("total: "+totalGain);
				
				double eGain = ((double)gain/totalGain);
				/*System.out.print(" itemset: ");
			    for(int l=0;l<nx3.getKey().length-1;l++)
					System.out.print((int)nx3.getKey()[l]+", ");
				System.out.print((int)nx3.getKey()[nx3.getKey().length-1]);
	            System.out.print(" gain: "+ gain);
	            System.out.println("	gain ratio: "+ eGain);*/
	             
				if(eGain>minGain)
				{
					allcandidate.remove(nx3.getKey());
				}
				else 
				{
					allcandidate.remove(nx3.getKey());
					allcandidate.add(nx3.getKey(), gain);
				}
			}
 			System.out.println("=====================================");
 			System.out.println();
     		
     		
     		//印出所有Temporal Erasable Itemset
     		EI_Num = allcandidate.size();
			ElementaryTable[] Finalresult = new ElementaryTable[1];
			Finalresult[0] = allcandidate;
     		Output_Info oi = new Output_Info(FilePath , minGain, Finalresult, totalGain, lifespan, periodtotalgain); 
	
			long t7 =System.currentTimeMillis(); 							

			//列印「探勘結果」
			System.out.println("-------------------------------------------------------------");
			System.out.println("-------------------------------------------------------------");
			System.out.println("-------------------------------------------------------------");
			System.out.println();
			System.out.println(" Temporal Erasable Itemsets Mining  在記憶體te方面，共使用: "+usedStr); 
			System.out.println(" minGain: "+minGain);     	
			System.out.println(" Filename of database:"+ FilePath);			
			System.out.println(" Number of CK from first phase : "+CK_Num);
			System.out.println(" Number of EI : "+EI_Num);
			System.out.println(" Execution time for all program : "+ Double.toString((t7-t1)/1000.0)+"s");  
			System.out.println();
			System.out.println("-------------------------------------------------------------");
			System.out.println("-------------------------------------------------------------");
			System.out.println("-------------------------------------------------------------");
		
        }
		catch(Exception ex)
        {
			System.out.println(" Error about main function (in OHUI.java):"+ex.getMessage());
		}
	}
}

