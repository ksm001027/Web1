let stompClient = null;
let roomId = null;

function connect() {
  const socket = new SockJS('/chat');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/public/' + roomId, function (messageOutput) {
      showMessage(JSON.parse(messageOutput.body));
    });
  });
}

function sendMessage() {
  const username = document.getElementById('usernameInput').value.trim();
  const messageContent = document.getElementById('messageInput').value.trim();

  if (messageContent && stompClient) {
    const chatMessage = {
      sender: username,
      content: messageContent,
      type: 'CHAT'
    };
    stompClient.send("/app/sendMessage/" + roomId, {}, JSON.stringify(chatMessage));
  }
}

function showMessage(message) {
  const messageElement = document.createElement('div');
  messageElement.className = 'message';

  if (message.sender === document.getElementById('usernameInput').value.trim()) {
    messageElement.classList.add('my-message');
  } else {
    messageElement.classList.add('their-message');
  }

  const usernameElement = document.createElement('span');
  usernameElement.className = 'username';
  usernameElement.appendChild(document.createTextNode(message.sender));

  const textElement = document.createElement('p');
  textElement.appendChild(document.createTextNode(message.content));

  messageElement.appendChild(usernameElement);
  messageElement.appendChild(textElement);

  document.getElementById('messages').appendChild(messageElement);
}

window.addEventListener('load', function () {
  roomId = window.location.pathname.split('/').pop();
  connect();
});

function toggleQRCode() {
  const qrContainer = document.getElementById('qrcode-container');
  if (qrContainer.style.display === 'none' || qrContainer.style.display === '') {
    const url = window.location.href;
    fetch(`/generate-qr?url=${encodeURIComponent(url)}&purpose=chat`)
      .then(response => response.blob())
      .then(blob => {
        const imageUrl = URL.createObjectURL(blob);
        document.getElementById('qrcode').src = imageUrl;
        document.getElementById('qr-url').innerText = url; // QR 코드 URL을 표시
        qrContainer.style.display = 'block';
      });
  } else {
    qrContainer.style.display = 'none';
  }
}
