<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="header.jsp"%>
<%@ include file="searchbar.jsp"%>
<div class="gap"></div>


<div class="container">
	<div class="row">
		<div class="col-md-3">
			<div class="product-page-meta box" style="float: left">
				<img src="${outlet.images[0]}"
					style="width: 75px; float: left; margin-right: 10px" />
				<h1
					style="color: #337ab7; margin-top: 0px; display: -webkit-box; margin-bottom: 10px;">${outlet.name}</h1>
				<input type="hidden" id="outletName" value="${outlet.name}">
				<div class="storeInfo">
					<img src="img/Location Filled-50.png">
					<div>${outlet.address},${outlet.city}</div>
				</div>
				<div class="storeInfo">
					<i class="icon-camera-retro icon-large"></i>
					<div class="storeDesc">0${outlet.phone}</div>
				</div>

				<div class="storeInfo">
					<input type="hidden" id="outletIsFav${outlet.id}" value=${outlet.outletIsFav} />
					<c:if test='${outlet.outletIsFav}'>
						<img id="img${outlet.id}" src="img/fav.png">
					</c:if>
					<c:if test='${!outlet.outletIsFav}'>
						<img id="img${outlet.id}" src="img/Nofav.png">
					</c:if>
					<div class="storeDesc">
						<a style="cursor: pointer;" onClick="addFav(${outlet.id})">Favorites</a>
					</div>
				</div>

				<ul class="list product-page-meta-info">
					<li>
						<p style="float: left; font-size: 12px; font-weight: bold;">Share
							this Store offers to earn points</p> <img id="fbshare"
						src="img/Facebook.png"
						style="width: 21%; float: left; clear: both;" /> <a id=tweetshare
						href="http://twitter.com/intent/tweet?url=www.vindoshop.com/storeDetail?outletId=${outlet.id};"
						style="float: left; width: 22%; margin-left: 6px;"><img
							src="img/twitter.png" /></a>
						<div style="float: left; margin: 9px;">
							<g:plus action="share" annotation="none"
								href="www.vindoshop.com/storeDetail?outletId=${outlet.id}"
								onendinteraction="endInteraction" onshare="shareState"
								style="height: 40px; "></g:plus>
						</div>

					</li>
				</ul>
				<!-- <div id="shareIcons" class="jssocials"></div> -->
			</div>
			<div class="gap gap-small"></div>
			<div class="sidebar-box">
				<h4>Store Ratings</h4>
				<div class="ratingTitle">New Inventory</div>
				<input id="inventoryRating" type="number" class="rating ratingstars"
					min=0 max=5 step=0.5 data-size="xs"
					value="${outlet.inventoryRating}" disabled="disabled"
					style="display: inline;"> <input type="hidden"
					id="outletId" value="${outlet.id}">
				<div class="ratingTitle">Staff Co-operation</div>
				<input id="staffRating" type="number" class="rating ratingstars"
					min=0 max=5 step=0.5 data-size="xs" value="${outlet.staffRating}"
					disabled="disabled" style="display: inline;">
				<div class="ratingTitle">Offers and Deals</div>
				<input id="discountRating" type="number" class="rating ratingstars"
					min=0 max=5 step=0.5 data-size="xs"
					value="${outlet.discountRating}" disabled="disabled"
					style="display: inline;">
				<div class="ratingTitle">Overall Rating</div>
				<input id="avgRating" type="number" class="rating ratingstars" min=0
					max=5 step=0.5 data-size="xs" value="${outlet.rating}"
					disabled="disabled" style="display: inline;"> <a
					class="popup-text"
					href="<%=(request.getUserPrincipal() == null
					? "#login-dialog"
					: "#rating_dialog")%>"
					data-effect="mfp-zoom-out">Rate this Store and Earn points</a>

			</div>
			<div class="gap gap-small"></div>
			<div class="sidebar-box">
				<h4>Categories</h4>
				<ul class="tags-list">
					<c:forTokens var="tags" items="${outlet.tags }" delims=",">
						<li><a>${tags }</a></li>
					</c:forTokens>
				</ul>
			</div>
		</div>
		<div class="col-md-9">
			<div class="fotorama" data-allowfullscreen="1" style="height: 480px;">
				<c:forEach items="${outlet.images}" var="image" varStatus="status">
					<c:choose>
						<c:when test="${!status.first }">
							<img src="${image }" alt="No Store Images" title="cascada" />
						</c:when>
					</c:choose>
				</c:forEach>
			</div>
			<c:if test="${!empty outlet.currentOffers}">
				<div class="gap gap-small"></div>
				<h3 class="mb20">Top Deals For You</h3>
				<div class="row row-wrap">
					<div class="owl-carousel storeDetails" id="owl-carousel1"
						data-items="4">
						<c:set var="offers" value="${outlet.currentOffers}" />
						<c:forEach items="${offers}" var="offers" varStatus="status1">
							<div style="height: 92%">
								<div class="product-thumb">
									<div class="product-inner">
										<div class="product-inner-head">
											<h4 class="product-title" style="margin: 0px; height: 30%">${offers.shortDesc}</h4>
											<p class="product-desciption" style="height: 50%;">${offers.detail}</p>
											<p class="product-desciption">Valid Till:
												${offers.endDate}</p>
										</div>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
			</c:if>
			<c:if test="${!empty outlet.upcomingOffers}">
				<div class="gap gap-small"></div>
				<h3 class="mb20">Upcoming Deals</h3>
				<div class="row row-wrap">
					<div class="owl-carousel storeDetails" id="owl-carousel2"
						data-items="4">
						<c:set var="offers" value="${outlet.upcomingOffers}" />
						<c:forEach items="${offers}" var="offers" varStatus="status1">
							<div style="height: 92%">
								<div class="product-thumb">
									<div class="product-inner">
										<div class="product-inner-head">
											<h5 class="product-title" style="margin: 0px; height: 30%">${offers.shortDesc}</h5>
											<p class="product-desciption" style="height: 50%;">${offers.detail}</p>
											<p class="product-desciption">Starts From:
												${offers.startDate}</p>
										</div>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
			</c:if>
			<div class="gap gap-small"></div>
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab">
					<li class="active"><a href="#google-map-tab" data-toggle="tab"><i
							class="fa fa-map-marker"></i>Location</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane fade in active" id="google-map-tab">
						<div class="row">
							<div class="col-md-12">
								<div id="map-canvas" style="width: 100%; height: 300px;"
									latitude="${outlet.latitude}" longitude="${outlet.longitude}"></div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="gap gap-small"></div>

		</div>


	</div>

