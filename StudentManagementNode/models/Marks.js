import mongoose from 'mongoose';

const marksSchema = new mongoose.Schema({
  studentId: { type: mongoose.Schema.Types.ObjectId, ref: 'Student', required: true },
  subjectId: { type: mongoose.Schema.Types.ObjectId, ref: 'Subject', required: true },
  semester: { type: Number, required: true },
  marks: { type: Number, required: true },
  
}, { timestamps: true });

const Marks = mongoose.model('Marks', marksSchema);
export default Marks;
