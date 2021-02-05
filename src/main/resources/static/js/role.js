/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var table;

$(document).ready( function () {
    getAllRegion();
    
    $('#btn-save').on('click',function(e){
        e.preventDefault();
        let id = $(this).attr('data-id');
        if(id == undefined){
            saveRegion("/ajax/regions/add"); 
        }
        else{
            updateRegion("/ajax/regions/" + id + "/edit");
        }
    })
});

function detailRegion(id, name) {
    setForm(id, name);
    setEnableField(true, false);
}

function create() {
    setForm('', '');
    setEnableField(false, false);
}
function update(id, name) {
    setForm(id, name);
    setEnableField(false, true);
}

function setForm(id, name) {
    $('#id').val(id);
    $('#name').val(name);
}

function setEnableField(isEnabled, isUpdate) {
    $('#btn-save').prop('disabled', isEnabled);
    $('#id').prop('disabled', isUpdate ? !isEnabled : isEnabled);
    $('#name').prop('disabled', isEnabled);
}

function deleteRegion(id) {
    Swal.fire({
      title: "Are you sure want to delete this data?",
      text: "You will not be able to recover this data",
      icon: "warning",
      showCancelButton: true,
      confirmButtonClass: "btn btn-danger",
      confirmButtonText: "Yes, delete it!",
      cancelButtonClass: "btn btn-info",
      cancelButtonText: "No, cancel it!",
      closeOnConfirm: false,
      closeOnCancel: false
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: "/ajax/regions/" + id + "/delete",
                type: "GET",
                dataType: "JSON",
                success: function (response) {
                    console.log(response);
                    if (response.success == true) {
                        Swal.fire(response.message, 'Data Deleted Successfully', 'success')
                    }
                    table.destroy();
                    getAllRegion();
                },
                error: function (e) {
                    console.log(e);
                }
            });
        } else {
            Swal.fire('Cancelled', 'Your data is safe :)', 'error')
        }
    })    
}

function regionDetail(id) {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/ajax/regions/" + id,
        success: function (response) {
//            console.log(response);
            setForm(response.regionId, response.regionName);
            setEnableField(true, false);
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function getAllRegion(){
    table = $('#regionTable').DataTable({
        'sAjaxSource': '/ajax/regions',
        'sAjaxDataProp': '',
        'columns': [
            {'data': 'regionId'},
            {'data': 'regionName'},
            {
                'render': function (data, type, row, meta) {
                    return '\
                    <button class="btn btn-primary" href=# \n\
                        onclick="regionDetail(\'' + row.regionId + '\')" \n\
                        data-toggle="modal" data-target="#regionModal"><i class="fas fa-eye"></i> \n\
                    </button> \n\
                    <button class="btn btn-warning" href="#" data-id="\'' + row.regionId + '\'"  \n\
                        onclick="update(\'' + row.regionId + '\', \'' + row.regionName + '\')" \n\
                        data-toggle="modal" data-target="#regionModal"><i class="fas fa-edit"></i> \n\
                    </button> \n\
                    <button class="btn btn-danger" href="#" \n\
                        onclick="deleteRegion(\'' + row.regionId + '\')"><i class="fas fa-trash-alt"></i> \n\
                    </button>';
                }
            }
        ]
    });
}

function saveRegion() {
    var data = $('#form').serialize();
    $.ajax({
        url: "ajax/regions/add",
        type: "POST",
        data: data,
        dataType: "JSON",
        success: function (response) {
            console.log(response);
            if(response.success == true) {
                Swal.fire('Save Success', response.message, 'success')
            }
            table.destroy();
            getAllRegion();
        }
    });
}

function updateRegion(id) {
    var data = $('#form').serialize();
    $.ajax({
        url: "ajax/regions/" + id + "/edit",
        type: "POST",
        data: data,
        dataType: "JSON",
        success: function (response) {
            console.log(response);
            if(response.success == true) {
                Swal.fire('Data Added Successfully', response.message, 'success')
            }
            table.destroy();
            getAllRegion();
        }
    });
}
