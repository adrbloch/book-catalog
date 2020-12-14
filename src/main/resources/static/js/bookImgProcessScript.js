$().ready(function ($) {
    $('.search-file-btn').children("input").bind('change', function () {
        let fileName = '';
        fileName = $(this).val().split("\\").slice(-1)[0];
        $(this).parent().parent().children("span").html(fileName);
    })
});

function onFileSelected(event) {
    let selectedFile = event.target.files[0];
    let reader = new FileReader();

    let imgTag = document.getElementById("bookImg");
    imgTag.title = selectedFile.name;

    reader.onload = function(event) {
        imgTag.src = event.target.result;
    };

    reader.readAsDataURL(selectedFile);
}