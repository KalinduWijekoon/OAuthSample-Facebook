<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>
    </head>
    <body>
        <script type="text/javascript">
            function makereq(){
                var authendpoint = "https://www.facebook.com/dialog/oauth";
                var responsetype = "code";
                var appid = "1789938591061725";
                var redirecturi = "https://localhost:8443/OAuthApp/callback";
                var scope = "public_profile email groups_access_member_info publish_to_groups user_age_range user_birthday user_events user_friends user_gender user_hometown user_likes user_link user_location user_photos user_posts user_tagged_places user_videos";   
                var requestEndpoint = authendpoint + "?" + "response_type=" + encodeURIComponent(responsetype) + "&" + "client_id=" + encodeURIComponent(appid) + "&" + "redirect_uri=" + encodeURIComponent(redirecturi) + "&" + "scope=" + encodeURIComponent(scope);
                window.location.href = requestEndpoint;
            }
        </script>
        <button id="goButton" type="button" onclick="makereq()" >Click To Check Friends Count !</button>
    </body>
</html>
