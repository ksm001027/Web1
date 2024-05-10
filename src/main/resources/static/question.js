document.addEventListener('DOMContentLoaded', function() {
  loadQuiz();
});

function loadQuiz() {
  fetch('/api/quiz')
    .then(response => response.json())
    .then(quizzes => {
      const quizContainer = document.getElementById('quizContainer');
      quizzes.forEach((quiz, index) => {
        const questionDiv = document.createElement('div');
        questionDiv.innerHTML = `<p>질문 ${index + 1}: ${quiz.question}</p>`;
        if (quiz.type === 'objective') {
          const options = quiz.options.map(option =>
            `<label><input type="radio" name="answer${index}" value="${option}">${option}</label>`
          ).join('');
          questionDiv.innerHTML += options;
        } else {
          questionDiv.innerHTML += `<input type="text" name="answer${index}">`;
        }
        quizContainer.appendChild(questionDiv);
      });
    });
}

function submitAnswers() {
  // 여기에 사용자의 답변을 수집하고 서버에 제출하는 로직 추가
}
