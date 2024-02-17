/*!
* Start Rfa -
*
*
*/
//
// Scripts
// https://rfa.toloka.media

var toloka = {};

    function setcover ( alcoid, albumid, cdid ) {
        setnewcover.alcoid = alcoid;
        getStatus(alcoid, albumid, cdid);
}

async function getStatus(alcoid, albumid, cdid) {
    let siteurl = "https://rfa.toloka.media/api/1.0/setalbumcover/"+alcoid+'/'+albumid+"/"+cdid
    const response = await fetch(siteurl);
    const data = await response.json();
    stationstate = data["status"];

}


window.addEventListener('DOMContentLoaded', event => {

console.log("стартували");

});