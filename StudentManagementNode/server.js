import express from 'express';
import mongoose from 'mongoose';
import dotenv from 'dotenv';
import studentRoutes from './routes/student.route.js';
import subjectRoutes from './routes/subject.route.js';
import semesterRoutes from './routes/semester.route.js';
import marksRoutes from './routes/marks.route.js';
import cgpaRoutes from './routes/cgpa.route.js';


dotenv.config();

mongoose
  .connect(process.env.MONGO, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(() => {
    console.log('MongoDb is connected');
  })
  .catch((err) => {
    console.log(err);
  });


const app = express();

app.use(express.json());

// Routes
app.use('/api/students', studentRoutes);
app.use('/api/subjects', subjectRoutes);
app.use('/api/semesters', semesterRoutes);
app.use('/api/marks', marksRoutes);
app.use('/api/cgpa', cgpaRoutes);

app.listen(3000, () => {
  console.log('Server is running on port 3000!');
});

