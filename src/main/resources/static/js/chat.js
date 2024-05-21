document.addEventListener('DOMContentLoaded', function() {
  var stompClient = null;
  var username = "You"; // 사용자 이름을 실제 사용자 이름으로 설정하세요.

  function connect() {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      console.log('Connected: ' + frame);
      stompClient.subscribe('/topic/public', function (message) {
        showMessage(JSON.parse(message.body));
      });
    });
  }

  function sendMessage() {
    var messageContent = document.getElementById('messageInput').value.trim();
    if (messageContent && stompClient) {
      var chatMessage = {
        sender: username,
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
    if (message.sender === username) {
      messageElement.classList.add('my-message');
    } else {
      messageElement.classList.add('their-message');
    }

    var senderElement = document.createElement('span');
    senderElement.className = 'sender';
    senderElement.innerText = message.sender;

    var textElement = document.createElement('p');
    textElement.innerText = message.content;

    messageElement.appendChild(senderElement);
    messageElement.appendChild(textElement);
    messages.appendChild(messageElement);
    messages.scrollTop = messages.scrollHeight; // Scroll to bottom
  }

  connect();

  // Send button event listener
  document.querySelector('button[onclick="sendMessage()"]').addEventListener('click', function() {
    sendMessage();
  });

  // Enter key event listener
  document.getElementById('messageInput').addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
      sendMessage();
    }
  });

  // QR 코드 표시/숨기기 토글 함수
  function toggleQRCode() {
    const qrCodeContainer = document.getElementById('qrcode-container');
    if (qrCodeContainer.style.display === 'none' || qrCodeContainer.style.display === '') {
      qrCodeContainer.style.display = 'block';
    } else {
      qrCodeContainer.style.display = 'none';
    }
  }

  // QR 코드 버튼 이벤트 리스너
  document.getElementById('showQRButton').addEventListener('click', function() {
    toggleQRCode();
  });

  document.getElementById('hideQRButton').addEventListener('click', function() {
    toggleQRCode();
  });

  // 초기에는 QR 코드를 숨김
  window.onload = function() {
    document.getElementById('qrcode-container').style.display = 'none';
  };
});
