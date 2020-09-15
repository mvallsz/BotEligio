<%@page import="DH.DB.soporte.DEF"%>
<%@page import="DH.DB.soporte.Soporte"%>
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
            <h2 style="margin-bottom: 10px;">Servicios Disponibles</h2> 
            <div id="divServDisp">
                <table class="display" id="tableServiciosDisp"> 
                <thead>
                    <tr>
                        <th >Nombre del Servicio</th>
                        <th >Email de notificación</th>
                        <th >Zip Codes</th>
                        <th >Key Words</th>
                        <th >Fecha de Creación</th>
                        <th >Acciones</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
            </div>
        </div>
    </section>
    <section class="grid_12"> 
        <div class="message success">
            <h2 style="margin-bottom: 10px;">Hilos Activos</h2> 
            <div id="divServActivo">
                <table class="display" id="tableServiciosAct"> 
                <thead>
                    <tr>
                        <th >Nombre del Servicio</th>
                        <th >Ejecuciones</th>
                        <th >Zip Codes</th>
                        <th >Key Words</th>
                        <th >Fecha de Creación</th>
                        <th >Acciones</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
            </div>
        </div>
    </section>
    <section class="portlet grid_12 leading">
        <div class="message success">
            <h2 style="margin-bottom: 10px;">Historico de Hilos de Servicios RPA</h2> 
            <div id="divServHistorico">
                <div class="clearfix">
                    <div class="grid_4">
                            <label for="form-bureauu" class="form-label">Servicio RPA</label>
                        <div class="form-input">
                            <select id="form-servRPA" onchange="initTables1(1)">
                                <option value="0" selected>Todos</option>
                            </select>
                        </div>
                    </div>
                </div>
                <br>
                <table class="display" id="tableHistoricoRPA"> 
                    <thead>
                        <tr>
                            <th >Nombre del Servicio</th>
                            <th >Ejecuciones</th>
                            <th >Ejecuciones Exitosas</th>
                            <th >Zip Codes</th>
                            <th >Key Words</th>
                            <th >Fecha de Creación</th>
                            <th >Fecha de Finalización</th>
                        </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>
    </section>
<!--    <section class="portlet grid_12 leading"> 
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
    </section> -->

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