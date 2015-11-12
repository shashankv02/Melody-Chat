package com.shashank.MelodyChat.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniqueIdentifier {
	private static List<Integer> ids = new ArrayList<Integer>();
	private static final int RANGE = 1000;
	private static int index=0;
	static {
		for (int i=0; i<RANGE; i++) {
			ids.add(i);
		}
		//Collections.shuffle(ids);
	}
	
	public static int getIdentifier() {
		if(index > ids.size() -1) {
			index = 0;
		}
		return ids.get(index++);
	}
	private void UnqiueIdentifer() {
	}
	
}
