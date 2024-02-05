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
var toloka = {};

window.addEventListener('DOMContentLoaded', event => {

function getkeyhrev () {
    toloka.blink = document.getElementById('toStation');
    toloka.bstart = document.getElementById('startStation');
    toloka.bstop = document.getElementById('stopStation');
    console.log(toloka);

    toloka.hreflink     = toloka.blink.href;
    toloka.hrefstart    = toloka.bstart.href;
    toloka.hrestop      = toloka.bstop.href;
    console.log(toloka);
}



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
    console.log(toloka);

    switch (stationstate) {
        case '6':
            myspan.textContent = 'Всі сервіси працюють нормально.';
            myspan.setAttribute("style", "color:green;");
            break;
        case '0':
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


getkeyhrev();

intervalId = setInterval(getStatus, 3000);


});