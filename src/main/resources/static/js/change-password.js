/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global Swal */

$('#changePassword').on('click', function (e) {
    e.preventDefault();
    let oldPassword = $('#oldPassword').val();
    let newPassword = $('#newPassword').val();
    if (oldPassword === newPassword) {
        Swal.fire({
            title: "Same password!",
            text: "New password can not be same as old",
            timer: 1500,
            icon: "error",
            showConfirmButton: false
        });
    } else {
        console.log("submit");
        $('#passwordForm')[0].submit();
    }
});
