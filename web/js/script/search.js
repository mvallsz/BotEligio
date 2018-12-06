//console.log('search');
var SERV = [];
var contS = 1;
var DATASERV = [];
$(document).ready(function () {
    listarEmpresaSearch();
});

function listarEmpresaSearch() {
    $.ajax({
        url: 'Svl_Servicios',
        type: 'POST',
        dataType: 'json',
        data: {accion: 'listarEmpresas'},
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                for (var dato in data.datos) {
                    if (codigo != 23) {
                        if (data.datos[dato].ID === idEmp) {
                            $('#form-empresaS').append('<option value="' + data.datos[dato].ID + '">' + data.datos[dato].nom_empresa + '</option>');
                            selectedF('form-empresaS', idEmp);
                            listarServ();
                            break;
                        }
                    } else {
                        $('#form-empresaS').append('<option value="' + data.datos[dato].ID + '">' + data.datos[dato].nom_empresa + '</option>');
                    }
                }
                if (codigo === 23) {
                    $('#form-empresaS').attr('onchange', 'listarServ();');
                }
            }
        }
    });
}

function listarServ() {
    var emp = $("select#form-empresaS option:checked").val();
    $.ajax({
        url: 'Svl_Datos',
        type: 'POST',
        dataType: 'json',
        data: {accion: 'listarServWeb', emp: emp},
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                SERV = data.datos;
                addSerr();
            }
        }
    });
}

function limpiarSelectS(text, mantener) {
    $("select#" + text + " option").each(function () {
        if ($(this).val() != mantener) {
            $(this).remove();
        } else {
            $('#uniform-' + text + ' span').text($(this).text());
            $('#' + text + ' option[value="' + mantener + '"]').attr("selected", true);
        }
    });
}

function searchRut() {
    var emp = $("select#form-empresaS option:checked").val();
    var rut = $('#form-rut').val();
    var dv = $('#form-dv').val();
    var servicios = [];
    $("#addServ input").each(function () {
        if ($(this).is(":checked")) {
            var id = $(this).attr('id').split('-');
            if (id != "0") {
                servicios.push(parseInt(id[2]));
            }
        }
    });
    if (emp === '0') {
        alert('Debe Seleccionar una empresa para continuar');
    } else if (rut === "") {
        alert('Debe ingresar Rut para continuar');
    } else if (servicios.length === 0) {
        alert('Debe al menos elegir un servicio');
    } else {
        $('#infoS p').html('Buscando Información del Rut ' + rut + '-' + dv + ' <i class="fa fa-spinner fa-pulse fa-fw"></i>');
        $('.ocult').hide();
        $('#infoS').show();
        limpiar();
        $.ajax({
            url: 'Svl_Datos',
            type: 'POST',
            dataType: 'json',
            data: {accion: 'buscarDatos',
                rut: rut,
                dv: dv,
                servicios: JSON.stringify(servicios),
                emp: emp
            },
            success: function (data, textStatus, jqXHR) {
                if (data.estado == 200) {
                    mostrarData(data.datos);
                } else {
                    $('#infoS').hide();
                    $('.ocult').show();
                    $('#atrasS').show();
                    $('#errorS p').val('Problemas al Buscar Información del Rut ' + rut + '-' + dv);
                    alert('Error al buscar Rut');
                }
            }
        });
    }
}

function limpiar() {
    $('#form-rut').val('');
    $('#form-dv').val('');
    $("#addServ input").each(function () {
        if ($(this).is(":checked")) {
            $(this).parent().removeClass("checked");
            $(this).prop("checked", false);
        }
    });
    $("#form-serv-0").prop('checked', false);
    $("#form-serv-0").parent().removeClass("checked");
    if (codigo === 23) {
        selectedF('form-empresaS', 0);
    }
}

