var contVar = 1;
var contPar = 1;
var contArr = 1;
var SERV_DEFAULT;
var Daata = [];
var varDeF = false;
var limitVar = 0;
console.log("Entra a solicitud_rpa.js");

$(document).ready(function () {
    
    listarEstados();
    initTables(1);
});

function initTables(tablaN) {
    switch (tablaN) {
        case 1:
            $('#tableServicios').DataTable().rows().remove().draw();
            $.ajax({
                url: 'Svl_Servicios',
                type: 'POST',
                dataType: 'json',
                data: {
                    accion: 'buscarServicios'
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

    var nombre_rpa = $('#form-nombre-rpa').val().trim();
    var US_state = $("select#form-US_state option:checked").val();
    var zipCodes = $('#form-zipCodes').val().trim();
    var appliance = $('#form-appliance').val().trim();
    var correo_notificacion = $('#form-correo-notificacion').val().trim();
    var usuario_host = $('#form-usuario-host').val().trim();
    var password_host = $('#form-password-host').val().trim();
    
    if (nombre_rpa === "") {
        alert('Debe ingresar Nombre del Servicio');
        return false;
    } else if (US_state === "0") {
        alert('Debe seleccionar el Estado');
        return false;
    } else if (zipCodes === "") {
        alert('Debe indicar al menos un Zip Code');
        return false;
    } else if (appliance === "") {
        alert('Debe indicar al menos un appliance');
        return false;
    } else if (correo_notificacion === "") {
        alert('Debe ingresar el correo al cual se notificara la aceptación del proceso');
        return false;
    } else if (usuario_host === "") {
        alert('Debe ingresar el usuario del sistema Host');
        return false;
    } else if (password_host === "") {
        alert('Debe ingresar el password del sistema Host');
        return false;
    }
    return true;
}

function GuardarForm() {
    if (ValidaForm()) {
        var nombre_rpa = $('#form-nombre-rpa').val().trim();
        var US_state = $("select#form-US_state option:checked").val();
        var zipCodes = $('#form-zipCodes').val().trim();
        var appliance = $('#form-appliance').val().trim();
        var correo_notificacion = $('#form-correo-notificacion').val().trim();
        var usuario_host = $('#form-usuario-host').val().trim();
        var password_host = $('#form-password-host').val().trim();
        
        $.ajax({
            url: 'Svl_Servicios',
            type: 'POST',
            dataType: 'json',
            data: {
                accion: 'agregarServicioRPA',
                nombre_rpa: nombre_rpa,
                US_state: US_state,
                zipCodes: zipCodes,
                appliance: appliance,
                correo_notificacion: correo_notificacion,
                usuario_host: usuario_host,
                password_host: password_host
            },
            success: function (data, textStatus, jqXHR) {
                alert('Servicio RPA creado');
                limpiarFormRPA();
                initTables(1);
            }
        });
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
    $('#form-xml').val('');
    if ($("select#form-tipoWs option:checked").val() === "1") {
        $('#divXML').show();
        $('#lbl-xml').html('XML<em>*</em>');
        $('#form-xml').attr('placeholder', '<?xml version="1.0"?>\n<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-\nenvelope/" soap:encodingStyle="http://www.w3.org/2003/05/soap-\nencoding">\n<soap:Header>\n;...\n</soap:Header>\n<soap:Body>\n...\n<soap:Fault>\n...\n</soap:Fault>\n</soap:Body>\n</soap:Envelope>\n');
    } else if ($("select#form-tipoWs option:checked").val() === "3") {
        $('#divXML').show();
        $('#lbl-xml').html('BODY<em>*</em>');
        $('#form-xml').attr('placeholder', '{\n   "var1": "?",\n   "var2": "?",\n   "var3": "?"\n}');
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
//            '<label style="font-size: 12px;">Rut </label><input type="checkbox" id="form-chkRut' + contVar + '" onchange="EditVarRut(' + contVar + ');"/>' +
            '<label style="font-size: 12px;" title="Autentificación Basic">Basic Auth </label><input type="checkbox" id="form-chkAuth' + contVar + '" onchange="EditVarAuth(' + contVar + ');"/>' +
            '<label style="font-size: 12px;"> Header </label><input type="checkbox" id="form-chkHeader' + contVar + '" onchange="EditVarHeader(' + contVar + ');"/>' +
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
        $("#form-chkAuth" + num).parent().removeClass("checked");
        $("#form-chkAuth" + num).prop("checked", false);
        $("#form-chkParametro" + num).parent().removeClass("checked");
        $("#form-chkParametro" + num).prop("checked", false);
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter variable"/>');
//        $("#divCantDiaslabel" + num).html('<div class="selector" id="uniform-form-cantDiaslabel' + num + '"><span></span>' +
//                '<select id="form-cantDiaslabel' + num + '" onchange="mostrarSelect2(\'form-cantDiaslabel' + num + '\');">' +
//                '<option value="SOAPAction" selected>SOAPAction</option>' +
//                '<option value="Content-Type">Content-Type</option>' +
//                '</select></div>');
//        mostrarSelect2('form-cantDiaslabel' + num);
    } else if (!$('#form-chkAuth' + num).is(":checked")) {
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter variable"/>');
    } else if (!$('#form-chkParametro' + num).is(":checked")) {
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter variable"/>');
    }
}

function EditVarAuth(num) {
    if ($('#form-chkAuth' + num).is(":checked")) {
        $("#form-chkHeader" + num).parent().removeClass("checked");
        $("#form-chkHeader" + num).prop("checked", false);
        $("#form-chkParametro" + num).parent().removeClass("checked");
        $("#form-chkParametro" + num).prop("checked", false);
        $("#divCantDiaslabel" + num).html('<div class="selector" id="uniform-form-cantDiaslabel' + num + '"><span></span>' +
                '<select id="form-cantDiaslabel' + num + '" onchange="mostrarSelect2(\'form-cantDiaslabel' + num + '\');cambiarTextVari(' + num + ');">' +
                '<option value="user" selected>Usuario</option>' +
                '<option value="pass">Password</option>' +
                '</select></div>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter variable"/>');
        mostrarSelect2('form-cantDiaslabel' + num);
    } else if (!$('#form-chkHeader' + num).is(":checked")) {
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter variable"/>');
    } else if (!$('#form-chkParametro' + num).is(":checked")) {
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter variable"/>');
    }
}

function EditParametroHeader(num) {
    if ($('#form-chkParametro' + num).is(":checked")) {
        $("#form-chkAuth" + num).parent().removeClass("checked");
        $("#form-chkAuth" + num).prop("checked", false);
        $("#form-chkHeader" + num).parent().removeClass("checked");
        $("#form-chkHeader" + num).prop("checked", false);
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="variable"/>');
        $("#form-cantDiasvar" + num).attr("disabled", true);
    } else if (!$('#form-chkAuth' + num).is(":checked")) {
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

function limpiarFormRPA(){
    $('#form-nombre-rpa').val("");
    $('#form-zipCodes').val("");
    $('#form-appliance').val("");
    $('#form-correo-notificacion').val("");
    $('#form-usuario-host').val("");
    $('#form-password-host').val("");
    selectedF('form-US_state', '0');
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
    $("#form-chkAuth1").parent().removeClass("checked");
    $("#form-chkAuth1").prop("checked", false);
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
    $('#lbl-xml').html('XML<em>*</em>');
    $('#form-xml').attr('placeholder', '<?xml version="1.0"?>\n<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-\nenvelope/" soap:encodingStyle="http://www.w3.org/2003/05/soap-\nencoding">\n<soap:Header>\n;...\n</soap:Header>\n<soap:Body>\n...\n<soap:Fault>\n...\n</soap:Fault>\n</soap:Body>\n</soap:Envelope>\n');
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

function listarEstados() {
    $.ajax({
        url: 'Svl_Servicios',
        type: 'POST',
        dataType: 'json',
        data: {accion: 'listarEstados'},
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                for (var dato in data.datos) {
                        $('#form-US_state').append('<option value="' + data.datos[dato].name + '">' + data.datos[dato].name + '</option>');
                        //selectedF('form-empresa', idEmp);
                        initTables(1);
                }
            }
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
            {data: 'nombre', class: 'txt-center'},
                        {data: null, "render": function (data, type, row) {

                if(data.zip_codes.length >= 15){
                    zipCodesString = data.zip_codes.substring(0, 15)+"...";
                }else{
                    zipCodesString = data.zip_codes;
                }
                return "<span id='#tooltip-1' title = '"+data.zip_codes+"'>"+zipCodesString+"</span>";
            }},
            {data: null, "render": function (data, type, row) {
                
                if(data.key_words.length >= 15){
                    keyWordsString = data.key_words.substring(0, 15)+"...";
                }else{
                    keyWordsString = data.key_words;
                }
                return "<span id='#tooltip-2' title = '"+data.key_words+"'>"+keyWordsString+"</span>";
            }},
            {data: null, "render": function (data, type, row) {
                    if (data.estado) {
                        return "Activo";
                    } else {
                        return "Desactivado";
                    }
                }},
            {data: null, "render": function (data, type, row) {
                    var action = "";
                    if (data.estado) {
                        action = action + '<button title="Desactivar" onclick="activarServ(' + data.id + ', 0)"><i class="fa fa-ban"></i></button>';
                    } else {
                        action = action + '<button title="Activar" onclick="activarServ(' + data.id + ', 1)"><i class="fa fa-check-circle"></i></button>';
                    }
                    action = action + '<button title="Eliminar" onclick="EliminarServ(' + data.id + ')"><i class="fa fa-trash-alt"></i></button>';
                    return action;
                }}
        ],
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

function cambiarTextVari(cont) {
    var lbl = $('#form-cantDiaslabel' + cont).val();
    if (lbl == 'user') {
        $('#form-cantDiasvar' + cont).prop('type', 'text');
    } else {
        $('#form-cantDiasvar' + cont).prop('type', 'password');

    }
}
