function loadGetMsg() {
    let name = document.getElementById("name").value;
    fetch("/greeting?name=" + name)
        .then(response => response.text())
        .then(data => {
            document.getElementById("getrespmsg").innerHTML = data;
        });
}

function loadPostMsg() {
    let nameVar = document.getElementById("postname").value;
    let url = "/hellopost?name=" + nameVar;

    fetch(url, {method: 'POST'})
        .then(x => x.text())
        .then(y => document.getElementById("postrespmsg").innerHTML = y);
}
