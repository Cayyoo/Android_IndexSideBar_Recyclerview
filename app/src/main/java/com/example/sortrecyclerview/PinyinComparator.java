package com.example.sortrecyclerview;

import java.util.Comparator;

/**
 * 比较器类，主要就是根据ASCII码来对数据进行比较排序
 */
public class PinyinComparator implements Comparator<SortModel> {

	@Override
	public int compare(SortModel o1, SortModel o2) {
		if (o1.getLetters().equals("@") || o2.getLetters().equals("#")) {
			return -1;
		} else if (o1.getLetters().equals("#") || o2.getLetters().equals("@")) {
			return 1;
		} else {
			return o1.getLetters().compareTo(o2.getLetters());
		}
	}

}
