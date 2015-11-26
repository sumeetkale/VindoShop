<%@ include file="header.jsp"%>
<%@ include file="searchbar.jsp"%>
<div id="edit-profile"
	class="mfp-with-anim mfp-hide mfp-dialog clearfix">
	<i class="fa fa-sign-in dialog-icon"></i>
	<h3>My Profile</h3>
	<h5>Update details and save.</h5>
	<form:form class="dialog-form" action="updateProfile"
		modelAttribute="userDetail" method="POST" name="userDetailForm"
		id="editprofile">
		<div class="form-group">
			<label style="display: block;" for="">Name</label>
			<form:input type="text" style="width: 49%; display: inline;"
				data-validation-engine="validate[required,custom[onlyLetterSp]]" placeholder="First Name"
				path="firstName" class="form-control" />
			<form:input style="width: 49%; display: inline;" type="text"
				path="lastName" class="form-control" placeholder="Last Name"
				data-validation-engine="validate[required,custom[onlyLetterSp]]" />
		</div>
		<div class="form-group">
			<label>Date of Birth</label>
			<div class="form-group dob">

				<form:input type="date" class="form-control" path="dateofBirth"
					id="dob" data-validation-engine="validate[required]" />
				<!-- <div class="input-group-addon">
					<span class="glyphicon glyphicon-th"></span>
				</div> -->
			</div>
		</div>
		<div class="form-group">
			<label style="display: block;" for="">Address</label>
			<form:input placeholder="Country" type="text"
				style="width: 49%; display: inline;" path="country"
				data-validation-engine="validate[required]" class="form-control" />
			<form:input placeholder="State" style="width: 49%; display: inline;"
				data-validation-engine="validate[required]" type="text" path="state"
				class="form-control" />
			<form:input placeholder="City" style="width: 49%; display: inline;"
				data-validation-engine="validate[required]" type="text" path="city"
				class="form-control" />
			<form:input placeholder="Location"
				style="width: 49%; display: inline;" type="text" path="location"
				data-validation-engine="validate[required]" class="form-control" />
			<form:input placeholder="Zip/Postal"
				style="width: 49%; display: inline;" type="text" path="pinCode"
				data-validation-engine="validate[required,zip]" class="form-control" />
		</div>
		<%-- <input type="hidden" name="${_csrf.parameterName}"
										value="${_csrf.token}" /> --%>
		<input type="submit" value="Update" class="btn btn-primary">
	</form:form>

</div>
<div id="edit-password"
	class="mfp-with-anim mfp-hide mfp-dialog clearfix">
	<i class="fa fa-sign-in dialog-icon"></i>
	<h3>Change password</h3>
	<h5>Update details and save.</h5>
	<form:form class="dialog-form" action="changePwd" modelAttribute="user"
		method="POST" name="userDetailForm" id="changePwd">

		<div class="form-group">
			<label style="display: block;" for="">New Password</label>
			<form:input type="password" style=" display: inline;"
				data-validation-engine="validate[required]"
				placeholder="New Password" path="password" class="form-control" />
		</div>
		<div class="form-group">
			<label style="display: block;" for="">Confirm Password</label> <input
				type="password" style="display: inline;"
				data-validation-engine="validate[required,equals[password]]"
				placeholder="Confirm Password" class="form-control" />
		</div>
		<%-- <input type="hidden" name="${_csrf.parameterName}"
										value="${_csrf.token}" /> --%>
		<input type="submit" value="Update" class="btn btn-primary">
	</form:form>

</div>



<div id="edit-image" class="mfp-with-anim mfp-hide mfp-dialog clearfix">
	<i class="fa fa-sign-in dialog-icon"></i>
	<h3>My Profile Image</h3>
	<h5>Select new photo to update</h5>
	<form:form class="dialog-form" enctype="multipart/form-data"
		modelAttribute="userDetail" action="uploadImage" id="uploadImageForm">
		<div class="form-group">
			<form:input path="profileImage" type="file" id="profileImage"
				data-validation-engine="validate[required]" name="profileImage"
				size="20" />
		</div>
		<input type="submit" value="Update" class="btn btn-primary">
	</form:form>

