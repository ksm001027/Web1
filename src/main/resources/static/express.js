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
});

db.connect(err => {
  if (err) {
    console.error('Error connecting to the database:', err);
    return;
  }
  console.log('Database connection established');
});

app.post('/api/quiz', (req, res) => {
  console.log('Received quiz data:', req.body);
  const { quizName, questions } = req.body;

  db.query('INSERT INTO Quiz (QuizName) VALUES (?)', [quizName], (err, quizResults) => {
    if (err) {
      console.error('Error inserting quiz:', err);
      return res.status(500).send({ message: 'Failed to create quiz' });
    }
    const quizId = quizResults.insertId;

    questions.forEach(question => {
      const { text, type, options, correctAnswer } = question;
      db.query('INSERT INTO Quiz_Question (QuizID, Quiz_QuestionText, Quiz_QuestionType) VALUES (?, ?, ?)',
        [quizId, text, type], (err, questionResults) => {
          if (err) {
            console.error('Error inserting question:', err);
            return res.status(500).send({ message: 'Failed to add questions' });
          }
          const questionId = questionResults.insertId;
          handleOptionsAndAnswers(questionId, type, options, correctAnswer, res);
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
      return res.status(500).send({ message: 'Failed to load quizzes' });
    }
    res.send(results);
  });
});

app.get('/', (req, res) => {
  res.send('Welcome to the Quiz Server');
});

app.listen(3000, () => {
  console.log('Server is running on port 3000');
});
