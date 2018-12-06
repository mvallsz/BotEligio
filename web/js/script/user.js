console.log('user');
var USER = [];
$(document).ready(function () {
    listarEmpresasU();
    $('#tblUsuarios').DataTable().rows().remove().draw();
    $('#tblUsuarios tbody').append('<tr id="trCargando" class="odd" style="text-align: center;"><td valign="top" colspan="7" class="dataTables_empty" style="text-align: center;"><i class="fa fa-spinner fa-spin"></i>  Buscando Registros</td></tr>');
    listarUsuarios();
});

function listarEmpresasU() {
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
                            $('#form-empresau').append('<option value="' + data.datos[dato].ID + '">' + data.datos[dato].nom_empresa + '</option>');
                            selectedF('form-empresau', idEmp);
                            buscarUser();
                            break;
                        }
                    } else {
                        $('#form-empresau').append('<option value="' + data.datos[dato].ID + '">' + data.datos[dato].nom_empresa + '</option>');
                    }
                }
                if (codigo === 23) {
                    $('#form-empresau').attr('onchange', 'buscarUser()');
                }
            }
        }
    });
}

function limpiarU(tipo) {
//    if (tipo === 0) {
//        selectedF('form-empresau', '0');
//    }
    $('#form-nombreU').val('');
    $('#form-username').val('');
    $('#form-password').val('');
    $('#form-password-check').val('');
    $('#guardarUser').show();
    $('#ActualizarUser').hide();
    $('#ActualizarUser').attr('onclick', '');
}

function guardarU(button) {
    var html = $(button).html();
    $(button).html("<i class=\"fa fa-spinner fa-pulse fa-fw\"></i>  Guardando");
    $(button).prop("disabled", true);
    $('#reset').prop("disabled", true);
    var empresa = $("select#form-empresau option:checked").val();
    var nombre = $('#form-nombreU').val().trim();
    var user = $('#form-username').val().trim();
    var pass1 = $('#form-password').val().trim();
    var pass2 = $('#form-password-check').val().trim();
    if (empresa === '0') {
        alert('Debe ingresar una empresa');
    } else if (nombre === '') {
        alert('Debe ingresar nombre');
    } else if (user === '') {
        alert('Debe ingresar usuario');
    } else if (pass1 === '') {
        alert('Debe ingresar password');
    } else if (pass1 != pass2) {
        alert('Las contraseñas no coinciden');
    } else {
        $.ajax({
            url: 'Svl_Usuario',
            type: 'POST',
            dataType: 'json',
            data: {
                accion: 'agregarUsuario',
                empresa: empresa,
                nombre: nombre,
                user: user,
                pass1: pass1
            },
            success: function (data, textStatus, jqXHR) {
                if (data.estado === 200) {
                    alert('Usuario Creado Exitosamente');
                    buscarUser();
                } else if (data.estado === 300) {
                    alert('Ya existe ese Usuario para esa empresa');
                } else {
                    alert('Usuario no creado');
                }
            }
        });
    }
    $(button).html(html);
    $(button).prop("disabled", false);
    $('#reset').prop("disabled", false);
}

function limpiarSelectU(text, mantener) {
    $("select#" + text + " option").each(function () {
        if ($(this).val() != mantener) {
            $(this).remove();
        } else {
            $('#uniform-' + text + ' span').text($(this).text());
        }
    });
}

function buscarUser() {
    var idEmp = $("select#form-empresau option:checked").val();
    limpiarU(1);
    $('#tblUsuarios').DataTable().rows().remove().draw();
    $.ajax({
        url: 'Svl_Usuario',
        type: 'POST',
        dataType: 'json',
        data: {accion: 'listarUsuarios', idEmp: idEmp},
        beforeSend: function (xhr) {
            $('#tblUsuarios tbody').append('<tr id="trCargando" class="odd" style="text-align: center;"><td valign="top" colspan="7" class="dataTables_empty" style="text-align: center;"><i class="fa fa-spinner fa-spin"></i>  Buscando Registros</td></tr>');
        },
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                USER = data.datos;
                listarUsuarios();
            }
        }
    });
}

