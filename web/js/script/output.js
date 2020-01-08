console.log("output");
var DATOS = [];
var ORIGENES = [];
var SERVICIOS = [];
var VARIABLES = [];
var VARIHIJOS = [];
var cont = 1;
var contSer = 1;
var contVar = 1;
var contVarHijo = 1;
var contUser = 1;
var USER = [];
var webSEr = [];
$(document).ready(function () {
    listarEmp();
});
function buscarOrigen3() {
    var idEmp = $("select#form-empresao option:checked").val();
    $.ajax({
        url: 'Svl_Datos',
        type: 'POST',
        dataType: 'json',
        data: {accion: 'listarOrigenes', id_empresa: idEmp},
        success: function (data, textStatus, jqXHR) {
            if (data.estado === 200) {
                DATOS = data.datos;
                listarArbol();
            }
        }
    });
}

function listarOrigen2(nombre) {
    for (var i = 0; i < ORIGENES.length; i++) {
        $('#' + nombre).append('<option value="' + ORIGENES[i].ID + '">' + ORIGENES[i].NOMBRE + '</option>');
    }
}

function listarServicio(nombre, tipo) {
    if (tipo == 1) {
        contVarHijo = 1;
        contSer = 1;
        contVar = 1;
        $('#hijo' + cont + '_' + contSer + '_' + contVar).hide();
        $('#addservicio' + cont).html('');
        $('#addvariables' + cont + '_' + contSer).html('');
        $('#addHijos' + cont + '_' + contSer + '_' + contVar).html('');
        $('#buttonVarHijo' + cont + '_' + contSer + '_' + contVar).hide();
        $('#buttonVar' + cont + '_' + contSer).hide();
        $('#buttonSer' + cont).hide();
        habilitarEliminar();
        bloqueaServ(false, cont, contSer);
    }
    limpiarSelect(nombre, '0');
    limpiarSelect('form-variable' + cont + '_' + contSer + '_' + contVar, '0');
    limpiarSelect('form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo, '0');
    $('#hijo' + cont + '_' + contSer + '_' + contVar).hide();
    if (SERVICIOS.length > 0) {
        var id = parseInt($('select#form-origen' + cont + ' option:checked').val());
        for (var i = 0; i < SERVICIOS.length; i++) {
            if (id == SERVICIOS[i].IDORI) {
                var ser = SERVICIOS[i].SERVICIO;
                if (ser.length > 0) {
                    for (var j = 0; j < ser.length; j++) {
                        $('#' + nombre).append('<option value="' + ser[j].ID + '">' + ser[j].NOMBRE + '</option>');
                    }
                }
            }
        }
    }
}

function listarVariables(nombre, tipo) {
    if (tipo == 1) {
        contVarHijo = 1;
        contVar = 1;
        $('#hijo' + cont + '_' + contSer + '_' + contVar).hide();
        $('#addvariables' + cont + '_' + contSer).html('');
        $('#addHijos' + cont + '_' + contSer + '_' + contVar).html('');
        $('#buttonVarHijo' + cont + '_' + contSer + '_' + contVar).hide();
        $('#buttonVar' + cont + '_' + contSer).hide();
        habilitarEliminar();
    }
    $('#hijo' + cont + '_' + contSer + '_' + contVar).hide();
    limpiarSelect(nombre, '0');
    limpiarSelect('form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo, '0');
    if (VARIABLES.length > 0) {
        var id = parseInt($('select#form-servicio' + cont + '_' + contSer + ' option:checked').val());
        for (var i = 0; i < VARIABLES.length; i++) {
            if (id == VARIABLES[i].IDSER) {
                var ser = VARIABLES[i].VARIABLE;
                if (ser.length > 0) {
                    for (var j = 0; j < ser.length; j++) {
                        $('#' + nombre).append('<option value="' + ser[j].ID + '">' + ser[j].NOMBRE + '</option>');
                    }
                }
            }
        }
        $('#buttonSer' + cont).show();
    }
}

function listarVariablesHijo(nombre, tipo) {
    if (tipo == 1) {
        contVarHijo = 1;
        $('#hijo' + cont + '_' + contSer + '_' + contVar).hide();
        $('#addHijos' + cont + '_' + contSer + '_' + contVar).html('');
        $('#buttonVarHijo' + cont + '_' + contSer + '_' + contVar).hide();
        habilitarEliminar();
    }
    $('#hijo' + cont + '_' + contSer + '_' + contVar).hide();
    limpiarSelect(nombre, '0');
    if (VARIHIJOS.length > 0) {
        var id = parseInt($('select#form-variable' + cont + '_' + contSer + '_' + contVar + ' option:checked').val());
        for (var i = 0; i < VARIHIJOS.length; i++) {
            if (id == VARIHIJOS[i].IDVAR) {
                var ser = VARIHIJOS[i].HIJO;
                if (ser.length > 0) {
                    $('#hijo' + cont + '_' + contSer + '_' + contVar).show();
                    for (var j = 0; j < ser.length; j++) {
                        $('#' + nombre).append('<option value="' + ser[j].ID + '">' + ser[j].NOMBRE + '</option>');
                    }
                }
            }
        }
        $('#buttonVar' + cont + '_' + contSer).show();
    }
}

function limpiarSelect(text, mantener) {
    $("select#" + text + " option").each(function () {
        if ($(this).val() != mantener) {
            $(this).remove();
        } else {
            $('#uniform-' + text + ' span').text($(this).text());
            $('#' + text + ' option[value="' + mantener + '"]').attr("selected", true);
        }
    });
}

function AddOutput() {
    if (validar(0)) {
        bloquearOri(true, cont);
        $('#buttonSer' + cont).hide();
        $('#buttonVar' + cont + '_' + contSer).hide();
        cont++;
        contSer = 1;
        contVar = 1;
        contVarHijo = 1;
        if (cont >= 2) {
            $('#btnEliminar').show();
        }
        $('#addoutput').append('<div id="div' + cont + '"><br><div class="clearfix">' +
                '<label for="form-origen' + cont + '" class="form-label">Origen<em>*</em></label>' +
                '<div class="form-input">' +
                '<div class="selector" id="uniform-form-origen' + cont + '"><span></span>' +
                '<select style="opacity: 0;" id="form-origen' + cont + '" onchange="listarServicio(\'form-servicio' + cont + '_' + contSer + '\', 1);mostrarSelect(\'form-origen' + cont + '\');">' +
                '<option value="0" selected disabled>Select Option</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div id="ser' + cont + '" class="servVar">' +
                '<div class="clearfix">' +
                '<label for="form-servicio' + cont + '_' + contSer + '" class="form-label">Servicio<em>*</em></label>' +
                '<div class="form-input">' +
                '<div class="selector" id="uniform-form-servicio' + cont + '_' + contSer + '"><span></span>' +
                '<select style="opacity: 0;" id="form-servicio' + cont + '_' + contSer + '" class="ser" onchange="listarVariables(\'form-variable' + cont + '_' + contSer + '_' + contVar + '\', 1);mostrarSelect(\'form-servicio' + cont + '_' + contSer + '\');">' +
                '<option value="0" selected disabled>Select Option</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div id="var' + cont + '_' + contSer + '" class="servVar">' +
                '<div class="clearfix">' +
                '<label for="form-variable' + cont + '_' + contSer + '_' + contVar + '" class="form-label">Variable<em>*</em></label>' +
                '<div class="form-input">' +
                '<div class="selector" id="uniform-form-variable' + cont + '_' + contSer + '_' + contVar + '"><span></span>' +
                '<select style="opacity: 0;" id="form-variable' + cont + '_' + contSer + '_' + contVar + '" class="var" onchange="listarVariablesHijo(\'form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '\', 1);mostrarSelect(\'form-variable' + cont + '_' + contSer + '_' + contVar + '\');">' +
                '<option value="0" selected>Todo</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div id="hijo' + cont + '_' + contSer + '_' + contVar + '" style="display: none;" class="servVar">' +
                '<div class="clearfix">' +
                '<label for="form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '" class="form-label">Variable Hijo<em>*</em></label>' +
                '<div class="form-input">' +
                '<div class="selector" id="uniform-form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '"><span></span>' +
                '<select style="opacity: 0;" id="form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '" class="hijo" onchange="verificarHijo();mostrarSelect(\'form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '\');">' +
                '<option value="0" selected>Todo</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div id="addHijos' + cont + '_' + contSer + '_' + contVar + '"></div>' +
                '<br>' +
                '<div class="clearfix" id="buttonVarHijo' + cont + '_' + contSer + '_' + contVar + '" style="display: none;">' +
                '<button class="button" onclick="AddVariableHijo()">Añadir Variable Hijo</button>' +
                '<button class="button" onclick="ResVariableHijo()" id="btnEliminarHijo' + cont + '_' + contSer + '_' + contVar + '" style="display: none">Eliminar Variable Hijo</button>' +
                '</div>' +
                '</div>' +
                '<div id="addvariables' + cont + '_' + contSer + '"></div>' +
                '<div class="clearfix" id="buttonVar' + cont + '_' + contSer + '" style="display: none;">' +
                '<br>' +
                '<button class="button" onclick="AddVariable()">Añadir Variable</button>' +
                '<button class="button" onclick="ResVariable()" id="btnEliminarVar' + cont + '_' + contSer + '" style="display: none">Eliminar Variable</button>' +
                '</div>' +
                '</div>' +
                '<div id="addservicio' + cont + '"></div>' +
                '<div class="clearfix" id="buttonSer' + cont + '" style="display: none;">' +
                '<br>' +
                '<button class="button" onclick="AddServicio()">Añadir Servicio</button>' +
                '<button class="button" onclick="ResServicio()" id="btnEliminarServ' + cont + '" style="display: none">Eliminar Servicio</button>' +
                '</div></div>');
        listarOrigen2('form-origen' + cont);
        mostrarSelect('form-origen' + cont);
        mostrarSelect('form-servicio' + cont + '_' + contSer);
        mostrarSelect('form-variable' + cont + '_' + contSer + '_' + contVar);
        mostrarSelect('form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo);
    }
}

function ResOutput() {
    $('#div' + cont).remove();
    cont--;
    verificarSer(cont, 0);
    $('#buttonVar' + cont + '_' + contSer).show();
    $('#buttonSer' + cont).show();
    $('#buttonVarHijo' + cont + '_' + contSer + '_' + contVar).show();
    habilitarEliminar();
    bloquearOri(false, cont);
}

function AddServicio() {
    var id = parseInt($('select#form-origen' + cont + ' option:checked').val());
    if (id != 0) {
        bloqueaServ(true, cont, contSer);
        $('#buttonVar' + cont + '_' + contSer).hide();
        $('#buttonVarHijo' + cont + '_' + contSer + '_' + contVar).hide();
        contSer++;
        contVar = 1;
        if (contSer >= 2) {
            $('#btnEliminarServ' + cont).show();
        }
        $('#addservicio' + cont).append('<div id="divSer' + cont + '_' + contSer + '"><div class="clearfix">' +
                '<label for="form-servicio' + cont + '_' + contSer + '" class="form-label">Servicio<em>*</em></label>' +
                '<div class="form-input">' +
                '<div class="selector" id="uniform-form-servicio' + cont + '_' + contSer + '"><span></span>' +
                '<select style="opacity: 0;" id="form-servicio' + cont + '_' + contSer + '" class="ser" onchange="listarVariables(\'form-variable' + cont + '_' + contSer + '_' + contVar + '\', 1);mostrarSelect(\'form-servicio' + cont + '_' + contSer + '\');">' +
                '<option value="0" selected disabled>Select Option</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div id="var' + cont + '_' + contSer + '" class="servVar">' +
                '<div class="clearfix">' +
                '<label for="form-variable' + cont + '_' + contSer + '_' + contVar + '" class="form-label">Variable<em>*</em></label>' +
                '<div class="form-input">' +
                '<div class="selector" id="uniform-form-variable' + cont + '_' + contSer + '_' + contVar + '"><span></span>' +
                '<select style="opacity: 0;" id="form-variable' + cont + '_' + contSer + '_' + contVar + '" class="var" onchange="listarVariablesHijo(\'form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '\', 1);mostrarSelect(\'form-variable' + cont + '_' + contSer + '_' + contVar + '\');">' +
                '<option value="0" selected>Todo</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div id="hijo' + cont + '_' + contSer + '_' + contVar + '" style="display: none;" class="servVar">' +
                '<div class="clearfix">' +
                '<label for="form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '" class="form-label">Variable Hijo<em>*</em></label>' +
                '<div class="form-input">' +
                '<div class="selector" id="uniform-form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '"><span></span>' +
                '<select style="opacity: 0;" id="form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '" class="hijo" onchange="verificarHijo();mostrarSelect(\'form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '\');">' +
                '<option value="0" selected>Todo</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div id="addHijos' + cont + '_' + contSer + '_' + contVar + '"></div>' +
                '<div class="clearfix" id="buttonVarHijo' + cont + '_' + contSer + '_' + contVar + '" style="display: none;">' +
                '<br>' +
                '<button class="button" onclick="AddVariableHijo()">Añadir Variable Hijo</button>' +
                '<button class="button" onclick="ResVariableHijo()" id="btnEliminarHijo' + cont + '_' + contSer + '_' + contVar + '" style="display: none">Eliminar Variable Hijo</button>' +
                '</div>' +
                '</div>' +
                '<div id="addvariables' + cont + '_' + contSer + '"></div>' +
                '<div class="clearfix" id="buttonVar' + cont + '_' + contSer + '" style="display: none;">' +
                '<br>' +
                '<button class="button" onclick="AddVariable()">Añadir Variable</button>' +
                '<button class="button" onclick="ResVariable()" id="btnEliminarVar' + cont + '_' + contSer + '" style="display: none">Eliminar Variable</button>' +
                '</div>' +
                '</div>' +
                '</div></div>');
        listarServicio('form-servicio' + cont + '_' + contSer, 0);
        mostrarSelect('form-servicio' + cont + '_' + contSer);
        mostrarSelect('form-variable' + cont + '_' + contSer + '_' + contVar);
        mostrarSelect('form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo);
    } else {
        alert('Debe Seleccionar un Origen');
    }
}

function ResServicio() {
    $('#divSer' + cont + '_' + contSer).remove();
    contSer--;
    verificarVar(cont, contSer);
    habilitarEliminar();
    $('#buttonVar' + cont + '_' + contSer).show();
    bloqueaServ(false, cont, contSer);
}

function AddVariable() {
    var id = parseInt($('select#form-servicio' + cont + '_' + contSer + ' option:checked').val());
    if (id != 0) {
        var idvar = parseInt($('select#form-variable' + cont + '_' + contSer + '_' + contVar + ' option:checked').val());
        if (idvar != 0) {
            bloqueaVari(true, cont, contSer, contVar);
            $('#buttonVarHijo' + cont + '_' + contSer + '_' + contVar).hide();
            contVarHijo = 1;
            contVar++;
            if (contVar >= 2) {
                $('#btnEliminarVar' + cont + '_' + contSer).show();
            }
            $('#addvariables' + cont + '_' + contSer).append('<div id="divVar' + cont + '_' + contSer + '_' + contVar + '"><div class="clearfix">' +
                    '<label for="form-variable' + cont + '_' + contSer + '_' + contVar + '" class="form-label">Variable<em>*</em></label>' +
                    '<div class="form-input">' +
                    '<div class="selector" id="uniform-form-variable' + cont + '_' + contSer + '_' + contVar + '"><span></span>' +
                    '<select style="opacity: 0;" id="form-variable' + cont + '_' + contSer + '_' + contVar + '" class="var" onchange="listarVariablesHijo(\'form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '\', 1);mostrarSelect(\'form-variable' + cont + '_' + contSer + '_' + contVar + '\');">' +
                    '<option value="0" selected>Todo</option>' +
                    '</select>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '<div id="hijo' + cont + '_' + contSer + '_' + contVar + '" style="display: none;" class="servVar">' +
                    '<div class="clearfix">' +
                    '<label for="form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '" class="form-label">Variable Hijo<em>*</em></label>' +
                    '<div class="form-input">' +
                    '<div class="selector" id="uniform-form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '"><span></span>' +
                    '<select style="opacity: 0;" id="form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '" class="hijo" onchange="verificarHijo();mostrarSelect(\'form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '\');">' +
                    '<option value="0" selected>Todo</option>' +
                    '</select>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '<div id="addHijos' + cont + '_' + contSer + '_' + contVar + '"></div>' +
                    '<div class="clearfix" id="buttonVarHijo' + cont + '_' + contSer + '_' + contVar + '" style="display: none;">' +
                    '<br>' +
                    '<button class="button" onclick="AddVariableHijo()">Añadir Variable Hijo</button>' +
                    '<button class="button" onclick="ResVariableHijo()" id="btnEliminarHijo' + cont + '_' + contSer + '_' + contVar + '" style="display: none">Eliminar Variable Hijo</button>' +
                    '</div>');
            listarVariables('form-variable' + cont + '_' + contSer + '_' + contVar, 0);
            mostrarSelect('form-variable' + cont + '_' + contSer + '_' + contVar);
            mostrarSelect('form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo);
        } else {
            alert('Debe seleccionar una variable');
        }
    } else {
        alert('Debe Seleccionar un Servicio');
    }
}

function ResVariable() {
    $('#divVar' + cont + '_' + contSer + '_' + contVar).remove();
    contVar--;
    verificarH(cont, contSer, contVar);
    habilitarEliminar();
    verificarHijo();
    bloqueaVari(false, cont, contSer, contVar);
}

function AddVariableHijo() {
    var id = parseInt($('select#form-variable' + cont + '_' + contSer + '_' + contVar + ' option:checked').val());
    if (id != 0) {
        var idvH = parseInt($('select#form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + ' option:checked').val());
        if (idvH != 0) {
            bloqueaHijo(true, cont, contSer, contVar, contVarHijo);
            contVarHijo++;
            if (contVarHijo >= 2) {
                $('#btnEliminarHijo' + cont + '_' + contSer + '_' + contVar).show();
            }
            $('#addHijos' + cont + '_' + contSer + '_' + contVar).append('<div id="divHijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '"><div class="clearfix">' +
                    '<label for="form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '" class="form-label">Variable Hijo<em>*</em></label>' +
                    '<div class="form-input">' +
                    '<div class="selector" id="uniform-form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '"><span></span>' +
                    '<select style="opacity: 0;" id="form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '" class="hijo" onchange="verificarHijo();mostrarSelect(\'form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '\');">' +
                    '<option value="0" selected>Todo</option>' +
                    '</select>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>');
            listarVariablesHijo('form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo, 0);
            mostrarSelect('form-Hijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo);
        } else {
            alert('No puede ingresar más variables hijos');
        }
    } else {
        alert('Debe Seleccionar una Variable');
    }
}

function ResVariableHijo() {
    $('#divHijo' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo).remove();
    contVarHijo--;
    habilitarEliminar();
    bloqueaHijo(false, cont, contSer, contVar, contVarHijo);
}

function verificarSer(ori, tipo) {
    contSer = 0;
    $("#ser" + ori + " .ser").each(function () {
        contSer++;
    });
    if (tipo === 0) {
        verificarVar(ori, contSer);
        verificarH(ori, contSer, contVar);
    }
}

function verificarVar(ori, ser) {
    contVar = 0;
    $("#var" + ori + "_" + ser + " .var").each(function () {
        contVar++;
    });
}

function verificarH(ori, ser, vari) {
    contVarHijo = 0;
    $("#hijo" + ori + "_" + ser + "_" + vari + " .hijo").each(function () {
        contVarHijo++;
    });
}

function validar(tipo) {
    var nombre = 'demo';
    var emp = $('select#form-empresao option:checked').val();
    var user = true;
    var co = 1;
    if (tipo == 1) {
        nombre = $('#form-nombreWs').val().trim();
        for (var k = 1; k <= contUser; k++) {
            var us = $('select#form-usuarioo' + k + ' option:checked').val();
            if (us === "0") {
                user = false;
                co = k;
                break;
            }
        }
    }
    if (emp === "0") {
        alert('Debe ingresar una Empresa');
        return false;
    } else if (!user) {
        alert('Debe ingresar Usuario ' + co);
        return false;
    } else if (nombre === "") {
        alert('Debe ingresar un Nombre');
        return false;
    } else {
        var conDatos = validarArbol();
        if (conDatos) {
            return true;
        } else {
            alert('Debe Seleccionar por lo menos una variable');
            return false;
        }
    }

}

function guardarDatos() {
    if (validar(1)) {
        var nombre = $('#form-nombreWs').val().trim();
        var emp = $('select#form-empresao option:checked').val();
        var usuarios = [];
        for (var k = 1; k <= contUser; k++) {
            var us = $('select#form-usuarioo' + k + ' option:checked').val();
            usuarios.push(us);
        }
        var datos = guardarArbol();
        $.ajax({
            url: 'Svl_Datos',
            type: 'POST',
            dataType: 'json',
            data: {
                accion: 'guardarWS',
                servicios: JSON.stringify(datos),
                usuarios: JSON.stringify(usuarios),
                emp: emp,
                nombre: nombre
            },
            success: function (data, textStatus, jqXHR) {
                if (data.estado == 200) {
                    limpiarDatos(0);
                    alert('Datos Guardados Exitosamente');
                    $('#form-urlWeb').html(data.url);
                    $('#form-URLW').show();
                    buscarRutas();
                } else {
                    alert('Error al guardar los datos');
                }
            }
        });
    }
}

function limpiarDatos(tipo) {
    limpiarSelect('form-usuarioo1', '0');
    listarUser();
    $('#form-nombreWs').val('');
    cont = 1;
    contSer = 1;
    contVar = 1;
    contVarHijo = 1;
    contUser = 1;
    $('#arbolOrigen').html('');
    listarArbol();
    $('#addUsuario').html('');
}

function habilitarEliminar() {
    if (cont < 2) {
        $('#btnEliminar').hide();
    }
    if (contSer < 2) {
        $('#btnEliminarServ' + cont).hide();
    }
    if (contVar < 2) {
        $('#btnEliminarVar' + cont + '_' + contSer).hide();
    }
    if (contVarHijo < 2) {
        $('#btnEliminarHijo' + cont + '_' + contSer + '_' + contVar).hide();
    }
}

function verificarHijo() {
    $('#buttonVarHijo' + cont + '_' + contSer + '_' + contVar).show();
}

function bloquearOri(tipo, cont) {
    $("#form-origen" + cont).attr("disabled", tipo);
    for (var j = 1; j <= contSer; j++) {
        bloqueaServ(tipo, cont, j);
    }
}

function bloqueaServ(tipo, cont, serv) {
    $("#form-servicio" + cont + "_" + serv).attr("disabled", tipo);
    for (var k = 1; k <= contVar; k++) {
        bloqueaVari(tipo, cont, serv, k);
    }
}

function bloqueaVari(tipo, cont, serv, vari) {
    $("#form-variable" + cont + "_" + serv + "_" + vari).attr("disabled", tipo);
    for (var l = 1; l <= contVarHijo; l++) {
        bloqueaHijo(tipo, cont, serv, vari, l);
    }
}

function bloqueaHijo(tipo, cont, serv, vari, hijo) {
    $("#form-Hijo" + cont + "_" + serv + "_" + vari + "_" + hijo).attr("disabled", tipo);
}

function mostrarSelect(nombre) {
    var text = $('select#' + nombre + ' option:checked').text();
    $('#uniform-' + nombre + ' span').text(text);
}

function BuscarUser() {
    var idEmp = $("select#form-empresao option:checked").val();
    $.ajax({
        url: 'Svl_Usuario',
        type: 'POST',
        dataType: 'json',
        data: {accion: 'listarUsuario', idEmp: idEmp},
        success: function (data, textStatus, jqXHR) {
            if (data.estado === 200) {
                USER = data.datos;
                listarUser();
            }
        }
    });
}

function listarUser() {
    if (USER.length > 0) {
        for (var i = 0; i < USER.length; i++) {
            $('#form-usuarioo' + contUser).append('<option value="' + USER[i].id + '">' + USER[i].nombre + '</option>');
        }
        habilitarUser();
    }
}

function listarEmp() {
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
                        $('#form-empresao').append('<option value="' + data.datos[dato].ID + '">' + data.datos[dato].nom_empresa + '</option>');
                        selectedF('form-empresao', idEmp);
                        BuscarUser();
                        buscarOrigen3();
                        buscarRutas();
                        break;
                    }
//                    } else {
//                        $('#form-empresao').append('<option value="' + data.datos[dato].ID + '">' + data.datos[dato].nom_empresa + '</option>');
//                    }
                }
//                if (codigo === 23) {
//                    $('#form-empresao').attr('onchange', 'limpiarDatos(1);buscarOrigen3();BuscarUser();');
//                }
            }
        }
    });
}

function AddUser() {
    var id = parseInt($('select#form-empresao option:checked').val());
    if (id != 0) {
        contUser++;
        $('#addUsuario').append('<div id="divUser' + contUser + '">' +
                '<div class="clearfix">' +
                '<label for="form-usuarioo' + contUser + '" class="form-label">Usuario ' + contUser + '<em>*</em></label>' +
                '<div class="form-input">' +
                '<div class="selector" id="uniform-form-usuarioo' + contUser + '"><span></span>' +
                '<select id="form-usuarioo' + contUser + '" onchange="mostrarSelect(\'form-usuarioo' + contUser + '\');veriUser(this);">' +
                '<option value="0" selected disabled>Select Option</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>');
        mostrarSelect('form-usuarioo' + contUser);
        listarUser();
        habilitarUser();
    } else {
        alert('Debe ingresar una empresa');
    }
}

function ResUser() {
    $('#divUser' + contUser).remove();
    contUser--;
    habilitarUser();
}

function habilitarUser() {
    if (contUser < 2) {
        $('#btnEliminarUser').hide();
    } else if (contUser >= 2) {
        $('#btnEliminarUser').show();
    }
    if (USER.length === contUser) {
        $('#btnAgregarUser').hide();
    } else {
        $('#btnAgregarUser').show();
    }
}

function veriUser(select) {
    var sel = $(select).val();
    var id = $(select).attr('id').split('-')[1].substring(8);
    if (id != '1') {
        for (var k = 1; k < contUser; k++) {
            var us = $('select#form-usuarioo' + k + ' option:checked').val();
            if (sel === us) {
                selectedF('form-usuarioo' + contUser, '0');
                alert('El usuario ya ha sido seleccionado');
                break;
            }
        }
    }
}

function buscarRutas() {
    var idEmp = $("select#form-empresao option:checked").val();
    $('#tblWebSer').DataTable().rows().remove().draw();
    $.ajax({
        url: 'Svl_Datos',
        type: 'POST',
        dataType: 'json',
        data: {accion: 'listarWebServise', idEmp: idEmp},
        beforeSend: function (xhr) {
            $('#tblWebSer tbody').append('<tr id="trCargando" class="odd" style="text-align: center;"><td valign="top" colspan="7" class="dataTables_empty" style="text-align: center;"><i class="fa fa-spinner fa-spin"></i>  Buscando Registros</td></tr>');
        },
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                webSEr = data.datos;
            }
            listarWebServ();
        }
    });
}

function listarWebServ() {
    $('#trCargando').remove();
    $('#tblWebSer').DataTable().destroy();
    $('#tblWebSer').DataTable({
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
        "data": webSEr,
        "bSort": true,
        "columnDefs": [
            {"width": "30%", "targets": 0},
            {"width": "70%", "targets": 1}
        ],
        "columns": [
            {data: 'nombre', class: 'txt-center'},
            {data: 'ruta', class: 'txt-center'},
            {data: null, "render": function (data, type, row) {
                    return '<input type="button" id="popover-button-' + data.id + '" onclick="popupU(' + data.id + ');" class="popoverClass popover-buttonn" value="Ver"/>';
                }}
        ]
    });
    $('.popover-buttonn').popover(
            '#popover-div', {
                preventRight: true,
                preventLeft: true,
                preventBottom: true,
                preventTop: true}
    );
}

function popupU(id) {
    var user = new Array;
    for (var i = 0; i < webSEr.length; i++) {
        if (webSEr[i].id == id) {
            user = webSEr[i].user;
        }
    }
    var textU = '';
    if (user.length > 0) {
        for (var i = 0; i < user.length; i++) {
            textU += '<li class="read"><a><span class="avatar"></span>' + user[i] + '</a></li>';
        }
    }
    $('#modal-web').html((textU === '' ? 'Sin Usuarios Asignados' : textU));
}

function fontClick(font, nameli) {
    var val = $(font).attr("class");
    if (val === 'closeFont') {
        $(font).removeClass("closeFont").addClass("openFont");
        $('#' + nameli).toggle(300);
    }
    if (val === 'openFont') {
        $(font).removeClass("openFont").addClass("closeFont");
        $('#' + nameli).toggle(300);
    }
}

function listarArbol() {
    var sinDatos = true;
    var html = '<ul class="treejs">';
    if (DATOS.length > 0) {
        for (var i = 0; i < DATOS.length; i++) {
            var ser = DATOS[i].SERVICIO;
            if (ser.length > 0) {
                sinDatos = false;
                contSer = 1;
                html += '<li class="origenes" id="id_' + DATOS[i].ID + '"><i class="openFont" onclick="fontClick(this, \'origen_' + cont + '\');"  style="' + (ser.length > 0 ? '' : 'display: none;') + '"></i> ';
                html += (ser.length > 0 ? '' : '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;');
                html += '<label><div class="checker checkboxTree" id="uniform-check_' + cont + '"><span class=""><input onchange="changee(this,\'id_' + DATOS[i].ID + '\', 1);" type="checkbox" id="check_' + cont + '" /></span></div> ' + DATOS[i].NOMBRE + '</label>';
                html += (ser.length > 0 ? '<ul class="treejs" id="origen_' + cont + '">' : '');
                for (var j = 0; j < ser.length; j++) {
                    contVar = 1;
                    var vari = ser[j].VARIABLE;
                    html += '<li class="servicios" id="id_' + ser[j].ID + '"><i class="openFont" onclick="fontClick(this, \'origen_' + cont + '_' + contSer + '\');" style="' + (vari.length > 0 ? '' : 'display: none;') + '"></i> ';
                    html += (vari.length > 0 ? '' : '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;');
                    html += '<label><div class="checker checkboxTree" id="uniform-check_' + cont + '_' + contSer + '"><span class=""><input onchange="changee(this,\'id_' + ser[j].ID + '\', 2);" type="checkbox" id="check_' + cont + '_' + contSer + '" /></span></div> ' + ser[j].NOMBRE + '</label>';
                    html += (vari.length > 0 ? '<ul  class="treejs" id="origen_' + cont + '_' + contSer + '">' : '');
                    for (var k = 0; k < vari.length; k++) {
                        contVarHijo = 1;
                        var variH = vari[k].VARHIJO;
                        html += '<li class="variables" id="id_' + vari[k].ID + '"><i class="openFont" onclick="fontClick(this, \'origen_' + cont + '_' + contSer + '_' + contVar + '\');"  style="' + (variH.length > 0 ? '' : 'display: none;') + '"></i> ';
                        html += (variH.length > 0 ? '' : '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;');
                        html += '<label><div class="checker checkboxTree" id="uniform-check_' + cont + '_' + contSer + '_' + contVar + '"><span class=""><input onchange="changee(this,\'id_' + vari[k].ID + '\', 3);" type="checkbox" id="check_' + cont + '_' + contSer + '_' + contVar + '" /></span></div> ' + vari[k].NOMBRE + '</label>';
                        html += (variH.length > 0 ? '<ul  class="treejs" id="origen_' + cont + '_' + contSer + '_' + contVar + '">' : '');
                        for (var l = 0; l < variH.length; l++) {
                            html += '<li class="variHijos" id="id_' + variH[l].ID + '">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label><div class="checker checkboxTree" id="uniform-check_' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '"><span class=""><input onchange="changee(this,\'id_' + variH[l].ID + '\', 4);" type="checkbox" id="check_' + cont + '_' + contSer + '_' + contVar + '_' + contVarHijo + '" /></span></div> ' + variH[l].NOMBRE + '</label></li>';
                            contVarHijo++;
                        }
                        html += (variH.length > 0 ? '</ul>' : '');
                        html += '</li>';
                        contVar++;
                    }
                    html += (vari.length > 0 ? '</ul>' : '');
                    html += '</li>';
                    contSer++;
                }
                html += (ser.length > 0 ? '</ul>' : '');
                html += '</li>';
                cont++;
            }
        }
    }
    html += '</ul>';
    $('#arbolOrigen').html(html);
    togglee();
    if (sinDatos) {
        $('#form-arbol').show();
        $('#text-arbol').hide();
    } else {
        $('#form-arbol').hide();
        $('#text-arbol').show();
    }
}

function changee(sel, idO, tipo) {//tipo = 1: origen, 2: servicio, 3:variable, 4: variable hijo
    var check = $(sel).prop("checked");
    var asing;
    if (check) {
        $(sel).parent().addClass("checked");
        $(sel).prop("checked", true);
        asing = true;
    } else {
        $(sel).parent().removeClass("checked");
        $(sel).prop("checked", false);
        asing = false;
    }
    activarDescArbol(idO, tipo, asing);
}

function togglee() {
    for (var i = 1; i < (cont + 1); i++) {
        var contS = 0;
        $('#origen_' + i + ' .servicios').each(function () {
            var contV = 0;
            contS++;
            var idS = $(this).attr("id");
            $('#' + idS + ' .variables').each(function () {
                var contVH = 0;
                contV++;
                var idV = $(this).attr("id");
                $('#' + idV + ' .variHijos').each(function () {
                    contVH++;
                    $('#origen_' + i + '_' + contS + '_' + contV + '_' + contVH).toggle();
                });
                $('#origen_' + i + '_' + contS + '_' + contV).toggle();
            });
            $('#origen_' + i + '_' + contS).toggle();
        });
        $('#origen_' + i).toggle();
    }
}

function guardarArbol() {
    var datos = [];
    var conO = 0;
    $('#arbolOrigen .origenes').each(function () {
        var id = $(this).attr("id");
        conO++;
        var conS = 0;
        var check = $('#check_' + conO).prop("checked");

        if (check) {
            $('#' + id + ' .servicios').each(function () {
                var idSer = $(this).attr("id");
                conS++;
                var conV = 0;
                var checkS = $('#check_' + conO + '_' + conS).prop("checked");
                var json = {};

                if ($('#' + idSer + ' .variables').html() != null) {
                    var tieneV = false;

                    $('#' + idSer + ' .variables').each(function () {
                        var idvar = $(this).attr("id");
                        conV++;
                        var conVH = 0;
                        var checkV = $('#check_' + conO + '_' + conS + '_' + conV).prop("checked");
                        var json1 = {};

                        if ($('#' + idvar + ' .variHijos').html() != null) {
                            var tieneVH = false;

                            $('#' + idvar + ' .variHijos').each(function () {
                                var idvarH = $(this).attr("id");
                                conVH++;
                                var checkVh = $('#check_' + conO + '_' + conS + '_' + conV + '_' + conVH).prop("checked");
                                if (checkVh) {
                                    tieneVH = true;
                                    tieneV = true;
                                    var json2 = {};
                                    json2.idOrigen = parseInt(parseInt(id.split('_')[1]));
                                    json2.idServicio = parseInt(parseInt(idSer.split('_')[1]));
                                    json2.idVariable = parseInt(parseInt(idvar.split('_')[1]));
                                    json2.idHijo = parseInt(parseInt(idvarH.split('_')[1]));
                                    datos.push(json2);
                                }
                            });
                            if (checkV && !tieneVH) {
                                tieneV = true;
                                json1.idOrigen = parseInt(parseInt(id.split('_')[1]));
                                json1.idServicio = parseInt(parseInt(idSer.split('_')[1]));
                                json1.idVariable = parseInt(parseInt(idvar.split('_')[1]));
                                json1.idHijo = parseInt(0);
                                datos.push(json1);
                            }
                        } else {
                            if (checkV) {
                                tieneV = true;
                                json1.idOrigen = parseInt(parseInt(id.split('_')[1]));
                                json1.idServicio = parseInt(parseInt(idSer.split('_')[1]));
                                json1.idVariable = parseInt(parseInt(idvar.split('_')[1]));
                                json1.idHijo = parseInt(0);
                                datos.push(json1);
                            }
                        }

                    });

                    if (checkS && !tieneV) {
                        json.idOrigen = parseInt(parseInt(id.split('_')[1]));
                        json.idServicio = parseInt(parseInt(idSer.split('_')[1]));
                        json.idVariable = parseInt(0);
                        json.idHijo = parseInt(0);
                        datos.push(json);
                    }
                } else {
                    if (checkS) {
                        json.idOrigen = parseInt(parseInt(id.split('_')[1]));
                        json.idServicio = parseInt(parseInt(idSer.split('_')[1]));
                        json.idVariable = parseInt(0);
                        json.idHijo = parseInt(0);
                        datos.push(json);
                    }
                }

            });
        }
    });
    return datos;
}

function validarArbol() {
    var conO = 0;
    var varli = false;
    $('#arbolOrigen .origenes').each(function () {
        var id = $(this).attr("id");
        conO++;
        var check = $('#check_' + conO).prop("checked");
        if (check) {
            varli = true;
        }
    });
    return varli;
}

function activarDescArbol(idO, tipo, asing) {
    var conO = 0;
    $('#arbolOrigen .origenes').each(function () {
        var id = $(this).attr("id");
        conO++;
        var conS = 0;
        var tipo1 = false;
        if (tipo === 1) {
            tipo1 = (id === idO ? true : false);
        } else {
            tipo1 = true;
        }

        if (tipo1) {
            $('#' + id + ' .servicios').each(function () {
                var idSer = $(this).attr("id");
                conS++;
                var conV = 0;
                var tipo2 = false;
                if (tipo === 2) {
                    tipo2 = (idSer === idO ? true : false);
                } else {
                    tipo2 = true;
                }
                if (tipo2) {
                    $('#' + idSer + ' .variables').each(function () {
                        var idvar = $(this).attr("id");
                        conV++;
                        var conVH = 0;
                        var tipo3 = false;
                        if (tipo === 3) {
                            tipo3 = (idvar === idO ? true : false);
                        } else {
                            tipo3 = true;
                        }
                        if (tipo3) {
                            $('#' + idvar + ' .variHijos').each(function () {
                                conVH++;
                                if (tipo === 1 || tipo === 2 || tipo === 3) {
                                    if (asing) {
                                        $('#check_' + conO + '_' + conS + '_' + conV + '_' + conVH).parent().addClass("checked");
                                        $('#check_' + conO + '_' + conS + '_' + conV + '_' + conVH).prop("checked", true);
                                    } else {
                                        $('#check_' + conO + '_' + conS + '_' + conV + '_' + conVH).parent().removeClass("checked");
                                        $('#check_' + conO + '_' + conS + '_' + conV + '_' + conVH).prop("checked", false);
                                    }
                                }
                            });
                            if (tipo === 1 || tipo === 2) {
                                if (asing) {
                                    $('#check_' + conO + '_' + conS + '_' + conV).parent().addClass("checked");
                                    $('#check_' + conO + '_' + conS + '_' + conV).prop("checked", true);
                                } else {
                                    $('#check_' + conO + '_' + conS + '_' + conV).parent().removeClass("checked");
                                    $('#check_' + conO + '_' + conS + '_' + conV).prop("checked", false);
                                }
                            }
                        }
                    });
                    if (tipo === 1) {
                        if (asing) {
                            $('#check_' + conO + '_' + conS).parent().addClass("checked");
                            $('#check_' + conO + '_' + conS).prop("checked", true);
                        } else {
                            $('#check_' + conO + '_' + conS).parent().removeClass("checked");
                            $('#check_' + conO + '_' + conS).prop("checked", false);
                        }
                    }
                }
            });
        }
    });
    verificarNodosP(tipo);
}

function verificarNodosP(tipo) {
    var conO = 0;
    var conS = 0;
    var conV = 0;
    var conVH = 0;

    switch (tipo) {
        case 2:
        {
            $('#arbolOrigen .origenes').each(function () {
                var activarO2 = false;
                var id = $(this).attr("id");
                conO++;
                conS = 0;
                $('#' + id + ' .servicios').each(function () {
                    conS++;
                    if ($('#check_' + conO + '_' + conS).prop("checked")) {
                        activarO2 = true;
                    }
                });
                if (activarO2) {
                    $('#check_' + conO).parent().addClass("checked");
                    $('#check_' + conO).prop("checked", true);
                } else {
                    $('#check_' + conO).parent().removeClass("checked");
                    $('#check_' + conO).prop("checked", false);
                }
            });
            break;
        }
        case 3:
        {
            $('#arbolOrigen .origenes').each(function () {
                var activarO = false;
                var id = $(this).attr("id");
                conO++;
                conS = 0;
                $('#' + id + ' .servicios').each(function () {
                    var activarOS = false;
                    var idSer = $(this).attr("id");
                    conS++;
                    conV = 0;
                    $('#' + idSer + ' .variables').each(function () {
                        conV++;
                        if ($('#check_' + conO + '_' + conS + '_' + conV).prop("checked")) {
                            activarOS = true;
                            activarO = true;
                        }
                    });
                    if (activarOS) {
                        $('#check_' + conO + '_' + conS).parent().addClass("checked");
                        $('#check_' + conO + '_' + conS).prop("checked", true);
                    } else {
                        $('#check_' + conO + '_' + conS).parent().removeClass("checked");
                        $('#check_' + conO + '_' + conS).prop("checked", false);
                    }
                });
                if (activarO) {
                    $('#check_' + conO).parent().addClass("checked");
                    $('#check_' + conO).prop("checked", true);
                } else {
                    $('#check_' + conO).parent().removeClass("checked");
                    $('#check_' + conO).prop("checked", false);
                }
            });
            break;
        }
        case 4:
        {
            $('#arbolOrigen .origenes').each(function () {
                var activarO = false;
                var id = $(this).attr("id");
                conO++;
                conS = 0;
                $('#' + id + ' .servicios').each(function () {
                    var activarOS = false;
                    var idSer = $(this).attr("id");
                    conS++;
                    conV = 0;
                    $('#' + idSer + ' .variables').each(function () {
                        var activarOSV = false;
                        var idvar = $(this).attr("id");
                        conV++;
                        conVH = 0;
                        if ($('#' + idvar + ' .variHijos').html() != null) {
                            $('#' + idvar + ' .variHijos').each(function () {
                                conVH++;
                                if ($('#check_' + conO + '_' + conS + '_' + conV + '_' + conVH).prop("checked")) {
                                    activarOSV = true;
                                    activarO = true;
                                    activarOS = true;
                                }
                            });
                        } else {
                            if ($('#check_' + conO + '_' + conS + '_' + conV).prop("checked")) {
                                activarOSV = true;
                                activarO = true;
                                activarOS = true;
                            }
                        }
                        if (activarOSV) {
                            $('#check_' + conO + '_' + conS + '_' + conV).parent().addClass("checked");
                            $('#check_' + conO + '_' + conS + '_' + conV).prop("checked", true);
                        } else {
                            $('#check_' + conO + '_' + conS + '_' + conV).parent().removeClass("checked");
                            $('#check_' + conO + '_' + conS + '_' + conV).prop("checked", false);
                        }
                    });
                    if (activarOS) {
                        $('#check_' + conO + '_' + conS).parent().addClass("checked");
                        $('#check_' + conO + '_' + conS).prop("checked", true);
                    } else {
                        $('#check_' + conO + '_' + conS).parent().removeClass("checked");
                        $('#check_' + conO + '_' + conS).prop("checked", false);
                    }
                });
                if (activarO) {
                    $('#check_' + conO).parent().addClass("checked");
                    $('#check_' + conO).prop("checked", true);
                } else {
                    $('#check_' + conO).parent().removeClass("checked");
                    $('#check_' + conO).parent().prop("checked", false);
                }
            });
            break;
        }
    }
}