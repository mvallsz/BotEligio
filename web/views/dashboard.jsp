<!-- jQplot CSS -->
<link rel="stylesheet" media="screen" href="lib/jqplot/jquery.jqplot.min.css" />
<!-- jQplot CSS END -->

                <h1 class="page-title">Dashboard</h1>
                <div class="container_12 clearfix leading">
                    <section class="grid_12"> 
                        <div class="message success">
                            <h2 style="margin-bottom: 10px;">Bureau Activos</h2> 
                            <h3 style="margin-bottom: 0px;">Sinacofi</h3> 
                            <h3 style="margin-bottom: 0px;">Equifax</h3>
                            <h3 style="margin-bottom: 0px;">TransUnion</h3> 
                            <h3 style="margin-bottom: 0px;">Siisa</h3> 
                        </div>
                    </section>
                    <section class="portlet grid_12 leading"> 
                        <header>
                            <h2>Consultas Bureau</h2>
                        </header>
                        <section>
                            <div class="jqPlot" id="chart1" style="width:100%;height:250px;"></div>
                        </section>
                    </section>
                    
                    <section class="portlet grid_12 leading"> 
                        <header>
                            <h2>Bureau vs Cache</h2>
                        </header>
                        <section>
                            <div class="jqPlot" id="chart2" style="width:100%;height:200px;"></div>
                        </section>
                    </section> 
                    
                    <section class="portlet grid_6 leading"> 
                        <header>
                            <h2>Porcentaje de Almacenamiento</h2>
                        </header>
                        <section>
                            <div class="jqPlot" id="chart3" style="width:100%;height:300px;"></div>
                        </section>
                    </section> 
                    
                </div>

    <!-- jQplot SETUP -->
    <!--[if lt IE 9]>
    <script type="text/javascript" src="lib/jqplot/excanvas.js"></script>
    <![endif]-->
    <script type="text/javascript" src="lib/jqplot/jquery.jqplot.min.js"></script>
    <script type="text/javascript" src="lib/jqplot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
    <script type="text/javascript" src="lib/jqplot/plugins/jqplot.barRenderer.min.js"></script>
    <script type="text/javascript" src="lib/jqplot/plugins/jqplot.categoryAxisRenderer.min.js"></script> 
    <script type="text/javascript" src="lib/jqplot/plugins/jqplot.pieRenderer.min.js"></script>
    <script type="text/javascript" src="js/script/dashborad.js"></script> 
    <!-- jQplot SETUP END -->