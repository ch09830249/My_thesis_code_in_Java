import java.io.*;
import java.util.*; //為了套用StringTokenizer和TreeMap的元件

class Sort_C{

	char[][] CItemsets;			// 排序前的二維陣列 Candidate itemsets
	char[][] Sort_CItemsets;	// 排序後的二維陣列 (用quicksort排序)
	
	
    QuickSort_Apr qs = new QuickSort_Apr();	//QuickSort   	
	
	//ElementaryTableCII C = new ElementaryTableCII();
	
	int p=0;

	public Sort_C(ElementaryTable C) {  //建立的同時設定大小基準
		try{
        	 CItemsets = new char[C.size()][];
        
         	 Set set33 = C.ElementSet();
         	 Iterator iter33 = set33.iterator();
         	 Element nx33;
         	 
         	 //先將資料抓到CItemsets這個二維陣列
         	 while (iter33.hasNext()) 
          	 {   
              	   nx33 = (Element)iter33.next();
              	   CItemsets[p] = new char[nx33.getKey().length];
                   System.arraycopy(nx33.getKey(),0,CItemsets[p],0,nx33.getKey().length);  //複製itemset
                      
                   p++;//記錄「交易記錄的指標」

           	 }
	         //將此階段的候選項目集資料，並進行順序排序動作！
	         Sort_CItemsets = qs.put(CItemsets);
	         
		}catch(Exception e){
			System.out.println(" Error about sorting the candidate itemsets: "+e.getMessage());	
		}	
    }
        
	public char[][] get_Sort_CItemsets() {     //取得排序後的Sorted Candidate Itemsets的集合
		return Sort_CItemsets;
	}        
   
}