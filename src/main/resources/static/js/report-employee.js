/* global Swal */

$('document').ready(function () {
    let today = new Date();
    let yyyy = today.getFullYear();
    let isUploaded = $('.report-status-page')[0].innerText;
    $('#yearpicker')[0].value = yyyy;
    $('#yearpicker').datepicker({
        format: "yyyy",
        viewMode: "years",
        minViewMode: "years"
    });
});

$(document).on('show.bs.modal', '.modal', function () {
    var zIndex = 1040 + (10 * $('.modal:visible').length);
    $(this).css('z-index', zIndex);
    setTimeout(function () {
        $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
    }, 0);
});

$(document).on('hidden.bs.modal', '.modal', function () {
    $('.modal:visible').length && $(document.body).addClass('modal-open');
});

$('#yearpicker').change(function () {
    let year = this.value;
    $('#dataTable').DataTable().destroy();
    reloadReport(year);
});

$('#accept-btn').on('click', function () {
    let self = $(this);
    $('#myLargeModalLabel')[0].innerText = 'Accept Report';
    $('#message-report-id')[0].innerText = $('#report-id').val();
    $('#message-status')[0].innerText = 'accept';
    $(self.data('target')).modal('show');
});

$('#reject-btn').on('click', function () {
    let self = $(this);
    $('#myLargeModalLabel')[0].innerText = 'Reject Report';
    $('#message-report-id')[0].innerText = $('#report-id').val();
    $('#message-status')[0].innerText = 'reject';
    $(self.data('target')).modal('show');
});

$('#message-btn').on('click', function () {
    let self = $(this);
    $('#myLargeModalLabel')[0].innerText = 'Send Message';
    $('#message-report-id')[0].innerText = $('#report-id').val();
    $('#message-status')[0].innerText = null;
    $(self.data('target')).modal('show');
});

$('#message-btn-page').on('click', function () {
    let self = $(this);
    $('#myLargeModalLabel')[0].innerText = 'Send Message';
    $('#message-report-id')[0].innerText = "No Report Yet";
    $('#message-status')[0].innerText = null;
    $(self.data('target')).modal('show');
});

$('#messageForm').on('submit', function (e) {
    e.preventDefault();
    let status = $('#message-status')[0].innerText;
    let id = $('#message-report-id')[0].innerText;
    let empId = $('#empId')[0].innerText;
    let noteBody = $('#note-body').val();
    let isAccepted;
    if (id == 'No Report Yet') {
        sendReminderNote(noteBody, empId);
    } else if (status == 'accept') {
        isAccepted = true;
        sendNoteReport(id, noteBody, isAccepted);
    } else if (status == 'reject') {
        isAccepted = false;
        sendNoteReport(id, noteBody, isAccepted);
    } else if (status == "") {
        isAccepted = null;
        sendNoteReport(id, noteBody, isAccepted);
    }
});

