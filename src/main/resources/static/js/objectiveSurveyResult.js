document.addEventListener("DOMContentLoaded", function() {
  // 데이터를 JSON 형식의 문자열로 받아와서 파싱합니다.
  const surveyDataJson = document.getElementById('surveyDataJson').textContent;
  const surveyData = JSON.parse(surveyDataJson);

  console.log(surveyData); // 디버깅을 위한 로그
  console.log(surveyData.answerCounts);

  const ctx = document.getElementById('surveyChart').getContext('2d');
  const surveyChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: surveyData.options,
      datasets: [{
        label: '응답 수',
        data: [
          surveyData.answerCounts['1'],
          surveyData.answerCounts['2'],
          surveyData.answerCounts['3'],
          surveyData.answerCounts['4']
        ],
        backgroundColor: [
          'rgba(75, 192, 192, 0.2)',
          'rgba(54, 162, 235, 0.2)',
          'rgba(255, 206, 86, 0.2)',
          'rgba(153, 102, 255, 0.2)'
        ],
        borderColor: [
          'rgba(75, 192, 192, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(255, 206, 86, 1)',
          'rgba(153, 102, 255, 1)'
        ],
        borderWidth: 1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
});
