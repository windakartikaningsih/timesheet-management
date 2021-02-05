/* global Swal */

var table;
let empId = $('#empId')[0].innerText;

$('document').ready(function () {
    let today = new Date();
    let dd = today.getDate();
    let yyyy = today.getFullYear();
    let MM = String(today.getMonth() + 1).padStart(2, '0');
//    $('select').select2();
//    $('#dataTable').DataTable();
    $('#dateChoosen')[0].value = yyyy + "-" + MM;
    $('#dataTable').DataTable().destroy();
    reloadTask(empId, yyyy + "-" + MM + "-" + dd);
});

$('#dateChoosen').change(function () {
    let date = $(this)[0].value + "-01";
    $('#dataTable').DataTable().destroy();
    reloadTask(empId, date);
});

function reloadTask(empId, date){
    $('#dataTable').DataTable({
        'sAjaxSource': '/employee/task/' + date + "/" + empId,
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
            }
        ]
    });
}

function generate() {
    let date = $('#dateChoosen')[0].value;
    let splitDate = date.split('-');
    let dateString = '01-' + splitDate[1] + '-' + splitDate[0];
    let data = document.getElementById('dataTable').rows[1].firstElementChild.innerText;
    let task = {
        'date': dateString,
        'employees': {
            'id': empId
        }
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