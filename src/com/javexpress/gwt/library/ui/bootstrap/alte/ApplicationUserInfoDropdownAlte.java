package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationUserInfoDropdown;

public class ApplicationUserInfoDropdownAlte extends ApplicationUserInfoDropdown {

	private Element	icon;
	private Element	user;
	private Element	dropdown;

	public ApplicationUserInfoDropdownAlte(String id) {
		super(id, "user user-menu");

		dropdown = DOM.createElement("ul");
		dropdown.setClassName("dropdown");
		getElement().appendChild(dropdown);
		/*<li class="dropdown user user-menu">
		<a href="#" class="dropdown-toggle" data-toggle="dropdown">
		<img src="dist/img/user2-160x160.jpg" class="user-image" alt="User Image"/>
		<span class="hidden-xs">Alexander Pierce</span>
		</a>
		<ul class="dropdown-menu">
		<!-- User image -->
		<li class="user-header">
		<img src="dist/img/user2-160x160.jpg" class="img-circle" alt="User Image" />
		<p>
		  Alexander Pierce - Web Developer
		  <small>Member since Nov. 2012</small>
		</p>
		</li>
		<!-- Menu Body -->
		<li class="user-body">
		<div class="col-xs-4 text-center">
		  <a href="#">Followers</a>
		</div>
		<div class="col-xs-4 text-center">
		  <a href="#">Sales</a>
		</div>
		<div class="col-xs-4 text-center">
		  <a href="#">Friends</a>
		</div>
		</li>
		<!-- Menu Footer-->
		<li class="user-footer">
		<div class="pull-left">
		  <a href="#" class="btn btn-default btn-flat">Profile</a>
		</div>
		<div class="pull-right">
		  <a href="#" class="btn btn-default btn-flat">Sign out</a>
		</div>
		</li>
		</ul>
		</li>*/
	}

	@Override
	protected void fillAnchor() {
		icon = DOM.createElement("i");
		icon.setClassName(FaIcon.user.getCssClass());
		anchor.appendChild(icon);

		user = DOM.createSpan();
		user.setClassName("hidden-xs");
		anchor.appendChild(user);
	}

	@Override
	public void setUser(String value) {
		user.setInnerHTML("<small>" + ClientContext.nlsCommon.hosgeldiniz() + ",<br/>" + value + "</small>");
	}

	@Override
	protected void onUnload() {
		icon = null;
		user = null;
		dropdown = null;
		super.onUnload();
	}

}