// express 모듈과 mysql 모듈을 가져옵니다.
const express = require('express');
const mysql = require('mysql');

// Express 애플리케이션을 생성합니다.
const app = express();
app.use(express.json()); // JSON 파싱 미들웨어 사용

// DB 연결 설정
const db = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '3192',
  database: 'kingjinpan'
});

db.connect(err => {
  if (err) {
    console.error('Error connecting to the database:', err);
    return;
  }
  console.log('Database connection established');
});

// 퀴즈 생성 API
app.post('/api/quiz', (req, res) => {
  console.log('Received quiz data:', req.body);
  const { quizName, questions } = req.body;

  db.query('INSERT INTO Quiz (QuizName) VALUES (?)', [quizName], (err, quizResults) => {
    if (err) {
      console.error('Error inserting quiz:', err);
      return res.status(500).send({ message: 'Failed to create quiz' });
    }
    const quizId = quizResults.insertId;

    let pendingQuestions = questions.length;
    questions.forEach(question => {
      const { text, type, options, correctAnswer } = question;
      db.query('INSERT INTO Quiz_Question (QuizID, Quiz_QuestionText, Quiz_QuestionType) VALUES (?, ?, ?)',
        [quizId, text, type], (err, questionResults) => {
          if (err) {
            console.error('Error inserting question:', err);
            return res.status(500).send({ message: 'Failed to add questions' });
          }
          const questionId = questionResults.insertId;
          if (type === 'objective') {
            options.forEach(option => {
              db.query('INSERT INTO Quiz_Option (Quiz_QuestionID, Quiz_OptionText) VALUES (?, ?)',
                [questionId, option], (err) => {
                  if (err) {
                    console.error('Error inserting option:', err);
                    return;
                  }
                });
            });
          }
          db.query('INSERT INTO Quiz_Answer (Quiz_QuestionID, CorrectAnswer) VALUES (?, ?)',
            [questionId, correctAnswer], (err) => {
              if (err) {
                console.error('Error inserting answer:', err);
                return;
              }
              pendingQuestions--;
              if (pendingQuestions === 0) {
                res.send({ message: 'Quiz created successfully', quizId: quizId });
              }
            });
        });
    });
  });
});

// 퀴즈 데이터 불러오기 API
app.get('/api/quiz', (req, res) => {
  db.query('SELECT * FROM Quiz_Question', (err, results) => {
    if (err) {
      console.error('Failed to load quizzes:', err);
      return res.status(500).send({ message: 'Failed to load quizzes' });
    }
    res.send(results);
  });
});

app.get('/', (req, res) => {
  res.send('Welcome to the Quiz Server');
});

const cors = require('cors');
app.use(cors());


// 포트 3000에서 서버 실행
app.listen(3000, () => {
  console.log('Server is running on port 3000');
});
