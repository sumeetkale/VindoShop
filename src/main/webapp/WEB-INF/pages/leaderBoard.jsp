<%@ include file="header.jsp"%>
<%@ include file="searchbar.jsp"%>
<div class="gap"></div>

<div class="container">
	<div class="row">
		<div class="col-md-3">
			<aside class="sidebar-left">
				<ul class="nav nav-pills nav-stacked nav-arrow">
					<li><a href="profile">Profile</a></li>
					<li><a href="wishlist">Favorites</a></li>
					<li class="active"><a href="leaderBoard">Leaderboard</a></li>
				</ul>
			</aside>
		</div>
		<div class="col-md-4 leaderboard">
			<aside class="sidebar-left" style="margin-right: 0px">
				<div class="box clearfix">
					<table class="table" id="leaderTable">
						<thead>
							<tr>
								<th>Rank</th>
								<th>First Name</th>
								<th>Last Name</th>
								<th>Points</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${result.data}" var="user" varStatus="counter">
								<c:if test="${user.getClass().getSimpleName() == 'Object[]'}">
									<tr id="${counter.index + 1}">
										<td>${counter.index + 1}</td>
										<td>${user[0]}</td>
										<td>${user[1]}</td>
										<td>${user[2]}</td>
									</tr>
								</c:if>
								<c:if test="${user.getClass().getSimpleName() == 'UserDetail'}">
									<script>
										if ("${user.rank<=counter.index}") {
											$("#${user.rank}")
													.replaceWith(
															'<tr style="text-decoration: underline;"><td>${user.rank}</td><td>${user.firstName}</td><td>${user.lastName}</td><td>${user.points}</td></tr>')
										} else {
											$("#leaderTable")
													.append(
															'<tr style="text-decoration: underline;"><td>${user.rank}</td><td>${user.firstName}</td><td>${user.lastName}</td><td>${user.points}</td></tr>')
										}
									</script>
								</c:if>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</aside>
		</div>
		<div class="col-md-5">
			<div class="form-group"
				style="float: left; clear: both; width: 100%;">
				<h2 for="" style="float: left; clear: both; width: 100%">Rewards</h2>
			</div>
			<div class="fotorama" data-allowfullscreen="1" style="height: 480px;">
				<img src="img\Being Human\photo 3.JPG" alt="No Store Images"
					title="cascada" /> <img src="img\Being Human\photo 3.JPG"
					alt="No Store Images" title="cascada" />
			</div>
		</div>
	</div>
	<div class="gap"></div>
</div>

<!-- //////////////////////////////////
	//////////////MAIN FOOTER//////////////
	////////////////////////////////////-->
<%@include file="footer.jsp"%>

<!-- //////////////////////////////////
	//////////////END PAGE CONTENT///////// 
	////////////////////////////////////-->

</div>
</body>

</html>
