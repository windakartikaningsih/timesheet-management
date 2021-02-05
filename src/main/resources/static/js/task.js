/* global Swal */

var table;

$('document').ready(function () {
    let today = new Date();
    let dd = today.getDate();
    let yyyy = today.getFullYear();
    let MM = String(today.getMonth() + 1).padStart(2, '0');
    $('select').select2();
//    $('#dataTable').DataTable();
    $('#dateChoosen')[0].value = yyyy + "-" + MM;
    reloadTask(yyyy + "-" + MM + "-" + dd);
});

$('#dateChoosen').change(function () {
    let date = $(this)[0].value + "-01";
    $('#dataTable').DataTable().destroy();
    reloadTask(date);
});

$('#modalForm').on('submit', function (e) {
    e.preventDefault();
    let isCreate = document.getElementById('id').hidden;
    (isCreate) ? createTask() : updateTask();
});

function reloadTask(date) {
    $('#dataTable').DataTable({
        'sAjaxSource': '/employee/task/' + date + "/0",
        'sAjaxDataProp': '',
        'columns': [
            {'data': 'projectName'},
            {'data': 'date'},
            {'data': 'startHour'},
            {'data': 'endHour'},
            {'data': 'statusAttendance.statusAttendanceName'},
            {
                'render': function (data, type, row, meta) {
                    return row.category.categoryName + " - " + row.activity;
                }
            },
            {
                'render': function (data, type, row, meta) {
                    return "<center class='form-inline'> " +
                            "<button onclick='detail(\"" + row.projectName + "\",\"" + row.date + "\",\"" + row.startHour + "\",\"" + row.endHour + "\",\"" + row.activity + "\",\"" + row.statusAttendance.id + "\",\"" + row.category.id + "\",\"" + row.id + "\")' " + "class='btn btn-info btn-circle' data-toggle='modal' data-target='#taskModal'> " +
                            "<i class='fas fa-info-circle'></i></button> " +
                            "<button onclick='edit(\"" + row.projectName + "\",\"" + row.date + "\",\"" + row.startHour + "\",\"" + row.endHour + "\",\"" + row.activity + "\",\"" + row.statusAttendance.id + "\",\"" + row.category.id + "\",\"" + row.id + "\")' " + "class='btn btn-warning btn-circle' data-toggle='modal' data-target='#taskModal'> " +
                            "<i class='fas fa-pencil-alt'></i></button> " +
                            "<form class='form-inline'> " +
                            "<button data-id='" + row.id + "' type='button' onclick='confirm(this)' class='btn btn-danger btn-circle delete-button'> " +
                            "<i class='fas fa-trash'></i></button></form></center>";
                }
            }
        ]
    });
}


function setValue(taskName, date, startHour, endHour, activity, statusAttendance, category, id) {
    let dateSplit = date.split('-');
    $('#idVal').val(id);
    $('#projectName').val(taskName);
    $('#date').val(dateSplit[2] + '-' + dateSplit[1] + '-' + dateSplit[0]);
    $('#startHour').val(startHour);
    $('#endHour').val(endHour);
    $('#activity').val(activity);
    $('#category-dropdown').select2().select2('val', category);
    $('#status-attendance-dropdown').select2().select2('val', statusAttendance);

}

function setDisabled(isDetail, isCreate) {
    $('#id').prop('hidden', isCreate);
    $('#idVal').prop('disabled', true);
    $('#projectName').prop('disabled', isDetail);
    $('#date').prop('disabled', isDetail);
    $('#startHour').prop('disabled', isDetail);
    $('#endHour').prop('disabled', isDetail);
    $('#activity').prop('disabled', isDetail);
    $('#category-dropdown').prop('disabled', isDetail);
    $('#status-attendance-dropdown').prop('disabled', isDetail);
    $('#btn-save').prop('hidden', isDetail);
}

function detail(taskName, date, startHour, endHour, activity, statusAttendance, category, id) {
    setValue(taskName, date, startHour, endHour, activity, statusAttendance, category, id);
    setDisabled(true, false);
}

function edit(taskName, date, startHour, endHour, activity, statusAttendance, category, id) {
    setValue(taskName, date, startHour, endHour, activity, statusAttendance, category, id);
    setDisabled(false, false);
}

function create() {
    setValue(null, "", null, null, null, null, null);
    setDisabled(false, true);
}

function generate(e) {
    let date = $('#dateChoosen')[0].value;
    let data = document.getElementById('dataTable').rows[1].firstElementChild.innerText;
    let splitDate = date.split('-');
    let dateString = '01-' + splitDate[1] + '-' + splitDate[0];
    let task = {
        'date': dateString
    };
    if (date != '' && data != "No data available in table") {
        Swal.fire({
            title: "Generate Report?",
            text: "Generate Monthly Report on " + date,
            icon: "question",
            showCancelButton: true,
            confirmButtonText: "Yes, Generate Report!"
        }).then((result) => {
            if (result.isConfirmed) {
                var request = new XMLHttpRequest();
                var fileName = "Report-" + date;
                request.open('POST', "/employee/task/generate", true);
                request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
                request.responseType = 'blob';

                request.onload = function (e) {
                    if (this.status === 200) {
                        var blob = this.response;
                        if (window.navigator.msSaveOrOpenBlob) {
                            window.navigator.msSaveBlob(blob, fileName);
                        } else {
                            var downloadLink = window.document.createElement('a');
                            var contentTypeHeader = request.getResponseHeader("Content-Type");
                            downloadLink.href = window.URL.createObjectURL(new Blob([blob], {type: contentTypeHeader}));
                            downloadLink.download = fileName;
                            document.body.appendChild(downloadLink);
                            downloadLink.click();
                            document.body.removeChild(downloadLink);
                        }
                    }
                };
                request.send(JSON.stringify(task));
            }
        });
    } else if (data == "No data available in table") {
        Swal.fire({
            title: "Can Not Generate!",
            text: "No Task On This Month!",
            timer: 1200,
            icon: "error",
            showConfirmButton: false
        });
    } else if (date == '') {
        Swal.fire({
            title: "Can Not Generate!",
            text: "Please Choose the date!",
            timer: 1200,
            icon: "error",
            showConfirmButton: false
        });
    }
}

