$('document').ready(function() {
    $('table #editButton').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function (blog, status) {
            $('#idEdit').val(blog.id);
            $('#titleEdit').val(blog.title);
            $('#contentEdit').val(blog.content);
        });
        $('#editModal').modal();
    });
});