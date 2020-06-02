<%@page import="cl.HBES.soporte.DEF"%>
<%@page import="cl.HBES.soporte.Soporte"%>
<%@page import="cl.HBES.clases.CredencialesUsuario"%>
<%
    if (!Soporte.isSesionActiva(request)) {
        response.sendRedirect("cmd");
        return;
    }
    CredencialesUsuario user = (CredencialesUsuario) Soporte.getUsuarioSesion(request);
%>
<!DOCTYPE html>
<!--[if IE 7 ]>   <html lang="en" class="ie7 lte8"> <![endif] 
[if IE 8 ]>   <html lang="en" class="ie8 lte8"> <![endif] 
[if IE 9 ]>   <html lang="en" class="ie9"> <![endif] 
[if gt IE 9]> <html lang="en"> <![endif]
[if !IE]><! -->
<html lang="en"> 
    <!--<![endif]-->
    <head>
        <!--[if lte IE 9 ]><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><![endif]-->

        <!--iPad Settings--> 
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" /> 
        <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0" />
        <!--Adding "maximum-scale=1" fixes the Mobile Safari auto-zoom bug: http://filamentgroup.com/examples/iosScaleBug/--> 
        <!--iPad Settings End--> 

        <title><%= DEF.TITULO%> <%= DEF.VERSION%></title>

        <!--<link rel="shortcut icon" href="favicon.ico">-->
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css"/>


        <!-- FAVICON ICONS -->
        <link rel="apple-touch-icon" sizes="180x180" href="images/favicon/apple-touch-icon.png">
        <link rel="icon" type="image/png" sizes="32x32" href="images/favicon/favicon-32x32.png">
        <link rel="icon" type="image/png" sizes="16x16" href="images/favicon/favicon-16x16.png">
        <!--<link rel="manifest" href="images/favicon/site.webmanifest">-->
        <link rel="mask-icon" href="images/favicon/safari-pinned-tab.svg" color="#5b7fd5">
        <meta name="msapplication-TileColor" content="#00aba9">
        <meta name="theme-color" content="#ffffff">
        <!-- FAVICON ICONS END -->


        <!--STYLESHEETS-->         
        <link rel="stylesheet" href="css/reset.css" media="screen" />
        <link rel="stylesheet" href="css/grids.css" media="screen" />
        <link rel="stylesheet" href="css/ui.css" media="screen" />
        <link rel="stylesheet" href="css/forms.css" media="screen" />
        <link rel="stylesheet" href="css/device/general.css" media="screen" />
        <!--[if !IE]><!-->
        <link rel="stylesheet" href="css/device/tablet.css" media="only screen and (min-width: 768px) and (max-width: 991px)" />
        <link rel="stylesheet" href="css/device/mobile.css" media="only screen and (max-width: 767px)" />
        <link rel="stylesheet" href="css/device/wide-mobile.css" media="only screen and (min-width: 480px) and (max-width: 767px)" />
        <!--<![endif]-->
        <link rel="stylesheet" href="css/jquery.uniform.css" media="screen" />
        <link rel="stylesheet" href="css/jquery.popover.css" media="screen">
        <link rel="stylesheet" href="css/jquery.itextsuggest.css" media="screen">
        <link rel="stylesheet" href="css/themes/gray/style.css" media="screen" />
        <!--<link rel="stylesheet" href="css/themes/lightblue/style.css" media="screen" />-->
        <link href="css/device/style_tree.css" rel="stylesheet" type="text/css"/>
        <style type = "text/css">
            #loading-container {position: absolute; top:50%; left:50%;}
            #loading-content {width:800px; text-align:center; margin-left: -400px; height:50px; margin-top:-25px; line-height: 50px;}
            #loading-content {font-family: "Helvetica", "Arial", sans-serif; font-size: 18px; color: black; text-shadow: 0px 1px 0px white; }
            #loading-graphic {margin-right: 0.2em; margin-bottom:-2px;}
            /*    #loading {background-color:#abc4ff; 
                          background-image: -moz-radial-gradient(50% 50%, ellipse closest-side, #abc4ff, #87a7ff 100%); 
                          background-image: -webkit-radial-gradient(50% 50%, ellipse closest-side, #abc4ff, #87a7ff 100%); 
                          background-image: -o-radial-gradient(50% 50%, ellipse closest-side, #abc4ff, #87a7ff 100%); 
                          background-image: -ms-radial-gradient(50% 50%, ellipse closest-side, #abc4ff, #87a7ff 100%); 
                          background-image: radial-gradient(50% 50%, ellipse closest-side, #abc4ff, #87a7ff 100%); height:100%; width:100%; overflow:hidden; position: absolute; left: 0; top: 0; z-index: 99999;}*/
            #loading {background-color:#f2f2f2; 
                      background-image: -moz-radial-gradient(50% 50%, ellipse closest-side, #f2f2f2, #8c8c8c 100%); 
                      background-image: -webkit-radial-gradient(50% 50%, ellipse closest-side, #f2f2f2, #8c8c8c 100%); 
                      background-image: -o-radial-gradient(50% 50%, ellipse closest-side, #f2f2f2, #8c8c8c 100%); 
                      background-image: -ms-radial-gradient(50% 50%, ellipse closest-side, #f2f2f2, #8c8c8c 100%); 
                      background-image: radial-gradient(50% 50%, ellipse closest-side, #f2f2f2, #8c8c8c 100%); 
                      height:100%; width:100%; overflow:hidden; position: absolute; left: 0; top: 0; z-index: 99999;}
            .agregarConexion{
                font-weight: 700;
            }
            .new{
                display: none;
            }
            .origen {
                border-bottom-color: #FFF;
                margin-bottom: -1px;
                border: 1px solid #CCC;
                border-left-width: 0;
                border-bottom-width: 0;
                font-family: Arial, Helvetica, sans-serif;
                font-size: 14px;
                font-weight: bold;
                line-height: 11px;
                overflow: hidden;
                padding: 10px 20px;
                text-overflow: ellipsis;
                text-shadow: 0 1px 0 #FFF;
                white-space: nowrap;
                zoom: 1;
            }
            .servVar{
                margin-left: 66px;
            }
        </style>
        <!--STYLESHEETS END--> 

        <!--        [if lt IE 9]>
                <script src="js/html5.js"></script>
                <script type="text/javascript" src="js/selectivizr.js"></script>
                <![endif]-->

    </head>
    <body style="overflow: hidden;">
        <div id="loading"> 
            <script type = "text/javascript">
                document.write("<div id='loading-container'><p id='loading-content'>" +
                        "<img id='loading-graphic' width='16' height='16' src='images/ajax-loader-trans.gif' /> " +
                        "Loading...</p></div>");
            </script>
        </div> 

        <div id="wrapper">
            <header>
                <h1><a href="#">vPad</a></h1>
                <nav>
                    <div class="container_12">
                        <div class="grid_12">
                            <button class="button fr" id="CerrarSesion">Sign Out</button>
                            <!--<a href="login.jsp" title="Logout" class="button icon-with-text fr"><img src="images/navicons-small/129.png" alt=""/>Logout</a>-->
                            <!--                            <div class="user-info fr">
                                                            Logged in as Administrator<a href="#">Administrator</a>
                                                        </div>-->
                        </div>
                    </div>
                </nav>
            </header>

            <section>
                <!--Sidebar--> 

                <aside>
                    <nav class="drilldownMenu">
                        <!--                                                <h1>
                                                                            <span class="title">Main Menu</span>
                                                                            <button title="Go Back" class="back">Back</button>
                                                                        </h1>-->
                        <ul class="tlm">
                            <li class="current"><a href="#dashboard_comercial.jsp" title="Dashboard"><img src="images/navicons/81.png" alt=""/><span>Dashboard</span></a></li>
                            <li class="current"><a href="#conectionBureau_comercial.jsp" title="Data Source Integration"><img src="images/navicons/130.png" alt=""/><span>Data Source Integration</span></a></li>
                            <li class="current"><a href="#user.jsp" title="Web Service Users"><img src="images/navicons/112.png" alt=""/><span>Web Service Users</span></a></li>
                            <li class="current"><a href="#output_ws.jsp" title="Web Service Generation"><img src="images/navicons/122.png" alt=""/><span>Web Service Generation</span></a></li>
                            <li class="current"><a href="#search.jsp" title="Search"><img src="images/navicons/06.png" alt=""/><span>Search</span></a></li>
                        </ul>
                    </nav>
                </aside>

                <!--Sidebar End--> 

                <section>
                    <header>
                        <div class="container_12 clearfix">
                            <a href="#menu" class="showmenu button">Menu</a>
                            <h1 class="grid_12">Dashboard</h1>
                        </div>
                    </header>
                    <section id="main-content" class="clearfix">
                    </section>
                    <footer class="clearfix">
                        <div class="container_12">
                            <div class="grid_12">
                                Copyright &copy; 2018. by <a target="_blank" href="https://www.expert-choice.com/">Expert-Choice</a>
                            </div>
                        </div>
                    </footer>
                </section>

                <!--Main Section End--> 
            </section>
        </div>

        <!--MAIN JAVASCRIPTS--> 
        <script src="//code.jquery.com/jquery-1.7.min.js"></script>
        <!--<script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.js"></script>-->
        <script>window.jQuery || document.write("<script src='js/jquery.min.js'>\x3C/script>")</script>
        <script type="text/javascript" src="js/jquery.tools.min.js"></script>
        <script type="text/javascript" src="js/jquery.uniform.min.js"></script>
        <script type="text/javascript" src="js/jquery.easing.js"></script>
        <script type="text/javascript" src="js/jquery.ui.totop.js"></script>
        <script type="text/javascript" src="js/jquery.itextsuggest.js"></script>
        <script type="text/javascript" src="js/jquery.itextclear.js"></script>
        <script type="text/javascript" src="js/jquery.hashchange.min.js"></script>
        <script type="text/javascript" src="js/jquery.drilldownmenu.js"></script>
        <script type="text/javascript" src="js/jquery.popover.js"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="js/script/funciones.js"></script>
        <!--[if lt IE 9]>-->
        <!--<script type="text/javascript" src="js/PIE.js"></script>-->
        <!--<script type="text/javascript" src="js/ie.js"></script>-->
        <!--<![endif]-->

        <script type="text/javascript" src="js/global.js"></script>
        <!--MAIN JAVASCRIPTS END--> 

        <!--LOADING SCRIPT--> 
        <script>
                $(window).load(function () {
                    $("#loading").fadeOut(function () {
                        $(this).remove();
                        $('body').removeAttr('style');
                    });
                });
                var codigo = <%=user.getEmpresa().getCod_subsidiary()%>
                var idEmp = <%=user.getEmpresa().getId()%>
        </script>
        <!--         LOADING SCRIPT 
        
                 POPOVERS SETUP-->
        <div id="activity-popover" class="popover">
            <header>
                Activity
            </header>
            <section>
                <div class="content">
                    <nav>
                        <ul>
                            <li class="new"><a><span class="avatar"></span>John Doe created a new project</a></li>
                            <li class="read"><a><span class="avatar"></span>John Doe created a new project</a></li>
                            <li class="read"><a><span class="avatar"></span>Jane Doe updated a project</a></li>
                            <li class="read"><a><span class="avatar"></span>John Doe uploaded a document</a></li>
                            <li class="read"><a><span class="avatar"></span>John Doe deleted a project</a></li>
                            <li class="read"><a><span class="avatar"></span>John Doe marked a project as done</a></li>
                        </ul>
                    </nav>
                </div>
            </section>
        </div>
        <div id="notifications-popover" class="popover">
            <header>
                Notifications
            </header>
            <section>
                <div class="content">
                    <nav>
                        <ul>
                            <li class="new"><a><span class="avatar"></span>John Doe created a new project</a></li>
                            <li class="new"><a><span class="avatar"></span>John Doe created a new project</a></li>
                            <li class="new"><a><span class="avatar"></span>Jane Doe updated a project</a></li>
                            <li class="read"><a><span class="avatar"></span>John Doe uploaded a document</a></li>
                            <li class="read"><a><span class="avatar"></span>John Doe deleted a project</a></li>
                            <li class="read"><a><span class="avatar"></span>John Doe marked a project as done</a></li>
                            <li><a href="#notifications.html" title="Notifications">See notification styles and growl like messages...</a></li>
                        </ul>
                    </nav>
                </div>
            </section>
        </div>
        <div id="settings-popover" class="popover">
            <header>
                Settings
            </header>
            <section>
                <div class="content">
                    <nav>
                        <ul>
                            <li><a>Project Settings</a></li>
                            <li><a>Account Settings</a></li>
                        </ul>
                    </nav>
                </div>
            </section>
        </div>
        <script>
            $(document).ready(function () {
                $('#activity-button').popover('#activity-popover', {preventRight: true});
                $('#notifications-button').popover('#notifications-popover', {preventRight: true});
                $('#settings-button').popover('#settings-popover', {preventRight: true});

                /**
                 * setup search
                 */
                function googleSearch(q) {
                    $('#searchform .searchbox a').fadeOut()
                    $.ajax({
                        url: 'php/google_search_results.php',
                        data: 'q=' + encodeURIComponent(q),
                        cache: false,
                        success: function (response) {
                            $('.search_results').html(response);
                        }
                    });
                }

                // Set iTextSuggest
                $('#searchform .searchbox').length && $('#searchform .searchbox').find('input[type=text]').iTextClear().iTextSuggest({
                    url: 'php/google_suggestions_results.php',
                    onKeydown: function (query) {
                        googleSearch(query);
                    },
                    onChange: function (query) {
                        googleSearch(query);
                    },
                    onSelect: function (query) {
                        googleSearch(query);
                    },
                    onSubmit: function (query) {
                        googleSearch(query);
                    },
                    onEmpty: function () {
                        $('.search_results').html('');
                    }
                }).focus(function () {
                    $('#wrapper > section > aside > nav > ul').fadeOut(function () {
                        $('#searchform .search_results').show();
                    });
                    $(this).parents('#searchform .searchbox').animate({marginRight: 70}).next().fadeIn();
                });

                $('#searchform .searchcontainer').find('input[type=button]').click(function () {
                    $('#searchform .search_results').hide();
                    $('#searchform .searchbox').find('input[type=text]').val('');
                    $('#searchform .search_results').html('');
                    $('#wrapper > section > aside > nav > ul').fadeIn();
                    $('.searchbox', $(this).parent()).animate({marginRight: 0}).next().fadeOut();
                });
            });
            $('#CerrarSesion').click(function () {
                go('Svl_Usuario', [{id: 'accion', val: 'CerrarSession'}], undefined, 'Svl_Usuario');
            });
        </script>
        <!--POPOVERS SETUP END-->

    </body>
</html>