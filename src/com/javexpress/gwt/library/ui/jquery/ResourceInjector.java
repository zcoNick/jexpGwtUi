package com.javexpress.gwt.library.ui.jquery;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.javexpress.gwt.library.ui.BaseResourceInjector;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.container.dashboard.Dashboard;
import com.javexpress.gwt.library.ui.container.panel.SplitPanel;
import com.javexpress.gwt.library.ui.data.jqgrid.JqGrid;
import com.javexpress.gwt.library.ui.data.jqplot.BarChart;
import com.javexpress.gwt.library.ui.data.jqplot.PieChart;
import com.javexpress.gwt.library.ui.data.schedule.Schedule;
import com.javexpress.gwt.library.ui.form.decimalbox.DecimalBox;
import com.javexpress.gwt.library.ui.form.maskedit.MaskEditBox;
import com.javexpress.gwt.library.ui.form.textbox.CKEditor;
import com.javexpress.gwt.library.ui.form.textbox.CodeEditor;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;
import com.javexpress.gwt.library.ui.menu.MenuBar;
import com.javexpress.gwt.library.ui.webcam.WebCam;

@Deprecated
public class ResourceInjector extends BaseResourceInjector {

	public static enum JqTheme {
		aristo(false), cupertino(true), sunny(false), flick(false), start(false),
		south_street(false), ui_lightness(false), ui_darkness(false), humanity(
				false),
		eggplant(false), smoothness(false), vader(false), redmond(false),
		trontastic(false), absolution(true), afternoon(true);

		boolean	invert;

		JqTheme(final boolean invert) {
			this.invert = invert;
		}

		public static JqTheme parse(final String theme) {
			if (JsUtil.isEmpty(theme))
				return JqTheme.afternoon;
			return JqTheme.valueOf(theme.replaceAll("-", "_"));
		}

		public boolean isInverted() {
			return invert;
		}
	}

	public static JqTheme	theme;

	@Override
	public void injectCore(JsonMap requireConfig, Command onload) {
		//injectStyleSheet("http://fonts.googleapis.com/css?family=Noto+Sans");
		List<String> styleSheets = new ArrayList<String>();
		String ptheme = Window.Location.getParameter("theme");
		ResourceInjector.theme = JqTheme.parse(ptheme);
		styleSheets.add("themes/" + theme.toString() + "/javexpress-gwt-library.ui.css");
		styleSheets.add("scripts/gritter/jquery.gritter.css");
		styleSheets.add("JexpGwtLibraryBase.css");

		List<String> javaScripts = new ArrayList<String>();
		javaScripts.add("scripts/jquery-1.11.3.min.js");
		javaScripts.add("scripts/jquery-ui-1.11.4.min.js");
		javaScripts.add("scripts/gritter/jquery.gritter.min.js");
		javaScripts.add("scripts/javexpress/jexpUICore-0.1.js");
		javaScripts.add("scripts/javexpress/jexpLocalizationStringHelper-0.1.js");
		injectLibrary("JexpUI Core", styleSheets, javaScripts, onload);
	}

	@Override
	public void injectUI(String applicationCss, Command onload) throws Exception {
		WidgetBundles wb = new WidgetBundles("JexpUI Extra");

		wb.addStyleSheet("themes/common/jquery-silk-icons.css");
		wb.addJavaScript("scripts/datetimepicker/jquery.ui.datepicker-" + LocaleInfo.getCurrentLocale().getLocaleName() + ".min.js");
		wb.addJavaScript("scripts/datetimepicker/jquery-ui-timepicker-1.3.js");
		MenuBar.fillResources(wb);
		Dashboard.fillResources(wb);
		JqGrid.fillResources(wb);
		MaskEditBox.fillResources(wb);
		DecimalBox.fillResources(wb);
		//-END User Interface

		//User Interface Extra
		BarChart.fillResources(wb);
		PieChart.fillResources(wb);
		Schedule.fillResources(wb);
		//-END User Interface Extra

		//AWESOME
		SplitPanel.fillResources(wb);
		CKEditor.fillResources(wb);
		CodeEditor.fillResources(wb);
		//Keyboard.fillResources(styleSheets, javaScripts);
		//TerminalConsole.fillResources(styleSheets, javaScripts);
		WebCam.fillResources(wb);
		//-END AWESOME

		//APPLICATION
		wb.addStyleSheet("scripts/javexpress/JexpGwtLibraryUI.css");
		wb.addJavaScript(applicationCss);
		//-END APPLICATION

		injectLibrary(wb, onload);
	}

	@Override
	public String getSkinName() {
		return null;
	}

	@Override
	public void applyIconInputGroupStyles(Element element, Element input, Element icon, ICssIcon iconClass) {
	}

	@Override
	public void applyIconStyles(Element iconSpan, ICssIcon iconClass) {
	}

}