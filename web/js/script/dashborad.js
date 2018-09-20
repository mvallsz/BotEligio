$(document).ready(function () {

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
        cursor:{
            zoom:true,
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
    var total = [['Utilizado', 10],['Libre', 90]];
    var chart3 = jQuery.jqplot ('chart3', [total], {
            seriesDefaults: {
            renderer: jQuery.jqplot.PieRenderer, 
            rendererOptions: { showDataLabels: true }
            }, 
            legend: { 
                    show:true, 
                    location: 'e' 
            }
    });
});


