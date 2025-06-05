import mongoose from 'mongoose';

const studentSchema = new mongoose.Schema({
    name: { type: String, required: true },
    rollNumber: { type: String, required: true, unique: true },
    email: { type: String, required: true, unique: true },
    department: { type: String, required: true },
    admissionYear: { type: Number, required: true },
    AdmissionNo: { type: String, required: true, unique: true },   
    ContactNo: { type: String, required: true, unique: true }      
}, { timestamps: true });

const Student = mongoose.model('Student', studentSchema);

export default Student;
