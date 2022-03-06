function filter(search, table_name, tr_name) {
    // Declare variables
    var input, filter, table, tr, a, i;
    input = document.getElementById(search);
    filter = input.value.toUpperCase();
    table = document.getElementById(table_name);
    tr = table.getElementsByClassName(tr_name);

    for (i = 0; i < tr.length; i++) {
        a = tr[i].getElementsByTagName("td")[0];
        if (a.textContent.toString().toUpperCase().indexOf(filter) > -1) {
            tr[i].style.display = "";
        } else {
            tr[i].style.display = "none";
        }
    }

}

function writeCookie(c, d) {
    var a = document.getElementById('url' + c).value;
    var b = document.getElementById(c.toString()).value;
    document.cookie = "location=" +  $(window).scrollTop();
    window.location.href = a + "/" + b + "/" + c + "/" + d;
}

function readCookie() {
    var lastScrollTop = getCookie("location");
        if (lastScrollTop) {
        $(window).scrollTop(lastScrollTop);
        removeCookie('location');
    }
}

function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
        for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
            if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function removeCookie(cname) {
    document.cookie = cname + "=;expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}

function toggleTable(string) {
    let lTable1 = document.getElementById(string);
    for (let i = 1; i < lTable1.rows.length; i++) {
        lTable1.rows[i].style.display = (lTable1.rows[i].style.display === "none") ? "table-row" : "none";
    }
}

function toggleTable(string) {
    let lTable1 = document.getElementById(string);
    for (let i = 1; i < lTable1.rows.length; i++) {
        lTable1.rows[i].style.display = (lTable1.rows[i].style.display === "none") ? "table-row" : "none";
    }
}