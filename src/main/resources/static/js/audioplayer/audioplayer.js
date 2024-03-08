import lottieWeb from "https://cdn.skypack.dev/lottie-web";

class AudioPlayer extends HTMLElement {
  constructor() {
    super();
    const template = document.querySelector("template");
    const templateContent = template.content;
    const shadow = this.attachShadow({ mode: "open" });
    shadow.appendChild(templateContent.cloneNode(true));
  }

  connectedCallback() {
    everything(this);
  }
}

const everything = function (element) {
  const shadow = element.shadowRoot;
  const audioPlayerContainer = shadow.getElementById("audio-player-container");
  const playIconContainer = shadow.getElementById("play-icon");
  const muteIconContainer = shadow.getElementById("mute-icon");
  const buttonUkraine = shadow.getElementById("toloka-ukraine");
  const buttonRetro = shadow.getElementById("toloka-retro");
  const buttonRadio22 = shadow.getElementById("toloka-radio22");
  let currentStation = "toloka-ukraine";
  const mycontainer = shadow.getElementById("name-station");

  const audio = shadow.querySelector("audio");
  audio.src = "https://ukraine.rfa.toloka.media:8443/ukraine";
  let playState = "play";
  let muteState = "unmute";
  let raf = null;
  let cnt = 0;
  //let rsp =

  async function getCurentTrack() {
    try {
      //let res = await fetch("https://ukraine.rfa.toloka.media/api/live-info");
      //let res = await fetch("https://api.github.com/users/gulshansainis");
      //console.log(res);
      //let jsonResult = await res.json();
      cnt++;
      //let response = new Response();

      console.log("---- 1 ---- " + cnt);
      //let myurl = "https://api.github.com/users/gulshansainis";
      let myurl = "http://copyleft.net.ua/toloka/media.json";
      //let myurl = "https://ukraine.rfa.toloka.media/api/live-info/";
      await fetch(myurl, {
        Accept: "application/json",
        "Content-Type": "application/json",
        "User-Agent":
          "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/118.0"
      })
        .then((response) => {
          console.log("---- 2 ---- " + cnt);
          console.log("response.status = ", response.status);
          console.log("response URL = ", response.url);
          //return response.json();
          console.log("---- 2 OK ---- " + response.ok); // => false
          console.log("---- 2 STATUS ---- " + response.status); // => 404
          return response.text();
          //return response.headers();
        })
        .then((data) => {
          console.log("---- 3 ---- " + cnt);
          //console.log(data);
        });
      //      console.log(jsonResult);
    } catch (error) {
      console.log("---- 3 ----");
      console.log(error);
    }
    console.log("---- 4 ---- " + cnt);
    //console.log("response.status = ", response.status);
    //console.log("response = ", response.blob);
    //console.log("response.status = ", response.text);
    console.log("response = url ", response.url);
  }
  let intervalId = window.setInterval(getCurentTrack, 5000);

  const playAnimation = lottieWeb.loadAnimation({
    container: playIconContainer,
    path:
      "https://maxst.icons8.com/vue-static/landings/animated-icons/icons/pause/pause.json",
    renderer: "svg",
    loop: false,
    autoplay: false,
    name: "Play Animation"
  });

  const muteAnimation = lottieWeb.loadAnimation({
    container: muteIconContainer,
    path:
      "https://maxst.icons8.com/vue-static/landings/animated-icons/icons/mute/mute.json",
    renderer: "svg",
    loop: false,
    autoplay: false,
    name: "Mute Animation"
  });

  // запалюємо кнопку
  const buttonON = function (button) {
    shadow.getElementById(button).style.color = "black";
    shadow.getElementById(button).style.backgroundColor = "#ff8000";
    shadow.getElementById(button).style.fontSize = "15px";
    shadow.getElementById(button).style.fontWeight = "bold";
    shadow.getElementById(button).style.border = "2px solid white";
  };
  // гасимо кнопку
  const buttonOFF = function (button) {
    shadow.getElementById(button).style.color = "#ff8000";
    shadow.getElementById(button).style.backgroundColor = "black";
    shadow.getElementById(button).style.fontSize = "12px";
    shadow.getElementById(button).style.fontWeight = "normal";
    shadow.getElementById(button).style.border = "2px solid #ff8000";
  };

  // змінюємо стан плеєра
  const changePlayState = function () {
    if (playState === "play") {
      audio.play();
      playAnimation.playSegments([14, 27], true);
      playState = "pause";
    } else {
      audio.pause();
      playAnimation.playSegments([0, 14], true);
      cancelAnimationFrame(raf);
      playState = "play";
    }
  };

  playAnimation.goToAndStop(14, true);

  mycontainer.addEventListener("click", (event) => {
    if (event.target.id === currentStation) {
      changePlayState();
    } else {
      // дивимося, в якому стані плеєр. Якщо грає, то зупиняємо
      if (playState === "pause") {
        changePlayState();
      }
      // гасимо поточну кнопку
      buttonOFF(currentStation);
      // Змінюємо поточний УРЛ для відтворення та поточну станцію.
      switch (event.target.id) {
        case "toloka-radio22":
          audio.src = "https://ua22.rfa.toloka.media:8443/ua22";
          break;
        case "toloka-ukraine":
          audio.src = "https://ukraine.rfa.toloka.media:8443/ukraine";
          break;
        case "toloka-retro":
          audio.src = "https://retro.rfa.toloka.media:8443/retro";
          break;
        default:
          console.log("Sorry, we are out of " + "======" + ".");
      }
      currentStation = event.target.id;
      changePlayState();
      currentStation = event.target.id;
      buttonON(currentStation);
    }
  });

  playIconContainer.addEventListener("click", (event) => {
    changePlayState();
  });

  muteIconContainer.addEventListener("click", () => {
    if (muteState === "unmute") {
      muteAnimation.playSegments([0, 15], true);
      audio.muted = true;
      muteState = "mute";
    } else {
      muteAnimation.playSegments([15, 25], true);
      audio.muted = false;
      muteState = "unmute";
    }
  });

  function onclicktrack(event) {
    if (muteState === "unmute") {
      // мутимо плеєр. Кнопку плеєра беремо по ID елементу
        muteIconContainer = document.getElementById("mutekey"); // кнопка муте знаходиться десь на сторінці.
      muteAnimation.playSegments([0, 15], true);
      audio.muted = true;
      muteState = "mute";
    } else {
      muteAnimation.playSegments([15, 25], true);
      audio.muted = false;
      muteState = "unmute";
    }

  }
};

customElements.define("audio-player", AudioPlayer);
