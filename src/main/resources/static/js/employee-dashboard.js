/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
const monthNames = ["January:", "February:", "March:", "April:", "May:", "June:",
  "July:", "August:", "September:", "October:", "November:", "December:"
];

$(document).ready(function () {
    let taskDone = $('#task-done')[0].innerText;
    let today = new Date();
    let month = today.getMonth();
    let lastDate = new Date(today.getFullYear(), month + 1, 0).getDate();
    let taskpct = (100*taskDone/lastDate).toFixed(2);
    $('#dashboardMonth')[0].innerText = monthNames[month];
    $('#task-pct')[0].innerText = taskpct + "%";
    $('#bar-pct').width(taskpct + "%");
});
