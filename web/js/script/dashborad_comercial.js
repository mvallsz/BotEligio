console.log("Entra");
var Dataaa = [];
$(document).ready(function () {
    addAnio();
    listarEmpresas();
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
            $('#tableConsultas').DataTable().rows().remove().draw();
            var anio = $("#form-anio option:selected").val();
            var mes = $("#form-mes option:selected").val();
            var bureau = $("#form-bureauu option:selected").val();
            var empresa = $("#form-empresa1 option:selected").val();
            $.ajax({
                url: 'Svl_Servicios',
                type: 'POST',
                dataType: 'json',
                data: {
                    accion: 'consultasMes',
                    mes: mes,
                    anio: anio,
                    bureau: bureau,
                    empresa: empresa
                },
                beforeSend: function (xhr) {
                    $('#tableConsultas tbody').append('<tr id="trCargando" class="odd" style="text-align: center;"><td valign="top" colspan="7" class="dataTables_empty" style="text-align: center;"><i class="fa fa-spinner fa-spin"></i>  Buscando Registros</td></tr>');
                },
                success: function (data) {
                    if (data.estado === 200) {
                        Dataaa = data.datos;
                    }
                    listarConsultas();
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

function listarConsultas() {
    $('#trCargando').remove();
    $('#tableConsultas').DataTable().destroy();
    $('#tableConsultas').DataTable({
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
        "data": Dataaa,
        "bSort": true,
        "columnDefs": [
            {"width": "25%", "targets": 0},
            {"width": "30%", "targets": 1},
            {"width": "25%", "targets": 2},
            {"width": "20%", "targets": 3}
        ],
        "columns": [
            {data: 'ORIGEN', class: 'txt-center'},
            {data: 'SERVICIO', class: 'txt-center'},
            {data: 'USUARIO', class: 'txt-center'},
            {data: 'FECHA', class: 'txt-center'},
            {data: null, "render": function (data, type, row) {
                    if (data.BUSCAR_DATOS === 1) {
                        return "Servicio";
                    } else {
                        return "Cache";
                    }
                }}
        ]
    });
}


