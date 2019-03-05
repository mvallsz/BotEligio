<%@page import="cl.HBES.soporte.DEF"%>
<%@page import="cl.HBES.soporte.Soporte"%>
<%
    if (!Soporte.isSesionActiva(request)) {
        response.sendRedirect("cmd");
        return;
    }
%>
<script type="text/javascript" src="js/jquery.itextclear.js"></script>
<script type="text/javascript">
//    $(document).ready(function () {
//        $('input[type=text], input[type=password], input[type=url], input[type=email], input[type=number], textarea', '.form').iTextClear();
//    });
</script>
<h1 class="page-title">Web Service Generation</h1>
<div class="container_12 clearfix leading">
    <div class="grid_12">
        <div class="form has-validation box recorrer">
            <div class="clearfix" style="display: none;">
                <label for="form-empresao" class="form-label">Empresa <em>*</em></label>
                <div class="form-input">
                    <select id="form-empresao">
                        <option value="0" selected disabled>Select Option</option>
                    </select>
                </div>
            </div>
            <div class="clearfix">
                <label for="form-usuarioo1" class="form-label">Usuario 1<em>*</em></label>
                <div class="form-input">
                    <select id="form-usuarioo1" onchange="veriUser(this);">
                        <option value="0" selected disabled>Select Option</option>
                    </select>
                </div>
            </div>
            <div id="addUsuario"></div>
            <div class="clearfix">
                <br>
                <button class="button" onclick="AddUser()" id="btnAgregarUser">Añadir Usuario</button>
                <button class="button" onclick="ResUser()" id="btnEliminarUser" style="display: none">Eliminar Usuario</button>
            </div>
            <br>
            <div class="clearfix">
                <label for="form-nombreWs" class="form-label">Nombre<em>*</em></label>
                <div class="form-input"><input type="text" id="form-nombreWs" name="name" required="required" placeholder="Busqueda de Morosidades" /></div>
            </div>
            <br>
            <div class="clearfix" id="text-arbol">
                <label for="form-nombreWs" class="form-label">Selección de  variables<em>*</em></label>
            </div>
            <div class="clearfix">
                <div id="arbolOrigen">
                </div>
            </div>
            <div class="form has-validation" style="display: none;text-align: center;" id="form-arbol">
                <section class="grid_12"> 
                    <div class="message info">
                        <h3><em>*</em> Los Servicios no se encuentran disponibles, diríjase a <span style="font-size: 14px;font-weight: 700;">'Data Source Integration'</span> para poder activarlos.</h3> 
                    </div>
                </section>
            </div>
            <br>
            <div class="form-action clearfix">
                <button class="button" type="button" onclick="guardarDatos();">OK</button>
                <button class="button" type="reset" onclick="limpiarDatos();">Reset</button>
            </div>
        </div>
        <br>
        <div class="form has-validation" style="display: none;text-align: center;" id="form-URLW">
            <section class="grid_12"> 
                <div class="message info">
                    <h3><em>*</em> Para acceder al Web Service creado debe dirigirse a la ruta indicada abajo <span style="font-size: 10px;">(Debe reemplazar los '-')</span></h3> 
                    <p id="form-urlWeb" style="font-size: 15px;font-weight: bold;"> </p> 
                </div>
            </section>
        </div>
    </div>
</div>
<br>
<div class="container_12 clearfix leading">
    <div class="grid_12">
        <table class="display" id="tblWebSer"> 
            <thead>
                <tr>
                    <th >Nombre Web Service</th>
                    <th >Ruta</th>
                    <th >Usuarios</th>
                </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
        <div id="popover-div" class="popover">
            <header>Usuarios</header>
            <section>
                <div class="content">
                    <nav>
                        <ul id="modal-web"></ul>
                    </nav>
                </div>
            </section>
        </div>
    </div>
</div>

<script type="text/javascript" src="js/script/output.js"></script>
<script type="text/javascript" src="js/script/funciones.js"></script>