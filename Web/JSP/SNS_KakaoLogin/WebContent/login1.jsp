<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login Demo - Kakao JavaScript SDK</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width"/>
    <script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
</head>
<body>


    <a id="kakao-login-btn"></a>
    <a href="http://developers.kakao.com/logout">aaaaa</a>
    <script type='text/javascript'>
      //<![CDATA[
        Kakao.init('8beb03cd0b3e1684f92220e9b0c345bf');
        // 카카오 로그인 버튼을 생성합니다.
        Kakao.Auth.createLoginButton({
          container: '#kakao-login-btn',
          success: function(authObj) {
            alert(authObj);
          },
          fail: function(err) {
             alert(authObj);
          }
        });
      //]]>
    </script>
    
</body>
</html>
