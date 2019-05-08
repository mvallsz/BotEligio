var contVar = 1;
var contPar = 1;
var contArr = 1;
var SERV_DEFAULT;
var Daata = [];
var varDeF = false;
var limitVar = 0;
console.log("entra");

$(document).ready(function () {
    listarEmpresas();
    buscarTipoDato('#form-TipoDato1', '#form-tipoDatoHijo1_1');
//    listarServiciosDefault();
});

function initTables(tablaN) {
    switch (tablaN) {
        case 1:
            var empresa = $("#form-empresa option:selected").val();
            $('#tableServicios').DataTable().rows().remove().draw();
            $.ajax({
                url: 'Svl_Servicios',
                type: 'POST',
                dataType: 'json',
                data: {
                    accion: 'buscarServicios',
                    empresa: empresa
                },
                beforeSend: function (xhr) {
                    $('#tableServicios tbody').append('<tr id="trCargando" class="odd" style="text-align: center;"><td valign="top" colspan="7" class="dataTables_empty" style="text-align: center;"><i class="fa fa-spinner fa-spin"></i>  Buscando Registros</td></tr>');
                },
                success: function (data) {
                    if (data.estado === 200) {
                        Daata = data.datos;
                    }
                    listarServi();
                }
            });
            break;
    }
}

$('#form-cantDias').on('input', function (e) {
    if (this.value.length > 3) {
        this.value = this.value.slice(0, 3);
    }
});

$('#form-cantDias').on('keydown', function (e) {
    -1 !== $.inArray(e.keyCode, [46, 8, 9, 27, 13, 110]) || (/65|67|86|88/.test(e.keyCode) && (e.ctrlKey === true || e.metaKey === true)) && (!0 === e.ctrlKey || !0 === e.metaKey) || 35 <= e.keyCode && 40 >= e.keyCode || (e.shiftKey || 48 > e.keyCode || 57 < e.keyCode) && (96 > e.keyCode || 105 < e.keyCode) && e.preventDefault()
});


function activarServ(idServ, activo) {
    $.ajax({
        url: 'Svl_Servicios',
        type: 'POST',
        dataType: 'json',
        data: {
            accion: 'activarServicio',
            idServ: idServ,
            activo: activo
        },
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                alert("Servicio Actualizado");
                initTables(1);
            } else {
                alert("No se puedo actualizar el servicio");
            }
        }
    });
}

function ValidaForm() {
    var idEmp = $("#form-empresa option:selected").val();
    var nombre = $('#form-nombre').val().trim();
    var tipoPersona = $("select#form-tipoPersona option:checked").val();
    var bureau = $("select#form-bureau option:checked").val();
    var tipoServicio = $("select#form-tipoWs option:checked").val();
    var url = $('#form-url').val().trim();
    var cantDias = $('#form-cantDias').val().trim();
    var xml = $('#form-xml').val().trim();
    if (idEmp === "0") {
        alert('Debe ingresar Empresa');
        return false;
    } else if (nombre === "") {
        alert('Debe ingresar Servicio');
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
    } else if (xml === "" && tipoServicio == 1) {
        alert('Debe ingresar XML');
        return false;
    }
    for (var i = 1; i <= contVar; i++) {
        if ($('#form-chkRut' + i).is(":checked")) {
            if ($('#form-cantDiaslabel' + i).val().trim() === "") {
                alert('Debe ingresar Nombre de la Variable');
                return false;
            }
        } else if ($('#form-chkHeader' + i).is(":checked")) {
            if ($('#form-cantDiasvar' + i).val().trim() === "") {
                alert('Debe ingresar Variable');
                return false;
            }
        } else if ($('#form-chkParametro' + i).is(":checked")) {
            if ($('#form-cantDiaslabel' + i).val().trim() === "") {
                alert('Debe ingresar Nombre de la Variable');
                return false;
            }
        } else {
            if ($('#form-cantDiaslabel' + i).val().trim() === "") {
                alert('Debe ingresar Nombre de la Variable');
                return false;
            } else if ($('#form-cantDiasvar' + i).val().trim() === "") {
                alert('Debe ingresar Variable');
                return false;
            }
        }
    }
    for (var i = 1; i <= contPar; i++) {
        var labelPar = $('#form-cantDiaslabelPar' + i).val().trim();
        var nombrePar = $('#form-cantDiasPar' + i).val().trim();
        var tipo_dato = $("select#form-TipoDato" + i + " option:checked").val();
        if (i > 1) {
            if (labelPar === "") {
                alert('Debe ingresar Nombre de variable ' + i + ' en parametros de salida');
                return false;
            } else if (nombrePar === "") {
                alert('Debe ingresar Variable de dato de variable ' + i + ' en parametros de salida');
                return false;
            } else if (tipo_dato === "0") {
                alert('Debe ingresar tipo de dato de variable ' + i + ' en parametros de salida');
                return false;
            }
        } else {
            if (labelPar != "" && nombrePar === "") {
                alert('Debe ingresar Nombre de variable ' + i + ' en parametros de salida');
                return false;
            }
            if (labelPar != "" && nombrePar != "" && tipo_dato === "0") {
                alert('Debe ingresar tipo de dato de variable ' + i + ' en parametros de salida');
                return false;
            }
            if (labelPar === "" && nombrePar != "") {
                alert('Debe ingresar Variable de dato de variable ' + i + ' en parametros de salida');
                return false;
            }
            if (labelarr != "" && nombrePar === "" && tipo_dato != "0") {
                alert('Debe ingresar Nombre de variable ' + i + ' en parametros de salida');
                return false;
            }
        }
        verificarHi(i);
        for (var k = 1; k <= contArr; k++) {
            var labelarr = $('#form-nombreHijo' + i + "_" + k).val().trim();
            var nombrearr = $('#form-variableHijo' + i + "_" + k).val().trim();
            var tipo_datoarr = $("select#form-tipoDatoHijo" + i + "_" + k + " option:checked").val();
            if (k > 1) {
                if (labelarr === "") {
                    alert('Debe ingresar Nombre de variable arreglo ' + k + ' en parametros de salida');
                    return false;
                } else if (nombrearr === "") {
                    alert('Debe ingresar Variable de dato de variable arreglo ' + k + ' en parametros de salida');
                    return false;
                } else if (tipo_datoarr === "0") {
                    alert('Debe ingresar tipo de dato de variable arreglo ' + k + ' en parametros de salida');
                    return false;
                }
            } else {
                if (labelarr != "" && nombrearr === "") {
                    alert('Debe ingresar Nombre de variable arreglo ' + k + ' en parametros de salida');
                    return false;
                }
                if (labelarr != "" && nombrearr != "" && tipo_datoarr === "0") {
                    alert('Debe ingresar tipo de dato de variable arreglo ' + k + ' en parametros de salida');
                    return false;
                }
                if (labelarr === "" && nombrearr != "") {
                    alert('Debe ingresar Variable de dato de variable arreglo ' + k + ' en parametros de salida');
                    return false;
                }
                if (labelarr != "" && nombrearr === "" && tipo_datoarr != "0") {
                    alert('Debe ingresar Nombre de variable arreglo ' + k + ' en parametros de salida');
                    return false;
                }
            }
        }
    }
    return true;
}

