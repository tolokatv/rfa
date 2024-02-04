/*!
* Start Rfa -
*
*
*/
//
// Scripts
//

let intervalId;

window.addEventListener('DOMContentLoaded', event => {

async function getStatus() {
  const response = await fetch("https://rfa.toloka.media/api/1.0/ps/1352");
  const data = await response.json();
  console.log(data);
  return data;
}

intervalId = setInterval(getStatus, 500);

});