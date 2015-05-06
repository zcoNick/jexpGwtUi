package com.javexpress.gwt.library.shared.model;

import java.util.List;
import java.util.Map;

import com.javexpress.common.model.item.MenuNode;
import com.javexpress.common.model.item.Result;
import com.javexpress.common.model.item.type.Pair;

public class LoginResult extends Result<JexpGwtUser> {

	private Map<Long, MenuNode>						menuNodes;
	private Map<Long, List<Pair<Long, String>>>		reports;
	private Map<Long, List<Pair<Long, String[]>>>	processes;
	private int										unreadMessages	= 0;

	public Map<Long, MenuNode> getMenuNodes() {
		return menuNodes;
	}

	public void setMenuNodes(final Map<Long, MenuNode> menuNodes) {
		this.menuNodes = menuNodes;
	}

	public Map<Long, List<Pair<Long, String>>> getReports() {
		return reports;
	}

	public void setReports(final Map<Long, List<Pair<Long, String>>> reports) {
		this.reports = reports;
	}

	public int getUnreadMessages() {
		return unreadMessages;
	}

	public void setUnreadMessages(final int unreadMessages) {
		this.unreadMessages = unreadMessages;
	}

	public Map<Long, List<Pair<Long, String[]>>> getProcesses() {
		return processes;
	}

	public void setProcesses(Map<Long, List<Pair<Long, String[]>>> processes) {
		this.processes = processes;
	}

}