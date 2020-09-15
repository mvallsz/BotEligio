console.log("Entra a dashboard_comercial.js");
var DataAct = [];
var DataDisp = [];
var DataHist = [];

$(document).ready(function () {
    addAnio();
    initTables1(1);
    initTables1(2);
    initTables1(3);
    listarServRPA();
    $(function() {
        $("#tooltip-1").tooltip();
        $("#tooltip-2").tooltip();
    });
    
//Plot 1
    var Bureau1 = [0, 0, 0, 0, 0, 0, 0, 0, 0];
    var Bureau2 = [0, 0, 0, 0, 0, 0, 0, 0, 0];
    
    var tickslabel = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    var chart2 = $.jqplot('chart1', [Bureau1, Bureau2], {
        title: 'Consulta Fuentes / Mes',
        legend: {
            show: true,
            labels: ['FUENTE DE TERCEROS', 'SUNAT'],
            location: 'ne'
        },
        axes: {
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: tickslabel
            },
            yaxis: {
                tickOptions: {
//                    formatString: '$%.2f'
                    formatString: '%.0f'
                }
            }
        },
        highlighter: {
            show: true,
            sizeAdjust: 7.5,
            tooltipAxes: 'y'
        },
        cursor: {
            zoom: true,
            looseZoom: true
        }
    });

//Plot 2
    var line1 = [0, 0, 0, 0, 0, 0, 0, 0, 0];
    var line2 = [0, 0, 0, 0, 0, 0, 0, 0, 0];
    var tickslabel2 = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    var plot2 = $.jqplot('chart2', [line1, line2], {
        title: 'Consulta Fuente / Cache (Mes)',
        show: true,
        legend: {show: true, location: 'ne', xoffset: 0},
        seriesDefaults: {
            renderer: $.jqplot.BarRenderer,
            rendererOptions: {barPadding: 8, barMargin: 10, shadowDepth: 2}
        },
        series: [
            {label: 'Fuente'},
            {label: 'Cache'}
        ],
        axes: {
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: tickslabel2
            },
            yaxis: {min: 0}
        }
    });

//Plot 3
    var total = [['Utilizado', 5], ['Libre', 95]];
    var chart3 = jQuery.jqplot('chart3', [total], {
        seriesDefaults: {
            renderer: jQuery.jqplot.PieRenderer,
            rendererOptions: {showDataLabels: true}
        },
        legend: {
            show: true,
            location: 'e'
        }
    });
});

function initTables1(tablaN) {
    switch (tablaN) {
        case 1:
            $('#tableHistoricoRPA').DataTable().rows().remove().draw();
            var ServRPA = $("#form-servRPA option:selected").val();
            $.ajax({
                url: 'Svl_Servicios',
                type: 'POST',
                dataType: 'json',
                data: {
                    accion: 'consultasHistoricas',
                    idServ: ServRPA
                    
                },
                beforeSend: function (xhr) {
                    $('#tableHistoricoRPA tbody').append('<tr id="trCargando" class="odd" style="text-align: center;"><td valign="top" colspan="7" class="dataTables_empty" style="text-align: center;"><i class="fa fa-spinner fa-spin"></i>  Buscando Registros</td></tr>');
                },
                success: function (data) {
                    if (data.estado === 200) {
                        DataHist = data.datos;
                    }
                    listarServHist();
                }
            });
            break;
        case 2:
            $('#tableServiciosAct').DataTable().rows().remove().draw();
            $.ajax({
                url: 'Svl_Servicios',
                type: 'POST',
                dataType: 'json',
                data: {
                    accion: 'consultasServAct'
                },
                beforeSend: function (xhr) {
                    $('#tableServiciosAct tbody').append('<tr id="trCargando" class="odd" style="text-align: center;"><td valign="top" colspan="7" class="dataTables_empty" style="text-align: center;"><i class="fa fa-spinner fa-spin"></i>  Buscando Registros</td></tr>');
                },
                success: function (data) {
                    if (data.estado === 200) {
                        DataAct = data.datos;
                    }
                    listarServAct();
                }
            });
            break;
        case 3:
            $('#tableServiciosDisp').DataTable().rows().remove().draw();
            $.ajax({
                url: 'Svl_Servicios',
                type: 'POST',
                dataType: 'json',
                data: {
                    accion: 'consultasServDisp'
                },
                beforeSend: function (xhr) {
                    $('#tableServiciosDisp tbody').append('<tr id="trCargando" class="odd" style="text-align: center;"><td valign="top" colspan="7" class="dataTables_empty" style="text-align: center;"><i class="fa fa-spinner fa-spin"></i>  Buscando Registros</td></tr>');
                },
                success: function (data) {
                    if (data.estado === 200) {
                        DataDisp = data.datos;
                    }
                    listarServDisp();
                }
            });
            break;
    }
}

