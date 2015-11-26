<%@ include file="header.jsp"%>


<!-- TOP AREA -->
<div class="text-center">
	<h1 class="mb20">Deals of the day</h1>
</div>
<div class="top-area">
	<div class="owl-carousel owl-slider" id="owl-carousel-slider"
		data-inner-pagination="true" data-white-pagination="true">
		<c:choose>
			<c:when
				test="${result.status == true && result.data.trending_offers.status == true}">
				<c:set var="trending_offers"
					value="${result.data.trending_offers.data}" />
				<c:forEach items="${trending_offers}" var="trending_offer"
					varStatus="status1">
					<div style="height: inherit;">
						<div class="bg-holder" style="height: inherit;">
							<img src="${trending_offer.image}"
								alt="${trending_offer.shortDesc}"
								title="${trending_offer.shortDesc}" style="height: inherit;" />

							<%-- <div class="bg-front vert-center text-white text-center">
									<h2 class="text-hero">"${trending_offer.shortDesc}"</h2>
								</div> --%>
						</div>
					</div>
				</c:forEach>
			</c:when>
		</c:choose>
	</div>
</div>
<!-- END TOP AREA -->
<%@ include file="searchbar.jsp"%>
<div class="container">
	<div class="col-md-12">
		<div class="text-center">

			<div class="row">
				<h1 class="mb20">
					Most viewed offers <small><a href="offerListing?type=mall">View
							All</a></small>
				</h1>
				<div class="owl-carousel" id="owl-carousel2" data-items="4">
					<c:choose>
						<c:when
							test="${result.status == true && result.data.mall_offers.status == true}">
							<c:set var="mall_offers" value="${result.data.mall_offers.data}" />
							<c:forEach items="${mall_offers}" var="mall_offer"
								varStatus="status1">
								<div style="height: 100%">
									<div class="product-thumb">
										<header class="product-header">
											<img src="${mall_offer.outlet.images[1]}"
												alt="${mall_offer.shortDesc}"
												title="${mall_offer.outlet.name}" />
										</header>
										<div class="product-inner">
											<div class="product-inner-head">
												<h5 class="product-title">${mall_offer.outlet.name}</h5>
												<p class="product-desciption">${mall_offer.outlet.miniAdd}</p>
												<input id="input-21e" type="number" class="rating" min=0
													max=5 step=0.5 data-size="xs"
													value="${mall_offer.outlet.rating}" disabled="disabled">
												<p class="product-desciption">${mall_offer.detail}</p>
											</div>
											<input type="hidden" id="outletIsFav${mall_offer.outlet.id}" value=${mall_offer.outlet.outletIsFav} />
											<div class="product-inner-footer">
												<ul class="product-actions-list">
													<li><a id="click${mall_offer.outlet.id}" class="btn btn-sm"
														onClick="addFav(${mall_offer.outlet.id})"><c:if
																test='${mall_offer.outlet.outletIsFav}'>
																<i id="fav${mall_offer.outlet.id}" class="fa fa-heart" style="color: #2a8fbd"></i>
															</c:if>
															<c:if
																test='${!mall_offer.outlet.outletIsFav}'>
																<i id="fav${mall_offer.outlet.id}" class="fa fa-heart"></i>
															</c:if> Favorites</a></li>
													<li><a class="btn btn-sm"
														href="storeDetail?outletId=${mall_offer.outlet.id}"><i
															class="fa fa-bars"></i> Details</a></li>
												</ul>
											</div>
										</div>
									</div>
								</div>
							</c:forEach>
						</c:when>
					</c:choose>
				</div>
			</div>
			<div class="row">
				<div class="gap gap-small"></div>
				<h1 class="mb20">
					Your Store next door <small><a
						href="offerListing?type=exclusive">View All</a></small>
				</h1>
				<div class="owl-carousel" id="owl-carousel3" data-items="4">
					<c:choose>
						<c:when
							test="${result.status == true && result.data.exclusive_offers.status == true}">
							<c:set var="exclusive_offers"
								value="${result.data.exclusive_offers.data}" />
							<c:forEach items="${exclusive_offers}" var="exclusive_offer"
								varStatus="status1">
								<div style="height: 100%">
									<div class="product-thumb">
										<header class="product-header">
											<img src="${exclusive_offer.outlet.images[1]}"
												alt="${exclusive_offer.shortDesc}" title="${exclusive_offer.shortDesc}" />
										</header>
										<div class="product-inner">
											<div class="product-inner-head">
												<h5 class="product-title">${exclusive_offer.outlet.name}</h5>
												<p class="product-desciption">${exclusive_offer.outlet.miniAdd}</p>

												<input id="input-21e" type="number" class="rating" min=0
													max=5 step=0.5 data-size="xs"
													value="${exclusive_offer.outlet.rating}"
													disabled="disabled">
												<p class="product-desciption">${exclusive_offer.detail}</p>
											</div>
											<input type="hidden" id="outletIsFav${exclusive_offer.outlet.id}" value=${exclusive_offer.outlet.outletIsFav} />
											<div class="product-inner-footer">
												<ul class="product-actions-list">
													<li><a id="click${exclusive_offer.outlet.id}" class="btn btn-sm"
														onClick="addFav(${exclusive_offer.outlet.id})"><c:if
																test='${exclusive_offer.outlet.outletIsFav}'>
																<i id="fav${exclusive_offer.outlet.id}" class="fa fa-heart" style="color: #2a8fbd"></i>
															</c:if>
															<c:if
																test='${!exclusive_offer.outlet.outletIsFav}'>
																<i id="fav${exclusive_offer.outlet.id}" class="fa fa-heart"></i>
															</c:if>Favorites</a></li>
													<li><a class="btn btn-sm"
														href="storeDetail?outletId=${exclusive_offer.outlet.id}"><i
															class="fa fa-bars"></i> Details</a></li>
												</ul>
											</div>
										</div>
									</div>
								</div>
							</c:forEach>
						</c:when>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
	<div class="gap"></div>
</div>


<!-- //////////////////////////////////
        
            //////////////END PAGE CONTENT///////// 
        
            ////////////////////////////////////-->


<!-- //////////////////////////////////

 //////////////MAIN FOOTER//////////////

 ////////////////////////////////////-->


<!-- //////////////////////////////////
	//////////////MAIN FOOTER//////////////
	////////////////////////////////////-->
<%@include file="footer.jsp"%>

<!-- //////////////////////////////////

 //////////////END MAIN  FOOTER/////////

 ////////////////////////////////////-->
<!-- Custom scripts -->
<script src="js/custom.js"></script>
