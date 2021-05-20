const config = {
    toolbar: {
        items: [
            'bold',
            'italic',
            'link',
            'bulletedList',
            'numberedList',
            '|',
            'indent',
            'outdent',
            '|',
            'undo',
            'redo'
        ]
    },
    language: 'hu',
    image: {
        toolbar: [
            'imageTextAlternative',
            'imageStyle:full',
            'imageStyle:side'
        ]
    },
    table: {
        contentToolbar: [
            'tableColumn',
            'tableRow',
            'mergeTableCells'
        ]
    },
    licenseKey: '',
};

$(window).bind("load", function () {

    $(".numeric").numeric();

    if ($(".editor").is(":visible")) {
        ClassicEditor.create(document.querySelector('.editor'), config)
            .then(editor => {
                window.editor = editor;
            })
            .catch(error => {
            });
    }

    if ($(".editor2").is(":visible")) {
        ClassicEditor.create(document.querySelector('.editor2'), config)
            .then(editor => {
                window.editor = editor;
            })
            .catch(error => {
            });
    }

    $(document).on("click", ".button-status", function() {
        let action = $(this).data("action");
        $("form[name=status]").attr("action", action);

        $("#statusModal").modal({
            show: true,
            keyboard: false
        });
    });

    $(document).on("click", ".button-delete", function() {
        let href = $(this).data("href");
        $("#link-delete").attr("href", href);

        $("#deleteModal").modal({
            show: true,
            keyboard: false
        });
    });

    $('form').on("submit", function() {
        $("button[type=submit]", $(this)).attr("disabled", "disabled");
    });

    $(document).on("click", ".button-delete-item", function() {
        $(this).closest('.row-item').remove();
    });

    // Show order
    $(document).on("click", ".button-show", function() {
        let href = $(this).data("href");

        $.ajax({
            url: href,
            type: 'GET',
            headers: {},
            data: {},
            dataType: 'text',
            beforeSend: function () {
            },
            success: function (data) {
                $("body").prepend(data);
                $("#showOrderModal").modal({
                    show: true,
                    keyboard: true
                });
            }
        });
    });

    // Show invoice
    $(document).on("click", ".button-show-invoice", function() {
        let href = $(this).data("href");

        $.ajax({
            url: href,
            type: 'GET',
            headers: {},
            data: {},
            dataType: 'text',
            beforeSend: function () {
            },
            success: function (data) {
                $("body").prepend(data);
                $("#showInvoiceModal").modal({
                    show: true,
                    keyboard: true
                });
            }
        });
    });

    //list filter
    $(document).on("change", ".autosend", function() {
        $('[name=filter]').submit();
    });

});