function GuardarForm() {
    if (ValidaForm()) {
        var idEmp = $("#form-empresa option:selected").val();
        var nombre = $('#form-nombre').val().trim();
        var bureau = $("select#form-bureau option:checked").val();
        var tipoPersona = $("select#form-tipoPersona option:checked").val();
        var tipoServicio = $("select#form-tipoWs option:checked").val();
        var url = $('#form-url').val().trim();
        var tipoVigencia = $("select#form-tipoVigencia option:checked").val();
        var cantDias = $('#form-cantDias').val().trim();
        var diaVigencia = $("select#form-diaVigencia option:checked").val();
        var xml = " ";
        if (tipoServicio == 1) {
            xml = $('#form-xml').val().trim();
        }
        var contP = 0;
        var jsonAr = new Array();
        var json = new Object();
        for (var i = 1; i <= contVar; i++) {
            var labelVar = $('#form-cantDiaslabel' + i).val().trim();
            var nombreVar = ""
            if ($('#form-chkRut' + i).is(":checked")) {
                nombreVar = $("select#form-diaVigencia" + i + " option:checked").val();
            } else if ($('#form-chkParametro' + i).is(":checked")) {
                jsonAr.push(labelVar);
                contP++;
            } else {
                nombreVar = $('#form-cantDiasvar' + i).val().trim();
            }
            if (!$('#form-chkParametro' + i).is(":checked")) {
                json[labelVar] = nombreVar;
            }
        }
        json['parametros'] = jsonAr;
        var response = $("select#form-tipoSalida option:checked").val();
        var json2 = new Array();
        for (var i = 1; i <= contPar; i++) {
            var labelPar = $('#form-cantDiaslabelPar' + i).val().trim();
            var nombrePar = $('#form-cantDiasPar' + i).val().trim();
            var tipo_dato = $("select#form-TipoDato" + i + " option:checked").val();
            verificarHi(i);
            var jsonh = new Array();
            for (var k = 1; k <= contArr; k++) {
                var labelarr = $('#form-nombreHijo' + i + "_" + k).val().trim();
                var nombrearr = $('#form-variableHijo' + i + "_" + k).val().trim();
                var tipo_datoarr = $("select#form-tipoDatoHijo" + i + "_" + k + " option:checked").val();
                if (labelarr != "" && nombrearr != "" && tipo_datoarr != "0") {
                    var j = new Object();
                    j.NOMBRE = labelarr;
                    j.VARIABLE = nombrearr;
                    j.TIPODATO = tipo_datoarr;
                    jsonh.push(j);
                }
            }
            var x = new Object();
            x.NOMBRE = labelPar;
            x.VARIABLE = nombrePar;
            x.TIPODATO = tipo_dato;
            x.VARHIJO = jsonh;
            json2.push(x);
        }
        if (jsonAr.length < contP && Object.keys(json).length < (contP === 0 ? contVar : (contVar - (contP - 1)))) {
            alert("Nombre de Variables de Entrada Repetido");
        } else if (Object.keys(json2).length < contPar) {
            alert("Nombre de Variables de Salida Repetido");
        } else {
            $.ajax({
                url: 'Svl_Servicios',
                type: 'POST',
                dataType: 'json',
                data: {
                    accion: 'agregarServicio',
                    idEmpresa: idEmp,
                    nombre: nombre,
                    tipoPersona: tipoPersona,
                    bureau: bureau,
                    tipoServicio: tipoServicio,
                    url: url,
                    tipoVigencia: tipoVigencia,
                    cantDias: cantDias,
                    diaVigencia: diaVigencia,
                    xml: xml,
                    json: JSON.stringify(json),
                    response: response,
                    varResponse: JSON.stringify(json2)
                },
                success: function (data, textStatus, jqXHR) {
                    alert('Servicio creado, debe activarlo para utilizarlo');
                    limpiarForm(2);
                    initTables(1);
                }
            });
        }
    }
}

