/* global Swal */

$('#report-dropdown').change(function () {
    $('#dataTables').DataTable().destroy();
    reloadHistory(this.value);
});

function read(e) {
    let url = $(e).parent('form')[0].action;
    console.log(url);
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: url,
        success: function (result) {
            e.disabled = true;
            e.innerText = "Read";
            console.log("read");
        },
        error: function (e) {
            console.log(e);
            Swal.fire({
                title: "Error Occurs",
                timer: 1000,
                icon: "error",
                showConfirmButton: false
            });
        }
    });
}

function reloadHistory(reportId) {
    let url = (reportId === "Select All") ? '/employee/history/ajax' : '/employee/history/report/' + reportId;
    table = $('#dataTables').DataTable({
        'sAjaxSource': url,
        'sAjaxDataProp': '',
        'columns': [
            {'data': 'createdDate'},
            {'data': 'report.id'},
            {'data': 'employees.firstName'},
            {'data': 'note'},
            {
                'render': function (data, type, row, meta) {
                    if (row.isRead) {
                        return "<form action='/employee/history/read/" + row.id + "' method='POST'> " +
                                "<button class='btn btn-success' type='button' disabled>Read</button> " +
                                "</form>";
                    } else {
                        return "<form action='/employee/history/read/" + row.id + "' method='POST'> " +
                                "<button class='btn btn-success read-btn' type='button' onclick='read(this)'>Mark As Read</button> " +
                                "</form>";
                    }
                }
            }
        ]
    });
}

$('document').ready(function () {
    $('#dataTables').DataTable();
    $('#report-dropdown').select2();
});