</div>



<!-- //////////////////////////////////
	//////////////END PAGE CONTENT/////////
	////////////////////////////////////-->
<%@include file="footer.jsp"%>
<script type="text/javascript"
	src="http://maps.googleapis.com/maps/api/js?amp;extension=.js&amp;output=embed"></script>

<script>	window.onload = function() {
 var myCenter=new google.maps.LatLng(Number($("#map-canvas").attr("latitude")), Number($("#map-canvas").attr("longitude")));
var marker=new google.maps.Marker({
    position:myCenter
    });
var mapProp = {
center:myCenter,
zoom: 14,
draggable: true,
scrollwheel: true,
mapTypeId:google.maps.MapTypeId.ROADMAP
};

var map=new google.maps.Map(document.getElementById("map-canvas"),mapProp);
marker.setMap(map);

google.maps.event.addListener(marker, 'click', function() {

infowindow.setContent(contentString);
infowindow.open(map, marker);

});}</script>
<script src="js/jssocials.min.js"></script>
<script src="js/jquery.contact-buttons.js"></script>

<script type="text/javascript">
$("#fbshare").on('click', function(event) {
    event.preventDefault();
    var that = $(this);
    var post = that.parents('article.post-area');

    $.ajaxSetup({ cache: true });
        $.getScript('//connect.facebook.net/en_US/sdk.js', function(){
        FB.init({
          appId: '1687180814851994', //replace with your app ID
          version: 'v2.3'
        });
        FB.ui({
        	method : 'share',
			title : 'VindoShop',
			description : 'Check out exciting offers on Vindoshop',
			href: 'https://developers.facebook.com/docs/',
          },
          function(response) {
            if (response && !response.error_code) {
            	shareCallBack("Share");
            } else {
              alert('Error while posting.');
            }
        });
  });
});
</script>
</body>
<style type="text/css">
x
.share-btn-wrp {
	list-style: none; display: block; margin: 0px; padding: 0px;
	width: 32px; left: 0px; position: fixed;
}

.share-btn-wrp .button-wrap {
	text-indent: -100000px; width: 32px; height: 32px; cursor: pointer;
	transition: width 0.1s ease-in-out;
}

@media all and (max-width: 699px) {
	.share-btn-wrp {
		width: 100%; text-align: center; position: fixed; bottom: 1px;
	}
	.share-btn-wrp .button-wrap {
		display: inline-block; margin-left: -2px; margin-right: -2px;
	}
}
</style>
<script type="text/javascript">
$(document).ready(function(){
    var pageTitle	= document.title; //HTML page title
    var pageUrl		= location.href; //Location of this page
	
	function OpenShareUrl(openLink){
		//Parameters for the Popup window
        winWidth    = 650; 
        winHeight   = 450;
        winLeft     = ($(window).width()  - winWidth)  / 2,
        winTop      = ($(window).height() - winHeight) / 2,
        winOptions   = 'width='  + winWidth  + ',height=' + winHeight + ',top='    + winTop    + ',left='   + winLeft;
        window.open(openLink,'Share This Link',winOptions); //open Popup window to share website.
        return false;
	}
});
</script>
<script src="js/custom.js"></script>
</html>