function NuevoOrigen() {
    var idEmp = $("#form-empresa option:selected").val();
    var origen = $('#form-origen').val().trim();
    if (origen === "") {
        alert('Debe ingresar Nombre del Origen');
    } else {
        $.ajax({
            url: 'Svl_Servicios',
            type: 'POST',
            dataType: 'json',
            data: {
                accion: 'agregarOrigen',
                origen: origen,
                idEmp: idEmp
            },
            success: function (data, textStatus, jqXHR) {
                if (data.estado == 200) {
                    alert("Conexión agregada exitosamente");
                    Cancelar();
                } else {
                    alert("No se puedo agregar la nueva Conexión");
                }
            }
        });
    }
}

function EliminarServ(idServ) {
    $.ajax({
        url: 'Svl_Servicios',
        type: 'POST',
        dataType: 'json',
        data: {
            accion: 'eliminarServicio',
            idServ: idServ
        },
        success: function (data, textStatus, jqXHR) {
            alert('Servicio Eliminado');
            initTables(1);
        }
    });
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
            '<label for="form-variable' + contVar + '" class="form-label">Variable ' + contVar + '<em>*</em>' +
            '<div class="checkgroup" >' +
            '<label style="font-size: 12px;">Rut </label><input type="checkbox" id="form-chkRut' + contVar + '" onchange="EditVarRut(' + contVar + ');"/>' +
            '<label style="font-size: 12px;"> XML Header </label><input type="checkbox" id="form-chkHeader' + contVar + '" onchange="EditVarHeader(' + contVar + ');"/>' +
            '<label style="font-size: 12px;" title="Parametro para Web Service Users"> Parametro </label><input type="checkbox" id="form-chkParametro' + contVar + '" onchange="EditParametroHeader(' + contVar + ')"/>' +
            '</div>' +
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
    if (varDeF) {
        if (contVar < limitVar) {
            $('#btnEliminarVar').hide();
        }
    } else {
        if (contVar < 2) {
            $('#btnEliminarVar').hide();
        }
    }
}

function EditVarRut(num) {
    if ($('#form-chkRut' + num).is(":checked")) {
        $("#form-chkHeader" + num).parent().removeClass("checked");
        $("#form-chkHeader" + num).prop("checked", false);
        $("#form-chkParametro" + num).parent().removeClass("checked");
        $("#form-chkParametro" + num).prop("checked", false);
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<div class="selector" id="uniform-form-diaVigencia' + num + '"><span></span>' +
                '<select id="form-diaVigencia' + num + '" onchange="mostrarSelect2(\'form-diaVigencia' + num + '\');">' +
                '<option value="rut" selected>RUT</option>' +
                '<option value="dv">DV</option>' +
                '<option value="rut-dv">RUT-DV</option>' +
                '<option value="rutdv">RUTDV</option>' +
                '</select></div>');
        mostrarSelect2('form-diaVigencia' + num);
    } else if (!$('#form-chkHeader' + num).is(":checked")) {
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter variable"/>');
    } else if (!$('#form-chkParametro' + num).is(":checked")) {
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter variable"/>');
    }
}

function EditVarHeader(num) {
    if ($('#form-chkHeader' + num).is(":checked")) {
        $("#form-chkRut" + num).parent().removeClass("checked");
        $("#form-chkRut" + num).prop("checked", false);
        $("#form-chkParametro" + num).parent().removeClass("checked");
        $("#form-chkParametro" + num).prop("checked", false);
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#form-cantDiaslabel" + num).val('SOAPAction');
        $("#form-cantDiaslabel" + num).attr("disabled", true);
    } else if (!$('#form-chkRut' + num).is(":checked")) {
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter variable"/>');
    } else if (!$('#form-chkParametro' + num).is(":checked")) {
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter variable"/>');
    }
}

