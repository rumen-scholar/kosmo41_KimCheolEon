<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Write-Form</title>

<%
	String userName = null;
	if(session.getAttribute("ValidMem") == null) {
%>
<jsp:forward page="login.jsp" />
<%
	}else{
		userName = (String)session.getAttribute("userName");
	}
%>s

<script src="https://cdn.ckeditor.com/4.10.0/standard/ckeditor.js"></script>

<!-- 웹페이지 메인 -->
<link href="resources/web/css/bootstrap.css" rel='stylesheet'
	type='text/css' />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="resources/web/js/jquery.min.js"></script>
<!-- Custom Theme files -->
<link href="resources/web/css/style.css" rel="stylesheet"
	type="text/css" media="all" />
<!-- Custom Theme files -->
<!-- navigation -->
<link href="resources/web/css/component.css" rel="stylesheet"
	type="text/css" />
<!-- navigation -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords"
	content="My Pets Responsive web template, Bootstrap Web Templates, Flat Web Templates, Andriod Compatible web template, 
Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
<script type="application/x-javascript">	
	
	 addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } 
	 
</script>
<!--webfont-->
<link
	href='http://fonts.googleapis.com/css?family=Roboto:400,100,100italic,300,300italic,400italic,500,500italic,700,700italic,900,900italic'
	rel='stylesheet' type='text/css'>
<script src="resources/web/js/responsiveslides.min.js"></script>
<script>
	$(function() {
		$("#slider").responsiveSlides({
			auto : true,
			speed : 500,
			namespace : "callbacks",
			pager : true,
		});
	});
</script>
<script type="text/javascript" src="resources/web/js/move-top.js"></script>
<script type="text/javascript" src="resources/web/js/easing.js"></script>
</head>
<body>

	<!-- header-section-starts -->
	<div class="header">
		<div class="header-top">
			<div class="container">
				<p class="location">
					<i class="location"></i>한국소프트웨어인재개발원
				</p>
				<%
                  if(session.getAttribute("ValidMem") != null){
                  %>
				<p class="phonenum"><%=userName %>
					님 안녕하세요
				</p>
				<%
                  }else{
                  %>
				<p class="phonenum">로그인 해주세요</p>
				<%
                  }
                  %>
				<div class="clearfix"></div>
			</div>
		</div>
		<div class="header-bottom">
			<div class="container">
				<div class="logo">
					<a href="/spring"><img src="resources/web/images/logo.png"
						alt="" /></a>
				</div>
				<span class="menu"></span>
				<div class="top-menu">
					<ul>
						<nav class="cl-effect-5">
							<li><a class="active" href="/spring"><span
									data-hover="Home">Home</span></a></li>
							<li><a href="list"><span data-hover="일반게시판">일반게시판</span></a></li>
							<li><a href="joinlist"><span data-hover="모임게시판">모임게시판</span></a></li>
							<%
                  			if(session.getAttribute("ValidMem") != null){
                  			%>
							<li><a href="recordcheck"><span data-hover="기록관리">기록관리</span></a></li>
							<li><a href=""><span data-hover="마이페이지">마이페이지</span></a></li>
							<li><a href=""><span data-hover="로그아웃">로그아웃</span></a></li>
							<%
                 			 } 
                 			 %>
						</nav>
					</ul>
				</div>
				<!-- script for menu -->
				<script>
            $( "span.menu" ).click(function() {
              $( ".top-menu" ).slideToggle( "slow", function() {
                // Animation complete.
              });
            });
         </script>
				<!-- script for menu -->
				<div class="clearfix"></div>
			</div>
		</div>
	</div>

	<!-- content-section-starts -->
	<div class="content">
		<div class="main">
			<div class="container">
				<div class="cont span_2_of_3">
					<div class="single">
						<div class="leave">
							<h4>일반게시판 글작성</h4>
						</div>


						<form action="write.do" method="post" enctype="multipart/form-data">
							<div class="container">
								<table class="table table-striped" width="800">
									<div>
										<tr>
											<td>이름</td>
											<td><input type="text" name="bName" size="50" 
												value="<%=userName %>" readonly /></td>
										</tr>
										<tr>
											<td>제목</td>
											<td><input type="text" name="bTitle" size="50"></td>
										</tr>
										<tr>
											<td>내용</td>
											<td><textarea name="bContent" id="editor1" rows="10"
													cols="80">
	          									</textarea> <script>
	                						// Replace the <textarea id="editor1"> with a CKEditor
	                						// instance, using default configuration.
	                						CKEDITOR.replace( 'editor1' );
              							</script> 파일 : <input type="file" name="filename"><br /></td>
										</tr>
										<tr>

											<td colspan="2" align="right"><input type="submit"
												value="입력">&nbsp;&nbsp; <a href="list.do">목록보기</a></td>
										</tr>
									</div>
								</table>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>



</body>
</html>