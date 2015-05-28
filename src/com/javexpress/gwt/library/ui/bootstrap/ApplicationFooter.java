package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;

public class ApplicationFooter extends AbstractContainer {
	/*<footer class="main-footer">
	  <!-- To the right -->
	  <div class="pull-right hidden-xs">
	    Anything you want
	  </div>
	  <!-- Default to the left -->
	  <strong>Copyright &copy; 2015 <a href="#">Company</a>.</strong> All rights reserved.
	</footer>*/
	public ApplicationFooter(String className) {
		super(DOM.createDiv());
		getElement().setClassName(className);
	}

}