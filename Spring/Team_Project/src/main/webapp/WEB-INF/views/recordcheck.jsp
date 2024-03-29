<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>recordcheck</title>

<%
	String userName = null;
	if(session.getAttribute("ValidMem") != null){
		userName = (String)session.getAttribute("userName");
	}
%>

<!-- 웹페이지 메인 -->
<link href="resources/web/css/bootstrap.css" rel='stylesheet' type='text/css' />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="resources/web/js/jquery.min.js"></script>
<!-- Custom Theme files -->
<link href="resources/web/css/style.css" rel="stylesheet" type="text/css" media="all" />
<!-- Custom Theme files -->
<!-- navigation -->
<link href="resources/web/css/component.css" rel="stylesheet" type="text/css"  />
<!-- navigation -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="My Pets Responsive web template, Bootstrap Web Templates, Flat Web Templates, Andriod Compatible web template, 
Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<!--webfont-->
<link href='http://fonts.googleapis.com/css?family=Roboto:400,100,100italic,300,300italic,400italic,500,500italic,700,700italic,900,900italic' rel='stylesheet' type='text/css'>
<script src="resources/web/js/responsiveslides.min.js"></script>
 <script>
    $(function () {
      $("#slider").responsiveSlides({
         auto: true,
         speed: 500,
        namespace: "callbacks",
        pager: true,
      });
    });
  </script>
  <script type="text/javascript" src="resources/web/js/move-top.js"></script>
<script type="text/javascript" src="resources/web/js/easing.js"></script>

<script type="text/javascript"
	src="https://openapi.map.naver.com/openapi/v3/maps.js?clientId=3JqMeRQ5Uq03soADkuw8&submodules=geocoder"></script>

<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
	integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
	crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
	integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
	crossorigin="anonymous"></script>
</head>
<body class="onoffmix">

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
				<p class="phonenum"><%=userName %> 님 안녕하세요</p>
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

	
	<script>

	var map = new naver.maps.Map('map', {
	    center: new naver.maps.LatLng(37.3674001, 127.1181196),
	    zoom: 9
	});

	var polygon = new naver.maps.Polygon({
	    map: map,
	    paths: [
	        [
	            new naver.maps.LatLng(37.37544345085402, 127.11224555969238),
	            new naver.maps.LatLng(37.37230584065902, 127.10791110992432),
	            new naver.maps.LatLng(37.35975408751081, 127.10795402526855),
	            new naver.maps.LatLng(37.359924641705476, 127.11576461791992),
	            new naver.maps.LatLng(37.35931064479073, 127.12211608886719),
	            new naver.maps.LatLng(37.36043630196386, 127.12293148040771),
	            new naver.maps.LatLng(37.36354029942161, 127.12310314178465),
	            new naver.maps.LatLng(37.365211629488016, 127.12456226348876),
	            new naver.maps.LatLng(37.37544345085402, 127.11224555969238)
	        ]
	    ],
	    fillColor: '#ff0000',
	    fillOpacity: 0.3,
	    strokeColor: '#ff0000',
	    strokeOpacity: 0.6,
	    strokeWeight: 3
	});

	var polyline = new naver.maps.Polyline({
	    map: map,
	    path: [
	        new naver.maps.LatLng(37.359924641705476, 127.1148204803467),
	        new naver.maps.LatLng(37.36343797188166, 127.11486339569092),
	        new naver.maps.LatLng(37.368520071054576, 127.11473464965819),
	        new naver.maps.LatLng(37.3685882848096, 127.1088123321533),
	        new naver.maps.LatLng(37.37295383612657, 127.10876941680907),
	        new naver.maps.LatLng(37.38001321351567, 127.11851119995116),
	        new naver.maps.LatLng(37.378546827477855, 127.11984157562254),
	        new naver.maps.LatLng(37.376637072444105, 127.12052822113036),
	        new naver.maps.LatLng(37.37530703574853, 127.12190151214598),
	        new naver.maps.LatLng(37.371657839593894, 127.11645126342773),
	        new naver.maps.LatLng(37.36855417793982, 127.1207857131958)
	    ]
	});
		</script>
	
	
</body>
</html>