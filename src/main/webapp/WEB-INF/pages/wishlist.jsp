<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ include file="header.jsp"%>
<%@ include file="searchbar.jsp"%>
<%@page import="org.castor.util.Base64Encoder"%>
<%@page import="com.itextpdf.text.pdf.codec.Base64"%>
<div class="gap"></div>

<div class="container" style="min-height: 500px;">
	<div class="row">
		<div class="col-md-3">
			<aside class="sidebar-left">
				<ul class="nav nav-pills nav-stacked nav-arrow">
					<li><a href="profile">Profile</a></li>
					<li class="active"><a href="wishlist">Favorites</a></li>
					<li><a href="leaderBoard">Leaderboard</a></li>
				</ul>
			</aside>
		</div>
		<c:choose>
			<c:when test="${result.status == true}">
				<div class="col-md-9">
					<div class="row row-wrap">
						<c:forEach items="${result.data}" var="bookmarks"
							varStatus="status1">
							<div class="col-md-4 wishlistOffer" id="store${bookmarks.id}">
								<div class="product-thumb">
									<header class="product-header">
										<img src="${bookmarks.outlet.images[1]}"
											alt="${bookmarks.outlet.name}"
											title="${bookmarks.outlet.name}" />
									</header>
									<div class="product-inner">
										<div class="product-inner-head">
											<input id="input-21e" type="number" class="rating" min=0
												max=5 step=0.5 data-size="xs"
												value="${bookmarks.outlet.rating}" disabled="disabled">
											<h5 class="product-title">${bookmarks.outlet.name}</h5>
											<p class="product-desciption">${bookmarks.outlet.address}</p>
										</div>
										<div class="product-inner-footer">
											<ul class="product-actions-list">
												<li><a class="btn btn-sm"
													href="storeDetail?outletId=${bookmarks.outlet.id}"><i
														class="fa fa-bars"></i> Details</a></li>
											</ul>
										</div>
									</div>
								</div>
								<div class="product-wishlist-remove">
									<a class="btn btn-ghost btn-sm"
										onclick="removeBookmark(${bookmarks.id})"><i
										class="fa fa-times"></i> Remove from Favorites</a>
								</div>
							</div>
						</c:forEach>
					</div>
					<div class="gap gap-small"></div>
				</div>
			</c:when>
		</c:choose>
	</div>

</div>


<!-- //////////////////////////////////
	//////////////END PAGE CONTENT///////// 
	////////////////////////////////////-->

<!-- //////////////////////////////////
	//////////////MAIN FOOTER//////////////
	////////////////////////////////////-->
	<%@include file="footer.jsp" %>


<!-- Custom scripts -->
<script src="js/custom.js"></script>
</div>
</body>
</html>
