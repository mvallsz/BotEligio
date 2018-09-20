<script type="text/javascript" src="js/jquery.itextclear.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('input[type=text], input[type=password], input[type=url], input[type=email], input[type=number], textarea', '.form').iTextClear();
    });
</script>
<h1 class="page-title">Form Sample</h1>
<div class="container_12 clearfix leading">
    <div class="grid_12">
        <div class="form has-validation">
            <div class="clearfix">
                <label for="form-nombre" class="form-label">Nombre del Servicio<em>*</em></label>
                <div class="form-input"><input type="text" id="form-nombre" name="name" required="required" placeholder="Enter name service" /></div>
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
                <label for="form-bureau" class="form-label">Bureaus</label>
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
                        <option value="0" disabled selected>Select Option</option>
                        <option value="1">SOAP</option>
                        <option value="2">REST</option>
                    </select>
                </div>
            </div>
            <div class="clearfix">
                <label for="form-url" class="form-label">URL<em>*</em></label>
                <div class="form-input"><input type="text" id="form-url" name="name" required="required" placeholder="Enter url service" /></div>
            </div>
            <div class="clearfix">
                <label for="form-tipoVigencia" class="form-label">Tipo de Vigencia</label>
                <div class="form-input">
                    <select id="form-tipoWs">
                        <option value="0" disabled selected>Select Option<em>*</em></option>
                        <option value="1">Cantidad de Dias</option>
                        <option value="2">Dias de la semana</option>
                    </select>
                </div>
            </div>
            <div class="clearfix">
                <label for="form-diaVigencia" class="form-label">Dia de Vigencia<em>*</em></label>
                <div class="form-input">
                    <select id="form-diaVigencia">
                        <option value="0" disabled selected>Select Option</option>
                        <option value="1">Lunes</option>
                        <option value="2">Martes</option>
                        <option value="3">Miercoles</option>
                        <option value="4">Jueves</option>
                        <option value="5">Viernes</option>
                        <option value="6">Sabado</option>
                        <option value="6">Domingo</option>
                    </select>
                </div>
            </div>
            <div class="clearfix">
                <label for="form-cantDias" class="form-label">Cantidad de Dias<em>*</em></label>
                <div class="form-input"><input type="number" id="form-cantDias" name="name" required="required" placeholder="Enter number" /></div>
            </div>
            <div class="clearfix">
                <label for="form-xml" class="form-label">XML<em>*</em></label>
                <div class="form-input form-xml"><textarea id="form-xml" required="required" rows="5" placeholder="Enter XML from service"></textarea></div>
            </div>
            <div class="clearfix">
                <label for="form-variable1" class="form-label">Variable 1<em>*</em></label>
                <div class="form-input">
                    <input type="text" id="form-cantDiaslabel1" name="name" required="required" placeholder="Enter name variable" />
                    <input type="text" id="form-cantDiasvar1" name="name" required="required" placeholder="Enter variable" />
                </div>
            </div>
            <div id="addVariables"></div>
            <div class="clearfix">
                <button class="button" >Añadir Variable</button>
            </div>
            <div class="form-action clearfix">
                <button class="button" type="submit">OK</button>
                <button class="button" type="reset">Reset</button>
            </div>
        </div>
    </div>
</div>