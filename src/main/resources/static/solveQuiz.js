// 예시 데이터
const quizData = {
  question: "다음 중 프로세스에 대한 설명으로 틀린 것은?",
  options: ["1. 비동기적이다", "2. 상태가 존재한다.", "3. 실행중인 프로그램", "4."],
  correctAnswer: 1  // 옵션 인덱스는 0부터 시작
};

window.onload = function() {
  document.getElementById('question').textContent = quizData.question;
  const optionsList = document.getElementById('optionsList');
  quizData.options.forEach((option, index) => {
    let li = document.createElement('li');
    li.textContent = option;
    li.onclick = function() { checkAnswer(index); };
    optionsList.appendChild(li);
  });
};

function checkAnswer(selectedIndex) {
  const resultText = document.getElementById('resultText');
  const resultContainer = document.getElementById('resultContainer');
  if (selectedIndex === quizData.correctAnswer) {
    resultText.textContent = "정답입니다!";
  } else {
    resultText.textContent = "오답입니다. 정답은 " + quizData.options[quizData.correctAnswer] + "입니다.";
  }
  resultContainer.style.display = 'block';
}
