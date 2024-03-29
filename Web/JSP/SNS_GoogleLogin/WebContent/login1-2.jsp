<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>구글 아이디로 로그인하기 1</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="http://code.jquery.com/jquery.js"></script>
    
    <script src="https://apis.google.com/js/platform.js" async defer></script>
	<meta name="google-signin-client_id" content="448134969775-n2cpnokuosg0kjcvbk2m90cota6o5beq.apps.googleusercontent.com">

	<script>
	function onSignIn(googleUser) {
		var profile = googleUser.getBasicProfile();
		console.log('ID: ' + profile.getId());
		console.log('Name: ' + profile.getName());
		console.log('Image URL: ' + profile.getImageUrl());
		console.log('Email: ' + profile.getEmail());

		$('#login').css('display', 'none');
    	$('#logout').css('display', 'block');
    	$('#upic').attr('src', profile.getImageUrl());
    	$('#uname').html('[ ' +profile.getName() + ' ]');
	}
	function signOut() {
	    var auth2 = gapi.auth2.getAuthInstance();
	    auth2.signOut().then(function () {
	    	console.log('User signed out.');
	    
	    	$('#login').css('display', 'block');
	    	$('#logout').css('display', 'none');
	    	$('#upic').attr('src', '');
	    	$('#uname').html('');
	    });
	}
	
	
	
	var auth2; // The Sign-In object.
	var googleUser; // The current user.

	/**
	 * Calls startAuth after Sign in V2 finishes setting up.
	 */
	var appStart = function() {
	  gapi.load('auth2', initSigninV2);
	};

	/**
	 * Initializes Signin v2 and sets up listeners.
	 */
	var initSigninV2 = function() {
	  auth2 = gapi.auth2.init({
	      client_id: 'CLIENT_ID.apps.googleusercontent.com',
	      scope: 'profile'
	  });

	  // Listen for sign-in state changes.
	  auth2.isSignedIn.listen(signinChanged);

	  // Listen for changes to current user.
	  auth2.currentUser.listen(userChanged);

	  // Sign in the user if they are currently signed in.
	  if (auth2.isSignedIn.get() == true) {
	    auth2.signIn();
	  }

	  // Start with the current live values.
	  refreshValues();
	};

	/**
	 * Listener method for sign-out live value.
	 *
	 * @param {boolean} val the updated signed out state.
	 */
	var signinChanged = function (val) {
	  console.log('Signin state changed to ', val);
	  document.getElementById('signed-in-cell').innerText = val;
	};

	/**
	 * Listener method for when the user changes.
	 *
	 * @param {GoogleUser} user the updated user.
	 */
	var userChanged = function (user) {
	  console.log('User now: ', user);
	  googleUser = user;
	  updateGoogleUser();
	  document.getElementById('curr-user-cell').innerText =
	    JSON.stringify(user, undefined, 2);
	};

	/**
	 * Updates the properties in the Google User table using the current user.
	 */
	var updateGoogleUser = function () {
	  if (googleUser) {
	    document.getElementById('user-id').innerText = googleUser.getId();
	    document.getElementById('user-scopes').innerText =
	      googleUser.getGrantedScopes();
	    document.getElementById('auth-response').innerText =
	      JSON.stringify(googleUser.getAuthResponse(), undefined, 2);
	  } else {
	    document.getElementById('user-id').innerText = '--';
	    document.getElementById('user-scopes').innerText = '--';
	    document.getElementById('auth-response').innerText = '--';
	  }
	};

	/**
	 * Retrieves the current user and signed in states from the GoogleAuth
	 * object.
	 */
	var refreshValues = function() {
	  if (auth2){
	    console.log('Refreshing values...');

	    googleUser = auth2.currentUser.get();

	    document.getElementById('curr-user-cell').innerText =
	      JSON.stringify(googleUser, undefined, 2);
	    document.getElementById('signed-in-cell').innerText =
	      auth2.isSignedIn.get();

	    updateGoogleUser();
	  }
	}
	
	</script>
</head>
<body>

<div id="login" class="g-signin2" data-onsuccess="onSignIn"></div>

<div id="logout" style="display: none;">
    <input type="button" onclick="signOut();" value="로그아웃" /><br>

    <img id="upic" src=""><br>
    <span id="uname"></span>
</div>


</body>
</html>

