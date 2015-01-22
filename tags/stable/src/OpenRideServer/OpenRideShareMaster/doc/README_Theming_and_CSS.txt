Theming and CSS Howto

This document describes how to add custom themes and custom 
css to Your local installation of OpenRideShare.

Contents:

=== Adding Your own CSS  to OpenRide ===

 To add Your own CSS to OpenRideShare, 
 move name Your CSS file

 local.styles.css

 and move it to directory

 [OpenRideShareMaster]/local.resources/css 

 Inside of this directory, You will also find a sample file named 
 "local.styles.css.sample", which (surprise!) works as a sample file
 for Your local css. You may consider using this sample as a starting
 point for Your local.styles.css

=== Adding custom themes to OpenRide ===
 
 After Adding Your own CSS to OpenRideShare, You will notice, that 
 "widgets" like lists, accordion, menus... etc will not be affected
 by Your custom styles.
 To customize the widgets according to Your preferences, You'll
 need to install a primefaces theme.

 You may either download an existing primefaces theme from 

 http://primefaces.org  
 
 or create your own theme (as described on primefaces.org).
 When creating Your own theme, be sure to create a theme 
 for jquery-ui  v1.11.0, as this is the version of jquery 
 used in primefaces-5.0 that ships with OpenRideShare.


 Regardless wether You downloaded or created a theme,
 You'll end up with a file named "themeName.jar",
 where "themeName" stands for the name of Your theme.
 
 To install the theme "themeName", copy themeName.jar 
 to directory 


 [OpenRideShareMaster]/local.resources/themes
 
 
 and set the property jquery.ui.theme  in file

 [OpenRideShareMaster]/localconf.properties
 
 to the name of Your theme. e.g:

 
 jquery.ui.theme=yourTheme
 











