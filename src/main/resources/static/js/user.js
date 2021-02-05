/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global Swal */

$('document').ready(function () {
    console.log("kepanggil");
    reloadUser();
});

$('#modalForm').on('submit', function(e){
    e.preventDefault();
    create();
});

function changePassword(id) {
//    let id = $("[name='id']").val();
    alert(id);
    let oldPassword = $("[name='oldPassword']").val();
    let newPassword = $("[name='newPassword']").val();
    let user = {
        'oldPassword': oldPassword,
        'newPassword': newPassword
    };
    $.ajax({
        url: '/change-password' + id,
        type: "POST",
        data: JSON.stringify(user),
        contentType: 'application/json',
        dataType: 'JSON',
        success: function (response) {
            console.log('berhasil');
            console.log(response);
            Swal.fire({
                title: "Created!",
                text: "Password Has Been Changed",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
        },
        error: function (response) {
            console.log(response);
            Swal.fire({
                title: "Failed!",
                text: "Failed To Change Password",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}


function reloadUser() {
    $('#dataTable').DataTable({
        'sAjaxSource': '/ajax/super-admin/user',
        'sAjaxDataProp': '',
        'columns': [
            {'data': 'id'},
            {'data': 'username'},
            {'data': 'status.name'},
            {
                'render': function (data, type, row, meta) {
                    return "<center class='form-group'>"
                            + "<button class='btn btn-success btn-circle' onclick='verifiedUser(\"" + row.id + "\")'>"
                            + "<i class='fas fa-check'></i></button> "
                            + "<button class='btn btn-danger btn-circle' onclick='deleteUser(\"" + row.id + "\")'>"
                            + "<i class='fas fa-trash'></i></button></center>";
                }
            }
        ]
    });
}

function deleteUser(id) {
    console.log(id);
    Swal.fire({
        title: "Are you sure?",
        text: "You will not be able to recover this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, delete it!"
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: 'POST',
                contentType: 'application/json',
                url: '/ajax/super-admin/user/' + id + '/delete',
                success: function (result) {
                    console.log(result);
                    Swal.fire({
                        title: "Deleted!",
                        text: "User has been deleted",
                        timer: 1000,
                        icon: "success",
                        showConfirmButton: false
                    });
                    $('#dataTable').DataTable().destroy();
                    reloadUser();
                },
                error: function (result) {
                    Swal.fire({
                        title: "Failed!",
                        text: "Failed to create task",
                        timer: 1000,
                        icon: "error",
                        showConfirmButton: false
                    });
                }
            });
        }
    });
}

function create() {
    let id = $('#id').val();
    let username = $('#username').val();
    let user = {
        'id': id,
        'username': username
    };
    if (id == "" || id == null) {
        Swal.fire({
            title: "Failed!",
            text: "ID must not be null",
            timer: 1000,
            icon: "error",
            showConfirmButton: false
        });
    } else {
        Swal.fire({
            title: "Creating User...",
            text: "Please wait",
            showConfirmButton: false,
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });
        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/ajax/super-admin/user/',
            data: JSON.stringify(user),
            dataType: 'JSON',
            success: function (result) {
                Swal.fire({
                    title: "Created!",
                    text: "User has been created",
                    timer: 1000,
                    icon: "success",
                    showConfirmButton: false
                });
                $('#dataTable').DataTable().destroy();
                reloadUser();
            },
            error: function (err) {
                Swal.fire({
                    title: "Failed!",
                    text: "User Already Exist",
                    timer: 1500,
                    icon: "error",
                    showConfirmButton: false
                });
            }
        })
    }
}

function verifiedUser(id) {
    Swal.fire({
        title: "Verified this user?",
        text: "verified user",
        icon: "question",
        showCancelButton: true,
        confirmButtonText: "Yes, verified it!"
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: 'POST',
                contentType: 'application/json',
                url: '/ajax/super-admin/user/' + id + '/reset-verif',
                success: function (result) {
                    Swal.fire({
                        title: "Verified!",
                        text: "User has been verified",
                        timer: 1000,
                        icon: "success",
                        showConfirmButton: false
                    });
                    $('#dataTable').DataTable().destroy();
                    reloadUser();
                },
                error: function (result) {
                    Swal.fire({
                        title: "Failed!",
                        text: "Failed to create task",
                        timer: 1000,
                        icon: "error",
                        showConfirmButton: false
                    });
                }
            });
        }
    });
}