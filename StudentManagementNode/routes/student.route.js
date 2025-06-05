import express from 'express';
import {
  getAllStudents,
  getStudentById,
  createStudent,
  updateStudent
} from '../controllers/student.controller.js';

const router = express.Router();

router.get('/', getAllStudents);
router.get('/:id', getStudentById);
router.post('/', createStudent);
router.put('/:id', updateStudent);


export default router;
