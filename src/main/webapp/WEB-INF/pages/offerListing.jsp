<!-- TOP AREA -->
<%@ include file="header.jsp"%>

<%@ include file="searchbar.jsp"%>

<div class="gap"></div>

<form id="searchform" action="offerListing">
	<div class="container">
		<div class="row">
			<div class="col-md-3">
				<aside class="sidebar-left">
					<ul id="categories"
						class="nav nav-tabs nav-stacked nav-coupon-category">
						<li name="All"><a><i class="fa fa-ticket"></i>All</a></li>
						<c:forEach items="${result.data.offerListing.categoryList}"
							var="pagecategory" varStatus="categorycount">
							<c:set var="categoryClass" value="" />
							<c:choose>
								<c:when test="${category == pagecategory.key.name}">
									<c:set var="categoryClass" value="active"></c:set>
								</c:when>
							</c:choose>
							<li name="${pagecategory.key.name}"><a
								href="#"><i class="fa ${pagecategory.key.imageClass}"></i>${pagecategory.key.name}(${pagecategory.value})</a></li>
							<c:set var="categoryClass" value=""></c:set>
						</c:forEach>
					</ul>
				</aside>
			</div>
			<div class="col-md-9">
				<div class="row">
					<div class="col-md-3">
						<div class="product-sort">
							<input type="hidden" id="sortby" name="sortby" value="${sortby}">
							<input type="hidden" id="reverse" name="reverse" value="${reverse}">
							<input type="hidden" id="query" name="query" value="${query}">
							<input type="hidden" id="place" name="where" value="${place}">
							<input type="hidden" id="category" name="category"
								value="${category}"> <input type="hidden" id="pageNo"
								name="pageNo" value="${pageNo}"> <input type="hidden"
								id="type" name="type" value="${type}"> <input
								type="hidden" id="lastPage" name="lastPage"
								value="${result.data.offerListing.lastPageNumber}"> <span
								class="product-sort-selected">Sort by <b>${sortby}</b></span> <a
								href="#" class="product-sort-order fa fa-angle-down"></a>
							<ul id="sortList">
							<li name="relevance"><a>sort by Relevance</a></li>
								<li name="name"><a>sort by Name</a></li>
								<li name="discount"><a>sort by Discount</a></li>
								<li name="rating"><a>sort by Rating</a></li>
							</ul>
						</div>
					</div>
					<!-- <div class="col-md-2 col-md-offset-7">
                            <div class="product-view pull-right">
                                <a class="fa fa-th-large" href="category-page-shop.html"></a>
                                <a class="fa fa-list active"></a>
                            </div>
                        </div> -->
				</div>
				<c:choose>
					<c:when
						test="${result.status == true && result.data.offerListing.status == true}">
						<div class="col-md-12">
							<div class="row row-wrap">
								<c:set var="offerListings"
									value="${result.data.offerListing.data}" />
								<c:forEach items="${offerListings}" var="offer"
									varStatus="status1">
									<div class="col-md-4 wishlistOffer">
										<div class="product-thumb">
											<header class="product-header">
												<img src="${offer.outlet.images[1]}"
													alt="${offer.shortDesc}" title="${offer.shortDesc}" />
											</header>
											<div class="product-inner">
												<div class="product-inner-head">
													<h5 class="product-title">${offer.outlet.name}</h5>
													<p class="product-desciption">${offer.outlet.miniAdd}</p>
													<input id="input-21e" type="number" class="rating" min=0
														max=5 step=0.5 data-size="xs"
														value="${offer.outlet.rating}" disabled="disabled">
													<p class="product-desciption">${offer.shortDesc}
														${offer.detail}</p>
												</div>
												<input type="hidden" id="outletIsFav${offer.outlet.id}" value=${offer.outlet.outletIsFav} />
												<div class="product-inner-footer">
													<ul class="product-actions-list">
														<li><a id="click${offer.outlet.id}" class="btn btn-sm"
															onClick="addFav(${offer.outlet.id})"><c:if
																test='${offer.outlet.outletIsFav}'>
																<i id="fav${offer.outlet.id}" class="fa fa-heart" style="color: #2a8fbd"></i>
															</c:if>
															<c:if
																test='${!offer.outlet.outletIsFav}'>
																<i id="fav${offer.outlet.id}" class="fa fa-heart"></i>
															</c:if> Favorites</a></li>
														<li><a class="btn btn-sm"
															href="storeDetail?outletId=${offer.outlet.id}"><i
																class="fa fa-bars"></i> Details</a></li>
													</ul>
												</div>
											</div>
										</div>
									</div>
								</c:forEach>
							</div>
							<div class="gap gap-small"></div>
						</div>
					</c:when>
				</c:choose>
				<ul id="pageList" class="pagination">
					<li id="prevPage" class="prev disabled" onclick="prevPage()"><a></a></li>
					<c:forEach begin="1"
						end="${result.data.offerListing.lastPageNumber}" var="page"
						varStatus="status1">
						<li name="${page}"><a>${page}</a></li>
					</c:forEach>
					<li id="nextPage" class="next" onclick="nextPage()"><a></a></li>
				</ul>
				<div class="gap"></div>
			</div>
		</div>

	</div>
	<script type="text/javascript">
	$(document).ready(function() 
		 {
		    $('#sortList li').click(function(e) 
		    { 
		     	var sortName =  $(this).attr('name');
		     	var rev=$("#reverse").val() =='true';
		     	if($("#sortby").val() == sortName)
		     		$("#reverse").attr('value', !rev);
		     	else
		     		$("#reverse").attr('value', false);
		     	$("#sortby").attr('value', sortName);
		     	$("#searchform").submit();
		    });
		    
		    $('#categories li').click(function(e) 
		    { 
		     	var categoryName =  $(this).attr('name');
		     	$("#category").attr('value', categoryName);
		     	$("#searchform").submit();
		    });
		    
		    $('#pageList li').click(function(e) 
		    { 
		     	var page =  $(this).attr('name');
		     	$("#pageNo").attr('value', page);
		     	$("#searchform").submit();
		    });
		    
		    var pageNo= $('#pageNo').val();
		    var lastpage= $('#lastPage').val();
		    function nextPage()
		    { 
		    	if(parseInt(lastpage) >=parseInt(pageNo)){
		     	$("#pageNo").attr('value', parseInt(pageNo) + 1);
		     	$("#searchform").submit();}
		    };
		    
		    function prevPage()
		    { 
			  if(parseInt(pageNo) >=1){  
		     	$("#pageNo").attr('value', parseInt(pageNo) -  1);
		     	$("#searchform").submit();}
		    };
				    
				    
		    if(lastpage >= pageNo){
		    	$('#nextPage').addClass( "nextPage disabled" );
		    }
		    
		    if(!isNaN(pageNo) && parseInt(pageNo) > 1){
		    	$('#prevPage').addClass( "prev" );
		    }
		    
		    if(!isNaN(pageNo) && parseInt(pageNo) == 1){
		    	$('#prevPage').addClass( "prev disabled" );
		    }
		    
		 });
</script>

</form>

<%@include file="footer.jsp"%>
</body>

<script src="js/custom.js"></script>
</html>
