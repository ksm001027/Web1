let stompClient = null;

function connect() {
  const socket = new SockJS('/chat');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/public', function (messageOutput) {
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
    stompClient.send("/app/sendMessage", {}, JSON.stringify(chatMessage));
  }
}

function showMessage(message) {
  const messageElement = document.createElement('div');
  messageElement.className = 'message';

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
  connect();
});

function toggleQRCode() {
  const qrContainer = document.getElementById('qrcode-container');
  if (qrContainer.style.display === 'none' || qrContainer.style.display === '') {
    const url = window.location.href;
    fetch(`/generate-qr?url=${encodeURIComponent(url)}`)
      .then(response => response.blob())
      .then(blob => {
        const url = URL.createObjectURL(blob);
        document.getElementById('qrcode').src = url;
        qrContainer.style.display = 'block';
      });
  } else {
    qrContainer.style.display = 'none';
  }
}
