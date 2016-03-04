package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationNotificationDropdown;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;

public class ApplicationNotificationDropdownAlte extends
		ApplicationNotificationDropdown {

	public ApplicationNotificationDropdownAlte(String id, WContext styleName,
			ICssIcon iconClass) {
		super(id, styleName, iconClass, "dropdown-menu", "label");
		/*
		 * <li class="dropdown messages-menu"> <a href="#"
		 * class="dropdown-toggle" data-toggle="dropdown"> <i
		 * class="fa fa-envelope-o"></i> <span
		 * class="label label-success">4</span> </a> <ul class="dropdown-menu">
		 * <li class="header">You have 4 messages</li> <li> <!-- inner menu:
		 * contains the actual data --> <ul class="menu"> <li><!-- start message
		 * --> <a href="#"> <div class="pull-left"> <img
		 * src="dist/img/user2-160x160.jpg" class="img-circle"
		 * alt="User Image"/> </div> <h4> Sender Name <small><i
		 * class="fa fa-clock-o"></i> 5 mins</small> </h4> <p>Message
		 * Excerpt</p> </a> </li><!-- end message --> ... </ul> </li> <li
		 * class="footer"><a href="#">See All Messages</a></li> </ul> </li>
		 */
	}

	@Override
	public void setIcon(ICssIcon iconClass) {
		icon.setClassName(iconClass.getCssClass());
	}

	@Override
	public void setHeader(ICssIcon icon, String text) {
		if (header == null) {
			header = DOM.createElement("li");
			header.setClassName("header");
			dropdown.appendChild(header);
		}
		header.setInnerHTML(text);
	}

	@Override
	public void setFooter(ICssIcon icon, String text) {
		if (footer == null) {
			footer = DOM.createElement("li");
			footer.setClassName("footer");
			dropdown.appendChild(footer);
		}
		footer.setInnerHTML("<a href=\"#messages\">" + text + "</a>");
	}

}