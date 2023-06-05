package com.mycompany.a4;
import java.util.ArrayList;

public class GameObjectCollection implements ICollection {

	// FIELDS*************************	// private inner class Iterator
	private ArrayList<GameObject> list = new ArrayList<GameObject>();
	
	class Iterator implements IIterator {
		private int cursor = 0;
		
		// check if there is another element after the cursor
		public boolean hasNext() {
	
			if (cursor < list.size()) {
				return true;
			}
			else {
				setCursor(0);
				return false;
			}
	       
		}
		

		// return the current element and increments the cursor
		public GameObject getNext() {
			GameObject temp = list.get(cursor);
			cursor++;
			return temp;
		}
		
		// returns the current element, but not does increment cursor
		public GameObject get() {
			return list.get(cursor);
		}
		
		public GameObject get(int i) {
			return list.get(i);
		}
		
		/*public void set(int i, GameObject go) {
			list.set(i, go);
		}
		
		public void update(GameObject g) {
			list.set(cursor - 1, g);
		}*/
		
		public int getCursor() {
			return cursor;
		}

		public void setCursor(int cursor) {
			this.cursor = cursor;
		}

		// returns the location of the base that has the corresponding base number
		public Point getBaseLocation(int b) {
			Point answer = new Point(0 , 0);
			// search the collection for a Base
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof Base) {
					Base tempB = (Base)list.get(i);
					
					// if the base has the target base number
					if (tempB.getSeqNum() == b) {
						// get its location
						answer = list.get(i).getLocation();
						break;
					}
				}
			}
			return answer;
		}
	}
	
	// METHODS************************
	// Add the given game object into the collection
	public void add(GameObject obj) {
		list.add(obj);
	}
	
	public void clear() {
		list.clear();
	}

	// Return the iterator of the collection
	public Iterator getIterator() {
		return new Iterator();
	}

	// return the number of elements
	public int size() {
		return list.size();
	}
	

}