function confirm(e) {
    let date = $('#dateChoosen').val() + "-01";

    Swal.fire({
        title: "Are you sure?",
        text: "You will not be able to recover this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, delete it!"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: "Deleted!",
                text: "Task Has Been Deleted",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            deleteTask(e);
            $('#dataTable').DataTable().destroy();
            reloadTask(date);
        }
    });
}

function createTask() {
    let start = $("[name='startHour']").val();
    let end = $("[name='endHour']").val();
    let date = $("[name='date']").val();
    let dateChoosen = date.split('-')[0] + "-" + date.split('-')[1];
    let category = $('#category-dropdown').val();
    let statusAttendance = $('#status-attendance-dropdown').val();
    if (start > end) {
        Swal.fire({
            title: "Error",
            text: "End Hour Must Not Be Earlier Than Start Hour",
            timer: 1200,
            icon: "error",
            showConfirmButton: false
        });
    } else if (category == "") {
        Swal.fire({
            title: "Error",
            text: "Category Must Not Be Null",
            timer: 1200,
            icon: "error",
            showConfirmButton: false
        });
    } else if (statusAttendance == "") {
        Swal.fire({
            title: "Error",
            text: "Status Attendance Must Not Be Null",
            timer: 1200,
            icon: "error",
            showConfirmButton: false
        });
    } else {
        let task = {
            'projectName': $("[name='projectName']").val(),
            'date': date,
            'startHour': start,
            'endHour': end,
            'activity': $("[name='activity']").val(),
            'statusAttendance': {
                'id': statusAttendance
            },
            'category': {
                'id': category
            }
        };
        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/employee/task',
            data: JSON.stringify(task),
            dataType: 'JSON',
            success: function (jqXHR, textStatus, errorThrown) {
                console.log('berhasil');
                Swal.fire({
                    title: "Created!",
                    text: "Task has been created",
                    timer: 1000,
                    icon: "success",
                    showConfirmButton: false
                });
                $('#taskModal').modal('hide');
                $('#dataTable').DataTable().destroy();
                reloadTask(date);
                $('#dateChoosen').val(dateChoosen);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                if (jqXHR.status === 409) {
                    Swal.fire({
                        title: "Failed!",
                        text: "Data Already Exist",
                        timer: 1000,
                        icon: "error",
                        showConfirmButton: false
                    });
                } else {
                    Swal.fire({
                        title: "Failed!",
                        text: "Failed to Create Task",
                        timer: 1000,
                        icon: "error",
                        showConfirmButton: false
                    });
                }
            }
        });
    }
}

function updateTask() {
    let id = $("[name='id']").val();
    let start = $("[name='startHour']").val();
    let end = $("[name='endHour']").val();
    let date = $("[name='date']").val();
    let dateChoosen = date.split('-')[0] + "-" + date.split('-')[1];
    let category = $('#category-dropdown').val();
    let statusAttendance = $('#status-attendance-dropdown').val();
    if (start > end) {
        Swal.fire({
            title: "Error",
            text: "End Hour Must Not Be Earlier Than Start Hour",
            timer: 1200,
            icon: "error",
            showConfirmButton: false
        });
    } else if (category == null) {
        Swal.fire({
            title: "Error",
            text: "Category Must Not Be Null",
            timer: 1200,
            icon: "error",
            showConfirmButton: false
        });
    } else if (statusAttendance == null) {
        Swal.fire({
            title: "Error",
            text: "Status Attendance Must Not Be Null",
            timer: 1200,
            icon: "error",
            showConfirmButton: false
        });
    } else {
        let task = {
            'projectName': $("[name='projectName']").val(),
            'date': date,
            'startHour': start,
            'endHour': end,
            'activity': $("[name='activity']").val(),
            'statusAttendance': {
                'id': statusAttendance
            },
            'category': {
                'id': category
            }
        };
        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/employee/task/' + id + "/update",
            data: JSON.stringify(task),
            dataType: 'JSON',
            success: function (result) {
                console.log('berhasil');
                console.log(result);
                Swal.fire({
                    title: "Updated!",
                    text: "Task has been updated",
                    timer: 1000,
                    icon: "success",
                    showConfirmButton: false
                });
                $('#taskModal').modal('hide');
                $('#dataTable').DataTable().destroy();
                reloadTask(date);
                $('#dateChoosen').val(dateChoosen);
            },
            error: function (result) {
                Swal.fire({
                    title: "Failed!",
                    text: "Failed to update Task",
                    timer: 1000,
                    icon: "error",
                    showConfirmButton: false
                });
            }
        });
    }
}

function deleteTask(e) {
    let id = $(e).attr('data-id');
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: '/employee/task/' + id + '/delete',
        success: function (result) {
            console.log('deleted');
        },
        error: function (e) {
            Swal.fire({
                title: "Failed!",
                text: "Task failed to delete.",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}