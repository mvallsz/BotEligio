<%@page import="DH.DB.soporte.DEF"%>
<%@page import="DH.DB.soporte.Soporte"%>
<%
    if (!Soporte.isSesionActiva(request)) {
        response.sendRedirect("cmd");
        return;
    }
%>
<script type="text/javascript" src="js/jquery.itextclear.js"></script>
<!--<script type="text/javascript" src="js/jquery.min"></script>-->
<script type="text/javascript">
//    $(document).ready(function () {
//        $('input[type=text], input[type=password], input[type=url], input[type=email], input[type=number], textarea', '.form').iTextClear();
//    });
</script>
<h1 class="page-title">Integración de Servicios RPA</h1>
<div class="container_12 clearfix leading">
    <div class="grid_12">
        <div class="form has-validation box">
            <div class="clearfix">
                <label for="form-nombre-rpa" class="form-label">Nombre del servicio RPA<em>*</em></label>
                    <div class="form-input">
                        <input type="text" id="form-nombre-rpa" name="name" required="required" placeholder="The Appliance Repair Men" />
                    </div>
            </div>
            <div class="clearfix">
                <label for="form-US_states" class="form-label">Estado<em>*</em></label>
                <div class="form-input">
                    <select id="form-US_state">
                        <option value="0" selected disabled>Seleccione un Estado</option>
                    </select>
                </div>
            </div>
            <div class="clearfix" id="divCantDias">
                <label for="form-zipcodes" class="form-label">Ingrese los Zip Codes<em>*</em></label>
                <div class="form-input">
                    <input type="text" id="form-zipCodes" name="zipCodes" required="required" placeholder="33130,33187,33166,33179,33351,33472,33177" />
                </div>
            </div>
            <div class="clearfix">
                <label for="form-appliance" class="form-label">Ingrese los Appliances (Key Words)<em>*</em></label>
                <div class="form-input">
                    <input type="text" id="form-appliance" name="appliance" required="required" placeholder="Refrigerator dryer washer diswasher ice machine" />
                </div>
            </div>
            <div class="clearfix">
                <label for="form-correo-notificacion" class="form-label">Ingrese el correo de notificación<em>*</em></label>
                <div class="form-input">
                    <input type="text" id="form-correo-notificacion" name="correo-notificacion" required="required" placeholder="test@gmail.com" />
                </div>
            </div>
            <br>
            <div class="clearfix">
                <label class="form-label" style="font-size: 15px;"><i>Parametros de Entrada del Host RPA</i></label>
            </div>
            <div class="clearfix">
                <label for="form-tipoWs" class="form-label">Usuario del Host<em>*</em></label>
                <div class="form-input">
                    <input type="text" id="form-usuario-host" name="usuario-host" required="required" placeholder="test@gmail.com" />
                </div>
            </div>
            <div class="clearfix">
                <label for="form-tipoWs" class="form-label">Password del Host<em>*</em></label>
                <div class="form-input">
                    <input type="text" id="form-password-host" name="password-host" required="required" placeholder="SFSF3344" />
                </div>
            </div>
            <div class="form-action clearfix">
                <button class="button" type="button" onclick="GuardarForm()">OK</button>
                <button class="button" type="reset" onclick="limpiarForm()">Reset</button>
            </div>
        </div>
        <div class="tabs leading new">
            <ul class="clearfix">
            </ul>
            <section>
                <section class="clearfix">
                    <header class="alpha omega">
                        <h2>New Connection</h2>
                    </header>
                    <div class="form has-validation">
                        <div class="clearfix">
                            <label for="form-origen" class="form-label">Nombre del Origen<em>*</em></label>
                            <div class="form-input"><input type="text" id="form-origen" name="name" required="required" placeholder="Web Service de Consultas" /></div>
                        </div>
                        <div class="form-action clearfix">
                            <button class="button" type="button" onclick="NuevoOrigen()">Agregar</button>
                            <button class="button" type="button" onclick="Cancelar()">Cancelar</button>
                        </div>
                    </div>
                </section>
            </section>
        </div>
    </div>
</div>
<br><br>
<div class="container_12 clearfix leading">
    <div class="grid_12">
        <div id="divTableServicios" class="clearfix"> 
            <table class="display" id="tableServicios"> 
                <thead>
                    <tr>
                        <th >Nombre Servicio</th>
                        <th >Zip Codes</th>
                        <th >Appliance</th>
                        <th >Estado</th>
                        <th >Acciones</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table> 
        </div>
    </div>
</div>

<script type="text/javascript" src="js/script/solicitud_rpa.js"></script>
<script type="text/javascript" src="js/script/funciones.js"></script> 