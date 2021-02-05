/* global Swal */

$('document').ready(function () {
    let today = new Date();
    let yyyy = today.getFullYear();
    $('#yearpicker')[0].value = yyyy;
    $('#yearpicker').datepicker({
        format: "yyyy",
        viewMode: "years",
        minViewMode: "years"
    });
});

$('#yearpicker').change(function () {
    let year = this.value;
    $('#dataTable').DataTable().destroy();
    reloadReport(year);
});

$('#modalForm').on('submit', function (e) {
    e.preventDefault();
    let isCreate = document.getElementById('id').hidden;
    (isCreate) ? createReport() : updateReport();
});

function downloadBtn(id) {
    let fileName = id.replace("/", "_");
    Swal.fire({
        title: "Download Report?",
        text: "Download Report: " + id,
        icon: "question",
        showCancelButton: true,
        confirmButtonText: "Yes, Generate Report!"
    }).then((result) => {
        if (result.isConfirmed) {
            let url = '/employee/report/download/' + id;
            window.location.href = url;
        }
    });
}

function setValue(id, name, reportDate, uploadDate, status) {
    $('#id').val(id);
    $('#name').val(name);
    if (reportDate != null) {
        let rStr = reportDate.split("-");
        let rDate = rStr[2] + '-' + rStr[1] + '-' + rStr[0];
        $('#report-date').val(rDate);
    }
    if (uploadDate != null) {
        let uStr = uploadDate.split("-");
        let uDate = uStr[2] + '-' + uStr[1] + '-' + uStr[0];
        $('#upload-date').val(uDate);
    }
    $('#status').val(status);
}

function setDisabled(isDetail, isCreate) {
    $('#id').prop('hidden', isCreate);
    $('#id-l').prop('hidden', isCreate);
    $('#name').prop('disabled', isDetail);
    $('#name-l').prop('disabled', isDetail);
    $('#report-date').prop('disabled', isDetail);
    $('#report-date-l').prop('disabled', isDetail);
    $('#upload-date').prop('hidden', isCreate);
    $('#upload-date-l').prop('hidden', isCreate);
    $('#status').prop('hidden', isCreate);
    $('#status-l').prop('hidden', isCreate);
    $('#file').prop('hidden', isDetail);
    $('#file-l').prop('hidden', isDetail);
    $('#btn-save').prop('hidden', isDetail);
}

function detail(id, name, reportDate, uploadDate, status) {
    setValue(id, name, reportDate, uploadDate, status);
    setDisabled(true, false);
}

function edit(id, name, reportDate, uploadDate, status) {
    setValue(id, name, reportDate, uploadDate, status);
    setDisabled(false, false);
}

function create(id, name, reportDate, uploadDate, status) {
    setValue(id, name, reportDate, uploadDate, status);
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
            deleteReport(e);
        }
    })
            ;
}

