$(window).bind("load", function () {

    $(".numeric").numeric();

    if ($(".editor").is(":visible")) {
        ClassicEditor.create(document.querySelector('.editor'), {
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
        })
            .then(editor => {
                window.editor = editor;
            })
            .catch(error => {
            });
    }

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

    $(document).on("click", "#newItem", function() {
        let lastItem = $("select[name='materialId[]']:last");
        let type = $(this).data("url");

        if (lastItem.val() !== "") {
            $.ajax({
                url: type + '/item',
                type: 'GET',
                headers: {},
                data: {},
                dataType: 'text',
                beforeSend: function () {
                },
                success: function (data) {
                    $("#newItems").append(data);
                }
            });
        }
        else {
            lastItem.focus();
        }
    });

    $(document).on("click", ".button-delete-item", function() {
        $(this).closest('.row-item').remove();
    });

    $(document).on("change", "#shoppingMaterial", function() {
        let measure = $(this).children("option:selected").data("measure");
        $(".measure").html(measure);
    });

    // Measures
    function setMeasureSelect(row, selectFirst) {
        let material = $("[name=materialId]", row);

        let measureCategoryId = parseInt(material.children("option:selected").data("mc"));
        let customMeasureId = parseInt(material.children("option:selected").data("mcustomi"));
        let measures = $("[name=measureId] option", row);

        let matchingListElements = measures.filter(function() {
            let id = parseInt($(this).val());
            let categoryId = parseInt($(this).data("category"));

            return ($(this).val() === "" || id === customMeasureId || categoryId === measureCategoryId);
        });

        if (selectFirst) {
            $("[name=measureId]", row).val("");
        }

        measures.hide();
        matchingListElements.show();
    }

    $(document).on("change", "[name=materialId]", function() {
        let row = $(this).closest(".row-item");
        setMeasureSelect(row, true);
    });

    $(".row-item").each(function () {
        let row = $(this);
        setMeasureSelect(row, false);
    });

    // Prepare
    $(document).on("click", ".button-prepare", function() {
        let recipeId = $(this).data("id");
        let recipeName = $(this).data("name");

        $("[name=recipeId]").val(recipeId);
        $("#prepareModalLabel").html(recipeName + " elkészítése");

        $("#prepareModal").modal({
            show: true,
            keyboard: true
        });
    });

    $(document).on("click", "#prepareIt", function() {
        let url = $("#formCookingRecipe").attr("action");
        let formData = $("#formCookingRecipe").serialize();

        $.ajax({
            url: url,
            type: 'POST',
            headers: {},
            data: formData,
            dataType: 'text',
            beforeSend: function () {
            },
            success: function (data) {
                $("body").prepend(data);
                $("#prepareModal").modal("toggle");
                $("#prepareMessageModal").modal({
                    show: true,
                    keyboard: true
                });
            }
        });
    });

    // Show recipe
    $(document).on("click", ".button-show", function() {
        let href = $(this).data("href");

        $.ajax({
            url: href,
            type: 'get',
            headers: {},
            data: {},
            dataType: 'text',
            beforeSend: function () {
            },
            success: function (data) {
                $("body").prepend(data);
                $("#showRecipeModal").modal({
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
