var contVar = 1;

console.log("entra");

function ValidaForm() {
    var nombre = $('#form-nombre').val().trim();
    var tipoPersona = $("select#form-tipoPersona option:checked").val();
    var bureau = $("select#form-bureau option:checked").val();
    var tipoServicio = $("select#form-tipoWs option:checked").val();
    var url = $('#form-url').val().trim();
//    var tipoVigencia = $("select#form-tipoVigencia option:checked" ).val();
    var cantDias = $('#form-cantDias').val().trim();
//    var diaVigencia = $("select#form-diaVigencia option:checked" ).val();
    var xml = $('#form-xml').val().trim();
    if (nombre === "") {
        alert('Debe ingresar Nombre del Servicio');
        return false;
    } else if (tipoPersona === "0") {
        alert('Debe seleccionar Tipo de Persona');
        return false;
    } else if (bureau === "0") {
        alert('Debe seleccionar Bureau');
        return false;
    } else if (tipoServicio === "0") {
        alert('Debe seleccionar Tipo de Servicio');
        return false;
    } else if (url === "") {
        alert('Debe ingresar URL');
        return false;
    } else if (cantDias === "") {
        alert('Debe ingresar Cantidad de Dias');
        return false;
    } else if (xml === "") {
        alert('Debe ingresar XML');
        return false;
    }
    for (var i = 1; i <= contVar; i++) {
        var labelVar = $('#form-cantDiaslabel' + i).val().trim();
        var nombreVar = $('#form-cantDiasvar' + i).val().trim();
        if (labelVar === "" && !$('#form-chkHeader'+contVar).is(":checked") && !$('#form-chkRut'+contVar).is(":checked")) {
            alert('Debe ingresar Nombre de la Variable');
            return false;
        } else if (nombreVar === "") {
            alert('Debe ingresar Variable');
            return false;
        }
    }
    return true;
}

function GuardarForm() {
    if (ValidaForm()) {
        var nombre = $('#form-nombre').val().trim();
        var tipoPersona = $("select#form-tipoPersona option:checked").val();
        var bureau = $("select#form-bureau option:checked").val();
        var tipoServicio = $("select#form-tipoWs option:checked").val();
        var url = $('#form-url').val().trim();
        var tipoVigencia = $("select#form-tipoVigencia option:checked").val();
        var cantDias = $('#form-cantDias').val().trim();
        var diaVigencia = $("select#form-diaVigencia option:checked").val();
        var xml = $('#form-xml').val().trim();
        var json = "{";
        for (var i = 1; i <= contVar; i++) {
            if (i > 1) {
                json = json + ",";
            }
            var labelVar = "";
            if($('#form-chkRut'+i).is(":checked")){
                labelVar = $("select#form-diaVigencia"+i+" option:checked").val();
            }else{
                labelVar = $('#form-cantDiaslabel' + i).val().trim();  
            }
            var nombreVar = $('#form-cantDiasvar' + i).val().trim();
            json = json + '\"' + labelVar + '\":\"' + nombreVar + '\"';
        }
        json = json + "}";
        json = JSON.parse(json);
        if (Object.keys(json).length < contVar) {
            alert("Nombre de Variables Repetido");
        } else {
            $.ajax({
                url: 'Svl_Servicios',
                type: 'POST',
                dataType: 'json',
                data: {
                    accion: 'agregar-servicio',
                    nombre: nombre,
                    tipoPersona: tipoPersona,
                    bureau: bureau,
                    tipoServicio: tipoServicio,
                    url: url,
                    tipoVigencia: tipoVigencia,
                    cantDias: cantDias,
                    diaVigencia: diaVigencia,
                    xml: xml,
                    json: JSON.stringify(json)
                },
                success: function (data, textStatus, jqXHR) {

                }
            });
        }
    }
}

$("#form-tipoVigencia").change(function () {
    if ($("select#form-tipoVigencia option:checked").val() === "1") {
        $('#divCantDias').show();
        $('#divDiasVig').hide();
        $('#form-cantDias').val("");
        $("#form-diaVigencia").val('1').change();
    } else {
        $('#divCantDias').hide();
        $('#divDiasVig').show();
        $('#form-cantDias').val("0");
    }

});

$("#form-tipoWs").change(function () {
    if ($("select#form-tipoWs option:checked").val() === "1") {
        $('#divXML').show();
    } else {
        $('#divXML').hide();
    }
});

function AddVariable() {
    contVar++;
    if (contVar >= 2) {
        $('#btnEliminarVar').show();
    }
    $('#addVariables').append('<div class="clearfix" id ="divVar' + contVar + '">' +
            '<label for="form-variable' + contVar + '" class="form-label">Variable ' + contVar + '<em>*</em>'+
                '<div class="checkgroup" >'+
                    '<label style="font-size: 12px;">Rut </label><input type="checkbox" id="form-chkRut' + contVar + '" onchange="EditVarRut(' + contVar + ')"/>'+
                    '<label style="font-size: 12px;">XML Header </label><input type="checkbox" id="form-chkHeader' + contVar + '" onchange="EditVarHeader(' + contVar + ')"/>'+
                '</div>'+
            '</label>' +
            '<div class="form-input">' +
            '<div id="divCantDiaslabel' + contVar + '"><input type="text" id="form-cantDiaslabel' + contVar + '" name="name" required="required" placeholder="Enter name variable" /></div>' +
            '<div id="divCantDiasvar' + contVar + '"><input type="text" id="form-cantDiasvar' + contVar + '" name="name" required="required" placeholder="Enter variable" /></div>' +
            '</div>' +
            '</div>');
}

function ResVariable() {
    $('#divVar' + contVar).remove();
    contVar--;
    if (contVar < 2) {
        $('#btnEliminarVar').hide();
    }
}

function EditVarRut(num){
    if($('#form-chkRut'+num).is(":checked")){
        $("#form-chkHeader"+num).parent().removeClass("checked");
        $("#form-chkHeader"+num).prop("checked",false);
        $("#divCantDiaslabel"+num).html('<select id="form-diaVigencia'+num+'">'+
                                            '<option value="rut" selected>RUT</option>'+
                                            '<option value="dv">DV</option>'+
                                            '<option value="rut-dv">RUT-DV</option>'+
                                            '<option value="rutdv">RUTDV</option>'+
                                        '</select>');
    }else if(!$('#form-chkHeader'+num).is(":checked")){
        $("#divCantDiaslabel"+num).html('<input type="text" id="form-cantDiaslabel'+num+'" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar"+num).html('<input type="text" id="form-cantDiasvar1" name="name" required="required" placeholder="Enter variable"/>');
    }
}

function EditVarHeader(num){
    if($('#form-chkHeader'+num).is(":checked")){
        $("#form-chkRut"+num).parent().removeClass("checked");
        $("#form-chkRut"+num).prop("checked",false);
        $("#divCantDiaslabel"+num).html('<input type="text" id="form-cantDiaslabel'+num+'" name="name" required="required" placeholder="Enter name variable"/>');
        $("#form-cantDiaslabel"+num).val('SOAPAction');
        $("#form-cantDiaslabel"+num).attr("disabled", true);
    }else if(!$('#form-chkRut'+num).is(":checked")){
        $("#divCantDiaslabel"+num).html('<input type="text" id="form-cantDiaslabel'+num+'" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar"+num).html('<input type="text" id="form-cantDiasvar1" name="name" required="required" placeholder="Enter variable"/>');
    }
}
