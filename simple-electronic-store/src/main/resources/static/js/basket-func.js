function deleteProductFromBasket(productId, basketId) {
    if ((+document.getElementById(productId + '_count').innerHTML) > 0 ) {
        document.getElementById(productId + '_count').innerHTML = (+document.getElementById(productId + '_count').innerHTML) - (+1);
        var xhr = new XMLHttpRequest();
        xhr.open("PUT", "/basket/delete/" + basketId, true);
        xhr.onload = function () {
            window.location.reload();
        }
        xhr.send();
    }
}

function addProductToBasket(productId) {
    document.getElementById(productId + '_count').innerHTML = (+document.getElementById(productId + '_count').innerHTML) + (+1);
    var xhr = new XMLHttpRequest();
    xhr.open("PUT", "/basket/add/" + productId, true);
    xhr.onload = function () {
        window.location.reload();
    }
    xhr.send();
}

 function clearBasket(productId, basketId) {
    if ((+document.getElementById(productId + '_count').innerHTML) > 0 ) {
        document.getElementById(productId + '_count').innerHTML = 0;
        var xhr = new XMLHttpRequest();
        xhr.open("PUT", "/basket/clear/" + basketId, true);
        xhr.onload = function () {
            window.location.reload();
        }
        xhr.send();
    }
}

function placeAnOrder() {
    var xhr = new XMLHttpRequest();
    xhr.open("PUT", "/order/place", true);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.onload = function () {
        if (xhr.status == 200) {
            alert("ВАШ ЗАКАЗ УСПЕШНО ОФОРМЛЕН");
            window.location.replace(window.location.origin);
        } else {
            alert("При оформлении заказа что-то пошло не так");
        }
    }
    xhr.send(JSON.stringify({ "orderAmount": document.getElementById("current-order-sum").innerHTML }));
}

function checkPaymentAvailable() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/balance", true);
    xhr.onload = function () {
        if (parseFloat(xhr.response) < parseFloat(document.getElementById("current-order-sum").innerHTML)) {
            document.getElementById("place-an-order-button").disabled = true;
            document.getElementById("place-an-order-button").title = 'Недостаточно средст на вашем счету';
        }
    }
    xhr.send();
}