/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    reloadUserRole();
    $('#username-select').select2();
    $('#urole-select').select2();
});

//Create atau Update
$('#formModalRole').on('submit', function (e) {
    e.preventDefault();
    let isCreate = document.getElementById('id-input').hidden;
    (isCreate) ? createUserRole() : updateUserRole();
});


function reloadUserRole() {
    $('#dataTable').DataTable({
        'sAjaxSource': '/ajax/super-admin/user-role',
        'sAjaxDataProp': '',
        'columns': [
            {'data': 'id'},
            {'data': 'user.username'},
            {'data': 'role.roleName'},
            {
                'render': function (data, type, row, meta) {
                    return "<center class='form-inline'> " +
                            "<button onclick='detail(\"" + row.id + "\",\"" + row.user.id + "\",\"" + row.role.id + "\")' " + "class='btn btn-info btn-circle' data-toggle='modal' data-target='#userRoleModal'> " +
                            "<i class='fas fa-info-circle'></i></button> " +
                            "<button onclick='edit(\"" + row.id + "\",\"" + row.user.id + "\",\"" + row.role.id + "\")' " + "class='btn btn-warning btn-circle' data-toggle='modal' data-target='#userRoleModal'> " +
                            "<i class='fas fa-pencil-alt'></i></button> " +
                            "<form class='form-inline'> " +
                            "<button data-id='" + row.id + "' type='button' onclick='confirm(this)' class='btn btn-danger btn-circle delete-button'> " +
                            "<i class='fas fa-trash'></i></button></form></center>";
                }
            }
        ]
    });
}

function setValue(id, user, role) {
    $('#id-input').val(id);
    $('#username-select').select2().select2('val', user);
    $('#urole-select').select2().select2('val', role);
}

function setDisable(isDetail, isCreate) {
    //User Role Id
    $('#id-input').prop('hidden', isCreate);
    $('#id-label').prop('hidden', isCreate);

    //User Name
    $('#username-select').prop('disabled', isDetail);

    //User Role
    $('#urole-select').prop('disabled', isDetail);

    //Save Button
    $('#saveUserRole').prop('hidden', isDetail);
}

function detail(id, user, role) {
    setValue(id, user, role);
    setDisable(true, false);
}

function edit(id, user, role) {
    setValue(id, user, role);
    setDisable(false, false);
}

function create() {
    setValue(null, null, null);
    setDisable(false, true);
}


function confirm(e) {
    Swal.fire({
        title: "Are you sure ?",
        text: "You will not be able to recover this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, Delete it!"
    }).then((result) => {
        if (result.isConfirmed) {
            deleteUserRole(e);
        }
    });
}

function createUserRole() {
    let id = $("[name='id'").val();
    let user = $("[name='userName'").val();
    let role = $("[name='userRole']").val();
    let urole = {
        'id': id,
        'user': {
            'id': user
        },
        'role': {
            'id': role
        }
    };
    console.log(urole);
    $.ajax({
        url: "/ajax/super-admin/user-role/create",
        type: "POST",
        data: JSON.stringify(urole),
        contentType: 'application/json',
        dataType: 'JSON',
        success: function (response) {
            Swal.fire({
                title: "Created!",
                text: "User Role Has Been Created",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#userRoleModal').modal('hide');
            $('#dataTable').DataTable().destroy();
            reloadUserRole();
        },
        error: function (result) {
            Swal.fire({
                title: "Failed!",
                text: "Failed To Create User Role",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function updateUserRole() {
    console.log("updateUserRole");
    let id = $("[name='id']").val();
    let user = $("[name='userName']").val();
    let role = $("[name='userRole']").val();
    let roles = {
        'id': id,
        'user': {
            'id': user
        },
        'role': {
            'id': role
        }
    };
    $.ajax({
        url: "/ajax/super-admin/user-role/" + id + "/update",
        type: "POST",
        data: JSON.stringify(roles),
        contentType: 'application/json',
        dataType: 'JSON',
        success: function (response) {
            console.log('berhasil');
            Swal.fire({
                title: "Updated!",
                text: "User Role Has Been Updated",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#userRoleModal').modal('hide');
            $('#dataTable').DataTable().destroy();
            reloadUserRole();
        },
        error: function (result) {
            console.log(result);
            Swal.fire({
                title: "Failed!",
                text: "Failed To Update User Role",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function deleteUserRole(e) {
    let id = $(e).attr('data-id');
    $.ajax({
        url: '/ajax/super-admin/user-role/' + id + '/delete',
        type: 'POST',
        contentType: 'application/json',
        success: function (response) {
            Swal.fire({
                title: "Deleted!",
                text: "User Role Has Been Deleted.",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#dataTable').DataTable().destroy();
            reloadUserRole();
        },
        error: function (e) {
            Swal.fire({
                title: "Failed!",
                text: "User Role Failed To Delete.",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}