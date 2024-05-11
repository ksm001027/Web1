const express = require('express');
const mysql = require('mysql');
const cors = require('cors');

const app = express();
app.use(express.json());
app.use(cors());

const db = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '0000',
  database: 'project'
}

db.connect(err => { // 데이터베이스 연결
  if (err) {
    console.error('Error connecting to the database:', err); // 연결 오류 시 오류 메시지 출력
    return;
  }
  console.log('Database connection established'); // 연결 성공 시 메시지 출력
});

app.post('/api/quiz', (req, res) => {
  console.log('Received quiz data:', req.body);
  const { quizName, questions } = req.body;
// 퀴즈 생성 API
app.post('/api/quiz', (req, res) => { // POST 메서드를 사용하여 '/api/quiz' 엔드포인트에 요청이 들어왔을 때의 처리
  console.log('Received quiz data:', req.body); // 요청으로 받은 퀴즈 데이터를 콘솔에 출력
  const { quizName, questions } = req.body; // 요청으로 받은 퀴즈 이름과 문제 목록을 추출

  db.query('INSERT INTO Quiz (QuizName) VALUES (?)', [quizName], (err, quizResults) => { // 데이터베이스에 퀴즈 이름을 삽입하는 쿼리 실행
    if (err) { // 오류 처리
      console.error('Error inserting quiz:', err); // 오류 메시지 출력
      return res.status(500).send({ message: 'Failed to create quiz' }); // 오류 응답
    }
    const quizId = quizResults.insertId; // 삽입된 퀴즈의 ID 추출

    questions.forEach(question => {
      const { text, type, options, correctAnswer } = question;
    let pendingQuestions = questions.length; // 처리 대기 중인 문제 수 초기화
    questions.forEach(question => { // 각 문제에 대해 반복
      const { text, type, options, correctAnswer } = question; // 문제 데이터 추출
      db.query('INSERT INTO Quiz_Question (QuizID, Quiz_QuestionText, Quiz_QuestionType) VALUES (?, ?, ?)',
        [quizId, text, type], (err, questionResults) => { // 데이터베이스에 문제 삽입하는 쿼리 실행
          if (err) { // 오류 처리
            console.error('Error inserting question:', err); // 오류 메시지 출력
            return res.status(500).send({ message: 'Failed to add questions' }); // 오류 응답
          }
          const questionId = questionResults.insertId;
          handleOptionsAndAnswers(questionId, type, options, correctAnswer, res);
          const questionId = questionResults.insertId; // 삽입된 문제의 ID 추출
          if (type === 'objective') { // 객관식 문제인 경우
            options.forEach(option => { // 각 선택지에 대해 반복
              db.query('INSERT INTO Quiz_Option (Quiz_QuestionID, Quiz_OptionText) VALUES (?, ?)',
                [questionId, option], (err) => { // 선택지 삽입 쿼리 실행
                  if (err) { // 오류 처리
                    console.error('Error inserting option:', err); // 오류 메시지 출력
                    return;
                  }
                });
            });
          }
          db.query('INSERT INTO Quiz_Answer (Quiz_QuestionID, CorrectAnswer) VALUES (?, ?)',
            [questionId, correctAnswer], (err) => { // 정답 삽입 쿼리 실행
              if (err) { // 오류 처리
                console.error('Error inserting answer:', err); // 오류 메시지 출력
                return;
              }
              pendingQuestions--; // 처리 대기 중인 문제 수 감소
              if (pendingQuestions === 0) { // 모든 문제가 처리되었을 때
                res.send({ message: 'Quiz created successfully', quizId: quizId }); // 성공 응답
              }
            });
        });
    });
  });
});

function handleOptionsAndAnswers(questionId, type, options, correctAnswer, res) {
  if (type === 'objective') {
    options.forEach(option => {
      db.query('INSERT INTO Quiz_Option (Quiz_QuestionID, Quiz_OptionText) VALUES (?, ?)', [questionId, option], err => {
        if (err) {
          console.error('Error inserting option:', err);
          return res.status(500).send({ message: 'Error inserting options' });
        }
      });
    });
  }
  db.query('INSERT INTO Quiz_Answer (Quiz_QuestionID, CorrectAnswer) VALUES (?, ?)', [questionId, correctAnswer], err => {
    if (err) {
      console.error('Error inserting answer:', err);
      return res.status(500).send({ message: 'Error inserting answer' });
    }
  });
}

app.get('/api/quiz', (req, res) => {
  db.query('SELECT * FROM Quiz_Question', (err, results) => {
    if (err) {
      console.error('Failed to load quizzes:', err);
      return res.status(500).send({message: 'Failed to load quizzes'});
    ======
      =
// 퀴즈 데이터 불러오기 API
        app.get('/api/quiz', (req, res) => { // GET 메서드를 사용하여 '/api/quiz' 엔드포인트에 요청이 들어왔을 때의 처리
          db.query('SELECT * FROM Quiz_Question', (err, results) => { // 퀴즈 문제 데이터를 모두 불러오는 쿼리 실행
            if (err) { // 오류 처리
              console.error('Failed to load quizzes:', err); // 오류 메시지 출력
              return res.status(500).send({message: 'Failed to load quizzes'}); // 오류 응답
            >>>>>>>
              63
              c63fa1a917a034e4d661f70d3397e910c222d7
            }
            res.send(results); // 결과 응답
          });
        });

      app.get('/', (req, res) => { // 기본 루트 경로 요청에 대한 처리
        res.send('Welcome to the Quiz Server'); // 환영 메시지 응답
      });

      app.listen(3000, () => {
        console.log('Server is running on port 3000');
        const cors = require('cors'); // CORS 미들웨어 추가
        app.use(cors()); // CORS 미들웨어 사용 설정
      })
// 포트 8080에서 서버 실행
      app.listen(8080, () => {
        console.log('Server is running on port 8080');
      });
    }
  }
}
