var contVar = 1;
console.log("entra");

$(document).ready(function () {
    initTables(1);
});

function initTables(tablaN) {
    switch (tablaN) {
        case 1:
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
                        "data": data.datos,
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
    } else if (xml === "" && tipoServicio == 1) {
        alert('Debe ingresar XML');
        return false;
    }
    for (var i = 1; i <= contVar; i++) {
        var labelVar = "";
        if (!$('#form-chkRut' + i).is(":checked")) {
            labelVar = $('#form-cantDiaslabel' + i).val().trim();
        }
        var nombreVar = $('#form-cantDiasvar' + i).val().trim();
        if (labelVar === "" && !$('#form-chkHeader' + i).is(":checked") && !$('#form-chkRut' + i).is(":checked")) {
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
        var xml = " ";
        if (tipoServicio == 1) {
            xml = $('#form-xml').val().trim();
        }
        var json = "{";
        for (var i = 1; i <= contVar; i++) {
            if (i > 1) {
                json = json + ",";
            }
            var labelVar = "";
            if ($('#form-chkRut' + i).is(":checked")) {
                labelVar = $("select#form-diaVigencia" + i + " option:checked").val();
            } else {
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
                    accion: 'agregarServicio',
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
                    alert('Servicio creado');
                    limpiarForm();
                    initTables(1);
                }
            });
        }
    }
}

function EliminarServ(idServ){
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
            '<label style="font-size: 12px;">Rut </label><input type="checkbox" id="form-chkRut' + contVar + '" onchange="EditVarRut(' + contVar + ')"/>' +
            '<label style="font-size: 12px;">XML Header </label><input type="checkbox" id="form-chkHeader' + contVar + '" onchange="EditVarHeader(' + contVar + ')"/>' +
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
    if (contVar < 2) {
        $('#btnEliminarVar').hide();
    }
}

function EditVarRut(num) {
    if ($('#form-chkRut' + num).is(":checked")) {
        $("#form-chkHeader" + num).parent().removeClass("checked");
        $("#form-chkHeader" + num).prop("checked", false);
        $("#divCantDiaslabel" + num).html('<select id="form-diaVigencia' + num + '">' +
                '<option value="rut" selected>RUT</option>' +
                '<option value="dv">DV</option>' +
                '<option value="rut-dv">RUT-DV</option>' +
                '<option value="rutdv">RUTDV</option>' +
                '</select>');
    } else if (!$('#form-chkHeader' + num).is(":checked")) {
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter variable"/>');
    }
}

function EditVarHeader(num) {
    if ($('#form-chkHeader' + num).is(":checked")) {
        $("#form-chkRut" + num).parent().removeClass("checked");
        $("#form-chkRut" + num).prop("checked", false);
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#form-cantDiaslabel" + num).val('SOAPAction');
        $("#form-cantDiaslabel" + num).attr("disabled", true);
    } else if (!$('#form-chkRut' + num).is(":checked")) {
        $("#divCantDiaslabel" + num).html('<input type="text" id="form-cantDiaslabel' + num + '" name="name" required="required" placeholder="Enter name variable"/>');
        $("#divCantDiasvar" + num).html('<input type="text" id="form-cantDiasvar' + num + '" name="name" required="required" placeholder="Enter variable"/>');
    }
}

function limpiarForm() {
    $('#form-nombre').val("");
    $('#form-url').val("");
    $('#form-cantDias').val("");
    $('#form-xml').val("");
    for (var i = contVar; i > 1; i--) {
        ResVariable();
    }
    $("#divCantDiaslabel1").html('<input type="text" id="form-cantDiaslabel1" name="name" required="required" placeholder="Enter name variable"/>');
    $("#divCantDiasvar1").html('<input type="text" id="form-cantDiasvar1" name="name" required="required" placeholder="Enter variable"/>');
    $("#form-chkHeader1").parent().removeClass("checked");
    $("#form-chkHeader1").prop("checked", false);
    $("#form-chkRut1").parent().removeClass("checked");
    $("#form-chkRut1").prop("checked", false);
    contVar = 1;
}