function EditParametroHeader(num) {
    if ($('#form-chkParametro' + num).is(":checked")) {
        $("#form-chkRut" + num).parent().removeClass("checked");
        $("#form-chkRut" + num).prop("checked", false);
        $("#form-chkHeader" + num).parent().removeClass("checked");
        $("#form-chkHeader" + num).prop("checked", false);
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="variable"/>');
        $("#form-cantDiasvar" + num).attr("disabled", true);
    } else if (!$('#form-chkRut' + num).is(":checked")) {
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter variable"/>');
    } else if (!$('#form-chkHeader' + num).is(":checked")) {
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter variable"/>');
    }
}

function AddParametrosArr() {
    contArr++;
    if (contArr >= 2) {
        $('#btnEliminarArr' + contPar).show();
    }
    $('#addParaHijos' + contPar).append('<div class="clearfix" id ="divParH' + contPar + '_' + contArr + '">' + '<div class="clearfix">' +
            '<div class="clearfix"> ' +
            '<label for="form-parHijo' + contPar + '_' + contArr + '" class="form-label">Variable Hijo ' + contArr + '<em>*</em>' +
            ' </label>' +
            '<div class="clearfix">' +
            '<div class="form-input">' +
            '<div id="divNombreHijo' + contPar + '_' + contArr + '"><input type="text" id="form-nombreHijo' + contPar + '_' + contArr + '" name="name" required="required" placeholder="Ej: XCIdentificacion/Edad"/></div>' +
            '<div id="divVariableHijo' + contPar + '_' + contArr + '"><input type="text" id="form-variableHijo' + contPar + '_' + contArr + '" name="name" required="required" placeholder="Ej: Edad"/></div>' +
            '<div id="divtipoDatoHijo' + contPar + '_' + contArr + '"><div class="selector" id="uniform-form-tipoDatoHijo' + contPar + '_' + contArr + '"><span></span>' +
            '<select class="hijoo" id="form-tipoDatoHijo' + contPar + '_' + contArr + '" required="required"  onchange="mostrarSelect2(\'form-tipoDatoHijo' + contPar + '_' + contArr + '\');"><option value="0" disabled selected>Select Option</option></select></div>' +
            '</div></div>' +
            '</div>' +
            '</div>' +
            '</div>');
    listarTipoDato('#form-tipoDatoHijo' + contPar + '_' + contArr);
    mostrarSelect2('form-tipoDatoHijo' + contPar + '_' + contArr);
}

function limpiarForm(tipo) {
    if (tipo === 1) {
        limpiarSelect('form-bureau', '0');
        listarOrigen();
    } else if (tipo === 2) {
        selectedF('form-bureau', '0');
    }
    if (tipo != 0) {
        limpiarSelect('form-nombre-servicio', '0');
        $('#form-nombre').val("");
        if (tipo === 4) {
            $('#servicio-default').show();
            $('#servicio-normal').hide();
        } else {
            $('#servicio-normal').show();
            $('#servicio-default').hide();
        }
    }
    selectedF('form-tipoVigencia', '1');
    selectedF('form-tipoPersona', '0');
    selectedF('form-tipoWs', '1');
    selectedF('form-tipoSalida', '1');
    $('#form-origen').val("");
    $('#form-url').val("");
    $('#form-cantDias').val("");
    $('#divCantDias').show();
    $('#form-xml').val("");
    $('#varHijo1').hide();
    $('#addVariables').html('');
    $('#addParametros').html('');
    $('#addParaHijos1').html('');
//    for (var i = contVar; i > 1; i--) {
//        ResVariable();
//    }
//    for (var i = contPar; i > 1; i--) {
//        ResParametros();
//    }
    selectedF('form-diaVigencia', '1');
    $('#divDiasVig').hide();
    $("#divCantDiaslabel1").html('<input type="text" id="form-cantDiaslabel1" name="name" required="required" placeholder="Enter name variable"/>');
    $("#divCantDiasvar1").html('<input type="text" id="form-cantDiasvar1" name="name" required="required" placeholder="Enter variable"/>');
    $("#divCantDiaslabelPar1").html('<input type="text" id="form-cantDiaslabelPar1" name="name" required="required" placeholder="Ej: XCIdentificacion/Edad"/>');
    $("#divCantDiasPar1").html('<input type="text" id="form-cantDiasPar1" name="name" required="required" placeholder="Ej: Edad"/>');
    $("#form-chkHeader1").parent().removeClass("checked");
    $("#form-chkHeader1").prop("checked", false);
    $("#form-chkParametro1").parent().removeClass("checked");
    $("#form-chkParametro1").prop("checked", false);
    $("#form-chkRut1").parent().removeClass("checked");
    $("#form-chkRut1").prop("checked", false);
    $('#addVar' + contPar).show();
    $('#addVa').show();
    $('#addPar').show();
    $("#divCantDiaslabelPar1_1").html('<input type="text" id="form-cantDiaslabelHijo1" name="name" required="required" placeholder="Ej: XCIdentificacion/Edad"/>');
    $("#divCantDiasPar1_1").html('<input type="text" id="form-cantDiasHijo1" name="name" required="required" placeholder="Ej: Edad"/>');
    selectedF('form-TipoDato1', '0');
    selectedF('form-tipoDatoHijo1_1', '0');
    contVar = 1;
    contPar = 1;
    contArr = 1;
    $('#form-tipoPersona').attr('disabled', false);
    $('#form-tipoSalida').attr('disabled', false);
    $('#form-tipoWs').attr('disabled', false);
    $('#form-xml').attr('disabled', false);
    $('#form-url').attr('disabled', false);
    $("#divCantDiaslabel1").attr('disabled', false);
    $("#divCantDiasvar1").attr('disabled', false);
    $("#divCantDiaslabelPar1").attr('disabled', false);
    $("#divCantDiasPar1").attr('disabled', false);
    $("#form-chkHeader1").attr('disabled', false);
    $("#form-chkParametro1").attr('disabled', false);
    $("#form-chkRut1").attr('disabled', false);
    $("#form-TipoDato1").attr('disabled', false);
    $('#form-chkRut1').attr('disabled', false);
    $('#form-chkHeader1').attr('disabled', false);
    $("#form-TipoDato1_1").attr('disabled', false);
    $('#form-chkRut1_1').attr('disabled', false);
    $('#form-chkHeader1_1').attr('disabled', false);
    $('#form-diaVigencia').attr('disabled', false);
    $('#divXML').show();
}

