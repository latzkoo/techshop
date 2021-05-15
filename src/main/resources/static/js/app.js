function createListFilterURL() {
    let param = new URLSearchParams(window.location.search);
    let sort = $("#sort").val();

    if (!param.has("sort")) param.append("sort", sort);
    else param.set("sort", sort);

    return window.location.pathname + "?" + param.toString();
}

$(window).bind("load", function () {
    // Sort product list
    $(document).on("change", "#sort", function () {
        window.location = createListFilterURL();
    });

    // Add product to cart
    $(document).on("click", ".addToCart", function () {
        let productId = parseInt($(this).data("id"));
        let qty = parseInt($("[name=qty]").val()) || 1;

        $.ajax({
            url: '/kosar/add/' + productId,
            type: 'POST',
            data: {
                qty: qty
            },
            dataType: 'json',
            beforeSend: function() {},
            success: function(data){
                if (data.status === "error") {
                    alert(data.message);
                }
                else {
                    $("#cartQty").html(data.data.qty);
                    $("#cartValue").html(data.data.value + " Ft");

                    alert("A termék a kosárba került.");
                }
            }
        });
    });

    // Update cart item
    $(document).on("change keyup", ".updateCartItem", function () {
        let productId = parseInt($(this).data("id"));
        let qty = parseInt($(this).val());

        $.ajax({
            url: '/kosar/update/' + productId,
            type: 'POST',
            data: {
                qty: qty
            },
            dataType: 'json',
            beforeSend: function() {},
            success: function(data){
                if (data.status === "error") {
                    alert(data.message);
                }
                else {
                    $("#cartQty").html(data.data.qty);
                    $("#cartValue").html(data.data.value + " Ft");
                    $("#total").html(data.data.value + " Ft");
                }
            }
        });
    });

    $(document).on("submit", "form", function () {
        $("button[type=submit]", $(this)).attr("disabled", "disabled");
    });

    $(document).on("submit", "[name=form-comment]", function () {
        let url = $(this).attr("action");
        let params = $(this).serializeArray();

        $.ajax({
            url: url,
            type: 'POST',
            data: params,
            dataType: 'text',
            beforeSend: function() {},
            success: function(data){
                if (data === "") {
                    alert("A hozzászólás elküldéséhez kérem jelentkezzen be!");
                }
                else {
                    $("#comments").html(data);
                    $("[name=comment]").val("");
                    $("button[type=submit]").removeAttr("disabled");
                }
            }
        });

        return false;
    });

    $(document).on("click", ".btn-delete-comment", function () {
        if (confirm("Biztos, hogy törli?")) {
            let id = parseInt($(this).data("id"));
            let productId = parseInt($(this).data("productid"));
            let obj = $(this);

            $.ajax({
                url: '/hozzaszolas/delete/' + id,
                type: 'POST',
                data: {
                    productId: productId
                },
                dataType: 'text',
                beforeSend: function () {
                },
                success: function (data) {
                    if (data.status === "error") {
                        alert("A hozzászólás törléséhez kérem jelentkezzen be!");
                    }
                    else {
                        $("#comments").html(data);
                    }
                }
            });
        }
        else {
            return false;
        }
    });

});