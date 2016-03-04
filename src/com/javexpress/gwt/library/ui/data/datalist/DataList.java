package com.javexpress.gwt.library.ui.data.datalist;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style.Clear;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainerFocusable;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class DataList extends AbstractContainerFocusable {

	private Element			categoryListDiv;
	private Element			tabBoxDiv;
	private Element			tabBoxUl;
	private Element			tabBoxFilter;
	private Element			selecterDiv;
	private SelectElement	filterSelect;
	private Element			scrollerContentOfSortOptions;
	private Element			listingFilterDiv;

	public DataList(Widget parent, String id) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, getElement(), WidgetConst.DATALIST_PREFIX, id);
		addStyleName("jexpDataList");
		categoryListDiv = DOM.createDiv();
		categoryListDiv.setClassName("category-list");
		getElement().appendChild(categoryListDiv);

		tabBoxDiv = DOM.createDiv();
		tabBoxDiv.setClassName("tab-box");
		categoryListDiv.appendChild(tabBoxDiv);

		tabBoxUl = DOM.createElement("ul");
		tabBoxUl.setClassName("nav nav-tabs add-tabs");
		tabBoxUl.setAttribute("role", "tablist");
		tabBoxDiv.appendChild(tabBoxUl);

		tabBoxFilter = DOM.createDiv();
		tabBoxFilter.setClassName("tab-filter");
		tabBoxDiv.appendChild(tabBoxFilter);

		selecterDiv = DOM.createDiv();
		selecterDiv.setClassName("selecter select-short-by closed");
		tabBoxFilter.appendChild(selecterDiv);

		filterSelect = DOM.createSelect().cast();
		filterSelect.setClassName("selectpicker selecter-element");
		filterSelect.setAttribute("data-style", "btn-select");
		filterSelect.setAttribute("data-width", "auto");
		filterSelect.setTabIndex(-1);
		selecterDiv.appendChild(filterSelect);

		Element sortBySpan = DOM.createSpan();
		sortBySpan.setClassName("selecter-selected");
		selecterDiv.appendChild(sortBySpan);

		Element selecterOptionsScroller = DOM.createDiv();
		selecterOptionsScroller.setClassName("selecter-options scroller");
		selecterOptionsScroller.getStyle().setDisplay(Display.NONE);
		selecterDiv.appendChild(selecterOptionsScroller);

		Element scrollerBar = DOM.createDiv();
		scrollerBar.setClassName("scroller-bar");
		scrollerBar.getStyle().setHeight(113, Unit.PX);
		selecterOptionsScroller.appendChild(scrollerBar);
		scrollerContentOfSortOptions = DOM.createDiv();
		scrollerContentOfSortOptions.setClassName("scroller-content");
		selecterOptionsScroller.appendChild(scrollerContentOfSortOptions);

		addSortOption("DEF", "Sort By", true);
		addSortOption("PLTH", "Price: Low to High", false);
		addSortOption("PHTL", "Price: High to Low", false);

		listingFilterDiv = DOM.createDiv();
		listingFilterDiv.setClassName("listing-filter");
		categoryListDiv.appendChild(listingFilterDiv);

		Element listingViewDiv = DOM.createDiv();
		listingViewDiv.setClassName("pull-right col-xs-6 text-right listing-view-action");
		listingFilterDiv.appendChild(listingViewDiv);
		addViewOption(listingViewDiv, "list-view", "icon-th");
		addViewOption(listingViewDiv, "compact-view", "icon-th-list");
		addViewOption(listingViewDiv, "grid-view-active", "icon-th-large");
		Element listingViewClear = DOM.createDiv();
		listingViewClear.getStyle().setClear(Clear.BOTH);
		listingFilterDiv.appendChild(listingViewClear);

	}

	private void addViewOption(Element listingViewDiv, String type, String icon) {
		Element span = DOM.createSpan();
		span.setClassName(type);
		span.setInnerHTML("<i class=\"" + icon + "\"></i>");
		listingViewDiv.appendChild(span);
	}

	private void addSortOption(String value, String label, boolean selected) {
		Element span = DOM.createSpan();
		span.setClassName("selecter-item");
		if (selected)
			span.addClassName("selected");
		span.setAttribute("data-value", value);
		span.setInnerHTML(label);
		scrollerContentOfSortOptions.appendChild(span);
	}

}
/*
http://templatecycle.com/demo/bootclassified-v1.2/category.html#
<div class="category-list"> = categoryListDiv
	<div class="tab-box "> = tabBoxDiv
		<ul class="nav nav-tabs add-tabs" id="ajaxTabs" role="tablist"> = tabBoxUl
			<li class="active"><a href="#allAds" data-url="ajax/1.html" role="tab" data-toggle="tab" aria-expanded="true">All Ads <span class="badge">228,705</span></a></li>
			<li class=""><a href="#businessAds" data-url="ajax/2.html" role="tab" data-toggle="tab" aria-expanded="false">Business <span class="badge">22,805</span></a></li>
			<li class=""><a href="#personalAds" data-url="ajax/3.html" role="tab" data-toggle="tab" aria-expanded="false">Personal <span class="badge">18,705</span></a></li>
		</ul>
		<div class="tab-filter"> = tabBoxFilter
			<div class="selecter select-short-by closed" tabindex="0"> = selecterDiv
				<select class="selectpicker selecter-element" data-style="btn-select" data-width="auto" tabindex="-1"> = filterSelect
					<option value="Short by">Short by</option>
					<option value="Price: Low to High">Price: Low to High</option>
					<option value="Price: High to Low">Price: High to Low</option>
				</select>
				<span class="selecter-selected">Sort by</span>  = sortBySpan
				<div class="selecter-options scroller" style="display: none;"> = selecterOptionsScroller
					<div class="scroller-bar" style="height: 113px;"> = scrollerBar
						<div class="scroller-track" style="height: 113px; margin-bottom: 0px; margin-top: 0px;">
							<div class="scroller-handle" style="height: 113px; top: 0px;"></div>
						</div>
					</div>
					<div class="scroller-content"> = scrollerContentOfSortOptions
						<span class="selecter-item selected" data-value="Short by">Short by</span>
						<span class="selecter-item" data-value="Price: Low to High">Price: Low to High</span>
						<span class="selecter-item" data-value="Price: High to Low">Price: High to Low</span>
					</div>
				</div>
			</div>
		</div>
	</div>
 
	<div class="listing-filter"> = listingFilterDiv
		<!--div class="pull-left col-xs-6">
			<div class="breadcrumb-list">
				<a href="#" class="current"> <span>All ads</span></a> in New York 
					<a href="#selectRegion" id="dropdownMenu1" data-toggle="modal"> 
						<span class="caret"></span> 
					</a>
			</div>
		</div-->
		<div class="pull-right col-xs-6 text-right listing-view-action"> 
			<span class="list-view">
				<i class="  icon-th"></i>
			</span> 
			<span class="compact-view">
				<i class=" icon-th-list  "></i>
			</span>
			<span class="grid-view active">
				<i class=" icon-th-large "></i>
			</span> 
		</div>
		<div style="clear:both"></div>
	</div>
 
<div class="adds-wrapper">
	<div class="tab-content">
		<div class="tab-pane active" id="allAds">
			<div class="item-list make-grid" style="height: 373px;">
				<div class="cornerRibbons topAds">
					<a href="#"> Top Ads</a>
				</div>
				<div class="col-sm-2 no-padding photobox">
					<div class="add-image">
						<span class="photo-count">
							<i class="fa fa-camera"></i> 2
						</span>
						<a href="ads-details.html">
							<img class="thumbnail no-margin" src="images/item/tp/Image00015.jpg" alt="img">
						</a>
					</div>
				</div>
 
				<div class="add-desc-box col-sm-7">
					<div class="add-details">
						<h5 class="add-title"> <a href="ads-details.html">Brand New Samsung Phones </a> </h5>
						<span class="info-row"> 
							<span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> 
							<span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> 
						</span> 
					</div>
				</div>
 
				<div class="col-sm-3 text-right  price-box">
					<h2 class="item-price"> $ 320 </h2>
					<a class="btn btn-danger  btn-sm make-favorite"> <i class="fa fa-certificate"></i> <span>Top Ads</span> </a> 
					<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> 
				</div>
			</div>
 
<div class="item-list make-grid" style="height: 373px;">
<div class="cornerRibbons featuredAds">
<a href="#"> Featured Ads</a>
</div>
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/tp/Image00008.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html">
Sony Xperia dual sim 100% brand new </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 250 </h2>
<a class="btn btn-danger  btn-sm make-favorite"> <i class="fa fa-certificate"></i> <span>Featured Ads</span> </a> <a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
<div class="item-list make-grid" style="height: 373px;">
<div class="cornerRibbons urgentAds">
<a href="#"> Urgent</a>
</div>
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/tp/Image00014.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> Samsung Galaxy S Dous (Brand New/ Intact Box) With 1year Warranty </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 230</h2>
<a class="btn btn-danger  btn-sm make-favorite"> <i class="fa fa-certificate"></i> <span>Urgent</span> </a> <a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
<div class="item-list make-grid" style="height: 373px;">
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/tp/Image00003.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> MSI GE70 Apache Pro-061 17.3" Core i5-4200H/8GB DDR3/NV GTX860M Gaming Laptop </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 400 </h2>
<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
<div class="item-list make-grid" style="height: 340px;">
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/tp/Image00022.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> Apple iPod touch 16 GB 3rd Generation </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 150 </h2>
<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
<div class="item-list make-grid" style="height: 340px;">
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/FreeGreatPicture.com-46405-google-drops-price-of-nexus-4-smartphone.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> Google drops Nexus 4 by $100, offers 15 day price protection refund </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 150 </h2>
<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
<div class="item-list make-grid" style="height: 340px;">
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/FreeGreatPicture.com-46404-google-drops-nexus-4-by-100-offers-15-day-price-protection-refund.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> Google drops Nexus 4 </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 150 </h2>
<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
  </div>
<div class="tab-pane" id="businessAds"> <div class="item-list make-grid" style="height: 340px;">
<div class="cornerRibbons featuredAds">
<a href="#"> Featured Ads</a>
</div>
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/FreeGreatPicture.com-46405-google-drops-price-of-nexus-4-smartphone.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> Google drops Nexus 4 by $100, offers 15 day price protection refund </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 150 </h2>
<a class="btn btn-danger  btn-sm make-favorite"> <i class="fa fa-certificate"></i> <span>Featured Ads</span> </a> <a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
<div class="item-list make-grid" style="height: 353px;">
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/tp/Image00015.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html">
Brand New Samsung Phones </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 320 </h2>
<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
<div class="item-list make-grid" style="height: 353px;">
<div class="cornerRibbons urgentAds">
<a href="#"> Urgent</a>
</div>
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/FreeGreatPicture.com-46404-google-drops-nexus-4-by-100-offers-15-day-price-protection-refund.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> Google drops Nexus 4 </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 150 </h2>
<a class="btn btn-danger  btn-sm make-favorite"> <i class="fa fa-certificate"></i> <span>Urgent</span> </a> <a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
<div class="item-list make-grid" style="height: 353px;">
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/tp/Image00008.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html">
Sony Xperia dual sim 100% brand new </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 250 </h2>
<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
<div class="item-list make-grid" style="height: 353px;">
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/tp/Image00014.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> Samsung Galaxy S Dous (Brand New/ Intact Box) With 1year Warranty </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 230</h2>
<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
<div class="item-list make-grid" style="height: 373px;">
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/tp/Image00003.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> MSI GE70 Apache Pro-061 17.3" Core i5-4200H/8GB DDR3/NV GTX860M Gaming Laptop </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 400 </h2>
<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
<div class="item-list make-grid" style="height: 373px;">
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/tp/Image00022.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> Apple iPod touch 16 GB 3rd Generation </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 150 </h2>
<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
  </div>
<div class="tab-pane" id="personalAds">
<div class="item-list make-grid" style="height: 373px;">
<div class="cornerRibbons topAds">
<a href="#"> Top Ads</a>
</div>
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/tp/Image00015.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html">
Brand New Samsung Phones </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 320 </h2>
<a class="btn btn-danger  btn-sm make-favorite"> <i class="fa fa-certificate"></i> <span>Top Ads</span> </a> <a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
<div class="item-list make-grid" style="height: 373px;">
<div class="cornerRibbons featuredAds">
<a href="#"> Featured Ads</a>
</div>
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/tp/Image00008.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html">
Sony Xperia dual sim 100% brand new </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 250 </h2>
<a class="btn btn-danger  btn-sm make-favorite"> <i class="fa fa-certificate"></i> <span>Featured Ads</span> </a> <a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
<div class="item-list make-grid" style="height: 373px;">
<div class="cornerRibbons urgentAds">
<a href="#"> Urgent</a>
</div>
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/FreeGreatPicture.com-46404-google-drops-nexus-4-by-100-offers-15-day-price-protection-refund.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> Google drops Nexus 4 </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 150 </h2>
<a class="btn btn-danger  btn-sm make-favorite"> <i class="fa fa-certificate"></i> <span>Urgent</span> </a>
<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
 
<div class="item-list make-grid" style="height: 373px;">
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/tp/Image00014.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> Samsung Galaxy S Dous (Brand New/ Intact Box) With 1year Warranty </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 230</h2>
<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
<div class="item-list make-grid" style="height: 373px;">
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/tp/Image00003.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> MSI GE70 Apache Pro-061 17.3" Core i5-4200H/8GB DDR3/NV GTX860M Gaming Laptop </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 400 </h2>
<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
<div class="item-list make-grid" style="height: 373px;">
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/tp/Image00022.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> Apple iPod touch 16 GB 3rd Generation </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 150 </h2>
<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
<div class="item-list make-grid">
<div class="col-sm-2 no-padding photobox">
<div class="add-image"> <span class="photo-count"><i class="fa fa-camera"></i> 2 </span> <a href="ads-details.html"><img class="thumbnail no-margin" src="images/item/FreeGreatPicture.com-46405-google-drops-price-of-nexus-4-smartphone.jpg" alt="img"></a> </div>
</div>
 
<div class="add-desc-box col-sm-7">
<div class="add-details">
<h5 class="add-title"> <a href="ads-details.html"> Google drops Nexus 4 by $100, offers 15 day price protection refund </a> </h5>
<span class="info-row"> <span class="add-type business-ads tooltipHere" data-toggle="tooltip" data-placement="right" title="Business Ads">B </span> <span class="date"><i class=" icon-clock"> </i> Today 1:21 pm </span> - <span class="category">Electronics </span>- <span class="item-location"><i class="fa fa-map-marker"></i> London </span> </span> </div>
</div>
 
<div class="col-sm-3 text-right  price-box">
<h2 class="item-price"> $ 150 </h2>
<a class="btn btn-default  btn-sm make-favorite"> <i class="fa fa-heart"></i> <span>Save</span> </a> </div>
 
</div>
 
</div>
</div>
</div>
 
<div class="tab-box  save-search-bar text-center"> <a href=""> <i class=" icon-star-empty"></i> Save Search </a> </div>
</div>
<div class="pagination-bar text-center">
<ul class="pagination">
<li class="active"><a href="#">1</a></li>
<li><a href="#">2</a></li>
<li><a href="#">3</a></li>
<li><a href="#">4</a></li>
<li><a href="#">5</a></li>
<li><a href="#"> ...</a></li>
<li><a class="pagination-btn" href="#">Next »</a></li>
</ul>
</div>
 
<div class="post-promo text-center">
<h2> Do you get anything for sell ? </h2>
<h5>Sell your products online FOR FREE. It's easier than you think !</h5>
<a href="post-ads.html" class="btn btn-lg btn-border btn-post btn-danger">Post a Free Ad </a></div>
 
</div>

*/