function limpiarSelect(text, mantener) {
    $("select#" + text + " option").each(function () {
        if ($(this).val() != mantener) {
            $(this).remove();
        } else {
            $('#uniform-' + text + ' span').text($(this).text());
        }
    });
}

function listarEmpresas() {
    $.ajax({
        url: 'Svl_Servicios',
        type: 'POST',
        dataType: 'json',
        data: {accion: 'listarEmpresas'},
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                for (var dato in data.datos) {
//                    if (codigo != 23) {
                    if (data.datos[dato].ID === idEmp) {
                        $('#form-empresa').append('<option value="' + data.datos[dato].ID + '">' + data.datos[dato].nom_empresa + '</option>');
                        selectedF('form-empresa', idEmp);
                        initTables(1);
                        listarOrigen();
                        break;
                    }
//                    } else {
//                        $('#form-empresa').append('<option value="' + data.datos[dato].ID + '">' + data.datos[dato].nom_empresa + '</option>');
//                    }
                }
//                if (codigo === 23) {
//                    $('#form-empresa').attr('onchange', 'initTables(1);listarOrigen();');
//                    $('#tableServicios').DataTable().rows().remove().draw();
//                    $('#tableServicios tbody').append('<tr id="trCargando" class="odd" style="text-align: center;"><td valign="top" colspan="7" class="dataTables_empty" style="text-align: center;"><i class="fa fa-spinner fa-spin"></i>  Buscando Registros</td></tr>');
//                    listarServi();
//                }
            }
        }
    });
}

function listarOrigen() {
    limpiarSelectF('form-bureau', 0);
    var idEmp = $("#form-empresa option:selected").val();
    $.ajax({
        url: 'Svl_Servicios',
        type: 'POST',
        dataType: 'json',
        data: {accion: 'listarOrigenes', idEmp: idEmp},
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                for (var dato in data.datos) {
                    $('#form-bureau').append('<option value="' + data.datos[dato].ID + '">' + data.datos[dato].nombre_origen + '</option>');
                }
            }
            $('#form-bureau').append('<option value="-1" class="agregarConexion">Agregar Conexión</option>');
        }
    });
}

function creacionOrigen() {
    var sel = $("select#form-bureau option:checked").val();
    var text = $("select#form-bureau option:checked").text().toUpperCase();
    if (sel == "-1") {
        if ($("#form-empresa option:selected").val() != '0') {
            limpiarForm(3);
            selectedF('form-bureau', 0);
            $('.new').show();
            $('.box').hide();
        } else {
            alert('Debe seleccionar una empresa');
        }
    } else {
        var entre = false;
        for (var dato in SERV_DEFAULT) {
            if (dato === text) {
                entre = true;
                limpiarForm(4);
                listarServDefault(text);
                break;
            }
        }
        if (!entre) {
            limpiarForm(3);
        }
    }
}

function Cancelar() {
    limpiarForm(1);
    $('.new').hide();
    $('.box').show();
}

