$(document).ready(function () {
    $.ajax({
        url:"http://localhost:8080/greeting-javaconfig"
    }).then(function (data) {
        $('.greeting-id').append(data.id);
        $('.greeting-content').append(data.content);

    });

})