<%@page import="cl.HBES.soporte.DEF"%>
<%@page import="cl.HBES.soporte.Soporte"%>
<%
    if (!Soporte.isSesionActiva(request)) {
        response.sendRedirect("cmd");
        return;
    }
%>
<script type="text/javascript" src="js/jquery.itextclear.js"></script>
<script type="text/javascript" src="js/script/search.js"></script>
<script type="text/javascript" src="js/jquery.Rut.js"></script>
<script type="text/javascript" src="js/script/funciones.js"></script>
<script type="text/javascript">

</script>
<style type = "text/css">
    .textRut{
        padding: 0.6em;
        border: 1px solid #FFF;
        line-height: 22px;
        display: block;
        font-size: 18px;
    }
</style>
<h1 class="page-title">Search Rut</h1>
<div class="container_12 clearfix leading ocult">
    <div class="grid_12">
        <form class="form has-validation">
            <div class="clearfix">
                <label for="form-empresaS" class="form-label">Empresa<em>*</em></label>
                <div class="form-input">
                    <select id="form-empresaS">
                        <option value="0" selected disabled>Select Option</option>
                    </select>
                </div>
            </div>
            <div class="clearfix">
                <label for="form-rut" class="form-label">Rut <em>*</em></label>
                <div class="form-group textRut">
                    <input id="form-rut" class="form-control" placeholder="12345678" type="text" maxlength="10" 
                           onkeyup="formatoNumero(this); formatRut(this, event, 'form-dv');addSerr();" 
                           onchange="formatoNumero(this); formatRut(this, event, 'form-dv');addSerr();">
                    <span class="input-group-addon">-</span>
                    <input disabled id="form-dv" class="form-control" type="text" style="width: 40px;"> 
                </div>
            </div>
            <br>
            <div class="clearfix">
                <label for="form-servicios" class="form-label">Servicio(s)<em>*</em></label>
                <label style="font-size: 12px;"><input type="checkbox" id="form-serv-0"/> Seleccionar Todo</label><br><br>
                <div class="checkgroup" id="addServ" style="margin-left: 35%;"></div>
            </div>
        </form>
        <br>
        <br>
        <div class="clearfix" style="text-align: center;">
            <button id="buscarRut" class="button" onclick="searchRut(this);">Consultar</button>
            <button class="button" id="resetS" type="button" onclick="limpiar()">Reset</button>
        </div>
    </div>
</div>
<div class="message info" id="infoS" style="display:none;">
    <h3>Information</h3>
    <p></p>
    <br>
</div>
<div class="message error" id="errorS" style="display:none;">
    <span class="message-close"></span>
    <h3>Error!</h3>
    <p></p>
    <br>
</div>
<div class="container_12 clearfix leading">
    <div class="grid_12">
        <div class="clearfix" style="text-align: right;">
            <button id="atrasS" class="button" onclick="volverSearch();" style="display: none;">Realizar Nueva Consultar</button>
        </div>
        <div class="clearfix" id="addt" style="display:none;"> 
        </div>
    </div>
</div>