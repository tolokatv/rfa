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
//  console.log(stationId);

    console.log(data);
    stationstate = data["status"];
    myspan = document.getElementById('spanstationstate');
    console.log(stationstate);
    if (stationstate === 6) {
        myspan.value == 'Всі сервіси працюють нормально.';
        myspan.setAttribute("style", "color:green;");
    } else if (stationstate === 6) {
        myspan.value == 'Не всі сервіси працюють нормально.';
        myspan.setAttribute("style", "color:orange;");
    } else {
        myspan.value == 'Станція вимкнена';
        myspan.setAttribute("style", "color:red;");
    }
  return data;
}

intervalId = setInterval(getStatus, 500);

});