function initBureauActivo() {
    $('#divBureauActivo').html('');
    $.ajax({
        url: 'Svl_Servicios',
        type: 'POST',
        dataType: 'json',
        data: {
            accion: 'buscarServiciosActivos',
            idEmpresa: $("#form-empresa1 option:selected").val()
        },
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                for (var i = 0; i < data.datos.length; i++) {
                    $('#divBureauActivo').append('<h3 style="margin-bottom: 0px;">' + data.datos[i].NOMBRE_ORIGEN + '</h3>');
                }
            } else {
                alert("No se pudo verificar los bureaus activos");
                $('#divBureauActivo').append('<h3 style="margin-bottom: 0px;">Sin Bureaus Activos</h3>');
            }
        }
    });
}

function addAnio() {
    var f = new Date();
    var anioAct = f.getFullYear();
    var mesAct = f.getMonth() + 1;
    for (var sumanio = 2016; sumanio <= anioAct; sumanio++) {
        $("#form-anio").append(new Option(sumanio, sumanio));
    }
    $("#form-anio option").each(function () {
        if ($(this).attr('value') == anioAct) {
            $(this).attr('selected', 'selected');
        }
    });
    $("#form-mes option").each(function () {
        if ($(this).attr('value') == mesAct) {
            $(this).attr('selected', 'selected');
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
                        $('#form-empresa1').append('<option value="' + data.datos[dato].ID + '">' + data.datos[dato].nom_empresa + '</option>');
                        selectedF('form-empresa1', idEmp);
                        initTables1(1);
                        initBureauActivo();
                        listarOrigen1();
                        break;
                    }
//                    } else {
//                        $('#form-empresa1').append('<option value="' + data.datos[dato].ID + '">' + data.datos[dato].nom_empresa + '</option>');
//                    }
                }
//                if (codigo === 23) {
//                    $('#form-empresa1').attr('onchange', 'initTables1(1);initBureauActivo();listarOrigen1();');
//                    $('#divBureauActivo').append('<h3 style="margin-bottom: 0px;">Sin datos</h3>');
//                    $('#tableConsultas').DataTable().rows().remove().draw();
//                    $('#tableConsultas tbody').append('<tr id="trCargando" class="odd" style="text-align: center;"><td valign="top" colspan="7" class="dataTables_empty" style="text-align: center;"><i class="fa fa-spinner fa-spin"></i>  Buscando Registros</td></tr>');
//                    listarConsultas();
//                }
            }
        }
    });
}

function listarOrigen1() {
    limpiarSelectF('form-bureauu', 0);
    var idEmp = $("#form-empresa1 option:selected").val();
    $.ajax({
        url: 'Svl_Servicios',
        type: 'POST',
        dataType: 'json',
        data: {accion: 'listarOrigenes2', idEmp: idEmp},
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                for (var dato in data.datos) {
                    $('#form-bureauu').append('<option value="' + data.datos[dato].ID + '">' + data.datos[dato].nombre_origen + '</option>');
                }
            }
        }
    });
}

function activarServ(idServ, activo, tabla) {
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
                initTables(tabla);
            } else {
                alert("No se puedo actualizar el servicio");
            }
        }
    });
}

function apagarHilo(idHilo) {
    $.ajax({
        url: 'Svl_Servicios',
        type: 'POST',
        dataType: 'json',
        data: {
            accion: 'apagarHilo',
            idHilo: idHilo
        },
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                alert("Hilo apagado");
                initTables1(2);
            } else {
                alert("No se puede apagar el hilo");
            }
        }
    });
}

