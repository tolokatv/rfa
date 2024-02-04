/*!
* Start Rfa -
*
*
*/
//
// Scripts
// https://rfa.toloka.media

let intervalId;

window.addEventListener('DOMContentLoaded', event => {

async function getStatus() {
    let siteurl = "https://rfa.toloka.media/api/1.0/ps/"+stationId;
//    console.log(siteurl);
  const response = await fetch(siteurl);
  const data = await response.json();
  console.log(data);
//  console.log(stationId);
  return data;
}

intervalId = setInterval(getStatus, 3000);

});