function AddParametros() {
    if ($("select#form-TipoDato" + contPar + " option:checked").val() === "50") {
        $('#addVar' + contPar).hide();
    }
    contArr = 1;
    contPar++;
    if (contPar >= 2) {
        $('#btnEliminarPar').show();
    }
    $('#addParametros').append('<div class="clearfix" id ="divPar' + contPar + '">' +
            '<label for="form-variable' + contPar + '" class="form-label">Variable ' + contPar + '<em>*</em>' +
            '</label>' +
            '<div class="form-input">' +
            '<div id="divCantDiaslabelPar' + contPar + '"><input type="text" id="form-cantDiaslabelPar' + contPar + '" name="name" required="required" placeholder="Ej: XCIdentificacion/Edad" /></div>' +
            '<div id="divCantDiasPar' + contPar + '"><input type="text" id="form-cantDiasPar' + contPar + '" name="name" required="required" placeholder="Ej: Edad" /></div>' +
            '<div id="divTipoDato' + contPar + '"><div class="selector" id="uniform-form-TipoDato' + contPar + '"><span></span>' +
            '<select id="form-TipoDato' + contPar + '" required="required" onchange="tipoDatoArr(this);mostrarSelect2(\'form-TipoDato' + contPar + '\');"><option value="0" disabled selected>Select Option</option></select></div></div>' +
            '<div id="varHijo' + contPar + '" style="display: none"> ' +
            '<div class="clearfix"> ' +
            '<label for="form-parHijo' + contPar + '_' + contArr + '" class="form-label">Variable Hijo ' + contArr + '<em>*</em>' +
            ' </label>' +
            '<div class="clearfix">' +
            '<div class="form-input">' +
            '<div id="divNombreHijo' + contPar + '_' + contArr + '"><input type="text" id="form-nombreHijo' + contPar + '_' + contArr + '" name="name" required="required" placeholder="Ej: XCIdentificacion/Edad"/></div>' +
            '<div id="divVariableHijo' + contPar + '_' + contArr + '"><input type="text" id="form-variableHijo' + contPar + '_' + contArr + '" name="name" required="required" placeholder="Ej: Edad"/></div>' +
            '<div id="divtipoDatoHijo' + contPar + '_' + contArr + '"><div class="selector" id="uniform-form-tipoDatoHijo' + contPar + '_' + contArr + '"><span></span>' +
            '<select class="hijoo" id="form-tipoDatoHijo' + contPar + '_' + contArr + '" required="required" onchange="mostrarSelect2(\'form-tipoDatoHijo' + contPar + '_' + contArr + '\');"><option value="0" disabled selected>Select Option</option></select></div></div>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<div id="addParaHijos' + contPar + '"></div>' +
            '<br>' +
            '<div class="clearfix" id="addVar' + contPar + '">' +
            '<button class="button" onclick="AddParametrosArr()">Añadir Variable Arreglo</button>' +
            '<button class="button" onclick="ResParametrosArr()" id="btnEliminarArr' + contPar + '" style="display: none">Eliminar Variable Arreglo</button>' +
            '</div>' +
            '</div>' +
            '</div>');
    listarTipoDato('#form-TipoDato' + contPar);
    listarTipoDato('#form-tipoDatoHijo' + contPar + '_' + contArr);
    mostrarSelect2('form-TipoDato' + contPar);
    mostrarSelect2('form-tipoDatoHijo' + contPar + '_' + contArr);
}

function ResParametros() {
    $('#divPar' + contPar).remove();
    contPar--;
    verificarHi(contPar);
    if (contPar < 2) {
        $('#btnEliminarPar').hide();
    }
    if ($("select#form-TipoDato" + contPar + " option:checked").val() === "50") {
        $('#addVar' + contPar).show();
    }
}

function ResParametrosArr() {
    $('#divParH' + contPar + '_' + contArr).remove();
    contArr--;
    if (contArr < 2) {
        $('#btnEliminarArr' + contPar).hide();
    }
}

function buscarTipoDato(text1, text2) {
    $.ajax({
        url: 'Svl_Servicios',
        type: 'POST',
        dataType: 'json',
        data: {accion: 'listarTipoDato'},
        success: function (data, textStatus, jqXHR) {
            TIPO_DATO = data;
            listarTipoDato(text1);
            listarTipoDato(text2);
        }
    });
}

function listarTipoDato(text) {
    limpiarSelect(text, '0');
    if (TIPO_DATO.estado == 200) {
        for (var dato in TIPO_DATO.datos) {
            $(text).append('<option value="' + TIPO_DATO.datos[dato].ID + '">' + TIPO_DATO.datos[dato].NOMBRE + '</option>');
        }
    }
}

function tipoDatoArr(select) {
    for (var i = contArr; i > 1; i--) {
        ResParametrosArr();
    }
    $("#divNombreHijo" + contPar + "_1").html('<input type="text" id="form-nombreHijo' + contPar + '_1" name="name" required="required" placeholder="Ej: XCIdentificacion/Edad"/>');
    $("#divVariableHijo" + contPar + "_1").html('<input type="text" id="form-variableHijo' + contPar + '_1" name="name" required="required" placeholder="Ej: Edad"/>');
    selectedF('form-tipoDatoHijo' + contPar + '_1', '0');
    var sel = $(select).val();
    if (sel == "50") {
        $('#varHijo' + contPar).show();
    } else {
        $('#varHijo' + contPar).hide();
    }
}

