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
<h1 class="page-title">Generate Web Service Request</h1>
<div class="container_12 clearfix leading">
    <div class="grid_12">
        <div class="form has-validation box recorrer">
            <div class="clearfix">
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
            <div class="clearfix">
                <label for="form-origen1" class="form-label">Origen<em>*</em></label>
                <div class="form-input">
                    <select id="form-origen1" onchange="listarServicio('form-servicio1_1', 1);">
                        <option value="0" selected disabled>Select Option</option>
                    </select>
                </div>
            </div>
            <div id="ser1" class="servVar">
                <div class="clearfix">
                    <label for="form-servicio1_1" class="form-label">Servicio<em>*</em></label>
                    <div class="form-input">
                        <select id="form-servicio1_1" class="ser" onchange="listarVariables('form-variable1_1_1', 1);">
                            <option value="0" selected disabled>Select Option</option>
                        </select>
                    </div>
                </div>
                <div id="var1_1" class="servVar">
                    <div class="clearfix">
                        <label for="form-variable1_1_1" class="form-label">Variable<em>*</em></label>
                        <div class="form-input">
                            <select id="form-variable1_1_1" class="var" onchange="listarVariablesHijo('form-Hijo1_1_1_1', 1);">
                                <option value="0" selected>Todo</option>
                            </select>
                        </div>
                    </div>
                    <div id="hijo1_1_1" style="display: none;" class="servVar">
                        <div class="clearfix">
                            <label for="form-Hijo1_1_1_1" class="form-label">Variable Hijo<em>*</em></label>
                            <div class="form-input">
                                <select id="form-Hijo1_1_1_1" class="hijo" onchange="verificarHijo();">
                                    <option value="0" selected>Todo</option>
                                </select>
                            </div>
                        </div>
                        <div id="addHijos1_1_1"></div>
                        <br>
                        <div class="clearfix" id="buttonVarHijo1_1_1" style="display: none;">
                            <button class="button" onclick="AddVariableHijo()">Añadir Variable Hijo</button>
                            <button class="button" onclick="ResVariableHijo()" id="btnEliminarHijo1_1_1" style="display: none">Eliminar Variable Hijo</button>
                        </div>
                    </div>
                    <div id="addvariables1_1"></div>
                    <div class="clearfix" id="buttonVar1_1" style="display: none;">
                        <br>
                        <button class="button" onclick="AddVariable()">Añadir Variable</button>
                        <button class="button" onclick="ResVariable()" id="btnEliminarVar1_1" style="display: none">Eliminar Variable</button>
                    </div>
                </div>
                <br>
                <div id="addservicio1"></div>
                <div class="clearfix" id="buttonSer1" style="display: none;">
                    <br> 
                    <button class="button" onclick="AddServicio()">Añadir Servicio</button>
                    <button class="button" onclick="ResServicio()" id="btnEliminarServ1" style="display: none">Eliminar Servicio</button>
                </div>
            </div>
            <div id="addoutput"></div>
            <br>
            <div class="clearfix">
                <button class="button" onclick="AddOutput()">Añadir Origen</button>
                <button class="button" onclick="ResOutput()" id="btnEliminar" style="display: none">Eliminar Origen</button>
            </div>
            <div class="form-action clearfix">
                <button class="button" type="button" onclick="guardarDatos();">OK</button>
                <button class="button" type="reset" onclick="limpiarDatos();">Reset</button>
            </div>
        </div>
    </div>
</div>
<br><br>

<script type="text/javascript" src="js/script/output.js"></script>
<script type="text/javascript" src="js/script/funciones.js"></script>