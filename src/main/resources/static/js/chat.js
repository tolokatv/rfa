'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
//var connectingElement = document.querySelector('');
var connectingElement = document.getElementById('connectstatus');


var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];
function connect() {
    //username = document.querySelector('#name').value.trim();
    var socket = new SockJS('/rfachat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    console.log("subscribe curroom="+curroom);
    subcurroom = stompClient.subscribe('/topic/'+curroom, onPublicMessageReceived);

    console.log("========= "+'/hello/'+curuuid);
    subhello = stompClient.subscribe('/hello/'+curuuid, onPublicMessageReceived);

    subpriv = stompClient.subscribe('/topic/'+curuuid, onPrivateMessageReceived);

    stompClient.send("/app/hello",
        {},
        JSON.stringify({
        uuid: curuuid,
        roomuuid: curroom,
        send: Date.now()
        })
    );
    ligthOnRoom(curroom);
}


function onError(error) {
    console.log(error);
    console.log("Помилка");
}

function onPublicMessageReceived(payload) {
    console.log("==== PUBLIC ==========================================");
    var jbody = JSON.parse(payload.body);

    var spanname = document.createElement('span');
    spanname.textContent = jbody.fromname+': ';

    var spanbody = document.createElement('span');
    spanbody.textContent = jbody.body;

    var iDiv = document.createElement('div');
        iDiv.id = jbody.fromuuid;
        iDiv.onclick= function () {selectuserFromPublic(jbody.fromuuid, jbody.fromname)};

    iDiv.appendChild(spanname);
    iDiv.appendChild(spanbody);

    document.getElementById('publicarea').appendChild(iDiv);

    var objDiv = document.getElementById("publicarea");
    objDiv.scrollTop = objDiv.scrollHeight;
}

function onPrivateMessageReceived(payload) {
    var jbody = JSON.parse(payload.body);

    var spanname = document.createElement('span');
    if (curuuid.includes(jbody.fromuuid)) {
        spanname.textContent = '> '+jbody.toname+': ';
    } else {
        spanname.textContent = jbody.fromname+': ';
    }

    var spanbody = document.createElement('span');
    spanbody.textContent = jbody.body;

    var iDiv = document.createElement('div');
    iDiv.id = jbody.fromuuid;
    iDiv.onclick= function () {selectuserFromPublic(jbody.fromuuid, jbody.fromname)};
//    iDiv.setAttribute("onclick", "selectuser(this)");

    iDiv.appendChild(spanname);
    iDiv.appendChild(spanbody);
    document.getElementById('privatearea').appendChild(iDiv);

    var objDiv = document.getElementById("privatearea");
    objDiv.scrollTop = objDiv.scrollHeight;
//    console.log("==== PRIVATE ==========================================");
}

function unsubscribeRoom(lcurroom) {
        console.log("unsubscribeRoom");
        console.log("curroom="+lcurroom);
        // subcuroom
        subcurroom.unsubscribe();


    }
function subscribeRoom(lcurroom) {
        console.log("subscribeRoom");
        console.log("curroom="+lcurroom);
        subcurroom = stompClient.subscribe('/topic/'+lcurroom, onPublicMessageReceived);

        console.log("curroom="+lcurroom);
        // get content public room
        console.log("+++ Enter to room ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            stompClient.send("/app/hello",{},
                JSON.stringify({
                    uuid: curuuid,
                    roomuuid: curroom,
                    send: Date.now()
                })
            );
        console.log("=== curroom="+subcurroom);
        console.log("+++ Enter to room ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }
function LigthOffRoom(lcurroom) {
        document.getElementById(lcurroom).style.color = 'black';
//        myNode.style.color = "#000000";
//        myNode.setAttribute("style","color:black;");
//        console.log("LigthOffRoom");
//        console.log("curroom="+lcurroom);
    }

function ligthOnRoom(lcurroom) {
        document.getElementById(lcurroom).style.color = 'red';
//        myNode.style.color = "#ff0000";
//        lcurroom.setAttribute("style","color:orange;");
//        console.log("ligthOnRoom");
//        console.log("curroom="+lcurroom);
    }

function selectroom(event) {
// відписуємося від кімнати
    unsubscribeRoom(curroom);
    LigthOffRoom(curroom);
// clear public
    var myNode = document.getElementById("publicarea");
    myNode.innerHTML = '';

// підписуємося на кімнату
//    console.log('////////// before change room: '+curroom);
    curroom = event.id;
//    console.log('////////// after change room: '+curroom);
    subscribeRoom(curroom);
    ligthOnRoom(curroom);
}

function sendpublic(event) {
//myinput = document.getElementById('newmessage');

    stompClient.send("/app/topic",
        {},
        JSON.stringify({
        uuid: curuuid,
        send: Date.now(),
        fromname: curusername,
        fromuuid: curuuid,
        body: document.getElementById('newmessage').value,
        roomuuid: curroom
        })
    );
    document.getElementById('newmessage').value = "";
}

function sendprivate(event) {
//myinput = document.getElementById('newmessage');

    stompClient.send("/app/private",
        {},
        JSON.stringify({uuid: curuuid,
        send: Date.now(),
        fromname: curusername,
        fromuuid: curuuid,
        toname: toclientname,
        touuid: clientuuid,
        body: document.getElementById('newmessage').value
        })
    );
    document.getElementById('newmessage').value = "";
}

function selectuserFromPublic(luuid, lname) {
    clientuuid = luuid;
    toclientname = lname;
    document.getElementById('connectstatus').innerHTML = lname;
}

function selectuser(event ) {
    var spanname = document.getElementById(event.id);
    clientuuid = spanname.id;
    toclientname = spanname.innerText;
    console.log(toclientname);
    document.getElementById('connectstatus').innerHTML = 'Кому:'+spanname.innerText;
}

window.addEventListener('DOMContentLoaded', event => {
connect();
})