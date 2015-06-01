package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationMainContent;
import com.javexpress.gwt.library.ui.bootstrap.MainContentView;

public class ApplicationMainContentAlte extends ApplicationMainContent {
	/*<div class="content-wrapper">
	  <!-- Content Header (Page header) -->
	  <section class="content-header">
	    <h1>
	      Page Header
	      <small>Optional description</small>
	    </h1>
	    <ol class="breadcrumb">
	      <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
	      <li class="active">Here</li>
	    </ol>
	  </section>

	  <!-- Main content -->
	  <section class="content">

	    <!-- Your Page Content Here -->

	  </section><!-- /.content -->
	</div><!-- /.content-wrapper -->
	*/

	public ApplicationMainContentAlte() {
		super(DOM.createDiv());
		getElement().setClassName("content-wrapper");

		page = DOM.createDiv();
		page.setClassName("page-content");
		getElement().appendChild(page);
	}

	@Override
	public MainContentView createView(String id) {
		MainContentView mcv = new MainContentViewAlte(id);
		return mcv;
	}

}