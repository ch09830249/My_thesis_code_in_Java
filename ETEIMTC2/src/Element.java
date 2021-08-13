class Element {
	final char[] key;		//元素的key值(字串)
	int value;				//元素的value值
	int hash;				//元素的hash值
	Element next;			//相同hash值的下一個元素
	
	//建構元
	Element(int h, char[] k, int v, Element n) { 
    		hash = h;
        	key = k;
        	value = v;
        	next = n;
	}

	public char[] getKey() { 
        	return key; 
	}

	public int getValue() {
        	return value;
	}

	public boolean equals(Element e) {  				//檢查elements之間key值是否相同(引數為element)
			char[] ch = e.getKey();
			return equals(ch);
	}
	
	public boolean equals(char[] ch) {						//檢查key值是否相同(引數為字串)
			if (this.key.length!=ch.length) return false;	//長度不同一定不一樣
			for(int i=0; i<key.length; i++)
				if (key[i]!=ch[i]) return false;			//若長度相同, 逐字元去檢查
			return true;
	}
	/*
	public recordRemoval(HashMap m) {
		
	}*/
}