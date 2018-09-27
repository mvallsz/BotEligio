$(document).ready(function () {
    initBureauActivo();
    addAnio();
    initTables(1);
//Plot 1
    var Bureau1 = [50, 70, 90, 100, 120, 140, 160, 190, 250];
    var Bureau2 = [243, 345, 465, 344, 200, 250, 455, 254, 145];
    var Bureau3 = [234, 243, 234, 345, 345, 145, 125, 130, 100];
    var Bureau4 = [0, 0, 5, 10, 8, 2, 0, 0, 0];
    var tickslabel = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    var chart2 = $.jqplot('chart1', [Bureau1, Bureau2, Bureau3, Bureau4], {
        title: 'Consulta Bureau / Mes',
        legend: {
            show: true,
            labels: ['Sinacofi', 'Equifax', 'TransUnion', 'Siisa'],
            location: 'ne'
        },
        axes: {
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: tickslabel
            },
            yaxis: {
                tickOptions: {
                    formatString: '$%.2f'
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
    var line1 = [600, 700, 650, 600, 500, 400, 500, 600, 550];
    var line2 = [50, 100, 200, 251, 300, 500, 450, 400, 600];
    var tickslabel2 = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    var plot2 = $.jqplot('chart2', [line1, line2], {
        title: 'Consulta Bureau / Cache (Mes)',
        show: true,
        legend: {show: true, location: 'ne', xoffset: 0},
        seriesDefaults: {
            renderer: $.jqplot.BarRenderer,
            rendererOptions: {barPadding: 8, barMargin: 10, shadowDepth: 2}
        },
        series: [
            {label: 'Bureau'},
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
    var total = [['Utilizado', 10], ['Libre', 90]];
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

function initTables(tablaN) {
    switch (tablaN) {
        case 1:
            var anio = $("#form-anio option:selected").val();
            var mes = $("#form-mes option:selected").val();
            var bureau = $("#form-bureau option:selected").val();
            $.ajax({
                url: 'Svl_Servicios',
                type: 'POST',
                dataType: 'json',
                data: {
                    accion: 'consultasMes',
                    mes: mes,
                    anio: anio,
                    bureau: bureau
                },
                beforeSend: function (xhr) {
                    $('#tableConsultas tbody').append('<tr id="trCargando" class="odd" style="text-align: center;"><td valign="top" colspan="7" class="dataTables_empty" style="text-align: center;"><i class="fa fa-spinner fa-spin"></i>  Buscando Registros</td></tr>');
                },
                success: function (data) {
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
                        "data": data.datos,
                        "bSort": true,
                        "columnDefs": [
                            {"width": "25%", "targets": 0},
                            {"width": "30%", "targets": 1},
                            {"width": "25%", "targets": 2},
                            {"width": "20%", "targets": 3}
                        ],
                        "columns": [
                            {data: 'BUREAU', class: 'txt-center'},
                            {data: 'SERVICIO', class: 'txt-center'},
                            {data: 'USUARIO', class: 'txt-center'},
                            {data: 'FECHA', class: 'txt-center'},
                            {data: null, "render": function (data, type, row) {
                                    if (data.BUSCAR_DATOS === 1) {
                                        return "Bureau";
                                    } else {
                                        return "Cache";
                                    }
                                }}
                        ]
                    });
                }
            });
            break;
    }
}

function initBureauActivo() {
    $.ajax({
        url: 'Svl_Servicios',
        type: 'POST',
        dataType: 'json',
        data: {
            accion: 'buscarServiciosActivos'
        },
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                for (var i = 0; i < data.datos.length; i++) {
                    $('#divBureauActivo').append('<h3 style="margin-bottom: 0px;">' + data.datos[i].NOMBRE_BUREAU + '</h3>');
                }
            } else {
                alert("No se pudo verificar los bureaus activos");
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


