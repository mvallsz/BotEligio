var contVar = 1;

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
        if (labelVar === "") {
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
    console.log("prueba");
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
            var labelVar = $('#form-cantDiaslabel' + i).val().trim();
            var nombreVar = $('#form-cantDiasvar' + i).val().trim();
            json = json + '\"' + labelVar + '\":\"' + nombreVar + '\"';
        }
        json = json + "}";
        json = JSON.parse(json);
        
//        $.ajax({
//            url: 'php/google_search_results.php',
//            data: 'q='+encodeURIComponent(q),
//            cache: false,
//            success: function(response){
//                $('.search_results').html(response);
//            }
//        });
        
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

function AddVariable() {
    contVar++;
    if (contVar >= 2) {
        $('#btnEliminarVar').show();
    }
    $('#addVariables').append('<div class="clearfix" id ="divVar' + contVar + '">' +
            '<label for="form-variable' + contVar + '" class="form-label">Variable ' + contVar + '<em>*</em></label>' +
            '<div class="form-input">' +
            '<input type="text" id="form-cantDiaslabel' + contVar + '" name="name" required="required" placeholder="Enter name variable" />' +
            '<input type="text" id="form-cantDiasvar' + contVar + '" name="name" required="required" placeholder="Enter variable" />' +
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