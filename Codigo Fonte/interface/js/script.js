//Variaveis Globais
var serverAddress = "localhost";
var port = 4000;

//Listeners ----------------------------------------------------------------------------
document.addEventListener("wheel", () => {}, {
  passive: true
});
document.addEventListener("mousewheel", () => {}, {
  passive: true
});
document.addEventListener("touchstart", () => {}, {
  passive: true
});
document.addEventListener("touchmove", () => {}, {
  passive: true
});

//Funções -------------------------------------------------------------------------------
function sendRequest(path, dados, err) {
  var xmlhttp = new XMLHttpRequest();
  console.log(dados);

  return new Promise((resolve, reject) => {
    xmlhttp.onreadystatechange = e => {
      if (xmlhttp.readyState !== 4) {
        return;
      }
      if (xmlhttp.status >= 200) {
        console.log(JSON.parse(xmlhttp.responseText));
        resolve(JSON.parse(xmlhttp.responseText));
      } else {
        console.warn("request_error");
        err.apply(this);
      }
    };
    xmlhttp.open("POST", serverAddress + path, true);
    xmlhttp.setRequestHeader(
      "Content-Type",
      "application/x-www-form-urlencoded"
    );
    xmlhttp.send(dados);
  });
}

function fazAi(tableId, path, dados, err) {
  sendRequest(path, dados, err).then(res => {
    res.array.forEach(line => {
      $("#table-data").append(`
        <tr>
          <td>${line[0]}</td>
          <td>${line[1]}</td>
          <td>${line[2]}</td>
          <td>${line[3]}</td>
          <td>${line[4]}</td>
          <td>${line[5]}</td>
          <td>${line[6]}</td>
          <td>${line[7]}</td>
          <td>${line[8]}</td>
          <td>${line[9]}</td>
          <td>${line[10]}</td>
        </tr>
      `);
    });
  });
}
