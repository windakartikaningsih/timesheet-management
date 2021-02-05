/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function(){
    reloadUserPermission();
    $('#userVal').select2();
    $('#permitVal').select2();
});

$('#formModalPermit').on('submit', function (e){
    e.preventDefault();
    let isCreate = document.getElementById('idVal').hidden;
    (isCreate) ? createUserPermission() : updateUserPermission();
});

function reloadUserPermission(){
    $('#dataTable').DataTable({
        'sAjaxSource': '/ajax/super-admin/user-permission',
        'sAjaxDataProp': '',
        'columns': [
            {'data': 'id'},
            {'data': 'user.username'},
            {'data': 'permission.permissionName'},
            {
                'render': function (data, type, row, meta) {
                    
                    return "<center class='form-inline'>" +
                            "<button class='btn btn-primary btn-circle mr-2' data-toggle='modal' data-target='#userPermissionModal'" +
                            "onclick='detail(\"" + row.id + "\", \"" + row.user.id + "\", \"" + row.permission.id + "\")'>" +
                            "<i class='fas fa-info-circle'></i></button> " +
                            "<button class='btn btn-warning btn-circle mr-2' data-toggle='modal' data-target='#userPermissionModal'" +
                            "onclick='edit(\"" + row.id + "\", \"" + row.user.id + "\", \"" + row.permission.id + "\")'>" +
                            "<i class='fas fa-pencil-alt'></i></button>" +
                            "<form class='form-inline'>" +
                            "<button data-id='" + row.id + "' type='button' onclick='confirm(this)' class='btn btn-danger btn-circle delete-button mr-2'> " +
                            "<i class='fas fa-trash'></i></button></form></center>";
                }
            }
        ]
    });
}

function setValue(id, user, permission){
    $('#idVal').val(id);
    $('#userVal').val(user);
    $('#permitVal').val(permission);
    $('#userVal').select2().select2('val', user);
    $('#permitVal').select2().select2('val', permission);
}

function setDisable(isDetail, isCreate){
    $('#idVal').prop('hidden', isCreate);
    $('#id').prop('hidden', isCreate);
    $('#userVal').prop('disabled', isDetail);
    $('#permitVal').prop('disabled', isDetail);
    $('#saveUserPermission').prop('hidden', isDetail);
}

function detail(id, user, permission){
    setValue(id, user, permission);
    setDisable(true, false);
}

function edit(id, user, permission) {
    setValue(id, user, permission);
    setDisable(false, false);
}

function create() {
    setValue(null, null, null);
    setDisable(false, true);
}

function createUserPermission() {
    let id = $("[name='id']").val();
    let user = $("[name='userId'").val();
    let permission = $("[name='permissionId']").val();
    let upermit = {
        'id': id,
        'user': {
            'id': user
        },
        'permission': {
            'id': permission
        }
    };
    console.log("Ini Create User Permission");
    console.log(upermit);
    $.ajax({
        url: "/ajax/super-admin/user-permission/create",
        type: "POST",
        data: JSON.stringify(upermit),
        contentType: 'application/json',
        dataType: 'JSON',
        success: function (response) {
            Swal.fire({
                title: "Created!",
                text: "User Permission Has Been Created",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#userPermissionModal').modal('hide');
            $('#dataTable').DataTable().destroy();
            reloadUserPermission();
        },
        error: function (result) {
            Swal.fire({
                title: "Failed!",
                text: "Failed To Create User Permission",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function updateUserPermission(){
    console.log("updateUserPermission");
    let id = $("[name='id']").val();
    let user = $("[name='userId']").val();
    let permission = $("[name='permissionId']").val();
    console.log(id);
    console.log(name);
    let permit = {
        'id': id,
        'user': {
            'id': user
        },
        'permission': {
            'id': permission
        }
    };
    $.ajax({
        url: "/ajax/super-admin/user-permission/" + id + "/update",
        type: "POST",
        data: JSON.stringify(permit),
        contentType: 'application/json',
        dataType: 'JSON',
        success: function (response) {
            console.log('berhasil');
            Swal.fire({
                title: "Updated!",
                text: "User Permission Has Been Updated",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#userPermissionModal').modal('hide');
            $('#dataTable').DataTable().destroy();
            reloadUserPermission();
        },
        error: function (result) {
            console.log(result);
            Swal.fire({
                title: "Failed!",
                text: "Failed To Update User Permission",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function deleteUserPermission(e){
    let id = $(e).attr('data-id');
    $.ajax({
        url: '/ajax/super-admin/user-permission/' + id + '/delete',
        type: 'POST',
        dataType: 'TEXT',
        success: function (response) {
            Swal.fire({
                title: "Deleted!",
                text: "User Permission Has Been Deleted.",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#dataTable').DataTable().destroy();
            reloadUserPermission();
        },
        error: function (e) {
            Swal.fire({
                title: "Failed!",
                text: "User Permission Failed To Delete.",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }   
    });
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
            deleteUserPermission(e);
        }
    });
}