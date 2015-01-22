<%@page import="de.acando.facebooklogin.OAuth2Constants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title> Redirected via facebook </title>
</head>
<body>


<jsp:useBean id="oauth2Constants"  class="de.acando.facebooklogin.OAuth2Constants" scope="application"/>


<h1> Inspect Access Token </h1>

<a href="/facebooklogin"> startpage </a>

<form  action="${oauth2Constants.getFacebookInspectAccessTokenUri()}" method="GET">

	 <p>  access_token (token to be inspected) <br/>
	 
	 <textarea name="input_token" cols="50" rows="5"  ></textarea>
	 
	 </p>
	 <p> Application Access Token  </p>
	 <p>
	 ( get it <a href="${oauth2Constants.getFacebookAppAccessTokenUri()}" target="_blank"  > here </a> )
	 </p>
	  <input name="access_token" size="50" />
	 </p>
	 
	
    <button type="submit"> Submit to facebook for Authorization Token </button>
  
    <p> 
</form>
   
 
 
</body>
</html>