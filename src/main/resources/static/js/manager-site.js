/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global Swal */

$(document).ready(function() {
    showManagerSite();
    $('#site').select2();
});

$('#formModal').on('submit', function (e) {
    e.preventDefault();
    let isCreate = document.getElementById('id').hidden;
    (isCreate) ? createManagerSite() : updateManagerSite();
});

function showManagerSite() {
    $('#dataTable').DataTable({
        'sAjaxSource': '/ajax/super-admin/manager-site',
        'sAjaxDataProp': '',
        'columns': [
            {'data': 'id'},
            {'render': function (data, type, row, meta) {
                    return row.firstName + ' ' + row.lastName;
            }},
            {'data': 'email'},
            {'render': function (data, type, row, meta) {
                    if (row.site != null){
                        return row.site.siteName;
                    } else {
                        return "Not Choosen Site";
                    }
            }},
            {
                'render': function (data, type, row, meta) {
                    return "<center class='form-inline'>" +
                            "<button class='btn btn-primary btn-circle mr-2' data-toggle='modal' data-target='#managerModal'" +
                            "onclick='detail(\"" + row.id + "\", \"" + row.firstName + "\", \"" + row.lastName + "\", \"" + row.email + "\", \"" + row.site.id + "\")'>" +
                            "<i class='fas fa-info-circle'></i></button> " +
                            "<button class='btn btn-warning btn-circle mr-2' data-toggle='modal' data-target='#managerModal'" +
                            "onclick='edit(\"" + row.id + "\", \"" + row.firstName + "\", \"" + row.lastName + "\", \"" + row.email + "\", \"" + row.site.id + "\")'>" +
                            "<i class='fas fa-pencil-alt'></i></button>" +
                            "<form class='form-inline'>" +
                            "<button data-id='" + row.id + "' type='button' onclick='confirm(this)' class='btn btn-danger btn-circle delete-button mr-2'> " +
                            "<i class='fas fa-trash'></i></button></form></center>";
                }
            }
        ]
    });
}

function setValue(id, firstName, lastName, email, siteId) {
    $('#id').val(id);
    $('#firstName').val(firstName);
    $('#lastName').val(lastName);
    $('#email').val(email);
//    $('#site').val(siteId);
    $('#site').select2().select2('val', siteId);
}

function setDisabled(isDetail, isCreate) {
    $('#id').prop('hidden', isCreate);
    $('#id-1').prop('hidden', isCreate);
    $('#firstName').prop('disabled', isDetail);
    $('#lastName').prop('disabled', isDetail);
    $('#email').prop('disabled', isDetail);
    $('#site').prop('disabled', isDetail);
    $('#saveManagerSite').prop('hidden', isDetail);
}

function detail(id, firstName, lastName, email, siteId) {
    setValue(id, firstName, lastName, email, siteId);
    setDisabled(true, false);
}

function edit(id, firstName, lastName, email, siteId) {
    setValue(id, firstName, lastName, email, siteId);
    setDisabled(false, false);
}

function create() {
    setValue(null, null, null, null, null);
    setDisabled(false, true);
}

function confirm(e) {
    Swal.fire({
        title: "Are you sure?",
        text: "You will not be able to recover this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, delete it!"
    }).then((result) => {
        if (result.isConfirmed) {
            deleteManagerSite(e);
        }
    });
}

function createManagerSite() {
    let firstName = $("[name='firstName']").val();
    let lastName = $("[name='lastName']").val();
    let email = $("[name='email']").val();
    let siteId = $("[name='site']").val();
    if (siteId == "" || siteId == null) {
        siteId = null;
    }
    let ms = {
        'firstName': firstName,
        'lastName': lastName,
        'email': email,
        'site': {
            'id': siteId
        }
    };
    console.log(ms);
    $.ajax({
        url: "/ajax/super-admin/manager-site/create",
        type: "POST",
        data: JSON.stringify(ms),
        contentType: 'application/json',
        dataType: 'JSON',
        success: function (response) {
            Swal.fire({
                title: "Created!",
                text: "Manager Site Has Been Created",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#managerModal').modal('hide');
            $('#dataTable').DataTable().destroy();
            showManagerSite();
        },
        error: function (response) {
            console.log(response);
            Swal.fire({
                title: "Failed!",
                text: "Failed To Create Manager Site",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function updateManagerSite() {
    let id = $("[name='id']").val();
    let firstName = $("[name='firstName']").val();
    let lastName = $("[name='lastName']").val();
    let email = $("[name='email']").val();
    let siteId = $("[name='site']").val();
    let ms = {
        'firstName': firstName,
        'lastName': lastName,
        'email': email,
        'site': {
            'id': siteId
        }
    };
    $.ajax({
        url: "/ajax/super-admin/manager-site/" + id + "/update",
        type: "POST",
        data: JSON.stringify(ms),
        contentType: 'application/json',
        dataType: 'JSON',
        success: function (response) {
            Swal.fire({
                title: "Updated!",
                text: "Manager Site Has Been Updated",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#managerModal').modal('hide');
            $('#dataTable').DataTable().destroy();
            showManagerSite();
        },
        error: function (response) {
            console.log(response);
            Swal.fire({
                title: "Failed!",
                text: "Failed To Update Manager Site",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function deleteManagerSite(e) {
    let id = $(e).attr('data-id');
    $.ajax({
        url: '/ajax/super-admin/manager-site/' + id + '/delete',
        type: 'POST',
        dataType: 'TEXT',
        success: function (response) {
            console.log(response);
            Swal.fire({
                title: "Deleted!",
                text: "Manager Site Has Been Deleted.",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#dataTable').DataTable().destroy();
            showManagerSite();
        },
        error: function (e) {
            console.log(e);
            Swal.fire({
                title: "Failed!",
                text: "Manager Site Failed To Delete.",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });  
}