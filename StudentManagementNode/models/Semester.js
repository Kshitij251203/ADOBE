import mongoose from 'mongoose';

const semesterSchema = new mongoose.Schema({
  semesterNumber: { type: Number, required: true, unique: true }, // e.g. 1 to 8
  subjects: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Subject' }]
}, { timestamps: true });

const Semester = mongoose.model('Semester', semesterSchema);
export default Semester;
