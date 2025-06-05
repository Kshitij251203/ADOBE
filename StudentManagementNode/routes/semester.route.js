import express from 'express';
import {
  getAllSemesters,
  getSemesterById,
  createSemester,
  updateSemester
} from '../controllers/semester.controller.js';

const router = express.Router();

router.get('/', getAllSemesters);
router.get('/:id', getSemesterById);
router.post('/', createSemester);
router.put('/:id', updateSemester);


export default router;
