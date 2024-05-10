window.onload = function() {
  const params = new URLSearchParams(window.location.search);
  const quizName = params.get('quizName');
  const question = params.get('question');
  const options = JSON.parse(params.get('options'));
  const correctAnswer = params.get('correctAnswer');

  const quizInfoDiv = document.getElementById('quizInfo');
  quizInfoDiv.innerHTML = `
    <p>퀴즈 이름: ${quizName}</p>
    <p>질문: ${question}</p>
    <p>옵션:</p>
    <ul>
      ${options.map((option, index) => `<li>${index + 1}: ${option} ${index + 1 == correctAnswer ? '(정답)' : ''}</li>`).join('')}
    </ul>
  `;
};
