'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
//var connectingElement = document.querySelector('');
var connectingElement = document.getElementById('connectstatus');
var stompClient = null;
var username = null;
var clientuuid = null;
var toclientname = null;
var subcurroom = null;
var subprivate = null;
var subpublic = null
var subhello = null;
var subusers = null;
var subroom = null;




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

    subcurroom = stompClient.subscribe('/topic/'+curroom, onPublicMessageReceived);

    subpublic = stompClient.subscribe('/public/'+curuuid, onPublicMessageReceived);

    subprivate = stompClient.subscribe('/private/'+curuuid, onPrivateMessageReceived);

    subusers = stompClient.subscribe('/userslist/'+curuuid, onUserList);

    subroom = stompClient.subscribe('/roomslist/'+curuuid, onRoomList);


    stompClient.send("/app/hello",
        {},
        JSON.stringify({
        uuid: curuuid,
        roomuuid: curroom,
        send: Date.now()
        })
    );
    getuserlist();
    getroomlist();

    ligthOnRoom(curroom);
}

function getuserlist() {
   stompClient.send("/app/userslist",
        {},
        JSON.stringify({
        uuid: curuuid,
        send: Date.now()
        })
    );
 }

function getroomlist() {
    stompClient.send("/app/roomslist",
        {},
        JSON.stringify({
        uuid: curuuid,
        roomuuid: curroom,
        send: Date.now()
        })
    );
}

function onUserList(payload) {
// userlist
//    console.log("==== onUserList ==========================================");
    let jbody = JSON.parse(payload.body);
    // clear userlist
    document.getElementById('userlist').innerHTML = '';
    // перелік користувачів передається в body повідомлення - масив Маp???
    // тепер цикл по jbody.body
    let bodyuserslist = JSON.parse(jbody.body);
    bodyuserslist.forEach(function(elem, ind) {
        // elem to json
//        console.log(elem);
        let ejson = JSON.parse(elem);
        // create span with name
        let spanname = document.createElement('span');
        spanname.textContent = ejson.name;
        ejson.uuid.includes(curusername) ? spanname.style.color = 'red' : spanname.style.color = 'black';

        spanname.onclick= function () {selectuserFromPublic(ejson.uuid, ejson.name)};
        // create div with id = userUuid from
        let iDiv = document.createElement('div');
        iDiv.id =  ejson.uuid;
        //iDiv.onclick= function () {selectuser(ejson.uuid)};

        // add span to div
        iDiv.appendChild(spanname);
        // add user div to userList
        document.getElementById('userlist').appendChild(iDiv);
    });
}

function onRoomList(payload) {
// roomlist
//    console.log("==== onRoomList ==========================================");
    let jbody = JSON.parse(payload.body);
    // clear userlist
    document.getElementById('roomlist').innerHTML = '';
    // перелік кімнат передається в body повідомлення - масив Маp???
    // тепер цикл по jbody.body
    let bodyuserslist = JSON.parse(jbody.body);
    bodyuserslist.forEach(function(elem, ind) {
        // elem to json
//        console.log(elem);
        let ejson = JSON.parse(elem);
        // create span with name
        let spanname = document.createElement('span');
        spanname.textContent = ejson.name;
        ejson.uuid.includes(curroom) ? spanname.style.color = 'red' : spanname.style.color = 'black';


        spanname.onclick= function () {selectroom(ejson.uuid)};
        // create div with id = userUuid from
        let iDiv = document.createElement('div');

        // add span to div
        iDiv.appendChild(spanname);
        // add user div to userList
        document.getElementById('roomlist').appendChild(iDiv);
    });
}

function onPublicMessageReceived(payload) {
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
    if (curuuid.includes(jbody.toname)) {
        spanname.textContent = '> '+jbody.fromname+': ';
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

function unsubscribeRoom(lcurroom) {subcurroom.unsubscribe();}

function subscribeRoom(lcurroom) {
//        console.log("subscribeRoom");
//        console.log("curroom="+lcurroom);
        subcurroom = stompClient.subscribe('/topic/'+lcurroom, onPublicMessageReceived);

//        console.log("curroom="+lcurroom);
        // get content public room
//        console.log("+++ subscribeRoom Enter to room ++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            stompClient.send("/app/hello",{},
                JSON.stringify({
                    uuid: curuuid,
                    roomuuid: curroom,
                    send: Date.now()
                })
            );
//        console.log("=== curroom="+subcurroom);
//        console.log("+++ Enter to room ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

function LigthOffRoom(lcurroom) {
//        document.getElementById(lcurroom).style.color = 'black';
//        myNode.style.color = "#000000";
//        myNode.setAttribute("style","color:black;");
//        console.log("LigthOffRoom");
//        console.log("curroom="+lcurroom);
    }

function ligthOnRoom(lcurroom) {
//        document.getElementById(lcurroom).style.color = 'red';
//        myNode.style.color = "#ff0000";
//        lcurroom.setAttribute("style","color:orange;");
//        console.log("ligthOnRoom");
//        console.log("curroom="+lcurroom);
    }

function selectroom(toroom) {
// відписуємося від кімнати
    unsubscribeRoom(curroom);
    LigthOffRoom(curroom);
// clear public
    var myNode = document.getElementById("publicarea");
    myNode.innerHTML = '';

// підписуємося на кімнату
//    console.log('////////// before change room: '+curroom);
    curroom = toroom
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
//    console.log(toclientname);
    document.getElementById('connectstatus').innerHTML = 'Кому:'+spanname.innerText;
}

function onError(){
    console.log("Щось пішло не так при коннекті :( ");
}

window.addEventListener('DOMContentLoaded', event => {
connect();

var intervaluser = setInterval(getuserlist, 10000);
var intervalroom = setInterval(getroomlist, 10000);
})