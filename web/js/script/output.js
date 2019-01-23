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
                ORIGENES = [];
                SERVICIOS = [];
                VARIABLES = [];
                VARIHIJOS = [];
                if (DATOS.length > 0) {
                    for (var i = 0; i < DATOS.length; i++) {
                        var jsonori = {};
                        jsonori.ID = DATOS[i].ID;
                        jsonori.NOMBRE = DATOS[i].NOMBRE;
                        ORIGENES.push(jsonori);
                        var ser = DATOS[i].SERVICIO;
                        var serv = [];
                        var serv1 = {};
                        for (var j = 0; j < ser.length; j++) {
                            var jsonvar = {};
                            jsonvar.ID = ser[j].ID;
                            jsonvar.NOMBRE = ser[j].NOMBRE;
                            serv.push(jsonvar);
                            var vari = ser[j].VARIABLE;
                            var variv = [];
                            var vari1 = {};
                            for (var k = 0; k < vari.length; k++) {
                                var jsonvari = {};
                                jsonvari.ID = vari[k].ID;
                                jsonvari.NOMBRE = vari[k].NOMBRE;
                                variv.push(jsonvari);
                                var variH = vari[k].VARHIJO;
                                var variHv = [];
                                var variH1 = {};
                                for (var l = 0; l < variH.length; l++) {
                                    var jsonvariH = {};
                                    jsonvariH.ID = variH[l].ID;
                                    jsonvariH.NOMBRE = variH[l].NOMBRE;
                                    variHv.push(jsonvariH);
                                }
                                variH1.IDVAR = vari[k].ID;
                                variH1.HIJO = variHv;
                                VARIHIJOS.push(variH1);
                            }
                            vari1.IDSER = ser[j].ID;
                            vari1.VARIABLE = variv;
                            VARIABLES.push(vari1);
                        }
                        serv1.IDORI = DATOS[i].ID;
                        serv1.SERVICIO = serv;
                        SERVICIOS.push(serv1);
                    }
                }
                listarOrigen2('form-origen1');
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
        var sinDatos = false;
        for (var i = 1; i <= cont; i++) {
            var origen = $("select#form-origen" + i + " option:checked").val();
            if (origen === "0") {
                alert('Debe ingresar el Origen');
                sinDatos = true;
            } else {
                verificarSer(i);
                if (contSer > 0) {
                    for (var j = 1; j <= contSer; j++) {
                        var servicio = $("select#form-servicio" + i + "_" + j + " option:checked").val();
                        if (servicio === "0") {
                            alert('Debe ingresar el Servicio');
                            sinDatos = true;
                        } else {
                            verificarVar(i, j);
                            if (contVar > 0) {
                                for (var k = 1; k <= contVar; k++) {
                                    var variable = $("select#form-variable" + i + "_" + j + "_" + k + " option:checked").val();
                                    if (variable === "0" && k > 1) {
                                        alert('Debe ingresar una variable');
                                        sinDatos = true;
                                    } else {
                                        verificarH(i, j, k);
                                        for (var l = 1; l <= contVarHijo; l++) {
                                            var hijo = $("select#form-Hijo" + i + "_" + j + "_" + k + "_" + l + " option:checked").val();
                                            if (hijo === "0" && l > 1) {
                                                alert('Debe ingresar Variable Hijo');
                                                sinDatos = true;
                                            }
                                        }
                                    }
                                    if (sinDatos) {
                                        break;
                                    }
                                }
                            }
                        }
                        if (sinDatos) {
                            break;
                        }
                    }
                } else {
                    sinDatos = true;
                    break;
                }
            }
            if (sinDatos) {
                break;
            }
        }

        if (sinDatos) {
            return false;
        } else {
            return true;
        }
    }

}
//var datos = [];
function guardarDatos() {
    if (validar(1)) {
        var nombre = $('#form-nombreWs').val().trim();
        var emp = $('select#form-empresao option:checked').val();
        var usuarios = [];
        for (var k = 1; k <= contUser; k++) {
            var us = $('select#form-usuarioo' + k + ' option:checked').val();
            usuarios.push(us);
        }
        var datos = [];
        for (var i = 1; i <= cont; i++) {
            var origen = $("select#form-origen" + i + " option:checked").val();
            verificarSer(i);
            for (var j = 1; j <= contSer; j++) {
                var servicio = $("select#form-servicio" + i + "_" + j + " option:checked").val();
                verificarVar(i, j);
                if (contVar > 0) {
                    for (var k = 1; k <= contVar; k++) {
                        var variable = $("select#form-variable" + i + "_" + j + "_" + k + " option:checked").val();
                        verificarH(i, j, k);
                        if (contVarHijo > 0) {
                            for (var l = 1; l <= contVarHijo; l++) {
                                var hijo = $("select#form-Hijo" + i + "_" + j + "_" + k + "_" + l + " option:checked").val();
                                var json2 = {};
                                json2.idOrigen = parseInt(origen);
                                json2.idServicio = parseInt(servicio);
                                json2.idVariable = parseInt(variable);
                                json2.idHijo = parseInt(hijo);
                                datos.push(json2);
                            }
                        } else {
                            var json1 = {};
                            json1.idOrigen = parseInt(origen);
                            json1.idServicio = parseInt(servicio);
                            json1.idVariable = parseInt(variable);
                            json1.idHijo = parseInt(0);
                            datos.push(json1);
                        }
                    }
                } else {
                    var json = {};
                    json.idOrigen = parseInt(origen);
                    json.idServicio = parseInt(servicio);
                    json.idVariable = parseInt(0);
                    json.idHijo = parseInt(0);
                    datos.push(json);
                }
            }
        }
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
    $('#form-nombreWs').val('');
    cont = 1;
    contSer = 1;
    contVar = 1;
    contVarHijo = 1;
    contUser = 1;
    limpiarSelect('form-origen1', '0');
    limpiarSelect('form-servicio1_1', '0');
    limpiarSelect('form-variable1_1_1', '0');
    limpiarSelect('form-Hijo1_1_1_1', '0');
    $('#hijo1_1_1').hide();
    $('#addoutput').html('');
    $('#addservicio1').html('');
    $('#addvariables1_1').html('');
    $('#addHijos1_1_1').html('');
    $('#addUsuario').html('');

    $('#buttonVarHijo1_1_1').hide();
    $('#buttonVar1_1').hide();
    $('#buttonSer1').hide();
    habilitarEliminar();
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
    for (var k = 1; k < contUser; k++) {
        var us = $('select#form-usuarioo' + k + ' option:checked').val();
        if (sel === us) {
            selectedF('form-usuarioo' + contUser, '0');
            alert('El usuario ya ha sido seleccionado');
            break;
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