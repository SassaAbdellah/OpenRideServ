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


   <h1>OAuth with facebook, Authorization Request</h1>
   
   <a href="/facebooklogin"> startpage </a>

	<form action="${oauth2Constants.getOAuth2URL()}" method="get">
  
   	<h2>client_id  :</h2>
   	<p> 
    	<input name="client_id" size="50"  value="${oauth2Constants.getClientId()}" />
    </p>
    
    <h2>redirect_uri :</h2>
    <p> 
    	<input name="redirect_uri"  size="50"  value="${oauth2Constants.getBaseURL()}step1.jsp" />
    </p>
    
   
    
    <h2>state :</h2>
	<p>
     <input name="state"  value="<%=request.getParameter("state")%>" />
   </p>
    
    
     <h2>scope :</h2>
    <p>
    	<input name="scope"  size="50"   />
    </p>
    <p>
    	some examples: <br/>
        public_profile,email,publish_actions,user_friends
    </p>
    <p>
      complete list of permissions is 
      <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.1" target="_blank" >
      here
      </a>
    </p>
    
    
    <button type="submit"> Submit to facebook for Authorization Token </button>
 </form>
 




</p>






</body>
</html>