/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    showSite();
    $('#relationManagerId').select2();
});

$('#formModal').on('submit', function (e) {
    e.preventDefault();
    let isCreate = document.getElementById('id').disabled;
    (!isCreate) ? createSite() : updateSite();
});

function showSite() {
    $('#dataTable').DataTable({
        'sAjaxSource': '/ajax/super-admin/site',
        'sAjaxDataProp': '',
        'columns': [
            {'data': 'id'},
            {'data': 'siteName'},
            {'data': 'address'},
            {'data': 'city'},
            {
                'render': function (data, type, row, meta) {
                    return row.employees.firstName + ' ' + row.employees.lastName;
                }
            },
            {
                'render': function (data, type, row, meta) {
                    return "<center class='form-inline'> " +
                            "<button class='btn btn-primary btn-circle mr-2' data-toggle='modal' data-target='#siteModal'" +
                            "onclick='detail(\"" + row.id + "\", \"" + row.siteName + "\", \"" + row.address + "\", \"" + row.city + "\", \"" + row.employees.id + "\")'>" +
                            "<i class='fas fa-info-circle'></i></button> " +
                            "<button  class='btn btn-warning btn-circle mr-2' data-toggle='modal' data-target='#siteModal'" +
                            "onclick='edit(\"" + row.id + "\", \"" + row.siteName + "\", \"" + row.address + "\", \"" + row.city + "\", \"" + row.employees.id + "\")'>" +
                            "<i class='fas fa-pencil-alt'></i></button> " +
                            "<form class='form-inline'> " +
                            "<button data-id='" + row.id + "' type='button' onclick='confirm(this)' class='btn btn-danger btn-circle delete-button mr-2'> " +
                            "<i class='fas fa-trash'></i></button></form></center>";
                }
            }
        ]
    });
}

function setValue(id, siteName, address, city, relationManager) {
    $('#id').val(id);
    $('#siteName').val(siteName);
    $('#address').val(address);
    $('#city').val(city);
//    $('#relationManager').val(relationManager);
    $('#relationManagerId').select2().select2('val', relationManager);
}

function setDisabled(isEnable, isUpdate) {
    $('#id').prop('disabled', isUpdate ? !isEnable : isEnable);
    $('#id-l').prop('disabled', isEnable);
    $('#siteName').prop('disabled', isEnable);
    $('#address').prop('disabled', isEnable);
    $('#city').prop('disabled', isEnable);
    $('#relationManagerId').prop('disabled', isEnable);
    $('#saveSite').prop('hidden', isEnable);
}

function detail(id, siteName, address, city, relationManagerId) {
    setValue(id, siteName, address, city, relationManagerId);
    setDisabled(true, false);
}

function edit(id, siteName, address, city, relationManager) {
    setValue(id, siteName, address, city, relationManager);
    setDisabled(false, true);
}

function create() {
    setValue(null, null, null, null, null, null);
    setDisabled(false, false);
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
            deleteSite(e);
        }
    });
}

function createSite() {
    let id = $("[name='id']").val();
    let siteName = $("[name='siteName']").val();
    let address = $("[name='address']").val();
    let city = $("[name='city']").val();
    let relationManager = $("[name='relationManagerId']").val();
    if (relationManager == "" || relationManager == null) {
        relationManager = null;
    }
    let site = {
        'id': id,
        'siteName': siteName,
        'address': address,
        'city': city,
        'employees': {
            'id': relationManager
        }
    };
    $.ajax({
        url: "/ajax/super-admin/site/create",
        type: "POST",
        data: JSON.stringify(site),
        contentType: 'application/json',
        dataType: 'JSON',
        success: function (response) {
            Swal.fire({
                title: "Created!",
                text: "Site Has Been Created",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#siteModal').modal('hide');
            $('#dataTable').DataTable().destroy();
            showSite();
        },
        error: function (result) {
            Swal.fire({
                title: "Failed!",
                text: "Failed To Create Site",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function updateSite() {
    let id = $("[name='id']").val();
    let siteName = $("[name='siteName']").val();
    let address = $("[name='address']").val();
    let city = $("[name='city']").val();
    let relationManager = $("[name='relationManagerId']").val();
    let site = {
        'id': id,
        'siteName': siteName,
        'address': address,
        'city': city,
        'employees': {
            'id': relationManager
        }
    };
    $.ajax({
        url: "/ajax/super-admin/site/" + id + "/update",
        type: "POST",
        data: JSON.stringify(site),
        contentType: 'application/json',
        dataType: 'JSON',
        success: function (response) {
            Swal.fire({
                title: "Updated!",
                text: "Site Has Been Updated",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#siteModal').modal('hide');
            $('#dataTable').DataTable().destroy();
            showSite();
        },
        error: function (e) {
            Swal.fire({
                title: "Failed!",
                text: "Failed To Create Site",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function deleteSite(e) {
    let id = $(e).attr('data-id');
    $.ajax({
        url: '/ajax/super-admin/site/' + id + '/delete',
        type: 'POST',
        dataType: 'TEXT',
        success: function (response) {
            Swal.fire({
                title: "Deleted!",
                text: "Site Has Been Deleted.",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#dataTable').DataTable().destroy();
            showSite();
        },
        error: function (e) {
            Swal.fire({
                title: "Failed!",
                text: "Site Failed To Delete.",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });  
}