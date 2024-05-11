document.addEventListener('DOMContentLoaded', function () {
  handleQuestionTypeChange(); // 페이지 로드 시 초기 상태 설정
});

function handleQuestionTypeChange() {
  const questionType = document.getElementById('questionType').value;
  const optionContainer = document.getElementById('option-container');
  const subjectiveAnswer = document.getElementById('subjective-answer');

  if (questionType === 'objective') {
    optionContainer.style.display = 'block';
    subjectiveAnswer.style.display = 'none';
  } else {
    optionContainer.style.display = 'none';
    subjectiveAnswer.style.display = 'block';
  }
}
function addOption() {
  const container = document.getElementById('option-container');
  const answerOptions = document.getElementById('answer-options');

  // 컨테이너 내 옵션 입력 필드의 개수를 카운트
  const optionInputs = container.querySelectorAll('input[type="text"]');
  const optionCount = optionInputs.length + 1; // 새 옵션 번호

  const optionDiv = document.createElement('div');
  optionDiv.className = 'option-entry';

  const newOption = document.createElement('input');
  newOption.type = 'text';
  newOption.name = 'options[]';
  newOption.placeholder = '문제 ' + optionCount + ' 입력';
  newOption.required = true;

  const radioOption = document.createElement('input');
  radioOption.type = 'radio';
  radioOption.name = 'correctAnswer';
  radioOption.value = 'option' + optionCount;

  const label = document.createElement('label');
  label.textContent = ' 이 문제가 정답';
  label.prepend(radioOption);

  optionDiv.appendChild(newOption);
  optionDiv.appendChild(label);
  container.appendChild(optionDiv);

  // 정답 선택지를 위해 라디오 버튼 복제 및 정답 선택 영역에 추가
  const answerLabel = document.createElement('label');
  const clonedRadioOption = radioOption.cloneNode(true); // 라디오 버튼 복제
  answerLabel.textContent = ' 문제 ' + optionCount; // 정답 레이블에도 옵션 번호 지정
  answerLabel.prepend(clonedRadioOption);
  answerOptions.appendChild(answerLabel);
}



function submitQuiz(event) {
  event.preventDefault();
  const quizName = document.getElementById('quizName').value;
  const questionType = document.getElementById('questionType').value;
  const question = document.getElementById('question').value;
  let options = [];
  let correctAnswer;

  if (questionType === 'objective') {
    const optionElements = document.querySelectorAll('#option-container input[type="text"]');
    options = Array.from(optionElements).map(option => option.value);
    const correctOptionIndex = document.querySelector('input[name="correctAnswer"]:checked').value;
    correctAnswer = options[correctOptionIndex - 1];
  } else {
    correctAnswer = document.getElementById('subjectiveAnswer').value;
  }

  const quizData = {
    quizName: quizName,
    questions: [{
      text: question,
      type: questionType,
      options: options,
      correctAnswer: correctAnswer
    }]
  };

  fetch('http://localhost:8080/api/quiz', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(quizData)
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then(data => {
      if (data.quizId) {
        window.location.href = '/question.html'; // 퀴즈 페이지로 리다이렉트
      } else {
        console.error('Quiz registration failed:', data.message);
        document.getElementById('displayAnswer').textContent = '퀴즈 등록 실패';
        document.getElementById('submittedAnswer').style.display = 'block';
      }
    })
    .catch(error => {
      console.error('Error submitting quiz:', error);
      document.getElementById('displayAnswer').textContent = '퀴즈 등록 중 오류 발생: ' + error;
      document.getElementById('submittedAnswer').style.display = 'block';
    });
}

