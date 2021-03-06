<%@page import="cl.HBES.soporte.DEF"%>
<%@page import="cl.HBES.soporte.Soporte"%>
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
<h1 class="page-title">Creation of Connections</h1>
<div class="container_12 clearfix leading">
    <div class="grid_12">
        <div class="form has-validation">
            <div class="clearfix">
                <label for="form-nombre" class="form-label">Nombre del Servicio<em>*</em></label>
                <div class="form-input"><input type="text" id="form-nombre" name="name" required="required" placeholder="Servicio de Consultas" /></div>
            </div>
            <div class="clearfix">
                <label for="form-tipoPersona" class="form-label">Tipo Persona<em>*</em></label>
                <div class="form-input">
                    <select id="form-tipoPersona">
                        <option value="0" disabled selected>Select Option</option>
                        <option value="1">Natural</option>
                        <option value="2">Juridica</option>
                        <option value="3">Ambas</option>
                    </select>
                </div>
            </div>
            <div class="clearfix">
                <label for="form-bureau" class="form-label">Bureaus<em>*</em></label>
                <div class="form-input">
                    <select id="form-bureau">
                        <option value="0" disabled selected>Select Option</option>
                        <option value="1">Sinacofi</option>
                        <option value="2">Equifax</option>
                        <option value="3">Trasunion</option>
                        <option value="4">Siisa</option>
                    </select>
                </div>
            </div>
            <div class="clearfix">
                <label for="form-tipoWs" class="form-label">Tipo de Servicio<em>*</em></label>
                <div class="form-input">
                    <select id="form-tipoWs">
                        <option value="1" selected>SOAP</option>
                        <option value="2">REST</option>
                    </select>
                </div>
            </div>
            <div class="clearfix">
                <label for="form-url" class="form-label">URL<em>*</em></label>
                <div class="form-input"><input type="text" id="form-url" name="name" required="required" placeholder="http://www.webservicex.com/globalweather.asmx?wsdl" /></div>
            </div>
            <div class="clearfix">
                <label for="form-tipoVigencia" class="form-label">Tipo de Vigencia<em>*</em></label>
                <div class="form-input">
                    <select id="form-tipoVigencia">
                        <option value="1" selected>Cantidad de Dias</option>
                        <option value="2">Dias de la semana</option>
                    </select>
                </div>
            </div>
            <div class="clearfix" id="divCantDias">
                <label for="form-cantDias" class="form-label">Cantidad de Dias<em>*</em></label>
                <div class="form-input"><input min="1" max="999" type="number" id="form-cantDias" name="name" required="required" placeholder="365" /></div>
            </div>
            <div class="clearfix" id="divDiasVig" style="display: none">
                <label for="form-diaVigencia" class="form-label">Dia de Vigencia<em>*</em></label>
                <div class="form-input">
                    <select id="form-diaVigencia">
                        <option value="1" selected>Lunes</option>
                        <option value="2">Martes</option>
                        <option value="3">Miercoles</option>
                        <option value="4">Jueves</option>
                        <option value="5">Viernes</option>
                        <option value="6">Sabado</option>
                        <option value="6">Domingo</option>
                    </select>
                </div>
            </div>
            <div class="clearfix" id="divXML">
                <label for="form-xml" class="form-label">XML<em>*</em></label>
                <div class="form-input form-xml"><textarea id="form-xml" required="required" rows="15" placeholder="<?xml version=&#34;1.0&#34;?>&#10;<soap:Envelope xmlns:soap=&#34;http://www.w3.org/2003/05/soap-envelope/&#34; soap:encodingStyle=&#34;http://www.w3.org/2003/05/soap-encoding&#34;>&#10;<soap:Header>&#10;...&#10;</soap:Header>&#10;<soap:Body>&#10;...&#10;<soap:Fault>&#10;...&#10;</soap:Fault>&#10;</soap:Body>&#10;</soap:Envelope>&#10;"></textarea></div>
            </div>
            <div>
                <div class="clearfix">
                    <label for="form-variable1" class="form-label">Variable 1<em>*</em>
                        <div class="checkgroup" >
                            <label style="font-size: 12px;">Rut </label><input type="checkbox" id="form-chkRut1" onchange="EditVarRut(1)"/>
                            <label style="font-size: 12px;">XML Header </label><input type="checkbox" id="form-chkHeader1" onchange="EditVarHeader(1)"/>
                        </div>
                    </label>
                    <div class="form-input">
                        <div id="divCantDiaslabel1"><input type="text" id="form-cantDiaslabel1" name="name" required="required" placeholder="Enter name variable"/></div>
                        <div id="divCantDiasvar1"><input type="text" id="form-cantDiasvar1" name="name" required="required" placeholder="Enter variable"/></div>
                    </div>
                </div>
                <div id="addVariables"></div>
            </div>
            <div class="clearfix">
                <button class="button" onclick="AddVariable()">A�adir Variable</button>
                <button class="button" onclick="ResVariable()" id="btnEliminarVar" style="display: none">Eliminar Variable</button>
            </div>
            <div class="form-action clearfix">
                <button class="button" type="button" onclick="GuardarForm()">OK</button>
                <button class="button" type="reset" onclick="limpiarForm()">Reset</button>
            </div>
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
                        <th >Tipo Persona</th>
                        <th >Bureau</th>
                        <th >Estado</th>
                        <th >Propiedades</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table> 
        </div>
    </div>
</div>

<script type="text/javascript" src="js/script/conectionBureau.js"></script>