function addSerr() {
    $("#form-serv-0").prop('checked', false);
    $("#form-serv-0").parent().removeClass("checked");
    $('#addServ').html('');
    var tipo = 1;
    if (parseInt($('#form-rut').val().replace(/\./g, '')) > 50000000) {
        tipo = 2;
    }
    for (var i = 0; i < SERV.length; i++) {
        if (SERV[i].TIPO === 3 || SERV[i].TIPO === tipo) {
            $('#addServ').append('<label style="font-size: 12px;' + (SERV[i].ACTIVO === 0 ? 'text-decoration: line-through;' : '') + '"><div class="checker" id="uniform-form-serv-' + SERV[i].ID + '"><span class=""><input type="checkbox" onchange="chang(this);" id="form-serv-' + SERV[i].ID + '" /></span></div>' + SERV[i].ORIGEN + '/' + SERV[i].NOMBRE + ' </label><br><br>');
            if (SERV[i].ACTIVO === 0) {
                $('#form-serv-' + SERV[i].ID).attr('disabled', true);
            }
        }
    }
}

var contt = 0;
function mostrarData(data) {
    $('#addt').append('<div class="tabs leading">' +
            '<ul class="clearfix">' +
            '</ul>' +
            '<section>' +
            '</section>' +
            '</div>');
    contt = 0;
    for (var item in data) {
        $('#addt .tabs ul').append('<li><a href="#" ' + (contt == 0 ? 'class="current"' : '') + ' id="li_' + contt + '">' + item + ' <i class="fa fa-spinner fa-pulse fa-fw"></i></a></li>');
        $('#addt .tabs section').append('<section class="clearfix" ' + (contt == 0 ? '' : 'style="display: none;"') + ' id="tab_' + contt + '"></section>');
        var dataa = data[item];
        var con = 0;
        var json = {};
        json.val = 'tab_' + contt;
        var html = '';
        for (var item2 in dataa) {
            $('#addt .tabs section #tab_' + contt).append('<header class="alpha omega">' +
                    '<h5>' + item2 + '</h5>' +
                    '</header>' +
                    '<div id="idp' + con + '"></div><br>');
            setTableObject(dataa[item2], ('#addt .tabs section #tab_' + contt + ' #idp' + con), '');
//            await sleep(3000);
            html += $('#addt .tabs section #tab_' + contt).html();
            $('#addt .tabs section #tab_' + contt).html('');
            con++;
        }
        json.datos = html;
        $('#li_' + contt).attr('onclick', 'mostrarTab(this);');
        $('#li_' + contt).html(item);
        DATASERV.push(json);
        if (contt === 0) {
            $('#addt').show();
            $('#atrasS').show();
            $('#atrasS').attr('disabled', true);
            $('#addt .tabs section #tab_0').html(DATASERV[0].datos);
        }
        contt++;
    }
    $('#infoS').hide();
    $('#atrasS').attr('disabled', false);
}

function mostrarTab(sel) {
    $("#addt .tabs ul li a").each(function () {
        if ($(this).attr('class') === 'current') {
            $(this).removeClass('current');
            var id = $(this).attr('id').split('_');
            $('#addt .tabs section #tab_' + id[1]).hide();
            $('#addt .tabs section #tab_' + id[1]).html('');
        }
    });
    $(sel).addClass('current');
    var id1 = $(sel).attr('id').split('_');
    $('section #tab_' + id1[1]).show();
    for (var i = 0; i < DATASERV.length; i++) {
        if (DATASERV[i].val === ('tab_' + id1[1])) {
            $('#addt .tabs section #tab_' + id1[1]).html(DATASERV[i].datos);
        }
    }
}

function volverSearch() {
    DATASERV = [];
    $('#atrasS').hide();
    $('.ocult').show();
    $('#addt').html('');
    $('.ocult form').show();
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function chang(sel) {
    var check = $(sel).prop("checked");
    if (check) {
        $(sel).parent().addClass("checked");
    } else {
        $(sel).parent().removeClass("checked");
    }
    $("#form-serv-0").prop('checked', false);
    $("#form-serv-0").parent().removeClass("checked");
}

$("#form-serv-0").change(function () {
    var check = $(this).prop("checked");
    $("#addServ input").each(function () {
        if (!$(this).attr('disabled')) {
            if (check) {
                $(this).parent().addClass("checked");
            } else {
                $(this).parent().removeClass("checked");
            }
            $(this).prop("checked", check);
        }
    });
});