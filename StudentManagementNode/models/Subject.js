import mongoose from 'mongoose';

const subjectSchema = new mongoose.Schema({
  subjectCode: { type: String, required: true, unique: true },  // e.g. "CS101"
  subjectName: { type: String, required: true },
  credits: { type: Number, required: true },
  department: { type: String, required: true }
}, { timestamps: true });

const Subject = mongoose.model('Subject', subjectSchema);
export default Subject;