function listarServiciosDefault() {
    $.ajax({
        url: 'Svl_Servicios',
        type: 'POST',
        dataType: 'json',
        data: {accion: 'listarServiciosDefault'},
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                SERV_DEFAULT = data.datos;
            }
        }
    });
}

function listarServDefault(text) {
    limpiarSelect("form-nombre-servicio", "0");
    for (var dato in SERV_DEFAULT) {
        if (dato === text) {
            var serv = SERV_DEFAULT[dato];
            for (var i = 0; i < serv.length; i++) {
                $('#form-nombre-servicio').append('<option value="' + serv[i].ID + '">' + serv[i].NOMBRE + '</option>');
            }
        }
    }
}

function listarDefault(tipo, ip, xml, tipo_rut, tipo_ws, response) {
    var bloqueado = true;
    if (tipo === 0) {
        bloqueado = false;
    }
    selectedF('form-tipoPersona', '' + tipo_rut);
    selectedF('form-tipoSalida', '' + response);
    selectedF('form-tipoWs', '' + tipo_ws);
    if (tipo_ws === 1) {
        $('#divXML').show();
    } else {
        $('#divXML').hide();
    }
    $('#form-xml').val('');
    $('#form-url').val('');
    $('#form-xml').val(xml);
    $('#form-url').val(ip);

    $('#form-tipoPersona').attr('disabled', bloqueado);
    $('#form-tipoSalida').attr('disabled', bloqueado);
    $('#form-tipoWs').attr('disabled', bloqueado);
    $('#form-xml').attr('disabled', bloqueado);
    $('#form-url').attr('disabled', bloqueado);

}

$("#form-nombre-servicio").change(function () {
    var listo = false;
    var select = parseInt($("select#form-nombre-servicio option:checked").val());
    var origen = $("select#form-bureau option:checked").text();
    for (var dato in SERV_DEFAULT) {
        if (dato === origen.toUpperCase()) {
            var serv = SERV_DEFAULT[dato];
            for (var i = 0; i < serv.length; i++) {
                if (serv[i].ID === select) {
                    listarDefault(1, serv[i].IP, serv[i].XML, serv[i].TIPO_RUT, serv[i].TIPO_WS, serv[i].RESPONSE, serv[i].CREDEN);
                    agregarVariEntrada(serv[i].CREDEN);
                    agregarVari(serv[i].VARI);
                    listo = true;
                    break;
                }
            }
        }
        if (listo) {
            break;
        }
    }
    if (!listo) {
        limpiarForm(0);
    }
});


function agregarVari(vari) {
    for (var i = contPar; i > 1; i--) {
        ResParametros();
    }
    if (vari.length > 0) {
        for (var i = 0; i < vari.length; i++) {
            if (i != 0) {
                AddParametros();
            }
            $('#form-cantDiaslabelPar' + contPar).val(vari[i].NOMBRE);
            $('#form-cantDiasPar' + contPar).val(vari[i].VARIABLE);
            selectedF('form-TipoDato' + contPar, '' + vari[i].TIPO_DATO);
            $('#form-TipoDato' + contPar).attr('disabled', true);
            $('#form-cantDiaslabelPar' + contPar).attr('disabled', true);
            agregarVariHijo(vari[i].VARIHIJO);
        }
        $('#addPar').hide();
    } else {
        $('#addPar').show();
    }
}

function agregarVariEntrada(creden) {
    for (var i = contVar; i > 1; i--) {
        ResVariable();
    }
    if (JSON.stringify(creden).length > 3) {
        var i = 0;
        for (var item in creden) {
            if (i != 0) {
                AddVariable();
            }
            if (item === 'SOAPAction') {
                $("#form-chkHeader" + contVar).parent().addClass("checked");
                $("#form-chkHeader" + contVar).prop("checked", true);
                $("#form-chkHeader" + contVar).click();
                $('#form-cantDiasvar' + contVar).val(creden[item]);
                $('#form-cantDiaslabel' + contVar).val(item);
                $('#form-cantDiasvar' + contVar).attr('disabled', true);
                $('#form-cantDiaslabel' + contVar).attr('disabled', true);
            } else if (creden[item] === 'rut' || creden[item] === 'dv' || creden[item] === 'rut-dv' || creden[item] === 'rutdv') {
                $("#form-chkRut" + contVar).parent().addClass("checked");
                $("#form-chkRut" + contVar).prop("checked", true);
                $('#form-cantDiaslabel' + contVar).attr('disabled', true);
                $("#divCantDiasvar" + contVar).html('<div class="selector" id="uniform-form-diaVigencia' + contVar + '"><span></span>' +
                        '<select id="form-diaVigencia' + contVar + '" onchange="mostrarSelect2(\'form-diaVigencia' + contVar + '\')">' +
                        '<option value="rut" selected>RUT</option>' +
                        '<option value="dv">DV</option>' +
                        '<option value="rut-dv">RUT-DV</option>' +
                        '<option value="rutdv">RUTDV</option>' +
                        '</select></div>');
                $('#form-cantDiaslabel' + contVar).val(item);
                mostrarSelect2('form-diaVigencia' + contVar);
                selectedF('form-diaVigencia' + contVar, '' + creden[item]);
                $('#form-diaVigencia' + contVar).attr('disabled', true);
            } else if (item.toLowerCase().includes('username') || item.toLowerCase().includes('user') || item.toLowerCase().includes('cli') ||
                    item.toLowerCase().includes('password') || item.toLowerCase().includes('pswd') || item.toLowerCase().includes('pass')) {
                $('#form-cantDiaslabel' + contVar).val(item);
                $('#form-cantDiaslabel' + contVar).attr('disabled', true);
            } else {
                $('#form-cantDiaslabel' + contVar).val(item);
                $('#form-cantDiasvar' + contVar).val(creden[item]);
                $('#form-cantDiaslabel' + contVar).attr('disabled', true);
                $('#form-cantDiasvar' + contVar).attr('disabled', true);
            }
            i++;
            $('#form-chkRut' + contVar).attr('disabled', true);
            $('#form-chkHeader' + contVar).attr('disabled', true);
            $('#form-chkParametro' + contVar).attr('disabled', true);
        }
        limitVar = contVar + 1;
        varDeF = true;
        $('#btnEliminarVar').hide();
    } else {
        limitVar = 0;
        varDeF = false;
        $('#btnEliminarVar').show();
    }
}

