package com.javexpress.gwt.library.ui.data.jqplot;

import java.util.List;
import java.util.Map;

public interface IJqPlotGroupSeriesDataSupplier {
	
	public List<String> getGroups();

	public Map<String, List<Number>> populateData();

}