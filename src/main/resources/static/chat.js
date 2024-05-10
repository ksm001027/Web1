var stompClient = null;

function connect() {
  var socket = new SockJS('/chat');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/public', function (message) {
      showMessage(JSON.parse(message.body).content);
    });
  });
}

function sendMessage() {
  var messageContent = document.getElementById('messageInput').value.trim();
  if (messageContent && stompClient) {
    var chatMessage = {
      content: messageContent,
      type: 'CHAT'
    };
    stompClient.send("/app/sendMessage", {}, JSON.stringify(chatMessage));
    document.getElementById('messageInput').value = '';
  }
}

function showMessage(message) {
  var messages = document.getElementById('messages');
  var messageElement = document.createElement('div');
  messageElement.className = 'message';
  messageElement.innerText = message;
  messages.appendChild(messageElement);
  messages.scrollTop = messages.scrollHeight; // Scroll to bottom
}

connect();
