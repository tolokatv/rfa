/*!
* Start Rfa -
*
*
*/
//
// Scripts
// https://rfa.toloka.media

let intervalId;
let curstatus;

window.addEventListener('DOMContentLoaded', event => {

//function setkey

async function getStatus() {
    let siteurl = "https://rfa.toloka.media/api/1.0/ps/"+stationId;
//    console.log(siteurl);
  const response = await fetch(siteurl);
  const data = await response.json();
//  console.log(data);

    console.log(data);
    stationstate = data["status"];
    myspan = document.getElementById('spanstationstate');
    console.log(stationstate);

    switch (stationstate) {
        case 6:
            myspan.textContent = 'Всі сервіси працюють нормально.';
            myspan.setAttribute("style", "color:green;");
            break;
        case 0:
            myspan.textContent = 'Станція вимкнена';
            myspan.setAttribute("style", "color:red;");
            break;
        default:
            myspan.textContent = 'Станція в процесі запуску або зупинки';
            myspan.setAttribute("style", "color:orange;");
            break;
    }
  return data;
}

intervalId = setInterval(getStatus, 500);

});