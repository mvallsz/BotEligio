<%@page import="cl.HBES.soporte.DEF"%>
<%@page import="cl.HBES.soporte.Soporte"%>
<%
    if (!Soporte.isSesionActiva(request)) {
        response.sendRedirect("cmd");
        return;
    }
%>
<!-- jQplot CSS -->
<link rel="stylesheet" media="screen" href="lib/jqplot/jquery.jqplot.min.css" />
<!-- jQplot CSS END -->

<h1 class="page-title">Dashboard</h1>
<div class="container_12 clearfix leading">
    <section class="grid_12"> 
        <div class="message success">
            <h2 style="margin-bottom: 10px;">Bureau Activos</h2> 
            <div id="divBureauActivo"></div>
            <!--<h3 style="margin-bottom: 0px;">Sinacofi</h3>
                <h3 style="margin-bottom: 0px;">Equifax</h3>
                <h3 style="margin-bottom: 0px;">TransUnion</h3>
                <h3 style="margin-bottom: 0px;">Siisa</h3> -->
        </div>
    </section>
    <section class="portlet grid_12 leading">
        <header>
            <h2>Consultas HBES</h2>
        </header>
        <section>
            <div class="clearfix">
                <div class="grid_4">
                    <label for="form-bureauu" class="form-label">Bureau</label>
                    <div class="form-input">
                        <select id="form-bureauu" onchange="initTables1(1)">
                            <option value="0" selected>Todos</option>
                            <!--                            <option value="1">Sinacofi</option>
                                                        <option value="2">Equifax</option>
                                                        <option value="3">Trasunion</option>
                                                        <option value="4">Siisa</option>-->
                        </select>
                    </div>
                </div>
                <div class="grid_4">
                    <label for="form-mes" class="form-label">Mes</label>
                    <div class="form-input">
                        <select id="form-mes" onchange="initTables1(1)">
                            <option value="1">Enero</option>
                            <option value="2">Febrero</option>
                            <option value="3">Marzo</option>
                            <option value="4">Abril</option>
                            <option value="5">Mayo</option>
                            <option value="6">Junio</option>
                            <option value="7">Julio</option>
                            <option value="8">Agosto</option>
                            <option value="9">Septiembre</option>
                            <option value="10">Octubre</option>
                            <option value="11">Noviembre</option>
                            <option value="12">Diciembre</option>
                        </select>
                    </div>
                </div>
                <div class="grid_4">
                    <label for="form-anio" class="form-label">Año</label>
                    <div class="form-input">
                        <select id="form-anio" onchange="initTables1(1)">

                        </select>
                    </div>
                </div>
            </div>
            <br>
            <div class="clearfix">
                <div class="grid_3">
                    <label for="form-empresa1" class="form-label">Empresa</label>
                    <div class="form-input">
                        <select id="form-empresa1">
                            <option value="0" selected disabled>Select Option</option>
                        </select>
                    </div>
                </div>
            </div>
            <br>
            <table class="display" id="tableConsultas"> 
                <thead>
                    <tr>
                        <th >Nombre Origen</th>
                        <th >Servicio</th>
                        <th >Usuario</th>
                        <th >Fecha</th>
                        <th >Tipo</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </section>
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
<script type="text/javascript" src="js/script/dashborad_comercial.js"></script> 
<script type="text/javascript" src="js/script/funciones.js"></script> 
<!-- jQplot SETUP END -->