function listarUsuarios() {
    $('#trCargando').remove();
    $('#tblUsuarios').DataTable().destroy();
    $('#tblUsuarios').DataTable({
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
        "data": USER,
        "bSort": true,
        "columnDefs": [
            {"width": "27%", "targets": 0},
            {"width": "28%", "targets": 1},
            {"width": "28%", "targets": 2},
            {"width": "20%", "targets": 3},
        ],
        "columns": [
            {data: 'nombre', class: 'txt-center'},
            {data: 'user', class: 'txt-center'},
            {data: null, "render": function (data, type, row) {
                    if (data.estado === "1") {
                        return 'Activo';
                    } else {
                        return 'Desactivasdo';
                    }
                }},
            {data: null, "render": function (data, type, row) {
                    var action = "";
                    if (data.estado === "1") {
                        action = '<button title="Desactivar" onclick="activarUsuario(' + data.id + ', 0)"><i class="fa fa-ban"></i></button>';
                    } else {
                        action = '<button title="Activar" onclick="activarUsuario(' + data.id + ', 1)"><i class="fa fa-check-circle"></i></button>';
                    }
                    action = action + '<button title="Editar" onclick="editarUser(' + data.id + ')"><i class="far fa-edit"></i></button>';
                    return action;
                }}
        ]
    });
}

function activarUsuario(idUser, estado) {
    $.ajax({
        url: 'Svl_Usuario',
        type: 'POST',
        dataType: 'json',
        data: {
            accion: 'bloquearUser',
            idUser: idUser,
            estado: estado
        },
        success: function (data, textStatus, jqXHR) {
            if (data.estado == 200) {
                alert("Usuario " + (estado === 1 ? "Desactivado" : "Activado"));
                buscarUser();
            } else {
                alert("No se puedo " + (estado === 1 ? "Desactivar" : "activar") + " el Usuario");
            }
        }
    });
}

function editarUser(idUser) {
    for (var i = 0; i < USER.length; i++) {
        if (parseInt(USER[i].id) === idUser) {
            $('#form-nombreU').val(USER[i].nombre);
            $('#form-username').val(USER[i].user);
            $('#guardarUser').hide();
            $('#ActualizarUser').show();
            $('#ActualizarUser').attr('onclick', 'editarU(' + idUser + ',' + USER[i].estado + ',this);');
            break;
        }
    }
}

function editarU(idUser, estado, button) {
    var html = $(button).html();
    $(button).html("<i class=\"fa fa-spinner fa-pulse fa-fw\"></i>  Guardando");
    $(button).prop("disabled", true);
    $('#reset').prop("disabled", true);
    var empresa = $("select#form-empresau option:checked").val();
    var nombre = $('#form-nombreU').val().trim();
    var user = $('#form-username').val().trim();
    var pass1 = $('#form-password').val().trim();
    var pass2 = $('#form-password-check').val().trim();
    if (empresa === '0') {
        alert('Debe ingresar una empresa');
    } else if (nombre === '') {
        alert('Debe ingresar nombre');
    } else if (user === '') {
        alert('Debe ingresar usuario');
    } else if (pass1 === '') {
        alert('Debe ingresar password');
    } else if (pass1 != pass2) {
        alert('Las contraseñas no coinciden');
    } else {
        $.ajax({
            url: 'Svl_Usuario',
            type: 'POST',
            dataType: 'json',
            data: {
                accion: 'actualizarUsuario',
                idUser: idUser,
                nombre: nombre,
                user: user,
                pass: pass1,
                estado: estado
            },
            success: function (data, textStatus, jqXHR) {
                if (data.estado === 200) {
                    alert('Usuario Actualizado Exitosamente');
                    buscarUser();
                } else {
                    alert('Usuario no Actualizado');
                }
            }
        });
    }
    $(button).html(html);
    $(button).prop("disabled", false);
    $('#reset').prop("disabled", false);
}