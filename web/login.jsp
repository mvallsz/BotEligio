<%@page import="DH.DB.soporte.DEF"%>
<%@page import="DH.DB.soporte.Soporte"%>
<%
    if (Soporte.isSesionActiva(request)) {
        response.sendRedirect("cmd");
        return;
    }
%>
<!doctype html>
<!--[if IE 7 ]>   <html lang="en" class="ie7 lte8"> <![endif]--> 
<!--[if IE 8 ]>   <html lang="en" class="ie8 lte8"> <![endif]--> 
<!--[if IE 9 ]>   <html lang="en" class="ie9"> <![endif]--> 
<!--[if gt IE 9]> <html lang="en"> <![endif]-->
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <!--[if lte IE 9 ]><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><![endif]-->

        <!-- iPad Settings -->
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" /> 
        <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0" />
        <!-- Adding "maximum-scale=1" fixes the Mobile Safari auto-zoom bug: http://filamentgroup.com/examples/iosScaleBug/ -->
        <!-- iPad End -->

        <title><%= DEF.TITULO%>&nbsp;<%= DEF.VERSION%></title>

        <link rel="shortcut icon" href="favicon.ico">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css"/>

        <!-- FAVICON ICONS -->
        <link rel="apple-touch-icon" sizes="180x180" href="images/favicon/apple-touch-icon.png">
        <link rel="icon" type="image/png" sizes="32x32" href="images/favicon/favicon-32x32.png">
        <link rel="icon" type="image/png" sizes="16x16" href="images/favicon/favicon-16x16.png">
        <!--<link rel="manifest" href="images/favicon/site.webmanifest">-->
        <link rel="mask-icon" href="images/favicon/safari-pinned-tab.svg">
        <meta name="msapplication-TileColor" content="#00aba9">
        <meta name="theme-color" content="#ffffff">

        <!-- STYLESHEETS -->

        <link rel="stylesheet" media="screen" href="css/reset.css" />
        <link rel="stylesheet" media="screen" href="css/grids.css" />
        <link rel="stylesheet" media="screen" href="css/style.css" />
        <link rel="stylesheet" media="screen" href="css/ui.css" />
        <link rel="stylesheet" media="screen" href="css/jquery.uniform.css" />
        <link rel="stylesheet" media="screen" href="css/forms.css" />
        <link rel="stylesheet" media="screen" href="css/themes/lightblue/style.css" />

        <style type = "text/css">
            #loading-container {position: absolute; top:50%; left:50%;}
            #loading-content {width:800px; text-align:center; margin-left: -400px; height:50px; margin-top:-25px; line-height: 50px;}
            #loading-content {font-family: "Helvetica", "Arial", sans-serif; font-size: 18px; color: black; text-shadow: 0px 1px 0px white; }
            #loading-graphic {margin-right: 0.2em; margin-bottom:-2px;}
            #loading {background-color:#abc4ff; background-image: -moz-radial-gradient(50% 50%, ellipse closest-side, #abc4ff, #87a7ff 100%); background-image: -webkit-radial-gradient(50% 50%, ellipse closest-side, #abc4ff, #87a7ff 100%); background-image: -o-radial-gradient(50% 50%, ellipse closest-side, #abc4ff, #87a7ff 100%); background-image: -ms-radial-gradient(50% 50%, ellipse closest-side, #abc4ff, #87a7ff 100%); background-image: radial-gradient(50% 50%, ellipse closest-side, #abc4ff, #87a7ff 100%); height:100%; width:100%; overflow:hidden; position: absolute; left: 0; top: 0; z-index: 99999;}
        </style>

        <!-- STYLESHEETS END -->

        <!--[if lt IE 9]>
        <script src="js/html5.js"></script>
        <script type="text/javascript" src="js/selectivizr.js"></script>
        <![endif]-->

    </head>
    <body class="login" style="overflow: hidden;">
        <div id="loading"> 

            <script type = "text/javascript">
                document.write("<div id='loading-container'><p id='loading-content'>" +
                        "<img id='loading-graphic' width='16' height='16' src='images/ajax-loader-abc4ff.gif' /> " +
                        "Loading...</p></div>");
            </script> 

        </div> 

        <div class="login-box">
            <section class="login-box-top">
                <header>
                    <h2 class="logo ac"></h2>
                </header>
                <section>
                    <form id="form" class="has-validation" action="index.html" method="post" style="margin-top: 20px">
                        <div class="user-pass">
                            <input type="text" id="username" class="full" value="" name="username" required="required" placeholder="Username" />
                            <input type="password" id="password" class="full" value="" name="password" required="required" placeholder="Password" />
                        </div>
                        <p class="clearfix">
                            <span class="fl" style="line-height: 23px;">
                                <label class="choice" for="remember">
                                    <input type="checkbox" id="remember" class="" value="1" name="remember"/>
                                    Mantener mi sesi�n abierta. 
                                </label>
                            </span>

                            <button class="fr" type="submit" id="btnEntrar">Login</button>
                        </p>
                    </form>
                </section>
                <section>
                    <span id="msgLogin" class="label label-warning"></span>
                </section>
            </section>
        </div>

        <!-- MAIN JAVASCRIPTS -->
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.js"></script>
        <script>window.jQuery || document.write("<script src='js/jquery.min.js'>\x3C/script>")</script>
        <script type="text/javascript" src="js/jquery.tools.min.js"></script>
        <script type="text/javascript" src="js/jquery.uniform.min.js"></script>
        <script type="text/javascript" src="js/jquery.easing.js"></script>
        <script type="text/javascript" src="js/jquery.ui.totop.js"></script>
        <script type="text/javascript" src="js/script/login.js"></script>
        <script type="text/javascript" src="js/script/funciones.js"></script>
        <!--[if lt IE 9]>
        <script type="text/javascript" src="js/PIE.js"></script>
        <script type="text/javascript" src="js/ie.js"></script>
        <![endif]-->

        <script type="text/javascript" src="js/global.js"></script>
        <!-- MAIN JAVASCRIPTS END -->

        <!-- LOADING SCRIPT -->
        <script>
                $(window).load(function () {
                    $("#loading").fadeOut(function () {
                        $(this).remove();
                        $('body').removeAttr('style');
                    });
                });
        </script>
        <!-- LOADING SCRIPT -->

    </body>
</html>