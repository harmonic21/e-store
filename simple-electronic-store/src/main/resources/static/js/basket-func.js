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
    xhr.onload = function () {
        alert("ВАШ ЗАКАЗ УСПЕШНО ОФОРМЛЕН");
        window.location.replace(window.location.origin);
    }
    xhr.send();
}