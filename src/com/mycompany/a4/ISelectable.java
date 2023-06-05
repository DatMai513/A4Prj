package com.mycompany.a4;

public interface ISelectable {
	
		// a way to mark an object as “selected” or not
		public void setSelected(boolean b);
		// a way to test whether an object is selected
		public boolean isSelected();
		// a way to determine if a pointer is “in” an object
		// pPtrRelPrnt is pointer position relative to the parent origin
		public boolean contains(float x, float y, int absX, int absY);
		// a way to “draw” the object that knows about drawing
		// different ways depending on “isSelected”
}