function createReport() {
    console.log('ini create');
    let name = $("[name='name']").val();
    let splitDate = $("[name='reportDate']").val().split("-");
    let reportDate = splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0];
    let data = new FormData();
    jQuery.each(jQuery('#file')[0].files, function (i, file) {
        data.append('file', file);
    });
    $.ajax({
        type: 'POST',
        url: '/employee/report?' + $.param({name: name, reportDate: reportDate}),
        data: data,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        success: function (result) {
            console.log(result);
            Swal.fire({
                title: "Created!",
                text: "Report has been created!",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#reportModal').modal('hide');
            $('#yearpicker').val(splitDate[0]);
            $('#dataTable').DataTable().destroy();
            reloadReport(splitDate[0]);
        }, error: function (e) {
            Swal.fire({
                title: "Failed!",
                text: "Failed to submit report",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function updateReport() {
    console.log('ini update');
    let id = $("[name='id']").val();
    let name = $("[name='name']").val();
    let splitDate = $("[name='reportDate']").val().split("-");
    let reportDate = splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0];
    let file = $("[name='file']")[0].files[0];
    let data = new FormData();
    jQuery.each(jQuery('#file')[0].files, function (i, file) {
        data.append('file', file);
    });
    $.ajax({
        type: 'POST',
        url: '/employee/report/' + id + "/update?" + $.param({name: name, reportDate: reportDate}),
        data: data,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        success: function (result) {
            console.log(result);
            Swal.fire({
                title: "Updated!",
                text: "Report has been updated",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#reportModal').modal('hide');
            $('#yearpicker').val(splitDate[0]);
            $('#dataTable').DataTable().destroy();
            reloadReport(splitDate[0]);
        }, error: function (e) {
            Swal.fire({
                title: "Failed!",
                text: "Failed to update report",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });

}

function deleteReport(e) {
    let id = $(e).attr('data-id');
    let year = $('#yearpicker').val();
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: '/employee/report/' + id + "/delete",
        success: function (result) {
            Swal.fire({
                title: "Deleted!",
                text: "Report has been deleted",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#dataTable').DataTable().destroy();
            reloadReport(year);
        },
        error: function (e) {
            Swal.fire({
                title: "Failed!",
                text: "Report failed to delete.",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function reloadReport(year) {
    $('#dataTable').DataTable({
        'sAjaxSource': '/employee/report/' + year,
        'sAjaxDataProp': '',
        'columns': [
            {'data': 'id'},
            {'data': 'name'},
            {'data': 'reportDate'},
            {
                'render': function (data, type, row, meta) {
                    if (row.uploadDate != null) {
                        return row.uploadDate;
                    } else {
                        return "Not Uploaded";
                    }
                }
            },
            {'data': 'reportStatus.reportStatusName'},
            {
                'render': function (data, type, row, meta) {
                    if (row.uploadDate != null) {
                        return "<div class='form-inline'> " +
                                "<button onclick='detail(\"" + row.id + "\",\"" + row.name + "\",\"" + row.reportDate + "\",\"" + row.uploadDate + "\",\"" + row.reportStatus.reportStatusName + "\")' " + "class='btn btn-info btn-circle mr-1' data-toggle='modal' data-target='#reportModal'> " +
                                "<i class='fas fa-info-circle'></i></button> " +
                                "<button onclick='edit(\"" + row.id + "\",\"" + row.name + "\",\"" + row.reportDate + "\",\"" + row.uploadDate + "\",\"" + row.reportStatus.reportStatusName + "\")' " + "class='btn btn-warning btn-circle mr-1' data-toggle='modal' data-target='#reportModal'> " +
                                "<i class='fas fa-pencil-alt'></i></button> " +
                                "<form class='form-inline'> " +
                                "<button data-id='" + row.id + "' type='button' onclick='confirm(this)' class='btn btn-danger btn-circle delete-button mr-1'> " +
                                "<i class='fas fa-trash'></i></button></form> " +
                                "<button id='downloadBtn' class='btn btn-success btn-circle' onclick='downloadBtn(\"" + row.id + "\")'> " +
                                "<i class='fas fa-download'></i></button>" +
                                "</div>";
                    } else {
                        return  "<div class='form-inline'> " +
                                "<button onclick='detail(\"" + row.id + "\",\"" + row.name + "\",\"" + row.reportDate + "\",\"" + row.uploadDate + "\",\"" + row.reportStatus.reportStatusName + "\")' " + "class='btn btn-info btn-circle' data-toggle='modal' data-target='#reportModal'> " +
                                "<i class='fas fa-info-circle'></i></button> " +
                                "<button onclick='edit(\"" + row.id + "\",\"" + row.name + "\",\"" + row.reportDate + "\",\"" + row.uploadDate + "\",\"" + row.reportStatus.reportStatusName + "\")' " + "class='btn btn-warning btn-circle' data-toggle='modal' data-target='#reportModal'> " +
                                "<i class='fas fa-pencil-alt'></i></button> " +
                                "<form class='form-inline'> " +
                                "<button data-id='" + row.id + "' type='button' onclick='confirm(this)' class='btn btn-danger btn-circle delete-button'> " +
                                "<i class='fas fa-trash'></i></button></form> " +
                                "</div>";

                    }
                }
            }

        ]
    })
}