function mostrarSelect2(nombre) {
    var text = $('select#' + nombre + ' option:checked').text();
    $('#uniform-' + nombre + ' span').text(text);
}

function mostrarCheck(nombre) {
    if ($("#form-" + nombre).is(":checked")) {
        $("#uniform-form-" + nombre + " span").removeClass("checked");
    } else {
        $("#uniform-form-" + nombre + " span").addClass("checked");
    }
}

function verificarHi(par) {
    contArr = 0;
    $("#varHijo" + par + " .hijoo").each(function () {
        contArr++;
    });
}

function agregarVariHijo(vari) {
    if (vari.length > 0) {
        $('#addVar' + contPar).hide();
        $('#varHijo' + contPar).show();
        for (var i = 0; i < vari.length; i++) {
            if (i != 0) {
                AddParametrosArr();
            }
            $('#form-nombreHijo' + contPar + '_' + contArr).val(vari[i].NOMBRE);
            $('#form-variableHijo' + +contPar + '_' + contArr).val(vari[i].VARIABLE);
            selectedF('form-tipoDatoHijo' + +contPar + '_' + contArr, '' + vari[i].TIPO_DATO);
            $('#form-tipoDatoHijo' + +contPar + '_' + contArr).attr('disabled', true);
            $('#form-nombreHijo' + +contPar + '_' + contArr).attr('disabled', true);
        }
    }
}

function listarServi() {
    $('#trCargando').remove();
    $('#tableServicios').DataTable().destroy();
    $('#tableServicios').DataTable({
        "language": {
                   "lengthMenu": "Mostrar _MENU_ registros por página.",
                   "zeroRecords": "Lo sentimos. No se encontraron registros.",
                         "info": "Mostrando página _PAGE_ de _PAGES_",
                         "infoEmpty": "No hay registros aún.",
                         "infoFiltered": "(filtrados de un total de _MAX_ registros)",
                         "search": "Búsqueda",
                         "LoadingRecords": "Cargando ...",
                         "Processing": "Procesando...",
                         "SearchPlaceholder": "Comience a teclear...",
                         "paginate": {
                     "previous": "Anterior",
                     "next": "Siguiente"
                 }
              },
        "data": Daata,
        "bSort": true,
        "columnDefs": [
            {"width": "30%", "targets": 0},
            {"width": "20%", "targets": 1},
            {"width": "20%", "targets": 2},
            {"width": "20%", "targets": 3},
            {"width": "10%", "targets": 4}
        ],
        "columns": [
            {data: 'NOMBRE', class: 'txt-center'},
            {data: 'TIPO_RUT', class: 'txt-center'},
            {data: 'NOMBRE_BUREAU', class: 'txt-center'},
            {data: null, "render": function (data, type, row) {
                    if (data.ACTIVO) {
                        return "Activo";
                    } else {
                        return "Desactivado";
                    }
                }},
            {data: null, "render": function (data, type, row) {
                    var action = "";
                    if (data.ACTIVO) {
                        action = action + '<button title="Desactivar" onclick="activarServ(' + data.ID + ', 0)"><i class="fa fa-ban"></i></button>';
                    } else {
                        action = action + '<button title="Activar" onclick="activarServ(' + data.ID + ', 1)"><i class="fa fa-check-circle"></i></button>';
                    }
                    action = action + '<button title="Eliminar" onclick="EliminarServ(' + data.ID + ')"><i class="fa fa-trash-alt"></i></button>';
                    return action;
                }}
        ],
    });
}
