<%@page import="de.acando.facebooklogin.OAuth2Constants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>OAuth2 Interactive </title>
</head>
<body>


<jsp:useBean id="oauth2Constants"  class="de.acando.facebooklogin.OAuth2Constants" scope="application"/>


   <h1>OAuth with facebook, Graph API </h1>
   
   
   
   <a href="/facebooklogin"> startpage </a>



 <h1> Get User Object </h1>	
 <form action="${oauth2Constants.getGraphApiBaseURLMe()}" method="get">
  
  	 <p>  access_token  <br/>
	 <textarea name="access_token" cols="50" rows="5"  ></textarea>
	 </p>
	 
    <button type="submit"> Submit to facebook Graph API for User Object  </button>
 </form>
 
 
 
 <h1> Get User Object Fields </h1>	
 <form action="${oauth2Constants.getGraphApiBaseURLMe()}" method="get">
 
  	 <p>  access_token  <br/>
	 <textarea name="access_token" cols="50" rows="5"  ></textarea>
	 </p>
	 
	 <p>  fields  <br/>
	 <input name="fields" size="50"  value="email,picture.type(large)" />
	 </p>
    <button type="submit"> Submit to facebook Graph API for User Object  </button>
 </form>

</p>

<p>
 Evil Graph API Explorer is   <a href="https://developers.facebook.com/tools/explorer/"  target="_blank"> here </a>
</p>



</body>
</html>