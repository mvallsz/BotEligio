<%@page import="cl.HBES.soporte.DEF"%>
<%@page import="cl.HBES.soporte.Soporte"%>
<%
    if (!Soporte.isSesionActiva(request)) {
        response.sendRedirect("cmd");
        return;
    }
%>
<script type="text/javascript" src="js/jquery.itextclear.js"></script>
<script type="text/javascript" src="js/script/user.js"></script>
<script type="text/javascript" src="js/script/funciones.js"></script>
<script type="text/javascript">
//    $(document).ready(function () {
//        $('input[type=text], input[type=password], input[type=url], input[type=email], input[type=number], textarea', '.form').iTextClear();
//    });
</script>
<h1 class="page-title">User for Web Service</h1>
<div class="container_12 clearfix leading">
    <div class="grid_12">
        <form class="form has-validation">
            <div class="clearfix">
                <label for="form-empresau" class="form-label">Empresa<em>*</em></label>
                <div class="form-input">
                    <select id="form-empresau">
                        <option value="0" selected disabled>Select Option</option>
                    </select>
                </div>
            </div>
            <div class="clearfix">
                <label for="form-nombreU" class="form-label">Nombre <em>*</em></label>
                <div class="form-input"><input type="text" id="form-nombreU"  placeholder="nombre" /></div>
            </div>
            <div class="clearfix">
                <label for="form-username" class="form-label">Username <em>*</em></label>
                <div class="form-input"><input type="text" id="form-username" name="username" placeholder="Alphanumeric" /></div>
                <!--<div class="form-input"><input type="text" id="form-username" name="username" maxlength="12" placeholder="Alphanumeric (max 12 char.)" /></div>-->
            </div>
            <div class="clearfix">
                <label for="form-password" class="form-label">Password</label>
                <div class="form-input"><input type="password" id="form-password" name="password" maxlength="30" placeholder="max. 30 char." /></div>
            </div>
            <div class="clearfix">
                <label for="form-password-check" class="form-label">Password check</label>
                <div class="form-input"><input type="password" id="form-password-check" name="check" data-equals="password" maxlength="30" placeholder="Re-enter your password" /></div>
            </div>
        </form>
        <br>
        <div class="clearfix" style="text-align: center;">
            <button id="guardarUser" class="button" onclick="guardarU(this)">OK</button>
            <button style="display: none;" id="ActualizarUser" class="button" onclick="">Actualizar</button>
            <button class="button" type="reset" id="reset" onclick="limpiarU(1)">Reset</button>
        </div>
    </div>
</div>
<br>
<br>
<div class="container_12 clearfix leading">
    <div class="grid_12">
        <table class="display" id="tblUsuarios"> 
            <thead>
                <tr>
                    <th >Nombre Usuario</th>
                    <th >Usuario</th>
                    <th >Estado</th>
                    <th >Acción</th>
                </tr>
            </thead>
            <tbody style="text-align: center;">
            </tbody>
        </table>
    </div>
</div>