function go(page, param, target, cmd) {
    var form;
    var acc;
    var metod = 'POST';
    var Cmd = cmd || 'cmd';
    var formId = 'Redirect';
    if (document.getElementById(formId) === undefined || document.getElementById(formId) === null) {
        form = document.createElement('form');
        form.id = formId;
        form.action = Cmd;
        form.method = metod;
        document.body.appendChild(form);
    } else {
        form = document.getElementById(formId);
        form.action = Cmd;
        form.method = metod;
    }
    form.acceptCharset = 'ISO-8859-1';
    if (target != undefined && target == true) {
        form.target = '_blank';
    } else {
        form.removeAttribute("target");
    }

    if (document.getElementById('sendTo') === undefined || document.getElementById('sendTo') === null) {
        acc = document.createElement('input');
        acc.id = 'sendTo';
        acc.name = 'sendTo';
        acc.type = 'hidden';
        form.appendChild(acc);
    } else {
        acc = document.getElementById('sendTo');
        acc.name = 'sendTo';
        acc.type = 'hidden';
    }
    if ((param !== undefined && param !== null) && param.length > 0) {
        addParams(param, form);
    }
    acc.value = page;
    form.submit();
}

function addParams(params, form) {
    var nelem;
    if (params.length > 0) {
        $.each(params, function (i, field) {
            nelem = $(form).find("[name='" + field.id + "']").is('input') ? document.getElementById(field.id) : document.createElement('input');
            nelem.type = 'hidden';
            nelem.id = field.id;
            nelem.name = field.id;
            nelem.value = field.val;
            form.appendChild(nelem);
        });
    }
}
function formatoNumero(input) {
    var valor = $(input).val().replace(/\./g, '');
    $(input).val(number_format(valor, 0, ',', '.'));
}

function formatRut(inp, event, inpDv) {
    $('#' + inpDv).val($.Rut.getDigito($(inp).val()));
}

function number_format(number, decimals, dec_point, thousands_sep) {
    number = (number + '').replace(/[^0-9+\-Ee.]/g, '');
    var n = !isFinite(+number) ? 0 : +number, prec = !isFinite(+decimals) ? 0 : Math.abs(decimals), sep = (typeof thousands_sep === 'undefined') ? '.' : thousands_sep, dec = (typeof dec_point === 'undefined') ? ','
            : dec_point, s = '', toFixedFix = function (n, prec) {
                var k = Math.pow(10, prec);
                return '' + Math.round(n * k) / k;
            };
    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
    if (s[0].length > 3) {
        s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
    }
    if ((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
}

function setTableObject(json, name, title, tipoo, va) {
    if (Object.keys(json).length === 0 || json.length < 1) {
        $(name).append('<section class="portlet grid_12 leading">' +
                '<header>' +
                '<h2>' + title + '</h2> ' +
                '</header>' +
                ' <section>' +
                '<table class="full"> ' +
                '<tbody> ' +
                '<tr> ' +
                '<td style="text-align: center;">No posee información</td> ' +
                '</tr> ' +
                '</tbody> ' +
                '</table>' +
                '</section>' +
                ' </section>');
        return "";
    }
    var tipo = verificar(JSON.parse(json));
    if (tipo === 1) {
        var j = JSON.parse(json);
        if (j.length > 0) {
            var h = '';
            var b = '';
            for (var i = 0; i < j.length; i++) {
                var aux = j[i];
                var ti = verificar(aux);
                var a = '';
                if (ti != 3) {
                    a = setTableObject(JSON.stringify(aux), name, '', 1, (i === 0 ? 1 : 0));
                }
                if (a.includes('|')) {
                    b = a.split('|')[1];
                    h += a.split('|')[0];
                } else {
                    h += a;
                }
//                h += '<tr> <td>' + (i != 0 ? '\n' : '') + a + '</td></tr>';
            }
            if (tipoo === 1) {
                return hh;
            } else {
                if (hh != '') {
                    $(name).append('<section class="portlet grid_12 leading">' +
                            '<header>' +
                            '<h2>' + title + '</h2> ' +
                            '</header>' +
                            ' <section>' +
                            '<table class="full"> ' +
                            '<thead> ' +
                            b +
                            '</thead> ' +
                            '<tbody style="text-align: center"> ' +
                            h +
                            '</tbody> ' +
                            '</table>' +
                            '</section>' +
                            ' </section>');
                }
            }
        }
    } else if (tipo === 2) {
        var j2 = JSON.parse(json);
        if (Object.keys(j2).length > 0) {
            var hh = (tipoo === 1 ? '<tr>' : '');
            var cabe = '<tr>';
            for (var item in j2) {
                var aux1 = j2[item];
                var tip = verificar(aux1);
                if (tip != 3) {
                    setTableObject(JSON.stringify(aux1), name, item, 0, 0);
                } else {
                    if (tipoo === 1) {
                        if (va === 1) {
                            cabe += '<th>' + item + '</th>';
                        }
                        hh += '<td>' + aux1 + '</td>';
                    } else {
                        hh += '<tr><td>' + item + '</td><td>' + aux1 + '</td></tr>';
                    }
                }
            }
            if (tipoo === 1) {
                cabe += '</tr>';
                hh += '</tr>';
                if (va === 1) {
                    hh += '|' + cabe;
                }
                return hh;
            } else {
                if (hh != '') {
                    $(name).append('<section class="portlet grid_12 leading">' +
                            '<header>' +
                            '<h2>' + title + '</h2> ' +
                            '</header>' +
                            ' <section>' +
                            '<table class="full"> ' +
                            '<tbody>' +
                            hh +
                            '</tbody> ' +
                            '</table>' +
                            '</section>' +
                            ' </section>');
                }
            }
        }
    }
}

function formatPoint(number) {
    var num = number.indexOf(".") != -1 ? number.substr(0, number.indexOf(".")) : number;
    num = num.toString().split('').reverse().join('').replace(/(?=\d*\.?)(\d{3})/g, '$1.');
    num = num.split('').reverse().join('').replace(/^[\.]/, '');
    return num;
}

function verificar(j) {
    if (Array.isArray(j)) {
        return 1;
    } else if (typeof (j) === "object") {
        return 2;
    } else {
        return 3;
    }
}

function selectedF(text, mantener) {
    $("select#" + text + " option").each(function () {
        if ($(this).val() == mantener) {
            $('#uniform-' + text + ' span').text($(this).text());
            $('#' + text + ' option[value="' + mantener + '"]').attr("selected", true);
        }
    });
}

function limpiarSelectF(text, mantener) {
    $("select#" + text + " option").each(function () {
        if ($(this).val() != mantener) {
            $(this).remove();
        } else {
            $('#uniform-' + text + ' span').text($(this).text());
        }
    });
}