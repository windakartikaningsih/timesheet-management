/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    showEmployee();
    $('#division').select2();
});

$('#formModal').on('submit', function (e) {
    e.preventDefault();
    let isCreate = document.getElementById('id').hidden;
    (isCreate) ? createEmployee() : updateEmployee();
});

function showEmployee() {
    $('#dataTable').DataTable({
        'sAjaxSource': '/ajax/super-admin/employee',
        'sAjaxDataProp': '',
        'columns': [
            {'data': 'id'},
            {'render': function (data, type, row, meta) {
                    return row.firstName + ' ' + row.lastName;
                }},
            {'data': 'email'},
            {'data': 'phoneNumber'},
            {
                'render': function (data, type, row, meta) {
                    if (row.division != null) {
                        return row.division.name;
                    } else {
                        return "Relation Manager";
                    }
                }
            },
            {
                'render': function (data, type, row, meta) {
                    if (row.division != null) {
                        return "<center class='form-inline'> " +
                                "<button class='btn btn-primary btn-circle' data-toggle='modal' data-target='#employeeModal'" +
                                "onclick='detail(\"" + row.id + "\", \"" + row.firstName + "\", \"" + row.lastName + "\", \"" + row.email + "\", \"" + row.phoneNumber + "\", \"" + row.division.id + "\")'>" +
                                "<i class='fas fa-info-circle'></i></button> " +
                                "<button class='btn btn-warning btn-circle' data-toggle='modal' data-target='#employeeModal'" +
                                "onclick='edit(\"" + row.id + "\", \"" + row.firstName + "\", \"" + row.lastName + "\", \"" + row.email + "\", \"" + row.phoneNumber + "\", \"" + row.division.id + "\")'>" +
                                "<i class='fas fa-pencil-alt'></i></button> " +
                                "<form class='form-inline'> " +
                                "<button data-id='" + row.id + "' type='button' onclick='confirm(this)' class='btn btn-danger btn-circle delete-button'> " +
                                "<i class='fas fa-trash'></i></button></form></center>";
                    } else {
                        return "<center class='form-inline'> " +
                                "<button class='btn btn-primary btn-circle' data-toggle='modal' data-target='#employeeModal'" +
                                "onclick='detail(\"" + row.id + "\", \"" + row.firstName + "\", \"" + row.lastName + "\", \"" + row.email + "\", \"" + row.phoneNumber + "\")'>" +
                                "<i class='fas fa-info-circle'></i></button> " +
                                "<button class='btn btn-warning btn-circle' data-toggle='modal' data-target='#employeeModal'" +
                                "onclick='edit(\"" + row.id + "\", \"" + row.firstName + "\", \"" + row.lastName + "\", \"" + row.email + "\", \"" + row.phoneNumber + "\")'>" +
                                "<i class='fas fa-pencil-alt'></i></button> " +
                                "<form class='form-inline'> " +
                                "<button data-id='" + row.id + "' type='button' onclick='confirm(this)' class='btn btn-danger btn-circle delete-button'> " +
                                "<i class='fas fa-trash'></i></button></form></center>";
                    }
                }
            }
        ]
    });
}

function setValue(id, firstName, lastName, email, phoneNumber, division) {
    $('#id').val(id);
    $('#firstName').val(firstName);
    $('#lastName').val(lastName);
    $('#email').val(email);
    $('#phoneNumber').val(phoneNumber);
//    $('#division').val(division);
    $('#division').select2().select2('val', division);
}

function setDisabled(isDetail, isCreate) {
    $('#id').prop('hidden', isCreate);
    $('#id-1').prop('hidden', isCreate);
    $('#firstName').prop('disabled', isDetail);
    $('#lastName').prop('disabled', isDetail);
    $('#email').prop('disabled', isDetail);
    $('#phoneNumber').prop('disabled', isDetail);
    $('#division').prop('disabled', isDetail);
    $('#saveEmployee').prop('hidden', isDetail);
}

function detail(id, firstName, lastName, email, phoneNumber, division) {
    setValue(id, firstName, lastName, email, phoneNumber, division);
    setDisabled(true, false);
}

function edit(id, firstName, lastName, email, phoneNumber, division) {
    setValue(id, firstName, lastName, email, phoneNumber, division);
    setDisabled(false, false);
}

function create() {
    setValue(null, null, null, null, null, null);
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
            deleteEmployee(e);
        }
    });
}

function createEmployee() {
    let firstName = $("[name='firstName']").val();
    let lastName = $("[name='lastName']").val();
    let email = $("[name='email']").val();
    let phoneNumber = $("[name='phoneNumber']").val();
    let divisionId = $("[name='division']").val();
    if (divisionId == "" || divisionId == null) {
        divisionId = null;
    }
    let employee = {
        'firstName': firstName,
        'lastName': lastName,
        'email': email,
        'phoneNumber': phoneNumber,
        'division': {
            'id': divisionId
        }
    };
    console.log(employee);
    $.ajax({
        url: "/ajax/super-admin/employee/create",
        type: "POST",
        data: JSON.stringify(employee),
        contentType: 'application/json',
        dataType: 'JSON',
        success: function (response) {
            console.log(response);
            Swal.fire({
                title: "Created!",
                text: "Employee Has Been Created",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#employeeModal').modal('hide');
            $('#dataTable').DataTable().destroy();
            showEmployee();
        },
        error: function (result) {
            console.log(result);
            Swal.fire({
                title: "Failed!",
                text: "Failed To Create Employee",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function updateEmployee() {
    let id = $("[name='id']").val();
    let firstName = $("[name='firstName']").val();
    let lastName = $("[name='lastName']").val();
    let email = $("[name='email']").val();
    let phoneNumber = $("[name='phoneNumber']").val();
    let divisionId = $("[name='division']").val();
    let employee = {
        'id': id,
        'firstName': firstName,
        'lastName': lastName,
        'email': email,
        'phoneNumber': phoneNumber,
        'division': {
            'id': divisionId
        }
    };
    $.ajax({
        url: "/ajax/super-admin/employee/" + id + "/update",
        type: "POST",
        data: JSON.stringify(employee),
        contentType: 'application/json',
        dataType: 'JSON',
        success: function (response) {
            Swal.fire({
                title: "Updated!",
                text: "Employee Has Been Updated",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#employeeModal').modal('hide');
            $('#dataTable').DataTable().destroy();
            showEmployee();
        },
        error: function (e) {
            Swal.fire({
                title: "Failed!",
                text: "Failed To Create Employee",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function deleteEmployee(e) {
    let id = $(e).attr('data-id');
    $.ajax({
        url: '/ajax/super-admin/employee/' + id + '/delete',
        type: 'POST',
        contentType: 'application/json',
        success: function (response) {
            Swal.fire({
                title: "Deleted!",
                text: "Employee Has Been Deleted.",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#dataTable').DataTable().destroy();
            showEmployee();
        },
        error: function (e) {
            Swal.fire({
                title: "Failed!",
                text: "Employee Failed To Delete.",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}