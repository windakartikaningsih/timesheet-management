/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    showDivision();
    $('#managerSite').select2();
});

$('#formModal').on('submit', function (e) {
    e.preventDefault();
    let isCreate = document.getElementById('id').disabled;
    (!isCreate) ? createDivision() : updateDivision();
});

function showDivision() {
    $('#dataTable').DataTable({
        'sAjaxSource': '/ajax/super-admin/division',
        'sAjaxDataProp': '',
        'columns': [
            {'data': 'id'},
            {'data': 'name'},
            {
                'render': function (data, type, row, meta) {
                    return row.managerSite.firstName + ' ' + row.managerSite.lastName;
                }
            },
            {
                'render': function (data, type, row, meta) {
                    return "<center class='form-inline'> " +
                            "<button class='btn btn-primary btn-circle mr-2' data-toggle='modal' data-target='#divisionModal'" +
                            "onclick='detail(\"" + row.id + "\", \"" + row.name + "\", \"" + row.managerSite.id + "\")'>" +
                            "<i class='fas fa-info-circle'></i></button> " +
                            "<button  class='btn btn-warning btn-circle mr-2' data-toggle='modal' data-target='#divisionModal'" +
                            "onclick='edit(\"" + row.id + "\", \"" + row.name + "\", \"" + row.managerSite.id + "\")'>" +
                            "<i class='fas fa-pencil-alt'></i></button> " +
                            "<form class='form-inline'> " +
                            "<button data-id='" + row.id + "' type='button' onclick='confirm(this)' class='btn btn-danger btn-circle delete-button mr-2'> " +
                            "<i class='fas fa-trash'></i></button></form></center>";
                }
            }
        ]
    });
}

function setValue(id, name, managerSite) {
    $('#id').val(id);
    $('#name').val(name);
//    $('#managerSite').val(managerSite);
    $('#managerSite').select2().select2('val', managerSite);
}

function setDisabled(isEnable, isUpdate) {
    $('#id').prop('disabled', isUpdate ? !isEnable : isEnable);
    $('#id-l').prop('disabled', isEnable);
    $('#name').prop('disabled', isEnable);
    $('#managerSite').prop('disabled', isEnable);
    $('#saveDivision').prop('hidden', isEnable);
}

function detail(id, name, managerSiteId) {
    setValue(id, name, managerSiteId);
    setDisabled(true, false);
}

function edit(id, name, managerSiteId) {
    setValue(id, name, managerSiteId);
    setDisabled(false, true);
}

function create() {
    setValue(null, null, null);
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
            deleteDivision(e);
        }
    });
}

function createDivision() {
    let id = $("[name='id']").val();
    let name = $("[name='name']").val();
    let managerSiteId = $("[name='managerSite']").val();
    if (managerSiteId == "" || managerSiteId == null) {
        managerSiteId = null;
    }
    let division = {
        'id': id,
        'name': name,
        'managerSite': {
            'id': managerSiteId
        }
    };
    $.ajax({
        url: "/ajax/super-admin/division/create",
        type: "POST",
        data: JSON.stringify(division),
        contentType: 'application/json',
        dataType: 'JSON',
        success: function (response) {
            Swal.fire({
                title: "Created!",
                text: "Division Has Been Created",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#divisionModal').modal('hide');
            $('#dataTable').DataTable().destroy();
            showDivision();
        },
        error: function (result) {
            Swal.fire({
                title: "Failed!",
                text: "Failed To Create Division",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function updateDivision() {
    let id = $("[name='id']").val();
    let name = $("[name='name']").val();
    let managerSiteId = $("[name='managerSite']").val();
    let division = {
        'id': id,
        'name': name,
        'managerSite': {
            'id': managerSiteId
        }
    };
    $.ajax({
        url: "/ajax/super-admin/division/" + id + "/update",
        type: "POST",
        data: JSON.stringify(division),
        contentType: 'application/json',
        dataType: 'JSON',
        success: function (response) {
            Swal.fire({
                title: "Updated!",
                text: "Division Has Been Updated",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#divisionModal').modal('hide');
            $('#dataTable').DataTable().destroy();
            showDivision();
        },
        error: function (result) {
            Swal.fire({
                title: "Failed!",
                text: "Failed To Update Division",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function deleteDivision(e) {
    let id = $(e).attr('data-id');
    $.ajax({
        url: '/ajax/super-admin/division/' + id + '/delete',
        type: 'POST',
        dataType: 'TEXT',
        success: function (response) {
            Swal.fire({
                title: "Deleted!",
                text: "Division Has Been Deleted.",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#dataTable').DataTable().destroy();
            showDivision();
        },
        error: function (e) {
            Swal.fire({
                title: "Failed!",
                text: "Division Failed To Delete.",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });  
}