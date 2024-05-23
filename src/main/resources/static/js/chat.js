document.addEventListener('DOMContentLoaded', function() {
  var stompClient = null;
  var username = null;

  function connect() {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      console.log('Connected: ' + frame);
      stompClient.subscribe('/topic/public', function (message) {
        showMessage(JSON.parse(message.body));
      });
    }, function(error) {
      console.error('STOMP error: ' + error);
    });
  }

  function sendMessage() {
    if (!username) {
      username = document.getElementById('usernameInput').value.trim();
      if (!username) {
        alert('이름을 입력하세요.');
        return;
      }
      document.getElementById('usernameInput').readOnly = true;
    }

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
      if (!document.getElementById('qrcode').src) {
        fetchQRCode();
      }
      qrCodeContainer.style.display = 'block';
    } else {
      qrCodeContainer.style.display = 'none';
    }
  }

  // QR 코드 생성 및 표시 함수
  function fetchQRCode() {
    const currentUrl = window.location.href;
    fetch(`/generate-qr?url=${encodeURIComponent(currentUrl)}`)
      .then(response => response.blob())
      .then(blob => {
        const url = URL.createObjectURL(blob);
        document.getElementById('qrcode').src = url;
      })
      .catch(error => console.error('Error fetching QR code:', error));
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