function sendReminderNote(noteBody, empId) {
    let note = {
        'note': noteBody,
        'employees': {
            'id': empId
        }
    };
    Swal.fire({
        title: "Sending...",
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
        url: '/relation-manager/note-history/send',
        data: JSON.stringify(note),
        dataType: 'TEXT',
        success: function (result) {
            Swal.fire({
                title: "Sent!",
                text: "Message Successfully Sent",
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#messageModal').modal('hide');
            location.reload();
        }
    });
}

function sendNoteReport(id, noteBody, isAccepted) {
    let status;
    if (isAccepted == null) {
        status = "Sent";
    } else if (isAccepted) {
        status = "Accepted"
    } else if (!isAccepted) {
        status = "Rejected";
    }
    let note = {
        'note': noteBody,
        'report': {
            'id': id
        },
        'isAccepted': isAccepted
    };
    let title = status + "!";
    let text = "Successfully " + status;
    Swal.fire({
        title: "Sending...",
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
        url: '/relation-manager/note-history/send',
        data: JSON.stringify(note),
        dataType: 'TEXT',
        success: function (result) {
            Swal.fire({
                title: title,
                text: text,
                timer: 1000,
                icon: "success",
                showConfirmButton: false
            });
            $('#messageModal').modal('hide');
            if (isAccepted == null) {
                console.log(null);
            } else if (isAccepted) {
                $('.report-status-page')[0].innerText = 'Accepted';
            } else if (!isAccepted) {
                $('.report-status-page')[0].innerText = 'Rejected';
            }
            $("#note-table-body").empty();
            getNoteByReport(id);
            $('#messageModal').modal('hide');
            $('#reportEmployeeModal').modal('hide');
            $('#dataTable').DataTable().destroy();
            location.reload();
        },
        error: function (e) {
            console.log(e);
            Swal.fire({
                title: "Error!",
                text: "Error has occurs",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function reloadReport(year) {
    let empId = $('#empId')[0].innerText;
    $('#dataTable').DataTable({
        'sAjaxSource': '/relation-manager/report/' + year + "/" + empId,
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
                    return "<div class='form-group' style='text-align: center'>"
                            + "<a class='btn btn-info btn-circle mr-2' data-toggle='modal' data-target='#reportEmployeeModal' onclick='setValue(\"" + row.id + "\",\"" + row.name + "\",\"" + row.reportDate + "\",\"" + row.uploadDate + "\",\"" + row.reportStatus.reportStatusName + "\")'>"
                            + "<i class='fas fa-info-circle'></i></a>"
                            + "</div>";
                }
            }

        ]
    })
}

function message(reportId) {
    $('#message-report-id')[0].innerText = reportId;
}

function setValue(id, name, reportDate, uploadDate, status) {
    $('#report-id').val(id);
    $('#report-name').val(name);
    $('#report-date').val(reportDate);
    $('#upload-date').val(uploadDate);
    $('#report-status').val(status);
    setHidden(status);
    $("#note-table-body").empty();
    getNoteByReport(id);
}

function setHiddenPage(status) {
    if (status == "Not Uploaded") {
        $('#detail-btn-page').prop('hidden', true);
        $('#message-btn-page').prop('hidden', false);
    } else {
        $('#detail-btn-page').prop('hidden', false);
        $('#message-btn-page').prop('hidden', true);
    }
}

function setHidden(status) {
    if (status == "Not Uploaded") {
        $('#download-btn').prop('hidden', true);
        $('#accept-btn').prop('hidden', true);
        $('#reject-btn').prop('hidden', true);
    } else if (status == 'Accepted') {
        $('#download-btn').prop('hidden', false);
        $('#accept-btn').prop('hidden', true);
        $('#reject-btn').prop('hidden', true);
    } else if (status == 'Rejected') {
        $('#download-btn').prop('hidden', false);
        $('#accept-btn').prop('hidden', false);
        $('#reject-btn').prop('hidden', true);
    } else {
        $('#download-btn').prop('hidden', false);
        $('#accept-btn').prop('hidden', false);
        $('#reject-btn').prop('hidden', false);
    }
}

function getNoteByReport(reportId) {
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/employee/history/report/' + reportId,
        success: function (result) {
            let event_data = "";
            $.each(result, function (index, value) {
                event_data += '<tr>';
                event_data += '<td>' + value.createdDate + '</td>';
                console.log(value.isAccepted);
                if (value.isAccepted == null) {
                    event_data += '<td>' + 'Message - ' + value.note + '</td>';
                } else if (value.isAccepted == false) {
                    event_data += '<td>' + 'Rejected - ' + value.note + '</td>';
                } else if (value.isAccepted == true) {
                    event_data += '<td>' + 'Accepted - ' + value.note + '</td>';
                }
                event_data += '</tr>';
            });
            $('#note-table').append(event_data);
        },
        error: function (e) {
            console.log(e);
        }
    })
}

function download() {
    let id = $('#report-id').val();
    let fileName = id.replace("/", "_");
    let report = {
        'id': id
    };
    let url = '/employee/report/download/' + id;
    Swal.fire({
        title: "Download Report?",
        text: "Download Report: " + id,
        icon: "question",
        showCancelButton: true,
        confirmButtonText: "Yes, Download Report!"
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = url;
        }
    });
}