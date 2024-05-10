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



function submitQuiz(event) {
  event.preventDefault(); // 폼의 기본 제출 막기
  const questionType = document.getElementById('questionType').value;
  const question = document.getElementById('question').value;
  let answer;

  if (questionType === 'objective') {
    // 객관식 정답 처리 로직
  } else {
    answer = document.getElementById('subjectiveAnswer').value;
  }

  // 제출된 답변을 표시
  document.getElementById('displayAnswer').textContent = `질문: ${question}, 답변: ${answer}`;
  document.getElementById('submittedAnswer').style.display = 'block';
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
