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

//function connect_old(event) {
//    username = document.querySelector('#name').value.trim();
//
//    if(username) {
//        usernamePage.classList.add('hidden');
//        chatPage.classList.remove('hidden');
//
//        var socket = new SockJS('/ws');
//        stompClient = Stomp.over(socket);
//
//        stompClient.connect({}, onConnected, onError);
//    }
//    event.preventDefault();
//}


//function onConnected_old() {
//    // Subscribe to the Public Topic
//    stompClient.subscribe('/topic/public', onMessageReceived);
//
//    // Tell your username to the server
//    stompClient.send("/app/chat.addUser",
//        {},
//        JSON.stringify({sender: username, type: 'JOIN'})
//    )
//
//    connectingElement.classList.add('hidden');
//}


//function onError(error) {
//    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
//    connectingElement.style.color = 'red';
//}


//function sendMessage(event) {
//    var messageContent = messageInput.value.trim();
//    if(messageContent && stompClient) {
//        var chatMessage = {
//            sender: username,
//            content: messageInput.value,
//            type: 'CHAT'
//        };
//        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
//        messageInput.value = '';
//    }
//    event.preventDefault();
//}


//function onMessageReceived(payload) {
//    var message = JSON.parse(payload.body);
//
//    var messageElement = document.createElement('li');
//
//    if(message.type === 'JOIN') {
//        messageElement.classList.add('event-message');
//        message.content = message.sender + ' joined!';
//    } else if (message.type === 'LEAVE') {
//        messageElement.classList.add('event-message');
//        message.content = message.sender + ' left!';
//    } else {
//        messageElement.classList.add('chat-message');
//
//        var avatarElement = document.createElement('i');
//        var avatarText = document.createTextNode(message.sender[0]);
//        avatarElement.appendChild(avatarText);
//        avatarElement.style['background-color'] = getAvatarColor(message.sender);
//
//        messageElement.appendChild(avatarElement);
//
//        var usernameElement = document.createElement('span');
//        var usernameText = document.createTextNode(message.sender);
//        usernameElement.appendChild(usernameText);
//        messageElement.appendChild(usernameElement);
//    }
//
//    var textElement = document.createElement('p');
//    var messageText = document.createTextNode(message.content);
//    textElement.appendChild(messageText);
//
//    messageElement.appendChild(textElement);
//
//    messageArea.appendChild(messageElement);
//    messageArea.scrollTop = messageArea.scrollHeight;
//}


//function getAvatarColor(messageSender) {
//    var hash = 0;
//    for (var i = 0; i < messageSender.length; i++) {
//        hash = 31 * hash + messageSender.charCodeAt(i);
//    }
//    var index = Math.abs(hash % colors.length);
//    return colors[index];
//}

//usernameForm.addEventListener('submit', connect, true)
//messageForm.addEventListener('submit', sendMessage, true)

//function connect(event) {
function connect() {
    //username = document.querySelector('#name').value.trim();
    var socket = new SockJS('/rfachat');
    stompClient = Stomp.over(socket);
//    const stompClient = new StompJs.Client({
//        brokerURL: 'ws://localhost:8080/rfachat'
//    });
    stompClient.connect({}, onConnected, onError);
}
//    if(username) {
//        usernamePage.classList.add('hidden');
//        chatPage.classList.remove('hidden');
//
//        var socket = new SockJS('/ws');
//        stompClient = Stomp.over(socket);
//
//        stompClient.connect({}, onConnected, onError);
//    }
//    event.preventDefault();
//}

function onConnected() {
    // Subscribe to the Public Topic
//    stompClient.subscribe('/topic/public', onMessageReceived);
    console.log("subscribe curroom="+curroom);
    subcuroom = stompClient.subscribe('/topic/'+curroom, onPublicMessageReceived);
    subpriv = stompClient.subscribe('/topic/'+curuuid, onPrivateMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/hello",
        {},
        JSON.stringify({uuid: curuuid,
        roomuuid: curroom,
        send: Date.now()
//        fromname: ' ===fromname===',
//        fromuuid: ' === fromuuid ===',
//        toname: ' === toname ===',
//        touuid: ' === touuid ===',
//        body: ' === BODY ==='
//        to: ' === to ===',
//        from: ' === from ==='
        })
    )

//    connectingElement.classList.add('hidden');
}


function onError(error) {
    console.log(error);
    console.log("Помилка");
//    connectingElement.innerHTML = 'Could not connect to WebSocket server. Please refresh this page to try again!';
//    connectingElement.style.color = 'red';
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

//    document.getElementById("publicarea").scrollHeight;

//    iDiv.className = 'block';
//    var div2 = document.createElement('div');
//    div2.className = 'block-2';
//    iDiv.appendChild(div2);
//    console.log("=== PUBLIC ===========================================");
}
function onPrivateMessageReceived(payload) {
    var jbody = JSON.parse(payload.body);
    var spanname = document.createElement('span');
    spanname.textContent = jbody.fromname+': ';
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
    console.log("==== PRIVATE ==========================================");
}

function unsubscribeRoom(lcurroom) {
        console.log("unsubscribeRoom");
        console.log("curroom="+lcurroom);
    }
function subscribeRoom(lcurroom) {
        console.log("subscribeRoom");
        console.log("curroom="+lcurroom);
    }
function LigthOffRoom(lcurroom) {
        console.log("LigthOffRoom");
        console.log("curroom="+lcurroom);
    }

function ligthOnRoom(lcurroom) {
        console.log("ligthOnRoom");
        console.log("curroom="+lcurroom);
    }

function selectroom(event) {
// відписуємося від кімнати
    unsubscribeRoom(curroom);
    LigthOffRoom(curroom);

// підписуємося на кімнату
    curroom = event.id;
    subscribeRoom(curroom);
    ligthOnRoom(curroom);
}

function sendmessage(event) {
//myinput = document.getElementById('newmessage');

    stompClient.send("/app/topic",
        {},
        JSON.stringify({
        send: Date.now(),
        fromname: curusername,
        fromuuid: curuuid,
        body: document.getElementById('newmessage').value,
        roomuuid: curroom
//        to: ' === to ===',
//        from: ' === from ==='
        })
    );
    document.getElementById('newmessage').value = "";

}

function sendprivate(event) {
//myinput = document.getElementById('newmessage');

    stompClient.send("/app/hello",
        {},
        JSON.stringify({uuid: curuuid,
        send: Date.now(),
        fromname: curusername,
        fromuuid: curuuid,
        toname: toclientname,
        touuid: clientuuid,
        body: document.getElementById('newmessage').value
//        to: ' === to ===',
//        from: ' === from ==='
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
//    console.log("=== onclick user ===========================================");
    var spanname = document.getElementById(event.id);
    clientuuid = spanname.id;
    toclientname = spanname.innerText;
    console.log(toclientname);
    document.getElementById('connectstatus').innerHTML = spanname.innerText;
//    connectingElement.innerHTML = toclient;
}

window.addEventListener('DOMContentLoaded', event => {
//httpNodeCors: {
//  origin: "*",
//  methods: "GET,PUT,POST,DELETE"
//}
connect();
})