</div>
<div class="gap"></div>
<div class="container">
	<div class="row">
		<div class="col-md-3">
			<aside class="sidebar-left">
				<ul class="nav nav-pills nav-stacked nav-arrow">
					<li class="active"><a href="profile">Profile</a></li>
					<li><a href="wishlist">Favorites</a></li>
					<li><a href="leaderBoard">Leaderboard</a></li>
				</ul>
			</aside>
		</div>
		<div class="col-md-9">
			<div class="row">
				<div class="col-md-4">
					<a class="hover-img proile-img popup-text" href="#edit-image"
						data-effect="mfp-move-from-top"> <img
						src="data:image/jpeg;base64,${image}" alt="Upload Profile Image">
						<h5 class="hover-title hover-title-center hover-title-hide">Click
							to change</h5>
					</a>
					<div class="form-group"
						style="float: left; clear: both; width: 100%; margin-top: 10px; margin-bottom: 0px;">
						<h3 for=""
							style="float: left; clear: both; width: 60%; margin-top: 0px">Points
							earned:</h3>
						<h3
							style="float: left; padding-left: 2%; margin-top: 0px; font-weight: normal">${userDetail.points}</h3>
					</div>
					<div class="form-group"
						style="float: left; clear: both; width: 100%; margin-bottom: 0px;">
						<label for="" style="float: left; clear: both; width: 60%">Referral
							Code:</label> <label
							style="float: left; padding-left: 2%; font-weight: normal"></label>
						<label
							style="float: left; padding-left: 2%; font-weight: normal; margin-bottom: 10px;">${userDetail.referral}</label>
					</div>
				</div>
				<div class="col-md-8">
					<div class="form-group"
						style="float: left; clear: both; width: 100%; margin-bottom: 5px;">
						<h2 for=""
							style="float: left; clear: both; width: 70%; margin-top: 0px">
							Personal Information</h2>
						<div style="float: right; width: 30%;"
							class="fa-hover col-md-3 col-sm-4">
							<a target="_blank" class="popup-text" href="#edit-profile"
								data-effect="mfp-move-from-top"><i class="fa fa-edit"></i>
								Edit Profile<span class="text-muted"></span></a> <a
								style="float: left" target="_blank" class="popup-text"
								href="#edit-password" data-effect="mfp-move-from-top"><i
								class="fa fa-edit"></i> Change Password<span class="text-muted"></span></a>
						</div>
					</div>
					<div class="form-group"
						style="float: left; clear: both; width: 100%; margin-bottom: 0px;">
						<label for="" style="float: left; clear: both; width: 30%">First
							Name:</label> <label
							style="float: left; padding-left: 2%; font-weight: normal">${userDetail.firstName}</label>
					</div>
					<div class="form-group"
						style="float: left; clear: both; width: 100%; margin-bottom: 0px;">
						<label for="" style="float: left; clear: both; width: 30%">Last
							Name:</label> <label
							style="float: left; padding-left: 2%; font-weight: normal">${userDetail.lastName}</label>
					</div>
					<div class="form-group"
						style="float: left; clear: both; width: 100%; margin-bottom: 0px;">
						<label for="" style="float: left; clear: both; width: 30%">DOB:</label>
						<label style="float: left; padding-left: 2%; font-weight: normal"><fmt:formatDate value="${userDetail.dateofBirth}" pattern="MM/dd/yyyy"/></label>
					</div>
					<div class="form-group"
						style="float: left; clear: both; width: 100%">
						<label for="" style="float: left; clear: both; width: 30%">Phone
							Number:</label> <label
							style="float: left; padding-left: 2%; font-weight: normal">${userDetail.phone}</label>
					</div>
					<div class="form-group"
						style="float: left; clear: both; width: 100%; margin-bottom: 5px;">
						<h2 for=""
							style="float: left; clear: both; width: 100%;; margin-top: 0px">Address:</h2>

					</div>



					<div class="form-group"
						style="float: left; clear: both; width: 100%; margin-bottom: 0px;">
						<label for="" style="float: left; clear: both; width: 30%">Country
							:</label> <label
							style="float: left; padding-left: 2%; font-weight: normal">${userDetail.country}</label>
					</div>
					<div class="form-group"
						style="float: left; clear: both; width: 100%; margin-bottom: 0px;">
						<label for="" style="float: left; clear: both; width: 30%">State
							:</label> <label
							style="float: left; padding-left: 2%; font-weight: normal">${userDetail.state}</label>
					</div>
					<div class="form-group"
						style="float: left; clear: both; width: 100%; margin-bottom: 0px;">
						<label for="" style="float: left; clear: both; width: 30%">City
							:</label> <label
							style="float: left; padding-left: 2%; font-weight: normal">${userDetail.city}</label>
					</div>
					<div class="form-group"
						style="float: left; clear: both; width: 100%; margin-bottom: 0px;">
						<label for="" style="float: left; clear: both; width: 30%">Location
							:</label> <label
							style="float: left; padding-left: 2%; font-weight: normal">${userDetail.location}</label>
					</div>
					<div class="form-group"
						style="float: left; clear: both; width: 100%;">
						<label for="" style="float: left; clear: both; width: 30%">Zip/Postal
							:</label> <label
							style="float: left; padding-left: 2%; font-weight: normal">${userDetail.pinCode}</label>
					</div>


				</div>


			</div>
			<div class="gap"></div>
		</div>
		<div class="col-md-3"></div>
		<div class="col-md-9" style="margin-top: -40px;">

			<c:choose>
				<c:when test="${userDetail.checkins != null}">
					<h2>Recent Checkins</h2>

					<!-- START COMMENTS -->
					<ul class="comments-list">

						<c:forEach var="checkin" items="${userDetail.checkins}">
							<li>
								<!-- COMMENT -->
								<article class="comment">
									<div class="comment-author">
										<img style="width: 170px; height: 120px"
											src="${checkin.outlet.images[0]}"
											alt="${checkin.outlet.name}" title="${checkin.outlet.name}">
									</div>
									<div class="comment-inner">
										<div class="comment-author-name">${checkin.outlet.name}</div>
										<p class="comment-content">${checkin.outlet.address}</p>
										<span class="comment-time">${checkin.checkinDate}</span>
									</div>
								</article>

							</li>
						</c:forEach>
					</ul>
				</c:when>
			</c:choose>
			<!-- END COMMENTS -->
			<div class="gap"></div>
		</div>
	</div>

</div>
<!-- //////////////////////////////////
	//////////////MAIN FOOTER//////////////
	////////////////////////////////////-->

<%@include file="footer.jsp" %>


<!-- //////////////////////////////////
	//////////////END PAGE CONTENT/////////
	////////////////////////////////////-->
<!-- Custom scripts -->
<script src="js/custom.js"></script>
<script>
	$(document).ready(
			function() {
				$("#editprofile").validationEngine();
				$("#uploadImageForm").validationEngine();
				$(".proile-img img").css("height",
						$(".proile-img img").css("width"));
				$(window).resize(
						function() {
							$(".proile-img img").css("height",
									$(".proile-img img").css("width"));
						});
			});
</script>
</div>
</body>

</html>