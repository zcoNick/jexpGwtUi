package com.javexpress.gwt.library.ui;

public enum FaIcon implements ICssIcon {

	tasks("tasks"), bell("bell"), envelope("envelope"), caret_down("caret-down"), paste(
			"paste"), random("random"),
	user("user"), leaf("leaf"), history("history"), home("home"), bookmark_o(
			"bookmark-o"), refresh("refresh"), plusCircle("plus-circle"), stethoscope(
			"stethoscope"), puzzlePiece("puzzle-piece"),
	pencil("pencil"), print("print"), filter("filter"), copy("copy"), trash(
			"trash"), check("check"), key("key"), lock("lock"), phone("phone"), email(
			"at"), Fax("fax"), dashboard("dashboard"), warning(
			"warning-sign"), info_sign("info-sign"), arrowRight("arrow-right"), arrowLeft(
			"arrow-left"), close("close"), gear("gear"), rss("rss"), eraser(
			"eraser"), plus("plus"), minus("minus"), calendar("calendar"), question(
			"question"), questionCircle("question-circle"), search("search"), play(
			"play"), playCircle("play-circle"), undo("undo"), empire("empire"),
	xml("file-code-o"), xls("file-excel-o"), pdf("file-pdf-o"), doc(
			"file-word-o"), magic("magic"), arrowDown("arrow-down"), circleThin(
			"circle-thin"), longArrowRight("long-arrow-right"),
	levelUp("level-up"), levelDown("level-down"), star("star"), circleDot(
			"circle-dot-o"), server("server"), fileTextOpen("file-text-o"), listAlt(
			"list-alt"), pencilSquareO("pencil-square-o"), list("list"), attach(
			"paperclip"), table("table"), clock("clock-o"), database(
			"database"), download("download"), barChart("bar-chart"), comment(
			"comment-o"), comments("comments-o"), briefcase("briefcase");

	private String	icon;

	private FaIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String getCssClass() {
		return "fa fa-" + icon;
	}

}