function iniciarServ(idServ) {
        $.ajax({
        url: 'Svl_Servicios',
        type: 'POST',
        dataType: 'json',
        data: {
            accion: 'iniciarServicio',
            idServ: idServ
            
        },
        success: function (data, textStatus, jqXHR) {
            if (data.estado === 200) {
                alert("Se ha iniciado de forma exitosa la activación del hilo para el servicio: "+idServ);
                $('#tableServiciosAct tbody').append('<tr id="trCargando" class="odd" style="text-align: center;"><td valign="top" colspan="7" class="dataTables_empty" style="text-align: center;"><i class="fa fa-spinner fa-spin"></i>  El hilo se esta Activando...</td></tr>');
                setTimeout(() => {  initTables1(2); }, 20000);
                
            } else {
                alert("No se puede activar el hilo, contacto con el servicio tecnico del sistema");
            }
        }
    });
}

function listarServAct() {
    $('#trCargando').remove();
    $('#tableServiciosAct').DataTable().destroy();
    $('#tableServiciosAct').DataTable({
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
        "data": DataAct,
        "bSort": true,
        "columnDefs": [
            {"width": "20%", "targets": 0},
            {"width": "20%", "targets": 1},
            {"width": "25%", "targets": 2},
            {"width": "25%", "targets": 3},
            {"width": "10%", "targets": 4}
        ],
        "columns": [
            {data: 'nombre', class: 'txt-center'},
            {data: 'ejecuciones', class: 'txt-center'},
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
            {data: 'fecha_creacion', class: 'txt-center'},
            {data: null, "render": function (data, type, row) {
                    var action = "";
                    action = action + '<button title="Desactivar" onclick="apagarHilo(' + data.id + ')"><i class="fa fa-ban"></i></button>';
                    return action;
                    
                    if (data.estado === 1) {
                        return "Activo";
                    } else {
                        return "Desactivo";
                    }
                }}
        ]
    });
}

function listarServDisp() {
    $('#trCargando').remove();
    $('#tableServiciosDisp').DataTable().destroy();
    $('#tableServiciosDisp').DataTable({
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
        "data": DataDisp,
        "bSort": true,
        "columnDefs": [
            {"width": "20%", "targets": 0},
            {"width": "20%", "targets": 1},
            {"width": "25%", "targets": 2},
            {"width": "25%", "targets": 3},
            {"width": "10%", "targets": 4}
        ],
        "columns": [
            {data: 'nombre', class: 'txt-center'},
            {data: 'email_notification', class: 'txt-center'},
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
            {data: 'fecha_creacion', class: 'txt-center'},
            {data: null, "render": function (data, type, row) {
                    var action = "";
                    action = action + '<button title="Iniciar" onclick="iniciarServ(' + data.id + ')"><i class="fas fa-robot"></i></button>';
                    action = action + '<button title="Desactivar" onclick="activarServ(' + data.id + ', 0, 3)"><i class="fa fa-ban"></i></button>';
                    return action;
                    
                    if (data.estado === 1) {
                        return "Activo";
                    } else {
                        return "Desactivo";
                    }
                }}
        ]
    });
}

function listarServHist() {
    $('#trCargando').remove();
    $('#tableHistoricoRPA').DataTable().destroy();
    $('#tableHistoricoRPA').DataTable({
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
        "data": DataHist,
        "bSort": true,
        "columnDefs": [
            {"width": "15%", "targets": 0},
            {"width": "20%", "targets": 1},
            {"width": "20%", "targets": 2},
            {"width": "15%", "targets": 3},
            {"width": "10%", "targets": 4},
            {"width": "10%", "targets": 5},
            {"width": "10%", "targets": 6}
        ],
        "columns": [
            {data: 'nombre', class: 'txt-center'},
            {data: 'ejecuciones', class: 'txt-center'},
            {data: 'ejecuciones_exitosas', class: 'txt-center'},
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
            {data: 'fecha_creacion', class: 'txt-center'},
            {data: 'fecha_fin', class: 'txt-center'}
            
        ]
    });
}

function listarServRPA() {
    $.ajax({
        url: 'Svl_Servicios',
        type: 'POST',
        dataType: 'json',
        data: {accion: 'consultasServDisp'},
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                for (var dato in data.datos) {
                        $('#form-servRPA').append('<option value="' + data.datos[dato].id + '">' + data.datos[dato].nombre + '</option>');
                        
                }
            }
        }
    });
}