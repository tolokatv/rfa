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
  const response = await fetch("https://rfa.toloka.media/api/1.0/ps/1352"+stationId);
  const data = await response.json();
  console.log(data);
  console.log(stationId);
  return data;
}

intervalId = setInterval(getStatus, 3000);

});